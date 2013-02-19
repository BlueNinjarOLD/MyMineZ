package me.kitskub.myminez.command.user;

import java.util.Collection;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.games.MineZGame;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCommand extends Command {

	public ListCommand() {
		super(Perm.LIST_COMMAND, "list", USER_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String cmd, String[] args) {
		ChatUtils.send(cs, ChatColor.GREEN, ChatUtils.getHeadLiner());
		Collection<MineZGame> games = GameManager.INSTANCE.getRawGames();
		if (games.isEmpty()) {
			ChatUtils.error(cs, "No games have been created yet.");
			return;
		}

		for (MineZGame g : games) {
			ChatUtils.send(cs, ChatColor.GOLD, "- " + g.getInfo());
		}
	}

	@Override
	public String getInfo() {
		return "list games";
	}

	@Override
	protected String getPrivateUsage() {
		return "list";
	}
    
}
