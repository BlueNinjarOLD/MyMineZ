package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveSignCommand extends PlayerCommand {

	public RemoveSignCommand() {
		super(Perm.REMOVE_SIGN_COMMAND, Commands.REMOVE_HELP.getCommand(), "sign");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
		SessionListener.addSession(SessionListener.SessionType.SIGN_REMOVER, player, "");
		ChatUtils.send(player, ChatColor.GREEN, "Hit a sign to remove it. If you do not hit a sign, nothing will happen.");
	}

	@Override
	public String getInfo() {
		return "remove a sign or an info wall that contains the sign";
	}

	@Override
	protected String getPrivateUsage() {
		return "sign";
	}
	
}
