package com.daviga404;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.BukkitMetricsLite;

import com.daviga404.commands.PlottyExecutor;
import com.daviga404.data.DataManager;
import com.daviga404.language.Lang;
import com.daviga404.language.LangManager;
import com.daviga404.plots.Plot;
import com.daviga404.plots.PlotClearer;
import com.daviga404.plots.PlotRegion;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Plotty extends JavaPlugin{
	public WorldGuardPlugin worldGuard;
	public WorldEditPlugin worldEdit;
	public PlotClearer pc;
	public DataManager dm;
	public Lang lang;
	public LangManager langMan;
	public void onEnable(){
		if(!getWorldGuard() || !getWorldEdit()){
			System.out.println("[Plotty] WorldGuard/WorldEdit not found. Exiting.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getCommand("plot").setExecutor(new PlottyExecutor(this));
		getCommand("plotty").setExecutor(new PlottyExecutor(this));
		pc = new PlotClearer(this);
		dm = new DataManager(this);
		plotSize = dm.config.plotSize;
		plotHeight = dm.config.plotHeight;
		base = Material.getMaterial(dm.config.baseBlock);
		surface = Material.getMaterial(dm.config.surfaceBlock);
		PlotRegion.init(this);
		try {
			langMan = new LangManager(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		lang = langMan.getLang();
		try {
			BukkitMetricsLite bml = new BukkitMetricsLite(this);
			bml.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getServer().getPluginManager().registerEvents(new Listener(){
			@SuppressWarnings("unused")
			@EventHandler(priority = EventPriority.NORMAL)
			public void onPlayerJoin(PlayerJoinEvent e){
				e.getPlayer().sendMessage("§1[Plotty] §bThis server is running §9§lPlotty v2 §b§obeta testing.§b No other server has this yet, so enjoy it while its private!");
				List<String> list = stringArrayToList(dm.config.playerGrantNotify);
				if(list.contains(e.getPlayer().getName())){
					if(dm.getPlayer(e.getPlayer().getName()).grantedPlots > 0){
						e.getPlayer().sendMessage("§1[Plotty] §bYou have §9"+dm.getPlayer(e.getPlayer().getName()).grantedPlots+" §aplots allocated to you. You can claim them with /plot claim or /plot new!");
					}
					list.remove(e.getPlayer().getName());
					String[] listArray = list.toArray(new String[list.size()]);
					dm.config.playerGrantNotify = listArray;
					dm.save();
				}
			}
		}, this);
	}
	public boolean getWorldGuard(){
		Plugin p = this.getServer().getPluginManager().getPlugin("WorldGuard");
		if(p == null || !(p instanceof WorldGuardPlugin)) return false;
		worldGuard = (WorldGuardPlugin)p;
		return true;
	}
	public boolean getWorldEdit(){
		Plugin p = this.getServer().getPluginManager().getPlugin("WorldEdit");
		if(p == null | !(p instanceof WorldEditPlugin)) return false;
		worldEdit = (WorldEditPlugin)p;
		return true;
	}
	public PlotClearer getPlotClearer(){
		return pc;
	}
	public DataManager getDataManager(){
		return dm;
	}
	public void telePlayer(Plot p, Player pl){
		if(getDataManager().config.centertp){
			int x = Math.round(p.getX()+(plotSize/2));
			int y = plotHeight+1;
			int z = Math.round(p.getZ()+(plotSize/2));
			pl.teleport(new Location(p.getWorld(),x,y,z));
		}else{
			int x = p.getX();
			int y = plotHeight;
			int z = p.getZ();
			pl.teleport(new Location(p.getWorld(),x,y,z));
		}
	}
	public int plotSize=64;
	public int plotHeight=20;
	public Material base=Material.STONE;
	public Material surface=Material.GRASS;
	private List<String> stringArrayToList(String[] array){
		List<String> list = new ArrayList<String>();
		for(String s : array){
			list.add(s);
		}
		return list;
	}
}
