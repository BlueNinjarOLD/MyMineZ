package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.SessionListener.SessionType;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddSpawnPointCommand extends PlayerCommand {

	public AddSpawnPointCommand() {
		super(Perm.ADD_SPAWNPOINT_COMMAND, Commands.ADMIN_ADD_HELP.getCommand(), "spawnpoint");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {
	    if (args.length < 1) {
		    ChatUtils.send(player, getUsage(), MyMineZ.CMD_ADMIN);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);

	    if (game == null) {
		    ChatUtils.error(player, Configs.Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    
	    SessionListener.addSession(SessionType.SPAWN_ADDER, player, game.getName());
	    ChatUtils.send(player, ChatColor.GREEN, "Left-click blocks to add them as spawn points for %s. Right-click to finish.", game.getName());
	}

	@Override
	public String getInfo() {
		return "add a spawnpoint";
	}

	@Override
	protected String getPrivateUsage() {
		return "spawnpoint <game name>";
	}
	
}
