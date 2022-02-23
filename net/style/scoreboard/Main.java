package net.style.scoreboard;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nossr50.api.ExperienceAPI;

import net.milkbowl.vault.economy.Economy;
import net.style.scoreboard.listener.ScoreBoardCreator;
import net.style.scoreboard.mcmmo.McMMoCache;
import net.style.scoreboard.runnable.BoardUpdaterRunnable;

public class Main extends JavaPlugin {

	public static net.yiatzz.scoreboard.api.Vault Vault;
	private Economy econ;
	private McMMoCache mcmmocache;
	public static PlayerPoints playerPoints;

	public void onEnable() {
		setupEconomy();
		mcmmocache = new McMMoCache();
		Vault = new net.yiatzz.scoreboard.api.Vault();
		Bukkit.getPluginManager().registerEvents(new ScoreBoardCreator(), this);
		init();
		playerPoints = PlayerPoints.getPlugin(PlayerPoints.class);
	}

	protected void init() {
		updateStats();
		new BoardUpdaterRunnable();
	}

	protected void updateStats() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				ScoreBoardCreator.boards.entrySet().stream().forEach(r -> mcmmocache.MCMMO_TOTAL_LEVEL
						.put(r.getKey().getUniqueId(), ExperienceAPI.getPowerLevelOffline(r.getKey().getName())));
			}
		}.runTaskTimerAsynchronously(this, 1200L, 1200L);
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

		if (rsp == null)
			return false;

		econ = rsp.getProvider();
		return econ != null;
	}

	public Economy getEconomy() {
		return econ;
	}

	public final static Main get() {
		return JavaPlugin.getPlugin(Main.class);
	}

	public McMMoCache getMMOCache() {
		return mcmmocache;
	}
}
