package me.gsqfi.fitask.fitask.api.taskcomponent.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

@Getter
@Setter
public class PapiCondition implements ICondition<PapiCondition>{
    private String papi;
    private String contrastValue;
    private String description;
    private BasicTask locatedTask;

    public PapiCondition(){
        this.papi = "%player_name%";
        this.contrastValue = "player_name";
        this.description = "The condition is met when the variable {papi} is {contrastValue}";
    }
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
        return this.description.replace("{papi}",papi)
                .replace("{contrastValue}",contrastValue);
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

    /*
    * {
    *   "type": "me.gsqfi.fitask.fitask.api.taskcomponent.conditions.PapiCondition",
    *   "data":{
    *     "papi": "",
    *     "contrastValue": "",
    *     "description": ""
    *   }
    * }
    * 该条件的基础模板
    * */
}
