/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.Wrapper;
import java.io.PrintStream;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
extends Module {
    private int delay = 0;
    private boolean shouldPacket;
    private boolean shouldRealPacket;
    private boolean strafe;
    private boolean nocheat = false;

    public NoFall() {
        super("NoFall", 0, Category.PLAYER, 16763904, true, "Better TTF Chat", new String[]{"nf", "fall"});
    }

    @EventTarget
    public void onMotion(EventPreMotionUpdates event) {
        if (this.shouldRealPacket) {
            Wrapper.getPlayer().motionX /= 1.5;
            Wrapper.getPlayer().motionZ /= 1.5;
        }
        if (!Wrapper.isMoving(Wrapper.getPlayer()) && Wrapper.getPlayer().isCollidedVertically && this.shouldRealPacket) {
            if (this.strafe) {
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + 0.1, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                this.strafe = !this.strafe;
            } else if (!this.strafe) {
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX - 0.1, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                boolean bl2 = this.strafe = !this.strafe;
            }
        }
        if (this.nocheat) {
            if (!(Wrapper.getPlayer().fallDistance <= 2.0f || Wrapper.getDistanceToGround(Wrapper.getPlayer()) > 2.0f || Wrapper.getPlayer().isCollidedVertically || Wrapper.getGameSettings().keyBindJump.getIsKeyPressed() || this.shouldPacket)) {
                Wrapper.getPlayer().setVelocity(Wrapper.getPlayer().motionX, 0.0, Wrapper.getPlayer().motionZ);
                Wrapper.getPlayer().motionY = 0.0;
                this.shouldPacket = true;
                this.shouldRealPacket = true;
            } else {
                this.shouldPacket = false;
            }
        } else if (Wrapper.getPlayer().fallDistance >= 2.0f) {
            Wrapper.sendPacket(new C03PacketPlayer(true));
            Wrapper.getPlayer().fallDistance = 0.0f;
        }
    }

    @EventTarget
    public void onSendPacket(EventPacketSent event) {
        if (event.getPacket() instanceof C03PacketPlayer && (Wrapper.getPlayer().fallDistance > 4.0f || this.shouldRealPacket)) {
            Cheese.getInstance();
            if (!Cheese.moduleManager.getModbyName("CheeseWalker").getState()) {
                Cheese.getInstance();
                if (!Cheese.moduleManager.getModbyName("Glide").getState()) {
                    event.setCancelled(true);
                    System.out.println("asd");
                    Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.6, Wrapper.getPlayer().posZ, false));
                }
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.shouldPacket = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.shouldRealPacket = false;
    }
}

