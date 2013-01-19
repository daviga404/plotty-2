package com.daviga404.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import com.daviga404.Plotty;

public class LangManager {
	private Plotty plugin;
	private File file;
	private Lang language;
	private Gson gson;
	public LangManager(Plotty pl) throws Exception{
		this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		this.plugin = pl;
		File folder = plugin.getDataFolder();
		file = new File(folder+File.separator+"language.json");
		if(!folder.exists()){
			folder.mkdir();
		}
		if(!file.exists()){
			file.createNewFile();
			Lang lang = new Lang();
			lang.alreadyFriend = "&1[Plotty] &9This player is already a friend in your plot.";
			lang.createdPlot = "&a[Plotty] Plot created with ID &2%s";
			lang.dontOwn = "&4[Plotty] &cYou do not own this plot.";
			lang.friendAdded = "&a[Plotty] Friend added to plot!";
			lang.friendNotFound = "&4[Plotty] &cFriend not found.";
			lang.friendRemoved = "&a[Plotty] Friend removed from plot!";
			lang.madePublic = "&a[Plotty] Plot is now &2public.";
			lang.madePrivate = "&a[Plotty] Plot is now &2private.";
			lang.notFound = "&4[Plotty] &cPlot not found.";
			lang.notStandingInPlot = "&4[Plotty] &cYou are not standing in a plot.";
			lang.plotClaimed = "&a[Plotty] Plot claimed with ID &2%s.";
			lang.plotCleared = "&a[Plotty] Plot cleared!";
			lang.plotDeleted = "&a[Plotty] Plot deleted!";
			lang.plotFree = "&1[Plotty] &9This plot is free.";
			lang.plotHere = "&4[Plotty] &cThere is a plot here!";
			lang.reachedMaxPlots = "&4[Plotty] &cYou have reached your maximum number of plots. Type /plot del <id> to delete a plot.";
			lang.teleportedToPlot = "&1[Plotty] &9Teleported to plot (ID %s).";
			lang.mustBePlayer = "[Plotty] You must be a player to use Plotty's commands.";
			lang.mustBeInPlotsWorld = "&4[Plotty] &cYou must be in a plots world to do this!";
			lang.cantVote = "&4[Plotty] &cYou cannot vote for another %s minutes.";
			FileWriter fw = new FileWriter(file);
			fw.write(gson.toJson(lang,Lang.class));
			fw.flush();
			fw.close();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String ln,buff="";
		while((ln = br.readLine()) != null){
			buff += ln;
		}
		br.close();
		buff = buff.replaceAll("&", "§");
		language = gson.fromJson(buff, Lang.class);
	}
	public Lang getLang(){
		return language;
	}
}
