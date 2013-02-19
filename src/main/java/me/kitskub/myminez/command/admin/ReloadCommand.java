package me.kitskub.myminez.command.admin;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.utils.ChatUtils;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends Command {

	public ReloadCommand() {
		super(Perm.RELOAD_COMMAND, "reload", ADMIN_COMMAND);
	}

	@Override
	public void handle(CommandSender cs, String label, String[] args) {
		MyMineZ.reload();
		ChatUtils.send(cs, ChatUtils.getPrefix() + "Reloaded %s", MyMineZ.getInstance().getDescription().getVersion());
	}

	@Override
	public String getInfo() {
		return "reload MyMineZ";
	}

	@Override
	protected String getPrivateUsage() {
		return "reload";
	}
    
}
