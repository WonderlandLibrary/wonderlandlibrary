package com.initial.modules.impl.other;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.events.impl.*;
import net.minecraft.client.renderer.*;
import com.initial.events.*;

public class ScoreboardMover extends Module
{
    public DoubleSet x;
    public DoubleSet y;
    
    public ScoreboardMover() {
        super("Scoreboard", 0, Category.OTHER);
        this.x = new DoubleSet("PosX", 30.0, 0.0, 0.0, 1.0);
        this.y = new DoubleSet("PosY", 30.0, 0.0, 0.0, 1.0);
    }
    
    @EventTarget
    public void onRenderScoreboard(final EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-this.x.getValue(), this.y.getValue(), 1.0);
        }
        else {
            GlStateManager.translate(this.x.getValue(), -this.y.getValue(), 1.0);
        }
    }
}
