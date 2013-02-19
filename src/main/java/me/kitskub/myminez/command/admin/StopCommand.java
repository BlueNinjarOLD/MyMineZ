package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Config;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class StopCommand extends Command {

	public StopCommand() {
		super(Perm.STOP_COMMAND, "stop", ADMIN_COMMAND);
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
		game.stopGame(cs, false);
	}

	@Override
	public String getInfo() {
		return "manually stop a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "stop [game name]";
	}
    
}
