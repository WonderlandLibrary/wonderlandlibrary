package slavikcodd3r.rainbow.module.modules.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.RenderUtils2;

@Module.Mod(displayName = "ItemESP")
public class ItemESP extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final Render3DEvent event) {
        for (final Object e : this.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityItem) {
                final EntityItem item = (EntityItem)e;
                RenderUtils2.drawItemEsp(item, new Color(0, 0, 0, 21).getRGB());
            }
        }
	}
}
