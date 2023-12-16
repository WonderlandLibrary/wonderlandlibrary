// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.event.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import moonsense.config.ModuleConfig;
import moonsense.event.impl.SCMouseEvent;
import moonsense.enums.ModuleCategory;
import moonsense.features.SCModule;

public class ProjectLModule extends SCModule
{
    public ProjectLModule() {
        super("Head Rotation Fix", "Fixes a 1.8.~ player head rendering bug. Known as ProjectL.");
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
    
    @SubscribeEvent
    public void onMouse(final SCMouseEvent event) {
        if (ModuleConfig.INSTANCE.isEnabled(this) && (event.dx != 0 || event.dy != 0)) {
            final EntityPlayerSP player = this.mc.thePlayer;
            if (player != null) {
                player.prevRenderYawOffset = player.renderYawOffset;
                player.prevRotationYawHead = player.rotationYawHead;
                player.prevRotationYaw = player.rotationYaw;
                player.prevRotationPitch = player.rotationPitch;
            }
        }
    }
    
    @Override
    public String getAuthor() {
        return "OrangeMarshall";
    }
}
