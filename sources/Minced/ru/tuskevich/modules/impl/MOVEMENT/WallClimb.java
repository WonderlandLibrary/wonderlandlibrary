// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.tuskevich.util.chat.ChatUtility;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "WallClimb", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class WallClimb extends Module
{
    TimerUtility timerUtils;
    ModeSetting srModeSetting;
    SliderSetting climbSpeed;
    
    public WallClimb() {
        this.timerUtils = new TimerUtility();
        this.srModeSetting = new ModeSetting("Spider Mode", "Matrix", new String[] { "Matrix", "Sunrise" });
        this.climbSpeed = new SliderSetting("Climb Speed", 1.0f, 0.0f, 5.0f, 0.1f);
        this.add(this.srModeSetting, this.climbSpeed);
    }
    
    @EventTarget
    public void onMotion(final EventMotion eventMotion) {
        final Minecraft mc = WallClimb.mc;
        if (!Minecraft.player.collidedHorizontally) {
            return;
        }
        if (this.srModeSetting.is("Matrix") && this.timerUtils.hasTimeElapsed((long)(this.climbSpeed.getFloatValue() * 100.0f))) {
            eventMotion.setOnGround(true);
            final Minecraft mc2 = WallClimb.mc;
            Minecraft.player.onGround = true;
            final Minecraft mc3 = WallClimb.mc;
            Minecraft.player.collidedVertically = true;
            final Minecraft mc4 = WallClimb.mc;
            Minecraft.player.collidedHorizontally = true;
            final Minecraft mc5 = WallClimb.mc;
            Minecraft.player.isAirBorne = true;
            final Minecraft mc6 = WallClimb.mc;
            Minecraft.player.jump();
            this.timerUtils.reset();
        }
        if (this.srModeSetting.is("Sunrise")) {
            int block = -1;
            for (int i = 0; i < 9; ++i) {
                final Minecraft mc7 = WallClimb.mc;
                final ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
                if (s.getItem() instanceof ItemBlock) {
                    block = i;
                    break;
                }
            }
            if (block == -1 && this.timerUtils.hasTimeElapsed(1000L)) {
                ChatUtility.addChatMessage("\u0412\u043e\u0437\u044c\u043c\u0438\u0442\u0435 \u0431\u043b\u043e\u043a\u043e\u0432 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c");
                this.timerUtils.reset();
                return;
            }
            if (this.timerUtils.hasTimeElapsed((long)(this.climbSpeed.getFloatValue() * 55.0f))) {
                try {
                    if (block != -1 && WallClimb.mc.objectMouseOver != null && WallClimb.mc.objectMouseOver.hitVec != null && WallClimb.mc.objectMouseOver.sideHit != null) {
                        final Minecraft mc8 = WallClimb.mc;
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(block));
                        final Minecraft mc9 = WallClimb.mc;
                        final float prevPitch = Minecraft.player.rotationPitch;
                        final Minecraft mc10 = WallClimb.mc;
                        Minecraft.player.rotationPitch = -60.0f;
                        WallClimb.mc.entityRenderer.getMouseOver(1.0f);
                        final Vec3d facing = WallClimb.mc.objectMouseOver.hitVec;
                        final BlockPos stack = WallClimb.mc.objectMouseOver.getBlockPos();
                        final float f = (float)(facing.x - stack.getX());
                        final float f2 = (float)(facing.y - stack.getY());
                        final float f3 = (float)(facing.z - stack.getZ());
                        final Minecraft mc11 = WallClimb.mc;
                        final NetHandlerPlayClient connection = Minecraft.player.connection;
                        final Minecraft mc12 = WallClimb.mc;
                        connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SNEAKING));
                        final WorldClient world = WallClimb.mc.world;
                        final Minecraft mc13 = WallClimb.mc;
                        if (world.getBlockState(new BlockPos(Minecraft.player).add(0, 2, 0)).getBlock() == Blocks.AIR) {
                            final Minecraft mc14 = WallClimb.mc;
                            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack, WallClimb.mc.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f2, f3));
                        }
                        else {
                            final Minecraft mc15 = WallClimb.mc;
                            Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, stack, WallClimb.mc.objectMouseOver.sideHit));
                            final Minecraft mc16 = WallClimb.mc;
                            Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, stack, WallClimb.mc.objectMouseOver.sideHit));
                        }
                        final Minecraft mc17 = WallClimb.mc;
                        final NetHandlerPlayClient connection2 = Minecraft.player.connection;
                        final Minecraft mc18 = WallClimb.mc;
                        connection2.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
                        final Minecraft mc19 = WallClimb.mc;
                        Minecraft.player.rotationPitch = prevPitch;
                        WallClimb.mc.entityRenderer.getMouseOver(1.0f);
                        final Minecraft mc20 = WallClimb.mc;
                        final NetHandlerPlayClient connection3 = Minecraft.player.connection;
                        final Minecraft mc21 = WallClimb.mc;
                        connection3.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
                        eventMotion.setOnGround(true);
                        final Minecraft mc22 = WallClimb.mc;
                        Minecraft.player.onGround = true;
                        final Minecraft mc23 = WallClimb.mc;
                        Minecraft.player.collidedVertically = true;
                        final Minecraft mc24 = WallClimb.mc;
                        Minecraft.player.collidedHorizontally = true;
                        final Minecraft mc25 = WallClimb.mc;
                        Minecraft.player.isAirBorne = true;
                        final Minecraft mc26 = WallClimb.mc;
                        Minecraft.player.jump();
                        this.timerUtils.reset();
                    }
                }
                catch (Exception ex) {}
            }
        }
    }
}
