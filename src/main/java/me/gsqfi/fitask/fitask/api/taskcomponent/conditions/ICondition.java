package me.gsqfi.fitask.fitask.api.taskcomponent.conditions;

import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.IDescription;
import org.bukkit.OfflinePlayer;

public interface ICondition<T> extends IAdapter<T>, IDescription {
    boolean meet(OfflinePlayer player);
}
