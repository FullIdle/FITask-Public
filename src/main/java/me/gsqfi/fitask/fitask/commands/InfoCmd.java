package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class InfoCmd extends ACmd {
    public InfoCmd(ACmd superCmd) {
        super(superCmd, "info");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            UUID uuid;
            try {
                uuid = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid UUID format!");
                return false;
            }
            BasicTask task = TaskDataHelper.getTask(uuid);
            if (task != null) {
                ChatComponentsGui.clearChat(sender);
                sender.spigot().sendMessage(ChatComponentsGui.info(task));
                return false;
            }
            sender.sendMessage("§cTask not found!");
            return false;
        }
        return HelpCmd.instance.onCommand(sender, cmd, label, removeOneArg(args));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return this.taskTabList(args);
    }
}
