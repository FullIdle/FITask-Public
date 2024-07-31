package me.gsqfi.fitask.fitask.commands;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class ACmd implements TabExecutor {
    private final ACmd superCmd;
    private final String name;
    private final Map<String,ACmd> subCmdMap = new HashMap<>();

    public ACmd(ACmd superCmd,String name) {
        this.superCmd = superCmd;
        this.name = name;
        if (this.superCmd != null) {
            this.superCmd.subCmdMap.put(name,this);
        }
    }

    public String[] removeOneArg(String[] args){
        ArrayList<String> list = Lists.newArrayList(args);
        if (list.isEmpty()) {
            return new String[0];
        }
        list.remove(0);
        if (list.isEmpty()) {
            return new  String[0];
        }
        if (list.get(0).isEmpty()) {
            list.remove(0);
        }
        return list.toArray(new String[0]);
    }

    public ACmd nextExSub(String[] args){
        return this.subCmdMap.get(args.length > 0?args[0]:"");
    }

    public List<String> nextTab(
                      CommandSender sender,
                      Command cmd,
                      String label,
                      String[] args){
        if (args.length < 1) return Lists.newArrayList(this.subCmdMap.keySet());
        String s1 = args[0].toLowerCase();
        if (args.length == 1) return this.subCmdMap.keySet().stream().
                filter(s -> s.startsWith(s1)).collect(Collectors.toList());
        if (this.subCmdMap.containsKey(s1))
            return this.subCmdMap.get(s1).onTabComplete(sender, cmd, label, removeOneArg(args));
        return null;
    }

    public List<String> taskTabList(String[] args){
        if (args.length < 1)
            return TaskDataHelper.cacheTask.keySet().stream().map(UUID::toString).collect(Collectors.toList());
        if (args.length > 1)
            return TaskDataHelper.cacheTask.keySet().stream().map(UUID::toString).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
