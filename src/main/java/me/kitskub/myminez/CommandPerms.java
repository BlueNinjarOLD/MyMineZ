package me.kitskub.myminez;

import me.kitskub.myminez.command.Command;
import me.kitskub.myminez.command.admin.*;
import me.kitskub.myminez.command.admin.add.*;
import me.kitskub.myminez.command.admin.remove.*;
import me.kitskub.myminez.command.admin.set.SetHelp;
import me.kitskub.myminez.command.admin.set.SetSpawnCommand;
import me.kitskub.myminez.command.user.*;

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
        AUTO_SUBSCRIBE(new Permission("myminez.user.autosubscribe"), null, "Allows a user to autosubscribe to messages"),
        // Admin Command
		ADD_HELP(new Permission("myminez.admin.command.add"), ADMIN_COMMAND),
        ADD_CHEST_COMMAND(new Permission("myminez.admin.commmand.chest"), ADMIN),
        ADD_GAME_COMMAND(new Permission("myminez.admin.command.add.game"), ADD_HELP),
		ADD_GAME_SIGN_COMMAND(new Permission("myminez.admin.commmand.gamesign"), ADMIN),
		ADD_ITEMSET_COMMAND(new Permission("myminez.admin.commmand.itemset"), ADMIN),
		ADD_JOIN_SIGN_COMMAND(new Permission("myminez.admin.commmand.joinsign"), ADMIN),
		ADD_REWARD_COMMAND(new Permission("myminez.admin.commmand.reward"), ADMIN),
        ADD_SPAWNPOINT_COMMAND(new Permission("myminez.admin.command.add.spawnpoint"), ADD_HELP),
		ADD_WORLD_COMMAND(new Permission("myminez.admin.command.add.world"), ADD_HELP),
        REMOVE_HELP(new Permission("myminez.admin.command.help"), ADMIN, "allows the player to view remove help page"),
        REMOVE_SPAWNPOINT_COMMAND(new Permission("myminez.admin.command.spawnpoint"), ADMIN),
		REMOVE_CHEST_COMMAND(new Permission("myminez.admin.command.chest"), ADMIN),
		REMOVE_GAME_COMMAND(new Permission("myminez.admin.command.game"), ADMIN),
		REMOVE_ITEMSET_COMMAND(new Permission("myminez.admin.command.itemset"), ADMIN),
		REMOVE_SIGN_COMMAND(new Permission("myminez.admin.command.sign"), ADMIN),
		SET_HELP(new Permission("myminez.admin.command.help"), ADMIN, "allows the player to view set help page"),
		SET_SPAWN_COMMAND(new Permission("myminez.admin.command.spawn"), ADMIN),
        STOP_COMMAND(new Permission("myminez.admin.command.stop"), ADMIN),
		START_COMMAND(new Permission("myminez.admin.command.start"), ADMIN),
		RELOAD_COMMAND(new Permission("myminez.admin.command.reload"), ADMIN),
		KILL_COMMAND(new Permission("myminez.admin.command.kill"), ADMIN),
		RESTOCK_COMMAND(new Permission("myminez.admin.command.restock"), ADMIN),
		THIRST_COMMAND(new Permission("myminez.admin.command.thirst"), ADMIN_COMMAND),
        // User Command
        ABOUT_COMMAND(new Permission("myminez.user.command.about"), USER_COMMAND),
		AUTO_SUBSCRIBE_COMMAND(new Permission("myminez.user.command.autosubscribe"), null, "whether a user autosubscribes to a game or not; is not inherited from *"),
		AUTO_JOIN_ALLOWED_COMMAND(new Permission("myminez.user.command.autojoinallowed"), USER_COMMAND, "whether a user can autojoin games; can also have myminez.user.command.autojoinallowed.<game>"),
		BACK_COMMAND(new Permission("myminez.user.command.back"), USER_COMMAND),
		JOIN_COMMAND(new Permission("myminez.user.command.join"), USER_COMMAND),
		KIT_COMMAND(new Permission("myminez.user.command.kit"), null, "whether a user gets all kits on start; can also add specific kits with myminez.user.command.kit.<kit>"),
		LEAVE_COMMAND(new Permission("myminez.user.command.leave"), USER_COMMAND),
		LIST_COMMAND(new Permission("myminez.user.command.list"), USER_COMMAND),
		REJOIN_COMMAND(new Permission("myminez.user.command.rejoin"), USER_COMMAND),
		SEARCH_COMMAND(new Permission("myminez.user.command.search"), USER_COMMAND),
		SPECTATE_COMMAND(new Permission("myminez.user.command.spectate"), USER_COMMAND),
		SPONSOR_COMMAND(new Permission("myminez.user.command.sponsor"), USER_COMMAND),
		SUBSCRIBE_COMMAND(new Permission("myminez.user.command.subscribe"), USER_COMMAND),
		TEAM_COMMAND(new Permission("myminez.user.command.team"), USER_COMMAND),
		VOTE_COMMAND(new Permission("myminez.user.command.vote"), USER_COMMAND),
		STAT_COMMAND(new Permission("myminez.user.command.stat"), USER_COMMAND),
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
	    ADD_HELP(new AddHelp()),
        ADD_CHEST(new AddChestCommand()),
        ADD_GAME(new AddGameCommand()),
        ADD_GAME_SIGN(new AddGameSignCommand()),
        ADD_ITEMSET(new AddItemSetCommand()),
        ADD_JOIN_SIGN(new AddJoinSignCommand()),
        ADD_SPAWNPOINT(new AddSpawnPointCommand()),
        ADD_WORLD(new AddWorldCommand()),
        REMOVE_HELP(new RemoveHelp()),
        REMOVE_CHEST(new RemoveChestCommand()),
        REMOVE_GAME(new RemoveGameCommand()),
        REMOVE_ITEMSET(new RemoveItemSetCommand()),
        REMOVE_SIGN(new RemoveSignCommand()),
        REMOVE_SPAWNPOINT(new RemoveSpawnPointCommand()),
        SET_HELP(new SetHelp()),
        SET_SPAWN(new SetSpawnCommand()),
        START(new StartCommand()),
        STOP(new StopCommand()),
        RELOAD(new ReloadCommand()),
        KILL(new KillCommand()),
        RESTOCK(new RestockCommand()),
		THIRST(new ThirstCommand()),
        ABOUT(new AboutCommand()),
        BACK(new BackCommand()),
        JOIN(new JoinCommand()),
        LEAVE(new LeaveCommand()),
        LIST(new ListCommand()),
        SPECTATE(new SpectateCommand()),
        STAT(new StatCommand()),
        SUBSCRIBE(new SubscribeCommand()),
        TEAM(new TeamCommand()),
        QUIT(new QuitCommand());

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
