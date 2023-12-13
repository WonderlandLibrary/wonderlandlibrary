package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.BalanceUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiACSpeedImpl implements ModeImpl<Speed> {

    private long balance = 0, lastPacket;
    private int ticks;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Anti AC";
    }

    @Override
    public void onEnable() {
        balance = 0;
        lastPacket = 0;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        balance = 0;
        lastPacket = 0;
        ticks = 0;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03 = e.getPacket();
                if (MathUtil.getDifference(mc.thePlayer.posX, mc.thePlayer.lastTickPosX) == 0. &&
                        MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY) == 0. &&
                        MathUtil.getDifference(mc.thePlayer.posZ, mc.thePlayer.lastTickPosZ) == 0. && !c03.rotating) {
                    e.setCancelled(true);
                }
                if (lastPacket == 0) lastPacket = System.currentTimeMillis();
                long delay = System.currentTimeMillis() - lastPacket;
                balance += e.isCancelled() ? -delay : 50 - delay;
                lastPacket = System.currentTimeMillis();
            }
        }
        if (event instanceof EventRender2D) {
            final ScaledResolution sr = new ScaledResolution(mc);
            mc.fontRendererObj.drawStringWithShadow(String.valueOf(BalanceUtil.getBalance()), sr.getScaledWidth() / 2.0 - mc.fontRendererObj.getStringWidth(String.valueOf(BalanceUtil.getBalance())) / 2.0,
                    sr.getScaledHeight() / 2. + 40, -1);
        }
        if (event instanceof EventUpdate) {
            if (BalanceUtil.getBalance() > 0 || !mc.thePlayer.isMoving()) {
                mc.timer.timerSpeed = 1F;
            } else {
                mc.timer.timerSpeed = 2F;
            }
        }
    }

}