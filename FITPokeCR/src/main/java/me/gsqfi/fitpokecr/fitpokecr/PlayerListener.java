package me.gsqfi.fitpokecr.fitpokecr;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitaskgui.fitaskgui.api.events.TaskCompleteEvent;
import me.gsqfi.fitpokecr.fitpokecr.conditions.BeatPokeAmountCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.BeatPokeCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.CapturePokeAmountCondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.CapturePokeCondition;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Main.playerData.load(name);
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        Main.playerData.release(event.getPlayer().getName());
    }

    @EventHandler
    public void onTaskComplete(TaskCompleteEvent e) {
        Main.playerData.removePlayerTask(e.getPlayer().getName(), e.getTask().getUuid());
    }

    @SneakyThrows
    @EventHandler
    public void onForge(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BeatWildPixelmonEvent) {
            BeatWildPixelmonEvent e = (BeatWildPixelmonEvent) event.getForgeEvent();
            String playerName = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(playerName).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof BeatPokeAmountCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                playerName, uuid, BeatPokeAmountCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                playerName, uuid, BeatPokeAmountCondition.class,
                                data == null ? "0" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof BeatPokeCondition) {
                        BeatPokeCondition bpc = (BeatPokeCondition) condition;
                        if (e.wpp.allPokemon[0].getSpecies() != bpc.getSpecies()){
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                playerName, uuid, BeatPokeCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(bpc.getSpecies().name(), yaml.getInt(bpc.getSpecies().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                playerName, uuid, BeatPokeCondition.class,
                                yaml.saveToString());
                    }
                }
            }
            return;
        }
        if (event.getForgeEvent() instanceof CaptureEvent) {
            CaptureEvent e = (CaptureEvent) event.getForgeEvent();
            String name = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(name).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof CapturePokeAmountCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, CapturePokeAmountCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, CapturePokeAmountCondition.class,
                                data == null ? "0" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof CapturePokeCondition) {
                        CapturePokeCondition bpc = (CapturePokeCondition) condition;
                        if (e.getPokemon().getSpecies() != bpc.getSpecies()){
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, CapturePokeCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(bpc.getSpecies().name(), yaml.getInt(bpc.getSpecies().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, CapturePokeCondition.class,
                                yaml.saveToString());
                    }
                }
            }
        }
    }
}
