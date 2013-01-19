package com.daviga404.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
import com.daviga404.data.PlottyPlot;
import com.daviga404.plots.Plot;

public class CommandPlotTp extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotTp(Plotty pl){
		super(
		"tp",
		"(tp)(\\s+)(\\d+)",
		"plotty.tp",
		"/plot tp <id>",
		"Teleports to a plot."
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		PlottyPlot pl=null;
		DataManager dm = plugin.getDataManager();
		for(PlottyPlayer player : dm.config.players){
			for(PlottyPlot plot : player.plots){
				if(plot.id == Integer.parseInt(args[0])){
					pl = plot;
				}
			}
		}
		if(pl == null){p.sendMessage("§4[Plotty] §cPlot not found.");return true;}
		plugin.telePlayer(new Plot(pl.x,plugin.plotHeight,pl.z,Bukkit.getWorld(pl.world)), p);
		p.sendMessage(plugin.lang.teleportedToPlot.replaceAll("%s", pl.id+""));
		return true;
	}
}
