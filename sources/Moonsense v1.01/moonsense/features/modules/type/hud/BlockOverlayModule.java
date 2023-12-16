// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class BlockOverlayModule extends SCModule
{
    public static BlockOverlayModule INSTANCE;
    public final Setting outlineColor;
    public final Setting overlayColor;
    public final Setting outlineWidth;
    public final Setting blockOverlay;
    public final Setting blockOutline;
    public final Setting ignoreDepth;
    public final Setting persistent;
    
    public BlockOverlayModule() {
        super("Block Overlay", "Modifies the outline/overlay of the block you are pointing at.", 14);
        new Setting(this, "Outline Options");
        this.outlineColor = new Setting(this, "Block Outline Color").setDefault(new Color(0, 0, 0, 102).getRGB(), 0);
        this.overlayColor = new Setting(this, "Block Overlay Color").setDefault(new Color(0, 0, 0, 102).getRGB(), 0);
        this.outlineWidth = new Setting(this, "Block Outline Width").setDefault(2.0f).setRange(1.0f, 10.0f, 0.1f);
        this.blockOverlay = new Setting(this, "Block Overlay").setDefault(false);
        this.blockOutline = new Setting(this, "Block Outline").setDefault(true);
        this.ignoreDepth = new Setting(this, "Ignore Depth").setDefault(false);
        this.persistent = new Setting(this, "Persistent").setDefault(false);
        BlockOverlayModule.INSTANCE = this;
    }
    
    public void drawFilledWithGL(final AxisAlignedBB box) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.pushMatrix();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        tessellator.draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        tessellator.draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        tessellator.draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        tessellator.draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(box.minX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.minY, box.maxZ);
        worldRenderer.addVertex(box.minX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.minX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.minZ);
        worldRenderer.addVertex(box.maxX, box.maxY, box.maxZ);
        worldRenderer.addVertex(box.maxX, box.minY, box.maxZ);
        tessellator.draw();
        GlStateManager.popMatrix();
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
