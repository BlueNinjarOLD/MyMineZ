package me.kitskub.myminez.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Set;
import me.kitskub.myminez.CommandPerms.Perm;

import static me.kitskub.myminez.api.Game.GameState.*;
import me.kitskub.myminez.stats.PlayerStat;
import me.kitskub.myminez.Configs;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Logging;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.api.Game;
import me.kitskub.myminez.api.WorldNotFoundException;
import me.kitskub.myminez.api.event.PlayerKillEvent;
import me.kitskub.myminez.stats.PlayerStat.PlayerState;
import me.kitskub.myminez.stats.StatHandler;
import me.kitskub.myminez.utils.ChatUtils;
import me.kitskub.myminez.utils.GeneralUtils;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

	
public class MineZGame implements Comparable<MineZGame>, Game {
	// Per game
	private final Map<String, PlayerStat> stats;

	// Persistent
	private final Map<Location, Float> chests;
	private final List<Location> blacklistedChests;
	private final List<Location> spawnPoints;
	private final String name;
	private String setup;
	private final List<String> itemsets;
	private final Set<String> worlds;
	private Location spawn;
	private GameState state;

	
	// Temporary
	private final Map<String, Location> playerLocs;// For pausing
	private final Map<String, Location> spectators;
	private final Map<String, Boolean> spectatorFlying; // If a spectator was flying
	private final Map<String, Boolean> spectatorFlightAllowed; // If a spectator's flight was allowed
	private final Map<String, GameMode> playerGameModes; // Whether a player was in survival when game started
	private final List<String> playersFlying; // Players that were flying when they joined
	private final List<String> playersCanFly; // Players that could fly when they joined
	private final List<String> readyToPlay;

	public MineZGame(String name) {
		this(name, null);
	}

	public MineZGame(final String name, final String setup) {
		stats = new TreeMap<String, PlayerStat>();
		
		chests = new HashMap<Location, Float>();
		blacklistedChests = new ArrayList<Location>();
		spawnPoints = new ArrayList<Location>();
		this.name = name;
		this.setup = null;
		itemsets = new ArrayList<String>();
		worlds = new HashSet<String>();
		spawn = null;
		state = GameState.STOPPED;

		readyToPlay = new ArrayList<String>();
		playerLocs = new HashMap<String, Location>();
		spectators = new HashMap<String, Location>();
		spectatorFlying = new HashMap<String, Boolean>();
		spectatorFlightAllowed = new HashMap<String, Boolean>();
		playerGameModes = new HashMap<String, GameMode>();
		playersFlying = new ArrayList<String>();
		playersCanFly = new ArrayList<String>();
	}

	public void loadFrom(ConfigurationSection section) {
		spawnPoints.clear();
		chests.clear();
		itemsets.clear();
		worlds.clear();
		if (section.contains("spawn-points")) {
			ConfigurationSection spawnPointsSection = section.getConfigurationSection("spawn-points");
			for (String key : spawnPointsSection.getKeys(false)) {
				String str = spawnPointsSection.getString(key);
				Location loc = null;
				try {
					loc = GeneralUtils.parseToLoc(str);
				} catch (WorldNotFoundException ex) {
					Logging.warning(ex.getMessage());
					continue;
				} catch (NumberFormatException e) {
					Logging.debug(e.getMessage());
					continue;
				}
				spawnPoints.add(loc);
			}

		}

		if (section.contains("chests")) {
			ConfigurationSection chestsSection = section.getConfigurationSection("chests");
			for (String key : chestsSection.getKeys(false)) {
				String[] parts = chestsSection.getString(key).split(",");
				Location loc = null;
				float weight = 1f;
				try {
					loc = GeneralUtils.parseToLoc(parts[0]);
					weight = Float.parseFloat(parts[1]);
				} catch (WorldNotFoundException ex) {
					Logging.warning(ex.getMessage());
					continue;
				} catch (NumberFormatException e) {
					Logging.debug(e.getMessage());
					continue;
				}
				if (!(loc.getBlock().getState() instanceof Chest)) {
					Logging.warning("'%s' is no longer a chest.", parts[0]);
					continue;
				}
				chests.put(loc, weight);
			}

		}

		if (section.contains("blacklistedchests")) {
			ConfigurationSection chestsSection = section.getConfigurationSection("blacklistedchests");
			for (String key : chestsSection.getKeys(false)) {
				Location loc;
				try {
					loc = GeneralUtils.parseToLoc(chestsSection.getString(key));
				} catch (WorldNotFoundException ex) {
					Logging.warning(ex.getMessage());
					continue;
				} catch (NumberFormatException e) {
					Logging.debug(e.getMessage());
					continue;
				}
				if (!(loc.getBlock().getState() instanceof Chest)) {
					Logging.warning("'%s' is no longer a chest.", chestsSection.getString(key));
					continue;
				}
				blacklistedChests.add(loc);
			}

		}

                
        if(section.isList("itemsets")) {
            itemsets.addAll(section.getStringList("itemsets"));
        }

        if(section.isList("worlds")) {
            worlds.addAll(section.getStringList("worlds"));
        }
		if (section.contains("setup")) setup = section.getString("setup");
		try {
			if (section.contains("spawn")) spawn = GeneralUtils.parseToLoc(section.getString("spawn"));
		} catch (WorldNotFoundException ex) {
			Logging.warning(ex.getMessage());
		} catch (NumberFormatException e) {
			Logging.debug(e.getMessage());
		}
	}

