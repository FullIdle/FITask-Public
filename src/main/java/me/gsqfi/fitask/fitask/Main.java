package me.gsqfi.fitask.fitask;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.commands.MainCmd;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.taskComponent.conditions.ItemStackCondition;
import me.gsqfi.fitask.fitask.taskComponent.rewards.ItemStackReward;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        reloadConfig();
        /*cmd*/
        new MainCmd().register(this);
        /*listener*/
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DataPersistenceHelper(),this);
        /*condition*/
        FITaskApi.registerConditions(this,new ItemStackCondition());
        /*reward*/
        FITaskApi.registerRewards(this,new ItemStackReward());
        /*basicTask type register*/
        FITaskApi.registerTasks(this,new BasicTask());
        /*=======*/
        getLogger().info("\n" +
                "§3    ___       ___       §f___       ___       ___       ___   \n" +
                "§3   /\\  \\     /\\  \\     §f/\\  \\     /\\  \\     /\\  \\     /\\__\\  \n" +
                "§3  /::\\  \\   _\\:\\  \\    §f\\:\\  \\   /::\\  \\   /::\\  \\   /:/ _/_ \n" +
                "§3 /::\\:\\__\\ /\\/::\\__\\   §f/::\\__\\ /::\\:\\__\\ /\\:\\:\\__\\ /::-\"\\__\\\n" +
                "§3 \\/\\:\\/__/ \\::/\\/__/  §f/:/\\/__/ \\/\\::/  / \\:\\:\\/__/ \\;:;-\",-\"\n" +
                "§3    \\/__/   \\:\\__\\    §f\\/__/      /:/  /   \\::/  /   |:|  |  \n" +
                "§3             \\/__/               §f\\/__/     \\/__/     \\|__| ");
        getLogger().info("§3Plugin enabled!");
    }

    @Override
    public void reloadConfig() {
        super.saveDefaultConfig();
        super.reloadConfig();
        Bukkit.getScheduler().runTask(this,()->{
            DataPersistenceHelper.init();
            TaskDataHelper.init();
        });
    }
}