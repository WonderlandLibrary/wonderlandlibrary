package today.flux.utility;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import today.flux.Flux;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Misc.MurderMystery;

import java.awt.*;

public class WorldRenderUtils {
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public static int getScaledHeight() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledHeight();
    }

    public static int getScaledWidth() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledWidth();
    }

    public static void renderOne() {
        WorldRenderUtils.checkSetupFBO();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(3.0f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearStencil(0xF);
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void checkSetupFBO() {
        final Framebuffer fbo = mc.getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    public static void setupFBO(final Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.displayWidth,
                mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void renderThree() {
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderFour(EntityLivingBase base) {
        if (base != null) {
            int color = Flux.INSTANCE.getFriendManager().isFriend(base.getName()) ? -11157267 : getTeamColor(base);

            if (ModuleManager.murderMysteryMod.isEnabled()) {
                if (base instanceof EntityPlayer && MurderMystery.isMurder((EntityPlayer) base)) {
                    color = new Color(0xFF0010).getRGB();
                }
            }

            if (ModuleManager.killAuraMod.isEnabled() && KillAura.target != null
                    && base.getEntityId() == KillAura.target.getEntityId()) {
                color = new Color(0xFF00AF).getRGB();
            }

            setColor(color);
        }
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(mc);
    }

    public static void renderFour(int color) {
        setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static int getTeamColor(Entity player) {
        int var2 = 16777215;

        if (player instanceof EntityPlayer) {
            ScorePlayerTeam var6 = (ScorePlayerTeam) ((EntityPlayer) player).getTeam();

            if (var6 != null) {
                String var7 = FontRenderer.getFormatFromString(var6.getColorPrefix());

                if (var7.length() >= 2) {
                    if (!"0123456789abcdef".contains(String.valueOf(var7.charAt(1))))
                        return var2;

                    var2 = mc.fontRendererObj.getColorCode(var7.charAt(1));
                }
            }
        }

        return var2;
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0F, 2000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
    }

    public static void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);

        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);

        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    @Deprecated
    public static void drawRect(float x, float y, float x1, float y1, int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }

    @Deprecated
    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
        enableGL2D();
        GL11.glColor4f(r, g, b, a);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }

    @Deprecated
    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }


    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside,
                                               int border) {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }

    @Deprecated
    public static float getScaleFactor() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaleFactor();
    }

    @Deprecated
    public static int getDisplayWidth() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledWidth();
    }

    @Deprecated
    public static int getDisplayHeight() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledHeight();
    }

    private static void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 24;
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
            return;
        }
        if (stack.getItem() instanceof ItemArmor) {
            final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int projectileProtectionLevel = EnchantmentHelper
                    .getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
            final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId,
                    stack);
            final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId,
                    stack);
            final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (protectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pr" + protectionLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pp" + projectileProtectionLevel, x * 2, enchantmentY,
                        -1052689);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY,
                        -1052689);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("fp" + fireProtectionLevel, x * 2, enchantmentY,
                        -1052689);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("t" + thornsLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (powerLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("po" + powerLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("pu" + punchLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("f" + flameLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow("u" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("kn" + knockbackLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("f" + fireAspectLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() instanceof ItemTool) {
            final int unbreakingLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
            if (efficiencyLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (silkTouch > 0) {
                mc.fontRendererObj.drawStringWithShadow("st" + silkTouch, x * 2, enchantmentY, -1052689);
                enchantmentY += 8;
            }
            if (unbreakingLevel3 > 0) {
                mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel3, x * 2, enchantmentY, -1052689);
            }
        }
        if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1052689);
        }
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f,
                (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569f * (float) redRGB;
        float green = 0.003921569f * (float) greenRGB;
        float blue = 0.003921569f * (float) blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }

    @Deprecated
    public static void drawBorderRect(int left, int top, int right, int bottom, int bcolor, int icolor, int bwidth) {
        Gui.drawRect(left + bwidth, top + bwidth, right - bwidth, bottom - bwidth, icolor);
        Gui.drawRect(left, top + 1, left + bwidth, bottom - 1, bcolor);
        Gui.drawRect(left + bwidth, top, right, top + bwidth, bcolor);
        Gui.drawRect(left + bwidth, bottom - bwidth, right, bottom, bcolor);
        Gui.drawRect(right - bwidth, top + bwidth, right, bottom - bwidth, bcolor);
    }

    public static void drawRectZZ(double x, double y, double x1, double y1) {
        GL11.glBegin(7);
        GL11.glVertex2f((float) x, (float) y1);
        GL11.glVertex2f((float) x1, (float) y1);
        GL11.glVertex2f((float) x1, (float) y);
        GL11.glVertex2f((float) x, (float) y);
        GL11.glEnd();
    }

    public static int transparency(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * c.getRed();
        float g = 0.003921569f * c.getGreen();
        float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBlockESP(double x, double y, double z, int maincoolor, int borderColor, float lineWidth) {
        float alpha = (float) (maincoolor >> 24 & 255) / 255.0f;
        float red = (float) (maincoolor >> 16 & 255) / 255.0f;
        float green = (float) (maincoolor >> 8 & 255) / 255.0f;
        float blue = (float) (maincoolor & 255) / 255.0f;

        float lineAlpha = (float) (borderColor >> 24 & 255) / 255.0f;
        float lineRed = (float) (borderColor >> 16 & 255) / 255.0f;
        float lineGreen = (float) (borderColor >> 8 & 255) / 255.0f;
        float lineBlue = (float) (borderColor & 255) / 255.0f;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));

        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawPointESP(double x, double y, double z, int maincoolor) {
        float alpha = (float) (maincoolor >> 24 & 255) / 255.0f;
        float red = (float) (maincoolor >> 16 & 255) / 255.0f;
        float green = (float) (maincoolor >> 8 & 255) / 255.0f;
        float blue = (float) (maincoolor & 255) / 255.0f;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 0.1, y + 0.1, z + 0.1));

        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawFilledBBESP(final AxisAlignedBB axisalignedbb, final int color) {
        GL11.glPushMatrix();
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawFilledBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawFilledBox(final AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
    }

    public static void drawBoundingBoxESP(final AxisAlignedBB axisalignedbb, final float width, final int color) {
        GL11.glPushMatrix();
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha,
                                    float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawClickTPESP(double x, double y, double z, float red, float green, float blue, float alpha,
                                      float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y + 1.1, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y + 1.1, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
                                         float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBoxESP(double x, double y, double z, Color color, float size) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + size, y + size, z + size));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red,
                                             float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(3F);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void setColor(int colorHex) {
        float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
        float red = (float) (colorHex >> 16 & 255) / 255.0F;
        float green = (float) (colorHex >> 8 & 255) / 255.0F;
        float blue = (float) (colorHex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
    }

    public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red,
                                          float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
                                     float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawHat(double x, double y, double z, double width, double height, float red, float green,
                               float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    /*
     * public static void drawTracerLine(double x, double y, double z, int color,
     * float alpha, float lineWdith) { GL11.glPushMatrix(); GL11.glEnable(3042);
     * GL11.glEnable(2848); GL11.glDisable(2929); GL11.glDisable(3553);
     * GL11.glBlendFunc(770, 771); GL11.glEnable(3042); GL11.glLineWidth(lineWdith);
     * GuiRenderUtils.setColor(color); GL11.glBegin(2); GL11.glVertex3d(0,
     * mc.thePlayer.getEyeHeight(), 0); GL11.glVertex3d(x, y,
     * z); GL11.glEnd(); GL11.glDisable(3042); GL11.glEnable(3553);
     * GL11.glEnable(2929); GL11.glDisable(2848); GL11.glDisable(3042);
     * GL11.glPopMatrix(); }
     */

    public static void drawCircle(float x, float y, float radius, float lineWidth, int color) {
        WorldRenderUtils.enableGL2D();
        setColor(color);
        GL11.glLineWidth(lineWidth);
        int vertices = (int) Math.min(Math.max(radius, 45.0F), 360.0F);
        GL11.glBegin(2);

        for (int i = 0; i < vertices; ++i) {
            double angleRadians = 6.283185307179586D * (double) i / (double) vertices;
            GL11.glVertex2d((double) x + Math.sin(angleRadians) * (double) radius,
                    (double) y + Math.cos(angleRadians) * (double) radius);
        }

        GL11.glEnd();
        WorldRenderUtils.disableGL2D();
    }

    public static void drawCircle(int x, int y, double r, int c) {
        float f = (c >> 24 & 0xFF) / 255.0f;
        float f2 = (c >> 16 & 0xFF) / 255.0f;
        float f3 = (c >> 8 & 0xFF) / 255.0f;
        float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawFilledCircle(float x, float y, float radius, int color) {
        enableGL2D();
        setColor(color);
        int vertices = (int) Math.min(Math.max(radius, 45.0F), 360.0F);
        GL11.glBegin(9);

        for (int i = 0; i < vertices; ++i) {
            double angleRadians = 6.283185307179586D * (double) i / (double) vertices;
            GL11.glVertex2d((double) x + Math.sin(angleRadians) * (double) radius,
                    (double) y + Math.cos(angleRadians) * (double) radius);
        }

        GL11.glEnd();
        disableGL2D();
        drawCircle(x, y, radius, 1.5F, 16777215);
    }

    public static void drawFilledCircle(int x, int y, double r, int c) {
        float f = (c >> 24 & 0xFF) / 255.0f;
        float f2 = (c >> 16 & 0xFF) / 255.0f;
        float f3 = (c >> 8 & 0xFF) / 255.0f;
        float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void dr(double i, double j, double k, double l, int i1) {
        if (i < k) {
            double j2 = i;
            i = k;
            k = j2;
        }
        if (j < l) {
            double k2 = j;
            j = l;
            l = k2;
        }
        float f = (i1 >> 24 & 0xFF) / 255.0f;
        float f2 = (i1 >> 16 & 0xFF) / 255.0f;
        float f3 = (i1 >> 8 & 0xFF) / 255.0f;
        float f4 = (i1 & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(i, l, 0.0);
        worldRenderer.pos(k, l, 0.0);
        worldRenderer.pos(k, j, 0.0);
        worldRenderer.pos(i, j, 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor,
                                        int endColor) {
        float var7 = (float) (startColor >> 24 & 255) / 255.0F;
        float var8 = (float) (startColor >> 16 & 255) / 255.0F;
        float var9 = (float) (startColor >> 8 & 255) / 255.0F;
        float var10 = (float) (startColor & 255) / 255.0F;
        float var11 = (float) (endColor >> 24 & 255) / 255.0F;
        float var12 = (float) (endColor >> 16 & 255) / 255.0F;
        float var13 = (float) (endColor >> 8 & 255) / 255.0F;
        float var14 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer var16 = var15.getWorldRenderer();
        var16.begin(7, DefaultVertexFormats.POSITION_COLOR);
        var16.pos(right, top, 0).color(var8, var9, var10, var7).endVertex();
        var16.pos(left, top, 0).color(var8, var9, var10, var7).endVertex();
        var16.pos(left, bottom, 0).color(var12, var13, var14, var11).endVertex();
        var16.pos(right, bottom, 0).color(var12, var13, var14, var11).endVertex();
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawBorderedRectZ(float left, float top, float right, float bottom, float borderWidth,
                                         int borderColor, int color) {
        float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
        float red = (borderColor >> 16 & 0xFF) / 255.0f;
        float green = (borderColor >> 8 & 0xFF) / 255.0f;
        float blue = (borderColor & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        drawRects(left, top, right, bottom, new Color(red, green, blue, alpha));
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        if (borderWidth == 1.0F) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glLineWidth(borderWidth);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        tessellator.draw();
        GL11.glLineWidth(2.0F);

        if (borderWidth == 1.0F) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRects(double left, double top, double right, double bottom, Color color) {
        WorldRenderUtils.enableGL2D();
        WorldRenderUtils.glColor(color);
        WorldRenderUtils.drawRectZZ(left, top, right, bottom);
        WorldRenderUtils.disableGL2D();
    }

    public static void drawRects2(double left, double top, double right, double bottom, int color) {
        WorldRenderUtils.enableGL2D();
        WorldRenderUtils.glColor(color);
        WorldRenderUtils.drawRectZZ(left, top, right, bottom);
        WorldRenderUtils.disableGL2D();
    }

    public static void drawBorderedGradientRect(double left, double top, double right, double bottom, float borderWidth,
                                                int borderColor, int startColor, int endColor) {
        float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
        float red = (borderColor >> 16 & 0xFF) / 255.0f;
        float green = (borderColor >> 8 & 0xFF) / 255.0f;
        float blue = (borderColor & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        drawGradientRect(left, top, right, bottom, startColor, endColor);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        if (borderWidth == 1.0F) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glLineWidth(borderWidth);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        tessellator.draw();
        GL11.glLineWidth(2.0F);

        if (borderWidth == 1.0F) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }

    public static void boundingBox(final Entity entity, final double x, final double y, final double z, final int color,
                                   final int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth(1.0f);
        final AxisAlignedBB var11 = entity.getEntityBoundingBox();
        final AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y,
                var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y,
                var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            filledBox(var12, colorIn, true);
            GlStateManager.disableLighting();
            int[] rgba = ColorUtils.getRGBAInt(color);

            // RenderGlobal.drawOutlinedBoundingBox(var12, rgba[0], rgba[1], rgba[2],
            // rgba[3]);
        }
        GlStateManager.popMatrix();
    }

    public static void filledBox(final AxisAlignedBB boundingBox, final int color, final boolean shouldColor) {
        GlStateManager.pushMatrix();
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color(var12, var13, var14, var11);
        }
        final byte draw = 7;
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.begin(draw, DefaultVertexFormats.POSITION);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public static void drawBorderedRectZ(float left, float top, float right, float bottom, int borderColor, int color) {
        drawBorderedRectZ(left, top, right, bottom, 1.0F, borderColor, color);
    }

    public static void drawBorderedGradientRect(double left, double top, double right, double bottom, int borderColor,
                                                int startColor, int endColor) {
        drawBorderedGradientRect(left, top, right, bottom, 1.0F, borderColor, startColor, endColor);
    }

    public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
        GL11.glPushMatrix();
        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        WorldRenderUtils.setColor(rgb);
        enableGL3D(1f);
        Cylinder c = new Cylinder();
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        c.setDrawStyle(100011);
        // c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
        c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
        disableGL3D();
        GL11.glPopMatrix();
    }

    public static void drawExeterCrossESP(int rgb, double x, double y, double z) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - 0.4D, y, z - 0.4D, x + 0.4D, y + 2.0D, z + 0.4D);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(-x, -y, -z);

        WorldRenderUtils.enableGL3D(1.0F); // line width

        WorldRenderUtils.setColor(rgb);

        WorldRenderUtils.drawOutlinedBoundingBox(axisAlignedBB);

        WorldRenderUtils.disableGL3D();
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box != null) {
            GL11.glBegin(3);
            GL11.glVertex3d(box.minX, box.minY, box.minZ);
            GL11.glVertex3d(box.maxX, box.minY, box.minZ);
            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
            GL11.glVertex3d(box.minX, box.minY, box.maxZ);
            GL11.glVertex3d(box.minX, box.minY, box.minZ);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glVertex3d(box.minX, box.minY, box.minZ);
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);
            GL11.glVertex3d(box.maxX, box.minY, box.minZ);
            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
            GL11.glVertex3d(box.minX, box.minY, box.maxZ);
            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
            GL11.glEnd();
        }
    }

    public static void drawLaserPoint(int rgb, double posX, double posY, double posZ) {
        GL11.glPushMatrix();
        enableGL3D(2.0F);
        WorldRenderUtils.setColor(new Color(255, 0, 0).getRGB());
        GL11.glBegin(2);
        GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glEnd();
        disableGL3D();
        GL11.glPopMatrix();
    }

    public static void drawLine2D(double x1, double y1, double x2, double y2, float width, int color) {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
        setColor(color);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static class ColorUtils {
        public int RGBtoHEX(final int r, final int g, final int b, final int a) {
            return (a << 24) + (r << 16) + (g << 8) + b;
        }

        public Color getRainbow(final long offset, final float fade) {
            final float hue = (System.nanoTime() + offset) / 5.0E9f % 1.0f;
            final long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0f, 1.0f))),
                    16);
            final Color c = new Color((int) color);
            return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade,
                    c.getAlpha() / 255.0f);
        }

        public static Color glColor(final int color, final float alpha) {
            final float red = (color >> 16 & 0xFF) / 255.0f;
            final float green = (color >> 8 & 0xFF) / 255.0f;
            final float blue = (color & 0xFF) / 255.0f;
            GL11.glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
        }

        public void glColor(final Color color) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
                    color.getAlpha() / 255.0f);
        }

        public Color glColor(final int hex) {
            final float alpha = (hex >> 24 & 0xFF) / 256.0f;
            final float red = (hex >> 16 & 0xFF) / 255.0f;
            final float green = (hex >> 8 & 0xFF) / 255.0f;
            final float blue = (hex & 0xFF) / 255.0f;
            GL11.glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
        }

        public Color glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
            final float red = 0.003921569f * redRGB;
            final float green = 0.003921569f * greenRGB;
            final float blue = 0.003921569f * blueRGB;
            GL11.glColor4f(red, green, blue, alpha);
            return new Color(red, green, blue, alpha);
        }

        public static int transparency(final int color, final double alpha) {
            final Color c = new Color(color);
            final float r = 0.003921569f * c.getRed();
            final float g = 0.003921569f * c.getGreen();
            final float b = 0.003921569f * c.getBlue();
            return new Color(r, g, b, (float) alpha).getRGB();
        }

        public static float[] getRGBA(final int color) {
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            return new float[]{r, g, b, a};
        }

        public static int[] getRGBAInt(final int color) {
            final int a = (color >> 24 & 0xFF);
            final int r = (color >> 16 & 0xFF);
            final int g = (color >> 8 & 0xFF);
            final int b = (color & 0xFF);
            return new int[]{r, g, b, a};
        }

        public static int intFromHex(final String hex) {
            try {
                return Integer.parseInt(hex, 15);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        public static String hexFromInt(final int color) {
            return hexFromInt(new Color(color));
        }

        public static String hexFromInt(final Color color) {
            return Integer.toHexString(color.getRGB()).substring(2);
        }
    }
}
