package me.gsqfi.fitask.fitask.commands.edit;

import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SetNameCmd extends ACmd {
    public SetNameCmd(ACmd cmd) {
        super(cmd,"setname");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            UUID uuid;
            try {
                uuid = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid UUID format!");
                return false;
            }
            BasicTask task = TaskDataHelper.getTask(uuid);
            if (task == null) {
                sender.sendMessage("§cTask not found!");
                return false;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]);
            }
            task.setTaskName(builder.toString());
            task.saveSetFile(task.getFile());
            ChatComponentsGui.clearChat(sender);
            sender.spigot().sendMessage(ChatComponentsGui.info(task));
            sender.sendMessage("§aTask renamed successfully!");
            return false;
        }
        return EditHelpCmd.instance.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return taskTabList(args);
    }
}
