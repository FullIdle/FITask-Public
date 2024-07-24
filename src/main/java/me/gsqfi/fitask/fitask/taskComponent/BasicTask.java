package me.gsqfi.fitask.fitask.taskComponent;

import com.google.common.io.Files;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.IAdapter;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.taskComponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.taskComponent.rewards.IReward;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.UUID;

@Setter
@Getter
public class BasicTask implements IAdapter<BasicTask> {
    /*这是个必要方法*/
    public static BasicTask deserialize(String json){
        return new BasicTask(json);
    }

    private ICondition[] conditions;
    private IReward[] rewards;
    private final UUID uuid;

    public BasicTask() {
        uuid = UUID.randomUUID();
    }

    private BasicTask(String json) {
        this(DataPersistenceHelper.gson.fromJson(json, JsonObject.class));
    }

    @SneakyThrows
    private BasicTask(JsonObject object){
        JsonArray conditionArray = object.get("conditions").getAsJsonArray();
        this.conditions = new ICondition[conditionArray.size()];
        for (int i = 0; i < conditionArray.size(); i++) {
            JsonObject con = (JsonObject) conditionArray.get(i);
            this.conditions[i] = DataPersistenceHelper.gson.fromJson(con.get("data"),
                    ((Class<? extends ICondition>) Class.forName(con.get("type").getAsString())));
        }
        JsonArray rewardArray = object.get("rewards").getAsJsonArray();
        this.rewards = new IReward[rewardArray.size()];
        for (int i = 0; i < rewardArray.size(); i++) {
            JsonObject con = (JsonObject) rewardArray.get(i);
            this.rewards[i] = DataPersistenceHelper.gson.fromJson(con.get("data"),
                    ((Class<? extends IReward>) Class.forName(con.get("type").getAsString())));
        }
        if (object.has("uuid")) {
            this.uuid = UUID.fromString(object.get("uuid").getAsString());
        }else{
            this.uuid = UUID.randomUUID();
        }
    }

    /**
     * 给玩家这个任务的所有奖品
     */
    public void givePlayerRewards(OfflinePlayer player) {
        for (IReward reward : this.rewards) {
            reward.giveReward(player);
        }
    }

    /**
     *  满足所有条件返回true
     * */
    public boolean meetAllConditions(OfflinePlayer player) {
        for (ICondition condition : this.conditions) {
            if (!condition.meet(player)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BasicTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BasicTask(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(BasicTask basicTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        JsonArray conditionArray = new JsonArray();
        for (ICondition condition : basicTask.conditions) {
            JsonObject con = new JsonObject();
            con.addProperty("type", condition.getClass().getName());
            con.add("data", DataPersistenceHelper.gson.toJsonTree(con));
            conditionArray.add(con);
        }
        object.add("conditions", conditionArray);
        JsonArray rewardArray = new JsonArray();
        for (IReward reward : basicTask.rewards) {
            JsonObject con = new JsonObject();
            con.addProperty("type", reward.getClass().getName());
            con.add("data", DataPersistenceHelper.gson.toJsonTree(reward));
            rewardArray.add(con);
        }
        object.addProperty("uuid",uuid.toString());
        return object;
    }

    @SneakyThrows
    public void save(File file){
        JsonObject object = new JsonObject();
        object.addProperty("type",this.getClass().getName());
        object.add("data",DataPersistenceHelper.gson.toJsonTree(this));
        DataPersistenceHelper.gson.toJson(object,JsonObject.class, new JsonWriter(new FileWriter(file)));
    }
}
