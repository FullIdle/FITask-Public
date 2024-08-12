package me.gsqfi.fitpokecr.fitpokecr.playerdata;

import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;

import java.util.UUID;

public interface IPlayerData {
    String getPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition);
    void setPlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition, String value);
    void removePlayerTaskCondition(String playerName, UUID taskUid, Class<? extends ICondition> condition);
    void removePlayerTask(String playerName, UUID taskUid);
    void load(String playerName);
    void release(String playerName);
    void close();
}
