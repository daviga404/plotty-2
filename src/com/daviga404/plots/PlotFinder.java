package com.daviga404.plots;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

import com.daviga404.Plotty;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class PlotFinder {
	public static Plot findPlot(World w, Plotty pl){ //THIS ALGORITHM FINALLY WORKS! Now to do other stuff *groan*
		int size=pl.plotSize;
		int height=pl.plotHeight;
		int radius=1;
		boolean found=false;
		Plot plot = null;
		RegionManager rm = pl.worldGuard.getRegionManager(w);
		while(!found){
			int x = 4;
			int z = 4;
			x = x - ((size+7)*(radius-((radius+1)/2))); //e.g Radius 1: x = 4-(71x0) = 4, Radius 2: x = 4-(71x1) = -67
			z = z - ((size+7)*(radius-((radius+1)/2)));
			int cx = 0; //current x (will be multiplied)
			int cy = 0; //current y (will be multiplied)
			int ccx = x;
			int ccy = z;
			for(cx=0;cx<radius;cx++){
				for(cy=0;cy<radius;cy++){
					if(!found){
						int cxm = cx * (size+7);
						int cym = cy * (size+7);
						ccx = x + cxm;
						ccy = z + cym;
						boolean prot = rm.getApplicableRegions(new Location(w,ccx,height,ccy)).size() > 0;
						if(!prot){
							found = true;
							plot = new Plot(ccx,height+1,ccy,w);
							break; //breaks the for.
						}
					}
				}
			}
			if(!found){
				radius += 2;
			}else{
				break; //breaks the while.
			}
		}
		if(found){
			return plot;
		}else{
			return null;
		}
	}
	public static Integer[] getCornerFromCoords(int x, int z, int size){
		//X
		ArrayList<Integer> cornersx = new ArrayList<Integer>();
		for(int i=x-(size*2);i<x+(size*2);i++){
			if((i-4) % (size+7) == 0){
				cornersx.add(i);
			}
		}
		ArrayList<Integer> inboundsx = new ArrayList<Integer>();
		for(Integer i : cornersx){
			if(x >= i && x <= i+size){
				inboundsx.add(i);
			}
		}
		//Z
		ArrayList<Integer> cornersz = new ArrayList<Integer>();
		for(int i=z-(size*2);i<z+(size*2);i++){
			if((i-4) % (size+7) == 0){
				cornersz.add(i);
			}
		}
		ArrayList<Integer> inboundsz = new ArrayList<Integer>();
		for(Integer i : cornersz){
			if(z >= i && z <= i+size){
				inboundsz.add(i);
			}
		}
		if(inboundsx.size() == 0 || inboundsz.size() == 0){
			return new Integer[]{};
		}else if(inboundsx.size() > 1 || inboundsz.size() > 1){
			return new Integer[]{};
		}else if(inboundsx.size() == 1 && inboundsz.size() == 1){
			return new Integer[]{inboundsx.get(0),inboundsz.get(0)};
		}else{
			return new Integer[]{};
		}
	}
}
