package ru.smertnix.celestial.feature.impl.misc;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.inventory.InvenotryUtil;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class Disabler extends Feature {
    public ListSetting disablerMode = new ListSetting("Disabler Mode", "Elytra", () -> true, "Elytra");
    public static MultipleBoolSetting disablerSettings = new MultipleBoolSetting("Addons", new BooleanSetting("Flight", true), new BooleanSetting("Strafe"), new BooleanSetting("HighJump", true));
    public final NumberSetting disablerspeed = new NumberSetting("Disabler Speed", 0.2F, 0.01f, 0.3f, 0.01F, () -> disablerMode.currentMode.equals("Elytra"));
    public BooleanSetting resetMotion = new BooleanSetting("Reset Motion", "", true, () -> disablerMode.currentMode.equals("Elytra"));
    public TimerHelper timerHelper = new TimerHelper();
    static boolean oa = false;

    public Disabler() {
        super("Disabler", "Даёт уязвимость использовать любую функцию", FeatureCategory.Util);
        addSettings();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(disablerMode.currentMode);
        if (disablerMode.currentMode.equals("Matrix Old")) {
            if (mc.player.ticksExisted % 3 == 0) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        } else if (disablerMode.currentMode.equals("Elytra")) {

            InvenotryUtil.swapElytraToChestplate();
            if (mc.player.ticksExisted % 6 == 0)
                InvenotryUtil.disabler(InvenotryUtil.getSlotWithElytra());

            if (disablerSettings.getSetting("Flight").getBoolValue()) {
                MovementUtils.setMotion(MovementUtils.getSpeed() * 1 + (disablerspeed.getNumberValue()));

                mc.player.motionY = 0.39f;
                if (mc.player.ticksExisted % 5 == 0 && !mc.player.onGround && resetMotion.getBoolValue()) {
                    mc.player.motionY -= 0.5f;
                }
            }
            if (disablerSettings.getSetting("HighJump").getBoolValue()) {
                mc.player.motionY += 0.5f;
                if (mc.player.ticksExisted % 5 == 0 && !mc.player.onGround) {
                    mc.player.motionY -= 0.5f;
                }
            }
            if (disablerSettings.getSetting("Strafe").getBoolValue()) {
                MovementUtils.setMotion(MovementUtils.getSpeed() * 1 + (disablerspeed.getNumberValue()));

                if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
                    mc.player.jump();
                    MovementUtils.strafe();
                }
            }
            int eIndex = -1;
            int elytraCount = 0;

            for (int i = 0; i < 45; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                    eIndex = i;
                }

                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                    elytraCount++;
                }
            }
            if (elytraCount == 0 && eIndex == -1) {
                if (mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
                    NotificationRenderer.queue("�6Disabler Exploit", "�c�������� ������ � ���������!", 6, NotificationMode.WARNING);
                    toggle();
                }
            }
        }
    }

    @EventTarget
    public void on(EventSendPacket event) {
        if (disablerMode.currentMode.equals("Elytra")) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
                cPacketPlayer.onGround = false;
            }
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        if (disablerMode.currentMode.equals("Storm Movement")) {
            event.setOnGround(false);
        }
    }
}
