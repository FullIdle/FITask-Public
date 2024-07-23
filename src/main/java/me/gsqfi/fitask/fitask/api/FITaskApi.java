package me.gsqfi.fitask.fitask.api;

import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.taskComponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.taskComponent.rewards.IReward;
import org.bukkit.plugin.Plugin;


public class FITaskApi {
    public static Main getPlugin(){
        return Main.getPlugin(Main.class);
    }

    public static void registerConditions(Plugin plugin, ICondition<?>... conditions){
        DataPersistenceHelper.registerConditions(plugin,conditions);
    }
    public static void registerRewards(Plugin plugin, IReward<?>... rewards){
        DataPersistenceHelper.registerRewards(plugin,rewards);
    }
}
