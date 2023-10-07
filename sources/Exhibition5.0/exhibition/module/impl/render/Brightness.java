// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Brightness extends Module
{
    public Brightness(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        Brightness.mc.playerController.blockHitDelay = 0;
        final boolean item = Brightness.mc.thePlayer.getCurrentEquippedItem() == null;
        Brightness.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Brightness.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
    }
}
