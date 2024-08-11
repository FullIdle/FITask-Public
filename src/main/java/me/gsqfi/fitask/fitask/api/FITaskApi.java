package me.gsqfi.fitask.fitask.api;

import lombok.Getter;
import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.api.events.player.PlayerAbandonTaskEvent;
import me.gsqfi.fitask.fitask.api.events.player.PlayerAcceptTaskEvent;
import me.gsqfi.fitask.fitask.api.playerdata.IPlayerData;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.UUID;


public class FITaskApi {
    @Getter
    public static IPlayerData playerData;

    public static Main getPlugin(){
        return Main.getPlugin(Main.class);
    }

    /**
     * API类内调用会播事件
     */
    public static boolean playerAcceptTask(OfflinePlayer player, BasicTask task){
        return playerAcceptTask(player.getName(),task.getUuid());
    }
    /**
     * API类内调用会播事件
     */
    public static boolean playerAcceptTask(String playerName, UUID taskUid){
        PlayerAcceptTaskEvent event = new PlayerAcceptTaskEvent(playerName, taskUid);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        return playerData.accept(playerName, taskUid);
    }
    /**
     * API类内调用会播事件
     */
    public static boolean playerAbandonTask(OfflinePlayer player, BasicTask task) {
        return playerAbandonTask(player.getName(),task.getUuid());
    }
    /**
     * API类内调用会播事件
     */
    public static boolean playerAbandonTask(String playerName, UUID uuid) {
        PlayerAbandonTaskEvent event = new PlayerAbandonTaskEvent(playerName, uuid);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        return playerData.abandon(playerName, uuid);
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

    public static boolean playerHasAcceptedTask(String playerName, UUID taskUid) {
        return playerData.isAccept(playerName,taskUid);
    }
}
