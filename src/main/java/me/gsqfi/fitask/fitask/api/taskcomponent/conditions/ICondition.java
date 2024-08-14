package me.gsqfi.fitask.fitask.api.taskcomponent.conditions;

import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.IDescription;
import me.gsqfi.fitask.fitask.api.taskcomponent.ITaskLocator;
import org.bukkit.OfflinePlayer;

public interface ICondition<T> extends IAdapter<T>, IDescription, ITaskLocator {
    boolean meet(OfflinePlayer player);
}
