// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods.impl;

import com.darkmagician6.eventapi.events.impl.EventRenderDummy;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Mouse;
import com.darkmagician6.eventapi.events.impl.EventRender;
import java.util.ArrayList;
import java.util.List;
import fluid.client.mods.GuiMod;

public class CPS extends GuiMod
{
    public List<Long> rcps;
    public List<Long> lcps;
    public boolean lPrevPressed;
    public boolean rPrevPressed;
    public long lLastPressed;
    public long rLastPressed;
    
    public CPS() {
        super("CPS", "Shows your CPS");
        this.rcps = new ArrayList<Long>();
        this.lcps = new ArrayList<Long>();
        this.lPrevPressed = false;
        this.rPrevPressed = false;
        this.lLastPressed = 0L;
        this.rLastPressed = 0L;
        this.x = 50;
        this.drag.setxPosition(this.x);
        this.y = 50;
        this.drag.setyPosition(this.y);
        this.height = this.fr.FONT_HEIGHT;
        this.drag.setHeight(this.height);
        this.width = this.fr.getStringWidth("100 | 100 CPS");
        this.drag.setWidth(this.width);
    }
    
    @EventTarget
    public void onRender(final EventRender e) {
        final boolean lpressed = Mouse.isButtonDown(0);
        final boolean rpressed = Mouse.isButtonDown(1);
        if (lpressed != this.lPrevPressed) {
            this.lLastPressed = System.currentTimeMillis();
            if (this.lPrevPressed = lpressed) {
                this.lcps.add(this.lLastPressed);
            }
        }
        if (rpressed != this.rPrevPressed) {
            this.rLastPressed = System.currentTimeMillis();
            if (this.rPrevPressed = rpressed) {
                this.rcps.add(this.rLastPressed);
            }
        }
        this.fr.drawStringWithShadow(String.valueOf(this.getLCPS()) + " | " + this.getRCPS() + " CPS", (float)this.getX(), (float)this.getY(), -1);
    }
    
    public int getRCPS() {
        final long currentTime = System.currentTimeMillis();
        this.rcps.removeIf(l -> l + 1000L < n);
        return this.rcps.size();
    }
    
    public int getLCPS() {
        final long currentTime = System.currentTimeMillis();
        this.lcps.removeIf(l -> l + 1000L < n);
        return this.lcps.size();
    }
    
    @EventTarget
    public void onDummy(final EventRenderDummy e) {
        this.fr.drawStringWithShadow("100 | 100 CPS", (float)this.getX(), (float)this.getY(), -1);
        this.drag.draw(e.x, e.y);
    }
}
