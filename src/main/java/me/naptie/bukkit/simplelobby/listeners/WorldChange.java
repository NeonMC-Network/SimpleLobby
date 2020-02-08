package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.simplelobby.Main;
import me.naptie.bukkit.simplelobby.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().getName().toLowerCase().contains("lobby")) {
			if (player.hasPermission(Permissions.FLIGHT)) {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.teleport(Main.flight);
			} else {
				player.teleport(Main.lobby);
			}
		}
	}

}
