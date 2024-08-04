package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.gui.ChatComponentsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ShowCmd extends ACmd{
    public ShowCmd(ACmd superCmd) {
        super(superCmd, "show");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int page = 0;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cIllegal page number!");
                return false;
            }
        }
        ChatComponentsGui.clearChat(sender);
        sender.spigot().sendMessage(ChatComponentsGui.show(page));
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
