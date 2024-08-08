package me.gsqfi.fitask.fitask.api.playerdata;

import java.util.List;
import java.util.UUID;

public interface IPlayerData {
    boolean accept(String playerName, UUID taskUid);
    boolean abandon(String playerName, UUID taskUid);
    List<UUID> getAllAcceptedTasks(String playerName);
    boolean isAccept(String playerName, UUID taskUid);
    void load(String playerName);
    void release(String playerName);
    void close();
}