	public void saveTo(ConfigurationSection section) {
		ConfigurationSection spawnPointsSection = section.createSection("spawn-points");
		ConfigurationSection chestsSection = section.createSection("chests");
		ConfigurationSection blacklistedchestsSection = section.createSection("blacklistedchests");
		int cntr;
		
		for (cntr = 0; cntr < spawnPoints.size(); cntr++) {
			Location loc = spawnPoints.get(cntr);
			if (loc == null) continue;
			String parsed = GeneralUtils.parseToString(loc);
			Logging.debug("Saving a spawnpoint. It's location is: " + loc + "\n" + "Parsed as: " + parsed);
			spawnPointsSection.set("spawnpoint" + (cntr + 1), parsed);
		}
		cntr = 1;
		for (Location loc : chests.keySet()) {
			chestsSection.set("chest" + cntr, GeneralUtils.parseToString(loc) + "," + chests.get(loc));
			cntr++;
		}
		cntr = 1;
		for (Location loc : blacklistedChests) {
			blacklistedchestsSection.set("chest" + cntr, GeneralUtils.parseToString(loc));
			cntr++;
		}
		section.set("itemsets", itemsets);
		if (!worlds.isEmpty()) {
			section.set("worlds", new ArrayList<String>(worlds));
		}
		section.set("setup", setup);
		section.set("spawn", GeneralUtils.parseToString(spawn));
	}
	
	public int compareTo(MineZGame game) {
		return game.name.compareToIgnoreCase(name);
	}

	public boolean addSpectator(Player player, Player spectated) {
		if (GameManager.INSTANCE.getSpectating(player) != null) {
			ChatUtils.error(player, "You cannot spectate while in a game.");
			return false;
		}
		if (state != RUNNING) {
			ChatUtils.error(player, Configs.Lang.NOT_RUNNING.get(setup).replace("<game>", name));
			return false;
		}
		spectators.put(player.getName(), player.getLocation());
		Random rand = new Random();
		Location loc = spawnPoints.get(rand.nextInt(spawnPoints.size()));
		if (spectated != null) loc = spectated.getLocation();
		player.teleport(loc);
		spectatorFlying.put(player.getName(), player.isFlying());
		spectatorFlightAllowed.put(player.getName(), player.getAllowFlight());
		player.setAllowFlight(true);
		player.setFlying(true);
		for (Player p : getPlayingPlayers()) {
			p.hidePlayer(player);
		}
		ChatUtils.send(player, "You are now spectating %s", name);
		return true;
	}

	@Override
	public boolean isSpectating(Player player) {
		return spectators.containsKey(player.getName());
	}

