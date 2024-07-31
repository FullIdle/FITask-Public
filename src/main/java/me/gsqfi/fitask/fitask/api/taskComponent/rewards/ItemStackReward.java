package me.gsqfi.fitask.fitask.api.taskComponent.rewards;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

@Getter
@Setter
public class ItemStackReward implements IReward<ItemStackReward> {
    private Material material;
    private int amount;
    private String description;

    public ItemStackReward(){}
    private ItemStackReward(JsonObject object){
        this.material = Material.getMaterial(object.get("material").getAsString());
        this.amount = object.get("amount").getAsInt();
        this.description = object.get("description").getAsString();
    }

    @Override
    public void giveReward(OfflinePlayer player) {
        Player p = player.getPlayer();
        ItemStack itemStack = new ItemStack(this.material);
        itemStack.setAmount(this.amount);
        Item item = p.getWorld().dropItem(p.getEyeLocation(), itemStack);
        item.setInvulnerable(true);
        item.setPickupDelay(0);
        item.setGlowing(true);
        item.setGravity(false);
        item.setFallDistance(0);
    }

    @Override
    public ItemStackReward deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ItemStackReward(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(ItemStackReward o, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("material",o.material.name());
        object.addProperty("amount",o.amount);
        object.addProperty("description",o.description);
        return object;
    }
}
