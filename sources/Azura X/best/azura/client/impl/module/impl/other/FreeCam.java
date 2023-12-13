package best.azura.client.impl.module.impl.other;

import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Free Cam", category = Category.OTHER, description = "Move around freely")
public class FreeCam extends Module {

    @EventHandler
    public Listener<Event> eventListener = this::handle;
    private Vec3 startPos;
    private float startYaw, startPitch;
    private int startSlot;
    private EntityOtherPlayerMP temp;

    private void handle(Event event) {
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            e.setNoClip(true);
            e.setX(mc.thePlayer.motionX = 0);
            e.setY(mc.thePlayer.motionY = 0);
            e.setZ(mc.thePlayer.motionZ = 0);
            if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(0.5, e);
            if (mc.gameSettings.keyBindJump.pressed) e.setY(e.getY() + 0.5);
            if (mc.gameSettings.keyBindSneak.pressed) e.setY(e.getY() - 0.5);
            mc.playerController.curBlockDamageMP = 0;
        }
        if (event instanceof EventSentPacket) {
            if (mc.thePlayer == null || mc.thePlayer.isDead) {
                setEnabled(false);
                return;
            }
            EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C00PacketKeepAlive) return;
            if (e.getPacket() instanceof C0FPacketConfirmTransaction) return;
            e.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            startPos = mc.thePlayer.getPositionVector();
            startYaw = mc.thePlayer.rotationYaw;
            startPitch = mc.thePlayer.rotationPitch;
            startSlot = mc.thePlayer.inventory.currentItem;
            temp = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            temp.copyDataFromOld(mc.thePlayer);
        } catch (Exception ignored) {}
        mc.theWorld.addEntityToWorld(-1, temp);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            mc.thePlayer.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
            mc.thePlayer.rotationYaw = startYaw;
            mc.thePlayer.rotationPitch = startPitch;
            mc.thePlayer.inventory.currentItem = startSlot;
            mc.theWorld.removeEntityFromWorld(-1);
        } catch (Exception ignored) {}
        temp = null;
        startPos = null;
    }
}