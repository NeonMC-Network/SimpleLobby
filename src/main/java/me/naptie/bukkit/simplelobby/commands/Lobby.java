package me.naptie.bukkit.simplelobby.commands;

import me.naptie.bukkit.simplelobby.Main;
import me.naptie.bukkit.simplelobby.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lobby implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (player.getWorld().getName().toLowerCase().contains("lobby")) {
				player.teleport(Main.lobby);
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
