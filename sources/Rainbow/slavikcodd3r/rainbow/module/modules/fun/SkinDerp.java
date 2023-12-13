package slavikcodd3r.rainbow.module.modules.fun;

import net.minecraft.entity.player.EnumPlayerModelParts;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;

import java.util.Random;

@Module.Mod (displayName = "SkinDerp")
public class SkinDerp extends Module
{
    Random rand;
    
    public SkinDerp() {
        this.rand = new Random();
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        final EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1;
        final EnumPlayerModelParts[] parts = arrayOfEnumPlayerModelParts1 = EnumPlayerModelParts.values();
        for (int j = parts.length, i = 0; i < j; ++i) {
            final EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
            ClientUtils.gamesettings().func_178878_a(part, this.rand.nextBoolean());
        }
    }
}
