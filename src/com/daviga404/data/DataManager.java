package com.daviga404.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import com.daviga404.Plotty;
import com.daviga404.plots.Plot;

public class DataManager {
	public File file;
	public Gson gson;
	public Plotty plugin;
	public PlottyConfig config;
	public DataManager(Plotty plugin){
		this.plugin = plugin;
		try {
			checkForFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Checks if the plots file exists and loads data into class.
	 * @throws IOException
	 */
	public void checkForFile() throws IOException{
		if(!plugin.getDataFolder().exists()){
			plugin.getDataFolder().mkdir();
		}
		file = new File(plugin.getDataFolder()+File.separator+"plots.json");
		gson = new GsonBuilder().setPrettyPrinting().create();
		if(!file.exists()){
			file.createNewFile();
			Bukkit.getLogger().info("Creating default plots file...");
			String defaultJson = "{\"plotSize\":64,\"plotHeight\":20,\"maxPlots\":5,\"baseBlock\":1,\"surfaceBlock\":2,\"delCooldown\":30,\"clearOnDelete\":true,\"publicByDefault\":false,\"worlds\":[\"Creative\"],\"centertp\":true,\"players\":[]}";
			FileWriter out = new FileWriter(file);
			config = gson.fromJson(defaultJson, PlottyConfig.class);
			out.write(gson.toJson(config));
			out.flush();
			Bukkit.getLogger().info("Created default plots file (plugins/Plotty/plots.json)");
		}else{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String ln,buff="";
			while((ln = br.readLine()) != null){
				buff += ln;
			}
			config = gson.fromJson(buff, PlottyConfig.class);
			Bukkit.getLogger().info("Plotty data loaded.");
		}
	}
	/**
	 * Adds a plot to the config file and saves.
	 * @param p The plot to add
	 * @param owner The name of the owner of the plot
	 * @param id The ID of the plot
	 * @see getLatestId();
	 */
	public void addPlot(Plot p, String owner, int id){
		PlottyPlayer player = getPlayer(owner);///////////////
		PlottyPlot plot = new PlottyPlot();
		plot.friends = new String[]{};
		plot.id = id;
		plot.world = p.getWorld().getName();
		plot.x = p.getX();
		plot.z = p.getZ();
		plot.visible = config.publicByDefault;
		player.plots = pushPlottyPlot(player.plots, plot);
		
		config.players[pIndex(owner)] = player;
		
		save();
	}
	/**
	 * Removes a plot from the config file and saves.
	 * @param id The ID of the plot
	 * @param owner The name of the owner
	 * @return Whether the task succeeded.
	 */
	public boolean removePlot(int id, String owner){
		PlottyPlayer p = getPlayer(owner);
		if(p == null) return false;
		PlottyPlot[] newArray = new PlottyPlot[p.plots.length-1];
		int i=0;
		for(PlottyPlot plot : p.plots){
			if(plot.id != id){
				newArray[i] = plot;
				i++;
			}
		}
		p.plots = newArray;
		if(pIndex(p.name) == -1) return false;
		config.players[pIndex(p.name)] = p;
		save();
		return true;
	}
	/**
	 * Adds a friend to a plot in the config and saves.
	 * @param p The plot to add a friend to
	 * @param owner The name of the owner of the plot
	 * @param friend The friend to add
	 */
	public void addFriend(PlottyPlot p, String owner, String friend){
		p.friends = pushString(p.friends, friend);
		PlottyPlayer player = getPlayer(owner);
		player.plots[plotIndex(p.id, player)] = p;
		config.players[pIndex(owner)] = player;
		save();
	}
	/**
	 * Removes a friend from a plot in the config and saves.
	 * @param p The plot to remove a friend from
	 * @param owner The name of the plot owner
	 * @param friend The name of the friend to remove.
	 */
	public void removeFriend(PlottyPlot p, String owner, String friend){
		boolean exists=false;
		for(String cf : p.friends){
			if(friend.equalsIgnoreCase(cf)){
				exists = true;
			}
		}
		if(!exists){return;}
		String[] newFriends = new String[p.friends.length-1];
		int added=0;
		for(String cfriend : p.friends){
			if(!friend.equalsIgnoreCase(cfriend)){
				newFriends[added] = cfriend;
				added++;
			}
		}
		p.friends = newFriends;
		PlottyPlayer player = getPlayer(owner);
		player.plots[plotIndex(p.id, player)] = p;
		config.players[pIndex(owner)] = player; //Don't you just love fixed size arrays... T_T
		save();
	}
	/**
	 * Gets a plot object from coordinates of corner.
	 * @param x The X location of the plot corner.
	 * @param z The Z location of the plot corner.
	 * @return A plot object (null if not found)
	 */
	public PlottyPlot getPlotFromCoords(int x,int z){
		for(PlottyPlayer p : config.players){
			for(PlottyPlot pl : p.plots){
				if(pl.x == x && pl.z == z){
					return pl;
				}
			}
		}
		return null;
	}
	/**
	 * Gets a plot object from an ID.
	 * @param id The ID of the plot.
	 * @return
	 */
	public PlottyPlot getPlotFromId(int id){
		for(PlottyPlayer p : config.players){
			for(PlottyPlot pl : p.plots){
				if(pl.id == id){
					return pl;
				}
			}
		}
		return null;
	}
	/**
	 * Gets the owner of a plot from the plot object.
	 * @param pl The plot to find the owner of.
	 * @return The name of the plot owner (null if not found)
	 */
	public String getPlotOwner(PlottyPlot pl){
		for(PlottyPlayer p : config.players){
			for(PlottyPlot pp : p.plots){
				if(pp.id == pl.id){
					return p.name;
				}
			}
		}
		return null;
	}
	/**
	 * Gets the index of a plot in a player's config file (used to replace plots with new info)
	 * @param id The ID of the plot.
	 * @param p The player file of the owner of the plot.
	 * @return The index of the plot in a player's file. Returns -1 if not found.
	 */
	public int plotIndex(int id, PlottyPlayer p){
		int i=0;
		for(PlottyPlot pl : p.plots){
			if(pl.id == id){
				return i;
			}
			i++;
		}
		return -1;
	}
	/**
	 * Gets the index of a player in the config file (used to replace player files with new info)
	 * @param p The name of the player (case insensitive)
	 * @return The index of a player in the config file. Returns -1 if not found.
	 */
	public int pIndex(String p){
		int i=0;
		for(PlottyPlayer pl : config.players){
			if(pl.name.equalsIgnoreCase(p)){
				return i;
			}
			i++;
		}
		return -1;
	}
	/**
	 * Gets if the player has reached their maximum number of plots.
	 * @param name The name of the player.
	 * @return A boolean that is true if the player has reached their maximum number of plots.
	 */
	public boolean pExceededMaxPlots(String name){
		int count = getPlayer(name).plots.length;
		int max = getPlayer(name).maxPlots == -1 ? config.maxPlots : getPlayer(name).maxPlots;
		return count >= max;
	}
	
	//Utils
	public PlottyPlot[] pushPlottyPlot(PlottyPlot[] array, PlottyPlot object){
		PlottyPlot[] newArray = new PlottyPlot[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = object;
		return newArray;
	}
	public PlottyPlayer[] pushPlottyPlayer(PlottyPlayer[] array, PlottyPlayer object){
		PlottyPlayer[] newArray = new PlottyPlayer[array.length+1];
		System.arraycopy(array,0,newArray,0,array.length);
		newArray[newArray.length-1] = object;
		return newArray;
	}
	public String[] pushString(String[] array, String object){
		String[] newArray = new String[array.length+1];
		System.arraycopy(array,0,newArray,0,array.length);
		newArray[newArray.length-1] = object;
		return newArray;
	}
	public PlottyPlayer getPlayer(String name){
		for(PlottyPlayer p : config.players){
			if(p.name.equalsIgnoreCase(name)){
				return p;
			}
		}
		PlottyPlayer p = new PlottyPlayer();
		p.maxPlots = -1;
		p.name = name;
		p.plots = new PlottyPlot[]{};
		config.players = pushPlottyPlayer(config.players,p);
		save();
		return p;
	}
	public void save(){
		try {
		FileWriter out = new FileWriter(file);
		out.write(gson.toJson(config));
		out.flush();
		}catch(Exception e){
			Bukkit.getLogger().severe("IOException: "+e.getMessage()+" ("+e.toString()+")");
		}
	}
	public int getLatestId(){
		int greatest=-1;
		for(PlottyPlayer p : config.players){
			for(PlottyPlot pl : p.plots){
				if(pl.id > greatest){
					greatest = pl.id;
				}
			}
		}
		greatest++;
		return greatest;
	}
}
