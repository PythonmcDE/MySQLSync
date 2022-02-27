package net.craftersland.bridge.inventory.events;

import de.Herbystar.TTA.TTA_Methods;
import net.craftersland.bridge.inventory.Inv;
import net.craftersland.bridge.inventory.InventoryDataHandler;
import net.craftersland.bridge.inventory.objects.InventorySyncData;
import net.craftersland.bridge.inventory.objects.LocationManager;
import net.craftersland.bridge.inventory.objects.SyncCompleteTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayerJoin implements Listener {
	
	private Inv inv;
	public static LinkedList<Player> invsync = new LinkedList<>();
	
	public PlayerJoin(Inv inv) {
		this.inv = inv;
	}

    public static void setArmorNull(Player p){
        if(p.getInventory().getHelmet() != null){
            p.getInventory().setHelmet(new ItemStack(Material.AIR));
        }
        if(p.getInventory().getChestplate() != null){
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
        }
        if(p.getInventory().getLeggings() != null){
            p.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
        if(p.getInventory().getBoots() != null){
            p.getInventory().setBoots(new ItemStack(Material.AIR));
        }
    }

	@EventHandler
	public void onLogin(final PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		invsync.add(p);
		TTA_Methods.sendActionBar(p, "§8» §7warte auf §6Inventar Sync§7...", 10);
		final InventorySyncData syncData = new InventorySyncData();
		InventoryDataHandler.backupAndReset(p, syncData);
		if (Inv.isDisabling == false) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(inv, new Runnable() {

				@Override
				public void run() {
					if (p != null) {
						if (p.isOnline() == true) {
							inv.getInventoryDataHandler().onJoinFunction(p);
							new SyncCompleteTask(inv, System.currentTimeMillis(), p).runTaskTimerAsynchronously(inv, 5L, 20L);
							for(int i = 0; i<= 35; i++){
								if(event.getPlayer().getInventory().getItem(i) != null){
									if(event.getPlayer().getInventory().getItem(i).getItemMeta() != null){
										if(event.getPlayer().getInventory().getItem(i).getType() == Material.DRAGON_EGG){
											ItemStack item = event.getPlayer().getInventory().getItem(i);
											ItemMeta im = item.getItemMeta();
											im.addEnchant(Enchantment.ARROW_INFINITE, 10, false);
											item.setItemMeta(im);
										}
									}
								}
							}
						}
					}
				}
				
			}, 5L);
		}
		new BukkitRunnable(){
			@Override
			public void run() {
				if(invsync != null){
					if(invsync.contains(p)){
						if(p.getLocation().getX() != new LocationManager("Spawn").getLocation().getX()) {
							if(p.getLocation().getZ() != new LocationManager("Spawn").getLocation().getZ()) {
								p.teleport(new LocationManager("Spawn").getLocation());
								TTA_Methods.sendActionBar(p, "§8» §7warte auf §6Inventar Sync§7...", 1);
							}
						}
					}else {
						this.cancel();
					}
				}
			}
		}.runTaskTimer(Inv.getInstance(), 1, 1);
	}

}
