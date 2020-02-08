package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.rank.events.RankChangeEvent;
import me.naptie.bukkit.simplelobby.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankChange implements Listener {

	@EventHandler
	public void onRankChange(RankChangeEvent e) {
		Player player = e.getPlayer();
		if (player != null) {
			if (player.hasPermission(Permissions.FLIGHT)) {
				player.setAllowFlight(true);
				player.setFlying(true);
			} else {
				player.setFlying(false);
				player.setAllowFlight(false);
			}
		}
	}

}
