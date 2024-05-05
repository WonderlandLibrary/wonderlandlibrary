package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;
import net.minecraft.client.entity.*;
import com.initial.events.*;
import net.minecraft.client.*;
import net.minecraft.client.settings.*;

public class LongJump extends Module
{
    public ModeSet mode;
    boolean collided;
    boolean half;
    float ticks;
    float ticks2;
    public boolean back;
    protected boolean doneBow;
    double motionY;
    double aDouble2;
    int dmgStage;
    int ncpState;
    int ncpDmgState;
    int ncpBowState;
    int ncpTicks;
    Timer timer;
    
    public LongJump() {
        super("LongJump", 0, Category.MOVEMENT);
        this.mode = new ModeSet("Mode", "Redesky", new String[] { "Redesky", "BlocksMC" });
        this.ticks = 0.0f;
        this.ticks2 = 0.0f;
        this.doneBow = false;
        this.timer = new Timer();
        this.addSettings(this.mode);
        this.ncpState = 0;
        this.ncpTicks = 0;
    }
    
    @EventTarget
    @Override
    public void onEvent(final EventNigger e) {
        if (e instanceof UpdateEvent && e.isPre()) {
            this.setDisplayName("Long Jump §7" + this.mode.getMode());
            final String mode = this.mode.getMode();
            switch (mode) {
                case "BlocksMC": {
                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                        MovementUtils.strafe();
                        final EntityPlayerSP thePlayer = this.mc.thePlayer;
                        thePlayer.motionX *= 2.139999988079071;
                        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                        thePlayer2.motionY += 0.20999999977648254;
                        final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                        thePlayer3.motionZ *= 2.139999988079071;
                        this.mc.thePlayer.speedInAir = 0.1f;
                        break;
                    }
                    break;
                }
                case "Redesky": {
                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                    }
                    final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                    thePlayer4.motionX *= 0.979999988079071;
                    final EntityPlayerSP thePlayer5 = this.mc.thePlayer;
                    thePlayer5.motionY += 0.0199999997764825;
                    final EntityPlayerSP thePlayer6 = this.mc.thePlayer;
                    thePlayer6.motionZ *= 0.979999988079071;
                    this.mc.thePlayer.speedInAir = 0.08f;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.timer.reset();
        this.doneBow = false;
        this.aDouble2 = LongJump.localPlayer.posY;
        this.ncpTicks = 0;
        this.ncpDmgState = 0;
        this.ncpState = 0;
        this.ticks = 0.0f;
        this.ticks2 = 0.0f;
        this.back = false;
        this.motionY = this.mc.thePlayer.motionY;
        this.ncpBowState = 0;
        this.half = (this.mc.thePlayer.posY != (int)this.mc.thePlayer.posY);
        this.collided = this.mc.thePlayer.isCollidedHorizontally;
        this.dmgStage = 0;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.ticks = 0.0f;
        this.back = false;
        this.ticks2 = 0.0f;
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.thePlayer.speedInAir = 0.02f;
        resetCapabilities();
    }
    
    public static void resetCapabilities() {
        Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump);
        Minecraft.getMinecraft().thePlayer.stepHeight = 0.625f;
        Minecraft.getMinecraft().timer.timerSpeed = 1.0f;
        Minecraft.getMinecraft().thePlayer.isCollided = false;
        if (Minecraft.getMinecraft().thePlayer.isSpectator()) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = Minecraft.getMinecraft().playerController.isInCreativeMode();
        Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode = Minecraft.getMinecraft().playerController.isInCreativeMode();
        Minecraft.getMinecraft().thePlayer.speedInAir = 0.02f;
    }
}
