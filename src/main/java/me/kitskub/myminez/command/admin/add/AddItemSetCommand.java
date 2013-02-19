package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.entity.Player;

public class AddItemSetCommand extends PlayerCommand {

	public AddItemSetCommand() {
		super(Perm.ADD_ITEMSET_COMMAND, Commands.ADD_HELP.getCommand(), "itemset");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {	    
	    if(args.length < 2){
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    
	    if (game == null) {
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    game.addItemSet(args[1]);
	    ChatUtils.send(player, "Itemset created!");
	}

	@Override
	public String getInfo() {
		return "add an itemset";
	}

	@Override
	protected String getPrivateUsage() {
		return "itemset <game name> <itemset name>";
	}
	
}
