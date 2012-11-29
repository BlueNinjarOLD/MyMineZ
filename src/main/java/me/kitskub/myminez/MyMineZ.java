package me.kitskub.myminez;

import java.util.HashMap;
import java.util.Map;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.CommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class MyMineZ extends JavaPlugin {
	public Map<String, String> bandage = new HashMap<String, String>();
	private static MyMineZ instance;
	public static String CMD_ADMIN = "mza";
	public static String CMD_USER = "mz";

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(new MineZListener(), this);
		registerCommands();
		/*try{
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
 
            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);
 
            a.invoke(a, Zombie.class, "Zombie", 54);
        }catch (Exception e){
            e.printStackTrace();
            this.setEnabled(false);
        }
		*/
	}
	
	private void registerCommands() {
		getCommand(CMD_USER).setExecutor(CommandHandler.getInstance(CMD_USER));
		getCommand(CMD_ADMIN).setExecutor(CommandHandler.getInstance(CMD_ADMIN));
		for (Perm p : Perm.values()) {
			Permission permission = p.getPermission();
			if (p.getParent() != null) {
				permission.addParent(p.getParent().getPermission(), true);
			}
		}
		Commands.init();
	}
	
	public static MyMineZ getInstance() {
		return instance;
	}

	public static boolean hasPermission(CommandSender cs, Perm permission) {
		if (permission == null) return true;
		if (cs.hasPermission(permission.getPermission().getName())) {
			return true;
		}
		if (permission.getParent() != null) {
			return hasPermission(cs, permission.getParent());
		}
		return false;
	}
		
	public static boolean checkPermission(CommandSender cs, Perm perm) {
		if (!MyMineZ.hasPermission(cs, perm)) {
			cs.sendMessage(ChatColor.RED + Lang.NO_PERM.getGlobal());
			return false;
		}
		return true;
	}
}
