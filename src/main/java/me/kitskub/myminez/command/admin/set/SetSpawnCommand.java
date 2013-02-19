package me.kitskub.myminez.command.admin.set;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.Configs.Lang;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends PlayerCommand {

    public SetSpawnCommand() {
	    super(Perm.SET_SPAWN_COMMAND, Commands.SET_HELP.getCommand(), "spawn");
    }

    @Override
    public void handlePlayer(Player player, String cmd, String[] args) {
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    if (game == null) {
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    
	    Location loc = player.getLocation();
	    game.setSpawn(loc);
	    ChatUtils.send(player, "Spawn has been set for %s.", game.getName());
    }

	@Override
	public String getInfo() {
		return "set the spawnpoint for a game";
	}

	@Override
	protected String getPrivateUsage() {
		return "spawn <game name>";
	}
    
}
