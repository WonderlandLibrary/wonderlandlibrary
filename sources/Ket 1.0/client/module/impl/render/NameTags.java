package client.module.impl.render;

import client.Client;
import client.bot.BotManager;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render3DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.ModuleManager;
import client.module.impl.other.AntiBot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name = "Name Tags", description = "Renders a custom name tag above entities", category = Category.RENDER)
public class NameTags extends Module {
    @EventLink
    public final Listener<Render3DEvent> onRender3D = event -> {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.theWorld.loadedEntityList.stream().filter(entity -> {
            final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
            final BotManager botManager = Client.INSTANCE.getBotManager();
            return entity != mc.thePlayer && entity instanceof EntityPlayer && entity.isEntityAlive() && (!moduleManager.get(AntiBot.class).isEnabled() || !botManager.contains(entity));
        }).forEach(entity -> {
            final RenderManager renderManager = mc.getRenderManager();
            final EntityPlayer player = (EntityPlayer) entity;

            GL11.glPushMatrix();

            GL11.glTranslated(
                    entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - renderManager.renderPosX,
                    entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - renderManager.renderPosY + entity.height + 0.875,
                    entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - renderManager.renderPosZ
            );

            GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
            GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);

            final float scale = Math.max(mc.thePlayer.getDistanceToEntity(entity), 1) / 4 / 150 * 3 / 2;

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            final String text = (entity.isInvisible() ? "§6" : entity.isSneaking() ? "§4" : "§7") + entity.getDisplayName().getUnformattedText() + " §c" + (int) player.getHealth() + " HP";

            GL11.glScalef(-scale, -scale, scale);

            final int width = mc.fontRendererObj.getStringWidth(text) / 2;
            mc.fontRendererObj.drawStringWithShadow(text, 1 - width, 1, -1);

            final int dist = width * 2 + 6;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glColor4f(0, 0, 0, 100 / 255f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(width + 4, -2);
            GL11.glVertex2i(-width - 2, -2);
            GL11.glVertex2i(-width - 2, 13);
            GL11.glVertex2i(width + 4, 13);
            GL11.glEnd();

            GL11.glColor4f(10 / 255f, 155 / 255f, 10 / 255f, 1);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(-width - 2 + dist, 12);
            GL11.glVertex2i(-width - 2, 12);
            GL11.glVertex2i(-width - 2, 13);
            GL11.glVertex2i(-width - 2 + dist, 13);
            GL11.glEnd();

            final float f = dist * Math.max(Math.min((player.getHealth() / player.getMaxHealth()), 1), 0);
            GL11.glColor4f(10 / 255f, 255 / 255f, 10 / 255f, 1);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(-width - 2 + f, 12);
            GL11.glVertex2i(-width - 2, 12);
            GL11.glVertex2i(-width - 2, 13);
            GL11.glVertex2f(-width - 2 + f, 13);
            GL11.glEnd();

            GL11.glEnable(GL11.GL_TEXTURE_2D);

            mc.fontRendererObj.drawStringWithShadow(text, 1 - width, 1, -1);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            GlStateManager.resetColor();
            GL11.glColor4f(1, 1, 1, 1);

            GL11.glPopMatrix();
        });

        GL11.glPopMatrix();
        GL11.glPopAttrib();

        GL11.glColor4f(1, 1, 1, 1);
    };
}
