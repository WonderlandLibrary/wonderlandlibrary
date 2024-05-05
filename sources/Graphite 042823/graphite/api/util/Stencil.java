package graphite.api.util;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author Zeb.
 * @since 6/19/2017.
 */
public class Stencil {
    public static void write() {
        GL11.glClearStencil(0);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFFFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GlStateManager.colorMask(false, false, false, false);
    }

    public static void erase() {
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFFFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GlStateManager.colorMask(true, true, true, true);
    }

    public static void dispose() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }
}