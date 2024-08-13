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
 * 击败指定数量的NPC 不需要指定UID
 */
@Getter
@Setter
public class BeatNPCAmountCondition implements ICondition<BeatNPCAmountCondition> {
    private int amount;
    private String description;

    public BeatNPCAmountCondition() {
        this.amount = 1;
        this.description = "Beat NPCTrainer x{amount}";
    }

    private BeatNPCAmountCondition(JsonObject object) {
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
                    String data = Main.playerData.getPlayerTaskCondition(player.getName(), uuid, BeatNPCAmountCondition.class);
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
    public BeatNPCAmountCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BeatNPCAmountCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(BeatNPCAmountCondition beatNPCAmountCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", beatNPCAmountCondition.amount);
        object.addProperty("description", beatNPCAmountCondition.description);
        return object;
    }

    public String getDescription() {
        return description.replace("{amount}", String.valueOf(amount));
    }
}
