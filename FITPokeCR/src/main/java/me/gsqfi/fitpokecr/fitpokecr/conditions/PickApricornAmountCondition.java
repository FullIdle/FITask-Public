package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.Main;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

/**
 * 采摘球果 不需要指定球果类型
 */
@Getter
@Setter
public class PickApricornAmountCondition implements ICondition<PickApricornAmountCondition> {
    private int amount;
    private String description;
    private BasicTask locatedTask;

    public PickApricornAmountCondition() {
        this.amount = 1;
        this.description = "Picking Apricorn x{amount}";
    }

    private PickApricornAmountCondition(JsonObject object) {
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
                PickApricornAmountCondition.class);
        if (data == null) {
            return false;
        }
        return Integer.parseInt(data) >= this.amount;
    }

    @Override
    public PickApricornAmountCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new PickApricornAmountCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(PickApricornAmountCondition beatNPCCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", beatNPCCondition.amount);
        object.addProperty("description", beatNPCCondition.description);
        return object;
    }

    public String getDescription() {
        return description.replace("{amount}", String.valueOf(amount));
    }
}
