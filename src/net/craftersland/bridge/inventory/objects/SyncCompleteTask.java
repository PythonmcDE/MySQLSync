package net.craftersland.bridge.inventory.objects;

import net.craftersland.bridge.inventory.events.PlayerJoin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.craftersland.bridge.inventory.Inv;

public class SyncCompleteTask extends BukkitRunnable {
	
	private Inv pd;
	private long startTime;
	private Player p;
	private boolean inProgress = false;
	
	public SyncCompleteTask(Inv pd, long start, Player player) {
		this.pd = pd;
		this.startTime = start;
		this.p = player;
	}

	@Override
	public void run() {
		new BukkitRunnable(){

			@Override
			public void run() {
				if (inProgress == false) {
					if (p != null) {
						if (p.isOnline() == true) {
							inProgress = true;
							if (pd.getInventoryDataHandler().isSyncComplete(p) == true) {
								this.cancel();
							} else {
								if (System.currentTimeMillis() - startTime >= 20 * 1000) {
									//Set sync to true in database to force sync data after 20 sec
									pd.getInvMysqlInterface().setSyncStatus(p, "true");
								} else if (System.currentTimeMillis() - startTime >= 40 * 1000) {
									//Stop task after 40 sec
									this.cancel();
								}

							}
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
		}.runTaskLater(Inv.getInstance(), 20*5);
	}
	
	

}
