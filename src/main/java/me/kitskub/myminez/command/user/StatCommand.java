package me.kitskub.myminez.command.user;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.Configs.Config;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StatCommand extends Command {

	public StatCommand() {
		super(Perm.STAT_COMMAND, "stat", USER_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String cmd, String[] args) {		
		String name = (args.length < 1) ? Config.DEFAULT_GAME.getGlobalString() : args[0];
		if (name == null) {
			ChatUtils.helpCommand(cs, getUsage(), USER_COMMAND);
			return;
		}

		game = GameManager.INSTANCE.getRawGame(name);
		if (game == null) {
			ChatUtils.error(cs, Lang.NOT_EXIST.getGlobal().replace("<item>", name));
			return;
		}
		ChatUtils.send(cs, ChatColor.GREEN, ChatUtils.getHeadLiner());
		game.listStats(cs);
	}

	@Override
	public String getInfo() {
		return "list stats for a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "stat [game name]";
	}
    
}
