package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.Main;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;
import java.util.UUID;


/** 
 * 进化精灵 指定次数
 * */
@Getter
@Setter
public class EvolvePokeCondition implements ICondition<EvolvePokeCondition> {
    private int amount;
    private String description;
    
    public EvolvePokeCondition(){
        this.amount = 1;
        this.description = "Evolve Poke x{amount}";
    }
    
    @Override
    public boolean meet(OfflinePlayer player) {
        for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(player.getName()).keySet()) {
            for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                if (condition == this) {
                    String data = Main.playerData.getPlayerTaskCondition(player.getName(), uuid, EvolvePokeCondition.class);
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
    public EvolvePokeCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        EvolvePokeCondition epc = new EvolvePokeCondition();
        JsonObject object = jsonElement.getAsJsonObject();
        epc.setAmount(object.get("amount").getAsInt());
        epc.setDescription(object.get("description").getAsString());
        return epc;
    }

    @Override
    public JsonElement serialize(EvolvePokeCondition evolvePokeCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("amount", evolvePokeCondition.getAmount());
        object.addProperty("description", evolvePokeCondition.getDescription());
        return object;
    }

    @Override
    public String getDescription() {
        return this.description.replace("{amount}", String.valueOf(this.amount));
    }
}
