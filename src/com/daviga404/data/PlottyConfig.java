package com.daviga404.data;

import java.util.HashMap;
import java.util.Map;

public class PlottyConfig {
	public int plotSize;
	public int plotHeight;
	public int maxPlots;
	public int baseBlock;
	public int surfaceBlock;
	public int delCooldown;
	public boolean clearOnDelete;
	public boolean clearEnabled=false;
	public String[] worlds;
	public boolean centertp;
	public boolean publicByDefault;
	public boolean enableTnt;
	public double voteDelay=24.0;
	public String[] playerGrantNotify=new String[]{};
	public PlottyPlayer[] players;
	public boolean enableEco = false;
	public double plotCost = 0.0;
	public Map<String,String> flags = new HashMap<String,String>();
}
