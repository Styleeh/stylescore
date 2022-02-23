package net.style.scoreboard.mcmmo;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class McMMoCache {

	public HashMap<UUID, Integer> MCMMO_TOTAL_LEVEL;
	
	public McMMoCache() {
		MCMMO_TOTAL_LEVEL = new HashMap<>();
	}
	
	public HashMap<UUID, Integer> getLevels() {
		return MCMMO_TOTAL_LEVEL;
	}

	public String getPlayerInfo(Player p) {
		return "�a" + MCMMO_TOTAL_LEVEL.get(p.getUniqueId());
	}
	
	public void addToCache(UUID uuid, int value) {
		MCMMO_TOTAL_LEVEL.put(uuid, value);
	}
	
	public void removeFromCache(Player player) {
		MCMMO_TOTAL_LEVEL.remove(player.getUniqueId());
	}
}
