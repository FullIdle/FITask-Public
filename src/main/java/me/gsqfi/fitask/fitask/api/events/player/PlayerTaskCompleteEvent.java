package me.gsqfi.fitask.fitask.api.events.player;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class PlayerTaskCompleteEvent extends Event {
    private final String playerName;
    private final UUID taskUid;

    public PlayerTaskCompleteEvent(String playerName, UUID taskUid){
        this.playerName = playerName;
        this.taskUid = taskUid;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
}
