package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MainCmd extends ACmd{
    public MainCmd() {
        super(null,"fitask");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }

    public void register(Main plugin){
        PluginCommand command = plugin.getCommand("fitask");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
