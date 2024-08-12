package me.gsqfi.fitaskgui.fitaskgui.commands;

import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitaskgui.fitaskgui.gui.GuiTypeBasic;
import me.gsqfi.fitaskgui.fitaskgui.gui.ServerDailyGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HelpCmd extends ACmd {
    public static HelpCmd instance;

    private static final String[] help = new String[]{
            "§7§lFITaskGui-[HELP]",
            "§7§l  - help    §r§l查看帮助",
            "§7§l  - reload    §r§l重载配置",
            "§7§l  - daily    §r§l每日任务",
            "§7§l  - weekly    §r§l每周任务",
            "§7§l  - month    §r§l每月任务",
            "§7§l  - disposable    §r§l一次性任务",
    };

    public HelpCmd(ACmd superCmd) {
        super(superCmd, "help");
        instance = this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(help);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
