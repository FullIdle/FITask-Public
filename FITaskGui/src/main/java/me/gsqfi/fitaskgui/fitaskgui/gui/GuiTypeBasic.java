package me.gsqfi.fitaskgui.fitaskgui.gui;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.gsqfi.fitask.fitask.api.taskcomponent.BasicTask;
import me.gsqfi.fitask.fitask.helpers.TaskDataHelper;
import me.gsqfi.fitaskgui.fitaskgui.Main;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class GuiTypeBasic extends ListenerInvHolder {
    private final GuiType guiType;
    private TaskLevel nowLevel;
    private final Inventory inventory;
    private int nowPage;
    private final OfflinePlayer player;
    private final Map<Integer, BasicTask> slotWithTask = new HashMap<>();

    protected GuiTypeBasic(GuiType guiType, OfflinePlayer player, String title) {
        this.player = player;
        this.guiType = guiType;
        this.inventory = Bukkit.createInventory(this, 54, title);
        {
            //边框
            ItemStack item = getGuiTypeBasicFrameItem();
            for (int i = 0; i < 54; i++) {
                if (!this.guiType.getSlot().contains(i)) {
                    this.inventory.setItem(i, item);
                }
            }
        }
        {
            //任务类型
            for (TaskLevel value : TaskLevel.values()) {
                this.inventory.setItem(value.getInventorySlot(), value.getGuiItem());
            }
        }
        {
            //上下页
            ItemStack itemStack = new ItemStack(Material.ARROW);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§5§l上一页");
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(1, itemStack.clone());
            itemMeta.setDisplayName("§5§l下一页");
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(46, itemStack);
        }
        setNowLevel(TaskLevel.TOTAL);

        this.onDrag(e->{
            e.setCancelled(true);
        });
        this.onClick(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory) return;
            int slot = e.getSlot();
            if (slot == 1) {
                //上一页
                this.changePage(this.nowPage - 1);
            }
            if (slot == 46) {
                //下一页
                this.changePage(this.nowPage + 1);
            }
            //click task
            if (this.slotWithTask.containsKey(slot)){
                clickTaskHandler(e, this.slotWithTask.get(slot));
                return;
            }
            //click task type
            TaskLevel level = TaskLevel.getTaskLevelWithInventorySlot(slot);
            if (level != null && this.nowLevel != level){
                setNowLevel(level);
                Player p = (Player) e.getWhoClicked();
                Location location = p.getLocation();
                p.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        });
    }

    public void open(){
        Player p = this.player.getPlayer();
        if (p == null) {
            throw new RuntimeException("player is null");
        }
        p.openInventory(this.inventory);
    }

    public abstract void clickTaskHandler(InventoryClickEvent e, BasicTask task);

    //翻页
    public void changePage(int page) {
        if (page < 0) return;
        List<BasicTask> taskList = new ArrayList<>(this.guiType.getTaskList());
        if (this.nowLevel != TaskLevel.TOTAL) {
            taskList.removeIf(task -> !task.getTaskType().equals(this.nowLevel.fiTaskType));
        }
        int num = this.guiType.getSlot().size();
        int start = page * num;
        if (page >= taskList.size()) {
            return;
        }
        this.slotWithTask.clear();
        for (int i = 0; i < num; i++) {
            Integer slot = this.guiType.getSlot().get(i);
            if (start + i >= taskList.size()) {
                this.inventory.setItem(slot, null);
                continue;
            }
            BasicTask task = taskList.get(start + i);
            this.inventory.setItem(slot, getTaskInvItem(player, task));
            this.slotWithTask.put(slot, task);
        }
        this.nowPage = page;
    }

    public void setNowLevel(TaskLevel level) {
        for (TaskLevel value : TaskLevel.values()) {
            ItemStack item = this.inventory.getItem(value.getInventorySlot());
            for (Enchantment ect : item.getEnchantments().keySet()) {
                item.removeEnchantment(ect);
            }
        }
        this.inventory.getItem(level.getInventorySlot()).addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        this.nowLevel = level;
        for (Integer i : this.guiType.getSlot()) {
            this.inventory.setItem(i,null);
        }
        changePage(0);
    }

    //static
    private static ItemStack taskInvItemTemplate;

    public static ItemStack getTaskInvItem(OfflinePlayer player, BasicTask task) {
        ItemStack itemStack = taskInvItemTemplate.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        StringBuilder name = new StringBuilder();
        for (String s : taskInvPapi(player, task, itemMeta.getDisplayName())) {
            name.append(s);
        }
        itemMeta.setDisplayName(name.toString());
        ArrayList<String> list = new ArrayList<>();
        for (String s : itemMeta.getLore()) {
            list.addAll(taskInvPapi(player, task, s));
        }
        itemMeta.setLore(list);
        return itemStack;
    }

    private static List<String> taskInvPapi(OfflinePlayer player, BasicTask task, String str) {
        ArrayList<String> list = new ArrayList<>();
        if (str.contains("%fitask_{uuid}_condition_{slot}_description%")) {
            for (int i = 0; i < task.getConditions().length; i++) {
                list.add(PlaceholderAPI.setPlaceholders(player, str
                        .replace("{uuid}", task.getUuid().toString())
                        .replace("{slot}", String.valueOf(i))));
            }
        } else if (str.contains("%fitask_{uuid}_reward_{slot}_description%")) {
            for (int i = 0; i < task.getRewards().length; i++) {
                list.add(PlaceholderAPI.setPlaceholders(player, str
                        .replace("{uuid}", task.getUuid().toString())
                        .replace("{slot}", String.valueOf(i))));
            }
        } else {
            list.add(PlaceholderAPI.setPlaceholders(player, str
                    .replace("{uuid}", task.getUuid().toString())));
        }
        return list;
    }

    @Getter
    private static final ItemStack guiTypeBasicFrameItem;

    @Getter
    public enum GuiType {
        DAILY, WEEKLY, MONTHLY, DISPOSABLE;

        private String title;
        private List<BasicTask> taskList;
        private Mode mode;
        private List<Integer> slot;

        public enum Mode {
            ALL, RANDOM;
        }
    }

    @Getter
    public enum TaskLevel {
        EASY(10), MEDIUM(19), HARD(28), TOTAL(37);

        @Setter
        private ItemStack guiItem;
        @Setter
        private String fiTaskType;
        private final int inventorySlot;

        TaskLevel(int inventorySlot) {
            this.inventorySlot = inventorySlot;
        }

        public static TaskLevel getTaskLevelWithInventorySlot(int inventorySlot) {
            for (TaskLevel value : TaskLevel.values()) {
                if (value.getInventorySlot() == inventorySlot) {
                    return value;
                }
            }
            return null;
        }

        public static TaskLevel getTaskLevelWithName(String fiTaskType) {
            for (TaskLevel value : TaskLevel.values()) {
                if (value.isThisTaskType(fiTaskType)) {
                    return value;
                }
            }
            return null;
        }

        public boolean isThisTaskType(String fiTaskType) {
            return this.fiTaskType.equals(fiTaskType);
        }

        public boolean isThisTaskType(BasicTask task) {
            return task.getTaskType().equals(fiTaskType);
        }
    }

    //init
    public static void init(Main plugin) {
        FileConfiguration config = plugin.getConfig();
        for (TaskLevel value : TaskLevel.values()) {
            ConfigurationSection taskLevel = config.getConfigurationSection("taskLevel." + value.name().toUpperCase());
            value.fiTaskType = taskLevel.getString("fiTaskType");
            ItemStack itemStack = new ItemStack(Material.getMaterial(taskLevel.getString("material")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(taskLevel.getString("name"));
            itemMeta.setLore(taskLevel.getStringList("lore"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(itemMeta);
            value.guiItem = itemStack;
        }
        TaskLevel.TOTAL.setFiTaskType(null);

        //guiType
        for (GuiType value : GuiType.values()) {
            ConfigurationSection section = config.getConfigurationSection("guiType." + value.name().toUpperCase());
            value.title = section.getString("title");
            value.taskList = section.getStringList("taskUid").stream()
                    .map(s -> TaskDataHelper.getTask(UUID.fromString(s))).collect(Collectors.toList());
            value.mode = GuiType.Mode.valueOf(section.getString("mode"));
            value.slot = section.getIntegerList("slot");
        }
        {
            //taskInvItemTemplate
            taskInvItemTemplate = new ItemStack(Material.getMaterial(config.getString("taskInvItemTemplate.material")));
            ItemMeta itemMeta = taskInvItemTemplate.getItemMeta();
            itemMeta.setDisplayName(config.getString("taskInvItemTemplate.name"));
            itemMeta.setLore(config.getStringList("taskInvItemTemplate.lore"));
            taskInvItemTemplate.setItemMeta(itemMeta);
        }
    }


    static {
        {
            //guiTypeBasicFrameItem
            Material material = Arrays.stream(Material.values()).filter(m -> m.name().endsWith("GLASS_PANE")).findFirst().get();
            guiTypeBasicFrameItem = new ItemStack(material);
            ItemMeta itemMeta = guiTypeBasicFrameItem.getItemMeta();
            itemMeta.setDisplayName(" ");
            guiTypeBasicFrameItem.setItemMeta(itemMeta);
        }
    }
}
