package de.tired.util.render;

import com.jhlabs.image.GaussianFilter;
import de.tired.base.interfaces.IHook;
import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.base.guis.newaltmanager.storage.AltData;
import de.tired.util.math.vector.Vec;
import de.tired.util.render.shaderloader.KoksFramebuffer;
import de.tired.util.render.shaderloader.ShaderExtension;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.ShaderProgramOther;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil implements IHook {

    public static RenderUtil instance;


    public double xOffset = 0;
    public float animationY;

    private static final HashMap<Integer, Integer> chache = new HashMap<>();
    private final Animation hotbarAnimation = new Animation();

    public int yOffset = 0;

    private static final ShaderProgramOther gradientMaskShader = new ShaderProgramOther("AlphaGradient.glsl");
    private static final ShaderProgramOther gradientShader = new ShaderProgramOther("Gradient.glsl");

    public float delta;

    public RenderUtil() {
        frustum = new Frustum();
        instance = this;
    }

    public void push() {
        GlStateManager.pushMatrix();
    }

    public void bindTexture(int texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

    public void pop() {
        GlStateManager.pushMatrix();
    }

    public double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public void doRenderShadow(Color color, double x, double y, double width, double height, int blurRadius) {
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glDisable(GL_CULL_FACE);
        renderShadow(x, y, width, height, blurRadius, color);
        GL11.glDisable(GL_BLEND);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glEnable(GL_CULL_FACE);
        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
    }

    public void renderShadow(final double x, final double y, double width, double height, final int blurRadius, final Color color) {
        GlStateManager.alphaFunc(GL_GREATER, (float) (0.0 * .01));
        final float xCalc = (float) (x - (double) blurRadius - 0.25), yCalc = (float) (y - (double) blurRadius + 0.25);
        final int identifier = (int) ((width += blurRadius * 2) * (height += blurRadius * 2) + width + (double) (color.hashCode() * blurRadius) + (double) blurRadius);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_CULL_FACE);
        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glEnable(GL_BLEND);
        int texture;
        if (chache.containsKey(identifier)) {
            texture = chache.get(identifier);
            GlStateManager.bindTexture(texture);
        } else {
            width = MathHelper.clamp_double(width, 0.01, width);
            height = MathHelper.clamp_double(height, 0.01, height);
            final BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
            final Graphics g = original.getGraphics();
            {
                g.setColor(color);
                g.fillRect(blurRadius, blurRadius, (int) width - blurRadius * 2, (int) height - blurRadius * 2);
                g.dispose();
            }
            final GaussianFilter gaussianFilter = new GaussianFilter(blurRadius);
            final BufferedImage imageFilter = gaussianFilter.filter(original, null);
            texture = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), imageFilter, true, false);
            chache.put(identifier, texture);
        }
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        KoksFramebuffer.drawQuads(xCalc, yCalc, (int) width, (int) height);
        GL11.glDisable(GL_TEXTURE_2D);
    }


    public void drawRect2(double x, double y, double width, double height, int color) {
        GlStateManager.resetColor();
        handleRender(() -> render(GL11.GL_QUADS, () -> {
            color(color);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y + height);
            GL11.glVertex2d(x + width, y + height);
            GL11.glVertex2d(x + width, y);
        }));
    }

    public ThreadDownloadImageData getHead(ResourceLocation resourceLocationIn, String username) {
        ITextureObject iTextureObject = MC.getTextureManager().getTexture(resourceLocationIn);


        if (iTextureObject == null) {
            iTextureObject = new ThreadDownloadImageData(null, "https://crafatar.com/avatars/" + username, DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getOfflineUUID(username)), new ImageBufferDownload());

            MC.getTextureManager().loadTexture(resourceLocationIn, iTextureObject);
        }


        return (ThreadDownloadImageData) iTextureObject;
    }

    public void drawHead(AltData alt, int x, int y, int size) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        if (alt.getName() != null) {
            ThreadDownloadImageData thread = getHead(AbstractClientPlayer.getLocationSkin(alt.getName()), alt.getUuid() == null ? "" : alt.getUuid());
            try {
                thread.loadTexture(MC.getResourceManager());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        MC.getTextureManager().bindTexture(alt.getName() == null ? new ResourceLocation("") : AbstractClientPlayer.getLocationSkin(alt.getName()));
        if (alt.getEmailAddress() == null) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, size, size, size, size);
        } else {
            float textureWidth = size * 1.63f;
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, size, size, textureWidth, size * 1.63f);
        }
    }

    private final Frustum frustum;

    public String readShader(String fileName) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(String.format("assets/minecraft/shaders2/%s", fileName))));
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public void drawCircle(double x, double y, float radius, int color) {
        try {
            final ScaledResolution sr = new ScaledResolution(MC);
            color(color);
            handleRender(() -> {
                glEnable(GL_POINT_SMOOTH);
                glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
                glPointSize(radius * (2 * sr.getScaleFactor()));
                render(GL_POINTS, () -> glVertex2d(x, y));
            });
        } catch (Exception ignored) {

        }
    }

    public void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public void handleRender(final Runnable f) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        f.run();
        glEnable(GL_TEXTURE_2D);
        GlStateManager.disableBlend();
    }

    public void render(int mode, Runnable render) {
        glBegin(mode);
        render.run();
        glEnd();
    }


    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public Framebuffer createFramebuffer(Framebuffer framebuffer, boolean depth) {
        if (framebuffer == null || framebuffer.framebufferWidth != MC.displayWidth || framebuffer.framebufferHeight != MC.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(MC.displayWidth, MC.displayHeight, depth);
        }
        return framebuffer;
    }


    public void drawCheckMark(float x, float y, int width, int color) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d((double) (x + (float) width) - 6.5D, (double) (y + 4.0F));
        GL11.glVertex2d((double) (x + (float) width) - 11.5D, (double) (y + 10.0F));
        GL11.glVertex2d((double) (x + (float) width) - 13.5D, (double) (y + 8.0F));
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    public void drawPlayerHead(EntityPlayer player, double x, double y, int width, int height) {
        final AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        MC.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    public boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float) (color >> 24 & 255) / 255.0f;
        float f = (float) (color >> 16 & 255) / 255.0f;
        float f1 = (float) (color >> 8 & 255) / 255.0f;
        float f2 = (float) (color & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = MC.getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
    }


    public void line(Vec firstPoint, Vec secondPoint, int color) {
        line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), color);
    }

    private void line(double x, double y, double x1, double y1, int color) {
        GL11.glPushMatrix();
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        boolean lineSmooth = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5F);
        GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F);
        GL11.glBegin(1);
        GL11.glVertex2f((float) x1, (float) y1);
        GL11.glVertex2f((float) x, (float) y);
        GL11.glEnd();
        if (!lineSmooth)
            GL11.glDisable(GL11.GL_LINE_SMOOTH);


        if (texture2D)
            GL11.glEnable(GL11.GL_TEXTURE_2D);


        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);


        GL11.glPopMatrix();
    }

    public void renderHotbar(boolean animation, boolean rect) {
        this.animationY = (float) AnimationUtil.getAnimationState(animationY, MC.currentScreen instanceof GuiChat ? 0 : 27, 440);
        final ScaledResolution scaledResolution = new ScaledResolution(MC);


        final int item = MC.thePlayer.inventory.currentItem + 1;
        final float xPos = item * 20 + 1.5f;

        hotbarAnimation.update();
        hotbarAnimation.animate(xPos, .05, Easings.BACK_IN4);
        GL11.glEnable(GL_BLEND);

        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        if (!animation)
            hotbarAnimation.setValue(xPos);
        doRenderHotbarRectangle(RectangleType.BAR, scaledResolution, animationY, hotbarAnimation.getValue());
        doRenderHotbarRectangle(RectangleType.HOTBAR_POSITION, scaledResolution, animationY, hotbarAnimation.getValue());

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glEnable(GL_BLEND);

    }

    public final void clearColor() {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public float[] getColor(int color) {
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        return new float[]{(float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F};
    }


    public void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    private void doRenderHotbarRectangle(RectangleType rectangle_type, ScaledResolution scaledResolution, float animationY, float hotbarAnimation) {
        switch (rectangle_type) {
            case BAR: {
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound((int) ((float) (scaledResolution.getScaledWidth() / 2 - 89) + 22f - 24.5f), (int) (scaledResolution.getScaledHeight() - animationY), 180, 24, 0, new Color(20, 20, 20, 75));
            }
            break;

            case HOTBAR_POSITION:
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound((scaledResolution.getScaledWidth() / 2f - 88) + (int) hotbarAnimation - 24.5f, (int) (scaledResolution.getScaledHeight() - 24 - animationY + 23) + 1.5f, 19f, 23f, 0, new Color(20, 20, 20, 125));
        }
    }


    enum RectangleType {
        BAR, HOTBAR_POSITION
    }

    public void color(double red, double green, double blue) {
        color(red, green, blue, 1);
    }

    public void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public void drawOutlinedBoundingBox(AxisAlignedBB aa) {
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

    public void drawBoundingBox(AxisAlignedBB aa) {
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

    public void drawGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        ScaledResolution sr = new ScaledResolution(MC);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ShaderExtension.useShader(gradientShader.getShaderProgramID());
        gradientShader.setShaderUniform("location", x * sr.getScaleFactor(), (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientShader.setShaderUniform("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        gradientShader.setShaderUniform("alpha", alpha);
        gradientShader.setShaderUniform("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f);
        gradientShader.setShaderUniform("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f);
        gradientShader.setShaderUniform("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f);
        gradientShader.setShaderUniform("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f);

        KoksFramebuffer.drawQuads(x, y, width, height);


        ShaderExtension.deleteProgram();

        GlStateManager.disableBlend();
    }

    public void drawGradientLR(float x, float y, float width, float height, float alpha, Color left, Color right) {
        drawGradient(x, y, width, height, alpha, left, left, right, right);
    }

    public void drawGradientTB(float x, float y, float width, float height, float alpha, Color top, Color bottom) {
        drawGradient(x, y, width, height, alpha, bottom, top, bottom, top);
    }


    public void applyGradientHorizontal(float x, float y, float width, float height, float alpha, Color left, Color right, Runnable content) {
        applyGradient(x, y, width, height, alpha, left, left, right, right, content);
    }

    public void applyGradientVertical(float x, float y, float width, float height, float alpha, Color top, Color bottom, Runnable content) {
        applyGradient(x, y, width, height, alpha, bottom, top, bottom, top, content);
    }
    public Double interpolateC(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }


    public void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ShaderExtension.useShader(gradientMaskShader.getShaderProgramID());

        final ScaledResolution sr = new ScaledResolution(MC);

        gradientMaskShader.setShaderUniform("location", x * sr.getScaleFactor(), (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        gradientMaskShader.setShaderUniform("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        gradientMaskShader.setShaderUniform("alpha", alpha);
        gradientMaskShader.setShaderUniformI("tex", 0);
        gradientMaskShader.setShaderUniform("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f);
        gradientMaskShader.setShaderUniform("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f);
        gradientMaskShader.setShaderUniform("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f);
        gradientMaskShader.setShaderUniform("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f);
        content.run();
        ShaderExtension.deleteProgram();
        GlStateManager.disableBlend();

    }
}
