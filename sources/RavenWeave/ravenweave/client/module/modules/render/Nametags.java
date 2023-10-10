package ravenweave.client.module.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ravenweave.client.event.impl.RenderLabelEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.world.AntiBot;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class Nametags extends Module {
    public static SliderSetting offset;
    public static TickSetting rect, shadow, showHealth, showInvis;

    public Nametags() {
        super("Nametags", ModuleCategory.render);
        this.registerSetting(offset = new SliderSetting("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
        this.registerSetting(rect = new TickSetting("Rect", true));
        this.registerSetting(shadow = new TickSetting("Shadow", false));
        this.registerSetting(showHealth = new TickSetting("Show health", true));
        this.registerSetting(showInvis = new TickSetting("Show invis", true));
    }

    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        if (event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.thePlayer && event.getEntity().deathTime == 0) {
            EntityPlayer en = (EntityPlayer) event.getEntity();
            if (!showInvis.isToggled() && en.isInvisible()) {
                return;
            }

            if (AntiBot.bot(en) || en.getDisplayName().getUnformattedText().isEmpty()) return;

            String str = en.getDisplayName().getFormattedText();
            if (showHealth.isToggled()) {
                double r = en.getHealth() / en.getMaxHealth();
                String h = (r < 0.3D ? "\u00A7c" : (r < 0.5D ? "\u00A76" : (r < 0.7D ? "\u00A7e" : "\u00A7a")))
                        + Utils.Java.round(en.getHealth(), 1);
                str = str + " " + h;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float) event.getX() + 0.0F, (float) event.getY() + en.height + 0.5F, (float) event.getZ());
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            float f1 = 0.02666667F;
            GlStateManager.scale(-f1, -f1, f1);
            if (en.isSneaking()) {
                GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            }

            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = (int) (-offset.getInput());
            int j = mc.fontRendererObj.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            if (rect.isToggled()) {
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                tessellator.draw();
            }

            GlStateManager.enableTexture2D();
            if (shadow.isToggled()) {
                mc.fontRendererObj.drawStringWithShadow(str, (float) -mc.fontRendererObj.getStringWidth(str) / 2, i, -1); // IntelliJ wanted me to cast it to Float for some reason ¯\_(ツ)_/¯
            } else {
                mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, i, -1);
            }
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onRenderLabel(RenderLabelEvent e) {
        if(e.getTarget() instanceof EntityPlayer) {
            e.setCancelled(true);
        }
    }

}
