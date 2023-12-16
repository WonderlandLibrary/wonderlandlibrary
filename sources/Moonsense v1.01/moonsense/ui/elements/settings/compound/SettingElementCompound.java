// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.compound;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.elements.settings.SettingElementString;
import moonsense.ui.elements.settings.mode.SettingElementMode;
import moonsense.utils.KeyBinding;
import moonsense.ui.elements.settings.SettingElementKeybind;
import moonsense.MoonsenseClient;
import moonsense.utils.MathUtil;
import moonsense.ui.elements.settings.SettingElementSlider;
import moonsense.ui.elements.settings.SettingElementCheckbox;
import moonsense.utils.ColorObject;
import moonsense.ui.utils.GuiUtils;
import moonsense.ui.elements.settings.color.SettingElementColor;
import moonsense.ui.elements.settings.ElementCategory;
import moonsense.ui.screen.settings.AbstractSettingsGui;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Iterator;
import com.google.common.collect.Lists;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.ui.elements.Element;
import moonsense.settings.Setting;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.settings.SettingElement;

public class SettingElementCompound extends SettingElement
{
    private static final ResourceLocation settingsIcon;
    private final Consumer update;
    private ArrayList<Setting> settings;
    public boolean expanded;
    private int row;
    private int column;
    private ArrayList<Element> settingElements;
    
    static {
        settingsIcon = new ResourceLocation("streamlined/icons/settings.png");
    }
    
