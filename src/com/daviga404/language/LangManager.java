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
	private Lang defaultLang;
	public LangManager(Plotty pl) throws Exception{
		defaultLang = new Lang();
		defaultLang.alreadyFriend = "&1[Plotty] &9This player is already a friend in your plot.";
		defaultLang.createdPlot = "&a[Plotty] Plot created with ID &2%s";
		defaultLang.dontOwn = "&4[Plotty] &cYou do not own this plot.";
		defaultLang.friendAdded = "&a[Plotty] Friend added to plot!";
		defaultLang.friendNotFound = "&4[Plotty] &cFriend not found.";
		defaultLang.friendRemoved = "&a[Plotty] Friend removed from plot!";
		defaultLang.madePublic = "&a[Plotty] Plot is now &2public.";
		defaultLang.madePrivate = "&a[Plotty] Plot is now &2private.";
		defaultLang.notFound = "&4[Plotty] &cPlot not found.";
		defaultLang.notStandingInPlot = "&4[Plotty] &cYou are not standing in a plot.";
		defaultLang.plotClaimed = "&a[Plotty] Plot claimed with ID &2%s.";
		defaultLang.plotCleared = "&a[Plotty] Plot cleared!";
		defaultLang.plotDeleted = "&a[Plotty] Plot deleted!";
		defaultLang.plotFree = "&1[Plotty] &9This plot is free.";
		defaultLang.plotHere = "&4[Plotty] &cThere is a plot here!";
		defaultLang.reachedMaxPlots = "&4[Plotty] &cYou have reached your maximum number of plots. Type /plot del <id> to delete a plot.";
		defaultLang.teleportedToPlot = "&1[Plotty] &9Teleported to plot (ID %s).";
		defaultLang.mustBePlayer = "[Plotty] You must be a player to use Plotty's commands.";
		defaultLang.mustBeInPlotsWorld = "&4[Plotty] &cYou must be in a plots world to do this!";
		defaultLang.cantVote = "&4[Plotty] &cYou cannot vote for another %s minutes.";
		defaultLang.noMoney = "&4[Plotty] &cYou do not have enough money to buy a plot!";
		defaultLang.moneyTaken = "&a[Plotty] Plot purchased for %s!";
		this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		this.plugin = pl;
		File folder = plugin.getDataFolder();
		file = new File(folder+File.separator+"language.json");
		if(!folder.exists()){
			folder.mkdir();
		}
		if(!file.exists()){
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			fw.write(gson.toJson(defaultLang,Lang.class));
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
		checkDefaults();
	}
	public void checkDefaults(){
		language.alreadyFriend = language.alreadyFriend == null ? defaultLang.alreadyFriend : language.alreadyFriend;
		language.cantVote = language.cantVote == null ? defaultLang.cantVote : language.cantVote;
		language.createdPlot = language.createdPlot == null ? defaultLang.createdPlot : language.createdPlot;
		language.dontOwn = language.dontOwn == null ? defaultLang.dontOwn : language.dontOwn;
		language.friendAdded = language.friendAdded == null ? defaultLang.friendAdded : language.friendAdded;
		language.friendNotFound = language.friendNotFound == null ? defaultLang.friendNotFound : language.friendNotFound;
		language.friendRemoved = language.friendRemoved == null ? defaultLang.friendRemoved : language.friendRemoved;
		language.madePrivate = language.madePrivate == null ? defaultLang.madePrivate : language.madePrivate;
		language.madePublic = language.madePublic == null ? defaultLang.madePublic : language.madePublic;
		language.moneyTaken = language.moneyTaken == null ? defaultLang.moneyTaken : language.moneyTaken;
		language.mustBeInPlotsWorld = language.mustBeInPlotsWorld == null ? defaultLang.mustBeInPlotsWorld : language.mustBeInPlotsWorld;
		language.mustBePlayer = language.mustBePlayer == null ? defaultLang.mustBePlayer : language.mustBePlayer;
		language.noMoney = language.noMoney == null ? defaultLang.noMoney : language.noMoney;
		language.notFound = language.notFound == null ? defaultLang.notFound : language.notFound;
		language.notStandingInPlot = language.notStandingInPlot == null ? defaultLang.notStandingInPlot : language.notStandingInPlot;
		language.plotClaimed = language.plotClaimed == null ? defaultLang.plotClaimed : language.plotClaimed;
		language.plotCleared = language.plotCleared == null ? defaultLang.plotCleared : language.plotCleared;
		language.plotDeleted = language.plotDeleted == null ? defaultLang.plotDeleted : language.plotDeleted;
		language.plotFree = language.plotFree == null ? defaultLang.plotFree : language.plotFree;
		language.plotHere = language.plotHere == null ? defaultLang.plotHere : language.plotHere;
		language.reachedMaxPlots = language.reachedMaxPlots == null ? defaultLang.reachedMaxPlots : language.reachedMaxPlots;
		language.teleportedToPlot = language.teleportedToPlot == null ? defaultLang.teleportedToPlot : language.teleportedToPlot;
		language.alreadyFriend = language.alreadyFriend.replaceAll("§","&");
		language.cantVote = language.cantVote.replaceAll("§","&");
		language.createdPlot = language.createdPlot.replaceAll("§","&");
		language.dontOwn = language.dontOwn.replaceAll("§","&");
		language.friendAdded = language.friendAdded.replaceAll("§","&");
		language.friendNotFound = language.friendNotFound.replaceAll("§","&");
		language.friendRemoved = language.friendRemoved.replaceAll("§","&");
		language.madePrivate = language.madePrivate.replaceAll("§","&");
		language.madePublic = language.madePublic.replaceAll("§","&");
		language.moneyTaken = language.moneyTaken.replaceAll("§","&");
		language.mustBeInPlotsWorld = language.mustBeInPlotsWorld.replaceAll("§","&");
		language.mustBePlayer = language.mustBePlayer.replaceAll("§","&");
		language.noMoney = language.noMoney.replaceAll("§","&");
		language.notFound = language.notFound.replaceAll("§","&");
		language.notStandingInPlot = language.notStandingInPlot.replaceAll("§","&");
		language.plotClaimed = language.plotClaimed.replaceAll("§","&");
		language.plotCleared = language.plotCleared.replaceAll("§","&");
		language.plotDeleted = language.plotDeleted.replaceAll("§","&");
		language.plotFree = language.plotFree.replaceAll("§","&");
		language.plotHere = language.plotHere.replaceAll("§","&");
		language.reachedMaxPlots = language.reachedMaxPlots.replaceAll("§","&");
		language.teleportedToPlot = language.teleportedToPlot.replaceAll("§","&");
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(gson.toJson(language,Lang.class));
			fw.flush();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		language.alreadyFriend = language.alreadyFriend.replaceAll("&","§");
		language.cantVote = language.cantVote.replaceAll("&","§");
		language.createdPlot = language.createdPlot.replaceAll("&","§");
		language.dontOwn = language.dontOwn.replaceAll("&","§");
		language.friendAdded = language.friendAdded.replaceAll("&","§");
		language.friendNotFound = language.friendNotFound.replaceAll("&","§");
		language.friendRemoved = language.friendRemoved.replaceAll("&","§");
		language.madePrivate = language.madePrivate.replaceAll("&","§");
		language.madePublic = language.madePublic.replaceAll("&","§");
		language.moneyTaken = language.moneyTaken.replaceAll("&","§");
		language.mustBeInPlotsWorld = language.mustBeInPlotsWorld.replaceAll("&","§");
		language.mustBePlayer = language.mustBePlayer.replaceAll("&","§");
		language.noMoney = language.noMoney.replaceAll("&","§");
		language.notFound = language.notFound.replaceAll("&","§");
		language.notStandingInPlot = language.notStandingInPlot.replaceAll("&","§");
		language.plotClaimed = language.plotClaimed.replaceAll("&","§");
		language.plotCleared = language.plotCleared.replaceAll("&","§");
		language.plotDeleted = language.plotDeleted.replaceAll("&","§");
		language.plotFree = language.plotFree.replaceAll("&","§");
		language.plotHere = language.plotHere.replaceAll("&","§");
		language.reachedMaxPlots = language.reachedMaxPlots.replaceAll("&","§");
		language.teleportedToPlot = language.teleportedToPlot.replaceAll("&","§");
	}
	public Lang getLang(){
		return language;
	}
}
