package fun.expensive.client.feature.impl.misc;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.MultipleBoolSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.inventory.InvenotryUtil;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.movement.MovementUtils;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public class Disabler extends Feature {
    public ListSetting disablerMode = new ListSetting("Disabler Mode", "Old Matrix", () -> true, "Old Matrix", "Storm Movement");
    public static MultipleBoolSetting disablerSettings = new MultipleBoolSetting("Addons", new BooleanSetting("Flight", true), new BooleanSetting("Strafe"), new BooleanSetting("HighJump", true));
    public final NumberSetting disablerspeed = new NumberSetting("Disabler Speed", 0.2F, 0.01f, 0.3f, 0.01F, () -> disablerMode.currentMode.equals("Elytra"));
    public BooleanSetting resetMotion = new BooleanSetting("Reset Motion", "Если вас кикает за то , что нельзя долго летать, то это пофиксит", true, () -> disablerMode.currentMode.equals("Elytra"));
    public TimerHelper timerHelper = new TimerHelper();
    static boolean oa = false;

    public Disabler() {
        super("Disabler", "Ослабляет воздействие античитов на вас", FeatureCategory.Misc);
        addSettings(disablerMode, disablerSettings, disablerspeed,resetMotion);
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
                    NotificationRenderer.queue("§6Disabler Exploit", "§cВозьмите элитры в инвентарь!", 6, NotificationMode.WARNING);
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
