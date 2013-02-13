package com.daviga404.plots;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.daviga404.Plotty;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class PlotRegion {
	public static Plotty plugin;
	public static void init(Plotty pl){
		PlotRegion.plugin = pl;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void makePlotRegion(Plot p,String owner,int id){
		String name = "plot_"+owner.toLowerCase()+"_"+id;
		BlockVector point1 = new BlockVector(p.getX(),0,p.getZ());
		BlockVector point2 = new BlockVector(p.getX()+plugin.plotSize,256,p.getZ()+plugin.plotSize);
		RegionManager rm = plugin.worldGuard.getRegionManager(p.getWorld());
		ProtectedCuboidRegion pcr = new ProtectedCuboidRegion(name, point1,point2);
		pcr.getOwners().addPlayer(owner);
		if(!plugin.getDataManager().config.enableTnt){
			pcr.setFlag(DefaultFlag.TNT,State.DENY);
		}
		CommandSender s = (CommandSender)Bukkit.getPlayer(owner);
		for(Flag<?> flag : DefaultFlag.getFlags()){
			if(plugin.getDataManager().config.flags.containsKey(flag.getName())){
				try {
					Object obj = flag.parseInput(plugin.worldGuard, s, plugin.getDataManager().config.flags.get(flag.getName()));
					Flag f = (Flag)flag;
					pcr.setFlag(f, obj);
				}catch(Exception e1){
					s.sendMessage("§4[Plotty] §cError in config: custom WorldGuard flag has incorrect value.");
				}
			}
		}
		rm.addRegion(pcr);
		try {
			rm.save();
		} catch (ProtectionDatabaseException e) {
			e.printStackTrace();
		}
	}
	public static boolean addFriend(int id, String owner, String friend, World w){
		String name = "plot_"+owner.toLowerCase()+"_"+id;
		RegionManager rm = plugin.worldGuard.getRegionManager(w);
		if(rm.getRegion(name) != null){
			rm.getRegion(name).getOwners().addPlayer(friend);
			try {
				rm.save();
			} catch (ProtectionDatabaseException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}
	}
	public static boolean removeFriend(int id, String owner, String friend, World w){
		String name = "plot_"+owner.toLowerCase()+"_"+id;
		RegionManager rm = plugin.worldGuard.getRegionManager(w);
		if(rm.getRegion(name) != null){
			if(rm.getRegion(name).getOwners().contains(friend)){
				rm.getRegion(name).getOwners().removePlayer(friend);
			}
			try {
				rm.save();
			} catch (ProtectionDatabaseException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}
	}
}
