package me.ftbastler.BukkitGames;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class BGSign extends JavaPlugin{

	@SuppressWarnings("unused")
	private BGMain plugin;
	
	public ArrayList<Sign> signs = new ArrayList<Sign>();
	
	public BGSign(BGMain plugin) {
		
		this.plugin = plugin;
	}
	
	public void createSign (Location loc, String firstLine, String secondLine, String thirdLine, String fourthLine) {
		
		String fline = firstLine;
		String sline = secondLine;
		String tline = thirdLine;
		String foline = fourthLine;
		
		if(fline != null && fline.length() > 15) {
			fline = fline.substring(0, 15);
		}
		if(sline != null && sline.length() > 15) {
			sline = sline.substring(0, 15);
		}
		if(tline != null && tline.length() > 15) {
			tline = tline.substring(0, 15);
		}
		if(foline != null && foline.length() > 15) {
			foline = foline.substring(0, 15);
		}
		
		Block block = Bukkit.getWorld("world").getBlockAt(loc);
		block.setType(Material.SIGN_POST);
		Sign sign = (Sign) block.getState();
		
		if(fline != null)
			sign.setLine(0, fline);
		if(sline != null)
			sign.setLine(1, sline);
		if(tline != null)	
			sign.setLine(2, tline);
		if(foline != null)
			sign.setLine(3, foline);
		
		sign.update(true);
		
		signs.add(sign);
	}
}
