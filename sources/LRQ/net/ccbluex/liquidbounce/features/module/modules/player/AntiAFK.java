/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="AntiAFK", description="Prevents you from getting kicked for being AFK.", category=ModuleCategory.PLAYER)
public final class AntiAFK
extends Module {
    private final MSTimer swingDelayTimer = new MSTimer();
    private final MSTimer delayTimer = new MSTimer();
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Old", "Random", "Custom"}, "Random");
    private final IntegerValue swingDelayValue = new IntegerValue("SwingDelay", 100, 0, 1000);
    private final IntegerValue rotationDelayValue = new IntegerValue("RotationDelay", 100, 0, 1000);
    private final FloatValue rotationAngleValue = new FloatValue("RotationAngle", 1.0f, -180.0f, 180.0f);
    private final BoolValue jumpValue = new BoolValue("Jump", true);
    private final BoolValue moveValue = new BoolValue("Move", true);
    private final BoolValue rotateValue = new BoolValue("Rotate", true);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private boolean shouldMove;
    private long randomTimerDelay = 500L;

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "old": {
                MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                if (!this.delayTimer.hasTimePassed(500L)) break;
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setRotationYaw(iEntityPlayerSP2.getRotationYaw() + 180.0f);
                this.delayTimer.reset();
                break;
            }
            case "random": {
                IKeyBinding iKeyBinding = this.getRandomMoveKeyBind();
                if (iKeyBinding == null) {
                    Intrinsics.throwNpe();
                }
                iKeyBinding.setPressed(this.shouldMove);
                if (!this.delayTimer.hasTimePassed(this.randomTimerDelay)) {
                    return;
                }
                this.shouldMove = false;
                this.randomTimerDelay = 500L;
                switch (RandomUtils.nextInt(0, 6)) {
                    case 0: {
                        if (thePlayer.getOnGround()) {
                            thePlayer.jump();
                        }
                        this.delayTimer.reset();
                        break;
                    }
                    case 1: {
                        if (!thePlayer.isSwingInProgress()) {
                            thePlayer.swingItem();
                        }
                        this.delayTimer.reset();
                        break;
                    }
                    case 2: {
                        this.randomTimerDelay = RandomUtils.nextInt(0, 1000);
                        this.shouldMove = true;
                        this.delayTimer.reset();
                        break;
                    }
                    case 3: {
                        thePlayer.getInventory().setCurrentItem(RandomUtils.nextInt(0, 9));
                        MinecraftInstance.mc.getPlayerController().updateController();
                        this.delayTimer.reset();
                        break;
                    }
                    case 4: {
                        IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                        iEntityPlayerSP3.setRotationYaw(iEntityPlayerSP3.getRotationYaw() + RandomUtils.INSTANCE.nextFloat(-180.0f, 180.0f));
                        this.delayTimer.reset();
                        break;
                    }
                    case 5: {
                        if (thePlayer.getRotationPitch() <= (float)-90 || thePlayer.getRotationPitch() >= (float)90) {
                            thePlayer.setRotationPitch(0.0f);
                        }
                        IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                        iEntityPlayerSP4.setRotationPitch(iEntityPlayerSP4.getRotationPitch() + RandomUtils.INSTANCE.nextFloat(-10.0f, 10.0f));
                        this.delayTimer.reset();
                    }
                }
                break;
            }
            case "custom": {
                if (((Boolean)this.moveValue.get()).booleanValue()) {
                    MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(true);
                }
                if (((Boolean)this.jumpValue.get()).booleanValue() && thePlayer.getOnGround()) {
                    thePlayer.jump();
                }
                if (((Boolean)this.rotateValue.get()).booleanValue() && this.delayTimer.hasTimePassed(((Number)this.rotationDelayValue.get()).intValue())) {
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setRotationYaw(iEntityPlayerSP5.getRotationYaw() + ((Number)this.rotationAngleValue.get()).floatValue());
                    if (thePlayer.getRotationPitch() <= (float)-90 || thePlayer.getRotationPitch() >= (float)90) {
                        thePlayer.setRotationPitch(0.0f);
                    }
                    IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                    iEntityPlayerSP6.setRotationPitch(iEntityPlayerSP6.getRotationPitch() + (RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) * (float)2 - 1.0f));
                    this.delayTimer.reset();
                }
                if (!((Boolean)this.swingValue.get()).booleanValue() || thePlayer.isSwingInProgress() || !this.swingDelayTimer.hasTimePassed(((Number)this.swingDelayValue.get()).intValue())) break;
                thePlayer.swingItem();
                this.swingDelayTimer.reset();
            }
        }
    }

    private final IKeyBinding getRandomMoveKeyBind() {
        switch (RandomUtils.nextInt(0, 4)) {
            case 0: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindRight();
            }
            case 1: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindLeft();
            }
            case 2: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindBack();
            }
            case 3: {
                return MinecraftInstance.mc.getGameSettings().getKeyBindForward();
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindForward())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(false);
        }
    }
}

