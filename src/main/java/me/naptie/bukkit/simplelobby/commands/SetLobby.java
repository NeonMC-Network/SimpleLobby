package me.naptie.bukkit.simplelobby.commands;

import me.naptie.bukkit.simplelobby.Main;
import me.naptie.bukkit.simplelobby.Messages;
import me.naptie.bukkit.simplelobby.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (player.hasPermission(Permissions.SET_LOBBY)) {
				if (strings.length == 0) {
					Main.lobby = player.getLocation();
					player.sendMessage(Messages.getMessage(player, "LOBBY_SET"));
					return true;
				} else if (strings.length >= 6) {
					World world = Bukkit.getWorld(strings[0]);
					if (world == null) {
						player.sendMessage(Messages.getMessage(player, "WORLD_NOT_FOUND"));
						return true;
					}
					double x = Double.parseDouble(strings[1]);
					double y = Double.parseDouble(strings[2]);
					double z = Double.parseDouble(strings[3]);
					float pitch = Float.parseFloat(strings[4]);
					float yaw = Float.parseFloat(strings[5]);
					Main.lobby = new Location(world, x, y, z, yaw, pitch);
					player.sendMessage(Messages.getMessage(player, "LOBBY_SET"));
					return true;
				} else {
					player.sendMessage(Messages.getMessage(player, "USAGE").replace("%usage%", "/setlobby [world] [x] [y] [z] [pitch] [yaw]"));
					return true;
				}
			} else {
				player.sendMessage(Messages.getMessage(player, "PERMISSION_DENIED"));
				return true;
			}
		} else {
			if (strings.length >= 6) {
				World world = Bukkit.getWorld(strings[0]);
				if (world == null) {
					commandSender.sendMessage(Messages.getMessage("zh-CN", "WORLD_NOT_FOUND"));
					return true;
				}
				double x = Double.parseDouble(strings[1]);
				double y = Double.parseDouble(strings[2]);
				double z = Double.parseDouble(strings[3]);
				float pitch = Float.parseFloat(strings[4]);
				float yaw = Float.parseFloat(strings[5]);
				Main.lobby = new Location(world, x, y, z, yaw, pitch);
				commandSender.sendMessage(Messages.getMessage("zh-CN", "LOBBY_SET"));
				return true;
			} else {
				commandSender.sendMessage(Messages.getMessage("zh-CN", "USAGE").replace("%usage%", "setlobby [world] [x] [y] [z] [pitch] [yaw]"));
				return true;
			}
		}
	}

}
