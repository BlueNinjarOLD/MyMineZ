package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddGameCommand extends PlayerCommand {

	public AddGameCommand() {
		super(Perm.ADD_GAME_COMMAND, Commands.ADD_HELP.getCommand(), "game");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), MyMineZ.CMD_ADMIN);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);

	    if (game != null) {
		    ChatUtils.error(player, "%s already exists.", args[0]);
		    return;
	    }
	    if(args.length == 1){
		    GameManager.INSTANCE.createGame(args[0]);
	    }
	    else{
		    GameManager.INSTANCE.createGame(args[0], args[1]);
	    }
	    ChatUtils.send(player, ChatColor.GREEN, "%s has been created. To add spawn points, simply", args[0]);
	    ChatUtils.send(player, ChatColor.GREEN, "type the command 'add spawnpoint <game name>'", MyMineZ.CMD_ADMIN);
	}

	@Override
	public String getInfo() {
		return "add a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "game <game name> [setup]";
	}
	
}
