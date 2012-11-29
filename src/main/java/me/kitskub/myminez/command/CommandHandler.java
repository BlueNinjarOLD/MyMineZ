package me.kitskub.myminez.command;

import java.util.HashMap;
import java.util.Map;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.apache.commons.lang.ArrayUtils;


public class CommandHandler implements CommandExecutor {
	private static Map<String, CommandHandler> instances = new HashMap<String, CommandHandler>();
	public Map<String, Command> commands = new HashMap<String, Command>();

	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		handleCommand(sender, cmd, args);
		return false;
	}

	public void registerCommand(Command command) {
		commands.put(command.getName(), command);
		for (String s : command.getAliases()) {
			commands.put(s.toLowerCase(), command);
		}
	}

	private void handleCommand(CommandSender cs, org.bukkit.command.Command cmd, String[] args) {
		Command command = null;
		if (args.length == 0 || (command = commands.get(args[0].toLowerCase())) == null) {
			if (!MyMineZ.hasPermission(cs, Perm.ADMIN_HELP)) return;
			getCommand(cs, cmd);
			return;
		}
		command.execute(cs, args[0], (String[]) ArrayUtils.removeElement(args, args[0]));
		command.save();
	}

	private void getCommand(CommandSender cs, org.bukkit.command.Command cmd) {
		ChatUtils.send(cs, ChatColor.GREEN, ChatUtils.getHeadLiner());
		for (Command c : commands.values()) {
			ChatUtils.helpCommand(cs, c.getUsageAndInfo(), cmd.getLabel());
		}
	}

	public static CommandHandler getInstance(String command) {
		CommandHandler get = instances.get(command);
		if (get == null) {
			instances.put(command, new CommandHandler());
		}
		return instances.get(command);
	}

}
