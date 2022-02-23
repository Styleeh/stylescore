package net.style.scoreboard.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.style.scoreboard.Main;

public class Info {

	public static HashSet<UUID> CLAN_SCORE = new HashSet<>();

	private final DecimalFormatSymbols DFS = new DecimalFormatSymbols(new Locale("pt", "BR"));
	private DecimalFormat FORMATTER = new DecimalFormat("###,###,###", DFS);
	private Player p;

	public Info(Player p) {
		this.p = p;
	}

	public String getPlayerCoins() {
		return FORMATTER.format(Main.Vault.getPlayerBalance(p));
	}

	@SuppressWarnings("deprecation")
	public String getPlayerCash() {
		return FORMATTER.format(Main.playerPoints.getAPI().look(p.getName()));
	}


}
