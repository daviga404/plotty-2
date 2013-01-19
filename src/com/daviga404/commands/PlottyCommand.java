package com.daviga404.commands;

import org.bukkit.entity.Player;

public abstract class PlottyCommand {
	String base,regex,permission,usage,description;
	public PlottyCommand(String base, String regex, String permission, String usage, String description){
		this.base = base;
		this.regex = regex;
		this.permission = permission;
		this.usage = usage;
		this.description = description;
	}
	public abstract boolean execute(Player p, String[] args);
}
