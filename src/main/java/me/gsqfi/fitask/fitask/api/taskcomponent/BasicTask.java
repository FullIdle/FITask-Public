package me.gsqfi.fitask.fitask.api.taskcomponent;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Getter
public class BasicTask implements IAdapter<BasicTask>,IDescription{
    @Setter
    private ICondition<?>[] conditions;
    @Setter
    private IReward<?>[] rewards;
    private final UUID uuid;
    private File file;
    @Setter
    private String taskName;
    @Setter
    private String description;

    public BasicTask() {
        this.uuid = UUID.randomUUID();
        this.conditions = new ICondition[0];
        this.rewards = new IReward[0];
        this.taskName = " ";
        this.description = " ";
    }

    @SneakyThrows
    private BasicTask(JsonObject object){
        JsonArray conditionArray = object.get("conditions").getAsJsonArray();
        this.conditions = new ICondition<?>[conditionArray.size()];
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

        this.taskName = object.get("taskName").getAsString();
        this.description = object.get("description").getAsString();

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
        for (IReward<?> reward : this.rewards) {
            reward.giveReward(player);
        }
    }

    /**
     *  满足所有条件返回true
     * */
    public boolean meetAllConditions(OfflinePlayer player) {
        for (ICondition<?> condition : this.conditions) {
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
        for (ICondition<?> condition : basicTask.conditions) {
            JsonObject con = new JsonObject();
            con.addProperty("type", condition.getClass().getName());
            con.add("data", DataPersistenceHelper.gson.toJsonTree(condition));
            conditionArray.add(con);
        }
        object.add("conditions", conditionArray);
        JsonArray rewardArray = new JsonArray();
        for (IReward<?> reward : basicTask.rewards) {
            JsonObject con = new JsonObject();
            con.addProperty("type", reward.getClass().getName());
            con.add("data", DataPersistenceHelper.gson.toJsonTree(reward));
            rewardArray.add(con);
        }
        object.add("rewards",rewardArray);
        object.addProperty("taskName",basicTask.taskName);
        object.addProperty("description",basicTask.description);
        object.addProperty("uuid",basicTask.uuid.toString());
        return object;
    }

    public void saveSetFile(File file) {
        if (this.file != file)
            this.file = file;
        JsonObject object = new JsonObject();
        object.addProperty("type",this.getClass().getName());
        object.add("data",DataPersistenceHelper.gson.toJsonTree(this));
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))){
            writer.write(DataPersistenceHelper.gson.toJson(object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BasicTask getTask(UUID uuid){
        return TaskDataHelper.getTask(uuid);
    }
}
