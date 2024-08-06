package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AbandonCmd extends ACmd {
    public AbandonCmd(ACmd superCmd) {
        super(superCmd, "abandon");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            if (!(sender instanceof Player)){
                sender.sendMessage("§cOnly players can use this command!");
                return false;
            }
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
            if (FITaskApi.getPlayerData().abandon(sender.getName(),task.getUuid())) {
                sender.sendMessage("§aTask abandoned!");
            }else{
                sender.sendMessage("§aTask not abandoned!");
            }
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
