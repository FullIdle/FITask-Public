package me.gsqfi.fitpokecr.fitpokecr.playerdata;

import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlPlayerData implements IPlayerData{
    private final File folder;
    private final Map<String, FileConfiguration> cache;

    public YamlPlayerData(File folder){
        this.folder = folder;
        this.cache = new HashMap<>();
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
    }

    @Override
    public String getPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition) {
        if (!this.cache.containsKey(playerName)) {
            this.load(playerName);
        }
        return this.cache.get(playerName).getString(String.format("%s.%s", taskUid.toString(), condition.getName()));
    }

    @SneakyThrows
    @Override
    public void setPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition, String value) {
        FileConfiguration config = this.cache.get(playerName);
        config.set(String.format("%s.%s", taskUid.toString(), condition.getName()), value);
        config.save(this.getPlayerFile(playerName));
    }

    @Override
    public void removePlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition) {
        setPlayerTaskCondition(playerName, taskUid, condition, null);
    }

    @Override
    public void removePlayerTask(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        config.set(taskUid.toString(),null);
    }

    @SneakyThrows
    @Override
    public void load(String playerName) {
        File file = getPlayerFile(playerName);
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

    private File getPlayerFile(String playerName){
        return new File(folder,playerName+".yml");
    }
}
