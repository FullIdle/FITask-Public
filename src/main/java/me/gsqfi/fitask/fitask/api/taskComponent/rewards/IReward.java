package me.gsqfi.fitask.fitask.api.taskComponent.rewards;

import me.gsqfi.fitask.fitask.api.taskComponent.IAdapter;
import me.gsqfi.fitask.fitask.api.taskComponent.IDescription;
import org.bukkit.OfflinePlayer;

public interface IReward<T> extends IAdapter<T>, IDescription {
    void giveReward(OfflinePlayer player);
}
