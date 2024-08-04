package me.gsqfi.fitask.fitask.gui;

import com.google.common.collect.Lists;
import me.gsqfi.fitask.fitask.api.FITaskApi;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;
import me.gsqfi.fitask.fitask.api.taskcomponent.rewards.IReward;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;

public class ChatComponentsGui {
    private static final String[] clearChatMsg;

    public static void clearChat(CommandSender sender) {
        sender.sendMessage(clearChatMsg);
    }

    public static BaseComponent[] show(int page) {
        Collection<BasicTask> allTask = FITaskApi.getAllTask();
        if (allTask.isEmpty()) {
            return new ComponentBuilder("§c§lNo task found").create();
        }
        if (page < 0) page = 0;
        int total = (int) Math.ceil((double) allTask.size() / 7);
        if (page * 7 > allTask.size()) page = total - 1;
        int start = page * 7;

        ArrayList<BasicTask> list = Lists.newArrayList(allTask);
        ComponentBuilder builder = new ComponentBuilder("§7§l[All Tasks]");
        for (int i = start; i < Math.min(start + 7, list.size()); i++) {
            if (list.size() > i) {
                BasicTask task = list.get(i);
                builder.append(new ComponentBuilder("\n").create())
                        .append(new ComponentBuilder("§3§l" + task.getTaskName())
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§l" + task.getDescription()).create()))
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
                .append(new ComponentBuilder("  " + (page + 1) + "/" + total + "  ").create(), ComponentBuilder.FormatRetention.NONE)
                .append(new ComponentBuilder("§3§l[下一页]")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fitask show " + (page + 1)))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l下一页").create()))
                        .create());
        return builder.create();
    }

    public static BaseComponent[] info(BasicTask task) {
        ComponentBuilder builder = new ComponentBuilder("§7§l[任务信息]")
                .append("\n§7§l任务名称: ")
                .append(new ComponentBuilder("§3§l" + task.getTaskName())
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l点击更改任务名称").create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fitask edit rename " + task.getUuid()))
                        .create())
                .append("\n§7§l任务描述: ", ComponentBuilder.FormatRetention.NONE)
                .append(new ComponentBuilder("§3§l" + task.getDescription())
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§l点击更改任务描述").create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fitask edit  " + task.getUuid()))
                        .create())
                .append(new ComponentBuilder("\n§7§l任务条件:").create(), ComponentBuilder.FormatRetention.NONE);
        ICondition<?>[] conditions = task.getConditions();
        for (int i = 0; i < conditions.length; i++) {
            builder.append(new ComponentBuilder(
                    String.format("\n§7§l  - §3§l%s ",conditions[i].getDescription()))
                                    .create(), ComponentBuilder.FormatRetention.NONE)
                    .append(new ComponentBuilder("§c§l[-]")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§l!点击删除该条件!").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/fitask edit removecondition %s %d",task.getUuid(),i)))
                    .create());
        }
        builder.append("\n§7§l  - ", ComponentBuilder.FormatRetention.NONE).append(new ComponentBuilder("§a[+]")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§l点击添加条件").create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fitask edit addcondition " + task.getUuid()))
                .create())
                .append("\n§7§l任务奖励:", ComponentBuilder.FormatRetention.NONE);
        IReward<?>[] rewards = task.getRewards();
        for (int i = 0; i < rewards.length; i++) {
            builder.append(new ComponentBuilder(
                            String.format("\n§7§l  - §3§l%s ",rewards[i].getDescription()))
                            .create(), ComponentBuilder.FormatRetention.NONE)
                    .append(new ComponentBuilder("§c§l[-]")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§l!点击删除该奖励!").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/fitask edit removereward %s %d",task.getUuid(),i)))
                    .create());
        }

        builder.append("\n§7§l  - ", ComponentBuilder.FormatRetention.NONE).append(new ComponentBuilder("§a[+]")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§l点击添加奖励").create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fitask edit addreward " + task.getUuid()))
                .create())
                .append("\n§7§l任务UID: ", ComponentBuilder.FormatRetention.NONE)
                .append(new ComponentBuilder("§3§l" + task.getUuid().toString())
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
