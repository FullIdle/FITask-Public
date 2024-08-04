package me.gsqfi.fitask.fitask.api.taskcomponent.rewards;

import me.gsqfi.fitask.fitask.api.taskcomponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskcomponent.IDescription;
import org.bukkit.OfflinePlayer;

public interface IReward<T> extends IAdapter<T>, IDescription {
    void giveReward(OfflinePlayer player);
}
