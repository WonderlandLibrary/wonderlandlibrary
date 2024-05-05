package com.initial.modules.impl.visual;

import com.initial.modules.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import java.util.*;
import com.initial.events.*;

public class NoRender extends Module
{
    public NoRender() {
        super("No Render", 0, Category.VISUAL);
    }
    
    @EventTarget
    @Override
    public void onUpdate() {
        this.setDisplayName("No Render");
        if (!this.isToggled()) {
            return;
        }
        for (final Object o : this.mc.theWorld.loadedEntityList) {
            if (o instanceof EntityItem) {
                final EntityItem i = (EntityItem)o;
                this.mc.theWorld.removeEntity(i);
            }
        }
    }
}
