package me.gsqfi.fitask.fitask.taskComponent.conditions;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.gsqfi.fitask.fitask.helpers.DataPersistenceHelper;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/*一个实例条件*/
@Getter
@Setter
public class ItemStackCondition implements ICondition<ItemStackCondition>{
    /*这个是必要的方法*/
    public static ItemStackCondition deserialize(String json){
        return new ItemStackCondition(json);
    }

    private Material material;
    private int amount;

    public ItemStackCondition(){}
    private ItemStackCondition(String json){
        this(DataPersistenceHelper.gson.fromJson(json, JsonObject.class));
    }
    private ItemStackCondition(JsonObject object){
        this.material = Material.getMaterial(object.get("material").getAsString());
        this.amount = object.get("amount").getAsInt();
    }

    @Override
    public boolean meet(OfflinePlayer player) {
        Player p = player.getPlayer();
        int total = 0;
        for (ItemStack value : p.getInventory().all(this.material).values()) {
            total += value.getAmount();
        }
        return total >= this.amount;
    }

    @Override
    public ItemStackCondition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ItemStackCondition(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(ItemStackCondition itemStackCondition, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("material",itemStackCondition.material.name());
        object.addProperty("amount",itemStackCondition.amount);
        return object;
    }
}
