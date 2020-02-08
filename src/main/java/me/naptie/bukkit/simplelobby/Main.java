package me.naptie.bukkit.simplelobby;

import me.naptie.bukkit.core.utils.CoreStorage;
import me.naptie.bukkit.simplelobby.listeners.*;
import me.naptie.bukkit.simplelobby.commands.Fly;
import me.naptie.bukkit.simplelobby.commands.Lobby;
import me.naptie.bukkit.simplelobby.commands.SetLobby;
import me.naptie.bukkit.simplelobby.listeners.*;
import me.naptie.bukkit.simplelobby.utils.MySQLManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


public class Main extends JavaPlugin {

	public static Location lobby;
	public static Location flight;
	public static MySQLManager mysql;
	public static int id;
	public static String name;
	private static Logger logger;
	private static Main instance;
	private PluginDescriptionFile descriptionFile;

	public static Main getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		logger = getLogger();
		descriptionFile = getDescription();
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveDefaultConfig();
		for (String language : getConfig().getStringList("languages")) {
			File localeFile = new File(getDataFolder(), language + ".yml");
			if (localeFile.exists()) {
				if (getConfig().getBoolean("update-language-files")) {
					saveResource(language + ".yml", true);
				}
			} else {
				saveResource(language + ".yml", false);
			}
		}
		World world = Bukkit.getServer().getWorld((getConfig().getString("lobby.world")));
		double x = getConfig().getDouble("lobby.x");
		double y = getConfig().getDouble("lobby.y");
		double z = getConfig().getDouble("lobby.z");
		float pitch = (float) getConfig().getDouble("lobby.pitch");
		float yaw = (float) getConfig().getDouble("lobby.yaw");
		lobby = new Location(world, x, y, z, yaw, pitch);
		flight = new Location(world, x, y + 2, z, yaw, pitch);
		registerCommands();
		registerEvents();
		mysql = new MySQLManager();
		String serverName = CoreStorage.getServerName(getServer().getPort());
		boolean exists = false;
		for (int i : mysql.editor.getAllKeys()) {
			if (mysql.editor.get(i).get("server").equals(serverName)) {
				id = i;
				exists = true;
			}
		}
		while (!exists && mysql.editor.contains(id)) {
			id++;
		}
		name = StringUtils.capitalize(getConfig().getString("integration"));
		mysql.editor.set(id, name.replaceAll("\u0000", ""), serverName, name, 0, getServer().getMaxPlayers(), getServer().getOnlinePlayers().size(), 0, "LOBBY", 1);

		logger.info("Enabled " + descriptionFile.getName() + " v" + descriptionFile.getVersion());
	}

	public void onDisable() {
		getConfig().set("lobby.world", lobby.getWorld().getName());
		getConfig().set("lobby.x", lobby.getX());
		getConfig().set("lobby.y", lobby.getY());
		getConfig().set("lobby.z", lobby.getZ());
		getConfig().set("lobby.pitch", lobby.getPitch());
		getConfig().set("lobby.yaw", lobby.getYaw());
		saveConfig();
		mysql.editor.set(id, "players", 0);
		mysql.editor.set(id, "state", "ENDING");
		Set<Player> sentSet = new HashSet<>();
		for (int i : mysql.editor.getAllKeys()) {
			if (mysql.editor.get(i).get("type").equals("Lobby") && ((String) mysql.editor.get(i).get("name")).contains("Lobby") && !mysql.editor.get(i).get("state").equals("ENDING")) {
				if ((int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players") >= Bukkit.getOnlinePlayers().size()) {
					boolean hasSent = false;
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (!sentSet.contains(player) && !hasSent) {
							player.sendMessage(Messages.getMessage(player, "SERVER_WENT_DOWN"));
							me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
							sentSet.add(player);
							hasSent = true;
						}
					}
				} else {
					int available = (int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players");
					int sent = 0;
					while (sent <= available) {
						boolean hasSent = false;
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!sentSet.contains(player) && !hasSent) {
								player.sendMessage(Messages.getMessage(player, "SERVER_WENT_DOWN"));
								me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
								sentSet.add(player);
								hasSent = true;
							}
						}
						sent++;
					}
				}
			}
		}
		if (Bukkit.getOnlinePlayers().size() > 0) {
			for (int i : mysql.editor.getAllKeys()) {
				if (mysql.editor.get(i).get("type").equals("Lobby") && !mysql.editor.get(i).get("state").equals("ENDING")) {
					if ((int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players") >= Bukkit.getOnlinePlayers().size()) {
						boolean hasSent = false;
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!sentSet.contains(player) && !hasSent) {
								player.sendMessage(Messages.getMessage(player, "SERVER_WENT_DOWN"));
								me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
								sentSet.add(player);
								hasSent = true;
							}
						}
					} else {
						int available = (int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players");
						int sent = 0;
						while (sent <= available) {
							boolean hasSent = false;
							for (Player player : Bukkit.getOnlinePlayers()) {
								if (!sentSet.contains(player) && !hasSent) {
									player.sendMessage(Messages.getMessage(player, "SERVER_WENT_DOWN"));
									me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
									sentSet.add(player);
									hasSent = true;
								}
							}
							sent++;
						}
					}
				}
			}
		}
		mysql.editor.getMySQL().disconnect();
		if (Bukkit.getOnlinePlayers().size() > 0) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		instance = null;
		logger.info("Disabled " + descriptionFile.getName() + " v" + descriptionFile.getVersion());
		logger = null;
	}

	private void registerCommands() {
		getCommand("setlobby").setExecutor(new SetLobby());
		getCommand("lobby").setExecutor(new Lobby());
		getCommand("fly").setExecutor(new Fly());
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new EntityDamage(), this);
		pm.registerEvents(new FoodLevelChange(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new BlockPlace(), this);
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new RankChange(), this);
		pm.registerEvents(new WeatherChange(), this);
		pm.registerEvents(new WorldChange(), this);
	}

}
