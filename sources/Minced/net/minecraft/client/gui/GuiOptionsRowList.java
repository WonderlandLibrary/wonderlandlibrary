// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;
import java.util.List;

public class GuiOptionsRowList extends GuiListExtended
{
    private final List<Row> options;
    
    public GuiOptionsRowList(final Minecraft mcIn, final int p_i45015_2_, final int p_i45015_3_, final int p_i45015_4_, final int p_i45015_5_, final int p_i45015_6_, final GameSettings.Options... p_i45015_7_) {
        super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        this.options = (List<Row>)Lists.newArrayList();
        this.centerListVertically = false;
        for (int i = 0; i < p_i45015_7_.length; i += 2) {
            final GameSettings.Options gamesettings$options = p_i45015_7_[i];
            final GameSettings.Options gamesettings$options2 = (i < p_i45015_7_.length - 1) ? p_i45015_7_[i + 1] : null;
            final GuiButton guibutton = this.createButton(mcIn, p_i45015_2_ / 2 - 155, 0, gamesettings$options);
            final GuiButton guibutton2 = this.createButton(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, gamesettings$options2);
            this.options.add(new Row(guibutton, guibutton2));
        }
    }
    
    private GuiButton createButton(final Minecraft mcIn, final int p_148182_2_, final int p_148182_3_, final GameSettings.Options options) {
        if (options == null) {
            return null;
        }
        final int i = options.getOrdinal();
        return options.isFloat() ? new GuiOptionSlider(i, p_148182_2_, p_148182_3_, options) : new GuiOptionButton(i, p_148182_2_, p_148182_3_, options, mcIn.gameSettings.getKeyBinding(options));
    }
    
    @Override
    public Row getListEntry(final int index) {
        return this.options.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.options.size();
    }
    
    @Override
    public int getListWidth() {
        return 400;
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
    
    public static class Row implements IGuiListEntry
    {
        private final Minecraft client;
        private final GuiButton buttonA;
        private final GuiButton buttonB;
        
        public Row(final GuiButton buttonAIn, final GuiButton buttonBIn) {
            this.client = Minecraft.getMinecraft();
            this.buttonA = buttonAIn;
            this.buttonB = buttonBIn;
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
            if (this.buttonA != null) {
                this.buttonA.y = y;
                this.buttonA.drawButton(this.client, mouseX, mouseY, partialTicks);
            }
            if (this.buttonB != null) {
                this.buttonB.y = y;
                this.buttonB.drawButton(this.client, mouseX, mouseY, partialTicks);
            }
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            if (this.buttonA.mousePressed(this.client, mouseX, mouseY)) {
                if (this.buttonA instanceof GuiOptionButton) {
                    this.client.gameSettings.setOptionValue(((GuiOptionButton)this.buttonA).getOption(), 1);
                    this.buttonA.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.byOrdinal(this.buttonA.id));
                }
                return true;
            }
            if (this.buttonB != null && this.buttonB.mousePressed(this.client, mouseX, mouseY)) {
                if (this.buttonB instanceof GuiOptionButton) {
                    this.client.gameSettings.setOptionValue(((GuiOptionButton)this.buttonB).getOption(), 1);
                    this.buttonB.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.byOrdinal(this.buttonB.id));
                }
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            if (this.buttonA != null) {
                this.buttonA.mouseReleased(x, y);
            }
            if (this.buttonB != null) {
                this.buttonB.mouseReleased(x, y);
            }
        }
        
        @Override
        public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
        }
    }
}
