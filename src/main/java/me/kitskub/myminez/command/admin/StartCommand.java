package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs.Config;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class StartCommand extends Command {

	public StartCommand() {
		super(Perm.START_COMMAND, "start", ADMIN_COMMAND);
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
			ChatUtils.error(cs, Lang.NOT_EXIST.getGlobal().replace("<item>", name));
			return;
		}

		int seconds;

		if (args.length == 2) {
			try {
				seconds = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				ChatUtils.error(cs, "'%s' is not an integer.", args[1]);
				return;
			}
		}

		else {
			seconds = Config.DEFAULT_TIME.getInt(game.getSetup());
		}
		if (!game.startGame(cs)) {
			ChatUtils.error(cs, "Failed to start %s.", game.getName());
		}
	}

	@Override
	public String getInfo() {
		return "manually start a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "start [<game name> [seconds]]";
	}
    
}
