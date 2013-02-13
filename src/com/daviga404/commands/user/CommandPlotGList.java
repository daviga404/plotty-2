package com.daviga404.commands.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
import com.daviga404.data.PlottyPlot;

public class CommandPlotGList extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotGList(Plotty pl){
		super(
		"glist",
		"(glist)((\\s+)(top))?((\\s+)(\\d+))?",
		"plotty.glist",
		"/plot glist [top] [page]",
		"Displays the global list of plots. (add top for votes order)",
		false
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		DataManager dm = plugin.dm;
		ArrayList<PlottyPlot> plots = new ArrayList<PlottyPlot>();
		for(PlottyPlayer pp : dm.config.players){
			for(PlottyPlot plot : pp.plots){
				if(plot.visible){
					plots.add(plot);
				}
			}
		}
		boolean sortByTop = ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("top"));
		int page=0;
		if(args.length == 1){
			Pattern pat = Pattern.compile("\\d+");
			if(pat.matcher(args[0]).matches()){
				page = Integer.parseInt(args[0])-1;
			}
		}else if(args.length == 2){
			Pattern pat = Pattern.compile("\\d+");
			if(pat.matcher(args[1]).matches()){
				page = Integer.parseInt(args[1])-1;
			}
		}
		if(page < 0){
			page = 0;
		}
		if(sortByTop){
			Collections.sort(plots,new Comparator<PlottyPlot>(){
	
				public int compare(PlottyPlot p1, PlottyPlot p2) {
					return p1.rank - p2.rank;
				}
				
			});
			Collections.reverse(plots);
		}else{
			Collections.sort(plots,new Comparator<PlottyPlot>(){
				public int compare(PlottyPlot p1, PlottyPlot p2){
					return p1.id-p2.id;
				}
			});
		}
		String title = "§1§l[Plotty] §9§lGlobal Plot List (ordered by "+(sortByTop ? "rank" : "ID")+"):\n";
		if(sortByTop){
			title += "§8§oFormat: [Rank] ID | Creator | (Votes)\n";
		}else{
			title += "§8§oFormat: [ID] Creator (Votes)\n";
		}
		ArrayList<String> plotStrings = new ArrayList<String>();
		for(PlottyPlot plot : plots){
			if(sortByTop){
				StringBuilder s = new StringBuilder();
				s.append("§1- [");
				s.append(plots.indexOf(plot)+1);
				s.append("] §9");
				s.append(plot.id);
				s.append(" | ");
				s.append(dm.getPlotOwner(plot) != null ? dm.getPlotOwner(plot) : "Unknown");
				s.append(" §b(");
				s.append(plot.rank);
				s.append(" votes)\n");
				plotStrings.add(s.toString());
			}else{
				StringBuilder s = new StringBuilder();
				s.append("§1- [");
				s.append(plot.id);
				s.append("] §9");
				s.append(dm.getPlotOwner(plot) != null ? dm.getPlotOwner(plot) : "Unknown");
				s.append(" | §b(");
				s.append(plot.rank);
				s.append(" votes)\n");
				plotStrings.add(s.toString());
			}
		}
		ArrayList<List<String>> pages = new ArrayList<List<String>>();
		for(int i=0;i<plotStrings.size();i++){
			if(i >= plotStrings.size()-8){
				pages.add(plotStrings.subList(i,plotStrings.size()));
				break;
			}else if(i % 8 == 0){
				pages.add(plotStrings.subList(i,i+7));
			}
		}
		if(pages.size() == 0){
			p.sendMessage("§7No plots have been created.");
			return true;
		}
		if((page+1) > pages.size() || page < 0){
			p.sendMessage("§7Warning: Page not in range. Using page 1 instead.");
			page = 0;
		}
		p.sendMessage(title);
		for(String str : pages.get(page)){
			p.sendMessage(str);
		}
		p.sendMessage("§1Page "+(page+1)+"/"+pages.size());
		return true;
	}
}
