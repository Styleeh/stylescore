package net.style.scoreboard.runnable;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import net.md_5.bungee.api.ChatColor;
import net.style.scoreboard.Main;
import net.style.scoreboard.api.ScoreBoardAPI;
import net.style.scoreboard.listener.ScoreBoardCreator;
import net.style.scoreboard.api.Info;

public class BoardUpdaterRunnable extends BukkitRunnable {

	public BoardUpdaterRunnable() {
		runTaskTimerAsynchronously(Main.get(), 20L, 120L);
	}

	@Override
	public void run() {
		Iterator<Player> iter = ScoreBoardCreator.boards.keySet().iterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			update(player);
		}
	}

	public static final void update(Player player) {
		Info INFO = new Info(player);
		ScoreBoardAPI score = ScoreBoardCreator.boards.get(player);
		MPlayer mplayer = MPlayer.get(player);

		if (score == null)
			return;

		String factionNameString = getWorldName(player);

		if (factionNameString == null)
			return;

		score.setDisplayName(factionNameString);

		score.update("§6✪§f" + INFO.getPlayerCash(), 2);
		score.update("§2$§f" + INFO.getPlayerCoins(), 3);

		if (mplayer.hasFaction()) {
			Faction faction = mplayer.getFaction();

			if (faction.getId() == null)
				return;

			score.update(ChatColor.YELLOW.toString() + faction.getLandCount(), 5);
			score.update(ChatColor.YELLOW.toString() + faction.getMPlayersWhereOnline(true).size(), 6);
			score.update(ChatColor.YELLOW.toString() + faction.getPowerRounded() + "/" + faction.getPowerMaxRounded(),
					7);
			score.update(" §e[" + faction.getTag() + "§e] " + faction.getName(), 8);
			score.update(ChatColor.GREEN.toString() + mplayer.getPowerRounded() + "/" + mplayer.getPowerMaxRounded(),
					11);
			score.update(ChatColor.GREEN.toString() + Main.get().getMMOCache().getPlayerInfo(player), 12);
		} else {
			score.update(ChatColor.GREEN.toString() + mplayer.getPowerRounded() + "/" + mplayer.getPowerMaxRounded(),
					7);
			score.update(ChatColor.GREEN.toString() + Main.get().getMMOCache().getPlayerInfo(player), 8);
		}
	}

	protected final static String getWorldName(Player p) {
		Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));

		if (factionAt.getId() == null)
			return null;

		String factionName = factionAt.getColorTo(MPlayer.get(p)) + factionAt.getName();

		if (p.getWorld().getName().equals("vip"))
			return "§dÁrea VIP";
		else if (p.getWorld().getName().equals("mina"))
			return "§7Mundo Mina";
		else if (p.getWorld().getName().equals("glad"))
			return "§4Evento Guerra";
		else if (p.getWorld().getName().equals("arena"))
			return "§4Arena";
		else
			return factionName;
	}

}
