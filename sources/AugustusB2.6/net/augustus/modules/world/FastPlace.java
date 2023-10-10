// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.utils.RandomUtil;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import net.augustus.events.EventClick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class FastPlace extends Module
{
    private final TimeHelper timeHelper;
    public DoubleValue delay;
    
    public FastPlace() {
        super("FastPlace", new Color(54, 144, 217), Categorys.WORLD);
        this.timeHelper = new TimeHelper();
        this.delay = new DoubleValue(1, "Delay", this, 50.0, 0.0, 300.0, 0);
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (FastPlace.mc.currentScreen == null && Mouse.isButtonDown(1) && FastPlace.mc.objectMouseOver != null && FastPlace.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && FastPlace.mc.thePlayer.getCurrentEquippedItem() != null && FastPlace.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
            final long random = (this.delay.getValue() == 0.0) ? 0L : ((long)(this.delay.getValue() + RandomUtil.nextLong(-30L, 30L)));
            if (this.timeHelper.reached(random)) {
                FastPlace.mc.rightClickMouse();
                this.timeHelper.reset();
            }
            eventClick.setCanceled(true);
        }
    }
}
