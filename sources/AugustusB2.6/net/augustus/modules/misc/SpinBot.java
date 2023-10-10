// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.augustus.utils.RotationUtil;
import net.augustus.events.EventJump;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventMove;
import net.augustus.events.EventPreMotion;
import net.augustus.events.EventClick;
import net.augustus.utils.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import net.augustus.Augustus;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventEarlyTick;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleansSetting;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class SpinBot extends Module
{
    public final BooleanValue moveFix;
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    private final BooleanValue click;
    private final BooleanValue chest;
    private final BooleanValue craftingTable;
    private final BooleanValue furnace;
    private final BooleansSetting stopOn;
    private float[] rots;
    private float[] lastRots;
    private boolean spin;
    
    public SpinBot() {
        super("SpinBot", new Color(50, 168, 82), Categorys.MISC);
        this.moveFix = new BooleanValue(1, "MoveFix", this, false);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.click = new BooleanValue(3, "Click", this, true);
        this.chest = new BooleanValue(4, "Chest", this, true);
        this.craftingTable = new BooleanValue(5, "Crafter", this, false);
        this.furnace = new BooleanValue(6, "Furnace", this, false);
        this.stopOn = new BooleansSetting(2, "StopOn", this, new Setting[] { this.click, this.chest, this.craftingTable, this.furnace });
        this.rots = new float[2];
        this.lastRots = new float[2];
    }
    
    @Override
    public void onEnable() {
        if (SpinBot.mc.thePlayer != null) {
            this.rots = new float[] { SpinBot.mc.thePlayer.rotationYaw, SpinBot.mc.thePlayer.rotationPitch };
            this.lastRots = new float[] { SpinBot.mc.thePlayer.prevRotationYaw, SpinBot.mc.thePlayer.prevRotationPitch };
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        if (this.spin) {
            this.spin();
        }
    }
    
    private boolean shouldSpin() {
        final MovingObjectPosition objectPosition = SpinBot.mc.thePlayer.customRayTrace(4.5, 1.0f, Augustus.getInstance().getYawPitchHelper().realYaw, Augustus.getInstance().getYawPitchHelper().realPitch);
        if (this.click.getBoolean() && (Mouse.isButtonDown(1) || Mouse.isButtonDown(0))) {
            return false;
        }
        if (objectPosition != null && objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (this.chest.getBoolean() && SpinBot.mc.theWorld.getBlockState(objectPosition.getBlockPos()).getBlock() instanceof BlockChest) {
                return false;
            }
            if (this.craftingTable.getBoolean() && SpinBot.mc.theWorld.getBlockState(objectPosition.getBlockPos()).getBlock().equals(Block.getBlockById(58))) {
                return false;
            }
            if (this.furnace.getBoolean() && SpinBot.mc.theWorld.getBlockState(objectPosition.getBlockPos()).getBlock().equals(Block.getBlockById(61))) {
                return false;
            }
        }
        return (!SpinBot.mm.killAura.isToggled() || SpinBot.mm.killAura.target == null) && !BlockUtil.isScaffoldToggled() && !SpinBot.mm.teleport.isToggled();
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        this.spin = false;
        if (this.shouldSpin()) {
            this.spin = true;
        }
        else {
            this.timeHelper2.reset();
            if (this.spin) {
                SpinBot.mc.thePlayer.rotationYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
                SpinBot.mc.thePlayer.rotationPitch = Augustus.getInstance().getYawPitchHelper().realPitch;
                SpinBot.mc.thePlayer.prevRotationYaw = Augustus.getInstance().getYawPitchHelper().realLastYaw;
                SpinBot.mc.thePlayer.prevRotationPitch = Augustus.getInstance().getYawPitchHelper().realLastPitch;
            }
            this.spin = false;
        }
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        if (this.moveFix.getBoolean()) {
            return;
        }
        if (this.spin) {
            this.spin();
            if (this.timeHelper2.reached(1000L)) {
                eventPreMotion.setYaw(this.rots[0]);
                eventPreMotion.setPitch(this.rots[1]);
                SpinBot.mc.thePlayer.rotationYawHead = this.rots[0];
                SpinBot.mc.thePlayer.rotationPitchHead = this.rots[1];
                SpinBot.mc.thePlayer.renderYawOffset = this.rots[0];
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (!this.spin) {
            return;
        }
        if (!this.moveFix.getBoolean()) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (!this.spin) {
            return;
        }
        if (this.moveFix.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!this.spin) {
            return;
        }
        if (!this.moveFix.getBoolean()) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    private void spin() {
        if (this.timeHelper2.reached(1000L)) {
            if (this.timeHelper.reached(50L)) {
                final float[] rots = this.rots;
                final int n = 0;
                rots[n] += 90.0f;
                this.rots[1] = 90.0f;
                this.timeHelper.reset();
            }
            final RotationUtil rotationUtil = new RotationUtil();
            this.rots = RotationUtil.mouseSens(this.rots[0], this.rots[1], this.lastRots[0], this.lastRots[1]);
            if (this.moveFix.getBoolean()) {
                SpinBot.mc.thePlayer.rotationYaw = this.rots[0];
                SpinBot.mc.thePlayer.rotationPitch = this.rots[1];
                SpinBot.mc.thePlayer.prevRotationYaw = this.lastRots[0];
                SpinBot.mc.thePlayer.prevRotationPitch = this.lastRots[1];
            }
        }
    }
}
