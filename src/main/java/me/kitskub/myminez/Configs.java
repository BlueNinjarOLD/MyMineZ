package me.kitskub.myminez;

public class Configs {
	
    public enum Lang {
	
        NO_PERM("You do not have permission!"),
        NOT_RUNNING("<game> is not running."),
        NOT_EXIST("<item> does not exist."),
        RUNNING("<game> is running."),
        IN_GAME("You are in <game>."),
        NOT_IN_GAME("You are not in <game>."),
        JOIN("<player> has joined <game>."),
        REJOIN("<player> has rejoined <game>."),
        QUIT("<player> has quit <game>"),
        LEAVE("<player> has left <game>"),
        KILL("<killer> killed <killed> in <game>");
	
		private String value;

		private Lang(String message) {
			this.value = message;
		}

		public String getMessage(){
			return value;
		}
        
        public String get(String setup) { //TODO include Config file
            return value;
        }
        
        public String getGlobal() { //TODO include Config file
            return value;
        }
    }
    
    public enum Config {
		
        DEFAULT_GAME("minez"),
		REMOVE_ITEMS(true),
        REQUIRE_INV_CLEAR(true),
        FORCE_SURVIVAL(true),
        DISABLE_FLY(true),
        CLEAR_INV(true),
        HIDE_PLAYERS(true),
        ALLOW_REJOIN(true),
        USE_SPAWN(true),
        DEATH_CANNON(0),
        LIVES(1),
        DEFAULT_TIME(10),
        ALLOW_TEAM(false),
        ZOMBIE_DAMAGE_MULTIPLIER(1.5);

		private Object value;

		private Config(Object message) {
			this.value = message;
		}

		public boolean getGlobalBoolean(){
			return (Boolean) value;
		}

		public int getGlobalInt(){
			return (Integer) value;
		}

		public double getGlobalDouble(){
			return (Double) value;
		}

		public String getGlobalString(){
			return (String) value;	    
		}

		public boolean getBoolean(String setup){
			return (Boolean) value;
		}

		public int getInt(String setup){
			return (Integer) value;
		}

		public double getDouble(String setup){
			return (Double) value;
		}

		public String getString(String setup){
			return (String) value;	    
		}
    }
}