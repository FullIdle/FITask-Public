package me.gsqfi.fitask.fitask.api.events.player;

import lombok.Getter;
import lombok.Setter;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class PlayerSubmitTaskEvent extends Event implements Cancellable {
    private final String playerName;
    private final UUID taskUid;
    @Setter
    private boolean cancelled;

    public PlayerSubmitTaskEvent(String playerName, UUID taskUid){
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
