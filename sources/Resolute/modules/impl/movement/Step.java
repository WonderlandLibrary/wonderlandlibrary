// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import vip.Resolute.events.impl.EventPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventStep;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Step extends Module
{
    public ModeSetting mode;
    public BooleanSetting lessPackets;
    public static boolean cancelStep;
    private final double[] offsets;
    private float timerWhenStepping;
    private boolean cancelMorePackets;
    private byte cancelledPackets;
    
    public Step() {
        super("Step", 0, "Allows for a higher block step", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "NCP", new String[] { "NCP" });
        this.lessPackets = new BooleanSetting("Less Packets", true, () -> this.mode.is("NCP"));
        this.offsets = new double[] { 0.41999998688697815, 0.7531999945640564 };
        this.addSettings(this.mode);
    }
    
    @Override
    public void onDisable() {
        Step.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        final int i = 0;
        if (e instanceof EventStep) {
            final EventStep event = (EventStep)e;
            if (!MovementUtils.isInLiquid() && MovementUtils.isOnGround()) {
                if (e.isPre()) {
                    event.setStepHeight(Step.cancelStep ? 0.0f : 1.0f);
                }
                else {
                    final double steppedHeight = event.getStepHeight();
                    for (final double offset : this.offsets) {
                        Step.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + offset * steppedHeight, Step.mc.thePlayer.posZ, false));
                    }
                    this.timerWhenStepping = 1.0f / (this.offsets.length + 1);
                    this.cancelMorePackets = true;
                }
            }
        }
        if (e instanceof EventPacket && this.lessPackets.isEnabled() && ((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
            if (this.cancelledPackets > 0) {
                this.cancelMorePackets = false;
                this.cancelledPackets = 0;
                Step.mc.timer.timerSpeed = 1.0f;
            }
            if (this.cancelMorePackets) {
                Step.mc.timer.timerSpeed = this.timerWhenStepping;
                ++this.cancelledPackets;
            }
        }
    }
}
