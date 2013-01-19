package com.daviga404.commands.user;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlot;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotFinder;
import com.daviga404.plots.PlotRegion;

public class CommandPlotClaim extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotClaim(Plotty pl){
		super(
		"claim",
		"(claim)",
		"plotty.claim",
		"/plot claim",
		"Claims the plot you are standing in."
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		if(plugin.getDataManager().pExceededMaxPlots(p.getName())){
			p.sendMessage(plugin.lang.reachedMaxPlots);
			return true;
		}
		Location l = p.getLocation();
		int x = l.getBlockX();
		int z = l.getBlockZ();
		Integer[] corners = PlotFinder.getCornerFromCoords(x, z, plugin.plotSize);
		if(corners.length != 2){
			p.sendMessage(plugin.lang.notStandingInPlot);
			return true;
		}
		DataManager dm = plugin.getDataManager();
		PlottyPlot plot = dm.getPlotFromCoords(corners[0], corners[1]);
		if(plot != null){
			p.sendMessage(plugin.lang.plotHere);
			return true;
		}
		int id = plugin.getDataManager().getLatestId();
		Plot legacyPlot = new Plot(corners[0],plugin.plotHeight,corners[1],p.getWorld());
		PlotRegion.makePlotRegion(legacyPlot, p.getName(),id);
		plugin.getDataManager().addPlot(legacyPlot, p.getName(), id);
		plugin.telePlayer(legacyPlot, p);
		p.sendMessage(plugin.lang.plotClaimed.replaceAll("%s", id+""));
		return true;
	}
}
