package me.kitskub.myminez.command.admin.set;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.command.CommandSender;

public class SetHelp extends Command {

	public SetHelp() {
		super(Perm.SET_HELP, "set", ADMIN_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String label, String[] args) {
		for (Command c : subCommands) {
			ChatUtils.helpCommand(cs, c.getUsageAndInfo(), "hga");
		}
	}

	@Override
	public String getInfo() {
		return "set items";
	}

	@Override
	protected String getPrivateUsage() {
		return "set";
	}
	
}
