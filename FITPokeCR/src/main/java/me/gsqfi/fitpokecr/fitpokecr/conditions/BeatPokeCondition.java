package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
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
 * 击败野生宝可梦 需要指定物种 指定数量的条件
 */
@Getter
@Setter
public class BeatPokeCondition implements ICondition<BeatPokeCondition> {
    private EnumSpecies species;
    private int amount;
    private String description;

    public BeatPokeCondition() {
        species = EnumSpecies.Abomasnow;
        amount = 1;
        this.description = "Beat {species} x{amount}";
    }

    private BeatPokeCondition(JsonObject object) {
        this.species = EnumSpecies.getFromNameAnyCase(object.get("species").getAsString());
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
                    String data = Main.playerData.getPlayerTaskCondition(player.getName(), uuid, BeatPokeCondition.class);
                    if (data == null) {
                        return false;
                    }
                    YamlConfiguration yaml = new YamlConfiguration();
                    yaml.loadFromString(data);
                    return yaml.getInt(this.species.name()) >= this.amount;
                }
            }
        }
        return false;
    }

    @Override
    public BeatPokeCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BeatPokeCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(BeatPokeCondition beatPokeCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("species", beatPokeCondition.species.name());
        object.addProperty("amount", beatPokeCondition.amount);
        object.addProperty("description", beatPokeCondition.description);
        return object;
    }

    public String getDescription() {
        return description.replace("{species}", species.getLocalizedName())
                .replace("{amount}", String.valueOf(amount));
    }
}
