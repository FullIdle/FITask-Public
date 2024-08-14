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

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * 激活三鸟其中一个祭坛 次数 条件 不指定祭坛
 */
@Getter
@Setter
public class ActivateShrineAmountCondition implements ICondition<ActivateShrineAmountCondition> {
    private int amount;
    private String description;
    private BasicTask locatedTask;

    public ActivateShrineAmountCondition() {
        this.amount = 1;
        this.description = "Activate Shrine x{amount}";
    }

    private ActivateShrineAmountCondition(JsonObject object) {
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
                ActivateShrineAmountCondition.class);
        if (data == null) {
            return false;
        }
        return Integer.parseInt(data) >= this.amount;
    }

    @Override
    public ActivateShrineAmountCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ActivateShrineAmountCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(ActivateShrineAmountCondition activateShrineAmountCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", activateShrineAmountCondition.amount);
        object.addProperty("description", activateShrineAmountCondition.description);
        return object;
    }

    public String getDescription() {
        return description.replace("{amount}", String.valueOf(amount));
    }
}