    public SettingElementCompound(final int x, final int y, final int width, final int height, final int xOffset, final int yOffset, final Setting setting, final Consumer update, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, xOffset, yOffset, setting, consumer, parent);
        this.expanded = false;
        this.front = true;
        this.update = update;
        this.settings = ((Setting.CompoundSettingGroup)setting.getObject()).getSettings();
        this.settingElements = (ArrayList<Element>)Lists.newArrayList();
        this.row = 0;
        this.column = 1;
        for (final Setting s : this.settings) {
            if (s.isHidden()) {
                continue;
            }
            if (s.getValue().size() == 0) {
                this.addSetting(s, this.x + 8, this.getY() + 4 + (int)this.getRowHeight(this.row, 17));
            }
            else {
                this.addSetting(s, this.x + 3, this.getY() + 4 + (int)this.getRowHeight(this.row, 17));
            }
            ++this.column;
            if (this.column <= 1) {
                continue;
            }
            this.column = 1;
            ++this.row;
        }
    }
    
    @Override
    public void init() {
        super.init();
    }
    
    private void addSetting(final Setting setting, final int x, final int y) {
        if (setting.hasValue()) {
            Element element = null;
            switch (setting.getType()) {
                case COLOR: {
                    final AtomicBoolean found = new AtomicBoolean(false);
                    final AtomicBoolean atomicBoolean;
                    final AtomicBoolean atomicBoolean2;
                    element = new SettingElementColor(x + 1, y + 1, 10, 10, Math.max(356, this.parent.width - this.parent.width / 3) - 87, 0, setting, (mainElement, expanded) -> {
                        if (expanded) {
                            AbstractSettingsGui.totalOffset += 40;
                            this.parent.addScroll(40);
                        }
                        else {
                            AbstractSettingsGui.totalOffset -= 40;
                            this.parent.addScroll(-40);
                        }
                        this.settingElements.forEach(settingElement -> {
                            if ((settingElement instanceof SettingElement || settingElement instanceof ElementCategory) && (settingElement == mainElement || atomicBoolean.get())) {
                                atomicBoolean.set(true);
                                if (settingElement != mainElement && this.settingElements.indexOf(settingElement) > this.settingElements.indexOf(mainElement)) {
                                    settingElement.addOffsetToY(((boolean)expanded) ? 40 : -40);
                                }
                            }
                            return;
                        });
                        this.parent.elements.forEach(settingElement -> {
                            if ((settingElement instanceof SettingElement || settingElement instanceof ElementCategory) && (settingElement == mainElement || atomicBoolean2.get())) {
                                atomicBoolean2.set(true);
                                if (settingElement != mainElement && this.parent.elements.indexOf(settingElement) > this.parent.elements.indexOf(this)) {
                                    settingElement.addOffsetToY(((boolean)expanded) ? 40 : -40);
                                }
                            }
                        });
                        return;
                    }, (module, settingElement) -> module.setValue(new ColorObject(GuiUtils.getRGB(settingElement.color, settingElement.alpha), settingElement.chroma.active, (int)settingElement.chromaSpeed.getDenormalized())), this.parent);
                    break;
                }
                case CHECKBOX: {
                    element = new SettingElementCheckbox(x + 1, y + 1, 20, 10, setting, (module, settingElement) -> setting.setValue(!setting.getBoolean()), this.parent);
                    break;
                }
                case INT_SLIDER: {
                    element = new SettingElementSlider(x + 1, y + 4, 150, 5, setting.getRange(0), setting.getRange(1), setting.getRange(2), (float)setting.getInt(), setting, (module, settingElement) -> setting.setValue((int)MathUtil.denormalizeValue(settingElement.sliderValue, setting.getRange(0), setting.getRange(1), setting.getRange(2))), this.parent);
                    element.setXOffset((int)MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 6);
                    break;
                }
                case FLOAT_SLIDER: {
                    element = new SettingElementSlider(x + 1, y + 4, 150, 5, setting.getRange(0), setting.getRange(1), setting.getRange(2), setting.getFloat(), setting, (module, settingElement) -> setting.setValue(MathUtil.denormalizeValue(settingElement.sliderValue, setting.getRange(0), setting.getRange(1), setting.getRange(2))), this.parent);
                    element.setXOffset((int)MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 6);
                    break;
                }
                case KEYBIND: {
                    element = new SettingElementKeybind(x + 1, y + 1, 10, 10, setting, (module, settingElement) -> setting.setValue(new KeyBinding(settingElement.keycode)), this.parent);
                    element.setXOffset((int)(MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 4.0f));
                    break;
                }
                case MODE: {
                    element = new SettingElementMode(x + 1, y + 1, this.getWidth() - 125, 100, 10, setting.getInt(), setting, (module, settingElement) -> setting.setValue(settingElement.mode), this.parent);
                    break;
                }
                case STRING: {
                    element = new SettingElementString(x + 1, y + 1, 100, 10, setting.getString(), setting, (module, settingElement) -> setting.setValue(settingElement.getTextField().getText()), this.parent);
                    break;
                }
            }
            if (element == null) {
                return;
            }
            this.settingElements.add(element);
        }
        else {
            this.settingElements.add(new ElementCategory(x - 7, y + 3, 0, 10, setting.getDescription(), true));
        }
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2.0f - this.getHeight() / 2.0f + 25.0f + row * buttonHeight;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.keyTyped(typedChar, keyCode);
            }
        }
    }
    
    @Override
    public void mouseDragged(final int mouseX, final int mouseY) {
        super.mouseDragged(mouseX, mouseY);
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.mouseDragged(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.mouseReleased(mouseX, mouseY, state);
            }
        }
    }
    
    @Override
    public void addOffsetToY(final int add) {
        super.addOffsetToY(add);
        for (final Element e : this.settingElements) {
            e.setYOffset(e.getYOffset() + add);
        }
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        if (this.hovered || this.expanded) {
            this.mc.getTextureManager().bindTexture(SettingElementCompound.settingsIcon);
            GuiUtils.setGlColor(Color.white.darker().getRGB());
            Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
        }
        else {
            this.mc.getTextureManager().bindTexture(SettingElementCompound.settingsIcon);
            GuiUtils.setGlColor(Color.white.darker().darker().getRGB());
            Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.update();
            }
            this.consumer.accept(this.setting, this);
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        for (final Element e : this.settingElements) {
            if (e instanceof SettingElementSlider) {
                ((SettingElementSlider)e).setVisibilityOfSliders(this.expanded);
            }
        }
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.render(mouseX, mouseY, partialTicks);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered) {
            if (!(this.expanded = !this.expanded)) {
                for (final Element e : this.settingElements) {
                    if (e instanceof SettingElementColor && ((SettingElementColor)e).expanded) {
                        e.hovered = true;
                        e.mouseClicked(e.getX(), e.getY(), mouseButton);
                        e.hovered = false;
                    }
                }
            }
            this.update.apply(this, this.expanded);
        }
        if (this.expanded) {
            for (final Element e : this.settingElements) {
                e.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        if (this.expanded) {
            int size = 17 * this.settings.size();
            for (final Element e : this.settingElements) {
                if (e instanceof SettingElementColor && ((SettingElementColor)e).expanded) {
                    size += 40;
                }
            }
            GuiUtils.drawRoundedRect((float)this.x, (float)(this.getY() + this.height), (float)(this.getX() + this.width), (float)(this.getY() + this.getHeight() + size), 2.0f, new Color(83, 83, 84, 100).getRGB());
            for (final Element e : this.settingElements) {
                if (e instanceof SettingElementColor) {
                    e.renderBackground(partialTicks);
                }
            }
        }
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public ArrayList<Element> getElements() {
        return this.settingElements;
    }
    
    @FunctionalInterface
    public interface Consumer
    {
        void apply(final SettingElementCompound p0, final Boolean p1);
    }
}
