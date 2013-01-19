package com.daviga404.commands.user;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlot;

public class CommandPlotList extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotList(Plotty pl){
		super(
		"list",
		"list",
		"plotty.list",
		"/plot list",
		"Lists all plots."
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		StringBuilder builder = new StringBuilder();
		builder.append("§1[Plotty] Your Plots:\n");
		DataManager dm = plugin.getDataManager();
		for(PlottyPlot plot : dm.getPlayer(p.getName()).plots){
			builder.append("§b- Plot ");
			builder.append(plot.id);
			builder.append(" §9[x:");
			builder.append(plot.x);
			builder.append(", z:");
			builder.append(plot.z);
			builder.append("] §b[Friends: ");
			String friendsString="";
			for(String s : plot.friends){
				friendsString += s +", ";
			}
			if(friendsString != ""){
				friendsString = friendsString.substring(0,friendsString.length()-2);
			}else{
				friendsString = "none";
			}
			builder.append(friendsString);
			builder.append("]\n");
		}
		p.sendMessage(builder.toString());
		return true;
	}
}
