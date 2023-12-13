package info.sigmaclient.module.impl.render;

import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventRenderEntity;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.render.Stencil;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.MCStencil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Outline extends Module {

    private String TEAM = "TEAM";
    private boolean draw = false;
    private int draws = 0;

    public Outline(ModuleData data) {
        super(data);
        settings.put(TEAM, new Setting<>(TEAM, false, "Team colors."));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class, EventRenderEntity.class})
    public void onEvent(info.sigmaclient.event.Event event) {
        if (event instanceof EventRender3D) {
            if (!draw || draws < 10) return;

            for (Object obj : mc.theWorld.loadedEntityList) {
                Entity entity = (Entity) obj;

                if (entity != mc.thePlayer && entity instanceof EntityPlayer) {
                    GL11.glPushMatrix();

                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;

                    GL11.glTranslated(posX - RenderManager.renderPosX, (posY - Math.pow(10, 5)) - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                    Render entityRender = mc.getRenderManager().getEntityRenderObject(entity);
                    if (entityRender != null) {
                        entityRender.doRender(entity, 0.0, 0.0, 0.0, mc.timer.renderPartialTicks, mc.timer.renderPartialTicks);
                    }
                    GL11.glPopMatrix();
                }
            }

            MCStencil.checkSetupFBO();
            int list = GL11.glGenLists(1);
            Stencil.getInstance().startLayer();
            GL11.glPushMatrix();
            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            Stencil.getInstance().setBuffer(true);
            GL11.glNewList(list, GL11.GL_COMPILE);
            GlStateManager.enableLighting();
            for (Object obj : mc.theWorld.loadedEntityList) {
                Entity entity = (Entity) obj;

                if (entity != mc.thePlayer && entity instanceof EntityPlayer) {
                    float health = ((EntityPlayer) entity).getHealth();
                    if (health > 20) {
                        health = 20;
                    }
                    float[] fractions = new float[]{0f, 0.5f, 1f};
                    Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                    float progress = (health * 5) * 0.01f;
                    Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
                    if (((Boolean) settings.get(TEAM).getValue()) && !((EntityPlayer) entity).isMurderer) {
                        String text = entity.getDisplayName().getFormattedText();
                        for (int i = 0; i < text.length(); i++)
                            if ((text.charAt(i) == (char) 0x00A7) && (i + 1 < text.length())) {
                                char oneMore = Character.toLowerCase(text.charAt(i + 1));
                                int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                if (colorCode < 16) {
                                    try {
                                        int newColor = mc.fontRendererObj.colorCode[colorCode];
                                        GlStateManager.color((newColor >> 16) / 255.0F,
                                                (newColor >> 8 & 0xFF) / 255.0F,
                                                (newColor & 0xFF) / 255.0F, 255);
                                    } catch (ArrayIndexOutOfBoundsException ignored) {

                                    }
                                }
                            }
                    } else {
                        if (((EntityPlayer) entity).isMurderer) {
                            RenderingUtil.glColor(Colors.getColor(189, 44, 221));
                        } else {
                            RenderingUtil.glColor(customColor.getRGB());
                        }
                    }

                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glLineWidth(3.5f);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);

                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;

                    GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                    Render entityRender = mc.getRenderManager().getEntityRenderObject(entity);
                    if (entityRender != null) {
                        float distance = mc.thePlayer.getDistanceToEntity(entity);
                        if (entity instanceof EntityLivingBase) {
                            GlStateManager.disableLighting();
                            RendererLivingEntity.renderLayers = false;
                            entityRender.doRender(entity, 0.0, 0.0, 0.0, mc.timer.renderPartialTicks, mc.timer.renderPartialTicks);
                            RendererLivingEntity.renderLayers = true;
                            GlStateManager.enableLighting();

                        }
                    }
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glPopMatrix();
                }
            }
            GL11.glEndList();
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glCallList(list);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
            GL11.glCallList(list);
            Stencil.getInstance().setBuffer(false);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glCallList(list);
            Stencil.getInstance().cropInside();
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glCallList(list);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
            GL11.glCallList(list);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            Stencil.getInstance().stopLayer();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDeleteLists(list, 1);
            GL11.glPopMatrix();
        } else if (event instanceof EventRenderEntity) {
            EventRenderEntity err = (EventRenderEntity) event;
            if (err.getEntity() != mc.thePlayer && err.getEntity() instanceof EntityPlayer) {
                draw = true;
                draws++;
            }
        }
    }

}