	public void removeSpectator(Player player) {
		if (!spectators.containsKey(player.getName())) {
			ChatUtils.error(player, "You are not spectating that game.");
			return;
		}
		player.setFlying(spectatorFlying.get(player.getName()));
		player.setAllowFlight(spectatorFlightAllowed.get(player.getName()));
		player.teleport(spectators.remove(player.getName()));
		for (Player p : getPlayingPlayers()) {
			p.showPlayer(player);
		}
	}
	
	@Override
	public boolean stopGame(CommandSender cs, boolean isFinished) {
		String result = stopGame(isFinished);
		if (result != null && cs != null) {
			ChatUtils.error(cs, result);
			return false;
		}
		return true;
	}
	
	@Override
	public String stopGame(boolean isFinished) {
		if (state == DELETED) return "That game does not exist anymore.";

		for (Player player : getPlayingPlayers()) {
			stats.get(player.getName()).setState(PlayerState.NOT_IN_GAME);
			ItemStack[] contents = player.getInventory().getContents();
			List<ItemStack> list = new ArrayList<ItemStack>();
			for (ItemStack i : contents) {
				if (i != null) list.add(i); // Remove all null elements
			}
			contents = list.toArray(new ItemStack[list.size()]);
			playerLeaving(player, false);
			for (ItemStack i : contents) player.getLocation().getWorld().dropItem(player.getLocation(), i);
			teleportPlayerToSpawn(player);
		}
		for (String stat : stats.keySet()) {
			//StatHandler.updateStat(stats.get(stat));// TODO: this might be a little slow to do it this way. Thread?
			GameManager.INSTANCE.clearGamesForPlayer(stat, this);
		}
		stats.clear();
		for (String spectatorName : spectators.keySet()) {
			Player spectator = Bukkit.getPlayer(spectatorName);
			if (spectator == null) continue;
			removeSpectator(spectator);
		}
		if (Configs.Config.REMOVE_ITEMS.getBoolean(setup)) removeItemsOnGround();
		state = STOPPED;
		clear();
		return null;
	}

	@Override
	public boolean startGame(CommandSender cs) {
		String result = startGame();
		if (result != null) {
			ChatUtils.error(cs, result);
			return false;
		}
		return true;
	}

	@Override
	public String startGame() {
		if (state == DELETED) return "Game no longer exists.";
		if (state == RUNNING) return Configs.Lang.RUNNING.get(setup).replace("<game>", name);

		fillInventories();
		for (String playerName : stats.keySet()) {
			Player p = Bukkit.getPlayer(playerName);
			if (p == null) continue;
			World world = p.getWorld();
			world.setFullTime(0L);
			p.setHealth(20);
			p.setFoodLevel(20);
			stats.get(playerName).setState(PlayerStat.PlayerState.PLAYING);
		}
		state = RUNNING;
		readyToPlay.clear();
		ChatUtils.broadcast(this, "Starting %s. Go!!", name);
		return null;
	}

	@Override
	public void addAndFillChest(Chest chest) {
		if(!chests.keySet().contains(chest.getLocation()) && !blacklistedChests.contains(chest.getLocation())) {
			GeneralUtils.fillChest(chest, 0, itemsets);
			addChest(chest.getLocation(), 1f);
		}
	}
        
	@Override
	public void fillInventories() {
	    Location prev = null;
	    for (Location loc : chests.keySet()) {
		    if (prev != null && prev.getBlock().getFace(loc.getBlock()) != null) {
			    continue;
		    }
		    if (!(loc.getBlock().getState() instanceof Chest)) {
			    continue;
		    }
		    prev = loc;
		    Chest chest = (Chest) loc.getBlock().getState();
		    GeneralUtils.fillChest(chest, chests.get(loc), itemsets);
	    }
	}

