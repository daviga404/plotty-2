package com.daviga404.commands.admin;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.daviga404.Plotty;
import com.daviga404.commands.PlottyCommand;
import com.daviga404.data.PlottyConfig;

public class CommandPlotConfig extends PlottyCommand{
	private Plotty plugin;
	public CommandPlotConfig(Plotty pl){
		super(
		"config",
		"(config)((( )(\\w+))|(( )(\\w+)( )(\\w+))|(( )(worlds)( )(add)( )(\\w+))|(( )(worlds)( )(remove)( )(\\w+)))?", //config [[option] [value] worlds add <worldname>]
		"plotty.admin.config",
		"/plot config [option [value]|worlds add/remove <name>]",
		"Changes/views an option in config. (see documentation)",
		false
		);
		this.plugin = pl;
	}
	public boolean execute(Player p, String[] args){
		/*
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
	public PlottyPlayer[] players;
		 */
		PlottyConfig c = plugin.dm.config;
		if(args.length == 1){
			String op = args[0];
			op = op.toLowerCase();
			if(op.equalsIgnoreCase("plotsize")) return send("plot size",c.plotSize,p);
			if(op.equalsIgnoreCase("plotheight")) return send("plot height",c.plotHeight,p);
			if(op.equalsIgnoreCase("maxplots")) return send("max plots",c.maxPlots,p);
			if(op.equalsIgnoreCase("baseblock")) return send("base block id",c.baseBlock,p);
			if(op.equalsIgnoreCase("surfaceblock")) return send("surface block id",c.surfaceBlock,p);
			if(op.equalsIgnoreCase("delcooldown")) return send("deletion/clear cooldown",c.delCooldown,p);
			if(op.equalsIgnoreCase("clearondelete")) return send("clear on delete",c.clearOnDelete,p);
			if(op.equalsIgnoreCase("clearenabled")) return send("clear enabled",c.clearEnabled,p);
			if(op.equalsIgnoreCase("worlds")) return send("worlds",c.worlds,p);
			if(op.equalsIgnoreCase("centertp")) return send("teleport to center",c.centertp,p);
			if(op.equalsIgnoreCase("publicbydefault")) return send("public by default",c.publicByDefault,p);
			if(op.equalsIgnoreCase("enabletnt")) return send("enable tnt",c.enableTnt,p);
			if(op.equalsIgnoreCase("votedelay")) return send("vote delay",c.voteDelay,p);
			p.sendMessage("§4[Plotty] §cUnknown configuration value.");
		}else if(args.length == 2){
			System.out.println(args[0]+", "+args[1]);
			String op = args[0];
			op = op.toLowerCase();
			String newval = args[1];
			if(op.equalsIgnoreCase("plotsize")){
				try {
					int i = Integer.parseInt(newval);
					if(i <= 0){
						p.sendMessage("§4[Plotty] §cValue must be a positive number above 0.");
						return true;
					}
					c.plotSize = i;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("plotheight")){
				try {
					int i = Integer.parseInt(newval);
					if(i < 0){
						p.sendMessage("§4[Plotty] §cValue must be a positive number.");
						return true;
					}
					c.plotHeight = i;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("maxplots")){
				try {
					int i = Integer.parseInt(newval);
					if(i < 0){
						p.sendMessage("§4[Plotty] §cValue must be a positive number.");
						return true;
					}
					c.maxPlots = i;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("baseblock")){
				try {
					int b = Integer.parseInt(newval);
					if(Material.getMaterial(b) == null){
						p.sendMessage("§4[Plotty] §cID must exist.");
						return true;
					}
					c.baseBlock = b;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("surfaceblock")){
				try {
					int b = Integer.parseInt(newval);
					if(Material.getMaterial(b) == null){
						p.sendMessage("§4[Plotty] §cID must exist.");
						return true;
					}
					c.surfaceBlock = b;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("delcooldown")){
				try {
					int i = Integer.parseInt(newval);
					if(i < 0){
						p.sendMessage("§4[Plotty] §cValue must be a positive number.");
						return true;
					}
					c.delCooldown = i;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a number.");
					return true;
				}
			}else if(op.equalsIgnoreCase("clearondelete")){
				if(!(newval.equalsIgnoreCase("true") || newval.equalsIgnoreCase("false"))){
					p.sendMessage("§4[Plotty] §cValue must be 'true' or 'false'.");
					return true;
				}
				boolean newvalb = false;
				if(newval.equalsIgnoreCase("true")) newvalb = true;
				c.clearOnDelete = newvalb;
				plugin.dm.config = c;
				plugin.dm.save();
				p.sendMessage("§a[Plotty] Config option set!");
				return true;
			}else if(op.equalsIgnoreCase("clearenabled")){
				if(!(newval.equalsIgnoreCase("true") || newval.equalsIgnoreCase("false"))){
					p.sendMessage("§4[Plotty] §cValue must be 'true' or 'false'.");
					return true;
				}
				boolean newvalb = false;
				if(newval.equalsIgnoreCase("true")) newvalb = true;
				c.clearEnabled = newvalb;
				plugin.dm.config = c;
				plugin.dm.save();
				p.sendMessage("§a[Plotty] Config option set!");
				return true;
			}else if(op.equalsIgnoreCase("centertp")){
				if(!(newval.equalsIgnoreCase("true") || newval.equalsIgnoreCase("false"))){
					p.sendMessage("§4[Plotty] §cValue must be 'true' or 'false'.");
					return true;
				}
				boolean newvalb = false;
				if(newval.equalsIgnoreCase("true")) newvalb = true;
				c.centertp = newvalb;
				plugin.dm.config = c;
				plugin.dm.save();
				p.sendMessage("§a[Plotty] Config option set!");
				return true;
			}else if(op.equalsIgnoreCase("publicbydefault")){
				if(!(newval.equalsIgnoreCase("true") || newval.equalsIgnoreCase("false"))){
					p.sendMessage("§4[Plotty] §cValue must be 'true' or 'false'.");
					return true;
				}
				boolean newvalb = false;
				if(newval.equalsIgnoreCase("true")) newvalb = true;
				c.publicByDefault = newvalb;
				plugin.dm.config = c;
				plugin.dm.save();
				p.sendMessage("§a[Plotty] Config option set!");
				return true;
			}else if(op.equalsIgnoreCase("enabletnt")){
				if(!(newval.equalsIgnoreCase("true") || newval.equalsIgnoreCase("false"))){
					p.sendMessage("§4[Plotty] §cValue must be 'true' or 'false'.");
					return true;
				}
				boolean newvalb = false;
				if(newval.equalsIgnoreCase("true")) newvalb = true;
				c.enableTnt = newvalb;
				plugin.dm.config = c;
				plugin.dm.save();
				p.sendMessage("§a[Plotty] Config option set!");
				return true;
			}else if(op.equalsIgnoreCase("votedelay")){
				try {
					double d = Double.parseDouble(newval);
					if(d < 0.0){
						p.sendMessage("§4[Plotty] §cValue must be above 0.");
						return true;
					}
					c.voteDelay = d;
					plugin.dm.config = c;
					plugin.dm.save();
					p.sendMessage("§a[Plotty] Config option set!");
					return true;
				}catch(Exception e1){
					p.sendMessage("§4[Plotty] §cValue must be a decimal (e.g 5.0 or 6.1).");
					return true;
				}
			}else{
				p.sendMessage("§4[Plotty] §cConfig option not found.");
				return true;
			}
		}else if(args.length == 3 && args[0].equalsIgnoreCase("worlds") && args[1].equalsIgnoreCase("add")){
			if(plugin.getServer().getWorld(args[2]) == null){
				p.sendMessage("§4[Plotty] §cWorld not found.");
				return true;
			}
			for(String world : c.worlds){
				if(world.equalsIgnoreCase(args[2])){
					p.sendMessage("§4[Plotty] §cWorld is already in config.");
					return true;
				}
			}
			String[] newWorlds = new String[c.worlds.length+1];
			System.arraycopy(c.worlds, 0, newWorlds, 0, c.worlds.length);
			newWorlds[c.worlds.length] = args[2];
			c.worlds = newWorlds;
			plugin.dm.config = c;
			plugin.dm.save();
			p.sendMessage("§a[Plotty] World added to config.");
			return true;
		}else if(args.length == 3 && args[0].equalsIgnoreCase("worlds") && args[1].equalsIgnoreCase("remove")){
			ArrayList<String> newWorlds = new ArrayList<String>();
			boolean found = false;
			for(String world : c.worlds){
				if(!world.equalsIgnoreCase(args[2])){
					newWorlds.add(world);
				}else{
					found = true;
				}
			}
			if(!found){
				p.sendMessage("§4[Plotty] §cWorld not found in config.");
				return true;
			}
			String[] newWorldsArray = new String[newWorlds.size()];
			int i=0;
			for(String s : newWorlds){
				newWorldsArray[i] = s;
				i++;
			}
			c.worlds = newWorldsArray;
			plugin.dm.config = c;
			plugin.dm.save();
			p.sendMessage("§a[Plotty] World removed from config.");
			return true;
		}else{
			StringBuilder b = new StringBuilder();
			b.append("§1[Plotty] §9Config Values:\n");
			b.append("§9plotSize: §b");
			b.append(c.plotSize);
			b.append("\n§9plotHeight: §b");
			b.append(c.plotHeight);
			b.append("\n§9maxPlots: §b");
			b.append(c.maxPlots);
			b.append("\n§9baseBlock: §b");
			b.append(c.baseBlock);
			b.append("\n§9surfaceBlock: §b");
			b.append(c.surfaceBlock);
			b.append("\n§9delCooldown: §b");
			b.append(c.delCooldown+"s");
			b.append("\n§9clearOnDelete: §b");
			b.append(c.clearOnDelete ? "yes" : "no");
			b.append("\n§9clearEnabled: §b");
			b.append(c.clearEnabled ? "yes" : "no");
			b.append("\n§9worlds: §b");
			String s="";for(String ss:c.worlds){s+=ss+", ";}s = s.substring(0,s.length()-2);
			b.append(s);
			b.append("\n§9centertp: §b");
			b.append(c.centertp ? "yes" : "no");
			b.append("\n§9publicByDefault: §b");
			b.append(c.publicByDefault ? "yes" : "no");
			b.append("\n§9enableTnt: §b");
			b.append(c.enableTnt ? "yes" : "no");
			b.append("\n§9voteDelay: §b");
			b.append(c.voteDelay);
			p.sendMessage(b.toString());
		}
		return true;
	}
	public boolean send(String name, Object value,Player p){
		if(value instanceof String[]){
			String[] val = (String[])value;
			String newval="";
			for(String s : val){
				newval += s+", ";
			}
			newval = newval.substring(0,newval.length()-2);
			value = newval;
		}else if(value instanceof Boolean){
			if((Boolean)value){
				value = "yes";
			}else{
				value = "no";
			}
		}else if(value instanceof Integer){
			value = String.valueOf(value);
		}
		p.sendMessage("§1[Plotty] §9Current "+name+": §b"+((String)value));
		return true;
	}
}
