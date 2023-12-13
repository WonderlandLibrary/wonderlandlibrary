package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.gui.PhobosGui;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.player.XCarry;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AutoArmor
        extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 500));
    private final Setting<Boolean> mendingTakeOff = this.register(new Setting<Boolean>("AutoMend", false));
    private final Setting<Integer> closestEnemy = this.register(new Setting<Object>("Enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> helmetThreshold = this.register(new Setting<Object>("Helmet%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> chestThreshold = this.register(new Setting<Object>("Chest%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> legThreshold = this.register(new Setting<Object>("Legs%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> bootsThreshold = this.register(new Setting<Object>("Boots%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Boolean> curse = this.register(new Setting<Boolean>("CurseOfBinding", false));
    private final Setting<Integer> actions = this.register(new Setting<Integer>("Actions", 3, 1, 12));
    private final Setting<Bind> elytraBind = this.register(new Setting<Bind>("Elytra", new Bind(-1)));
    private final Setting<Boolean> tps = this.register(new Setting<Boolean>("TpsSync", true));
    private final Setting<Boolean> updateController = this.register(new Setting<Boolean>("Update", true));
    private final Setting<Boolean> shiftClick = this.register(new Setting<Boolean>("ShiftClick", false));
    private final Timer timer = new Timer();
    private final Timer elytraTimer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final List<Integer> doneSlots = new ArrayList<Integer>();
    private boolean elytraOn = false;

    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(AutoArmor.mc.currentScreen instanceof PhobosGui) && this.elytraBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.elytraOn = !this.elytraOn;
        }
    }

    @Override
    public void onLogin() {
        this.timer.reset();
        this.elytraTimer.reset();
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.elytraOn = false;
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
            if (this.mendingTakeOff.getValue().booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown() && (this.isSafe() || EntityUtil.isSafe(AutoArmor.mc.player, 1, false, true))) {
                int bootDamage;
                int leggingDamage;
                int chestDamage;
                int helmDamage;
                ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
                if (!helm.isEmpty && (helmDamage = DamageUtil.getRoundedDamage(helm)) >= this.helmetThreshold.getValue()) {
                    this.takeOffSlot(5);
                }
                ItemStack chest2 = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack();
                if (!chest2.isEmpty && (chestDamage = DamageUtil.getRoundedDamage(chest2)) >= this.chestThreshold.getValue()) {
                    this.takeOffSlot(6);
                }
                ItemStack legging2 = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack();
                if (!legging2.isEmpty && (leggingDamage = DamageUtil.getRoundedDamage(legging2)) >= this.legThreshold.getValue()) {
                    this.takeOffSlot(7);
                }
                ItemStack feet2 = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack();
                if (!feet2.isEmpty && (bootDamage = DamageUtil.getRoundedDamage(feet2)) >= this.bootsThreshold.getValue()) {
                    this.takeOffSlot(8);
                }
                return;
            }
            ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.AIR && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(5, slot4);
            }
            if ((chest = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack()).getItem() == Items.AIR) {
                if (this.taskList.isEmpty()) {
                    if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                        int elytraSlot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                        if (elytraSlot != -1) {
                            if (elytraSlot < 5 && elytraSlot > 1 || !this.shiftClick.getValue().booleanValue()) {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot));
                                this.taskList.add(new InventoryUtil.Task(6));
                            } else {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
                            }
                            if (this.updateController.getValue().booleanValue()) {
                                this.taskList.add(new InventoryUtil.Task());
                            }
                            this.elytraTimer.reset();
                        }
                    } else if (!this.elytraOn && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                        this.getSlotOn(6, slot3);
                    }
                }
            } else if (this.elytraOn && chest.getItem() != Items.ELYTRA && this.elytraTimer.passedMs(500L)) {
                if (this.taskList.isEmpty()) {
                    slot3 = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                    if (slot3 != -1) {
                        this.taskList.add(new InventoryUtil.Task(slot3));
                        this.taskList.add(new InventoryUtil.Task(6));
                        this.taskList.add(new InventoryUtil.Task(slot3));
                        if (this.updateController.getValue().booleanValue()) {
                            this.taskList.add(new InventoryUtil.Task());
                        }
                    }
                    this.elytraTimer.reset();
                }
            } else if (!this.elytraOn && chest.getItem() == Items.ELYTRA && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
                slot3 = InventoryUtil.findItemInventorySlot(Items.DIAMOND_CHESTPLATE, false, XCarry.getInstance().isOn());
                if (slot3 == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.IRON_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.GOLDEN_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.CHAINMAIL_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1) {
                    slot3 = InventoryUtil.findItemInventorySlot(Items.LEATHER_CHESTPLATE, false, XCarry.getInstance().isOn());
                }
                if (slot3 != -1) {
                    this.taskList.add(new InventoryUtil.Task(slot3));
                    this.taskList.add(new InventoryUtil.Task(6));
                    this.taskList.add(new InventoryUtil.Task(slot3));
                    if (this.updateController.getValue().booleanValue()) {
                        this.taskList.add(new InventoryUtil.Task());
                    }
                }
                this.elytraTimer.reset();
            }
            if ((legging = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack()).getItem() == Items.AIR && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(7, slot2);
            }
            if ((feet = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack()).getItem() == Items.AIR && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(8, slot);
            }
        }
        if (this.timer.passedMs((int) ((float) this.delay.getValue().intValue() * (this.tps.getValue() != false ? Phobos.serverManager.getTpsFactor() : 1.0f)))) {
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

    @Override
    public String getDisplayInfo() {
        if (this.elytraOn) {
            return "Elytra";
        }
        return null;
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (int i : InventoryUtil.findEmptySlots(XCarry.getInstance().isOn())) {
                if (this.doneSlots.contains(target)) continue;
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                if (target < 5 && target > 0 || !this.shiftClick.getValue().booleanValue()) {
                    this.taskList.add(new InventoryUtil.Task(slot));
                    this.taskList.add(new InventoryUtil.Task(target));
                } else {
                    this.taskList.add(new InventoryUtil.Task(slot, true));
                }
                if (this.updateController.getValue().booleanValue()) {
                    this.taskList.add(new InventoryUtil.Task());
                }
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object) target);
            if (target < 5 && target > 0 || !this.shiftClick.getValue().booleanValue()) {
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task(slot));
            } else {
                this.taskList.add(new InventoryUtil.Task(target, true));
            }
            if (this.updateController.getValue().booleanValue()) {
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private boolean isSafe() {
        EntityPlayer closest = EntityUtil.getClosestEnemy(this.closestEnemy.getValue().intValue());
        if (closest == null) {
            return true;
        }
        return AutoArmor.mc.player.getDistanceSq(closest) >= MathUtil.square(this.closestEnemy.getValue().intValue());
    }
}

