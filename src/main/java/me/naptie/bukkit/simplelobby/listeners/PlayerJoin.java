package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.simplelobby.Main;
import me.naptie.bukkit.simplelobby.Messages;
import me.naptie.bukkit.simplelobby.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		event.setJoinMessage(null);
		if (player.hasPermission(Permissions.FLIGHT)) {
			player.setAllowFlight(true);
			player.setFlying(true);
			player.teleport(Main.flight);
		} else {
			player.teleport(Main.lobby);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			for (Player player1 : player.getWorld().getPlayers()) {
				player1.sendMessage(Messages.getMessage(player1, "JOINED").replace("%player%", player.getDisplayName()).replace("%name%", Main.name));
			}
		}, 1);
		Main.mysql.editor.set(Main.id, "players", Bukkit.getOnlinePlayers().size());
	}

}
