package vestige.module.impl.movement;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.input.Mouse;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.*;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.combat.Killaura;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.misc.LogUtil;
import vestige.util.network.PacketUtil;

public class Noslow extends Module {

    private final ModeSetting swordMethod = new ModeSetting("Sword method", "Vanilla", "Vanilla", "NCP", "AAC4", "AAC5", "Spoof", "Spoof2", "Blink", "None");
    private final ModeSetting consumableMethod = new ModeSetting("Comsumable method", "Vanilla", "Vanilla", "Hypixel", "AAC4", "AAC5", "None");

    private final DoubleSetting forward = new DoubleSetting("Forward", 1, 0.2, 1, 0.05);
    private final DoubleSetting strafe = new DoubleSetting("Strafe", 1, 0.2, 1, 0.05);

    private final IntegerSetting blinkTicks = new IntegerSetting("Blink ticks", () -> swordMethod.is("Blink"), 5, 2, 10, 1);

    public final BooleanSetting allowSprinting = new BooleanSetting("Allow sprinting", true);

    private Killaura killauraModule;

    private boolean lastUsingItem;
    private int ticks;

    private int lastSlot;

    private boolean wasEating;

    public Noslow() {
        super("Noslow", Category.MOVEMENT);
        this.addSettings(swordMethod, consumableMethod, forward, strafe, blinkTicks, allowSprinting);
    }

    @Override
    public void onEnable() {
        lastUsingItem = wasEating = false;
        lastSlot = mc.thePlayer.inventory.currentItem;

        ticks = 0;
    }

    @Override
    public void onDisable() {
        Vestige.instance.getPacketBlinkHandler().stopAll();
    }

    @Override
    public void onClientStarted() {
        killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (isUsingItem()) {
            if (isBlocking()) {
                switch (swordMethod.getMode()) {
                    case "NCP":
                        PacketUtil.releaseUseItem(true);
                        break;
                    case "AAC4":
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            PacketUtil.releaseUseItem(true);
                        }
                        break;
                    case "AAC5":
                        if(lastUsingItem) {
                            PacketUtil.sendBlocking(true, false);
                        }
                        break;
                    case "Spoof":
                        int slot = mc.thePlayer.inventory.currentItem;

                        PacketUtil.sendPacket(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));

                        if(lastUsingItem) {
                            PacketUtil.sendBlocking(true, false);
                        }
                        break;
                    case "Spoof2":
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        break;
                }
            } else {
                switch (consumableMethod.getMode()) {
                    case "AAC4":
                        if(lastUsingItem) {
                            int slot = mc.thePlayer.inventory.currentItem;

                            PacketUtil.sendPacket(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                            PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
                        }
                        break;
                    case "AAC5":
                        if(lastUsingItem) {
                            PacketUtil.sendBlocking(true, false);
                        }
                        break;
                }
            }
        }

        if(swordMethod.is("Blink")) {
            if(isHoldingSword() && pressingUseItem()) {
                if(ticks == 1) {
                    Vestige.instance.getPacketBlinkHandler().releaseAll();
                    Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                }

                if(ticks > 0 && ticks < blinkTicks.getValue()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }

                if(ticks == blinkTicks.getValue()) {
                    Vestige.instance.getPacketBlinkHandler().stopAll();

                    mc.gameSettings.keyBindUseItem.pressed = true;

                    ticks = 0;
                }

                ticks++;
            } else {
                Vestige.instance.getPacketBlinkHandler().stopAll();

                ticks = 0;
            }
        }

        if(consumableMethod.is("Hypixel")) {
            if(wasEating && mc.thePlayer.isUsingItem()) {
                Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                mc.gameSettings.keyBindUseItem.pressed = false;
                ticks = 32;

                lastSlot = mc.thePlayer.inventory.currentItem;

                mc.thePlayer.inventory.currentItem = (lastSlot + 1) % 8;
                Vestige.instance.getSlotSpoofHandler().startSpoofing(lastSlot);
            }

            if(ticks == 31) {
                mc.thePlayer.inventory.currentItem = lastSlot;
                Vestige.instance.getSlotSpoofHandler().stopSpoofing();
            }

            if(ticks > 1) {
                mc.gameSettings.keyBindUseItem.pressed = false;

                if(!Mouse.isButtonDown(1)) {
                    ticks = 2;
                }
            } else if(ticks == 1) {
                Vestige.instance.getPacketBlinkHandler().stopAll();
                LogUtil.addChatMessage("Stopped eating");
            }

            ticks--;
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        boolean usingItem = mc.thePlayer.isUsingItem();

        if (usingItem) {
            if (isBlocking()) {
                switch (swordMethod.getMode()) {
                    case "NCP":
                        if (isBlocking()) {
                            PacketUtil.sendBlocking(true, false);
                        }
                        break;
                    case "AAC4":
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            PacketUtil.sendBlocking(true, false);
                        }
                        break;
                }
            } else {
                switch (consumableMethod.getMode()) {

                }
            }
        }

        lastUsingItem = usingItem;

        wasEating = usingItem && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
    }

    @Listener
    public void onSlowdown(SlowdownEvent event) {
        if((isBlocking() && swordMethod.is("None")) || (!isBlocking() && consumableMethod.is("None"))) {

        } else {
            event.setForward((float) forward.getValue());
            event.setStrafe((float) strafe.getValue());
            event.setAllowedSprinting(allowSprinting.isEnabled());
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if(event.getPacket() instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging packet = event.getPacket();

            if(packet.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                if(isHoldingSword() && swordMethod.is("Spoof")) {
                    event.setCancelled(true);

                    int slot = mc.thePlayer.inventory.currentItem;

                    PacketUtil.sendPacketFinal(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                    PacketUtil.sendPacketFinal(new C09PacketHeldItemChange(slot));
                }
            }
        }
    }
    
    @Listener
    public void onItemRender(ItemRenderEvent event) {
        if(isHoldingSword() && pressingUseItem() && swordMethod.is("Blink")) {
            event.setRenderBlocking(true);
        }

        if(consumableMethod.is("Hypixel") && ticks > 1) {
            event.setRenderBlocking(true);
        }
    }

    public boolean isBlocking() {
        return mc.thePlayer.isUsingItem() && isHoldingSword();
    }

    public boolean isUsingItem() {
        return mc.thePlayer.isUsingItem() && !(killauraModule.isEnabled() && killauraModule.getTarget() != null && !killauraModule.autoblock.is("None"));
    }

    public boolean isHoldingSword() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public boolean pressingUseItem() {
        return !(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) && Mouse.isButtonDown(1);
    }

}