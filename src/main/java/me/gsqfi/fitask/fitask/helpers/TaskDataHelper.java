package me.gsqfi.fitask.fitask.helpers;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.taskComponent.rewards.IReward;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskDataHelper {
    private static Map<UUID, BasicTask> cacheTask = new HashMap<>();

    @SneakyThrows
    public static void init(){
        Main plugin = FITaskApi.getPlugin();
        cacheTask.clear();
        File dataFolder = new File(plugin.getDataFolder(),"tasks");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File[] files = dataFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                FileReader fileReader = new FileReader(file);
                JsonObject object = DataPersistenceHelper.gson.fromJson(fileReader, JsonObject.class);
                BasicTask task = DataPersistenceHelper.gson.fromJson(object.get("data").getAsJsonObject(),
                        ((Class<? extends BasicTask>) Class.forName(object.get("type").getAsString())));
                cacheTask.put(task.getUuid(),task);
                task.save(file);
            }
        }
    }
}
