/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.CombatUtils;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

public class CivBreak
extends Module {
    BlockPos var1 = null;
    public static boolean isMining = false;

    public double getDistance() {
        double var7 = (double)this.var1.getX() - Wrapper.mc.thePlayer.posX;
        double var8 = (double)this.var1.getY() - Wrapper.mc.thePlayer.posY;
        double var9 = (double)this.var1.getZ() - Wrapper.mc.thePlayer.posZ;
        return MathHelper.sqrt_double(var7 * var7 + var8 * var8 + var9 * var9);
    }

    public CivBreak() {
        super("CivBreak", 0, Category.EXPLOITS, 8781660, true, "Spam breaks selected block", new String[]{"civb", "civ", "cvbreak", "cbreak", "cb"});
    }

    @EventTarget
    public void onTick(EventTick tick) {
        if (Wrapper.mc.objectMouseOver != null) {
            MovingObjectPosition objectMouseOver = Wrapper.mc.objectMouseOver;
            if (MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && Wrapper.mc.objectMouseOver.func_178782_a() != null && !Wrapper.mc.playerController.isHittingBlock && !isMining && Wrapper.mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
                this.var1 = Wrapper.mc.objectMouseOver.func_178782_a();
            }
        }
        if (!Wrapper.mc.gameSettings.keyBindAttack.getIsKeyPressed() || this.getDistance() >= 5.0) {
            this.var1 = null;
        }
        if (this.var1 != null) {
            isMining = true;
            CombatUtils.faceBlock(this.var1, 180.0f, 90.0f);
            Wrapper.mc.playerController.blockHitDelay = 0;
            Wrapper.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            Wrapper.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.var1, EnumFacing.fromAngle(-1.0)));
            Wrapper.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.var1, EnumFacing.fromAngle(-1.0)));
            Wrapper.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.var1, -1, Wrapper.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
        } else {
            isMining = false;
        }
    }

    @EventTarget
    public void onPacket(EventPacketSent sent) {
        if (!isMining) {
            return;
        }
        if (sent.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            sent.setPacket(new C03PacketPlayer.C05PacketPlayerLook(CombatUtils.blockYaw, CombatUtils.blockPitch, Wrapper.mc.thePlayer.onGround));
        }
        if (sent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            sent.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ, CombatUtils.blockYaw, CombatUtils.blockPitch, Wrapper.mc.thePlayer.onGround));
        }
    }
}

