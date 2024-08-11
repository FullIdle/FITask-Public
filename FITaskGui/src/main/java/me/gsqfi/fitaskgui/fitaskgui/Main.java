package me.gsqfi.fitaskgui.fitaskgui;

import me.gsqfi.fitask.fitask.api.events.player.TaskDataInitEvent;
import me.gsqfi.fitaskgui.fitaskgui.api.FITaskGuiApi;
import me.gsqfi.fitaskgui.fitaskgui.gui.GuiTypeBasic;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
            FITaskGuiApi.playerData = new
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginCommand command = getCommand("fitaskgui");
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

    @EventHandler
    public void onTaskDataInit(TaskDataInitEvent e){
        GuiTypeBasic.init(this);
    }
}