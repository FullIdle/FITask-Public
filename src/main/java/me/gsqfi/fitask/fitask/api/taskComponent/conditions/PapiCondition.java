package me.gsqfi.fitask.fitask.api.taskComponent.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

@Getter
@Setter
public class PapiCondition implements ICondition<PapiCondition>{
    private String papi;
    private String contrastValue;
    private String description;

    public PapiCondition(){}
    public PapiCondition(JsonObject object){
        this.papi = object.get("papi").getAsString();
        this.contrastValue = object.get("contrastValue").getAsString();
        this.description = object.get("description").getAsString();
    }

    @Override
    public boolean meet(OfflinePlayer player) {
        return PlaceholderAPI.setPlaceholders(player, papi).equals(contrastValue);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PapiCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new PapiCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(PapiCondition papiCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("papi",papiCondition.papi);
        object.addProperty("contrastValue",papiCondition.contrastValue);
        object.addProperty("description",papiCondition.description);
        return object;
    }
}
