package net.style.scoreboard.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.exceptions.InvalidPlayerException;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import net.style.scoreboard.Main;
import net.style.scoreboard.api.ScoreBoardAPI;
import net.style.scoreboard.runnable.BoardUpdaterRunnable;

public class ScoreBoardCreator implements Listener {

	public static final HashMap<Player, ScoreBoardAPI> boards = new HashMap<>();

	@EventHandler
	void event(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		createBoard(player);
		getPlayerInfo(player);
	}

	@EventHandler
	void event(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		Main.get().getMMOCache().removeFromCache(player);

		ScoreBoardAPI sideboard = boards.get(player);

		sideboard.getTeams().clear();
		sideboard.reset();
		boards.remove(player);
	}

	@EventHandler
	void event(EventFactionsMembershipChange e) {
		Player player = e.getMPlayer().getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				createBoard(player);
			}
		}.runTask(Main.get());
	}

	protected final void getPlayerInfo(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					int level = ExperienceAPI.getPowerLevelOffline(p.getUniqueId());
					Main.get().getMMOCache().MCMMO_TOTAL_LEVEL.put(p.getUniqueId(), level);
				} catch (InvalidPlayerException e) {
					Main.get().getMMOCache().MCMMO_TOTAL_LEVEL.put(p.getUniqueId(), 0);
				}
			}
		}.runTaskAsynchronously(Main.get());
	}

	@EventHandler
	void onDisband(EventFactionsDisband e) {
		Faction faction = e.getFaction();
		new BukkitRunnable() {
			@Override
			public void run() {
				faction.getOnlinePlayers().forEach(r -> createBoard(r));
			}
		}.runTask(Main.get());
	}

	@EventHandler
	void onExpGain(McMMOPlayerXpGainEvent e) {
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		ScoreBoardAPI score = ScoreBoardCreator.boards.get(p);
		if (mp == null)
			return;
		@SuppressWarnings("deprecation")
		int gn = Math.round(e.getXpGained());
		if (mp.hasFaction()) {
			score.update("§f  " + e.getSkill().getName() + "§a +" + gn, 12);

		} else {
			score.update("§f  " + e.getSkill().getName() + "§a +" + gn, 8);
		}

	}

	public final static void createBoard(Player player) {
		MPlayer mp = MPlayer.get(player);

		if (mp == null)
			return;

		String UUIDRandom = UUID.randomUUID().toString().substring(0, 6) + UUID.randomUUID().toString().substring(0, 6)
				+ ((int) Math.round(Math.random() * 100));

		ScoreBoardAPI score = new ScoreBoardAPI("", UUIDRandom);

		score.add("§e   loja.stylezinho.com ", 0);
		score.blankLine(1);
		score.add("§f  Cash:§6 ", 2);
		score.add("§f  Coins:§e ", 3);
		score.blankLine(4);

		if (mp.hasFaction()) {
			score.add("   §fTerras: ", 5);
			score.add("   §fOnline: ", 6);
			score.add("   §fPoder: ", 7);
			score.add("  ", 8);
			score.blankLine(9);
			score.add("  §fPoder: ", 11);
			score.add("  §fNível: ", 12);
			score.blankLine(13);
		} else {
			score.add(" ", 6);
			score.add("§f  Poder: ", 7);
			score.add("§f  Nível: ", 8);
			score.add("  §7Sem facção", 5);
			score.blankLine(9);
		}

		score.build();
		boards.remove(player);
		score.send(player);
		boards.put(player, score);
		BoardUpdaterRunnable.update(player);
	}
}
