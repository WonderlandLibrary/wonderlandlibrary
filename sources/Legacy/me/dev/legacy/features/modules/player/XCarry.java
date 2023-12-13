
package me.dev.legacy.features.modules.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import me.dev.legacy.event.events.ClientEvent;
import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.gui.OyVeyGui;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Bind;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.InventoryUtil;
import me.dev.legacy.util.ReflectionUtil;
import me.dev.legacy.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class XCarry
extends Module {
    private final Setting<Boolean> simpleMode = this.register(new Setting<Boolean>("Simple", false));
    private final Setting<Bind> autoStore = this.register(new Setting<Bind>("AutoDuel", new Bind(-1)));
    private final Setting<Integer> obbySlot = this.register(new Setting<Object>("ObbySlot", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(9), v -> this.autoStore.getValue().getKey() != -1));
    private final Setting<Integer> slot1 = this.register(new Setting<Object>("Slot1", Integer.valueOf(22), Integer.valueOf(9), Integer.valueOf(44), v -> this.autoStore.getValue().getKey() != -1));
    private final Setting<Integer> slot2 = this.register(new Setting<Object>("Slot2", Integer.valueOf(23), Integer.valueOf(9), Integer.valueOf(44), v -> this.autoStore.getValue().getKey() != -1));
    private final Setting<Integer> slot3 = this.register(new Setting<Object>("Slot3", Integer.valueOf(24), Integer.valueOf(9), Integer.valueOf(44), v -> this.autoStore.getValue().getKey() != -1));
    private final Setting<Integer> tasks = this.register(new Setting<Object>("Actions", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12), v -> this.autoStore.getValue().getKey() != -1));
    private final Setting<Boolean> store = this.register(new Setting<Boolean>("Store", false));
    private final Setting<Boolean> shiftClicker = this.register(new Setting<Boolean>("ShiftClick", false));
    private final Setting<Boolean> withShift = this.register(new Setting<Object>("WithShift", Boolean.valueOf(true), v -> this.shiftClicker.getValue()));
    private final Setting<Bind> keyBind = this.register(new Setting<Object>("ShiftBind", new Bind(-1), v -> this.shiftClicker.getValue()));
    private static XCarry INSTANCE = new XCarry();
    private GuiInventory openedGui = null;
    private final AtomicBoolean guiNeedsClose = new AtomicBoolean(false);
    private boolean guiCloseGuard = false;
    private boolean autoDuelOn = false;
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private boolean obbySlotDone = false;
    private boolean slot1done = false;
    private boolean slot2done = false;
    private boolean slot3done = false;
    private List<Integer> doneSlots = new ArrayList<Integer>();

    public XCarry() {
        super("XCarry", "Uses the crafting inventory for storage", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static XCarry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XCarry();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.shiftClicker.getValue().booleanValue() && XCarry.mc.currentScreen instanceof GuiInventory) {
            Slot slot;
            boolean ourBind;
            boolean bl = ourBind = this.keyBind.getValue().getKey() != -1 && Keyboard.isKeyDown((int)this.keyBind.getValue().getKey()) && !Keyboard.isKeyDown((int)42);
            if ((Keyboard.isKeyDown((int)42) && this.withShift.getValue().booleanValue() || ourBind) && Mouse.isButtonDown((int)0) && (slot = ((GuiInventory)XCarry.mc.currentScreen).getSlotUnderMouse()) != null && InventoryUtil.getEmptyXCarry() != -1) {
                int slotNumber = slot.slotNumber;
                if (slotNumber > 4 && ourBind) {
                    this.taskList.add(new InventoryUtil.Task(slotNumber));
                    this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                } else if (slotNumber > 4 && this.withShift.getValue().booleanValue()) {
                    boolean isHotBarFull = true;
                    boolean isInvFull = true;
                    for (int i : InventoryUtil.findEmptySlots(false)) {
                        if (i > 4 && i < 36) {
                            isInvFull = false;
                            continue;
                        }
                        if (i <= 35 || i >= 45) continue;
                        isHotBarFull = false;
                    }
                    if (slotNumber > 35 && slotNumber < 45) {
                        if (isInvFull) {
                            this.taskList.add(new InventoryUtil.Task(slotNumber));
                            this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                        }
                    } else if (isHotBarFull) {
                        this.taskList.add(new InventoryUtil.Task(slotNumber));
                        this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                    }
                }
            }
        }
        if (this.autoDuelOn) {
            this.doneSlots = new ArrayList<Integer>();
            if (InventoryUtil.getEmptyXCarry() == -1 || this.obbySlotDone && this.slot1done && this.slot2done && this.slot3done) {
                this.autoDuelOn = false;
            }
            if (this.autoDuelOn) {
                if (!this.obbySlotDone && !XCarry.mc.player.inventory.getStackInSlot((int)(this.obbySlot.getValue().intValue() - 1)).isEmpty) {
                    this.addTasks(36 + this.obbySlot.getValue() - 1);
                }
                this.obbySlotDone = true;
                if (!this.slot1done && !((Slot)XCarry.mc.player.inventoryContainer.inventorySlots.get((int)this.slot1.getValue().intValue())).getStack().isEmpty) {
                    this.addTasks(this.slot1.getValue());
                }
                this.slot1done = true;
                if (!this.slot2done && !((Slot)XCarry.mc.player.inventoryContainer.inventorySlots.get((int)this.slot2.getValue().intValue())).getStack().isEmpty) {
                    this.addTasks(this.slot2.getValue());
                }
                this.slot2done = true;
                if (!this.slot3done && !((Slot)XCarry.mc.player.inventoryContainer.inventorySlots.get((int)this.slot3.getValue().intValue())).getStack().isEmpty) {
                    this.addTasks(this.slot3.getValue());
                }
                this.slot3done = true;
            }
        } else {
            this.obbySlotDone = false;
            this.slot1done = false;
            this.slot2done = false;
            this.slot3done = false;
        }
        if (!this.taskList.isEmpty()) {
            for (int i = 0; i < this.tasks.getValue(); ++i) {
                InventoryUtil.Task task = this.taskList.poll();
                if (task == null) continue;
                task.run();
            }
        }
    }

    private void addTasks(int slot) {
        if (InventoryUtil.getEmptyXCarry() != -1) {
            int xcarrySlot = InventoryUtil.getEmptyXCarry();
            if (!(!this.doneSlots.contains(xcarrySlot) && InventoryUtil.isSlotEmpty(xcarrySlot) || !this.doneSlots.contains(++xcarrySlot) && InventoryUtil.isSlotEmpty(xcarrySlot) || !this.doneSlots.contains(++xcarrySlot) && InventoryUtil.isSlotEmpty(xcarrySlot) || !this.doneSlots.contains(++xcarrySlot) && InventoryUtil.isSlotEmpty(xcarrySlot))) {
                return;
            }
            if (xcarrySlot > 4) {
                return;
            }
            this.doneSlots.add(xcarrySlot);
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task(xcarrySlot));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    @Override
    public void onDisable() {
        if (!XCarry.fullNullCheck()) {
            if (!this.simpleMode.getValue().booleanValue()) {
                this.closeGui();
                this.close();
            } else {
                XCarry.mc.player.connection.sendPacket((Packet)new CPacketCloseWindow(XCarry.mc.player.inventoryContainer.windowId));
            }
        }
    }

    @Override
    public void onLogout() {
        this.onDisable();
    }

    @SubscribeEvent
    public void onCloseGuiScreen(PacketEvent.Send event) {
        if (this.simpleMode.getValue().booleanValue() && event.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow packet = (CPacketCloseWindow)event.getPacket();
            if (packet.windowId == XCarry.mc.player.inventoryContainer.windowId) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onGuiOpen(GuiOpenEvent event) {
        if (!this.simpleMode.getValue().booleanValue()) {
            if (this.guiCloseGuard) {
                event.setCanceled(true);
            } else if (event.getGui() instanceof GuiInventory) {
                this.openedGui = this.createGuiWrapper((GuiInventory)event.getGui());
                event.setGui((GuiScreen)this.openedGui);
                this.guiNeedsClose.set(false);
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
            Setting setting = event.getSetting();
            String settingname = event.getSetting().getName();
            if (setting.equals(this.simpleMode) && setting.getPlannedValue() != setting.getValue()) {
                this.disable();
            } else if (settingname.equalsIgnoreCase("Store")) {
                event.setCanceled(true);
                this.autoDuelOn = !this.autoDuelOn;
                Command.sendMessage("<XCarry> \u00a7aAutostoring...");
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(XCarry.mc.currentScreen instanceof OyVeyGui) && this.autoStore.getValue().getKey() == Keyboard.getEventKey()) {
            this.autoDuelOn = !this.autoDuelOn;
            Command.sendMessage("<XCarry> \u00a7aAutostoring...");
        }
    }

    private void close() {
        this.openedGui = null;
        this.guiNeedsClose.set(false);
        this.guiCloseGuard = false;
    }

    private void closeGui() {
        if (this.guiNeedsClose.compareAndSet(true, false) && !XCarry.fullNullCheck()) {
            this.guiCloseGuard = true;
            XCarry.mc.player.closeScreen();
            if (this.openedGui != null) {
                this.openedGui.onGuiClosed();
                this.openedGui = null;
            }
            this.guiCloseGuard = false;
        }
    }

    private GuiInventory createGuiWrapper(GuiInventory gui) {
        try {
            GuiInventoryWrapper wrapper = new GuiInventoryWrapper();
            ReflectionUtil.copyOf(gui, wrapper);
            return wrapper;
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class GuiInventoryWrapper
    extends GuiInventory {
        GuiInventoryWrapper() {
            super((EntityPlayer)Util.mc.player);
        }

        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            if (XCarry.this.isEnabled() && (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))) {
                XCarry.this.guiNeedsClose.set(true);
                this.mc.displayGuiScreen(null);
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        }

        public void onGuiClosed() {
            if (XCarry.this.guiCloseGuard || !XCarry.this.isEnabled()) {
                super.onGuiClosed();
            }
        }
    }
}

