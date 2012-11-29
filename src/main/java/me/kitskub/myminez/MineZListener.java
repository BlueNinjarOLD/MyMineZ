package me.kitskub.myminez;

import java.util.HashMap;
import java.util.Map;
import me.kitskub.myminez.games.MineZGame;
import me.kitskub.myminez.utils.GeneralUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MineZListener implements Listener {
    public Map<String, String> bandage = new HashMap<String, String>();
    
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event){
        if (event.isCancelled()) return;
        Entity entity = event.getEntity();
        if (!inGameWorld(event.getEntity())) return;
        EntityType creatureType = event.getEntityType();
        if (creatureType == EntityType.ZOMBIE){
			ZombieMaker.mineZombie((Zombie) entity);
        } else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event) {
		if (event.getEntity() instanceof Zombie) {
            if (!inGameWorld(event.getEntity())) return;
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHealthRegenerate(EntityRegainHealthEvent event) {
		EntityType entityType = event.getEntityType();
		if (entityType.equals(EntityType.PLAYER)) {
            if (GameManager.INSTANCE.getSession((Player) event.getEntity()) == null) return;
			if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
        if (GameManager.INSTANCE.getSession(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
        if (GameManager.INSTANCE.getSession(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onEat(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
            if (GameManager.INSTANCE.getSession((Player) event.getEntity()) == null) return; 
			Player p = (Player) event.getEntity();
            if (GameManager.INSTANCE.getSession(p) == null) return; 
			int old = p.getFoodLevel();
			int newLevel = event.getFoodLevel();
			if (newLevel - old > 0) {
				p.setHealth(Math.min(p.getHealth() + 1, p.getMaxHealth()));
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player == false || GameManager.INSTANCE.getSession((Player) event.getEntity()) == null) return; 
		if (event.getDamager() instanceof Zombie) {
            event.setDamage((int) (Configs.Config.ZOMBIE_DAMAGE_MULTIPLIER.getGlobalDouble() * event.getDamage()));
		} else if (event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			if (p.getItemInHand().getType().equals(Material.PAPER)) {
				event.setCancelled(true);
				bandage.put(p.getName(), ((Player) event.getEntity()).getName());
				GeneralUtils.subtractItemInHand(p);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
        if (GameManager.INSTANCE.getSession(event.getPlayer()) == null) return; 
		ItemStack item = event.getItem();
		if (item == null || (!event.getAction().equals(Action.RIGHT_CLICK_AIR )&& !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		if (item.getType().equals(Material.PAPER) && event.getPlayer().getHealth() != 20) {
			GeneralUtils.subtractItemInHand(event.getPlayer());
			GeneralUtils.addHealthByPaper(event.getPlayer());
		}
		if (event.hasBlock() && !Material.WORKBENCH.equals(event.getClickedBlock().getType())) event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player == false || GameManager.INSTANCE.getSession((Player) event.getRightClicked()) == null) return;
		ItemStack item = event.getPlayer().getItemInHand();
		if (item == null) return;
		Player clicked = (Player) event.getRightClicked();
		if (item.getType().equals(Material.SHEARS)) {
			if (bandage.containsKey(event.getPlayer().getName()) && bandage.get(event.getPlayer().getName()).equals(((Player) event.getRightClicked()).getName())) {
				GeneralUtils.addHealthByPaper(clicked);
			} else {
				event.getPlayer().sendMessage("Health: " + clicked.getHealth());
			}
		}
		
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
        if (!inGameWorld(event.getEntity())) return;
		event.setDroppedExp(0);
	}

    public boolean inGameWorld(Entity entity) {
        World world = entity.getWorld();
        for (MineZGame game : GameManager.INSTANCE.getRawGames()) {
            for (World w : game.getWorlds()) {
                if (world.equals(w)) return true;
            }
        }
        return false;
    }
}
