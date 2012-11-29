package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class QuitCommand extends PlayerCommand {

	public QuitCommand() {
		super(Perm.QUIT_COMMAND, "quit", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
		game = GameManager.INSTANCE.getRawSession(player);
		if (game == null) {
			ChatUtils.error(player, "You are currently not in a game.");
			return;
		}

		game.quit(player, true);
	}

	@Override
	public String getInfo() {
		return "quit the current game indefinitely";
	}

	@Override
	protected String getPrivateUsage() {
		return "quit";
	}
    
}
