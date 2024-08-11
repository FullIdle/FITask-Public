package me.gsqfi.fitaskgui.fitaskgui.gui;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitaskgui.fitaskgui.api.FITaskGuiApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.LocalDate;
import java.util.UUID;

public class ServerDailyGui extends GuiTypeBasic {
    protected ServerDailyGui(OfflinePlayer player, String title) {
        super(GuiType.DAILY, player, title);
    }

    @Override
    public void clickTaskHandler(InventoryClickEvent e, BasicTask task) {
        OfflinePlayer player = this.getPlayer();
        String playerName = player.getName();
        UUID taskUid = task.getUuid();
        HumanEntity whoClicked = e.getWhoClicked();
        whoClicked.closeInventory();

        if (FITaskApi.playerHasAcceptedTask(playerName, taskUid)) {
            if (task.meetAllConditions(player)) {
                FITaskGuiApi.playerData.completeTask(playerName, taskUid);
                task.givePlayerRewards(player);
                whoClicked.sendMessage("§a完成任务成功!奖励已发放!");
            }else {
                whoClicked.sendMessage("§c提交失败任务条件不满足!");
            }
            return;
        }

        LocalDate now = LocalDate.now();
        if (FITaskGuiApi.playerData.hasCompleteTask(playerName, taskUid)) {
            //有完成记录判断是否可以接取
            LocalDate lastTime = FITaskGuiApi.playerData.getLastCompleteTaskTime(playerName, taskUid);
            if (lastTime.isBefore(now)) {
                //可以接取
                FITaskApi.playerAcceptTask(playerName, taskUid);
                whoClicked.sendMessage("§a接取任务成功!");
            } else {
                whoClicked.closeInventory();
                whoClicked.sendMessage("§c该任务今日你已完成!不可重复接取!");
            }
        }else{
            //没有完成记录直接接取
            FITaskApi.playerAcceptTask(playerName, taskUid);
        }
    }
}
