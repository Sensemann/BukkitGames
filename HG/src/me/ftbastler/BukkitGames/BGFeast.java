package me.ftbastler.BukkitGames;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BGFeast {

	private BGMain plugin;
	private Block mainBlock = null;
	private Integer radius = 8;
	private Logger log = Logger.getLogger("Minecraft");
	
	public BGFeast(BGMain plugin) {	
		this.plugin = plugin;
	}
	
	public void announceFeast(Integer time) {
		if(mainBlock == null) {
			do {
				mainBlock = BGMain.getRandomLocation().getBlock();
			} while (!plugin.inBorder(mainBlock.getLocation()));
			mainBlock.setType(Material.NETHERRACK);
			createFeast(Material.SOUL_SAND);
		}
		
		String s = "";
		if(time > 1)
			s = "s";
		
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Feast will spawn in " + time + " minute" + s + " at X: " + df.format(mainBlock.getLocation().getX()) + " | Y: " + df.format(mainBlock.getLocation().getY()) + " | Z: " + df.format(mainBlock.getLocation().getZ()));
	}
	
	public void spawnFeast() {
		DecimalFormat df = new DecimalFormat("##.#");
		BGChat.printInfoChat("Feast spawned at X: " + df.format(mainBlock.getLocation().getX()) + " | Y: " + df.format(mainBlock.getLocation().getY()) + " | Z: " + df.format(mainBlock.getLocation().getZ()));
		
		List<String> items = BGFiles.cornconf.getStringList("ITEMS");
		for(String item : items) {
			String[] oneitem = item.split(",");
			ItemStack i = null;
			Random r = new Random();
			String itemid = oneitem[0];
			Integer minamount = Integer.parseInt(oneitem[1]);
			Integer maxamount = Integer.parseInt(oneitem[2]);
			Boolean force = Boolean.parseBoolean(oneitem[3]);
			Boolean spawn = force;
			Integer id = null;
			Short durability = null;
			
			if (item.contains(":")) {
				String[] it = itemid.split(":");
				id = Integer.parseInt(it[0]);
				durability = Short.parseShort(it[1]);
			} else {
				id = Integer.parseInt(itemid);
			}
			
			if(minamount == maxamount)
				i = new ItemStack(id, maxamount);
			else
				i = new ItemStack(id, minamount + r.nextInt(maxamount - minamount));
			
			if(durability != null)
				i.setDurability(durability);
			
			if(oneitem.length == 6) {
				i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem[4])), Integer.parseInt(oneitem[5]));
			}
			
			if(!force)
				spawn = r.nextBoolean();
			
			if(!spawn)
				continue;
			
			Integer ra = radius;
			Location c = mainBlock.getLocation();
			c.add(-(ra/2) + r.nextInt(ra), 1, -(ra/2) + r.nextInt(ra));
			
			while(c.getBlock().getType() == Material.CHEST) {
				c = mainBlock.getLocation();
				c.add(-8 + r.nextInt(16), 1, -8 + r.nextInt(16));
			}
			
			c.getBlock().setType(Material.CHEST);
			Chest chest = (Chest) c.getBlock().getState();
            
			chest.getInventory().addItem(i);
			chest.update();
		}
	}
	
	private void createFeast(Material m) {
		Location loc = mainBlock.getLocation();
		Integer r = radius;
	               
		log.info("[BukkitGames] Generating feast.");	    
	    
	    for (double x = -r; x <= r; x++) {
	        for (double z = -r; z <= r; z++) {
	        	Location l = new Location(Bukkit.getServer().getWorld("world"), loc.getX() + x, loc.getY(), loc.getZ() + z);
	        	if(l.distance(loc) <= r && l.getBlock().getType() != Material.NETHERRACK) {
	        		removeAbove(l.getBlock());
	        		l.getBlock().setType(m);
	        	}
	        }
	    }
	}
	
	public static void removeAbove(Block block) {
		Location loc = block.getLocation();
		loc.setY(loc.getY()+1);
		Block newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		while(newBlock.getType() != Material.AIR) {
			newBlock.setType(Material.AIR);
			loc.setY(loc.getY()+1);
			newBlock = Bukkit.getServer().getWorld("world").getBlockAt(loc);
		}
	}
	
}
