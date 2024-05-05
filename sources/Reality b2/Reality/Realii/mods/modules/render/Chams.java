
package Reality.Realii.mods.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventPostRenderPlayer;
import Reality.Realii.event.events.rendering.EventPreRenderPlayer;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import net.minecraft.client.renderer.GlStateManager;


public class Chams
        extends Module {
    public Mode mode = new Mode("Mode", "mode", new String[]{"Normal", "Textured"}, "Textured");

    public Chams() {
        super("Chams", ModuleType.Render);
        this.addValues(this.mode);
    }

    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e) {
    	Render2.color(0x800080);
        GL11.glEnable((int) 32823);
        GL11.glPolygonOffset((float) 1.0f, (float) -1100000.0f);
    }



}

