package com.daviga404.commands.user;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotFinder;
import com.daviga404.plots.PlotRegion;

public class CommandPlotNew extends PlottyCommand {

	Plotty plugin;
	
	public CommandPlotNew(Plotty plugin){
		super(
		"new",
		"new",
		"plotty.new",
		"/plot new",
		"Makes a new plot."
		);
		this.plugin = plugin;
	}

	public boolean execute(Player p, String[] args) {
		//if(p.getWorld().getGenerator() != null && p.getWorld().getGenerator().getClass().toString().equalsIgnoreCase("class uk.co.jacekk.bukkit.infiniteplots.plotsgenerator")){
			if(plugin.getDataManager().getPlayer(p.getName()).grantedPlots > 0){
				DataManager dm = plugin.getDataManager();
				PlottyPlayer newpp = dm.getPlayer(p.getName());
				newpp.grantedPlots--;
				dm.config.players[dm.pIndex(p.getName())] = newpp;
				dm.save();
				Plot freePlot = PlotFinder.findPlot(p.getWorld(),plugin);
				int id = plugin.getDataManager().getLatestId();
				PlotRegion.makePlotRegion(freePlot, p.getName(),id);
				plugin.getDataManager().addPlot(freePlot, p.getName(),id);
				plugin.telePlayer(freePlot, p);
				p.sendMessage(plugin.lang.createdPlot.replaceAll("%s",id+""));
			}else if(!plugin.getDataManager().pExceededMaxPlots(p.getName())){
				Plot freePlot = PlotFinder.findPlot(p.getWorld(),plugin);
				int id = plugin.getDataManager().getLatestId();
				PlotRegion.makePlotRegion(freePlot, p.getName(),id);
				plugin.getDataManager().addPlot(freePlot, p.getName(),id);
				plugin.telePlayer(freePlot, p);
				p.sendMessage(plugin.lang.createdPlot.replaceAll("%s",id+""));
			}else{
				p.sendMessage(plugin.lang.reachedMaxPlots);
			}
		//}else{
		//	p.sendMessage("§4[Plotty] §cYou must be in a world generated with InfinitePlots to do this!");
		//}
		return true;
	}

}
