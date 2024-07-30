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

public class RemoveCmd extends ACmd {
    public RemoveCmd(ACmd superCmd) {
        super(superCmd, "remove");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            try {
                UUID uuid = UUID.fromString(args[0]);
                BasicTask task = TaskDataHelper.getTask(uuid);
                if (task != null) {
                    boolean delete = task.getFile().delete();
                    if (delete) {
                        TaskDataHelper.removeTaskInCache(uuid);
                        sender.sendMessage("§aSuccessfully Deleted!");
                    }else {
                        sender.sendMessage("§cFailed to delete!");
                    }
                    return false;
                }
                sender.sendMessage("§cTask not found!");
                return false;
            } catch (Exception ignored) {}
        }
        EditHelpCmd.instance.onCommand(sender, cmd, label, args);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1)
            return TaskDataHelper.cacheTask.keySet().stream().map(UUID::toString).collect(Collectors.toList());
        if (args.length > 1)
            return TaskDataHelper.cacheTask.keySet().stream().map(UUID::toString).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
