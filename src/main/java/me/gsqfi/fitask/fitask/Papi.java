package me.gsqfi.fitask.fitask;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;

public class Papi extends PlaceholderExpansion {
    private final Main plugin;

    public Papi(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return Arrays.toString(this.plugin.getDescription().getAuthors().toArray());
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    /*
     * %fitask_{uuid}_name% 指定任务名
     * %fitask_{uuid}_description% 指定任务描述
     * %fitask_{uuid}_condition_{slot}_description% 指定任务指定条件描述
     * %fitask_{uuid}_condition_{slot}_meet% 指定任务指定条件是否达成
     * %fitask_{uuid}_reward_{slot}_description% 指定任务指定奖励描述
     *
     * %fitask_{uuid}_progress% 指定任务进展(满足的条件量/总条件量)
     * */
    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] split = params.split("_");
        UUID uuid;
        try {
            uuid = UUID.fromString(split[0]);
        } catch (Exception e) {
            return "Invalid UUID";
        }
        if (!TaskDataHelper.cacheTask.containsKey(uuid)) {
            return "Task not found";
        }
        BasicTask task = TaskDataHelper.getTask(uuid);
        if (split[1].equalsIgnoreCase("name")) {
            return task.getTaskName();
        }
        if (split[1].equalsIgnoreCase("description")) {
            return task.getDescription();
        }
        if (split[1].equalsIgnoreCase("conditions")) {
            if (split.length < 4) return "Wrong format";
            int slot;
            ICondition<?> condition;
            try {
                slot = Integer.parseInt(split[2]);
                condition = task.getConditions()[slot];
            } catch (NumberFormatException e) {
                return "Wrong format -> '"+split[2]+"'";
            }
            String arg = split[3];
            if (arg.equalsIgnoreCase("description")) {
                return condition.getDescription();
            }
            if (arg.equalsIgnoreCase("meet")) {
                return String.valueOf(condition.meet(player));
            }
            return "Wrong format -> '"+split[3]+"'";
        }
        if (split[1].equalsIgnoreCase("rewards")) {
            if (split.length < 4) return "Wrong format";
            int slot;
            IReward<?> reward;
            try {
                slot = Integer.parseInt(split[2]);
                reward = task.getRewards()[slot];
            } catch (NumberFormatException e) {
                return "Wrong format -> '"+split[2]+"'";
            }
            String arg = split[3];
            if (arg.equalsIgnoreCase("description")) {
                return reward.getDescription();
            }
            return "Wrong format -> '"+split[3]+"'";
        }
        if (split[1].equalsIgnoreCase("progress")) {
            ICondition<?>[] conditions = task.getConditions();
            int i = 0;
            for (ICondition<?> condition : conditions) {
                if (condition.meet(player)) {
                    i++;
                }
            }
            return String.valueOf(((double) (i / conditions.length)));
        }
        return "Wrong format";
    }
}
