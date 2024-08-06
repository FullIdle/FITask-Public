package me.gsqfi.fitask.fitask;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.playerdata.YamlData;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.PapiCondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.CommandReward;
import me.gsqfi.fitask.fitask.commands.MainCmd;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ItemStackCondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.ItemStackReward;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        reloadConfig();
        /*cmd*/
        new MainCmd().register(this);
        /*listener*/
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DataPersistenceHelper(),this);
        pluginManager.registerEvents(FITaskApi.playerData,this);
        /*condition*/
        FITaskApi.registerConditions(this,new ItemStackCondition());
        FITaskApi.registerConditions(this,new PapiCondition());
        /*reward*/
        FITaskApi.registerRewards(this,new ItemStackReward());
        FITaskApi.registerRewards(this,new CommandReward());
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
        System.out.println(new File("test").getAbsolutePath());
    }

    @Override
    public void reloadConfig() {
        super.saveDefaultConfig();
        super.reloadConfig();
        String path = this.getConfig().getString("database.url");
        if (this.getConfig().getString("storage-method","").equalsIgnoreCase("mysql")) {

        }else{
            File file;
            if (path == null || path.isEmpty()){
                file = new File(this.getDataFolder(),"playerdata.yml");
            }else{
                file = new File(path);
            }
            FITaskApi.playerData =  new YamlData(file);
        }
        Bukkit.getScheduler().runTask(this,()->{
            DataPersistenceHelper.gsonInit();
            TaskDataHelper.init();
        });
    }
}