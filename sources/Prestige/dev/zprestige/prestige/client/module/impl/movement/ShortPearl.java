package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.Rotation;
import dev.zprestige.prestige.client.util.impl.RotationUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class ShortPearl extends Module {

    public ModeSetting mode;
    public FloatSetting smoothing;
    public DragSetting minDelay;
    public BooleanSetting switchBack;
    public FloatSetting pitchSetting;
    public TimerUtil timer;
    public float pitch;
    public int progress;
    public int slot;

    public ShortPearl() {
        super("Short Pearl", Category.Movement, "Throws a pearl at your feet");
        mode = setting("Mode", "Legit", new String[]{"Legit", "Silent"});
        smoothing = setting("Smoothing", 0.5f, 0.1f, 2).description("Speed of the mouse movement").invokeVisibility(arg_0 -> mode.getObject().equals("Legit"));
        minDelay = setting("Min Delay", 30, 50, 0, 200).description("Delay between each action").invokeVisibility(arg_0 -> mode.getObject().equals("Legit"));
        switchBack = setting("Switch Back", true).description("Switches back to your previous slot after throwing pearl").invokeVisibility(arg_0 -> mode.getObject().equals("Legit"));
        pitchSetting = setting("Pitch", 70f, 30f, 90f).description("Pitch to throw pearl at");
        timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        timer.reset();
        pitch = getMc().player.getPitch();
        progress = 0;
        timer.reset();
        minDelay.setValue();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (mode.getObject().equals("Legit") && timer.delay(minDelay)) {
            switch (progress) {
                case 0 -> {
                    ClientPlayerEntity clientPlayerEntity = getMc().player;
                    if (clientPlayerEntity.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                        reset();
                        return;
                    }
                    Item item = Items.ENDER_PEARL;
                    Integer n = InventoryUtil.INSTANCE.findItemInHotbar(item);
                    if (n == null) {
                        toggle();
                        return;
                    }
                    slot = clientPlayerEntity.getInventory().selectedSlot;
                    InventoryUtil.INSTANCE.setCurrentSlot(n);
                    reset();
                }
                case 1 -> {
                    if (RotationUtil.INSTANCE.setPitch(new Rotation(0.0f, pitchSetting.getObject()), smoothing.getObject() / 2.0f, 0.0f, 0.5f)) {
                        PacketUtil.INSTANCE.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                        reset();
                    }
                }
                case 2 -> {
                    if (RotationUtil.INSTANCE.setPitch(new Rotation(0.0f, pitch), smoothing.getObject() / 2.0f, 0.0f, 0.5f)) {
                        if (!switchBack.getObject()) {
                            toggle();
                            break;
                        }
                        reset();
                    }
                }
                case 3 -> {
                    if (switchBack.getObject()) {
                        InventoryUtil.INSTANCE.setCurrentSlot(slot);
                        toggle();
                    }
                }
            }

        }
    }

    @EventListener
    public void event(TickEvent event) {
        if (mode.getObject().equals("Silent")) {
            Integer n = InventoryUtil.INSTANCE.findItemInHotbar(Items.ENDER_PEARL);
            if (n == null) {
                toggle();
                return;
            }
            float pitch = getMc().player.getPitch();
            int slot = getMc().player.getInventory().selectedSlot;
            InventoryUtil.INSTANCE.setCurrentSlot(n);
            getMc().player.setPitch(this.pitchSetting.getObject() - 1 + RandomUtil.INSTANCE.getRandom().nextFloat() * 2);
            getMc().interactionManager.interactItem(getMc().player, Hand.MAIN_HAND);
            getMc().player.setPitch(pitch);
            InventoryUtil.INSTANCE.setCurrentSlot(slot);
            toggle();
        }
    }

    void reset() {
        timer.reset();
        minDelay.setValue();
        progress += 1;
    }
}
