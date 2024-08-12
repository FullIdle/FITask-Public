package me.gsqfi.fitaskgui.fitaskgui.api.playerdata;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class YamlPlayerLastCompleteData implements IPlayerLastCompleteData{
    private final File folder;
    private final Map<String, FileConfiguration> cache = new HashMap<>();

    public YamlPlayerLastCompleteData(File folder){
        this.folder = folder;
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
    }

    @SneakyThrows
    @Override
    public void completeTask(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        config.set(taskUid.toString(),LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        config.save(this.getPlayerDataFile(playerName));
    }

    @SneakyThrows
    @Override
    public void removeTask(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        config.set(taskUid.toString(), null);
        config.save(this.getPlayerDataFile(playerName));
    }

    @Override
    public LocalDateTime getLastCompleteTaskTime(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        long time = config.getLong(taskUid.toString());
        if (time == 0L) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    @Override
    public boolean hasCompleteTask(String playerName, UUID taskUid) {
        return this.cache.get(playerName).getKeys(false).contains(taskUid.toString());
    }

    @SneakyThrows
    @Override
    public void load(String playerName) {
        File file = this.getPlayerDataFile(playerName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        this.cache.put(playerName, YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public void release(String playerName) {
        this.cache.remove(playerName);
    }

    @Override
    public void close() {
        this.cache.clear();
    }

    private File getPlayerDataFile(String playerName){
        return new File(folder, playerName + ".yml");
    }
}
