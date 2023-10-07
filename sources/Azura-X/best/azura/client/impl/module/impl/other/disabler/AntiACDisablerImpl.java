package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.util.other.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AntiACDisablerImpl implements ModeImpl<Disabler> {

    private boolean waitingForFlag, flagged;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "AntiAC";
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventWorldChange) {
            flagged = waitingForFlag = false;
        }
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (!e.isPre()) return;
            boolean foundShop = false;
            boolean foundTeamStand = false;
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer)) {
                    if (entity instanceof EntityArmorStand && entity.getName().contains("Team")) {
                        foundTeamStand = true;
                    }
                    continue;
                }
                if (entity == mc.thePlayer) continue;
                if (entity.getName().contains("Shop") && entity.getName().contains("§")) {
                    foundShop = true;
                    break;
                }
            }
            if (foundShop) flagged = true;
            if (mc.thePlayer.ticksExisted >= 60 && !foundShop && mc.thePlayer.onGround && !waitingForFlag && !flagged) {
                e.y += 4;
                e.onGround = false;
                waitingForFlag = true;
                ChatUtil.sendChat("Waiting for sex");
            }
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (waitingForFlag && e.getPacket() instanceof S08PacketPlayerPosLook) {
                ChatUtil.sendChat("Sexed on tick " + mc.thePlayer.ticksExisted);
                waitingForFlag = false;
                flagged = true;
            }
        }
    }

    @Override
    public void onEnable() {
        waitingForFlag = false;
        Client.INSTANCE.getEventBus().register(this);
    }

    @Override
    public void onDisable() {
        waitingForFlag = false;
        Client.INSTANCE.getEventBus().unregister(this);
    }
}