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
        new AcceptCmd(this);
        new AbandonCmd(this);
        new AcceptedListCmd(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        ACmd aCmd = this.nextExSub(args);
        if (aCmd == null) aCmd = HelpCmd.instance;
        String name = aCmd.getName();
        System.out.println(name);
        if (!sender.hasPermission("fitask.cmd." + name)){
            sender.sendMessage("§cYou don't have permission to use this command.");
            return false;
        }
        return aCmd.onCommand(sender, cmd, label, removeOneArg(args));
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
