package me.gsqfi.fitask.fitask.commands;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.command.TabExecutor;

import java.util.*;

@Getter
public abstract class ACmd implements TabExecutor {
    private final ACmd superCmd;
    private final String name;
    private final Map<String,ACmd> subCmdMap = new HashMap<>();

    public ACmd(ACmd superCmd,String name) {
        this.superCmd = superCmd;
        this.name = name;
        if (this.superCmd != null) {
            this.superCmd.subCmdMap.put(name,superCmd);
        }
    }

    public List<String> getSubCmdNames(){
        if (this.subCmdMap.isEmpty()) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(this.subCmdMap.keySet());
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
}
