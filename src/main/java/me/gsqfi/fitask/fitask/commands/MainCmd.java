package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.taskComponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.taskComponent.conditions.ItemStackCondition;
import me.gsqfi.fitask.fitask.taskComponent.rewards.IReward;
import me.gsqfi.fitask.fitask.taskComponent.rewards.ItemStackReward;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MainCmd extends ACmd{
    public MainCmd() {
        super(null,"fitask");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("test");
        BasicTask task = FITaskApi.getTask(UUID.fromString("8a3e68d6-0cd9-48df-b7a6-ce561153fdac"));
        Player player = (Player) sender;
        if (task.meetAllConditions(player)) {
            task.givePlayerRewards(player);
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }

    public void register(Main plugin){
        PluginCommand command = plugin.getCommand("fitask");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
