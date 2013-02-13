package com.daviga404.commands.user;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotFinder;

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
		int id = plugin.getDataManager().getLatestId();
		Plot freePlot = PlotFinder.findPlot(p.getWorld(), plugin);
		int x = freePlot.getX();
		int y = freePlot.getY();
		int z = freePlot.getZ();
		World w = p.getWorld();
		boolean claiming = false;
		String result = plugin.makePlot(id, x, y, z, w, p, claiming);
		p.sendMessage(result);
		//}else{
		//	p.sendMessage("§4[Plotty] §cYou must be in a world generated with InfinitePlots to do this!");
		//}
		return true;
	}

}
