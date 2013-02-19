package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCommand extends Command {

	public KillCommand() {
		super(Perm.KILL_COMMAND, "kill", ADMIN_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String label, String[] args) {
		if (args.length < 1) {
			ChatUtils.helpCommand(cs, getUsage(), ADMIN_COMMAND);
			return;
		}

		Player kill = Bukkit.getServer().getPlayer(args[0]);
		if (kill == null) {
		    ChatUtils.error(cs, "%s is not online.", args[0]);
		    return;
		}
		game = GameManager.INSTANCE.getRawSession(kill);
		if (game == null) {
		    ChatUtils.error(cs, "%s is currently not in a game.", kill.getName());
		    return;
		}
		ChatUtils.broadcast(game, "%s has been killed by an admin.", kill.getName());
		kill.setHealth(0);
	}

	@Override
	public String getInfo() {
		return "kills a player in a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "kill <player>";
	}
	
}
