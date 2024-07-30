package me.gsqfi.fitask.fitask.helpers;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.Main;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskComponent.BasicTask;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskDataHelper {
    public static final Map<UUID, BasicTask> cacheTask = new HashMap<>();
    public static File dataFolder;

    @SneakyThrows
    public static void init(){
        Main plugin = FITaskApi.getPlugin();
        cacheTask.clear();
        dataFolder = new File(plugin.getDataFolder(),"tasks");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        loadFolder(dataFolder);
    }

    @SneakyThrows
    private static void loadFolder(File folder){
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    loadFolder(file);
                    continue;
                }
                FileReader fileReader = new FileReader(file);
                JsonObject object = DataPersistenceHelper.gson.fromJson(fileReader, JsonObject.class);
                fileReader.close();
                BasicTask task = DataPersistenceHelper.gson.fromJson(object.get("data").getAsJsonObject(),
                        ((Class<? extends BasicTask>) Class.forName(object.get("type").getAsString())));
                cacheTask.put(task.getUuid(),task);
                task.saveSetFile(file);
            }
        }
    }

    public static BasicTask getTask(UUID uuid){
        return cacheTask.get(uuid);
    }

    /**
     * 将新建的任务加如缓存
     * 这个方法会调用一次saveSetFile
     * 如果没有file可保存将自动设置以uuid为名的json文件
     * 文件一定要是FITask插件的文件夹内的tasks文件夹内的文件
     */
    public static void addTaskInCache(BasicTask task){
        cacheTask.put(task.getUuid(),task);
        System.out.println(task.getUuid());
        if (task.getFile() == null) {
            task.saveSetFile(new File(dataFolder,task.getUuid()+".json"));
            return;
        }
        task.saveSetFile(task.getFile());
    }

    public static BasicTask removeTaskInCache(UUID uuid){
        return cacheTask.remove(uuid);
    }
}
