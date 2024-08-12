package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.Main;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * 击败野生宝可梦 需要指定数量不需要指定物种的条件
 */
@Getter
@Setter
public class BeatPokeAmountCondition implements ICondition<BeatPokeAmountCondition> {
    private int amount;
    private String description;

    public BeatPokeAmountCondition() {
        amount = 0;
        this.description = "Capture {amount} pokemon";
    }

    private BeatPokeAmountCondition(JsonObject object) {
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
                    String data = Main.playerData.getPlayerTaskCondition(player.getName(), uuid, BeatPokeAmountCondition.class);
                    if (data == null) {
                        return false;
                    }
                    return Integer.parseInt(data) >= this.amount;
                }
            }
        }
        return false;
    }

    @Override
    public BeatPokeAmountCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BeatPokeAmountCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(BeatPokeAmountCondition beatPokeCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", beatPokeCondition.getAmount());
        object.addProperty("description", beatPokeCondition.getDescription());
        return object;
    }

    public String getDescription() {
        return description.replace("{amount}", String.valueOf(amount));
    }
}
