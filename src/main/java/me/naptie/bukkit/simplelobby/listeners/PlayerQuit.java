package me.naptie.bukkit.simplelobby.listeners;

import me.naptie.bukkit.simplelobby.Main;
import me.naptie.bukkit.simplelobby.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerQuit implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(null);
		for (Player player1 : player.getWorld().getPlayers()) {
			player1.sendMessage(Messages.getMessage(player1, "LEFT").replace("%player%", player.getDisplayName()).replace("%name%", Main.name));
		}
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Main.mysql.editor.set(Main.id, "players", Bukkit.getOnlinePlayers().size());
			}
		});
	}

}
