package me.gsqfi.fitask.fitask.api;

import lombok.Getter;
import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.api.playerdata.IPlayerData;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.UUID;


public class FITaskApi {
    @Getter
    public static IPlayerData playerData;

    public static Main getPlugin(){
        return Main.getPlugin(Main.class);
    }

    public static void registerConditions(Plugin plugin, ICondition<?>... conditions){
        DataPersistenceHelper.registerConditions(plugin,conditions);
    }
    public static void registerRewards(Plugin plugin, IReward<?>... rewards){
        DataPersistenceHelper.registerRewards(plugin,rewards);
    }

    public static void registerTasks(Plugin plugin, BasicTask... basicTasks) {
        DataPersistenceHelper.registerTasks(plugin,basicTasks);
    }

    public static BasicTask getTask(UUID uuid){
        return TaskDataHelper.getTask(uuid);
    }

    public static Collection<BasicTask> getAllTask(){
        return TaskDataHelper.cacheTask.values();
    }
}
