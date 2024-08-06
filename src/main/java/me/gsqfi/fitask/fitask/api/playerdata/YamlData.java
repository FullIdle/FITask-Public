package me.gsqfi.fitask.fitask.api.playerdata;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class YamlData implements IPlayerData {
    private final Map<String, FileConfiguration> cache = new HashMap<>();
    private final File dataFolder;

    @SneakyThrows
    public YamlData(File folder) {
        this.dataFolder = folder;
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
    }

    @Override
    public boolean accept(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        List<String> list = config.getStringList("accepted");
        String uid = taskUid.toString();
        if (!list.contains(uid)) {
            list.add(uid);
            config.set("accepted", list);
            this.save(playerName);
            return true;
        }
        return false;
    }

    @Override
    public boolean abandon(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        List<String> list = config.getStringList("accepted");
        String uid = taskUid.toString();
        if (list.contains(uid)) {
            list.remove(uid);
            config.set("accepted", list);
            this.save(playerName);
            return true;
        }
        return false;
    }

    @Override
    public List<UUID> getAllAcceptedTasks(String playerName) {
        return this.cache.get(playerName).getStringList(playerName)
                .stream().map(UUID::fromString).collect(Collectors.toList());
    }

    @Override
    public boolean isAccept(String playerName, UUID taskUid) {
        return this.cache.get(playerName).getStringList(playerName).contains(taskUid.toString());
    }

    @SneakyThrows
    @Override
    public void load(String playerName) {
        File file = new File(this.dataFolder, playerName + ".yml");
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

    @SneakyThrows
    public void save(String playerName) {
        this.cache.get(playerName).save(new File(this.dataFolder, playerName + ".yml"));
    }
}
