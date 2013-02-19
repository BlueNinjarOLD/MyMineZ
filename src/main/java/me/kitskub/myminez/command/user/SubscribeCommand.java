package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class SubscribeCommand extends PlayerCommand {

	public SubscribeCommand() {
		super(Perm.SUBSCRIBE_COMMAND, "subscribe", USER_COMMAND);
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {		
		if (args.length > 0) {
			game = GameManager.INSTANCE.getRawGame(args[0]);
			if (game == null) {
				ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
				return;
			}
		}
		if (GameManager.INSTANCE.getSubscribedPlayers(game).contains(player.getName())) {
			GameManager.INSTANCE.removedSubscribedPlayer(player, game);
			ChatUtils.send(player, "You have been unsubscribed from those MyHungerGames messages.");
		}
		else {
			GameManager.INSTANCE.addSubscribedPlayer(player, game);
			ChatUtils.send(player, "You have been subscribed to those MyHungerGames messages.");
		}
	}

	@Override
	public String getInfo() {
		return "subscribe to game messages";
	}

	@Override
	protected String getPrivateUsage() {
		return "subscribe [game]";
	}
	
}
