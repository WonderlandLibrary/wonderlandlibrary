// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen.settings;

import moonsense.ui.elements.Element;
import moonsense.ui.elements.ElementReplay;
import moonsense.config.GeneralConfig;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import moonsense.ui.screen.AbstractGuiScrolling;
import java.util.Set;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashSet;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.elements.text.ElementModuleSearch;

public class GuiReplays extends AbstractSettingsGui
{
    private int row;
    private int gap;
    private ElementModuleSearch search;
    
    public GuiReplays(final GuiScreen parentScreen) {
        super(parentScreen);
    }
    
    @Override
    public void initGui() {
        this.initGui(false);
    }
    
    public void initGui(final boolean keepSearch) {
        this.row = 2;
        this.gap = this.getLayoutWidth(this.getMainWidth() / 6) / 8;
        this.elements.clear();
        this.components.clear();
        if (keepSearch) {
            this.elements.add(this.search);
        }
        else {
            this.elements.add(this.search = new ElementModuleSearch(this.width / 2 + this.getWidth() / 2 - 135, this.height / 2 - this.getHeight() / 2 + 2 - 11, 100, 14, this));
        }
        final LinkedHashSet<Map.Entry<String, String>> entries = new LinkedHashSet<Map.Entry<String, String>>(MoonsenseClient.INSTANCE.getReplayManager().entrySet());
        final Set<Map.Entry<String, String>> toRemove = (Set<Map.Entry<String, String>>)Sets.newLinkedHashSet();
        final Set<Map.Entry<CharSequence, V>> set;
        entries.forEach(entry -> {
            if (MoonsenseClient.getSearchPattern(this.search.getText()).matcher(entry.getKey()).find()) {
                this.addReplay(entry, this.width / 2 - this.getWidth() / 2 + 15, (int)this.getRowHeight(this.row, 17));
                ++this.row;
            }
            else {
                set.add((Map.Entry<CharSequence, V>)entry);
            }
            return;
        });
        if (keepSearch) {
            entries.removeAll(toRemove);
        }
        super.initGui();
        this.registerScroll(new GuiModules.Scroll(entries, this.width, this.height, this.height / 2 - this.getHeight() / 2 + 17, this.height / 2 + this.getHeight() / 2 + 20, 17, this.width / 2 + this.getWidth() / 2 - 4, 1, 0));
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        MoonsenseClient.titleRendererLarge.drawString("Replays", this.width / 2 - this.getWidth() / 2 + 12, this.height / 2 - this.getHeight() / 2 - 10, MoonsenseClient.getMainColor(255));
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int x = (this.width / 2 - this.getWidth() / 2) * sr.getScaleFactor();
        final int y = (this.height / 2 - this.getHeight() / 2 + 1) * sr.getScaleFactor();
        final int xWidth = (this.width / 2 + this.getWidth() / 2) * sr.getScaleFactor() - x;
        final int yHeight = (this.height / 2 + this.getHeight() / 2) * sr.getScaleFactor() - y;
        this.scissorFunc(sr, x, y - 40, xWidth, yHeight + 10);
        GL11.glDisable(3089);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.search.isFocused()) {
            this.initGui(true);
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        GeneralConfig.INSTANCE.saveConfig();
    }
    
    private double getRowHeight(double row, final int buttonHeight) {
        --row;
        return this.height / 2 - this.getHeight() / 2 + 5 + row * buttonHeight;
    }
    
    private int getLayoutWidth(final int margin) {
        return this.width / 2 + this.getWidth() / 2 - margin - (this.width / 2 - this.getWidth() / 2 + 18 + margin);
    }
    
    private int getMainWidth() {
        return this.width / 2 + this.getWidth() / 2 - (this.width / 2 - this.getWidth() / 2 + 16);
    }
    
    private void addReplay(final Map.Entry<String, String> entry, final int x, final int y) {
        Element element = null;
        element = new ElementReplay(x + 1, y + 1, this.getWidth() - 32, 12, entry, elem -> this.mc.thePlayer.sendChatMessage(entry.getValue()), this);
        element.setYOffset(0);
        this.elements.add(element);
    }
}
