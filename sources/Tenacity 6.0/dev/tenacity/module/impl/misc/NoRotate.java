// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class NoRotate extends Module
{
    private final ModeSetting mode;
    private final BooleanSetting fakeUpdate;
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        if (NoRotate.mc.thePlayer == null) {
            return;
        }
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)e.getPacket();
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Normal": {
                    packet.setYaw(NoRotate.mc.thePlayer.rotationYaw);
                    packet.setPitch(NoRotate.mc.thePlayer.rotationPitch);
                    break;
                }
                case "Cancel": {
                    e.cancel();
                    break;
                }
            }
            if (this.fakeUpdate.isEnabled()) {
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(NoRotate.mc.thePlayer.posX, NoRotate.mc.thePlayer.posY, NoRotate.mc.thePlayer.posZ, packet.getYaw(), packet.getPitch(), NoRotate.mc.thePlayer.onGround));
            }
        }
    }
    
    public NoRotate() {
        super("NoRotate", "No Rotate", Category.MISC, "Prevents servers from rotating you");
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Cancel" });
        this.fakeUpdate = new BooleanSetting("Fake Update", false);
        this.addSettings(this.fakeUpdate);
    }
}
