package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class AddHelp extends PlayerCommand {

	public AddHelp() {
		super(Perm.ADD_HELP, "add", ADMIN_COMMAND);
	}

	@Override
	public void handlePlayer(Player cs, String label, String[] args) {
		for (Command c : subCommands) {
			ChatUtils.helpCommand(cs, c.getUsageAndInfo(), "hga");
		}
	}

	@Override
	public String getInfo() {
		return "add items";
	}

	@Override
	protected String getPrivateUsage() {
		return "add";
	}
	
}
