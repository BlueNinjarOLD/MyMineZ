package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.ThirstHandler;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ThirstCommand extends Command {

	public ThirstCommand() {
		super(Perm.THIRST_COMMAND, "thirst", ADMIN_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String label, String[] args) {
	    if (args.length < 2) {
		    ChatUtils.helpCommand(cs, getUsage(), ADMIN_COMMAND);
		    return;
	    }
		String name = args[0];
		Player p = Bukkit.getPlayer(name);
	    if (p == null) {
		    ChatUtils.error(cs, "%s is not an online player.", name);
		    return;
	    }
		try {
			int thirst = Integer.parseInt(args[1]);
			ThirstHandler.setThirst(p, thirst);
		} catch(NumberFormatException ex) {
			ChatUtils.error(cs, args[1] + " is not a valid number");
		} catch(IllegalArgumentException ex) {
			ChatUtils.error(cs, ex.getMessage());
		}
	}

	@Override
	public String getInfo() {
		return "modify a player's thirst";
	}

	@Override
	public String getPrivateUsage() {
		return "thirst <player> <thirst>";
	}

}