	@Override
	public synchronized boolean rejoin(Player player) {
		if (state != RUNNING) {
			ChatUtils.error(player, Configs.Lang.NOT_RUNNING.get(setup).replace("<game>", name));
			return false;
		}
		if(!playerEnteringPreCheck(player)) return false;
		if (!Configs.Config.ALLOW_REJOIN.getBoolean(setup)) {
			ChatUtils.error(player, "You are not allowed to rejoin a game.");
			return false;
		}
		if (stats.get(player.getName()).getState() == PlayerState.PLAYING){
			ChatUtils.error(player, "You can't rejoin a game while you are in it.");
			return false;
		}
		if (!stats.containsKey(player.getName()) || stats.get(player.getName()).getState() != PlayerState.NOT_PLAYING) {
			ChatUtils.error(player, Configs.Lang.NOT_IN_GAME.get(setup).replace("<game>", name));
			return false;
		}
		if (!playerEntering(player, false)) return false;
		stats.get(player.getName()).setState(PlayerState.PLAYING);
		
		String mess = Configs.Lang.REJOIN.get(setup);
		mess = mess.replace("<player>", player.getName()).replace("<game>", name);
		ChatUtils.broadcast(this, mess);
		return true;
	}

	@Override
	public synchronized boolean join(Player player) {
	    if (GameManager.INSTANCE.getSession(player) != null) {
		    ChatUtils.error(player, "You are already in a game. Leave that game before joining another.");
		    return false;
	    }
	    if (stats.containsKey(player.getName())) {
		    ChatUtils.error(player, Configs.Lang.IN_GAME.get(setup).replace("<game>", name));
		    return false;
	    }
	    if (!playerEnteringPreCheck(player)) return false;
	    if(!playerEntering(player, false)) return false;
	    stats.put(player.getName(), GameManager.INSTANCE.createStat(this, player));
		String mess = Configs.Lang.JOIN.get(setup);
	    mess = mess.replace("<player>", player.getName()).replace("<game>", name);
	    ChatUtils.broadcast(this, mess);
		stats.get(player.getName()).setState(PlayerState.PLAYING);
	    return true;
	}

	private synchronized boolean playerEnteringPreCheck(Player player) {
	    if (state == DELETED) {
		    ChatUtils.error(player, Configs.Lang.NOT_EXIST.get(setup).replace("<item>", name));
		    return false;
	    }
	    if (state == STOPPED) {
		    ChatUtils.error(player, Configs.Lang.NOT_RUNNING.get(setup).replace("<game>", name));
		    return false;
	    }

	    if (Configs.Config.REQUIRE_INV_CLEAR.getBoolean(setup)) {
		    if(!GeneralUtils.hasInventoryBeenCleared(player)) {
			    ChatUtils.error(player, "You must clear your inventory first (Be sure you're not wearing armor either).");
			    return false;
		    }
	    }
	    return true;
	}

	/**
	 * When a player enters the game. Does not handle stats.
	 * This handles the teleporting.
	 * @param player
	 * @param fromTemporary if the player leaving was temporary. Leave is not temporary.
	 * @return
	 */
	 private synchronized boolean playerEntering(Player player, boolean fromTemporary) {
	    Location loc = getRandomSpawnPoint();
	    GameManager.INSTANCE.addSubscribedPlayer(player, this);
	    GameManager.INSTANCE.addBackLocation(player);
	    player.teleport(loc, TeleportCause.PLUGIN);
	    if (Configs.Config.FORCE_SURVIVAL.getBoolean(setup)) {
		    playerGameModes.put(player.getName(), player.getGameMode());
		    player.setGameMode(GameMode.SURVIVAL);
	    }
	    if (Configs.Config.DISABLE_FLY.getBoolean(setup)) {
		    if (!MyMineZ.hasPermission(player, Perm.ADMIN_ALLOW_FLIGHT)) {
			    if (player.getAllowFlight()) {
				    playersCanFly.add(player.getName());
				    player.setAllowFlight(false);
			    }
			    if (player.isFlying()) {
				    playersFlying.add(player.getName());
				    player.setFlying(false);
			    }
			    
		    }
	    }
	    if (Configs.Config.HIDE_PLAYERS.getBoolean(setup)) player.setSneaking(true);
	    if (Configs.Config.CLEAR_INV.getBoolean(setup)) InventorySave.saveAndClearInventory(player);
//	    for (String kit : ItemConfig.getKits()) { // TODO
//		    if (HGPermission.INSTANCE.hasPermission(player, Perm.USER_KIT.getPermission().getName()) || HGPermission.INSTANCE.hasPermission(player, Perm.USER_KIT.getPermission().getName() + "." + kit)) {
//			    player.getInventory().addItem((ItemStack[]) ItemConfig.getKit(kit).toArray());
//		    }
//	    }
	    for (String string : spectators.keySet()) {
		    Player spectator = Bukkit.getPlayer(string);
		    if (spectator == null) continue;
		    player.hidePlayer(spectator);
	    }
	    return true;
	}
	
