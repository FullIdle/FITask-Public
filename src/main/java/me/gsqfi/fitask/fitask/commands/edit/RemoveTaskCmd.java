package me.gsqfi.fitask.fitask.commands.edit;

import me.gsqfi.fitask.fitask.api.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RemoveTaskCmd extends ACmd {
    public RemoveTaskCmd(ACmd superCmd) {
        super(superCmd, "removetask");
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
                boolean delete = task.getFile().delete();
                if (delete) {
                    TaskDataHelper.removeTaskInCache(uuid);
                    sender.sendMessage("§aSuccessfully Deleted!");
                } else {
                    sender.sendMessage("§cFailed to delete!");
                }
                return true;
            }
            sender.sendMessage("§cTask not found!");
            return false;
        }
        return EditHelpCmd.instance.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return this.taskTabList(args);
    }
}
