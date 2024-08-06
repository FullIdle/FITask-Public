package me.gsqfi.fitask.fitask;

import me.gsqfi.fitask.fitask.api.FITaskApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        FITaskApi.playerData.load(e.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        FITaskApi.playerData.release(e.getPlayer().getName());
    }
}
