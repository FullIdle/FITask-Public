package me.gsqfi.fitpokecr.fitpokecr;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitaskgui.fitaskgui.api.events.TaskCompleteEvent;
import me.gsqfi.fitpokecr.fitpokecr.conditions.BeatPokeAmountCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.BeatPokeCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.CapturePokeAmountCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.CapturePokeCondition;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.IPlayerData;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.MySQLPlayerData;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.YamlPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Main extends JavaPlugin implements Listener {
    public static Main plugin;
    public static IPlayerData playerData;

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();
        String path = this.getConfig().getString("database.url");
        if (playerData != null) {
            playerData.close();
        }

        //data
        if (this.getConfig().getString("storage-method", "").equalsIgnoreCase("mysql")) {
            playerData = new MySQLPlayerData(
                    this.getConfig().getString("database.url"),
                    this.getConfig().getString("database.user"),
                    this.getConfig().getString("database.password")
            );
        } else {
            File file;
            if (path == null || path.isEmpty()) {
                file = new File(this.getDataFolder(), "playerdata");
            } else {
                file = new File(path);
            }
            playerData = new YamlPlayerData(file);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerData.load(player.getName());
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.reloadConfig();
        //condition
        FITaskApi.registerConditions(this,
                new BeatPokeAmountCondition(),
                new BeatPokeCondition(),
                new CapturePokeAmountCondition(),
                new CapturePokeCondition()
        );
        //cmd
        getCommand("fitpokecr").setExecutor(this);
        //listener
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("§aFITaskExtra has been enabled!");
    }

    @Override
    public void onDisable() {
        playerData.close();
    }

    //event
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        playerData.load(name);
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        playerData.release(event.getPlayer().getName());
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent e) {
        playerData.removePlayerTask(e.getPlayer().getName(),e.getTask().getUuid());
    }

    //reloadcmd
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§c您没有权限使用此命令!");
            return false;
        }
        this.reloadConfig();
        sender.sendMessage("§a已重新加载!");
        return false;
    }
}