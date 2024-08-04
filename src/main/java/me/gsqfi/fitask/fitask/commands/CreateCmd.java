package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class CreateCmd extends ACmd{
    public CreateCmd(ACmd superCmd) {
        super(superCmd,"create");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String name = args[0];
            BasicTask task = new BasicTask();
            task.setTaskName(name);
            File file = new File(TaskDataHelper.dataFolder, name + ".json");
            if (file.exists()) {
                sender.sendMessage("§cA file with the same name already exists!");
                return false;
            }
            task.saveSetFile(file);
            TaskDataHelper.addTaskInCache(task);
            ChatComponentsGui.clearChat(sender);
            sender.spigot().sendMessage(ChatComponentsGui.info(task));
            sender.sendMessage("§aSuccessfully Create!");
            return false;
        }
        HelpCmd.instance.onCommand(sender, cmd, label, args);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
