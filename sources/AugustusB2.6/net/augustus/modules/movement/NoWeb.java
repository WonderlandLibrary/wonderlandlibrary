// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.minecraft.util.MathHelper;
import net.augustus.events.EventSilentMove;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class NoWeb extends Module
{
    public final StringValue mode;
    private int counter;
    private boolean wasInWeb;
    
    public NoWeb() {
        super("NoWeb", new Color(179, 252, 255), Categorys.MOVEMENT);
        this.mode = new StringValue(1, "Mode", this, "Intave", new String[] { "Intave", "Ignore" });
        this.counter = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (NoWeb.mc.thePlayer != null) {
            NoWeb.mc.thePlayer.jumpMovementFactor = 0.02f;
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (NoWeb.mc.thePlayer != null) {
            NoWeb.mc.thePlayer.jumpMovementFactor = 0.02f;
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        if (NoWeb.mc.thePlayer.isInWeb()) {
            NoWeb.mc.thePlayer.onGround = false;
            final String selected2;
            final String selected = selected2 = this.mode.getSelected();
            switch (selected2) {
                case "Intave": {
                    if (!NoWeb.mc.thePlayer.isCollidedVertically) {
                        NoWeb.mc.thePlayer.jumpMovementFactor = 0.8f;
                        break;
                    }
                    if (NoWeb.mc.thePlayer.movementInput.moveStrafe == 0.0f && NoWeb.mc.gameSettings.keyBindForward.isKeyDown() && NoWeb.mc.thePlayer.isCollidedVertically) {
                        NoWeb.mc.thePlayer.jumpMovementFactor = 0.74f;
                        break;
                    }
                    NoWeb.mc.thePlayer.jumpMovementFactor = 0.2f;
                    NoWeb.mc.thePlayer.onGround = true;
                    break;
                }
            }
            this.wasInWeb = NoWeb.mc.thePlayer.isInWeb();
        }
        else if (NoWeb.mc.thePlayer.jumpMovementFactor > 0.03 && this.wasInWeb && !NoWeb.mc.thePlayer.isInWeb()) {
            this.wasInWeb = NoWeb.mc.thePlayer.isInWeb();
            NoWeb.mc.thePlayer.jumpMovementFactor = 0.02f;
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        final String selected2;
        final String selected = selected2 = this.mode.getSelected();
        switch (selected2) {
            case "Intave": {
                if (NoWeb.mc.thePlayer.isCollidedVertically) {
                    if (NoWeb.mc.thePlayer.isInWeb() && NoWeb.mc.thePlayer.movementInput.moveStrafe == 0.0f && NoWeb.mc.gameSettings.keyBindForward.isKeyDown()) {
                        NoWeb.mc.thePlayer.movementInput.moveForward = ((this.counter % 5 == 0) ? 0.0f : 1.0f);
                        ++this.counter;
                        break;
                    }
                    break;
                }
                else {
                    if (NoWeb.mc.thePlayer.isInWeb()) {
                        if (NoWeb.mc.thePlayer.isSprinting()) {
                            NoWeb.mc.thePlayer.movementInput.moveForward = 0.0f;
                        }
                        NoWeb.mc.thePlayer.movementInput.sneak = true;
                        NoWeb.mc.thePlayer.movementInput.moveForward = (float)MathHelper.clamp_double(NoWeb.mc.thePlayer.movementInput.moveForward, -0.3, 0.3);
                        NoWeb.mc.thePlayer.movementInput.moveStrafe = (float)MathHelper.clamp_double((NoWeb.mc.thePlayer.movementInput.moveForward == 0.0f) ? ((double)NoWeb.mc.thePlayer.movementInput.moveStrafe) : 0.0, -0.3, 0.3);
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
}
