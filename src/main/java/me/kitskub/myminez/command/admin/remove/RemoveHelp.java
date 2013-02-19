package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class RemoveHelp extends PlayerCommand {

	public RemoveHelp() {
		super(Perm.REMOVE_HELP, "remove", ADMIN_COMMAND);
	}

	@Override
	public void handlePlayer(Player cs, String label, String[] args) {
		for (Command c : subCommands) {
			ChatUtils.helpCommand(cs, c.getUsageAndInfo(), "hga");
		}
	}

	@Override
	public String getInfo() {
		return "remove items";
	}

	@Override
	protected String getPrivateUsage() {
		return "remove";
	}
	
}
