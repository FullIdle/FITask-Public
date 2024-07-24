package me.gsqfi.fitask.fitask.taskComponent.rewards;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
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
        String papi = PlaceholderAPI.setPlaceholders(player,command.replace('&', '§'));
        if (command.startsWith("tell: ")) {
            player.getPlayer().sendMessage(papi.substring(6));
            return;
        }
        if (command.startsWith("command: ")){
            Bukkit.dispatchCommand(player.getPlayer(),papi.substring(9));
            return;
        }
        if (command.startsWith("op: ")){
            boolean op = player.isOp();
            player.setOp(true);
            Bukkit.dispatchCommand(player.getPlayer(),papi.substring(4));
            player.setOp(op);
            return;
        }
        if (command.startsWith("console: ")){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),papi.substring(9));
            return;
        }
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
