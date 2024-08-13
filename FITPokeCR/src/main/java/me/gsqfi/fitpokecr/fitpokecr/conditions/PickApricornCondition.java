package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * 采摘球果 不需要指定球果类型
 */
@Getter
@Setter
public class PickApricornCondition implements ICondition<PickApricornCondition> {
    private EnumApricorns type;
    private int amount;
    private String description;

    public PickApricornCondition() {
        this.type = EnumApricorns.Red;
        this.amount = 1;
        this.description = "Picking Apricorn[{type}] x{amount}";
    }

    private PickApricornCondition(JsonObject object) {
        this.type = EnumApricorns.valueOf(object.get("type").getAsString());
        this.amount = object.get("amount").getAsInt();
        this.description = object.get("description").getAsString();
    }

    /**
     * 玩家如果没有接取包含该条件实例化的情况下,返回值始终返回false
     */
    @SneakyThrows
    @Override
    public boolean meet(OfflinePlayer player) {
        for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(player.getName()).keySet()) {
            for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                if (condition == this) {
                    String data = Main.playerData.getPlayerTaskCondition(player.getName(), uuid, PickApricornCondition.class);
                    if (data == null) {
                        return false;
                    }
                    YamlConfiguration yaml = new YamlConfiguration();
                    yaml.loadFromString(data);
                    return yaml.getInt(this.type.name()) >= this.amount;
                }
            }
        }
        return false;
    }

    @Override
    public PickApricornCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new PickApricornCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(PickApricornCondition beatNPCCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("type", beatNPCCondition.type.name());
        object.addProperty("amount", beatNPCCondition.amount);
        object.addProperty("description", beatNPCCondition.description);
        return object;
    }

    public String getDescription() {
        return description
                .replace("{type}", type.name())
                .replace("{amount}", String.valueOf(amount));
    }
}
