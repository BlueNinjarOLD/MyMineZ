package me.kitskub.myminez.command.admin.remove;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.SessionListener;
import me.kitskub.myminez.SessionListener.SessionType;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveSpawnPointCommand extends PlayerCommand {

	public RemoveSpawnPointCommand() {
		super(Perm.REMOVE_SPAWNPOINT_COMMAND, Commands.REMOVE_HELP.getCommand(), "spawnpoint");
	}

	@Override
	public void handlePlayer(Player player, String cmd, String[] args) {	    
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    if(game == null) {
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    
	    SessionListener.addSession(SessionType.SPAWN_REMOVER, player, game.getName());
	    ChatUtils.send(player, ChatColor.GREEN, "Hit a spawn point to remove it from %s.", game.getName());
	}

	@Override
	public String getInfo() {
		return "remove a spawnpoint";
	}

	@Override
	protected String getPrivateUsage() {
		return "spawnpoint <game name>";
	}
	
}
