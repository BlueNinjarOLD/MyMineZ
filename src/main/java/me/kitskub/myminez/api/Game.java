package me.kitskub.myminez.api;

import me.kitskub.myminez.stats.PlayerStat;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Game {
	public boolean isSpectating(Player player);

	public boolean stopGame(CommandSender notifier, boolean isFinished);
	
	public String stopGame(boolean isFinished);
	
	/**
	 * Starts this game with the default time if immediate is true. Otherwise, starts the game immediately.
	 * 
	 * @param notifier who to notify
	 * @return
	 */	
	public boolean startGame(CommandSender notifier);
		
	/**
	 * Starts the game
	 * 
	 * @return Null if game or countdown was successfully started. Otherwise, error message.
	 */
	public String startGame();

	public void addAndFillChest(Chest chest);
        
	public void fillInventories();
	
	/**
	 * Only used for players that have left the game, but not quitted. Only valid while game is running
	 * 
	 * @param player
	 * @return true if successful
	 */
	public boolean rejoin(Player player);

	public boolean join(Player player);
	
	public boolean leave(Player player, boolean callEvent);
	
	public boolean quit(Player player, boolean callEvent);
	
	/**
	 * Will be canceled if player is playing and teleporting is not allowed which should not ever happen
	 * @param player
	 */
	public void teleportPlayerToSpawn(Player player);
	
	public String getInfo();
	
	/**
	 * Checks if players are in the game and have lives, regardless is game is running and if they are playing.
	 * @param players players to check
	 * @return
	 */
	public boolean contains(Player... players);
	
	/**
	 * 
	 * @param players players to check
	 * @return true if players are in the game, have lives, and are playing
	 */
	public boolean isPlaying(Player... players);
	
	public PlayerStat getPlayerStat(OfflinePlayer player);
	
	public void listStats(CommandSender notifier);
	
	public String getName();

	public boolean addChest(Location loc, float weight);

	public boolean addSpawnPoint(Location loc);

	public boolean removeChest(Location loc);

	public boolean removeSpawnPoint(Location loc);
	
	public void setSpawn(Location newSpawn);

	public List<String> getAllPlayers();

	public List<PlayerStat> getStats();
	
	public Location getSpawn();

	public String getSetup();

	public List<String> getItemSets();

	public void addItemSet(String name);

	public void removeItemSet(String name);
		
	public void addWorld(World world);
	
	public Set<World> getWorlds();
	
	public void removeItemsOnGround();
	
	public int getSize();

	public void playCannonBoom();
    
    public List<Player> getPlayingPlayers();
	
	public GameState getState();
	
	public enum GameState {
		DELETED,
		STOPPED,
		RUNNING;		
	}
}
