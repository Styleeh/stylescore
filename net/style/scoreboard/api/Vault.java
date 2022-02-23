package net.style.scoreboard.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Vault {

	private Economy Economy;

	public Vault() {
		setupEconomy();
	}

	public double getPlayerBalance(Player p) {
		return Economy.hasBankSupport() ? Economy.bankBalance(p.getName()).balance : Economy.getBalance(p);
	}

	private boolean setupEconomy() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		this.Economy = rsp.getProvider();
		return Economy != null;
	}
}
