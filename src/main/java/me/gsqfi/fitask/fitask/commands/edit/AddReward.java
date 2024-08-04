package me.gsqfi.fitask.fitask.commands.edit;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import me.gsqfi.fitask.fitask.commands.ACmd;
import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddReward extends ACmd {
    public AddReward(ACmd cmd) {
        super(cmd, "addreward");
    }

    @SneakyThrows
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
            if (task == null) {
                sender.sendMessage("§cTask not found!");
                return false;
            }
            if (args.length > 1) {
                String clsP = args[1];
                for (Map<Class<? extends IAdapter>, IAdapter<?>> value : DataPersistenceHelper.allAdapter.values()) {
                    for (IAdapter<?> ad : value.values()) {
                        if (ad.getClass().getName().equals(clsP) && ad instanceof IReward<?>) {
                            ArrayList<IReward<?>> list = Lists.newArrayList(task.getRewards());
                            list.add((IReward<?>) ad.getClass().getDeclaredConstructor().newInstance());
                            task.setRewards(list.toArray(new IReward[0]));
                            task.saveSetFile(task.getFile());
                            ChatComponentsGui.clearChat(sender);
                            sender.spigot().sendMessage(ChatComponentsGui.info(task));
                            sender.sendMessage("§aCondition added!");
                            return false;
                        }
                    }
                }
                sender.sendMessage("§cInvalid condition class!");
                return false;
            }
        }
        return EditHelpCmd.instance.onCommand(sender, cmd, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            ArrayList<String> list = new ArrayList<>();
            for (Map<Class<? extends IAdapter>, IAdapter<?>> value : DataPersistenceHelper.allAdapter.values()) {
                for (Map.Entry<Class<? extends IAdapter>, IAdapter<?>> entry : value.entrySet()) {
                    if (entry.getValue() instanceof IReward<?>) {
                        list.add(entry.getKey().getName());
                    }
                }
            }
            return list;
        }
        return taskTabList(args);
    }
}
