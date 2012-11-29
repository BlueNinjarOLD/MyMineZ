package me.kitskub.myminez.command;

import java.util.ArrayList;
import java.util.List;

import me.kitskub.myminez.CommandPerms.Perm;
import me.kitskub.myminez.GameManager;
import me.kitskub.myminez.MyMineZ;
import me.kitskub.myminez.games.MineZGame;

import org.apache.commons.lang.ArrayUtils;

import org.bukkit.command.CommandSender;

/**
 * Represents a subcommand
 *
 */
public abstract class Command extends org.bukkit.command.Command {
    protected MineZGame game;
	protected final Perm perm;
	protected final List<Command> subCommands;
    protected final String type;
	protected final Command parent;
	
	public static final String ADMIN_COMMAND = MyMineZ.CMD_ADMIN;
	public static final String USER_COMMAND = MyMineZ.CMD_USER;
	
	public Command(Perm perm, Command parent, String name) {
		super(name);
		this.perm = perm;
		this.subCommands = new ArrayList<Command>();
		parent.registerSubCommand(this);
        type = parent.type;
		this.parent = parent;
	}	
	
	public Command(Perm perm, String name, String type) {
		super(name);
		this.perm = perm;
		this.subCommands = new ArrayList<Command>();
        this.type = type;
		this.parent = null;
		if (type.equalsIgnoreCase(ADMIN_COMMAND)) {
			CommandHandler.getInstance(ADMIN_COMMAND).registerCommand(this);
		}
		else {
			CommandHandler.getInstance(USER_COMMAND).registerCommand(this);
		}
	}	
	
	public abstract void handle(CommandSender cs, String label, String[] args);

	/**
	 * Checks permission then calls the handle
	 * @param cs
	 * @param label 
	 * @param args
	 * @return
	 */
	public boolean execute(CommandSender cs, String label, String[] args) {
		if (args.length >= 1) {
			Command com = searchSubCommands(args[0]);
			if (com != null) return com.execute(cs, args[0], (String[]) ArrayUtils.removeElement(args, args[0]));
		}
		if (!MyMineZ.checkPermission(cs, perm)) return true;
		handle(cs, label, args);
		return true;
	}

	public abstract String getInfo();

	@Override
    public final String getUsage() {
        String parentUsage = "";
		if (parent != null) {
			parentUsage = parent.getPrivateUsage() + " ";
		}
		return "\\" + type + " " + parentUsage + getPrivateUsage();
    }

	protected abstract String getPrivateUsage();

	public String getUsageAndInfo() {
		return getUsage() + " - " + getInfo();
	}

	protected void registerSubCommand(Command c) {
		subCommands.add(c);
	}

	public boolean save() {
		if(game != null) {
		    GameManager.INSTANCE.saveGame(game);
		    return true;
		}
        else {
		    GameManager.INSTANCE.saveGames();
		    return false;
		}
	}
	
	public Command searchSubCommands(String com) {
		for (Command c : subCommands) {
			if (c.getName().equalsIgnoreCase(com)) {
				return c;
			}
			for (String alias : c.getAliases()) {
				if (alias.equalsIgnoreCase(com)) {
					return c;
				}
			}
		}
		return null;
	}

    public Perm getPerm() {
        return perm;
    }
}
