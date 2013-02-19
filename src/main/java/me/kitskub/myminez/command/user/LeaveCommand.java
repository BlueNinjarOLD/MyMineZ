package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class LeaveCommand extends PlayerCommand {

	public LeaveCommand() {
		super(Perm.LEAVE_COMMAND, "leave", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
		game = GameManager.INSTANCE.getRawPlayingSession(player);
		if (game == null) {
			ChatUtils.error(player, "You are currently not playing a game.");
			return;
		}

		game.leave(player, true);
	}

	@Override
	public String getInfo() {
		return "leave current game temporarily (if enabled)";
	}

	@Override
	protected String getPrivateUsage() {
		return "leave";
	}
}
