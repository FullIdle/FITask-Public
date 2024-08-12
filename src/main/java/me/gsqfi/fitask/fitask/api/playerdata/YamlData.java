package me.gsqfi.fitask.fitask.api.playerdata;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
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
        ConfigurationSection section = config.getConfigurationSection("accepted");
        String uid = taskUid.toString();
        if (section == null || !section.getKeys(false).contains(uid)) {
            config.set("accepted."+uid, LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            this.save(playerName);
            return true;
        }
        return false;
    }

    @Override
    public boolean abandon(String playerName, UUID taskUid) {
        FileConfiguration config = this.cache.get(playerName);
        ConfigurationSection section = config.getConfigurationSection("accepted");
        String uid = taskUid.toString();
        if (section != null && section.getKeys(false).contains(uid)) {
            config.set("accepted."+uid, null);
            this.save(playerName);
            return true;
        }
        return false;
    }

    @Override
    public Map<UUID, LocalDateTime> getAllAcceptedTasks(String playerName) {
        ConfigurationSection section = this.cache.get(playerName).getConfigurationSection("accepted");
        return section == null ? Collections.emptyMap() :
                section.getKeys(false).stream().map(UUID::fromString)
                        .collect(Collectors.toMap(Function.identity(), uid -> this.getAcceptTime(playerName, uid)));
    }

    @Override
    public boolean isAccept(String playerName, UUID taskUid) {
        ConfigurationSection section = this.cache.get(playerName).getConfigurationSection("accepted");
        return section != null && section.getKeys(false).contains(taskUid.toString());
    }

    @Override
    public LocalDateTime getAcceptTime(String playerName, UUID taskUid) {
        long l = this.cache.get(playerName).getLong("accepted." + taskUid.toString());
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
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