	public Location getRandomSpawnPoint() {
		Random rand = new Random();
		Location loc;
		do {
			loc = spawnPoints.get(rand.nextInt(spawnPoints.size()));
			if (loc == null) spawnPoints.remove(loc);
			
		} while (loc == null);
		return loc;
	}
	
	@Override
	public synchronized boolean leave(Player player, boolean callEvent) {		
		if (!isPlaying(player)) {
			ChatUtils.error(player, "You are not playing the game %s.", name);
			return false;
		}

		if (!Configs.Config.ALLOW_REJOIN.getBoolean(setup)) {
			stats.get(player.getName()).die();
		}
		else {
			stats.get(player.getName()).setState(PlayerState.NOT_PLAYING);
			stats.get(player.getName()).death(PlayerStat.NODODY);
		}
		InventorySave.loadGameInventory(player);
		dropInventory(player);
		playerLeaving(player, false);
		teleportPlayerToSpawn(player);
		String mess = Configs.Lang.LEAVE.get(setup);
		mess = mess.replace("<player>", player.getName()).replace("<game>", name);
		ChatUtils.broadcast(this,mess);

		return true;
	}
	
	@Override
	public synchronized boolean quit(Player player, boolean callEvent) {
	    if (!contains(player)) {
		    ChatUtils.error(player, Configs.Lang.NOT_IN_GAME.get(setup).replace("<game>", name));
		    return false;
	    }
	    boolean wasPlaying = stats.get(player.getName()).getState() == PlayerState.PLAYING;
	    if (wasPlaying) {
		    dropInventory(player);
	    }
	    if(state == RUNNING) {
		    stats.get(player.getName()).die();
	    }
	    else {
		    stats.remove(player.getName());
		    GameManager.INSTANCE.clearGamesForPlayer(player.getName(), this);
	    }
	    playerLeaving(player, false);
	    if (wasPlaying || state != RUNNING) {
		    teleportPlayerToSpawn(player);
	    }
	    
	    String mess = Configs.Lang.QUIT.get(setup);
	    mess = mess.replace("<player>", player.getName()).replace("<game>", name);
	    ChatUtils.broadcast(this, mess);

	    return true;
	}
	
	/**
	 * Used when a player is exiting.
	 * This does not handle teleporting and should be used before the teleport.
	 * @param player
	 */
	private synchronized void playerLeaving(Player player, boolean temporary) {
		for (String string : spectators.keySet()) {
		    Player spectator = Bukkit.getPlayer(string);
		    if (spectator == null) continue;
		    player.showPlayer(spectator);
		}
		InventorySave.loadInventory(player);
        GameManager.INSTANCE.removedSubscribedPlayer(player, this);
		if (playerGameModes.containsKey(player.getName())) {
			player.setGameMode(playerGameModes.remove(player.getName()));
		}
		if (Configs.Config.DISABLE_FLY.getBoolean(setup)) {
			if (!MyMineZ.hasPermission(player, Perm.ADMIN_ALLOW_FLIGHT)) {
				player.setAllowFlight(playersCanFly.remove(player.getName()));
				player.setFlying(playersFlying.remove(player.getName()));
			}
		}
		if (Configs.Config.HIDE_PLAYERS.getBoolean(setup)) player.setSneaking(false);
		readyToPlay.remove(player.getName());
//		if (!temporary) { // TODO
//			PlayerQueueHandler.addPlayer(player);
//		}
	}

	// Complete clear just to be sure
	public void clear() {
		stats.clear();
		spectators.clear();
		
		readyToPlay.clear();
		playerLocs.clear();
		spectatorFlying.clear();
		spectatorFlightAllowed.clear();
		playerGameModes.clear();
		playersCanFly.clear();
		playersFlying.clear();
	}

