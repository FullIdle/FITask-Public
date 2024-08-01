package me.gsqfi.fitask.fitask.commands.edit;

import com.google.common.collect.Lists;
import me.gsqfi.fitask.fitask.api.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskComponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RemoveConditionCmd extends ACmd {
    public RemoveConditionCmd(ACmd superCmd) {
        super(superCmd, "removecondition");
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
            int slot;
            try {
                slot = Integer.parseInt(args[1]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid slot number!");
                return false;
            }
            BasicTask task = TaskDataHelper.getTask(uuid);
            if (task != null) {
                ArrayList<ICondition<?>> conditions = Lists.newArrayList(task.getConditions());
                conditions.remove(slot);
                task.setConditions(conditions.toArray(new ICondition<?>[0]));
                task.saveSetFile(task.getFile());
                ChatComponentsGui.clearChat(sender);
                sender.spigot().sendMessage(ChatComponentsGui.info(task));
                return false;
            }
            sender.sendMessage("§cTask not found!");
            return false;
        }
        return EditHelpCmd.instance.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
