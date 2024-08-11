package me.gsqfi.fitaskgui.fitaskgui;

import me.gsqfi.fitaskgui.fitaskgui.gui.GuiTypeBasic;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements TabExecutor {
    public static Main plugin;

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        GuiTypeBasic.init(this);
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginCommand command = getCommand("fitaskgui");
        getLogger().info("§aPlugin loaded!");
    }
}