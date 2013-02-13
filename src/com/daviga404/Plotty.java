package com.daviga404;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.BukkitMetricsLite;

import com.daviga404.commands.PlottyExecutor;
import com.daviga404.data.DataManager;
import com.daviga404.data.PlottyPlayer;
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
	public Economy eco;
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
		initVault();
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
	public void initVault(){
		try {
			if(Class.forName("net.milkbowl.vault.economy.Economy") != null){
				RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
				if(economyProvider != null){
					eco = economyProvider.getProvider();
				}else{
					eco = null;
					getServer().getLogger().warning("[Plotty] Vault not detected. Economy features are disabled.");
				}
			}else{
				getServer().getLogger().warning("[Plotty] Vault not detected. Economy features are disabled.");
			}
		} catch (ClassNotFoundException e) {
			getServer().getLogger().warning("[Plotty] Vault not detected. Economy features are disabled.");
		}
	}
	public String makePlot(int id, int x, int y, int z, World w, Player p,boolean claiming){
		boolean canMake = false;
		if(dm.getPlayer(p.getName()).grantedPlots > 0){
			PlottyPlayer pl = dm.getPlayer(p.getName());
			pl.grantedPlots--;
			dm.config.players[dm.pIndex(p.getName())] = pl;
			dm.save();
			canMake = true;
		}else if(!dm.pExceededMaxPlots(p.getName())){
			canMake = true;
		}
		if(!canMake){
			return lang.reachedMaxPlots;
		}
		boolean usedEco = false;
		if(eco != null && dm.config.enableEco && canMake){
			if(eco.has(p.getName(), dm.config.plotCost)){
				usedEco = true;
				eco.withdrawPlayer(p.getName(), dm.config.plotCost);
			}else{
				return lang.noMoney;
			}
		}
		Plot legacy = new Plot(x,y,z,w);
		PlotRegion.makePlotRegion(legacy, p.getName(), id);
		dm.addPlot(legacy, p.getName(), id);
		telePlayer(legacy, p);
		String msg = "";
		msg = claiming ? lang.plotClaimed : lang.createdPlot;
		msg += usedEco ? "\n"+lang.moneyTaken.replace("%s",eco.format(dm.config.plotCost)) : "";
		msg = msg.replaceAll("%s",id+"");
		return msg;
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
		int y=0;
		if(getDataManager().config.centertp){
			int x = Math.round(p.getX()+(plotSize/2));
			int z = Math.round(p.getZ()+(plotSize/2));
			int fy=0; //final y
			for(y=0;y<256;y++){
				if(new Location(p.getWorld(),x,y,z).getBlock().getType().isSolid()){
					fy = y;
				}
			}
			fy++;
			pl.teleport(new Location(p.getWorld(),x,fy,z));
		}else{
			int x = p.getX();
			int z = p.getZ();
			int fy=0; //final y
			for(y=0;y<256;y++){
				if(new Location(p.getWorld(),x,y,z).getBlock().getType().isSolid()){
					fy = y;
				}
			}
			fy++;
			pl.teleport(new Location(p.getWorld(),x,fy,z));
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
