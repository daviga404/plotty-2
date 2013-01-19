package com.daviga404.plots;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.daviga404.Plotty;

public class PlotDeleter{
	public static ArrayList<String> deletecooldown = new ArrayList<String>();
	public static void addCooldown(String player, Plotty plotty){
		if(!isCooling(player)){
			deletecooldown.add(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plotty, PlotDeleter.makeRunnable(player), plotty.dm.config.delCooldown*20);
		}
	}
	public static boolean isCooling(String player){
		return PlotDeleter.deletecooldown.contains(player);
	}
	public static Runnable makeRunnable(final String player){
		Runnable r = new Runnable(){

			public void run() {
				if(PlotDeleter.deletecooldown.contains(player)){
					PlotDeleter.deletecooldown.remove(player);
				}
			}
			
		};
		return r;
	}
}
