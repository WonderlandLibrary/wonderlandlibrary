// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.abstraction;

import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasCorner;
import moonsense.config.utils.AnchorPoint;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import moonsense.config.utils.Position;
import moonsense.utils.BoxUtils;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import org.lwjgl.input.Keyboard;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;
import moonsense.features.SCModule;
import com.google.common.collect.Lists;
import java.util.Collection;
import moonsense.ui.screen.settings.GuiHUDEditor;
import moonsense.config.ModuleConfig;
import moonsense.features.ModuleManager;
import moonsense.features.SCAbstractRenderModule;
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction;

public class GuiHUDEditorAbstraction implements CanvasAbstraction<SCAbstractRenderModule, Integer>
{
    private final ModuleManager moduleManager;
    private final ModuleConfig moduleConfig;
    private final GuiHUDEditor configScreen;
    
    public GuiHUDEditorAbstraction(final ModuleConfig api, final ModuleManager manager, final GuiHUDEditor configScreen) {
        this.moduleConfig = api;
        this.moduleManager = manager;
        this.configScreen = configScreen;
    }
    
    @NotNull
    @Override
    public Collection<SCAbstractRenderModule> getElements() {
        final List<SCAbstractRenderModule> list = (List<SCAbstractRenderModule>)Lists.newArrayList();
        for (final SCModule m : this.moduleConfig.getEnabled()) {
            if (m instanceof SCAbstractRenderModule) {
                list.add((SCAbstractRenderModule)m);
            }
        }
        return list;
    }
    
    @NotNull
    @Override
    public CanvasRectangle getRectangle(final SCAbstractRenderModule module) {
        if (Keyboard.isKeyDown(42)) {
            return new CanvasRectangle(new CanvasPoint(-1.0f, -1.0f), new CanvasPoint(-1.0f, -1.0f));
        }
        final float x = (float)BoxUtils.getBoxOffX(module, (int)ModuleConfig.INSTANCE.getActualX(module), (int)(module.getWidth() * module.getScale()));
        final float y = (float)BoxUtils.getBoxOffY(module, (int)ModuleConfig.INSTANCE.getActualY(module), (int)(module.getHeight() * module.getScale()));
        final Position pos = this.moduleConfig.getPosition(module);
        final CanvasPoint topLeft = new CanvasPoint(x, y);
        final CanvasPoint bottomRight = new CanvasPoint(x + module.getWidth(), y + module.getHeight());
        return new CanvasRectangle(topLeft, bottomRight);
    }
    
    @NotNull
    @Override
    public CanvasRectangle getCanvasRectangle() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final CanvasPoint topLeft = new CanvasPoint(0.25f, 0.25f);
        final CanvasPoint bottomRight = new CanvasPoint((float)scaledResolution.getScaledWidth_double() - 0.25f, (float)scaledResolution.getScaledHeight_double() - 0.25f);
        return new CanvasRectangle(topLeft, bottomRight);
    }
    
    @Override
    public void drawLine(@NotNull final CanvasPoint start, @NotNull final CanvasPoint end, final Integer color, float lineWidth) {
        lineWidth = 0.5f;
        final float halfLineWidth = lineWidth / 2.0f;
        GuiUtils.drawRect(start.getX() - halfLineWidth, start.getY() - halfLineWidth, end.getX() + halfLineWidth, end.getY() + halfLineWidth, color);
    }
    
    @Override
    public void drawRectangle(@NotNull final CanvasRectangle rectangle, final Integer color, final boolean hollow, final float lineWidth) {
    }
    
    @Override
    public void moveTo(final SCAbstractRenderModule moveTo, @NotNull final CanvasPoint point) {
        this.moduleConfig.setPosition(moveTo, AnchorPoint.TOP_LEFT, point.getX(), point.getY());
        this.moduleConfig.setClosestAnchorPoint(moveTo);
    }
    
    @Override
    public CanvasCorner getResizeHandleCorner(final SCAbstractRenderModule module) {
        CanvasCorner corner = null;
        switch (ModuleConfig.INSTANCE.getPosition(module).getAnchorPoint()) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
            case CENTER_LEFT: {
                corner = CanvasCorner.TOP_RIGHT;
                break;
            }
            case TOP_CENTER:
            case BOTTOM_CENTER:
            case CENTER: {
                corner = CanvasCorner.TOP_RIGHT;
                break;
            }
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                corner = CanvasCorner.TOP_LEFT;
                break;
            }
            default: {
                corner = CanvasCorner.BOTTOM_RIGHT;
                break;
            }
        }
        return corner;
    }
    
    @Override
    public float getElementScale(final SCAbstractRenderModule mod) {
        return 1.0f;
    }
    
    @Override
    public void setElementScale(final SCAbstractRenderModule module, final float scale) {
    }
}
