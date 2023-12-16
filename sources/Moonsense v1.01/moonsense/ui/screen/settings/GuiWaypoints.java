// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.util.MathHelper;
import moonsense.ui.elements.Element;
import moonsense.ui.elements.waypoint.ElementOpenWaypointEdit;
import moonsense.ui.elements.waypoint.ElementWaypoint;
import moonsense.ui.utils.LayoutBuilder;
import moonsense.ui.screen.GuiWaypointAdd;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;
import moonsense.config.ModuleConfig;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.Set;
import org.lwjgl.input.Keyboard;
import moonsense.ui.screen.AbstractGuiScrolling;
import moonsense.features.modules.type.world.waypoints.WaypointManager;
import com.google.common.collect.Sets;
import java.util.Collection;
import moonsense.features.modules.type.world.waypoints.Waypoint;
import java.util.LinkedList;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.text.ElementModuleSearch;

public class GuiWaypoints extends AbstractSettingsGui
{
    private int row;
    private int column;
    private int columns;
    private int gap;
    public static int scrollState;
    private ElementModuleSearch search;
    private GuiCustomButton addWaypoint;
    
    static {
        GuiWaypoints.scrollState = 0;
    }
    
    public GuiWaypoints(final GuiScreen parentScreen) {
        super(parentScreen);
        this.columns = 2;
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
        this.addWaypoint = new GuiCustomButton(0, this.width / 2 - this.getWidth() / 2 + 90, this.height / 2 - this.getHeight() / 2 + 2 - 11, 70, 14, "Add Waypoint", false);
        final LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>(MoonsenseClient.INSTANCE.getWaypointManager());
        final Set<Waypoint> toRemove = (Set<Waypoint>)Sets.newLinkedHashSet();
        final String dim;
        final Set<Waypoint> set;
        waypoints.forEach(waypoint -> {
            dim = ((waypoint.getDimension() == 0) ? "Overworld" : ((waypoint.getDimension() == -1) ? "The Nether" : "The End"));
            if ((MoonsenseClient.getSearchPattern(this.search.getText()).matcher(waypoint.getName()).find() || MoonsenseClient.getSearchPattern(this.search.getText()).matcher(dim).find()) && WaypointManager.getWaypointWorld().equals(waypoint.getWorld())) {
                this.addElement(waypoint);
            }
            else {
                set.add(waypoint);
            }
            return;
        });
        if (keepSearch) {
            waypoints.removeAll(toRemove);
        }
        super.initGui();
        this.registerScroll(new Scroll(waypoints, this.width, this.height, this.height / 2 - this.getHeight() / 2 + 17, this.height / 2 + this.getHeight() / 2 + 20, 40, this.width / 2 + this.getWidth() / 2 - 4, this.columns, 0));
        this.setScrollState(GuiWaypoints.scrollState);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        MoonsenseClient.titleRendererLarge.drawString("Waypoints", this.width / 2 - this.getWidth() / 2 + 12, this.height / 2 - this.getHeight() / 2 - 10, MoonsenseClient.getMainColor(255));
        this.addWaypoint.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        GuiWaypoints.scrollState = this.getScrollState();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int x = (this.width / 2 - this.getWidth() / 2) * sr.getScaleFactor();
        final int y = (this.height / 2 - this.getHeight() / 2 + 1) * sr.getScaleFactor();
        final int xWidth = (this.width / 2 + this.getWidth() / 2) * sr.getScaleFactor() - x;
        final int yHeight = (this.height / 2 + this.getHeight() / 2) * sr.getScaleFactor() - y;
        GL11.glEnable(3089);
        this.scissorFunc(sr, x, y - 40, xWidth, yHeight + 10);
        GL11.glDisable(3089);
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
        if (this.addWaypoint.isMouseOver()) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiWaypointAdd(this, false));
        }
    }
    
    private void addElement(final Waypoint waypoint) {
        final int minWidth = 90;
        int margin = (this.getMainWidth() / 6 < 70) ? 10 : (this.getMainWidth() / 6);
        margin = 15;
        final LayoutBuilder lb = new LayoutBuilder(this.width / 2 - this.getWidth() / 2 + 18 + margin, this.getLayoutWidth(margin), this.gap, this.columns);
        final int boxWidth = Math.max(90, lb.getSplittedWidth());
        final int boxHeight = 25;
        final int x = lb.getCoordinateForIndex(this.column - 1) - 20;
        final int y = (int)this.getRowHeight(this.row, 35);
        this.elements.add(new ElementWaypoint(x + 1, y + 1, boxWidth, 25, waypoint, button -> {
            if (this.elements.stream().filter(element -> element != button).noneMatch(element -> element.hovered)) {
                waypoint.toggleVisibility();
            }
            return;
        }));
        this.elements.add(new ElementOpenWaypointEdit(x + boxWidth - 25, y, 25, 25, "icons/edit.png", 14, true, waypoint, element -> this.nextGui(new GuiWaypointAdd(this, waypoint, true))));
        ++this.column;
        if (this.column > this.columns) {
            this.column = 1;
            ++this.row;
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
        private final LinkedList<?> list;
        private final int scrollbarX;
        private final int columns;
        private int expandedWidth;
        private int extra;
        
        public Scroll(final LinkedList<?> list, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn, final int scrollbarX, final int columns, final int extra) {
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
            if (this.list.size() % this.columns != 0) {
                return 1 + (int)Math.ceil(this.list.size() / this.columns);
            }
            return this.list.size() / this.columns;
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
