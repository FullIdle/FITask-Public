package me.gsqfi.fitask.fitask.commands.edit;

import me.gsqfi.fitask.fitask.commands.ACmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class EditHelpCmd extends ACmd {
    public static EditHelpCmd instance;
    private final String[] help = new String[]{
            "§7§lFITask-[HELP]-[EDIT]",
            "§7§l  - help  §r§l查看edit指令帮助",
            "§7§l  - addcondition [任务UID] [条件类路径]  §r§l添加条件到指定任务",
            "§7§l  - addreward [任务UID] [奖励类路径]  §r§l添加奖励到指定任务",
            "§7§l  - removecondition [任务UID] [slot]  §r§l移除指定任务的条件",
            "§7§l  - removereward [任务UID] [slot]  §r§l移除指定任务的奖励",
            "§7§l  - removetask [任务UID]  §r§l删除指定任务",
            "§7§l  - rename [任务UID] [新任务名]  §r§l重命名指定任务"
    };

    public EditHelpCmd(ACmd cmd) {
        super(cmd,"help");
        instance = this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(help);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
