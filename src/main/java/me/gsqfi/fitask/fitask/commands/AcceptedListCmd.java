package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;

public class AcceptedListCmd extends ACmd {
    public AcceptedListCmd(ACmd superCmd) {
        super(superCmd, "acceptlist");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        OfflinePlayer player;
        if (args.length > 0) {
            String name = args[0];
            Optional<OfflinePlayer> first = Arrays.stream(Bukkit.getOfflinePlayers()).filter(p -> {
                String n = p.getName();
                return n != null && n.equals(name);
            }).findFirst();
            if (!first.isPresent()) {
                sender.sendMessage("§cPlayer not found!");
                return false;
            }
            player = first.get();
        }else{
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cOnly player can use this command!");
                return false;
            }
            player = (Player) sender;
        }
        String nn = player.getName();
        Map<UUID, LocalDateTime> uuids = FITaskApi.getPlayerData().getAllAcceptedTasks(nn);
        if (uuids.isEmpty()) {
            sender.sendMessage("§cNo tasks found!");
            return false;
        }
        sender.sendMessage("§7§lTaskAccepted:§7[非常简陋-建议不用,没有半点艺术细胞!]");
        for (UUID uuid : uuids.keySet()) {
            BasicTask task = TaskDataHelper.getTask(uuid);
            if (task != null) {
                sender.sendMessage("§3§l"+task.getUuid().toString());
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
