// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class SpeedMine extends Module
{
    public SpeedMine(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        SpeedMine.mc.playerController.blockHitDelay = 0;
        final boolean item = SpeedMine.mc.thePlayer.getCurrentEquippedItem() == null;
        SpeedMine.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, (int)(item ? 1 : 0)));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        SpeedMine.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }
}