	@Override
	public void teleportPlayerToSpawn(Player player) {
		if (player == null) {
			return;
		}
		if (Configs.Config.USE_SPAWN.getBoolean(setup)) {
			if (spawn != null) {
				player.teleport(spawn);
				return;
			}
			else {
				ChatUtils.error(player, "There was no spawn set for %s. Teleporting to back location.", name);
			}
		}
		Location loc = GameManager.INSTANCE.getAndRemoveBackLocation(player);
		if (loc != null) {
			player.teleport(loc);
		}
		else {
			ChatUtils.error(player, "For some reason, there was no back location. Please contact an admin for help.", name);
			player.teleport(player.getWorld().getSpawnLocation());
		}
	}

	@Override
	public String getInfo() {
		return String.format("%s[%d/%d] Running: %b", name, stats.size(), spawnPoints.size(), state == RUNNING);
	}

	@Override
	public boolean contains(Player... players) {
	    if (state == DELETED) return false;
	    for (Player player : players) {
		if (!stats.containsKey(player.getName())) return false;
		PlayerState pState = stats.get(player.getName()).getState();
		if (pState == PlayerState.NOT_IN_GAME || pState == PlayerState.DEAD) return false;
	    }
	    return true;
	}
	
	@Override
	public boolean isPlaying(Player... players) {
	    for (Player player : players) {
		if (state != RUNNING || !stats.containsKey(player.getName()) 
			|| stats.get(player.getName()).getState() != PlayerState.PLAYING ){
		    return false;
		}
	    }
	    return true;
	}

	
	public void killed(final Player killer, final Player killed, PlayerDeathEvent deathEvent) {
		if (state == DELETED || state != RUNNING || stats.get(killed.getName()).getState() != PlayerState.PLAYING) return;

		deathEvent.setDeathMessage(null);
		killed.setHealth(20);
		killed.setFoodLevel(20);
		PlayerStat killedStat = stats.get(killed.getName());
		PlayerKillEvent event;
		if (killer != null) {
			PlayerStat killerStat = stats.get(killer.getName());
			killerStat.kill(killed.getName());
			String message = Configs.Lang.KILL.get(setup).replace("<killer>", killer.getName()).replace("<killed>", killed.getName()).replace("<game>", name);
			event = new PlayerKillEvent(this, killer, killed, message);
			ChatUtils.broadcast(this, message);
			killedStat.death(killer.getName());
		}
		else {
			event = new PlayerKillEvent(this, killed);
			killedStat.death(PlayerStat.NODODY);
		}
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (killedStat.getState() == PlayerState.DEAD) {
			playerLeaving(killed, false);
			final ItemStack[] armor = killed.getInventory().getArmorContents();
			final ItemStack[] inventory = killed.getInventory().getContents();
			Bukkit.getScheduler().scheduleSyncDelayedTask(MyMineZ.getInstance(), new Runnable() {
				@Override
				public void run() {
					killed.getInventory().setArmorContents(armor);
					killed.getInventory().setContents(inventory);
				}

			});
			for (ItemStack i : deathEvent.getDrops()) {
				killed.getWorld().dropItemNaturally(killed.getLocation(), i);
			}
			deathEvent.getDrops().clear();
			teleportPlayerToSpawn(killed);
            int cannon = Configs.Config.DEATH_CANNON.getInt(setup);
			if (cannon == 1 || cannon == 2) playCannonBoom();
//			if (Config.getShowDeathMessages(setup) == 1 || Config.getShowDeathMessages(setup) == 2) { // TODO
//				List<String> deathMessages = Lang.getDeathMessages(setup);
//				ChatUtils.broadcast(this, deathMessages.get((new Random()).nextInt(deathMessages.size()))
//					.replace("<killed>", killed.getDisplayName()
//					.replace("<killer>", killer.getDisplayName())
//					.replace("<game>", name)));
//			}
		}
		else {
			Location respawn = getRandomSpawnPoint();
			killed.teleport(respawn, TeleportCause.PLUGIN);
			if (Configs.Config.DEATH_CANNON.getInt(setup) == 1) playCannonBoom();
//			if (Config.getShowDeathMessages(setup) == 1) { // TODO
//				List<String> deathMessages = Lang.getDeathMessages(setup);
//				ChatUtils.broadcast(this, deathMessages.get((new Random()).nextInt(deathMessages.size()))
//					.replace("<killed>", killed.getDisplayName()
//					.replace("<killer>", killer.getDisplayName())
//					.replace("<game>", name)));
//			}
			ChatUtils.send(killed, "You have " + killedStat.getLivesLeft() + " lives left.");
		}
	}

