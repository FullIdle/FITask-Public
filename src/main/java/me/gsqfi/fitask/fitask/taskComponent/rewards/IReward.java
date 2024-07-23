package me.gsqfi.fitask.fitask.taskComponent.rewards;

import me.gsqfi.fitask.fitask.IAdapter;
import org.bukkit.OfflinePlayer;

public interface IReward<T> extends IAdapter<T> {
    void giveReward(OfflinePlayer player);
}
