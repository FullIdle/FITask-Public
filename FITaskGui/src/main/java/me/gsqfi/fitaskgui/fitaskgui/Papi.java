package me.gsqfi.fitaskgui.fitaskgui;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gsqfi.fitaskgui.fitaskgui.api.FITaskGuiApi;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Papi extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "fitaskgui";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GSQ_Lin";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    /*
    * fitaskgui_{uuid}_completed 指定任务是否完成
    * fitaskgui_{uuid}_lasttime 最近一次完成的时间 时间戳 long类型 一大段数字~!
    * fitaskgui_{uuid}_lasttime_yyyy:MM:dd:mm:ss
    * yyyy：表示四位数的年份。
    * MM：表示两位数的月份。
    * dd：表示两位数的日。
    * HH：表示两位数的小时（24小时制）。
    * mm：表示两位数的分钟。
    * ss：表示两位数的秒。
    * SSS：表示三位的毫秒。
    * n：表示纳秒。
    * N：表示时间戳。
    * */

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] split = params.split("_");
        if (split.length == 0) {
            return "Missing TaskUUID!";
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(split[0]);
        } catch (Exception e) {
            return "Wrong format -> "+split[0];
        }
        String arg = split[1].toLowerCase();
        if (arg.equals("completed")) {
            return String.valueOf(FITaskGuiApi.playerData.hasCompleteTask(player.getName(),uuid));
        }
        if (arg.equals("lasttime")){
            LocalDateTime time = FITaskGuiApi.playerData.getLastCompleteTaskTime(player.getName(), uuid);
            if (time == null) {
                return "NULL";
            }
            if (split.length > 2){
                return time.format(DateTimeFormatter.ofPattern(split[2]));
            }
            return String.valueOf(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        return "Wrong format!";
    }
}
