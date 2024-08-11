package me.gsqfi.fitaskextra.fitaskextra.Main.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.Getter;
import lombok.Setter;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

@Getter
@Setter
public class BeatPokeCondition implements ICondition<BeatPokeCondition> {
    private EnumSpecies species;
    private int amount;

    public BeatPokeCondition(){
        species = EnumSpecies.Abomasnow;
        amount = 0;
    }

    @Override
    public boolean meet(OfflinePlayer player) {

        return false;
    }

    @Override
    public BeatPokeCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(BeatPokeCondition beatPokeCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setDescription(String description) {

    }
}
