package me.gsqfi.fitask.fitask.commands.edit;

import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitask.fitask.commands.HelpCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditCmd extends ACmd {
    public EditCmd(ACmd superCmd) {
        super(superCmd, "edit");
        new EditHelpCmd(this);
        new RemoveCmd(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        ACmd aCmd = nextExSub(args);
        return aCmd == null ? HelpCmd.instance.onCommand(sender, cmd, label, removeOneArg(args))
                : aCmd.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return nextTab(commandSender, command, s, strings);
    }
}
