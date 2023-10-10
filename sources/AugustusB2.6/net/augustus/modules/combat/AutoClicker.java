// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.augustus.utils.RandomUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class AutoClicker extends Module
{
    public final DoubleValue minDelay;
    public final DoubleValue maxDelay;
    public final BooleanValue smart;
    public final BooleanValue onlyOnTarget;
    private final TimeHelper timeHelper;
    private long randomDelay;
    
    public AutoClicker() {
        super("AutoClicker", new Color(82, 186, 74), Categorys.COMBAT);
        this.minDelay = new DoubleValue(1, "MinDelay", this, 50.0, 0.0, 400.0, 0);
        this.maxDelay = new DoubleValue(5, "MaxDelay", this, 110.0, 0.0, 400.0, 0);
        this.smart = new BooleanValue(20, "Smart", this, false);
        this.onlyOnTarget = new BooleanValue(2, "OnlyOnTarget", this, false);
        this.timeHelper = new TimeHelper();
        this.randomDelay = 100L;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.randomDelay = 0L;
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (Mouse.isButtonDown(0) && AutoClicker.mc.currentScreen == null) {
            if (AutoClicker.mc.thePlayer.isUsingItem()) {
                if (!AutoClicker.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    AutoClicker.mc.playerController.onStoppedUsingItem(AutoClicker.mc.thePlayer);
                }
                while (AutoClicker.mc.gameSettings.keyBindAttack.isPressed()) {}
                while (AutoClicker.mc.gameSettings.keyBindUseItem.isPressed()) {}
                while (AutoClicker.mc.gameSettings.keyBindPickBlock.isPressed()) {}
            }
            else {
                if (this.attack()) {
                    if (this.onlyOnTarget.getBoolean()) {
                        if (AutoClicker.mc.objectMouseOver != null && AutoClicker.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                            AutoClicker.mc.clickMouse();
                            this.timeHelper.reset();
                            this.setRandomDelay();
                        }
                    }
                    else {
                        AutoClicker.mc.clickMouse();
                        this.timeHelper.reset();
                        this.setRandomDelay();
                    }
                }
                while (AutoClicker.mc.gameSettings.keyBindUseItem.isPressed()) {
                    AutoClicker.mc.rightClickMouse();
                }
            }
            if (AutoClicker.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoClicker.mc.getRightClickDelayTimer() == 0 && !AutoClicker.mc.thePlayer.isUsingItem()) {
                AutoClicker.mc.rightClickMouse();
            }
            final Minecraft mc = AutoClicker.mc;
            if (AutoClicker.mc.currentScreen == null) {}
            mc.sendClickBlockToController(false);
            eventClick.setCanceled(true);
        }
    }
    
    private boolean attack() {
        if (this.smart.getBoolean() && AutoClicker.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && AutoClicker.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase)AutoClicker.mc.objectMouseOver.entityHit;
            if (entity.hurtTime == 0 || entity.hurtTime == 1) {
                return true;
            }
        }
        return this.timeHelper.reached(this.randomDelay);
    }
    
    private void setRandomDelay() {
        this.randomDelay = (long)RandomUtil.nextSecureInt((int)this.minDelay.getValue(), (int)this.maxDelay.getValue());
    }
}
