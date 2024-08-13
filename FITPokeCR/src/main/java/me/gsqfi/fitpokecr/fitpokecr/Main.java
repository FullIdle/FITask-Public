package me.gsqfi.fitpokecr.fitpokecr;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitpokecr.fitpokecr.conditions.*;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.IPlayerData;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.MySQLPlayerData;
import me.gsqfi.fitpokecr.fitpokecr.playerdata.YamlPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Main extends JavaPlugin {
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
                new CapturePokeCondition(),
                new BeatNPCAmountCondition(),
                new BeatNPCCondition(),
                new ActivateShrineCondition(),
                new ActivateShrineAmountCondition(),
                new EvolvePokeCondition(),
                new PickApricornAmountCondition(),
                new PickApricornCondition()
        );
        //cmd
        getCommand("fitpokecr").setExecutor(this);
        //listener
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("§aFITaskExtra has been enabled!");
    }

    @Override
    public void onDisable() {
        playerData.close();
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