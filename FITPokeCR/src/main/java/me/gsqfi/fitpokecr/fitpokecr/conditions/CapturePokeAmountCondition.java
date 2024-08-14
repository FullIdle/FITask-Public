package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
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
 * 复活宝可梦 需要指定数量不需要指定物种的条件
 */
@Getter
@Setter
public class CapturePokeAmountCondition implements ICondition<CapturePokeAmountCondition> {
    private int amount;
    private String description;
    private BasicTask locatedTask;

    public CapturePokeAmountCondition(){
        this.amount = 1;
        this.description = "Capture {amount} Pokémon";
    }
    public CapturePokeAmountCondition(JsonObject object){
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
                CapturePokeAmountCondition.class);
        if (data == null) {
            return false;
        }
        return Integer.parseInt(data) >= this.amount;
    }

    @Override
    public CapturePokeAmountCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new CapturePokeAmountCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(CapturePokeAmountCondition capturePokemon, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", capturePokemon.amount);
        object.addProperty("description", capturePokemon.description);
        return object;
    }

    @Override
    public String getDescription() {
        return this.description.replace("{amount}", String.valueOf(this.amount));
    }
}
