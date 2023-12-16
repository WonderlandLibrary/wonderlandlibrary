// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import com.darkmagician6.eventapi.events.impl.EventRenderDummy;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import com.darkmagician6.eventapi.events.impl.EventAttack;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.impl.EventRender;
import java.text.DecimalFormat;
import fluid.client.mods.GuiMod;

public class Reach extends GuiMod
{
    public String lastReach;
    public long lastReached;
    public DecimalFormat format;
    
    public Reach() {
        super("Reach", "Shows your reach of the last hit");
        this.lastReach = "No target";
        this.lastReached = 0L;
        this.format = new DecimalFormat("#.##");
        this.x = 50;
        this.drag.setxPosition(this.x);
        this.y = 50;
        this.drag.setyPosition(this.y);
        this.height = this.fr.FONT_HEIGHT;
        this.drag.setHeight(this.height);
        this.width = this.fr.getStringWidth("§8[§6FPS§8] §f999");
        this.drag.setWidth(this.width);
    }
    
    @EventTarget
    public void onRender(final EventRender e) {
        if (this.lastReached + 5000L < System.currentTimeMillis()) {
            this.lastReach = "No target";
        }
        this.fr.drawStringWithShadow(this.lastReach, (float)this.getX(), (float)this.getY(), -1);
    }
    
    @EventTarget
    public void onAttack(final EventAttack e) {
        if (e.entity.hurtResistantTime != 10) {
            return;
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final Vec3 vec3 = entity.getPositionEyes(0.0f);
        final double reach = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
        this.lastReach = String.valueOf(this.format.format(reach)) + " blocks";
        this.lastReached = System.currentTimeMillis();
    }
    
    @EventTarget
    public void onDummy(final EventRenderDummy e) {
        this.fr.drawStringWithShadow("0.69 blocks", (float)this.getX(), (float)this.getY(), -1);
        this.drag.draw(e.x, e.y);
    }
}
