package me.gsqfi.fitask.fitask.api.taskComponent.conditions;

import me.gsqfi.fitask.fitask.api.taskComponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskComponent.IDescription;
import org.bukkit.OfflinePlayer;

public interface ICondition<T> extends IAdapter<T>, IDescription {
    boolean meet(OfflinePlayer player);
}
