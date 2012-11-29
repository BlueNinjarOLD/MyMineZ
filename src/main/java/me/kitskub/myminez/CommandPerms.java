package me.kitskub.myminez;

import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.command.admin.ThirstCommand;
import me.kitskub.myminez.command.admin.add.AddHelp;
import me.kitskub.myminez.command.admin.add.AddSpawnPointCommand;
import me.kitskub.myminez.command.admin.add.AddWorldCommand;
import me.kitskub.myminez.command.user.AboutCommand;
import me.kitskub.myminez.command.user.JoinCommand;
import me.kitskub.myminez.command.user.QuitCommand;
import me.kitskub.myminez.command.user.SpectateCommand;
import org.bukkit.permissions.Permission;

public class CommandPerms {

	// Commands
	// - Admin - myminez.admin.command.<command>
	// - User  - myminez.user.command.<command>
	// Other
	// - Admin - myminez.admin.<other>
	// - User  - myminez.user.<other>
    public enum Perm {
        // Global-type
		ALL(new Permission("myminez"), null),
		ADMIN(new Permission("myminez.admin"), ALL),
        ADMIN_COMMAND(new Permission("myminez.admin.command"), ADMIN),
		USER(new Permission("myminez.admin"), ALL),
        USER_COMMAND(new Permission("myminez.user.command"), USER),
        // Admin Other
		ADMIN_HELP(new Permission("myminez.admin.help"), ADMIN),
        ADMIN_ALLOW_FLIGHT(new Permission("myminez.admin.allowflight"), ADMIN, "Allows an admin to fly in game"),
        // User Other
		USER_HELP(new Permission("myminez.user.help"), USER),
        USER_AUTO_SUBSCRIBE(new Permission("myminez.user.autosubscribe"), null, "Allows a user to autosubscribe to messages"),
        // Admin Command
		ADD_HELP(new Permission("myminez.admin.command.add"), ADMIN_COMMAND),
		ADD_GAME_COMMAND(new Permission("myminez.admin.command.add.game"), ADD_HELP),
		ADD_SPAWNPOINT_COMMAND(new Permission("myminez.admin.command.add.spawnpoint"), ADD_HELP),
		ADD_WORLD_COMMAND(new Permission("myminez.admin.command.add.world"), ADD_HELP),
		THIRST_COMMAND(new Permission("myminez.admin.command.thirst"), ADMIN_COMMAND),
        // User Command
        ABOUT_COMMAND(new Permission("myminez.user.command.about"), USER_COMMAND),
        JOIN_COMMAND(new Permission("myminez.user.command.join"), USER_COMMAND),
        SPECTATE_COMMAND(new Permission("myminez.user.command.spectate"), USER_COMMAND),
        QUIT_COMMAND(new Permission("myminez.user.command.quit"), USER_COMMAND);


		private Permission value;
		private Perm parent;
        private String info;

		private Perm(Permission permission, Perm parent) {
			this.value = permission;
			this.parent = parent;
		}
	
        private Perm(Permission permission, Perm parent, String info) {
            this.value = permission;
            this.parent = parent;
            this.info = info;
        }
		public Permission getPermission(){
			return value;
		}

		public Perm getParent() {
			return parent;
		}
        
        public String getInfo() {
            return info;
        }
    }
    
    public enum Commands {	
	    
		ADMIN_ADD_HELP(new AddHelp()),
		ADMIN_ADD_SPAWNPOINT(new AddSpawnPointCommand()),
		ADMIN_ADD_WORLD(new AddWorldCommand()),
		ADMIN_THIRST(new ThirstCommand()),
        USER_ABOUT(new AboutCommand()),
        USER_JOIN(new JoinCommand()),
        USER_SPECTATE(new SpectateCommand()),
        USER_QUIT(new QuitCommand());

		private Command command;

		private Commands(Command command) {
			this.command = command;
		}

		public 	Command getCommand() {
			return command;
		}

		public static void init() {} // Just so the class gets loaded
    }
}
