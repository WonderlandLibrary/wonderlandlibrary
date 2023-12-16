// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import net.minecraft.potion.Potion;
import com.darkmagician6.eventapi.events.impl.EventWalking;
import com.darkmagician6.eventapi.events.impl.EventRenderDummy;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.impl.EventRender;
import fluid.client.mods.GuiMod;

public class ToggleSprint extends GuiMod
{
    public boolean toggled;
    
    public ToggleSprint() {
        super("ToggleSprint", "Makes your fingers wound-free if you are a no-life");
        this.toggled = false;
        this.x = 50;
        this.drag.setxPosition(this.x);
        this.y = 50;
        this.drag.setyPosition(this.y);
        this.height = this.fr.FONT_HEIGHT;
        this.drag.setHeight(this.height);
        this.width = this.fr.getStringWidth("[Sprinting (Toggled)]");
        this.drag.setWidth(this.width);
    }
    
    @EventTarget
    public void onRender(final EventRender e) {
        if (!this.toggled && this.mc.thePlayer.isSprinting()) {
            this.fr.drawStringWithShadow("[Sprinting (Vanilla)]", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (this.toggled) {
            this.fr.drawStringWithShadow("[Sprinting (Toggled)]", (float)this.getX(), (float)this.getY(), -1);
        }
    }
    
    @EventTarget
    public void onDummy(final EventRenderDummy e) {
        this.fr.drawStringWithShadow("[Sprinting (Toggled)]", (float)this.getX(), (float)this.getY(), -1);
        this.drag.draw(e.x, e.y);
    }
    
    @EventTarget
    public void onWalking(final EventWalking e) {
        if (this.mc.gameSettings.keyBindSprint.isPressed()) {
            this.toggled = !this.toggled;
            System.out.println(this.toggled);
        }
        if (!this.toggled) {
            return;
        }
        final float f = 0.8f;
        final boolean flag3 = this.mc.thePlayer.getFoodStats().getFoodLevel() > 6.0f || this.mc.thePlayer.capabilities.allowFlying;
        if (this.mc.thePlayer.movementInput.moveForward >= f && flag3 && !this.mc.thePlayer.isUsingItem() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && !this.mc.thePlayer.isCollidedHorizontally) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}
