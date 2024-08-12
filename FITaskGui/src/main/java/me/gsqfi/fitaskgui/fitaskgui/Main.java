package me.gsqfi.fitaskgui.fitaskgui;

import me.gsqfi.fitask.fitask.api.events.player.TaskDataInitEvent;
import me.gsqfi.fitaskgui.fitaskgui.api.FITaskGuiApi;
import me.gsqfi.fitaskgui.fitaskgui.api.playerdata.MySQLPlayerLastCompleteData;
import me.gsqfi.fitaskgui.fitaskgui.api.playerdata.YamlPlayerLastCompleteData;
import me.gsqfi.fitaskgui.fitaskgui.commands.MainCmd;
import me.gsqfi.fitaskgui.fitaskgui.gui.GuiTypeBasic;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements TabExecutor, Listener {
    public static Main plugin;

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        {
            //playerDataInit
            if (FITaskGuiApi.playerData != null) {
                FITaskGuiApi.playerData.close();
            }
            String path = this.getConfig().getString("database.url");
            String storageType = this.getConfig().getString("storage-method", "");
            if (storageType.equals("MYSQL")) {
                FITaskGuiApi.playerData = new MySQLPlayerLastCompleteData(
                        this.getConfig().getString("database.url"),
                        this.getConfig().getString("database.user"),
                        this.getConfig().getString("database.password")
                );
            }
            if (storageType.equals("YAML")){
                File file;
                if (path == null || path.isEmpty()) {
                    file = new File(this.getDataFolder(), "playerdata");
                } else {
                    file = new File(path);
                }
                FITaskGuiApi.playerData = new YamlPlayerLastCompleteData(file);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                FITaskGuiApi.playerData.load(player.getName());
            }
        }

        Bukkit.getScheduler().runTask(this,()->{
            GuiTypeBasic.init(this);
        });
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.reloadConfig();
        PluginCommand command = getCommand("fitaskgui");
        MainCmd mainCmd = new MainCmd();
        command.setExecutor(mainCmd);
        command.setTabCompleter(mainCmd);
        getLogger().info("§aPlugin loaded!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        FITaskGuiApi.playerData.load(name);
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        FITaskGuiApi.playerData.release(event.getPlayer().getName());
    }

    @Override
    public void onDisable() {
        FITaskGuiApi.playerData.close();
    }
}