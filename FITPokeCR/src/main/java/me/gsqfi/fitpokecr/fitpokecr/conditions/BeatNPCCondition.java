package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * 击败指定数量的NPC 需要指定UUID
 */
@Getter
@Setter
public class BeatNPCCondition implements ICondition<BeatNPCCondition> {
    private UUID uuid;
    private int amount;
    private String description;
    private BasicTask locatedTask;

    public BeatNPCCondition() {
        this.uuid = null;
        this.amount = 1;
        this.description = "Defeat the NPC with UUID {uuid} x{amount} times";
    }

    private BeatNPCCondition(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.amount = object.get("amount").getAsInt();
        this.description = object.get("description").getAsString();
    }

    /**
     * 玩家如果没有接取包含该条件实例化的情况下,返回值始终返回false
     */
    @SneakyThrows
    @Override
    public boolean meet(OfflinePlayer player) {
        String data = Main.playerData.getPlayerTaskCondition(player.getName(), this.locatedTask.getUuid(),
                BeatNPCCondition.class);
        if (data == null) {
            return false;
        }
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.loadFromString(data);
        return yaml.getInt(this.uuid.toString()) >= this.amount;
    }

    @Override
    public BeatNPCCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BeatNPCCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(BeatNPCCondition beatNPCCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", beatNPCCondition.uuid.toString());
        object.addProperty("amount", beatNPCCondition.amount);
        object.addProperty("description", beatNPCCondition.description);
        return object;
    }

    public String getDescription() {
        return description.replace("{uuid}", uuid.toString())
                .replace("{amount}", String.valueOf(amount));
    }
}
