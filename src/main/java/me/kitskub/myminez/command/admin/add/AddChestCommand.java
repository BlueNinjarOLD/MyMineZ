package me.kitskub.myminez.command.admin.add;

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

public class AddChestCommand extends PlayerCommand {

	public AddChestCommand() {
		super(Perm.ADD_CHEST_COMMAND, Commands.ADD_HELP.getCommand(), "chest");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
	    
	    if (args.length < 1) {
		    ChatUtils.helpCommand(player, getUsage(), ADMIN_COMMAND);
		    return;
	    }
	    game = GameManager.INSTANCE.getRawGame(args[0]);
	    
	    if (game == null) {
		    ChatUtils.error(player, Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
		    return;
	    }
	    ChatUtils.send(player, ChatColor.GREEN, "Hit a chest to add it to %s.", game.getName());
	    if (args.length == 2){
		    try {
			    float weight = Float.valueOf(args[1]);
			    SessionListener.addSession(SessionType.CHEST_ADDER, player, args[0], "weight", weight);
			    return;
		    } catch (NumberFormatException numberFormatException) {}
	    }
	    SessionListener.addSession(SessionType.CHEST_ADDER, player, args[0]);
	}

	@Override
	public String getInfo() {
		return "add a chest with optional weight";
	}

	@Override
	protected String getPrivateUsage() {
		return "chest <game name> [weight]";
	}
	
}
