package me.gsqfi.fitask.fitask.api.taskcomponent.rewards;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Type;

@Getter
@Setter
public class CommandReward implements IReward<CommandReward>{
    private String command;
    private String description;
    private BasicTask locatedTask;

    public CommandReward(){
        this.command = "console: say null command";
        this.description = "Command Reward";
    }
    private CommandReward(JsonObject json){
        this.command = json.get("command").getAsString();
        this.description = json.get("description").getAsString();
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
        }
    }

    @Override
    public CommandReward deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new CommandReward(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(CommandReward commandReward, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("command",commandReward.command);
        object.addProperty("description",commandReward.description);
        return object;
    }
}
