package me.gsqfi.fitaskgui.fitaskgui.gui;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitaskgui.fitaskgui.api.FITaskGuiApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ServerWeeklyGui extends GuiTypeBasic {
    public ServerWeeklyGui(OfflinePlayer player, String title) {
        super(GuiType.WEEKLY, player, title);
    }

    @Override
    public void clickTaskHandler(InventoryClickEvent e, BasicTask task) {
        OfflinePlayer player = this.getPlayer();
        String playerName = player.getName();
        UUID taskUid = task.getUuid();
        HumanEntity whoClicked = e.getWhoClicked();
        whoClicked.closeInventory();

        if (FITaskApi.playerHasAcceptedTask(playerName, taskUid)) {
            if (FITaskApi.playerSubmitTask(playerName,taskUid)){
                whoClicked.sendMessage("§a完成任务成功!奖励已发放!");
            }else{
                whoClicked.sendMessage("§c提交失败任务条件不满足!");
            }
            return;
        }

        LocalDate now = LocalDate.now();
        if (FITaskGuiApi.playerData.hasCompleteTask(playerName, taskUid)) {
            LocalDate lastTime = FITaskGuiApi.playerData.getLastCompleteTaskTime(playerName, taskUid).toLocalDate();
            long between = ChronoUnit.WEEKS.between(lastTime, now);
            if (between > 0) {
                FITaskApi.playerAcceptTask(playerName, taskUid);
                whoClicked.sendMessage("§a接取任务成功!");
            }else{
                whoClicked.closeInventory();
                whoClicked.sendMessage("§c该任务本周你已完成!不可重复接取!");
            }
        }else{
            FITaskApi.playerAcceptTask(playerName, taskUid);
            whoClicked.sendMessage("§a接取任务成功!");
        }
    }
}
