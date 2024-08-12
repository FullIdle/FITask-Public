package me.gsqfi.fitaskgui.fitaskgui.commands;

import me.gsqfi.fitask.fitask.commands.ACmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCmd extends ACmd {
    public MainCmd() {
        super(null, "fitaskgui");
        new HelpCmd(this);
        new ReloadCmd(this);
        new DailyCmd(this);
        new MonthlyCmd(this);
        new WeeklyCmd(this);
        new DisposableCmd(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        ACmd aCmd = this.nextExSub(args);
        if (aCmd == null) aCmd = HelpCmd.instance;
        return aCmd.onCommand(sender, cmd, label, removeOneArg(args));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return nextTab(sender, cmd, label, args);
    }
}
