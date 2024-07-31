package me.gsqfi.fitask.fitask.gui;

import com.google.common.collect.Lists;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskComponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskComponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskComponent.rewards.IReward;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ChatComponentsGui {
    public static final String[] clearChatMsg;

    public static void clearChat(CommandSender sender){
        sender.sendMessage(clearChatMsg);
    }

    public static BaseComponent[] show(int page) {
        if (page < 0) page = 0;
        int total = (int) Math.ceil((double) FITaskApi.getAllTask().size() / 7);
        if (page * 7 > FITaskApi.getAllTask().size()) page = total-1;
        int start = page * 7;

        ArrayList<BasicTask> list = Lists.newArrayList(FITaskApi.getAllTask());
        ComponentBuilder builder = new ComponentBuilder("§7§lAll Tasks:");
        for (int i = start; i < Math.min(start + 7, list.size()); i++) {
            if (list.size() > i) {
                BasicTask task = list.get(i);
                builder.append(new ComponentBuilder("\n").create())
                        .append(new ComponentBuilder("§3§l" + task.getTaskName())
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l" + task.getDescription()).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fitask info " + task.getUuid()))
                                .create());
                continue;
            }
            break;
        }
        builder.append(new ComponentBuilder("\n§3§l[上一页]")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fitask show " + (page - 1)))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l上一页").create()))
                        .create())
                .append(new ComponentBuilder("  " + (page+1) + "/" + total + "  ").create(), ComponentBuilder.FormatRetention.NONE)
                .append(new ComponentBuilder("§3§l[下一页]")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fitask show " + (page + 1)))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l下一页").create()))
                        .create());
        return builder.create();
    }

    public static BaseComponent[] info(BasicTask task) {
        ComponentBuilder builder = new ComponentBuilder("§7§l任务名称: §3§l" + task.getTaskName())
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l点击更改").create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fitask edit rename " + task.getUuid()));
        builder.append(new ComponentBuilder("\n§7§l任务描述: §3§l" + task.getDescription()).create())
                .append(new ComponentBuilder("\n§7§l任务条件:").create());
        for (ICondition<?> condition : task.getConditions()) {
            builder.append(new ComponentBuilder("\n§7§l  - §3§l" + condition.getDescription()).create());
        }
        builder.append(new ComponentBuilder("\n§7§l任务奖励:").create());
        for (IReward<?> reward : task.getRewards()) {
            builder.append(new ComponentBuilder("\n§7§l  - §3§l" + reward.getDescription()).create());
        }
        builder.append("\n§7§l任务UID: ")
                .append(new ComponentBuilder("§3§l"+task.getUuid().toString())
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§l点击复制").create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, task.getUuid().toString()))
                        .create());
        return builder.create();
    }



    static {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("");
        }
        clearChatMsg = list.toArray(new String[0]);
    }
}
