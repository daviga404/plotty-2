package com.daviga404.commands.admin;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotRegion;

public class CommandPlotMigrate extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotMigrate(Plotty pl){
		super(
		"migrate",
		"(migrate)",
		"plotty.admin.migrate",
		"/plot migrate",
		"Migrates old configs to Plotty v2."
		);
		this.plugin = pl;
	}
	
	public void log(String s){
		plugin.getServer().getLogger().info(s);
	}
	
	public boolean execute(Player p, String[] args){
		log("Migrating Plotty v1.x configs");
		File dir = new File(plugin.getDataFolder()+File.separator+"userdata");
		File mainConfigFile = new File(plugin.getDataFolder()+File.separator+"config.yml");
		if(!dir.exists() || !mainConfigFile.exists()){
			p.sendMessage("§4[Plotty] §cConfigs not found.");
			return true;
		}
		YamlConfiguration mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
		int count=0;
		for(File f : dir.listFiles()){
			if(f.isFile() && f.getName().endsWith(".yml")){
				YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Object>> plots = (ArrayList<ArrayList<Object>>) config.getList("plots");
				for(ArrayList<Object> plot : plots){
					if(plot.size() >= 4){
						if(plot.get(0) instanceof Integer && plot.get(1) instanceof Integer){
							int sx = (Integer) plot.get(0);
							int sz = (Integer) plot.get(1);
							int id = plugin.getDataManager().getLatestId();
							String owner = f.getName().lastIndexOf(".") == -1 ? f.getName() : f.getName().substring(0,f.getName().lastIndexOf("."));
							Plot legacy = new Plot(sx,21,sz,plugin.getServer().getWorld(mainConfig.getString("plotty.world")) == null ? plugin.getServer().getWorld(plugin.getDataManager().config.worlds[0]) : plugin.getServer().getWorld(mainConfig.getString("plotty.world")));
							PlotRegion.makePlotRegion(legacy, owner, id);
							plugin.getDataManager().addPlot(legacy, owner, id);
							count++;
						}else{
							log("Warning: first elements in plot "+plots.indexOf(plot)+" of "+f.getName()+"'s plots are not integers.");
						}
					}else{
						log("Warning: Bad plot found in file "+f.getName());
					}
				}
			}
		}
		log(count+" plots migrated to Plotty v2.");
		p.sendMessage("§a[Plotty] "+count+" plots migrated to Plotty v2.");
		return true;
	}
}
