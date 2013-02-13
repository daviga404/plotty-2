package com.daviga404.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;

public class CommandPlotGrant extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotGrant(Plotty pl){
		super(
		"grant",
		"(grant)( )(\\w+)(( )(\\d+))?",
		"plotty.admin.grant",
		"/plot grant <name> [amount]",
		"Gives a player the ability to make one or 'amount' plots.",
		false
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		int amount = 1;
		if(args.length == 2){
			amount = Integer.parseInt(args[1]);
		}
		DataManager dm = plugin.getDataManager();
		PlottyPlayer pp = dm.getPlayer(args[0]);
		pp.grantedPlots += amount;
		dm.config.players[dm.pIndex(args[0])] = pp;
		dm.save();
		
		if(Bukkit.getPlayer(args[0]) == null){
			String[] pgn = dm.config.playerGrantNotify;
			String[] newpgn = new String[pgn.length+1];
			int i=0;
			for(String s : pgn){
				newpgn[i] = s;
				i++;
			}
			newpgn[pgn.length] = args[0];
			dm.config.playerGrantNotify = newpgn;
			dm.save();
			p.sendMessage("§a[Plotty] Granted §2"+amount+"§a plots to §l"+args[0]+"§a. Player will be notified when they come online.");
		}else{
			p.sendMessage("§a[Plotty] Granted §2"+amount+"§a plots to §l"+args[0]);
			Bukkit.getPlayer(args[0]).sendMessage("§1[Plotty] §bYou have been granted §9"+amount+"§b plot(s)!");
		}
		return true;
	}
}
