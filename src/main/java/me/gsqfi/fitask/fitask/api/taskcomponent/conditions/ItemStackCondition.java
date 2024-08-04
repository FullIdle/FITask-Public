package me.gsqfi.fitask.fitask.api.taskcomponent.conditions;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

@Getter
@Setter
public class ItemStackCondition implements ICondition<ItemStackCondition>{
    private Material material;
    private int amount;
    private String description;

    public ItemStackCondition(){
        this.material = Material.STONE;
        this.amount = 1;
        this.description = "You need {amount} {material}";
    }
    private ItemStackCondition(JsonObject object){
        this.material = Material.getMaterial(object.get("material").getAsString());
        this.amount = object.get("amount").getAsInt();
        this.description = object.get("description").getAsString();
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
    public String getDescription() {
        return description
                .replace("{amount}",String.valueOf(this.amount))
                .replace("{material}",this.material.name());
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
        object.addProperty("description",itemStackCondition.description);
        return object;
    }
}
