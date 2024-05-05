package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventStrafe;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.RaytraceUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "Flag Reset", description = "Reset flag violations for watchdog", category = Category.OTHER)
public class FlagReset extends Module {

    private final DelayUtil disableDelay = new DelayUtil();

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY *= 0.5;
                final MovingObjectPosition object = RaytraceUtil.rayTrace(3.0f, mc.thePlayer.rotationYaw, -90);
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, null, object.getBlockPos(), object.sideHit, object.hitVec)) {
                    ChatUtil.sendChat("Reset scaffold vl.");
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
            }
            if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock))
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down(),
                        EnumFacing.UP.getIndex(), null, 0, (float) Math.random() / 5f, 0));
            mc.gameSettings.keyBindSneak.pressed = !mc.gameSettings.keyBindSneak.pressed;
            if (disableDelay.hasReached(2000))
                setEnabled(false);
        }
        if (event instanceof EventMotion) {
            mc.thePlayer.setSprinting(false);
        }
        if (event instanceof EventStrafe) {
            final EventStrafe e = (EventStrafe) event;
            e.setCancelled(true);
        }
        if (event instanceof EventJump) {
            final EventJump e = (EventJump) event;
            e.setSpeed(0);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (disableDelay.hasReached(2000) && !disableDelay.hasReached(3000)) {
            setEnabled(false);
            return;
        }
        disableDelay.reset();
        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Flag Reset", "Removing flags...", 2000, Type.INFO));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.keyBindSneak.pressed = false;
    }
}