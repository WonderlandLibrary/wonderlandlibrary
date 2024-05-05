package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;

@Mod(displayName = "TNTBlock")
public class TNTBlock extends Module
{
    public static boolean isntblocking;
    public static Minecraft mc;
    
    static {
        TNTBlock.isntblocking = true;
        TNTBlock.mc = Minecraft.getMinecraft();
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {;
        if (this.checkForExplodingTNTinRange()) {
            if (TNTBlock.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                TNTBlock.isntblocking = false;
                TNTBlock.mc.thePlayer.getCurrentEquippedItem().useItemRightClick(TNTBlock.mc.theWorld, TNTBlock.mc.thePlayer);
            }
        }
        else if (!this.checkForExplodingTNTinRange()) {
            TNTBlock.isntblocking = true;
        }
    }
    
    public boolean checkForExplodingTNTinRange() {
        for (final Object e : TNTBlock.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityTNTPrimed) {
                final EntityTNTPrimed tnt = (EntityTNTPrimed)e;
                if (TNTBlock.mc.thePlayer.getDistanceToEntity(tnt) <= 6.0) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
}
