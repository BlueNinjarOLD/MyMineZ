package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackCommand extends PlayerCommand {

	public BackCommand() {
		super(Perm.BACK_COMMAND, "back", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {		
		if (GameManager.INSTANCE.getSession(player) != null) {
			ChatUtils.send(player, "You cannot use that command while you are in-game.");
			return;
		}
		Location loc = GameManager.INSTANCE.getAndRemoveBackLocation(player);
		if (loc != null) {
			ChatUtils.send(player, "Teleporting you to your back location.");
			player.teleport(loc);
		}
		else {
			ChatUtils.error(player, "For some reason, there was no back location set. Did you already teleport back?");
		}
	}

	@Override
	public String getInfo() {
		return "returns a player to where they were before they joined";
	}

	@Override
	protected String getPrivateUsage() {
		return "back";
	}
	
}
