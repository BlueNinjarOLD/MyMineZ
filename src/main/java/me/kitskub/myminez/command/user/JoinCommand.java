package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class JoinCommand extends PlayerCommand {

	public JoinCommand() {
		super(Perm.JOIN_COMMAND, "join", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
		String name = (args.length < 1) ? Configs.Config.DEFAULT_GAME.getGlobalString() : args[0];
		if (name == null) {
			ChatUtils.helpCommand(player, getPrivateUsage(), MyMineZ.CMD_USER);
			return;
		}

		game = GameManager.INSTANCE.getRawGame(name);
		if (game == null) {
			ChatUtils.error(player, Configs.Lang.NOT_EXIST.name().replace("<item>", name));
			return;
		}

		game.join(player);
	}

	@Override
	public String getInfo() {
		return "join a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "join [game name]";
	}
}
