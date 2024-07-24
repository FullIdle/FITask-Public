package me.gsqfi.fitask.fitask.taskComponent.rewards;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

@Getter
@Setter
public class CommandReward implements IReward<CommandReward>{
    private String command;

    public CommandReward(){}
    private CommandReward(String command){
        this.command = command;
    }


    @Override
    public void giveReward(OfflinePlayer player) {

    }

    @Override
    public CommandReward deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new CommandReward(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(CommandReward commandReward, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(commandReward.command);
    }
}
