package me.gsqfi.fitpokecr.fitpokecr;

import com.pixelmonmod.pixelmon.api.events.*;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.events.player.PlayerTaskCompleteEvent;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitpokecr.fitpokecr.conditions.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.playerData.load(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        Main.playerData.release(event.getPlayer().getName());
    }

    @EventHandler
    public void onTaskComplete(PlayerTaskCompleteEvent e) {
        Main.playerData.removePlayerTask(e.getPlayerName(), e.getTaskUid());
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
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof BeatPokeCondition) {
                        BeatPokeCondition cc = (BeatPokeCondition) condition;
                        if (e.wpp.allPokemon[0].getSpecies() != cc.getSpecies()){
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                playerName, uuid, BeatPokeCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(cc.getSpecies().name(), yaml.getInt(cc.getSpecies().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                playerName, uuid, BeatPokeCondition.class,
                                yaml.saveToString());
                    }
                }
            }
            return;
        }
        if (event.getForgeEvent() instanceof CaptureEvent.SuccessfulCapture) {
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
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof CapturePokeCondition) {
                        CapturePokeCondition cc = (CapturePokeCondition) condition;
                        if (e.getPokemon().getSpecies() != cc.getSpecies()){
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, CapturePokeCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(cc.getSpecies().name(), yaml.getInt(cc.getSpecies().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, CapturePokeCondition.class,
                                yaml.saveToString());
                    }
                }
            }
        }
        if (event.getForgeEvent() instanceof BeatTrainerEvent) {
            BeatTrainerEvent e = (BeatTrainerEvent) event.getForgeEvent();
            String name = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(name).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof BeatNPCAmountCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, CapturePokeAmountCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, CapturePokeAmountCondition.class,
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof BeatNPCCondition){
                        BeatNPCCondition cc = (BeatNPCCondition) condition;
                        if (e.trainer.func_110124_au() != cc.getUuid()) {
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, BeatNPCCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(cc.getUuid().toString(), yaml.getInt(cc.getUuid().toString()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, BeatNPCCondition.class,
                                yaml.saveToString());
                    }
                }
            }
        }
        if (event.getForgeEvent() instanceof ApricornEvent.PickApricorn){
            ApricornEvent.PickApricorn e = (ApricornEvent.PickApricorn) event.getForgeEvent();
            String name = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(name).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof PickApricornAmountCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, PickApricornAmountCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, PickApricornAmountCondition.class,
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof PickApricornCondition){
                        PickApricornCondition cc = (PickApricornCondition) condition;
                        if (e.apricorn != cc.getType()) {
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, PickApricornCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(cc.getType().name(), yaml.getInt(cc.getType().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, PickApricornCondition.class,
                                yaml.saveToString());
                    }
                }
            }
        }
        if (event.getForgeEvent() instanceof EvolveEvent.PostEvolve){
            EvolveEvent.PostEvolve e = (EvolveEvent.PostEvolve) event.getForgeEvent();
            String name = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(name).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof EvolvePokeCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, EvolvePokeCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, EvolvePokeCondition.class,
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                    }
                }
            }
        }
        if (event.getForgeEvent() instanceof PlayerActivateShrineEvent){
            PlayerActivateShrineEvent e = (PlayerActivateShrineEvent) event.getForgeEvent();
            String name = e.player.func_70005_c_();
            for (UUID uuid : FITaskApi.playerData.getAllAcceptedTasks(name).keySet()) {
                for (ICondition<?> condition : FITaskApi.getTask(uuid).getConditions()) {
                    if (condition instanceof ActivateShrineAmountCondition) {
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, ActivateShrineAmountCondition.class
                        );
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, ActivateShrineAmountCondition.class,
                                data == null ? "1" : String.valueOf(Integer.parseInt(data) + 1));
                        continue;
                    }
                    if (condition instanceof ActivateShrineCondition){
                        ActivateShrineCondition cc = (ActivateShrineCondition) condition;
                        if (e.shrineType != cc.getType()) {
                            continue;
                        }
                        String data = Main.playerData.getPlayerTaskCondition(
                                name, uuid, ActivateShrineCondition.class
                        );
                        YamlConfiguration yaml = new YamlConfiguration();
                        if (data != null) {
                            yaml.loadFromString(data);
                        }
                        yaml.set(cc.getType().name(), yaml.getInt(cc.getType().name()) + 1);
                        Main.playerData.setPlayerTaskCondition(
                                name, uuid, ActivateShrineCondition.class,
                                yaml.saveToString());
                    }
                }
            }
        }
    }
}
