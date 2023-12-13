package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "FastBow", description = "Lets you use bows faster", category = Category.COMBAT)
public class FastBow extends Module {
    private final NumberSetting targets = new NumberSetting("Targets", this, 4, 1, 20, 1);
    private final NumberSetting packets = new NumberSetting("Packets", this, 20, 1, 25, 1);
    private final NumberSetting range = new NumberSetting("Range", this, 40, 10, 60, 0.5);
    private final BooleanSetting timerBypass = new BooleanSetting("Timer Bypass", this, true);
    private final BooleanSetting invisible = new BooleanSetting("Invisibles", this, true);
    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", this, true);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (!mc.thePlayer.onGround || !mc.gameSettings.keyBindUseItem.isKeyDown())
            return;

        final List<EntityLivingBase> targets = PlayerUtil.getEntities(range.getValue(), players.isEnabled(), nonPlayers.isEnabled(), true, invisible.isEnabled(), false).stream().filter(entity -> mc.thePlayer.canEntityBeSeen(entity)).collect(Collectors.toList());

        if (targets.isEmpty())
            return;

        for (final EntityLivingBase target : targets) {
            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

            if (timerBypass.isEnabled()) {
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.5F, mc.thePlayer.posZ, false));
            }

            for (int i = 0; i < packets.getValue(); i++) {
                final double x = (target.posX - (target.lastTickPosX - target.posX)) - mc.thePlayer.posX;
                final double z = (target.posZ - (target.lastTickPosZ - target.posZ)) - mc.thePlayer.posZ;

                double minus = (mc.thePlayer.posY - target.posY);

                if (minus < -1.4) minus = -1.4;
                if (minus > 0.1) minus = 0.1;

                final double y = (target.posY - (target.lastTickPosY - target.posY)) + target.getEyeHeight() * 1.75 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;

                final double xzSqrt = MathHelper.sqrt_double(x * x + z * z);

                final float yaw = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(z, x)) - 90.0F);
                final float pitch = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(-Math.atan2(y, xzSqrt)));

                PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, true));
            }
            swap(9, mc.thePlayer.inventory.currentItem);
            swap(9, mc.thePlayer.inventory.currentItem);
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    public void swap(final int slot, final int hotBarNumber) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotBarNumber, 2, mc.thePlayer);
    }
}
