// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import moonsense.config.utils.AnchorPoint;
import moonsense.utils.BoxUtils;
import org.lwjgl.input.Keyboard;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import moonsense.ui.elements.Element;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import java.util.Iterator;
import moonsense.MoonsenseClient;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.ElementHUDHelpButton;
import moonsense.ui.elements.module.ElementLocation;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import moonsense.ui.utils.blur.BlurShader;
import moonsense.features.modules.type.hud.MenuBlurModule;
import io.github.nickacpt.behaviours.canvas.abstractions.CanvasAbstraction;
import moonsense.ui.abstraction.GuiHUDEditorAbstraction;
import moonsense.features.ModuleManager;
import moonsense.config.ModuleConfig;
import io.github.nickacpt.behaviours.canvas.config.CanvasConfig;
import moonsense.config.utils.Position;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import moonsense.features.SCAbstractRenderModule;
import io.github.nickacpt.behaviours.canvas.Canvas;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import moonsense.features.SCModule;
import moonsense.ui.screen.AbstractGuiScreen;

public class GuiHUDEditor extends AbstractGuiScreen
{
    private SCModule dragging;
    public static SCModule lastDragging;
    private int dragClickX;
    private int dragClickY;
    private int offsetX;
    private int offsetY;
    private ScaledResolution sr;
    private final GuiScreen parentScreen;
    public static boolean isDraggingModule;
    private Canvas<SCAbstractRenderModule, Integer> canvas;
    private CanvasPoint mousePosition;
    private Position pos;
    
    static {
        GuiHUDEditor.isDraggingModule = false;
    }
    
    public GuiHUDEditor(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        final CanvasConfig<Integer> canvasConfig = new CanvasConfig<Integer>();
        this.canvas = new Canvas<SCAbstractRenderModule, Integer>(new GuiHUDEditorAbstraction(ModuleConfig.INSTANCE, ModuleManager.INSTANCE, this), canvasConfig);
        canvasConfig.setSafeZoneSize(2.75f);
        canvasConfig.getColors().setSelectionBackground(-251668737);
        canvasConfig.getColors().setSelectionBorder(-352321536);
        this.mousePosition = new CanvasPoint(0.0f, 0.0f);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        if (ModuleConfig.INSTANCE.isEnabled(MenuBlurModule.INSTANCE) && MenuBlurModule.INSTANCE.uiBlur.getBoolean()) {
            BlurShader.INSTANCE.onGuiOpen((float)MenuBlurModule.INSTANCE.uiBlurRadius.getInt());
        }
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.elements.clear();
        for (final SCModule module : ModuleManager.INSTANCE.modules) {
            if (module.isRender()) {
                if (!ModuleConfig.INSTANCE.isEnabled(module)) {
                    continue;
                }
                final SCModule module2;
                final float mouseX;
                final float mouseY;
                this.elements.add(new ElementLocation(module, this, element -> {
                    this.dragging = module2;
                    mouseX = (float)(Mouse.getX() / this.sr.getScaleFactor());
                    mouseY = (float)((this.mc.displayHeight - Mouse.getY()) / this.sr.getScaleFactor());
                    this.offsetX = (int)(-(mouseX - ModuleConfig.INSTANCE.getActualX(module2)));
                    this.offsetY = (int)(-(mouseY - ModuleConfig.INSTANCE.getActualY(module2)));
                    return;
                }));
            }
        }
        this.elements.add(new ElementHUDHelpButton());
        this.components.clear();
        this.buttonList.add(new GuiCustomButton(0, this.width / 2 - 40, this.height / 2 - 13, 80, 26, "MODS", false));
        this.buttonList.get(0).setFontRenderer(MoonsenseClient.titleRenderer);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.mousePosition.setX((float)mouseX);
        this.mousePosition.setY((float)mouseY);
        Gui.drawRect(0, 0, this.width, this.height, MenuBlurModule.INSTANCE.uiBackgroundColor.getColor());
        if (this.dragging != null && Mouse.isButtonDown(0)) {
            this.updateModulePosition(mouseX, mouseY);
        }
        if (Mouse.isButtonDown(0) && this.dragClickX != 0 && this.dragClickY != 0 && this.dragClickX != mouseX && this.dragClickY != mouseY) {
            Gui.drawRect(this.dragClickX, this.dragClickY, mouseX, mouseY, new Color(150, 150, 150, 100).getRGB());
            GuiUtils.drawRectOutline((float)Math.min(this.dragClickX, mouseX), (float)Math.min(this.dragClickY, mouseY), (float)Math.max(this.dragClickX, mouseX), (float)Math.max(this.dragClickY, mouseY), new Color(150, 150, 150, 150).getRGB());
        }
        GuiUtils.drawRectOutline(2.0f, 2.0f, (float)(this.width - 2), (float)(this.height - 2), new Color(0, 255, 255, 255).getRGB());
        this.elements.get(this.elements.size() - 1).y = new ScaledResolution(this.mc).getScaledHeight() - 20;
        this.elements.get(this.elements.size() - 1).renderElement(partialTicks);
        this.canvas.onRender(this.mousePosition);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiModules(this));
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (GuiHUDEditor.lastDragging != null) {
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            if (Keyboard.isKeyDown(42)) {
                switch (keyCode) {
                    case 203: {
                        --xOffset;
                        break;
                    }
                    case 205: {
                        ++xOffset;
                        break;
                    }
                    case 200: {
                        --yOffset;
                        break;
                    }
                    case 208: {
                        ++yOffset;
                        break;
                    }
                }
            }
            else {
                switch (keyCode) {
                    case 203: {
                        xOffset -= 2.0f;
                        break;
                    }
                    case 205: {
                        xOffset += 2.0f;
                        break;
                    }
                    case 200: {
                        yOffset -= 2.0f;
                        break;
                    }
                    case 208: {
                        yOffset += 2.0f;
                        break;
                    }
                }
            }
            final float x = ModuleConfig.INSTANCE.getPosition(GuiHUDEditor.lastDragging).getX() + xOffset;
            final float y = ModuleConfig.INSTANCE.getPosition(GuiHUDEditor.lastDragging).getY() + yOffset;
            ModuleConfig.INSTANCE.setPosition(GuiHUDEditor.lastDragging, ModuleConfig.INSTANCE.getPosition(GuiHUDEditor.lastDragging).getAnchorPoint(), x, y);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        GuiHUDEditor.isDraggingModule = true;
        if (this.elements.stream().filter(element -> element instanceof ElementLocation).noneMatch(element -> element.hovered)) {
            this.dragClickX = mouseX;
            this.dragClickY = mouseY;
        }
        this.canvas.onMouseDown(this.mousePosition);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.dragClickX = 0;
        this.dragClickY = 0;
        GuiHUDEditor.isDraggingModule = false;
        if (this.dragging != null) {
            ModuleConfig.INSTANCE.setClosestAnchorPoint(this.dragging);
            this.updateModulePosition(mouseX, mouseY);
            this.dragging = null;
        }
        this.canvas.onMouseUp(this.mousePosition);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        GuiHUDEditor.lastDragging = null;
        ModuleConfig.INSTANCE.saveConfig();
        if (ModuleConfig.INSTANCE.isEnabled(MenuBlurModule.INSTANCE) && MenuBlurModule.INSTANCE.uiBlur.getBoolean()) {
            BlurShader.INSTANCE.onGuiClose();
        }
    }
    
