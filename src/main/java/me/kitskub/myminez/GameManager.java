package me.kitskub.myminez;

import com.google.common.base.Strings;


import java.lang.ref.WeakReference;
import java.util.*;

import me.kitskub.myminez.api.Game;
import me.kitskub.myminez.games.MineZGame;
import me.kitskub.myminez.stats.PlayerStat;
import me.kitskub.myminez.utils.EquatableWeakReference;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class GameManager extends me.kitskub.myminez.api.GameManager {
	public static final GameManager INSTANCE = new GameManager();
	private static Map<String, Map<EquatableWeakReference<MineZGame>, PlayerStat>> stats = new HashMap<String, Map<EquatableWeakReference<MineZGame>, PlayerStat>>();
	private static final Set<MineZGame> games = new TreeSet<MineZGame>();
	private static final Map<String, EquatableWeakReference<MineZGame>> spectators = new HashMap<String, EquatableWeakReference<MineZGame>>(); // <player, game>
	private static final Set<String> globalSubscribedPlayers = new HashSet<String>();
	private static final Map<EquatableWeakReference<MineZGame>, Set<String>> subscribedPlayers = new HashMap<EquatableWeakReference<MineZGame>, Set<String>>();
	private static final Map<String, Location> playerBackLocations = new HashMap<String, Location>();
	
	@Override
	public boolean createGame(String name) {
	    MineZGame game = new MineZGame(name);
	    boolean attempt = games.add(game);
	    if(attempt){
		saveGames();
	    }
	    return attempt;
	}

	@Override
	public boolean createGame(String name, String setup){
	    MineZGame game = new MineZGame(name, setup);
	    boolean attempt = games.add(game);
	    if(attempt){
		saveGames();
	    }
	    return attempt;
	}
	
	@Override
	public boolean removeGame(String name) {
		MineZGame game = null;
		if (Strings.nullToEmpty(name).equals("")) return false;
		for (MineZGame g : games) {
			if (g.getName().equalsIgnoreCase(name)) {
				game = g;
			}

		}
		if(game == null) return false;
		boolean attempt = games.remove(game);
		game.delete();
		if(attempt){
			saveGames();
		}
		return attempt;
	}
		
	public PlayerStat createStat(MineZGame game, Player player) {
		PlayerStat stat = new PlayerStat(game, player);
		if (stats.get(player.getName()) == null) stats.put(player.getName(), new HashMap<EquatableWeakReference<MineZGame>, PlayerStat>());
		stats.get(player.getName()).put(new EquatableWeakReference<MineZGame>(game), stat);
		return stat;
	}
	
	public void clearGamesForPlayer(String player, MineZGame game) {
		stats.get(player).remove(new EquatableWeakReference<MineZGame>(game));
	}

	@Override
	public List<MineZGame> getRawGames() {
		return new ArrayList<MineZGame>(games);
	}

	
	@Override
	public List<EquatableWeakReference<MineZGame>> getGames() {
		List<EquatableWeakReference<MineZGame>> list = new ArrayList<EquatableWeakReference<MineZGame>>();
		for (MineZGame game : games ) {
			list.add(new EquatableWeakReference<MineZGame>(game));
		}
		return list;
	}

	@Override
	public EquatableWeakReference<MineZGame> getGame(String name) {
		MineZGame game = getRawGame(name);
		if (game != null) return new EquatableWeakReference<MineZGame>(game);
		return null;
	}

	@Override
	public MineZGame getRawGame(String name) {
		if (Strings.nullToEmpty(name).equals("")) return null;
		for (MineZGame game : games) {
			if (game.getName().equalsIgnoreCase(name)) {
				return game;
			}

		}
		return null;
	}
	
	

	@Override
	public WeakReference<MineZGame> getSession(Player player) {
		if (stats.get(player.getName()) != null) {
			for (EquatableWeakReference<MineZGame> gameGotten : stats.get(player.getName()).keySet()) {
				PlayerStat stat = stats.get(player.getName()).get(gameGotten);
				if (stat != null && stat.getState() != PlayerStat.PlayerState.DEAD && stat.getState() != PlayerStat.PlayerState.NOT_IN_GAME) return gameGotten;
			}
		}
		return null; 
	}

	@Override
	public MineZGame getRawSession(Player player) {
		WeakReference<MineZGame> session = getSession(player);
		return session == null ? null : session.get();
	}

	@Override
	public WeakReference<MineZGame> getPlayingSession(Player player) {
		if (stats.get(player.getName()) != null) {
			for (EquatableWeakReference<MineZGame> gameGotten : stats.get(player.getName()).keySet()) {
				PlayerStat stat = stats.get(player.getName()).get(gameGotten);
				if (stat != null && stat.getState() == PlayerStat.PlayerState.PLAYING) return gameGotten;
			}
		}
		return null;
	}

	@Override
	public MineZGame getRawPlayingSession(Player player) {
		WeakReference<MineZGame> session = getPlayingSession(player);
		return session == null ? null : session.get();
	}

	@Override
	public boolean doesNameExist(String name) {
		return getRawGame(name) != null;
	}

	public void playerLeftServer(Player player) {
		if (spectators.containsKey(player.getName())) {
			WeakReference<MineZGame> spectated = spectators.remove(player.getName());
			if (spectated.get() == null || spectated == null) return;
			spectated.get().removeSpectator(player);
			return;
		}
		WeakReference<MineZGame> game = getSession(player);
		if (game == null || game.get() == null) return;
		game.get().leave(player, true);
	}

	public void loadGames() {
		ConfigurationSection gamesSection = Files.GAMES.getConfig().getConfigurationSection("games");
		if (gamesSection == null) {
			return;
		}
		List<String> checked = new ArrayList<String>();
		for (Iterator<MineZGame> it = games.iterator(); it.hasNext();) {
			MineZGame game = it.next();
			checked.add(game.getName());
			if (gamesSection.contains(game.getName())) {
				game.loadFrom(gamesSection.getConfigurationSection(game.getName()));
			}
			else {
				game.delete();
				it.remove();
			}
		}
		for (String name : gamesSection.getKeys(false)) {
			if (checked.contains(name)) continue;
			MineZGame game = new MineZGame(name);
			game.loadFrom(gamesSection.getConfigurationSection(name));
			games.add(game);
		}
	}

	public void saveGames() {
		for (MineZGame game : games) {
		    saveGame(game);
		}
	}
	
	public void reloadGame(MineZGame game){
		ConfigurationSection gameSection = Files.GAMES.getConfig().getConfigurationSection("games." + game.getName());
		if (gameSection == null) {
			return;
		}
		game.loadFrom(gameSection);
		games.add(game);
	}

	public void saveGame(MineZGame game){
		ConfigurationSection section = Files.GAMES.getConfig().getConfigurationSection("games");
		if(section == null){
		    section = Files.GAMES.getConfig().createSection("games");
		}
		ConfigurationSection saveSection = section.createSection(game.getName());
		game.saveTo(saveSection);
		Files.GAMES.save();
	}

	@Override
	public boolean addSpectator(Player player, Game game, Player spectated) {
		if (spectators.containsKey(player.getName())) return false;
		if (!((MineZGame) game).addSpectator(player, spectated)) return false;
		spectators.put(player.getName(), new EquatableWeakReference<MineZGame>((MineZGame) game));
		return true;
	}
	
	@Override
	public EquatableWeakReference<MineZGame> getSpectating(Player player) {
	    if (player == null) return null;    
	    if (!spectators.containsKey(player.getName())) return null;
	    return spectators.get(player.getName());
	}
	
	@Override
	public boolean removeSpectator(Player player) {
		WeakReference<MineZGame> game = spectators.remove(player.getName());
		if (game != null && game.get() != null) {
			game.get().removeSpectator(player);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isPlayerSubscribed(Player player, Game game) {
		if (MyMineZ.hasPermission(player, CommandPerms.Perm.AUTO_SUBSCRIBE)) return true;
		if (game != null){
			if (subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)) == null) {
				subscribedPlayers.put(new EquatableWeakReference<MineZGame>(((MineZGame) game)), new HashSet<String>());
			}
			if (subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)).contains(player.getName())) return true;
		}
		return globalSubscribedPlayers.contains(player.getName());
	}
	
	@Override
	public void removedSubscribedPlayer(Player player, Game game) {
		if (game != null) {
			if (subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)) == null) {
				subscribedPlayers.put(new EquatableWeakReference<MineZGame>(((MineZGame) game)), new HashSet<String>());
			}
			subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)).remove(player.getName());
		}
		else {
			globalSubscribedPlayers.remove(player.getName());
		}
	}
	
	@Override
	public void addSubscribedPlayer(Player player, Game game) {
		if (game != null) {
			if (subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)) == null) {
				subscribedPlayers.put(new EquatableWeakReference<MineZGame>(((MineZGame) game)), new HashSet<String>());
			}
			subscribedPlayers.get(new EquatableWeakReference<MineZGame>((MineZGame) game)).add(player.getName());
		}
		else {
			globalSubscribedPlayers.add(player.getName());
		}
	}
	
	public Set<String> getSubscribedPlayers(MineZGame game) {
		Set<String> set = new HashSet<String>();
		if (game != null) {
			set.addAll(subscribedPlayers.get(new EquatableWeakReference<MineZGame>(game)));
		} else {
			set.addAll(globalSubscribedPlayers);
		}
		return set;
	}
	
	public void addBackLocation(Player player) {
		playerBackLocations.put(player.getName(), player.getLocation());
	}
	
	public Location getAndRemoveBackLocation(Player player) {
		return playerBackLocations.remove(player.getName());
	}

}
