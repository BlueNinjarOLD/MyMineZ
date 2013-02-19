package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Config;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.api.Game.GameState;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class RestockCommand extends Command {

	public RestockCommand() {
		super(Perm.RESTOCK_COMMAND, "restock", ADMIN_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String label, String[] args) {
	    
	    String name = (args.length < 1) ? Config.DEFAULT_GAME.getGlobalString() : args[0];
	    if (name == null) {
		    ChatUtils.helpCommand(cs, getUsage(), ADMIN_COMMAND);
		    return;
	    }

	    game = GameManager.INSTANCE.getRawGame(name);
	    if (game == null) {
		    ChatUtils.error(cs, "%s does not exist.", name);
		    return;
	    }
	    if (game.getState() != GameState.RUNNING) {
		    ChatUtils.error(cs, Lang.NOT_RUNNING.get(game.getSetup()).replace("<game>", game.getName()));
		    return;
	    }
	    game.fillInventories();
	}

	@Override
	public String getInfo() {
		return "restock all a game's chests";
	}

	@Override
	protected String getPrivateUsage() {
		return "restock [game name]";
	}
    
}
