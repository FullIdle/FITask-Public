package me.gsqfi.fitask.fitask.api.events.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class PlayerAcceptTaskEvent extends Event implements Cancellable {
    private final String playerName;
    private final UUID taskUid;
    @Setter
    private boolean cancelled;

    public PlayerAcceptTaskEvent(String playerName, UUID taskUid){
        this.playerName = playerName;
        this.taskUid = taskUid;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
