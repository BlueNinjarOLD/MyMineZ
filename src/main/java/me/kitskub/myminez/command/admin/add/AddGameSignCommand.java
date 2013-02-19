package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddGameSignCommand extends PlayerCommand {

	public AddGameSignCommand() {
		super(Perm.ADD_GAME_SIGN_COMMAND, Commands.ADD_HELP.getCommand(), "gamesign");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
		if (args.length < 1) {
			ChatUtils.send(player, getUsage(), ADMIN_COMMAND);
			return;
		}
		game = GameManager.INSTANCE.getRawGame(args[0]);

		if (game == null) {
			ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
			return;
		}

		SessionListener.addSession(SessionListener.SessionType.GAME_SIGN_ADDER, player, game.getName(), "game", game.getName());
		ChatUtils.send(player, ChatColor.GREEN, "Left-click the sign to add it as a game sign.");
	}

	@Override
	public String getInfo() {
		return "add a game sign";
	}

	@Override
	protected String getPrivateUsage() {
		return "gamesign <game name>";
	}

}
