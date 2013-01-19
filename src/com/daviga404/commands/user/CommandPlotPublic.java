package com.daviga404.commands.user;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
import com.daviga404.data.PlottyPlot;

public class CommandPlotPublic extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotPublic(Plotty pl){
		super(
		"public",
		"(public)(\\s+)(\\d+)",
		"plotty.public",
		"/plot public <id>",
		"Makes a plot visible in the global list & rankings."
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		DataManager dm = plugin.getDataManager();
		PlottyPlot plot = dm.getPlotFromId(Integer.parseInt(args[0]));
		if(plot == null){p.sendMessage(plugin.lang.notFound);return true;}
		String owner = dm.getPlotOwner(plot);
		if(!owner.equalsIgnoreCase(p.getName())){
			p.sendMessage(plugin.lang.dontOwn);
			return true;
		}
		plot.visible = true;
		PlottyPlayer pp = dm.getPlayer(p.getName());
		pp.plots[dm.plotIndex(plot.id, dm.getPlayer(p.getName()))] = plot;
		dm.config.players[dm.pIndex(p.getName())] = pp;
		dm.save();
		p.sendMessage(plugin.lang.madePublic);
		return true;
	}
}
