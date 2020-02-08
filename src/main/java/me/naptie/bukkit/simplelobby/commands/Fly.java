package me.naptie.bukkit.simplelobby.commands;

import me.naptie.bukkit.simplelobby.Messages;
import me.naptie.bukkit.simplelobby.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (player.getWorld().getName().toLowerCase().contains("lobby")) {
				if (player.hasPermission(Permissions.FLIGHT)) {
					if (strings.length == 0) {
						if (player.getAllowFlight()) {
							player.setFlying(false);
							player.setAllowFlight(false);
							player.sendMessage(Messages.getMessage(player, "FLIGHT_OFF"));
							return true;
						} else {
							player.setAllowFlight(true);
							player.setFlying(true);
							player.sendMessage(Messages.getMessage(player, "FLIGHT_ON"));
							return true;
						}
					} else {
						if (strings[0].equalsIgnoreCase("on")) {
							player.setAllowFlight(true);
							player.setFlying(true);
							player.sendMessage(Messages.getMessage(player, "FLIGHT_ON"));
							return true;
						} else if (strings[0].equalsIgnoreCase("off")) {
							player.setFlying(false);
							player.setAllowFlight(false);
							player.sendMessage(Messages.getMessage(player, "FLIGHT_OFF"));
							return true;
						} else {
							player.sendMessage(Messages.getMessage(player, "USAGE").replace("%usage%", "/fly [on|off]"));
							return true;
						}
					}
				} else {
					if (player.isFlying()) {
						player.setFlying(false);
						player.setAllowFlight(false);
					}
					player.sendMessage(Messages.getMessage(player, "PERMISSION_DENIED"));
				}
			} else {
				player.sendMessage(Messages.getMessage(player, "NOT_IN_LOBBY"));
			}
			return true;
		} else {
			commandSender.sendMessage(Messages.getMessage("zh-CN", "NOT_A_PLAYER"));
			return true;
		}
	}
}
