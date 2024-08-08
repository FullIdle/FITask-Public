package me.gsqfi.fitaskgui.fitaskgui.Main.gui;

import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

@Getter
public class OverallGui extends ListenerInvHolder {
    private final Inventory inventory;

    public OverallGui() {
        this.inventory = Bukkit.createInventory(this, 27, "Overall");
        {
            ItemStack itemStack = new ItemStack(Arrays.stream(Material.values()).filter(m -> m.name().toLowerCase().endsWith("STAINED_GLASS_PANE")).findFirst().get());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");
            itemStack.setItemMeta(itemMeta);
            for (int i = 0; i < 27; i++) {
                if (!empty.contains(i)) {
                    this.inventory.setItem(i,itemStack);
                }
            }
        }
    }


    private static final List<Integer> empty = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16
    );
}
