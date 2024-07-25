package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadCmd extends ACmd{
    public ReloadCmd(ACmd superCmd) {
        super(superCmd, "reload");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        FITaskApi.getPlugin().reloadConfig();
        sender.sendMessage("§aReloaded!");
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
