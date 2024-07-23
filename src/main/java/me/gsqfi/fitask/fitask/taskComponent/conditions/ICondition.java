package me.gsqfi.fitask.fitask.taskComponent.conditions;

import me.gsqfi.fitask.fitask.IAdapter;
import org.bukkit.OfflinePlayer;

public interface ICondition<T> extends IAdapter<T> {
    boolean meet(OfflinePlayer player);
}
