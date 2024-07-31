package me.gsqfi.fitask.fitask.commands.edit;

import me.gsqfi.fitask.fitask.commands.ACmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class RemoveConditionCmd extends ACmd {
    public RemoveConditionCmd(ACmd superCmd) {
        super(superCmd, "removecondition");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return EditHelpCmd.instance.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
