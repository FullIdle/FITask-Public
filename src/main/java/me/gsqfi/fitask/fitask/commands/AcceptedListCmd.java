package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AcceptedListCmd extends ACmd {
    public AcceptedListCmd(ACmd superCmd) {
        super(superCmd, "accept");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String name = args[0];
            Optional<OfflinePlayer> first = Arrays.stream(Bukkit.getOfflinePlayers()).filter(p -> {
                String n = p.getName();
                return n != null && n.equals(name);
            }).findFirst();
            if (first.isPresent()) {
                String nn = first.get().getName();
                List<UUID> uuids = FITaskApi.getPlayerData().getAllAcceptedTasks(nn);
                if (uuids.isEmpty()) {
                    sender.sendMessage("§cNo tasks found!");
                    return false;
                }
                sender.sendMessage("§7§lTaskAccepted:§7[非常简陋-建议不用,没有半点艺术细胞!]");
                for (UUID uuid : uuids) {
                    BasicTask task = TaskDataHelper.getTask(uuid);
                    if (task != null) {
                        sender.sendMessage("§3§l"+task.getUuid().toString());
                    }
                }
                return false;
            }
            sender.sendMessage("§cPlayer not found!");
            return false;
        }
        return HelpCmd.instance.onCommand(sender, cmd, label, removeOneArg(args));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
