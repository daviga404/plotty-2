package com.daviga404.commands.user;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlot;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotDeleter;

public class CommandPlotClear extends PlottyCommand{
	
	private Plotty plugin;
	
	public CommandPlotClear(Plotty pl){
		super(
		"clear",
		"(clear)(\\s+)(\\d+)",//"(clear)(\\s+)([+-]\\d+)(\\s+)([+-]\\d+)", //clear SPACE int SPACE int SPACE int
		"plotty.clear",
		"/plot clear <id>",
		"Clears a plot."
		);
		this.plugin = pl;
	}

	public boolean execute(Player p, String[] args) {
		if(!plugin.dm.config.clearEnabled){
			p.sendMessage("§4[Plotty] §cClearing of plots is prohibited.");
			return true;
		}
		DataManager dm = plugin.getDataManager();
		if(PlotDeleter.isCooling(p.getName())){
			p.sendMessage("§4[Plotty] §cYou cannot clear another plot for "+dm.config.delCooldown+" seconds.");
			return true;
		}
		PlottyPlot plot = dm.getPlotFromId(Integer.parseInt(args[0]));
		if(plot == null){
			p.sendMessage(plugin.lang.notFound);
			return true;
		}
		String owner = dm.getPlotOwner(plot);
		boolean canDelete = false;
		if(owner != null && p.getName().equalsIgnoreCase(owner)){
			canDelete = true;
		}else if(p.hasPermission("plotty.clear.others") || p.hasPermission("plotty.*") || p.isOp()){
			canDelete = true;
		}
		if(!canDelete){
			p.sendMessage(plugin.lang.dontOwn);
			return true;
		}
		plugin.getPlotClearer().clearPlot(new Plot(plot.x,plugin.plotHeight,plot.z,p.getWorld()));
		PlotDeleter.addCooldown(p.getName(), plugin);
		p.sendMessage(plugin.lang.plotCleared);
		return true;
	}
}
