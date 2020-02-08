package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.simplelobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getWorld().getName().toLowerCase().contains("lobby")) {
				if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
					event.setCancelled(true);
					player.teleport(Main.lobby);
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

}
