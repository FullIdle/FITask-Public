package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class CreateCmd extends ACmd{
    public CreateCmd(ACmd cmd) {
        super(cmd,"create");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String name = args[0];
            BasicTask task = new BasicTask();
            task.saveSetFile(new File(TaskDataHelper.dataFolder, name+".json"));
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
