package me.gsqfi.fitaskgui.fitaskgui.api.playerdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 存储玩家以完成的任务的,需要存如玩家已完成的任务以及完成时的日期
 */
public interface IPlayerLastCompleteData {
    void completeTask(String playerName, UUID taskUid);
    void removeTask(String playerName, UUID taskUid);
    LocalDateTime getLastCompleteTaskTime(String playerName, UUID taskUid);
    boolean hasCompleteTask(String playerName, UUID taskUid);
    void load(String playerName);
    void release(String playerName);
    void close();
}
