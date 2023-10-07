package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.ColorProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createColor;
import static java.awt.Color.BLACK;

public final class ChestESP extends AbstractModule {

    /* properties @off */
    @Property("color")
    private final ColorProperty color = createColor(0xFFFF00C5);
    @Property("mode")
    private final StringProperty mode = PropertyFactory.createString("Outline").acceptableValues("Outline", "Filled", "Chams", "Box");
    @Property("color-visible")
    private final ColorProperty visible = createColor(new Color(255, 101, 101).getRGB());
    @Property("color-hidden")
    private final ColorProperty hidden = createColor(new Color(165, 163, 165).getRGB());

    /* getters */
    public StringProperty getMode() {
        return mode;
    }

    /* constructors @on */
    public ChestESP(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "ChestESP", "Chest ESP", Keyboard.KEY_NONE, EnumModuleType.VISUALS);
        put(new Setting("CHEST_ESP_MODE", "Mode", SettingType.COMBOBOX, this, mode));
        put(new Setting("CHESTESP_COLOR", "Color", SettingType.COLOR_PICKER, this, this.color, null, () -> !mode.get().equalsIgnoreCase("Chams")));
        put(new Setting("CHESTESP_COLOR", "Visible", SettingType.COLOR_PICKER, this, this.visible, null, () -> mode.get().equalsIgnoreCase("Chams")));
        put(new Setting("CHESTESP_COLOR", "Hidden", SettingType.COLOR_PICKER, this, this.hidden, null, () -> mode.get().equalsIgnoreCase("Chams")));
    }

    /* methods */
    private void renderOutline(@NonNull TileEntity tileEntity) {
        final double posX = tileEntity.getPos().getX(), posY = tileEntity.getPos().getY(), posZ = tileEntity.getPos().getZ(); // @on
        AxisAlignedBB axisAlignedBB = null;
        final Block block;

        block = this.mc.world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
        final Block x1 = this.mc.world.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock(), // @off
                x2 = this.mc.world.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock(),
                z1 = this.mc.world.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock(),
                z2 = this.mc.world.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock(); // @on


        if (x1 == block) {
            axisAlignedBB = renderOutlineZero(posX, posY, posZ);
        } else if (z2 == block) {
            axisAlignedBB = renderOutlineFirst(posX, posY, posZ);
        } else if (x2 != block && z1 != block) {
            axisAlignedBB = renderOutlineSecond(posX, posY, posZ);
        }

        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glEnable(2848);

        final float[] colors = this.getColorForTileEntity();
        RenderHelper.drawCompleteBoxFilled(axisAlignedBB, 1.0f, toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 0.2f));

        GL11.glDisable(2848);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
    }

    private @NonNull AxisAlignedBB renderOutlineSecond(double posX, double posY, double posZ) {
        final RenderManager renderManager = this.mc.getRenderManager();
        return new AxisAlignedBB(posX + 0.05000000074505806 - renderManager.renderPosX, posY - renderManager.renderPosY,
                posZ + 0.05000000074505806 - renderManager.renderPosZ,
                posX + 0.949999988079071 - renderManager.renderPosX,
                posY + 0.8999999761581421 - renderManager.renderPosY,
                posZ + 0.949999988079071 - renderManager.renderPosZ);
    }

    private @NonNull AxisAlignedBB renderOutlineFirst(double posX, double posY, double posZ) {
        final RenderManager renderManager = this.mc.getRenderManager();
        return new AxisAlignedBB(posX + 0.05000000074505806 - renderManager.renderPosX, posY - renderManager.renderPosY,
                posZ + 0.05000000074505806 - renderManager.renderPosZ - 1.0,
                posX + 0.949999988079071 - renderManager.renderPosX,
                posY + 0.8999999761581421 - renderManager.renderPosY,
                posZ + 0.949999988079071 - renderManager.renderPosZ);
    }

    private @NonNull AxisAlignedBB renderOutlineZero(double posX, double posY, double posZ) {
        final RenderManager renderManager = this.mc.getRenderManager();
        return new AxisAlignedBB(posX + 0.05000000074505806 - renderManager.renderPosX, posY - renderManager.renderPosY,
                posZ + 0.05000000074505806 - renderManager.renderPosZ,
                posX + 1.9500000476837158 - renderManager.renderPosX,
                posY + 0.8999999761581421 - renderManager.renderPosY,
                posZ + 0.949999988079071 - renderManager.renderPosZ);
    }

    public float[] getColorForTileEntity() {
        final Color color = this.color.getAwtColor();
        return new float[]{(float) color.getRed(), color.getGreen(), color.getBlue(), 200.0f};
    }

    public int toRGBAHex(float r, float g, float b, float a) {
        return ((int) (a * 255.0f) & 255) << 24 | ((int) (r * 255.0f) & 255) << 16 | ((int) (g * 255.0f) & 255) << 8 | (int) (b * 255.0f) & 255;
    }

    /* events */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mode.equals("Filled")) {
            for (TileEntity tileEntity : this.mc.world.getLoadedTileEntityList()) {
                if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
                    if (!tileEntity.isInvalid() && this.mc.world.getBlockState(tileEntity.getPos()) != null) {
                        renderOutline(tileEntity);
                    }
                }
            }
        }
    }

    @EventTarget
    public void on2D(Render2DEvent event) {
        if (mode.equals("Box")) {
            for (TileEntity collectedEntity : mc.world.getLoadedTileEntityList().stream().filter(e -> e instanceof TileEntityChest).collect(Collectors.toList())) {
                BlockPos pos = collectedEntity.getPos();
                AxisAlignedBB aabb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                this.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

                Vector4d position = null;

                for (Vector3d vector : vectors) {
                    vector = project2D(event.getResolution(), vector.x - this.mc.getRenderManager().viewerPosX,
                            vector.y - this.mc.getRenderManager().viewerPosY,
                            vector.z - this.mc.getRenderManager().viewerPosZ);

                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }

                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                this.mc.entityRenderer.setupOverlayRendering();

                if (position != null) {
                    double posX = position.x, // @off
                            posY = position.y,
                            endPosX = position.z,
                            endPosY = position.w;
                    RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, 3, BLACK);

                    RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, 1, color.getAwtColor());
                }
            }
        }
    }

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);


    private Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat(2982, this.modelView);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);

        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelView, this.projection, this.viewport,
                this.vector)) {
            return new javax.vecmath.Vector3d(this.vector.get(0) / scaledResolution.getScaleFactor(),
                    (Display.getHeight() - this.vector.get(1)) / scaledResolution.getScaleFactor(), this.vector.get(2));
        }

        return null;
    }

    public void pre3D() {
        checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(3f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public void checkSetupFBO() {
        final Framebuffer framebuffer = Minecraft.getInstance().getFramebuffer();

        if (framebuffer != null && framebuffer.depthBuffer > -1) {
            setupFBO(framebuffer);

            framebuffer.depthBuffer = -1;
        }
    }

    public void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        final int stencilDepthBufferId = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferId);
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.getInstance().displayWidth, Minecraft.getInstance().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferId);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferId);
    }

    public void setupStencil() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public void setupStencil2() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }


    public void setupStencilFirst() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public void setupStencilSecond() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public void renderOutline(int color) {
        setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1f, -2e06f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
    }

    public void setColor(int i) {
        float f = (float) (i >> 24 & 255) / 255f;
        float f0 = (float) (i >> 16 & 255) / 255f;
        float f1 = (float) (i >> 8 & 255) / 255f;
        float f2 = (float) (i & 255) / 255f;
        if (f == 0.0f) {
            f = 1f;
        }
        GL11.glColor4f(f0, f1, f2, f);
    }

    public void post3D() {
        GL11.glPolygonOffset(1f, 2e06f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public ColorProperty getHidden() {
        return hidden;
    }

    public ColorProperty getVisible() {
        return visible;
    }
}
