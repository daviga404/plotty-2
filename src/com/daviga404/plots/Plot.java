package com.daviga404.plots;

import org.bukkit.World;

public class Plot {
	private int x,y,z;
	private World w;
	public Plot(int x, int y, int z,World w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public World getWorld(){
		return w;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
}
