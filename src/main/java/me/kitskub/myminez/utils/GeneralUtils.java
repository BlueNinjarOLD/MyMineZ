package me.kitskub.myminez.utils;

import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.kitskub.myminez.api.WorldNotFoundException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GeneralUtils {
	public static String parseToString(Location loc) {
		if (loc == null) return "";
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		df.setGroupingUsed(false);
		return String.format("%s %s %s %s %s %s", df.format((Number) loc.getX()), df.format((Number) loc.getY()), df.format((Number) loc.getZ()), df.format((Number) loc.getYaw()), 
			df.format((Number) loc.getPitch()), loc.getWorld().getName());
	}

	public static Location parseToLoc(String str) throws NumberFormatException, WorldNotFoundException, IllegalArgumentException {
		Strings.emptyToNull(str);
		if (str == null) {
			throw new IllegalArgumentException("Location can not be null.");
		}
		String[] strs = str.split(" ");
		double x = Double.parseDouble(strs[0]);
		double y = Double.parseDouble(strs[1]);
		double z = Double.parseDouble(strs[2]);
		float yaw = Float.parseFloat(strs[3]);
		float pitch = Float.parseFloat(strs[4]);
		World world = Bukkit.getServer().getWorld(strs[5]);
		if (world == null) throw new WorldNotFoundException("Could not load world \"" + strs[5] + "\" when loading location \"" + str);
		return new Location(world, x, y, z, yaw, pitch);
	}
    
    public static boolean equals(Location loc1, Location loc2) {
		return loc1.getWorld() == loc2.getWorld()
			&& loc1.getBlockX() == loc2.getBlockX()
			&& loc1.getBlockY() == loc2.getBlockY()
			&& loc1.getBlockZ() == loc2.getBlockZ();
	}
    
    public static void fillChest(Chest chest, float weight, List<String> itemsets) {
//		if (ItemConfig.getGlobalChestLoot().isEmpty() && (itemsets == null || itemsets.isEmpty())) { // TODO
//			return;
//		}
//
//		chest.getInventory().clear();
//		Map<ItemStack, Float> itemMap = ItemConfig.getAllChestLootWithGlobal(itemsets);
//		List<ItemStack> items = new ArrayList<ItemStack>(itemMap.keySet());
//		int size = chest.getInventory().getSize();
//		final int maxItemSize = 100;
//		int numItems = items.size() >= maxItemSize ? size : (int) Math.ceil((size * Math.sqrt(items.size()))/Math.sqrt(maxItemSize));
//		int minItems = (int) Math.floor(numItems/2);
//		int itemsIn = 0;
//        Random rand = new Random();
//		for (int cntr = 0; cntr < numItems || itemsIn < minItems; cntr++) {
//			int index = 0;
//			do {
//				index = rand.nextInt(chest.getInventory().getSize());
//			} while (chest.getInventory().getItem(index) != null);
//			
//			ItemStack item = items.get(rand.nextInt(items.size()));
//			if (weight * itemMap.get(item) >= rand.nextFloat()) {
//				chest.getInventory().setItem(index, item);
//				itemsIn++;
//			}
//
//		}
	}

	public static boolean hasInventoryBeenCleared(Player player) {
		PlayerInventory inventory = player.getInventory();
		for (ItemStack item : inventory.getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				return false;
			}

		}
		for (ItemStack item : inventory.getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) {
				return false;
			}

		}
		return true;
	}

	public static void addHealthByPaper(Player player) {
		Random gen = new Random();
		int rand = gen.nextInt(4);
		player.setHealth(Math.min(player.getHealth() + rand + 2, player.getMaxHealth()));
	}

	public static void subtractItemInHand(Player player) {
		ItemStack item = player.getItemInHand();
		if (item.getAmount() == 1) {
			player.setItemInHand(null);
		}
		item.setAmount(item.getAmount() - 1);
	}
}
