package me.kitskub.myminez.command.admin.add;

import me.kitskub.myminez.CommandPerms.Commands;
import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.Configs;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.command.PlayerCommand;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AddWorldCommand extends PlayerCommand {

	public AddWorldCommand() {
		super(Perm.ADD_WORLD_COMMAND, Commands.ADMIN_ADD_HELP.getCommand(), "world");
	}

	@Override
	public void handlePlayer(Player player, String label, String[] args) {
		if(args.length < 1){
			ChatUtils.helpCommand(player, getUsage(), MyMineZ.CMD_ADMIN);
			return;
		}
		game = GameManager.INSTANCE.getRawGame(args[0]);

		if (game == null) {
			ChatUtils.error(player, Configs.Lang.NOT_EXIST.getGlobal().replace("<item>", args[0]));
			return;
		}
		if (args.length == 1) {
			game.addWorld(player.getWorld());
		}
		else {
			World world = Bukkit.getWorld(args[1]);
			if (world == null) {
				ChatUtils.error(player, Configs.Lang.NOT_EXIST.getGlobal().replace("<item>", args[1]));
				return;
			}
			else {
				game.addWorld(player.getWorld());
			}
		}
		ChatUtils.send(player, "World added!");
	}

	@Override
	public String getInfo() {
		return "adds the world specified or you are currently in to the game";
	}

	@Override
	protected String getPrivateUsage() {
		return "world <game name> [world]";
	}
}
