// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import moonsense.settings.Setting;
import moonsense.ui.elements.Element;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import moonsense.ui.elements.module.ElementModuleSettings2;
import moonsense.ui.elements.module.ElementModule;
import moonsense.config.ModuleConfig;
import moonsense.ui.utils.LayoutBuilder;
import org.lwjgl.input.Keyboard;
import moonsense.ui.screen.AbstractGuiScrolling;
import java.util.Set;
import moonsense.MoonsenseClient;
import com.google.common.collect.Sets;
import java.util.Collection;
import moonsense.features.SCModule;
import java.util.LinkedHashSet;
import moonsense.features.ModuleManager;
import moonsense.ui.screen.AbstractGuiScreen;
import net.minecraft.client.gui.GuiScreen;
import moonsense.enums.ModuleCategory;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.text.ElementModuleSearch;

public class GuiModules extends AbstractSettingsGui
{
    private int row;
    private int column;
    private int columns;
    private int gap;
    public static int scrollState;
    private ElementModuleSearch search;
    private GuiCustomButton sortAll;
    private GuiCustomButton sortNew;
    private GuiCustomButton sortHud;
    private GuiCustomButton sortServer;
    private GuiCustomButton sortMechanic;
    private ModuleCategory sortBy;
    
    static {
        GuiModules.scrollState = 0;
    }
    
    public GuiModules(final GuiScreen parentScreen) {
        super(parentScreen);
        this.sortBy = ModuleCategory.ALL;
        this.columns = 3;
    }
    
    @Override
    public void initGui() {
        this.initGui(false);
    }
    
    public void initGui(final boolean keepSearch) {
        this.row = 1;
        this.column = 1;
        this.gap = this.getLayoutWidth(this.getMainWidth() / 6) / 8;
        this.gap -= 20;
        this.elements.clear();
        this.components.clear();
        if (keepSearch) {
            this.elements.add(this.search);
        }
        else {
            this.elements.add(this.search = new ElementModuleSearch(this.width / 2 + this.getWidth() / 2 - 135, this.height / 2 - this.getHeight() / 2 + 2 - 11, 100, 14, this));
        }
        this.sortAll = new GuiCustomButton(100, this.width / 2 - this.getWidth() / 2 + 18 - 5, this.height / 2 - this.getHeight() / 2 + 2, 50, 10, "ALL", false);
        this.sortNew = new GuiCustomButton(100, this.width / 2 - this.getWidth() / 2 + 18 + 50, this.height / 2 - this.getHeight() / 2 + 2, 50, 10, "NEW", false);
        this.sortHud = new GuiCustomButton(100, this.width / 2 - this.getWidth() / 2 + 18 + 100 + 5, this.height / 2 - this.getHeight() / 2 + 2, 50, 10, "HUD", false);
        this.sortServer = new GuiCustomButton(100, this.width / 2 - this.getWidth() / 2 + 18 + 150 + 10, this.height / 2 - this.getHeight() / 2 + 2, 50, 10, "SERVER", false);
        this.sortMechanic = new GuiCustomButton(100, this.width / 2 - this.getWidth() / 2 + 18 + 200 + 15, this.height / 2 - this.getHeight() / 2 + 2, 50, 10, "MECHANIC", false);
        final LinkedHashSet<SCModule> modules = new LinkedHashSet<SCModule>(ModuleManager.INSTANCE.modules);
        final Set<SCModule> toRemove = (Set<SCModule>)Sets.newLinkedHashSet();
        final Set<SCModule> set;
        modules.forEach(module -> {
            if (module.isChildModule()) {
                return;
            }
            else {
                if (MoonsenseClient.getSearchPattern(this.search.getText()).matcher(module.displayName).find() || module.settings.stream().anyMatch(setting -> MoonsenseClient.getSearchPattern(this.search.getText()).matcher(setting.getDescription()).find())) {
                    if (this.sortBy == ModuleCategory.ALL) {
                        this.addElement(module);
                    }
                    else if (this.sortBy == module.getCategory()) {
                        this.addElement(module);
                    }
                    else if (this.sortBy == ModuleCategory.NEW && module.isNewModule()) {
                        this.addElement(module);
                    }
                    else {
                        set.add(module);
                    }
                }
                else {
                    set.add(module);
                }
                return;
            }
        });
        if (keepSearch) {
            modules.removeAll(toRemove);
        }
        super.initGui();
        this.registerScroll(new Scroll(modules, this.width, this.height, this.height / 2 - this.getHeight() / 2 + 17, this.height / 2 + this.getHeight() / 2 + 20, 50, this.width / 2 + this.getWidth() / 2 - 4, this.columns, 0));
        this.setScrollState(GuiModules.scrollState);
        Keyboard.enableRepeatEvents(true);
    }
    