	@Override
	public PlayerStat getPlayerStat(OfflinePlayer player) {
		return stats.get(player.getName());
	}

	@Override
	public void listStats(CommandSender cs) {
		int living = 0, dead = 0;
		List<String> players = new ArrayList<String>(stats.keySet());
		String mess = "";
		for (int cntr = 0; cntr < players.size(); cntr++) {
			PlayerStat stat = stats.get(players.get(cntr));
			Player p = stat.getPlayer();
			if (p == null) continue;
			String statName;
			if (stat.getState() == PlayerState.DEAD) {
				statName = ChatColor.RED.toString() + p.getName() + ChatColor.GRAY.toString();
				dead++;
			}
			else if (stat.getState() == PlayerState.NOT_PLAYING) {
				statName = ChatColor.YELLOW.toString() + p.getName() + ChatColor.GRAY.toString();
				dead++;
			}
			else {
				statName = ChatColor.GREEN.toString() + p.getName() + ChatColor.GRAY.toString();
				living++;
			}
			mess += String.format("%s [%d/%d]", statName, stat.getLivesLeft(), stat.getKills().size());
			if (players.size() >= cntr + 1) {
				mess += ", ";
			}
		}
		ChatUtils.send(cs, "<name>[lives/kills]");
		ChatUtils.send(cs, "Total Players: %s Total Living: %s Total Dead or Not Playing: %s", stats.size(), living, dead);
		ChatUtils.send(cs, "");
		ChatUtils.send(cs, mess);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean addChest(Location loc, float weight) {
		if (chests.keySet().contains(loc)) return false;
		blacklistedChests.remove(loc);
		chests.put(loc, weight);
		Block b = loc.getBlock();
		if (b.getRelative(BlockFace.NORTH).getState() instanceof Chest) chests.put(b.getRelative(BlockFace.NORTH).getLocation(), weight);
		else if (b.getRelative(BlockFace.SOUTH).getState() instanceof Chest) chests.put(b.getRelative(BlockFace.SOUTH).getLocation(), weight);
		else if (b.getRelative(BlockFace.EAST).getState() instanceof Chest) chests.put(b.getRelative(BlockFace.EAST).getLocation(), weight);
		else if (b.getRelative(BlockFace.WEST).getState() instanceof Chest) chests.put(b.getRelative(BlockFace.WEST).getLocation(), weight);
		return true;
	}

	@Override
	public boolean addSpawnPoint(Location loc) {
		if (loc == null) return false;
		if (spawnPoints.contains(loc)) return false;
		spawnPoints.add(loc);
		return true;
	}

	@Override
	public boolean removeChest(Location loc) {
		Block b = loc.getBlock();
		Location ad = null;
		if (b.getRelative(BlockFace.NORTH).getState() instanceof Chest) ad = b.getRelative(BlockFace.NORTH).getLocation();
		else if (b.getRelative(BlockFace.SOUTH).getState() instanceof Chest) ad = b.getRelative(BlockFace.SOUTH).getLocation();
		else if (b.getRelative(BlockFace.EAST).getState() instanceof Chest) ad = b.getRelative(BlockFace.EAST).getLocation();
		else if (b.getRelative(BlockFace.WEST).getState() instanceof Chest) ad = b.getRelative(BlockFace.WEST).getLocation();
		if (ad != null) {
			if (chests.remove(ad) == null) {
				blacklistedChests.add(ad);
			}
		}
		if (chests.remove(loc) == null) {
			blacklistedChests.add(loc);
			return false;
		}
		return true;
	}

	public void chestBroken(Location loc) {
		Block b = loc.getBlock();
		Location ad = null;
		if (b.getRelative(BlockFace.NORTH).getState() instanceof Chest) ad = b.getRelative(BlockFace.NORTH).getLocation();
		else if (b.getRelative(BlockFace.SOUTH).getState() instanceof Chest) ad = b.getRelative(BlockFace.SOUTH).getLocation();
		else if (b.getRelative(BlockFace.EAST).getState() instanceof Chest) ad = b.getRelative(BlockFace.EAST).getLocation();
		else if (b.getRelative(BlockFace.WEST).getState() instanceof Chest) ad = b.getRelative(BlockFace.WEST).getLocation();
		if (ad != null) {
			chests.remove(ad);
		}
		chests.remove(loc);
	}
	@Override
	public boolean removeSpawnPoint(Location loc) {
		if (loc == null) return false;
		Iterator<Location> iterator = spawnPoints.iterator();
		while (iterator.hasNext()) {
			if (GeneralUtils.equals(loc, iterator.next())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	private static void dropInventory(Player player) {
		for (ItemStack i : player.getInventory().getContents()) {
			if (i == null || i.getType().equals(Material.AIR)) continue;
			player.getWorld().dropItemNaturally(player.getLocation(), i);
		}
		player.getInventory().clear();
	}

	@Override
	public void setSpawn(Location newSpawn) {
		spawn = newSpawn;
	}

	@Override
	public List<String> getAllPlayers() {
		return new ArrayList<String>(stats.keySet());
	}

	@Override
	public List<PlayerStat> getStats() {
		return new ArrayList<PlayerStat>(stats.values());
	}
	
	@Override
	public Location getSpawn() {
		return spawn;
	}

	@Override
	public String getSetup() {
		return (setup == null || "".equals(setup)) ? null : setup;
	}

	@Override
	public List<String> getItemSets() {
		return itemsets;
	}

	@Override
	public void addItemSet(String name) {
		itemsets.add(name);
	}

	@Override
	public void removeItemSet(String name) {
		itemsets.remove(name);
	}
	
	@Override
	public void addWorld(World world) {
		worlds.add(world.getName());
	}
	
	@Override
	public Set<World> getWorlds() {
		if (worlds.size() <= 0) return Collections.emptySet();
		Set<World> list = new HashSet<World>();
		for (String s : worlds) {
			if (Bukkit.getWorld(s) == null) continue;
			list.add(Bukkit.getWorld(s));
		}
	return list;
	}

	@Override
	public void removeItemsOnGround() {
		Logging.debug("Aboout the check items on the ground for %s worlds.", worlds.size());
		for (String s : worlds) {
			World w = Bukkit.getWorld(s);
			if (w == null) continue;
			Logging.debug("Checking world for items.");
			int count = 0;
			for (Entity e : w.getEntities()) {
				count++;
				if (!(e instanceof Item)) continue;
				e.remove();
			}
			Logging.debug("Checked: ", count);
		}
	}
	
	@Override
	public int getSize() {
		return spawnPoints.size();
	}

	@Override
	public void playCannonBoom() {
		for (Player p : getPlayingPlayers()) {
			p.getWorld().createExplosion(p.getLocation(), 0f, false);
		}
	}
    
    @Override
	public List<Player> getPlayingPlayers() {
	    List<Player> remaining = new ArrayList<Player>();
	    for (String playerName : stats.keySet()) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) continue;
		PlayerStat stat = stats.get(playerName);
		if (stat.getState() == PlayerState.PLAYING) {
		    remaining.add(player);
		}
	    }
	    return remaining;
	}
	
	@Override
	public GameState getState() {
		return state;
	}

	public void delete() {
		clear();
		state = DELETED;
		chests.clear();
		setup = null;
		itemsets.clear();
		worlds.clear();
		spawn = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MineZGame other = (MineZGame) obj;
		return this.compareTo(other) == 0;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + (this.name != null ? this.name.toLowerCase().hashCode() : 0);
		return hash;
	}
}