package me.kitskub.myminez.api.event;

import org.bukkit.event.Event;

import me.kitskub.myminez.api.Game;

public abstract class GameEvent extends Event {
	private final Game game;
	
	public GameEvent(final Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}

}