    private void addElement(final SCModule module) {
        final int minWidth = 90;
        int margin = (this.getMainWidth() / 6 < 70) ? 10 : (this.getMainWidth() / 18);
        margin = 15;
        final LayoutBuilder lb = new LayoutBuilder(this.width / 2 - this.getWidth() / 2 + margin, this.getLayoutWidth(margin), this.gap, this.columns);
        final int boxWidth = Math.max(90, lb.getSplittedWidth());
        final int boxHeight = 30;
        final int x = lb.getCoordinateForIndex(this.column - 1);
        final int y = (int)this.getRowHeight(this.row, 40);
        this.elements.add(new ElementModule(x + 1, y + 1, boxWidth, 30, module, button -> {
            if (this.elements.stream().filter(element -> element != button).noneMatch(element -> element.hovered)) {
                if (!module.isServerDisabled()) {
                    ModuleConfig.INSTANCE.setEnabled(module, !ModuleConfig.INSTANCE.isEnabled(module));
                    if (ModuleConfig.INSTANCE.isEnabled(module)) {
                        module.onEnable();
                    }
                    else {
                        module.onDisable();
                    }
                }
            }
            return;
        }));
        module.settings.size();
        this.elements.add(new ElementModuleSettings2(x + boxWidth - 10 - 3, y + 3, 10, 10, "icons/settings.png", 10, true, module, b -> this.nextGui(new GuiModuleSettings(module, this))));
        ++this.column;
        if (this.column > this.columns) {
            this.column = 1;
            ++this.row;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        MoonsenseClient.titleRendererLarge.drawString("Mods", this.width / 2 - this.getWidth() / 2 + 12, this.height / 2 - this.getHeight() / 2 - 16, MoonsenseClient.getMainColor(255));
        GuiModules.scrollState = this.getScrollState();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int x = (this.width / 2 - this.getWidth() / 2) * sr.getScaleFactor();
        final int y = (this.height / 2 - this.getHeight() / 2 + 1) * sr.getScaleFactor();
        final int xWidth = (this.width / 2 + this.getWidth() / 2) * sr.getScaleFactor() - x;
        final int yHeight = (this.height / 2 + this.getHeight() / 2) * sr.getScaleFactor() - y;
        GL11.glEnable(3089);
        this.scissorFunc(sr, x, y - 40, xWidth, yHeight + 10);
        GL11.glDisable(3089);
        if (this.sortBy == ModuleCategory.ALL) {
            final int xx = this.sortAll.xPosition;
            final int yy = this.sortAll.yPosition;
            GuiUtils.drawRoundedRect((float)xx, (float)yy, (float)(xx + 50), (float)(yy + 10), 2.0f, new Color(105, 116, 122, 100).brighter().getRGB());
        }
        else if (this.sortBy == ModuleCategory.NEW) {
            final int xx = this.sortNew.xPosition;
            final int yy = this.sortNew.yPosition;
            GuiUtils.drawRoundedRect((float)xx, (float)yy, (float)(xx + 50), (float)(yy + 10), 2.0f, new Color(105, 116, 122, 100).brighter().getRGB());
        }
        else if (this.sortBy == ModuleCategory.HUD) {
            final int xx = this.sortHud.xPosition;
            final int yy = this.sortHud.yPosition;
            GuiUtils.drawRoundedRect((float)xx, (float)yy, (float)(xx + 50), (float)(yy + 10), 2.0f, new Color(105, 116, 122, 100).brighter().getRGB());
        }
        else if (this.sortBy == ModuleCategory.SERVER) {
            final int xx = this.sortServer.xPosition;
            final int yy = this.sortServer.yPosition;
            GuiUtils.drawRoundedRect((float)xx, (float)yy, (float)(xx + 50), (float)(yy + 10), 2.0f, new Color(105, 116, 122, 100).brighter().getRGB());
        }
        else if (this.sortBy == ModuleCategory.MECHANIC) {
            final int xx = this.sortMechanic.xPosition;
            final int yy = this.sortMechanic.yPosition;
            GuiUtils.drawRoundedRect((float)xx, (float)yy, (float)(xx + 50), (float)(yy + 10), 2.0f, new Color(105, 116, 122, 100).brighter().getRGB());
        }
        this.sortAll.drawButton(this.mc, mouseX, mouseY);
        this.sortNew.drawButton(this.mc, mouseX, mouseY);
        this.sortHud.drawButton(this.mc, mouseX, mouseY);
        this.sortServer.drawButton(this.mc, mouseX, mouseY);
        this.sortMechanic.drawButton(this.mc, mouseX, mouseY);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        ModuleConfig.INSTANCE.saveConfig();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.search.isFocused()) {
            this.initGui(true);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.sortAll.isMouseOver()) {
            this.sortAll.focused = true;
            this.sortNew.focused = false;
            this.sortHud.focused = false;
            this.sortMechanic.focused = false;
            this.sortServer.focused = false;
            this.sortBy = ModuleCategory.ALL;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.initGui(true);
        }
        if (this.sortNew.isMouseOver()) {
            this.sortAll.focused = false;
            this.sortNew.focused = true;
            this.sortHud.focused = false;
            this.sortMechanic.focused = false;
            this.sortServer.focused = false;
            this.sortBy = ModuleCategory.NEW;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.initGui(true);
        }
        if (this.sortHud.isMouseOver()) {
            this.sortAll.focused = false;
            this.sortNew.focused = false;
            this.sortHud.focused = true;
            this.sortMechanic.focused = false;
            this.sortServer.focused = false;
            this.sortBy = ModuleCategory.HUD;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.initGui(true);
        }
        if (this.sortMechanic.isMouseOver()) {
            this.sortAll.focused = false;
            this.sortNew.focused = false;
            this.sortHud.focused = false;
            this.sortMechanic.focused = true;
            this.sortServer.focused = false;
            this.sortBy = ModuleCategory.MECHANIC;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.initGui(true);
        }
        if (this.sortServer.isMouseOver()) {
            this.sortAll.focused = false;
            this.sortNew.focused = false;
            this.sortHud.focused = false;
            this.sortMechanic.focused = false;
            this.sortServer.focused = true;
            this.sortBy = ModuleCategory.SERVER;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.initGui(true);
        }
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2 - this.getHeight() / 2 + 20 + row * (buttonHeight + 10);
    }
    
