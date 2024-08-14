package me.gsqfi.fitpokecr.fitpokecr.conditions;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumShrine;
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
 * 激活指定的三鸟之一的祭坛 次数 条件 需要指定祭坛
 */
@Getter
@Setter
public class ActivateShrineCondition implements ICondition<ActivateShrineCondition> {
    private EnumShrine type;
    private int amount;
    private String description;
    private BasicTask locatedTask;

    public ActivateShrineCondition() {
        this.type = EnumShrine.Articuno;
        this.amount = 1;
        this.description = "Activate Shrine {type} x{amount}";
    }

    private ActivateShrineCondition(JsonObject object) {
        this.type = EnumShrine.valueOf(object.get("type").getAsString());
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
                ActivateShrineCondition.class);
        if (data == null) {
            return false;
        }
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.loadFromString(data);
        return yaml.getInt(this.type.name()) >= this.amount;
    }

    @Override
    public ActivateShrineCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ActivateShrineCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(ActivateShrineCondition activateShrineCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("type", activateShrineCondition.type.name());
        object.addProperty("amount", activateShrineCondition.amount);
        object.addProperty("description", activateShrineCondition.description);
        return object;
    }

    public String getDescription() {
        return description
                .replace("{type}", type.name())
                .replace("{amount}", String.valueOf(amount));
    }
}
