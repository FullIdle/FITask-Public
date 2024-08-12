package me.gsqfi.fitaskgui.fitaskgui.commands;

import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitaskgui.fitaskgui.gui.GuiTypeBasic;
import me.gsqfi.fitaskgui.fitaskgui.gui.ServerMonthlyGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonthlyCmd extends ACmd {
    public MonthlyCmd(ACmd superCmd) {
        super(superCmd, "monthly");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§c该命令只能由玩家执行!");
            return false;
        }
        new ServerMonthlyGui(((Player) commandSender), GuiTypeBasic.GuiType.MONTHLY.getTitle()).open();
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
