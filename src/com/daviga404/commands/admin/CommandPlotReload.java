package com.daviga404.commands.admin;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.language.LangManager;

public class CommandPlotReload extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotReload(Plotty pl){
		super(
		"reload",
		"(reload)",
		"plotty.admin.reload",
		"/plot reload",
		"Reloads the configuration and language files.",
		false
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		try {
			plugin.dm.checkForFile();
			plugin.langMan = new LangManager(plugin);
			plugin.lang = plugin.langMan.getLang();
			p.sendMessage("§a[Plotty] All configs reloaded!");
		} catch (Exception e) {
			p.sendMessage("§4[Plotty] §cAn error occurred while reloading. Check the console for errors.");
			e.printStackTrace();
		}
		return true;
	}
}
