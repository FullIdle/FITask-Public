package me.gsqfi.fitask.fitask.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class HelpCmd extends ACmd{
    public static HelpCmd instance;
    private final String[] help = new String[]{
            "§7§lFITask-[HELP]",
            "§7§l  - help    §r§l查看帮助",
            "§7§l  - reload    §r§l重载数据",
            "§7§l  - info [任务UID]    §r§l查看任务信息",
            "§7§l  - create [任务名]    §r§l查看开发者信息",
            "§7§l  - show    §r§l列出所有任务",
            "§7§l  - edit [args]    §r§l编辑命令[edit help查看帮助]",
    };

    public HelpCmd(ACmd superCmd) {
        super(superCmd,"help");
        instance = this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(help);

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
