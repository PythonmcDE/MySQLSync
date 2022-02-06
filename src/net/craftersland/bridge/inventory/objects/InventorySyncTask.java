package net.craftersland.bridge.inventory.objects;

import com.comphenix.protocol.PacketType;
import de.Herbystar.TTA.TTA_Methods;
import net.craftersland.bridge.inventory.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.craftersland.bridge.inventory.Inv;

public class InventorySyncTask extends BukkitRunnable {
	
	private Inv pd;
	private long startTime;
	private Player p;
	private boolean inProgress = false;
	private InventorySyncData syncD; 
	
	public InventorySyncTask(Inv pd, long start, Player player, InventorySyncData syncData) {
		this.pd = pd;
		this.startTime = start;
		this.p = player;
		this.syncD = syncData;
	}

	@Override
	public void run() {
		if (inProgress == false) {
			if (p != null) {
				if (p.isOnline() == true) {
					inProgress = true;
					DatabaseInventoryData data = pd.getInvMysqlInterface().getData(p);
					if (data.getSyncStatus().matches("true")) {
						pd.getInventoryDataHandler().setPlayerData(p, data, syncD, true);
						inProgress = false;
						if(PlayerJoin.invsync != null){
							if(PlayerJoin.invsync.contains(p)){
								PlayerJoin.invsync.remove(p);
							}
							if (pd.getConfigHandler().getString("ChatMessages.syncComplete").matches("") == false) {
								p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessages.syncComplete"));
								TTA_Methods.sendActionBar(p, "§8» §7Inventar Sync §aabgeschlossen§7!", 20*3);
							}
							pd.getSoundHandler().sendLevelUpSound(p);
						}
						this.cancel();
					} else if (System.currentTimeMillis() - Long.parseLong(data.getLastSeen()) >= 600 * 1000) {
						pd.getInventoryDataHandler().setPlayerData(p, data, syncD, true);
						inProgress = false;
						if(PlayerJoin.invsync != null){
							if(PlayerJoin.invsync.contains(p)){
								PlayerJoin.invsync.remove(p);
							}
							if (pd.getConfigHandler().getString("ChatMessages.syncComplete").matches("") == false) {
								p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessages.syncComplete"));
								TTA_Methods.sendActionBar(p, "§8» §7Inventar Sync §aabgeschlossen§7!", 20*3);
							}
							pd.getSoundHandler().sendLevelUpSound(p);
						}
						this.cancel();
					} else if (System.currentTimeMillis() - startTime >= 22 * 1000) {
						pd.getInventoryDataHandler().setPlayerData(p, data, syncD, true);
						inProgress = false;
						if(PlayerJoin.invsync != null){
							if(PlayerJoin.invsync.contains(p)){
								PlayerJoin.invsync.remove(p);
							}
							if (pd.getConfigHandler().getString("ChatMessages.syncComplete").matches("") == false) {
								p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessages.syncComplete"));
								TTA_Methods.sendActionBar(p, "§8» §7Inventar Sync §aabgeschlossen§7!", 20*3);
							}
							pd.getSoundHandler().sendLevelUpSound(p);
						}
						this.cancel();
					}
					inProgress = false;
				} else {
					//inProgress = false;
					this.cancel();
				}
			} else {
				//inProgress = false;
				this.cancel();
			}
		}
	}
	
	

}
