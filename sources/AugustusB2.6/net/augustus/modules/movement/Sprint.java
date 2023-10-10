// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventSilentMove;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.settings.KeyBinding;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Sprint extends Module
{
    public BooleanValue allDirection;
    public boolean allSprint;
    
    public Sprint() {
        super("Sprint", new Color(170, 181, 182, 255), Categorys.MOVEMENT);
        this.allDirection = new BooleanValue(1, "AllDirection", this, false);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        KeyBinding.setKeyBindState(Sprint.mc.gameSettings.keyBindSprint.getKeyCode(), !Sprint.mm.scaffoldWalk.isToggled() && !Sprint.mm.newScaffold.isToggled() && (!Sprint.mm.blockFly.isToggled() || Sprint.mm.blockFly.sprint.getBoolean()));
        if (this.allDirection.getBoolean() && !Sprint.mc.thePlayer.isUsingItem() && Sprint.mc.thePlayer.movementInput.moveForward != 0.0f && !Sprint.mc.thePlayer.isCollidedHorizontally) {
            if (!Sprint.mm.blockFly.isToggled() || Sprint.mm.blockFly.sprint.getBoolean()) {
                this.allSprint = true;
            }
        }
        else {
            this.allSprint = false;
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventSilentMove eventSilentMove) {
        if (Sprint.mc.thePlayer.isSprinting() && Sprint.mm.blockFly.isToggled() && !Sprint.mm.blockFly.sprint.getBoolean()) {
            Sprint.mc.thePlayer.movementInput.moveForward = 0.0f;
        }
    }
}
