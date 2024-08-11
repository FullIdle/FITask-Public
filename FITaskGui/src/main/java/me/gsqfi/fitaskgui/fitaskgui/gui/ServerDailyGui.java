package me.gsqfi.fitaskgui.fitaskgui.gui;

import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ServerDailyGui extends GuiTypeBasic {
    protected ServerDailyGui(OfflinePlayer player, String title) {
        super(GuiType.DAILY, player, title);
    }

    @Override
    public void clickTaskHandler(InventoryClickEvent e, BasicTask task) {
        OfflinePlayer player = this.getPlayer();

    }
}
