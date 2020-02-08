package me.naptie.bukkit.simplelobby;

import me.naptie.bukkit.simplelobby.utils.CU;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

	public static String getMessage(YamlConfiguration language, String message) {
		return CU.t(language.getString(message));
	}

	public static String getMessage(String language, String message) {
		return CU.t(YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), language + ".yml")).getString(message));
	}

	public static String getMessage(OfflinePlayer player, String message) {
		return CU.t(getLanguage(player).getString(message));
	}

	private static YamlConfiguration getLanguage(OfflinePlayer player) {
		File locale = new File(Main.getInstance().getDataFolder(), ConfigManager.getLanguageName(player) + ".yml");
		return YamlConfiguration.loadConfiguration(locale);
	}

}
