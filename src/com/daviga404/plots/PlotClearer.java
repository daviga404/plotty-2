package com.daviga404.plots;

import org.bukkit.Material;
import org.bukkit.World;

import com.daviga404.Plotty;

public class PlotClearer {
	private Plotty plugin;
	public PlotClearer(Plotty plugin){
		this.plugin = plugin;
	}
	public void clearPlot(Plot p){
		int x = p.getX();
		int z = p.getZ();
		World w = p.getWorld();
		int h = plugin.plotHeight;
		int s = plugin.plotSize;
		for(int ax=0;ax<s;ax++){
			for(int az=0;az<s;az++){
				for(int ay=0;ay<=256;ay++){
					Material type = plugin.base;
					if(ay == 0){
						type = Material.BEDROCK;
					}else if(ay == h){
						type = plugin.surface;
					}else if(ay > h){
						type = Material.AIR;
					}
					w.getBlockAt(x+ax,ay,z+az).setType(type);
				}
			}
		}
	}
}
