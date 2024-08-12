package me.gsqfi.fitaskgui.fitaskgui.api.events;

import lombok.Getter;
import lombok.Setter;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class TaskCompleteEvent extends Event {
    private final Player player;
    private final BasicTask task;

    public TaskCompleteEvent(Player player, BasicTask task){
        this.player = player;
        this.task = task;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