    private int getLayoutWidth(final int margin) {
        return this.width / 2 + this.getWidth() / 2 - margin - (this.width / 2 - this.getWidth() / 2 + margin);
    }
    
    private int getLayoutHeight(final int margin) {
        return this.height / 2 + this.getHeight() / 2 + margin - (this.height / 2 - this.getHeight() / 2 - margin);
    }
    
    private int getMainWidth() {
        return this.width / 2 + this.getWidth() / 2 - (this.width / 2 - this.getWidth() / 2 + 16);
    }
    
    public static class Scroll extends AbstractGuiScrolling
    {
        private final Set<?> list;
        private final int scrollbarX;
        private final int columns;
        private int expandedWidth;
        private int extra;
        
        public Scroll(final Set<?> list, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn, final int scrollbarX, final int columns, final int extra) {
            super(Minecraft.getMinecraft(), width, height, topIn, bottomIn, slotHeightIn, extra);
            this.list = list;
            this.scrollbarX = scrollbarX;
            this.columns = columns;
            this.extra = extra;
        }
        
        public void expandBy(final int expandBy) {
            this.expandedWidth += expandBy;
        }
        
        @Override
        protected int getContentHeight() {
            return super.getContentHeight() + this.expandedWidth + this.extra;
        }
        
        public int getScrollBarX() {
            return this.scrollbarX;
        }
        
        @Override
        public void drawScroll(final int i, final int j) {
            final int j2 = this.func_148135_f();
            if (j2 > 0) {
                int height = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                height = MathHelper.clamp_int(height, 32, this.bottom - this.top - 8);
                height -= (int)Math.min((this.amountScrolled < 0.0) ? ((double)(int)(-this.amountScrolled)) : ((double)((this.amountScrolled > this.func_148135_f()) ? ((int)this.amountScrolled - this.func_148135_f()) : 0)), height * 0.75);
                final int minY = Math.min(Math.max(this.getAmountScrolled() * (this.bottom - this.top - height) / this.func_148135_f() + this.top, this.top), this.bottom - height);
                final Color c = new Color(255, 255, 255, 255);
                GuiUtils.drawRoundedRect((float)(j - 5), (float)this.top, (float)(j - 5 + 3), (float)(this.bottom - 1), 1.5f, new Color(0, 0, 0, 100).getRGB());
                GuiUtils.drawRoundedRect((float)(j - 5), (float)minY, (float)(j - 5 + 3), (float)(minY + height - 1), 1.5f, MoonsenseClient.getMainColor(150));
                GuiUtils.drawRoundedRect((float)(j - 5), (float)minY, j - 5 + 2.75f, minY + height - 1.25f, 1.5f, MoonsenseClient.getMainColor(255));
            }
            this.func_148142_b(this.mouseX, this.mouseY);
        }
        
        @Override
        public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
            super.drawScreen(mouseXIn, mouseYIn, p_148128_3_);
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
        }
        
        @Override
        protected int getSize() {
            final boolean isModuleGui = this.list.size() == ModuleManager.INSTANCE.modules.size();
            int listSize = this.list.size();
            if (isModuleGui) {
                for (final Object o : this.list) {
                    if (((SCModule)o).isChildModule()) {
                        --listSize;
                    }
                }
            }
            if (listSize % this.columns != 0) {
                return 1 + (int)Math.ceil(listSize / this.columns) + this.extra;
            }
            return listSize / this.columns + this.extra;
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
        }
    }
}
