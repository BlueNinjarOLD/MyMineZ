package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveGameCommand extends PlayerCommand {

	public RemoveGameCommand() {
		super(Perm.REMOVE_GAME_COMMAND, Commands.REMOVE_HELP.getCommand(), "game");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    if(game == null){
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    
	    GameManager.INSTANCE.removeGame(args[0]);
	    ChatUtils.send(player, ChatColor.GREEN, "%s has been removed.", args[0]);
	}

	@Override
	public String getInfo() {
		return "remove a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "game <game name>";
	}
	
}
