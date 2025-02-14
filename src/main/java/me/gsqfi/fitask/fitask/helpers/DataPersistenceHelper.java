package me.gsqfi.fitask.fitask.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.gsqfi.fitask.fitask.api.playerdata.IPlayerData;
import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class DataPersistenceHelper implements Listener {
    public static Map<Plugin,Map<Class<? extends IAdapter>,IAdapter<?>>> allAdapter = new HashMap<>();
    public static Gson gson;

    public static void gsonInit(){
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        for (Map<Class<? extends IAdapter>, IAdapter<?>> value : allAdapter.values()) {
            for (Map.Entry<Class<? extends IAdapter>, IAdapter<?>> entry : value.entrySet()) {
                builder.registerTypeAdapter(entry.getKey(),entry.getValue());
            }
        }
        gson = builder.create();
    }

    public static void registerTasks(Plugin plugin, BasicTask... basicTasks){
        registerAdapters(plugin,basicTasks);
    }
    public static void registerConditions(Plugin plugin,ICondition<?>... conditions){
        registerAdapters(plugin,conditions);
    }
    public static void registerRewards(Plugin plugin, IReward<?>... rewards){
        registerAdapters(plugin,rewards);
    }
    private static void registerAdapters(Plugin plugin,IAdapter<?>... adapters){
        Map<Class<? extends IAdapter>, IAdapter<?>> map = allAdapter.computeIfAbsent(plugin, k -> new HashMap<>());
        for (IAdapter<?> adapter : adapters) {
            map.put(adapter.getClass(),adapter);
        }
        if (gson != null){
            gsonInit();
        }
    }

    public static void unregister(Plugin plugin){
        allAdapter.remove(plugin);
        gsonInit();
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e){
        unregister(e.getPlugin());
    }
}
