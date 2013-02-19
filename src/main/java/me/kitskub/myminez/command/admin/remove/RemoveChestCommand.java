package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.SessionListener.SessionType;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveChestCommand extends PlayerCommand {

	public RemoveChestCommand() {
		super(Perm.REMOVE_CHEST_COMMAND, Commands.REMOVE_HELP.getCommand(), "chest");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    if(game == null){
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }

	    SessionListener.addSession(SessionType.CHEST_REMOVER, player, args[0]);
	    ChatUtils.send(player, ChatColor.GREEN, "Hit a chest to remove it from %s.", game.getName());
	}

	@Override
	public String getInfo() {
		return "remove a chest if it added to the game or blacklists it if it isn't";
	}

	@Override
	protected String getPrivateUsage() {
		return "chest <game name>";
	}
}
