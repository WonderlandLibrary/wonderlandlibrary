package me.dev.legacy.features.modules.combat;

import me.dev.legacy.Legacy;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.InventoryUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AutoArmor
        extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 500));
    private final Setting<Boolean> curse = this.register(new Setting<Boolean>("Vanishing", false));
    private final Setting<Boolean> mendingTakeOff = this.register(new Setting<Boolean>("AutoMend", false));
    private final Setting<Integer> closestEnemy = this.register(new Setting<Object>("Enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> repair = this.register(new Setting<Object>("Repair%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> actions = this.register(new Setting<Integer>("Packets", 3, 1, 12));
    private final Timer timer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final List<Integer> doneSlots = new ArrayList<Integer>();
    boolean flag;

    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onLogin() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }

    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }

    @Override
    public void onTick() {
        if (AutoArmor.fullNullCheck() || AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int slot;
            ItemStack feet;
            int slot2;
            ItemStack legging;
            int slot3;
            ItemStack chest;
            int slot4;
            if (this.mendingTakeOff.getValue().booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoArmor.mc.world.playerEntities.stream().noneMatch(e -> e != AutoArmor.mc.player && !Legacy.friendManager.isFriend(e.getName()) && AutoArmor.mc.player.getDistance(e) <= (float) this.closestEnemy.getValue().intValue()) && !this.flag) {
                int goods;
                int dam;
                int takeOff = 0;
                for (Map.Entry<Integer, ItemStack> armorSlot : this.getArmor().entrySet()) {
                    ItemStack stack = armorSlot.getValue();
                    float percent = (float) this.repair.getValue().intValue() / 100.0f;
                    dam = Math.round((float) stack.getMaxDamage() * percent);
                    if (dam >= (goods = stack.getMaxDamage() - stack.getItemDamage())) continue;
                    ++takeOff;
                }
                if (takeOff == 4) {
                    this.flag = true;
                }
                if (!this.flag) {
                    ItemStack itemStack1 = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
                    if (!itemStack1.isEmpty) {
                        int goods2;
                        float percent = (float) this.repair.getValue().intValue() / 100.0f;
                        int dam2 = Math.round((float) itemStack1.getMaxDamage() * percent);
                        if (dam2 < (goods2 = itemStack1.getMaxDamage() - itemStack1.getItemDamage())) {
                            this.takeOffSlot(5);
                        }
                    }
                    ItemStack itemStack2 = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack();
                    if (!itemStack2.isEmpty) {
                        int goods3;
                        float percent = (float) this.repair.getValue().intValue() / 100.0f;
                        int dam3 = Math.round((float) itemStack2.getMaxDamage() * percent);
                        if (dam3 < (goods3 = itemStack2.getMaxDamage() - itemStack2.getItemDamage())) {
                            this.takeOffSlot(6);
                        }
                    }
                    ItemStack itemStack3 = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack();
                    if (!itemStack3.isEmpty) {
                        float percent = (float) this.repair.getValue().intValue() / 100.0f;
                        dam = Math.round((float) itemStack3.getMaxDamage() * percent);
                        if (dam < (goods = itemStack3.getMaxDamage() - itemStack3.getItemDamage())) {
                            this.takeOffSlot(7);
                        }
                    }
                    ItemStack itemStack4 = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack();
                    if (!itemStack4.isEmpty) {
                        int goods4;
                        float percent = (float) this.repair.getValue().intValue() / 100.0f;
                        int dam4 = Math.round((float) itemStack4.getMaxDamage() * percent);
                        if (dam4 < (goods4 = itemStack4.getMaxDamage() - itemStack4.getItemDamage())) {
                            this.takeOffSlot(8);
                        }
                    }
                }
                return;
            }
            this.flag = false;
            ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.AIR && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(5, slot4);
            }
            if ((chest = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack()).getItem() == Items.AIR && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(6, slot3);
            }
            if ((legging = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack()).getItem() == Items.AIR && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(7, slot2);
            }
            if ((feet = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack()).getItem() == Items.AIR && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(8, slot);
            }
        }
        if (this.timer.passedMs((int) ((float) this.delay.getValue().intValue() * Legacy.serverManager.getTpsFactor()))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (int i : InventoryUtil.findEmptySlots(true)) {
                if (this.doneSlots.contains(target)) continue;
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object) target);
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    private Map<Integer, ItemStack> getArmor() {
        return this.getInventorySlots(5, 8);
    }

    private Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, AutoArmor.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
}