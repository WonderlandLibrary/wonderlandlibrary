package vip.radium.module.impl.esp;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.render.Render3DEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.OGLUtils;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(label = "Chest ESP", category = ModuleCategory.ESP)
public final class ChestESP extends Module {

    private static ChestESP instance;

    public final EnumProperty<Mode> mode = new EnumProperty<>("Mode", Mode.BOX);
    public final Property<Integer> visibleColorProperty = new Property<>(
            "Visible", Colors.PURPLE,
            this::isChams);
    public final Property<Integer> occludedColorProperty = new Property<>(
            "Occluded", 0x977C7CFF,
            this::isChams);
    public final Property<Boolean> visibleFlatProperty = new Property<>(
            "Visible Flat", false, this::isChams);
    public final Property<Boolean> occludedFlatProperty = new Property<>(
            "Occluded Flat", true, this::isChams);
    private final Property<Boolean> outlineProperty = new Property<>(
            "Outline", false,
            () -> mode.getValue() == Mode.BOX);
    private final Property<Integer> colorProperty = new Property<>(
            "Color", 0x7C7CFF,
            () -> mode.getValue() != Mode.CHAMS);
    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<Render3DEvent> onRender3DEvent = e -> {
        if (mode.getValue() == Mode.BOX) {
            final boolean outline = outlineProperty.getValue();

            if (outline) {
                glEnable(GL_LINE_SMOOTH);
                glLineWidth(1.0F);
            }

            for (TileEntity entity : Wrapper.getWorld().loadedTileEntityList) {
                if (entity instanceof TileEntityChest) {
                    BlockPos pos = entity.getPos();
                    AxisAlignedBB bb = entity.getBlockType().getCollisionBoundingBox(
                            Wrapper.getWorld(),
                            pos,
                            entity.getBlockType().getStateFromMeta(
                                    entity.getBlockMetadata()));
                    if (bb != null) {
                        glDisable(GL_DEPTH_TEST);
                        OGLUtils.enableBlending();
                        glDepthMask(false);
                        glDisable(GL_TEXTURE_2D);
                        OGLUtils.color(colorProperty.getValue());
                        final double rX = RenderManager.renderPosX;
                        final double rY = RenderManager.renderPosY;
                        final double rZ = RenderManager.renderPosZ;
                        glTranslated(-rX, -rY, -rZ);
                        RenderGlobal.func_181561_a(bb, outlineProperty.getValue(), true);
                        glTranslated(rX, rY, rZ);
                        glEnable(GL_DEPTH_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        glDepthMask(true);
                        glEnable(GL_TEXTURE_2D);
                    }
                }
            }

            if (outline)
                glDisable(GL_LINE_SMOOTH);
        }
    };

    public static ChestESP getInstance() {
        return instance != null ? instance : (instance = ModuleManager.getInstance(ChestESP.class));
    }

    public static void preOccludedRender(int occludedColor, boolean occludedFlat) {
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        if (occludedFlat)
            glDisable(GL_LIGHTING);
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(0.0F, -1000000.0F);
        OpenGlHelper.setLightmapTextureCoords(1, 240.0F, 240.0F);
        glDepthMask(false);
        OGLUtils.color(occludedColor);
    }

    public static void preVisibleRender(int visibleColor, boolean visibleFlat, boolean occludedFlat) {
        glDepthMask(true);
        if (occludedFlat && !visibleFlat)
            glEnable(GL_LIGHTING);
        else if (!occludedFlat && visibleFlat)
            glDisable(GL_LIGHTING);

        OGLUtils.color(visibleColor);
        glDisable(GL_POLYGON_OFFSET_FILL);
    }

    public static void postRender(boolean visibleFlat) {
        if (visibleFlat)
            glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public boolean isChams() {
        return mode.getValue() == Mode.CHAMS;
    }

    private enum Mode {
        CHAMS, BOX
    }

}
