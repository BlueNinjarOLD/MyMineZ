package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddJoinSignCommand extends PlayerCommand {

	public AddJoinSignCommand() {
		super(Perm.ADD_JOIN_SIGN_COMMAND, Commands.ADD_HELP.getCommand(), "joinsign");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
		if (args.length < 1) {
			ChatUtils.send(player, getUsage(), ADMIN_COMMAND);
			return;
		}
		game = GameManager.INSTANCE.getRawGame(args[0]);

		if (game == null) {
			ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
			return;
		}

		SessionListener.addSession(SessionListener.SessionType.JOIN_SIGN_ADDER, player, game.getName(), "game", game.getName());
		ChatUtils.send(player, ChatColor.GREEN, "Left-click the sign to add it as a join sign.");
	}

	@Override
	public String getInfo() {
		return "add a join sign";
	}

	@Override
	protected String getPrivateUsage() {
		return "joinsign <game name>";
	}

}
