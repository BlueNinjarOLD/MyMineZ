package me.kitskub.myminez.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import me.kitskub.myminez.api.Game;

// called when a player kills another player
public class PlayerKillEvent extends GameEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Player killer, killed;
	private String deathMessage;
	
	public PlayerKillEvent(final Game game, final Player killer, final Player killed, String message) {
		super(game);
		this.killer = killer;
		this.killed = killed;
		deathMessage = message;
	}
	
	public PlayerKillEvent(final Game game, final Player killed) {
		this(game, null, killed, null);
	}
	
	public Player getKiller() {
		return killer;
	}
	
	public Player getKilled() {
		return killed;
	}
	
	public void setDeathMessage(String message) {
		deathMessage = message;
	}
	
	public String getDeathMessage() {
		return deathMessage;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
