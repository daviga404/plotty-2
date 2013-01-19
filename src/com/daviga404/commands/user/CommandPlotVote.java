package com.daviga404.commands.user;

import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
import com.daviga404.data.PlottyPlot;
import com.daviga404.plots.PlotFinder;

public class CommandPlotVote extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotVote(Plotty pl){
		super(
		"vote",
		"(vote)",
		"plotty.vote",
		"/plot vote",
		"Upvotes the plot you are standing in."
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		DataManager dm = plugin.getDataManager();
		PlottyPlayer player = dm.getPlayer(p.getName());
		if(player == null){System.out.println("[Plotty] ERROR: PLAYER IS NULL. CHECK CONFIG FOR ERRORS.");return false;}
		long lastVoted = player.lastVoted;
		if(lastVoted == 0 || now()-lastVoted > (dm.config.voteDelay*60*60*1000)){
			Location l = p.getLocation();
			int x = l.getBlockX();
			int z = l.getBlockZ();
			Integer[] corners = PlotFinder.getCornerFromCoords(x, z, plugin.plotSize);
			if(corners.length != 2){
				p.sendMessage(plugin.lang.notStandingInPlot);
				return true;
			}
			PlottyPlot plot = dm.getPlotFromCoords(corners[0], corners[1]);
			if(plot == null){
				p.sendMessage(plugin.lang.plotFree);
				return true;
			}
			if(!plot.visible){
				p.sendMessage("§4[Plotty] §cThis plot is private.");
				return true;
			}
			plot.rank = plot.rank+1;
			if(dm.getPlotOwner(plot) == null || dm.getPlayer(dm.getPlotOwner(plot)) == null){p.sendMessage("§4ERROR: §cOwner's playerdata not found.");return true;}
			PlottyPlayer owner = dm.getPlayer(dm.getPlotOwner(plot));
			if(owner.name.equalsIgnoreCase(player.name)){
				p.sendMessage("§4[Plotty] §cYou can't vote for your own plot!");
				return true;
			}
			owner.plots[dm.plotIndex(plot.id, owner)] = plot;
			dm.config.players[dm.pIndex(owner.name)] = owner;
			player.lastVoted = now();
			dm.config.players[dm.pIndex(player.name)] = player;
			dm.save();
		}else{
			p.sendMessage(plugin.lang.cantVote.replaceAll("%s", Math.round(((lastVoted+(dm.config.voteDelay*60*60*1000))-now())/1000/60)+""));
			return true;
		}
		return true;
	}
	public long now(){
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}
}
