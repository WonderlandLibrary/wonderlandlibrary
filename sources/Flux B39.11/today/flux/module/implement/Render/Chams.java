package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.event.EventRendererLivingEntity;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;

import today.flux.module.value.ColorValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

import java.awt.*;

public class Chams extends Module {

    public static ColorValue chamsColours = new ColorValue("Chams", "Chams", Color.RED);

    public static ModeValue mode = new ModeValue("Chams", "Mode", "Normal", "Normal", "Colored");
    public static BooleanValue flat = new BooleanValue("Chams", "Flat", false);
    public static BooleanValue teamCol = new BooleanValue("Chams", "TeamColor", false);

    public static FloatValue alpha = new FloatValue("Chams", "Alpha", 170, 0, 255, 1);

    public Chams() {
        super("Chams", Category.Render, false);
    }

    @EventTarget
    public void onRenderLivingEntity(EventRendererLivingEntity evt) {
        if (evt.getEntity() != mc.thePlayer) {
            if (evt.isPre()) {
                if (mode.isCurrentMode("Colored")) {
                    evt.setCancelled(true);
                    try {
                        Render renderObject = mc.getRenderManager().getEntityRenderObject(evt.getEntity());
                        if (renderObject != null && mc.getRenderManager().renderEngine != null && renderObject instanceof RendererLivingEntity) {
                            GL11.glPushMatrix();
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glBlendFunc(770, 771);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_BLEND);
                            Color teamColor = null;

                            if (flat.getValueState()) {
                                GlStateManager.disableLighting();
                            }

                            if (teamCol.getValueState()) {
                                String text = evt.getEntity().getDisplayName().getFormattedText();
                                for (int i = 0; i < text.length(); i++) {
                                    if ((text.charAt(i) == (char) 0x00A7) && (i + 1 < text.length())) {
                                        char oneMore = Character.toLowerCase(text.charAt(i + 1));
                                        int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                        if (colorCode < 16) {
                                            try {
                                                Color newCol = teamColor = new Color(mc.fontRendererObj.colorCode[colorCode]);
                                                GL11.glColor4f(newCol.getRed() / 255f, newCol.getGreen() / 255f, newCol.getBlue() / 255f, alpha.getValueState() / 255f);
                                            } catch (ArrayIndexOutOfBoundsException exception) {
                                                GL11.glColor4f(1, 0, 0, alpha.getValueState() / 255f);
                                            }
                                        }
                                    }
                                }
                            } else {
                                Color c = chamsColours.getColor();
                                GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, alpha.getValueState() / 255f);
                            }

                            ((RendererLivingEntity) renderObject).renderModel(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glEnable(GL11.GL_DEPTH_TEST);

                            if (teamCol.getValueState() && teamColor != null) {
                                GL11.glColor4f(teamColor.getRed() / 255f, teamColor.getGreen() / 255f, teamColor.getBlue() / 255f, alpha.getValueState() / 255f);
                            } else {
                                Color c = chamsColours.getColor();
                                GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, alpha.getValueState() / 255f);
                            }

                            ((RendererLivingEntity) renderObject).renderModel(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha.getValueState() / 255f);

                            if (flat.getValueState()) {
                                GlStateManager.enableLighting();
                            }

                            GL11.glPopMatrix();
                            ((RendererLivingEntity) renderObject).renderLayers(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), mc.timer.renderPartialTicks, evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glPopMatrix();
                        }
                    } catch (Exception ex) {
                    }
                } else {
                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(1.0F, -1100000.0F);
                }
            } else if (!mode.isCurrentMode("Colored") && evt.isPost()) {
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1.0F, 1100000.0F);
            }
        }
    }
}
