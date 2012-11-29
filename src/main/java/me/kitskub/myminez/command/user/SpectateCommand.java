package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpectateCommand extends PlayerCommand {

	public SpectateCommand() {
		super(Perm.SPECTATE_COMMAND, "spectate", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
		if (GameManager.INSTANCE.removeSpectator(player)) return;
		String name = (args.length < 1) ? Configs.Config.DEFAULT_GAME.getGlobalString() : args[0];
		if (name == null) {
			ChatUtils.helpCommand(player, getPrivateUsage(), MyMineZ.CMD_USER);
			return;
		}
		game = GameManager.INSTANCE.getRawGame(name);
		if (game == null) {
			ChatUtils.error(player, Configs.Lang.NOT_EXIST.getGlobal().replace("<item>", name));
			return;
		}
		Player spectated;
		if (GameManager.INSTANCE.getSpectating(player) != null) {
			ChatUtils.error(player, "You are already spectating a game.");
			return;
		}
		if (args.length < 2 || (spectated = Bukkit.getPlayer(args[1])) == null) {
			GameManager.INSTANCE.addSpectator(player, game, null);
		}
		else {
			GameManager.INSTANCE.addSpectator(player, game, spectated);
		}
	}

	@Override
	public String getInfo() {
		return "sets player to flying to spectate a game or cancels a spectation";
	}

	@Override
	protected String getPrivateUsage() {
		return "spectate [<game name> [player]]";
	}
    
}
