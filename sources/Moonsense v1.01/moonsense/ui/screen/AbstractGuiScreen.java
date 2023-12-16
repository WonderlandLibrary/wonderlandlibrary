// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen;

import moonsense.ui.screen.settings.GuiModules;
import net.minecraft.client.gui.ScaledResolution;
import moonsense.ui.elements.module.ElementModuleSettings2;
import moonsense.ui.elements.module.ElementModule;
import org.lwjgl.opengl.GL11;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.Iterator;
import moonsense.ui.elements.settings.SettingElementKeybind;
import java.io.IOException;
import com.google.common.collect.Lists;
import java.util.concurrent.ScheduledExecutorService;
import moonsense.ui.components.Component;
import moonsense.ui.elements.Element;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public abstract class AbstractGuiScreen extends GuiScreen
{
    public final List<Element> elements;
    public final List<Component> components;
    private ScheduledExecutorService executorService;
    protected AbstractGuiScrolling scroll;
    private int xPosition;
    private int yPosition;
    private int scissorX;
    private int scissorY;
    private boolean scissor;
    protected boolean enableInput;
    
    public AbstractGuiScreen() {
        this.elements = (List<Element>)Lists.newArrayList();
        this.components = (List<Component>)Lists.newArrayList();
        this.enableInput = true;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.elements.forEach(Element::init);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.elements.forEach(element -> element.mouseClicked(mouseX, mouseY, mouseButton));
        this.components.forEach(component -> component.elements.forEach(element -> element.mouseClicked(mouseX, mouseY, mouseButton)));
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.elements.forEach(element -> element.mouseReleased(mouseX, mouseY, state));
        this.components.forEach(component -> component.elements.forEach(element -> element.mouseReleased(mouseX, mouseY, state)));
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            if (this.elements.stream().noneMatch(element -> element instanceof SettingElementKeybind) || this.elements.stream().filter(element -> element instanceof SettingElementKeybind).noneMatch(element -> element.selection)) {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
        this.elements.forEach(element -> element.keyTyped(typedChar, keyCode));
        this.components.forEach(component -> component.elements.forEach(element -> element.keyTyped(typedChar, keyCode)));
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.scroll != null && this.enableInput) {
            this.scroll.handleMouseInput();
        }
        for (final Element e : this.elements) {
            e.handleMouseInput();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.executorService == null) {
            (this.executorService = Executors.newSingleThreadScheduledExecutor()).scheduleAtFixedRate(() -> {
                this.elements.forEach(Element::update);
                this.components.forEach(component -> component.elements.forEach(Element::update));
                return;
            }, 0L, 16L, TimeUnit.MILLISECONDS);
        }
        if (this.scroll != null) {
            this.scroll.drawScreen(mouseX, mouseY, partialTicks);
            this.yPosition = this.scroll.getAmountScrolled();
        }
        this.components.forEach(component -> component.render(mouseX, mouseY, partialTicks));
        this.elements.forEach(element -> {
            if (element.shouldScissor) {
                if (this.scissor) {
                    GL11.glEnable(3089);
                }
                element.addOffsetToY(-this.yPosition);
            }
            else {
                GL11.glDisable(3089);
            }
            if (element instanceof ElementModule) {
                if (mouseY > this.scissorY) {
                    element.hovered(mouseX, mouseY);
                }
                else {
                    element.hovered = false;
                }
            }
            if (element instanceof ElementModuleSettings2) {
                if (mouseY > this.scissorY + 18) {
                    element.hovered(mouseX, mouseY);
                }
                else {
                    element.hovered = false;
                }
            }
            element.render(mouseX, mouseY, partialTicks);
            if (element.shouldScissor) {
                if (this.scissor) {
                    GL11.glDisable(3089);
                }
                element.addOffsetToY(this.yPosition);
            }
            return;
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void scissorFunc(final ScaledResolution sr, final int x, final int y, final int width, final int height) {
        this.scissorX = x;
        this.scissorY = y / sr.getScaleFactor();
        this.scissor = true;
        GL11.glScissor(x, y, width, height);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.executorService != null) {
            this.executorService.shutdownNow();
            this.executorService = null;
        }
    }
    
    protected void registerScroll(final AbstractGuiScrolling scroll) {
        (this.scroll = scroll).registerScrollButtons(7, 8);
    }
    
    public void expandScroll(final int expandedAmount) {
        ((GuiModules.Scroll)this.scroll).expandBy(expandedAmount);
        this.scroll.scrollTo((float)(this.scroll.getAmountScrolled() + expandedAmount), true, 1500L);
    }
    
    public void addScroll(final int expandedAmount) {
        ((GuiModules.Scroll)this.scroll).expandBy(expandedAmount);
    }
    
    protected void setScrollState(final int scrollState) {
        this.scroll.scrollTo((float)scrollState, true, 1500L);
    }
    
    protected int getScrollState() {
        return this.scroll.getAmountScrolled();
    }
}
