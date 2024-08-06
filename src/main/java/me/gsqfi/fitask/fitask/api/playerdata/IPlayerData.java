package me.gsqfi.fitask.fitask.api.playerdata;

import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public interface IPlayerData extends Listener {
    boolean accept(String playerName, UUID taskUid);
    boolean abandon(String playerName, UUID taskUid);
    List<UUID> getAllAcceptedTasks(String playerName);
    boolean isAccept(String playerName, UUID taskUid);
    void load(String playerName);
    void release(String playerName);
}
