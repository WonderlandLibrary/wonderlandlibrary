// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventJump;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventMove;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventUpdate;
import net.augustus.events.EventMoveEntity;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.augustus.utils.MoveUtil;
import net.augustus.Augustus;
import net.augustus.events.EventPreMotion;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Speed extends Module
{
    public StringValue mode;
    public DoubleValue vanillaSpeed;
    public DoubleValue vanillaHeight;
    public DoubleValue dmgSpeed;
    public BooleanValue damageBoost;
    public BooleanValue strafe;
    private int tickCounter;
    private int tickCounter2;
    private int ticks;
    private float speedYaw;
    private boolean wasOnGround;
    
    public Speed() {
        super("Speed", new Color(74, 133, 93), Categorys.MOVEMENT);
        this.mode = new StringValue(1, "Mode", this, "Mineplex", new String[] { "Mineplex", "VOnGround", "VBhop", "LegitHop", "Matrix", "NCP", "Test", "Verus" });
        this.vanillaSpeed = new DoubleValue(2, "Speed", this, 1.0, 0.0, 10.0, 2);
        this.vanillaHeight = new DoubleValue(3, "Height", this, 1.0, 0.0, 5.0, 2);
        this.dmgSpeed = new DoubleValue(4, "DMGSpeed", this, 1.0, 0.0, 2.0, 2);
        this.damageBoost = new BooleanValue(5, "DamageBoost", this, false);
        this.strafe = new BooleanValue(6, "Strafe", this, false);
        this.ticks = 0;
    }
    
    @Override
    public void onEnable() {
        Speed.mc.getTimer().timerSpeed = 1.0f;
        if (Speed.mc.thePlayer != null) {
            Speed.mc.thePlayer.jumpMovementFactor = 0.02f;
            Speed.mc.thePlayer.setSpeedInAir(0.02f);
            Speed.mc.thePlayer.setSpeedOnGround(0.1f);
        }
        this.tickCounter2 = -2;
        this.tickCounter = 0;
        this.ticks = 0;
    }
    
    @Override
    public void onDisable() {
        Speed.mc.getTimer().timerSpeed = 1.0f;
        if (Speed.mc.thePlayer != null) {
            Speed.mc.thePlayer.jumpMovementFactor = 0.02f;
            Speed.mc.thePlayer.setSpeedInAir(0.02f);
            Speed.mc.thePlayer.setSpeedOnGround(0.1f);
        }
        this.tickCounter2 = -2;
        this.tickCounter = 0;
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        this.speedYaw = ((Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) ? Speed.mm.targetStrafe.moveYaw : Augustus.getInstance().getYawPitchHelper().realYaw);
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Mineplex": {
                if (this.canJump()) {
                    if (this.tickCounter2 < 0) {
                        ++this.tickCounter2;
                    }
                    if (Speed.mc.thePlayer.posY != (int)Speed.mc.thePlayer.posY) {
                        this.tickCounter2 = -2;
                    }
                    Speed.mc.thePlayer.jump();
                    this.tickCounter = 0;
                    MoveUtil.setMotion(-0.20000000298023224, 0.0, this.speedYaw);
                    Speed.mc.getTimer().timerSpeed = 1.0f;
                    break;
                }
                if (!this.isMoving()) {
                    break;
                }
                if (Speed.mc.thePlayer.isCollidedHorizontally) {
                    this.tickCounter2 = -2;
                    return;
                }
                ++this.tickCounter;
                if (Speed.mc.thePlayer.motionY < -0.4) {
                    return;
                }
                if (Speed.mc.thePlayer.motionY < 0.0) {
                    final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                    thePlayer.motionY += 0.033;
                }
                if (Speed.mc.thePlayer.motionY > 0.0) {
                    final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                    thePlayer2.motionY += 0.03;
                }
                if (Speed.mc.thePlayer.moveStrafing == 0.0f) {
                    MoveUtil.setMotion(0.61f - this.tickCounter / 100.0f + this.tickCounter2 / 7.0f, 0.0, this.speedYaw);
                    break;
                }
                this.tickCounter2 = ((this.tickCounter < 1) ? this.tickCounter : 0);
                MoveUtil.setMotion(0.61f - this.tickCounter / 100.0f + this.tickCounter2 / 7.0f, 0.0, this.speedYaw);
                break;
            }
            case "Verus": {
                MoveUtil.setSpeed2(0.2919999957084656);
                if (this.damageBoost.getBoolean() && Speed.mc.thePlayer.hurtTime != 0 && Speed.mc.thePlayer.fallDistance < 3.0f) {
                    MoveUtil.setSpeed2((float)this.dmgSpeed.getValue());
                }
                else {
                    MoveUtil.setSpeed2(0.2919999957084656);
                }
                if (this.canJump()) {
                    Speed.mc.thePlayer.jump();
                    break;
                }
                Speed.mc.thePlayer.jumpMovementFactor = 0.1f;
                break;
            }
            case "VOnGround": {
                if (this.isMoving()) {
                    MoveUtil.setSpeed((float)(0.1 * this.vanillaSpeed.getValue()), this.strafe.getBoolean());
                    break;
                }
                break;
            }
            case "VBhop": {
                if (this.canJump()) {
                    Speed.mc.thePlayer.motionY = Speed.mc.thePlayer.jumpMovementFactor * this.vanillaHeight.getValue();
                    if (this.strafe.getBoolean()) {
                        MoveUtil.strafe();
                        break;
                    }
                    break;
                }
                else {
                    if (this.isMoving()) {
                        MoveUtil.setSpeed((float)(0.1 * this.vanillaSpeed.getValue()), this.strafe.getBoolean());
                        break;
                    }
                    break;
                }
                break;
            }
            case "Matrix": {
                ++this.tickCounter;
                if (Speed.mc.thePlayer.motionX == 0.0 && Speed.mc.thePlayer.motionZ == 0.0) {
                    return;
                }
                if (MoveUtil.isMoving()) {
                    return;
                }
                final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                thePlayer3.motionY -= 0.009998999536037445;
                if (Speed.mc.thePlayer.onGround) {
                    this.tickCounter = 0;
                    MoveUtil.strafeMatrix();
                    break;
                }
                if (Speed.mc.thePlayer.movementInput.moveForward > 0.0f && Speed.mc.thePlayer.movementInput.moveStrafe != 0.0f) {
                    Speed.mc.thePlayer.setSpeedInAir(0.02f);
                    break;
                }
                Speed.mc.thePlayer.setSpeedInAir(0.0208f);
                break;
            }
            case "Test": {
                if (Speed.mc.thePlayer.onGround) {
                    this.tickCounter = 0;
                }
                if (MoveUtil.isMoving() && Speed.mm.killAura.target == null && !Speed.mm.longJump.isToggled()) {
                    if (Speed.mc.thePlayer.isUsingItem()) {
                        if (Speed.mc.thePlayer.isBlocking()) {
                            Speed.mc.getTimer().timerSpeed = 1.23f;
                        }
                        else if (Speed.mc.getTimer().timerSpeed > 1.0f) {
                            Speed.mc.getTimer().timerSpeed = 1.0f;
                        }
                    }
                    else {
                        Speed.mc.getTimer().timerSpeed = 1.23f;
                    }
                }
                else if (Speed.mc.getTimer().timerSpeed > 1.0f) {
                    Speed.mc.getTimer().timerSpeed = 1.0f;
                }
                if (Speed.mc.thePlayer.hurtTime > 8 && !Speed.mm.longJump.isToggled() && !Speed.mc.thePlayer.isBurning() && Speed.mc.thePlayer.fallDistance < 2.0f && !Speed.mc.thePlayer.isPotionActive(Potion.wither) && !Speed.mc.thePlayer.isPotionActive(Potion.poison)) {
                    MoveUtil.addSpeed(0.4000000059604645, false);
                }
                ++this.tickCounter;
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventMoveEntity(final EventMoveEntity eventMoveEntity) {
        if (Speed.mc.thePlayer.onGround) {
            this.ticks = 0;
        }
        ++this.ticks;
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "NCP": {
                if (MoveUtil.isMoving()) {
                    if (Speed.mc.thePlayer.onGround) {
                        Speed.mc.thePlayer.jump();
                        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                        thePlayer.motionX *= 1.01;
                        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                        thePlayer2.motionZ *= 1.01;
                        Speed.mc.thePlayer.setSpeedInAir(0.022f);
                    }
                    final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                    thePlayer3.motionY -= 9.9999E-4;
                    MoveUtil.strafe();
                }
                else {
                    Speed.mc.thePlayer.motionX = 0.0;
                    Speed.mc.thePlayer.motionZ = 0.0;
                }
                Speed.mc.getTimer().timerSpeed = 1.0865f;
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "LegitHop":
            case "Matrix": {
                if (this.isMoving()) {
                    Speed.mc.thePlayer.movementInput.jump = true;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (Speed.mc.thePlayer.fallDistance > 4.0f && !this.mode.getSelected().equals("LegitHop")) {
            eventMove.setCanceled(true);
        }
        if (!BlockUtil.isScaffoldToggled()) {
            eventMove.setYaw((Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) ? Speed.mm.targetStrafe.moveYaw : ((Speed.mm.killAura.isToggled() && Speed.mm.killAura.target != null && Speed.mm.killAura.moveFix.getBoolean()) ? Speed.mc.thePlayer.rotationYaw : Augustus.getInstance().getYawPitchHelper().realYaw));
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!BlockUtil.isScaffoldToggled()) {
            eventJump.setYaw((Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) ? Speed.mm.targetStrafe.moveYaw : ((Speed.mm.killAura.isToggled() && Speed.mm.killAura.target != null && Speed.mm.killAura.moveFix.getBoolean()) ? Speed.mc.thePlayer.rotationYaw : Augustus.getInstance().getYawPitchHelper().realYaw));
        }
    }
    
    private boolean isMoving() {
        return Speed.mc.thePlayer.moveForward != 0.0f || (Speed.mc.thePlayer.moveStrafing != 0.0f && !Speed.mc.thePlayer.isCollidedHorizontally);
    }
    
    private boolean canJump() {
        return this.isMoving() && Speed.mc.thePlayer.onGround;
    }
}
