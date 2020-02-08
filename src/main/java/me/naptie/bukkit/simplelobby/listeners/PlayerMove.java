package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.simplelobby.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();

		if (location.getWorld().getName().toLowerCase().contains("lobby") && location.getY() < 0) {
			player.teleport(Main.lobby);
		}

	}

}
