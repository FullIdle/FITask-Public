package me.gsqfi.fitask.fitask.commands;

import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.commands.edit.EditCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCmd extends ACmd{
    public MainCmd() {
        super(null,"fitask");
        new HelpCmd(this);
        new CreateCmd(this);
        new InfoCmd(this);
        new ReloadCmd(this);
        new ShowCmd(this);
        new EditCmd(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        ACmd aCmd = this.nextExSub(args);
        return aCmd == null ? HelpCmd.instance.onCommand(sender, cmd, label, removeOneArg(args))
                : aCmd.onCommand(sender, cmd, label, removeOneArg(args));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return nextTab(commandSender, command, s, strings);
    }

    public void register(Main plugin){
        PluginCommand command = plugin.getCommand("fitask");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
