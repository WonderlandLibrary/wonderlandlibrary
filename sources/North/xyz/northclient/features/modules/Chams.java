package xyz.northclient.features.modules;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import xyz.northclient.UIHook;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.EntityRenderEvent;
import xyz.northclient.features.events.RenderModelEvent;
import xyz.northclient.util.shader.RenderUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "Chams",description = "", category = Category.RENDER)
public class Chams extends AbstractModule {
    @EventLink
    public void onRender(RenderModelEvent e) {
        if(!isValidEntity(e.getEntity()))
            return;

        if(e.isPre()) {
            Color behind = UIHook.north.getUiHook().getTheme().getMainColor();

            glPushMatrix();
            glEnable(GL_POLYGON_OFFSET_LINE);
            glPolygonOffset(1.0F, 1000000.0F);

            glDisable(GL_LIGHTING);

            glDisable(GL_TEXTURE_2D);
            RenderUtil.color(behind.getRGB());

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
        } else {
            glEnable(GL_DEPTH_TEST);
            glDepthMask(true);

            Color visible = UIHook.north.getUiHook().getTheme().getSecondColor();

            glDisable(GL_LIGHTING);
            RenderUtil.color(visible.getRGB());

            e.drawModel();


            glEnable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            glDisable(GL_BLEND);
            glEnable(GL_LIGHTING);

            glPolygonOffset(1.0f, -1000000.0f);
            glDisable(GL_POLYGON_OFFSET_LINE);
            glPopMatrix();
        }
    }

    private boolean isValidEntity(Entity entity) {
        return entity instanceof EntityPlayer || entity instanceof EntityAnimal || entity instanceof EntityMob;
    }
}
