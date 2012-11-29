package me.kitskub.myminez;

import org.bukkit.entity.Player;

public class ThirstHandler {

	public static void setThirst(Player p, int thirst) {
		if (thirst > 20 || thirst < 0) throw new IllegalArgumentException("Thirst has to be between 0 and 20.");
        p.setLevel(thirst);
	}
}