    private void updateModulePosition(final int mouseX, final int mouseY) {
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2) && !Mouse.isButtonDown(3) && !Mouse.isButtonDown(4)) {
            return;
        }
        float x = (float)(mouseX - BoxUtils.getBoxOffX(this.dragging, ModuleConfig.INSTANCE.getPosition(this.dragging).getAnchorPoint().getX(this.sr.getScaledWidth()), (int)(((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale())) + this.offsetX);
        float y = (float)(mouseY - BoxUtils.getBoxOffY(this.dragging, ModuleConfig.INSTANCE.getPosition(this.dragging).getAnchorPoint().getY(this.sr.getScaledHeight()), (int)(((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale())) + this.offsetY);
        switch (ModuleConfig.INSTANCE.getPosition(this.dragging).getAnchorPoint()) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
            case CENTER_LEFT: {
                if (x < 3.0f) {
                    x = 3.0f;
                }
                if (x > this.width - ((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f) {
                    x = this.width - ((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f;
                    break;
                }
                break;
            }
            case TOP_CENTER:
            case BOTTOM_CENTER:
            case CENTER: {
                if (x > this.width / 2 - ((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f) {
                    x = this.width / 2 - ((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f;
                }
                if (x < -(this.width / 2) + 3) {
                    x = (float)(-(this.width / 2) + 3);
                    break;
                }
                break;
            }
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                if (x < -this.width + 3) {
                    x = (float)(-this.width + 3);
                }
                if (x > -(((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale()) - 3.0f) {
                    x = -(((SCAbstractRenderModule)this.dragging).getWidth() * ((SCAbstractRenderModule)this.dragging).getScale()) - 3.0f;
                    break;
                }
                break;
            }
        }
        switch (ModuleConfig.INSTANCE.getPosition(this.dragging).getAnchorPoint()) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT: {
                if (y < 3.0f) {
                    y = 3.0f;
                }
                if (y > this.height - ((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f) {
                    y = this.height - ((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f;
                    break;
                }
                break;
            }
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT: {
                if (y > this.height / 2 - ((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f) {
                    y = this.height / 2 - ((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale() - 3.0f;
                }
                if (y < -(this.height / 2) + 3) {
                    y = (float)(-(this.height / 2) + 3);
                    break;
                }
                break;
            }
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT: {
                if (y < -this.height + 3) {
                    y = (float)(-this.height + 3);
                }
                if (y > -(((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale()) - 3.0f) {
                    y = -(((SCAbstractRenderModule)this.dragging).getHeight() * ((SCAbstractRenderModule)this.dragging).getScale()) - 3.0f;
                    break;
                }
                break;
            }
        }
        ModuleConfig.INSTANCE.setPosition(this.dragging, ModuleConfig.INSTANCE.getPosition(this.dragging).getAnchorPoint(), x, y);
    }
}
