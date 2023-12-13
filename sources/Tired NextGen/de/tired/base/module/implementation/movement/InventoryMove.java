package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.RotationEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import lombok.Setter;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(name = "InventoryMove", category = ModuleCategory.MOVEMENT, clickG = "You can move in the inventory")
public class InventoryMove extends Module {

    private BooleanSetting booleanSetting = new BooleanSetting("CancelAchievement", this, true);

    private BooleanSetting cancelSprint = new BooleanSetting("cancelSprint", this, true);
    private BooleanSetting fakeRotation = new BooleanSetting("FakeRotation", this, true);

    public List<SlotData> clickedSlots = new ArrayList<>();

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (!this.isState()) return;
        if ((MC.currentScreen instanceof GuiContainer) || MC.currentScreen instanceof GuiGameOver && !MC.playerController.isInCreativeMode()) {
            handleKey(MC.gameSettings.keyBindLeft);
            handleKey(MC.gameSettings.keyBindRight);
            handleKey(MC.gameSettings.keyBindBack);
            handleKey(MC.gameSettings.keyBindJump);
            handleKey(MC.gameSettings.keyBindSneak);
            for (SlotData slotData : clickedSlots) {
                System.out.println(slotData.slot + " " + slotData.beforeMovePosition + " " + slotData.afterMovePosition);
            }
        }
    }

    @EventTarget
    public void onRotation(RotationEvent event) {
        if (!this.isState()) return;
        if (!fakeRotation.getValue()) return;

        if (!MC.inGameHasFocus) {
            Keyboard.enableRepeatEvents(true);
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                event.setYaw(MC.thePlayer.rotationYaw -= 10);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                event.setYaw(MC.thePlayer.rotationYaw += 10);
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!this.isState()) return;
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction c0BPacketEntityAction = new C0BPacketEntityAction();
            switch (c0BPacketEntityAction.getAction()) {
                case OPEN_INVENTORY: {
                    if (booleanSetting.getValue())
                        event.setCancelled(true);
                }
                case START_SPRINTING: {
                    if (cancelSprint.getValue())
                        event.setCancelled(true);
                    break;
                }

            }
        }
        if (event.getPacket() instanceof C16PacketClientStatus) {
            final C16PacketClientStatus c16PacketClientStatus = (C16PacketClientStatus) event.getPacket();
            if (c16PacketClientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                if (booleanSetting.getValue())
                    event.setCancelled(true);
            }
        }
    }

    public void handleKey(KeyBinding keyBinding) {
        keyBinding.pressed = isDown(keyBinding.getKeyCode());
    }

    public boolean isDown(int key) {
        if (key < 0) {
            while (Mouse.next()) {
                int i = Mouse.getEventButton();
                return i - 100 == key;
            }
        }
        return Keyboard.isKeyDown(key);
    }

    public static InventoryMove getInstance() {
        return ModuleManager.getInstance(InventoryMove.class);
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }

    public static class SlotData {
        public Slot slot;

        @Setter
        public int beforeMovePosition, afterMovePosition;

        public SlotData(Slot slot, int beforeMovePosition) {
            this.slot = slot;
            this.beforeMovePosition = beforeMovePosition;
            this.afterMovePosition = 0; //Cant define the after move position.. cause its not moved lol
        }

    }

}
