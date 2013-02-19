package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class RemoveItemSetCommand extends PlayerCommand {

	public RemoveItemSetCommand() {
		super(Perm.REMOVE_ITEMSET_COMMAND, Commands.REMOVE_HELP.getCommand(), "itemset");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {	    	    
	    if(args.length < 2){
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    
	    if(game == null){
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    game.removeItemSet(args[1]);
	}

	@Override
	public String getInfo() {
		return "remove a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "itemset <game name> <itemset name>";
	}
	
}
