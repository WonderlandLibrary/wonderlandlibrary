/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.NumberSetting;
import net.minecraft.entity.EntityLivingBase;

public class WTap
extends Module {
    public NumberSetting distance = new NumberSetting("Distance", 3.5, 1.0, 6.0, 0.5);

    public WTap() {
        super("WTap", 0, Module.Category.GHOST);
        this.addSettings(this.distance);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            for (Object o : this.mc.theWorld.loadedEntityList) {
                EntityLivingBase entity;
                if (!(o instanceof EntityLivingBase) || (entity = (EntityLivingBase)o) == this.mc.thePlayer || !((double)this.mc.thePlayer.getDistanceToEntity(entity) <= this.distance.getValue()) || !this.mc.thePlayer.isSprinting()) continue;
                this.mc.thePlayer.setSprinting(false);
            }
        }
    }
}

