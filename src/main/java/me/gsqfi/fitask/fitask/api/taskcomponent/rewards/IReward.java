package me.gsqfi.fitask.fitask.api.taskcomponent.rewards;

import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.IDescription;
import me.gsqfi.fitask.fitask.api.taskcomponent.ITaskLocator;
import org.bukkit.OfflinePlayer;

public interface IReward<T> extends IAdapter<T>, IDescription, ITaskLocator {
    void giveReward(OfflinePlayer player);
}
