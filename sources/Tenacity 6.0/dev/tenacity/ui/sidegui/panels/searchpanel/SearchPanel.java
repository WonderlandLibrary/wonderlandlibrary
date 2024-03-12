// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.searchpanel;

import dev.tenacity.intent.cloud.data.CloudConfig;
import dev.tenacity.intent.cloud.data.CloudScript;
import java.util.Iterator;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import java.util.Arrays;
import dev.tenacity.utils.render.StencilUtil;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.util.ArrayList;
import dev.tenacity.utils.objects.Scroll;
import dev.tenacity.ui.sidegui.panels.scriptpanel.CloudScriptRect;
import dev.tenacity.ui.sidegui.panels.configpanel.CloudConfigRect;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.ToggleButton;
import dev.tenacity.ui.sidegui.panels.Panel;

public class SearchPanel extends Panel
{
    private String searchType;
    private String searchTypeHold;
    private String searchHold;
    private final ToggleButton compactMode;
    private final List<CloudConfigRect> cloudConfigRects;
    private final List<CloudScriptRect> cloudScriptRects;
    private final Scroll searchScroll;
    
    public SearchPanel() {
        this.searchType = "";
        this.searchTypeHold = "";
        this.searchHold = "";
        this.compactMode = new ToggleButton("Compact Mode");
        this.cloudConfigRects = new ArrayList<CloudConfigRect>();
        this.cloudScriptRects = new ArrayList<CloudScriptRect>();
        this.searchScroll = new Scroll();
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        SearchPanel.tenacityFont18.drawString("Press ESC to return to the menu", this.getX() + 8.0f, this.getY() + 8.0f + SearchPanel.tenacityBoldFont40.getHeight() + 2.0f, ColorUtil.applyOpacity(this.getTextColor(), 0.3f));
        SearchPanel.tenacityBoldFont40.drawString("Search Results", this.getX() + 8.0f, this.getY() + 8.0f, this.getTextColor());
        final float spacing = 8.0f;
        final float backgroundX = this.getX() + spacing;
        final float backgroundY = this.getY() + (45.0f + spacing);
        final float backgroundWidth = this.getWidth() - spacing * 2.0f;
        final float backgroundHeight = this.getHeight() - (45.0f + spacing * 2.0f);
        RoundedUtil.drawRound(this.getX() + spacing, this.getY() + (45.0f + spacing), this.getWidth() - spacing * 2.0f, this.getHeight() - (45.0f + spacing * 2.0f), 5.0f, ColorUtil.tripleColor(27, this.getAlpha()));
        this.compactMode.setX(this.getX() + this.getWidth() - (this.compactMode.getWH() + 15.0f));
        this.compactMode.setY(this.getY() + 33.0f);
        this.compactMode.setAlpha(this.getAlpha());
        this.compactMode.drawScreen(mouseX, mouseY);
        final String search = Tenacity.INSTANCE.getSideGui().getHotbar().searchField.getText();
        if (this.check(search)) {
            return;
        }
        if (this.searchType.equals("Configs")) {
            this.drawConfigs(backgroundX, backgroundY, backgroundWidth, backgroundHeight, spacing, mouseX, mouseY, search);
        }
        else {
            this.drawScripts(backgroundX, backgroundY, backgroundWidth, backgroundHeight, spacing, mouseX, mouseY, search);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.compactMode.mouseClicked(mouseX, mouseY, button);
        if (this.searchType.equals("Configs")) {
            this.cloudConfigRects.forEach(cloudConfigRect -> cloudConfigRect.mouseClicked(mouseX, mouseY, button));
        }
        else {
            this.cloudScriptRects.forEach(cloudScriptRect -> cloudScriptRect.mouseClicked(mouseX, mouseY, button));
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public boolean check(final String search) {
        if (!this.searchHold.equals(search)) {
            this.searchScroll.setRawScroll(0.0f);
            this.searchHold = search;
        }
        if (!this.searchTypeHold.equals(this.searchType)) {
            final String searchType = this.searchType;
            switch (searchType) {
                case "Configs": {
                    this.cloudConfigRects.clear();
                    this.cloudScriptRects.clear();
                    this.cloudConfigRects.addAll(Tenacity.INSTANCE.getSideGui().getConfigPanel().getCloudConfigRects());
                    break;
                }
                case "Scripts": {
                    this.cloudConfigRects.clear();
                    this.cloudScriptRects.clear();
                    this.cloudScriptRects.addAll(Tenacity.INSTANCE.getSideGui().getScriptPanel().getCloudScriptRects());
                    break;
                }
            }
            this.searchTypeHold = this.searchType;
            return true;
        }
        return false;
    }
    
    public void setSearchType(final String searchType) {
        this.searchType = searchType;
    }
    
    public void drawScripts(final float x, final float y, final float width, final float height, final float spacing, final int mouseX, final int mouseY, final String search) {
        if (Tenacity.INSTANCE.getCloudDataManager().isRefreshing()) {
            return;
        }
        this.cloudScriptRects.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)CloudScriptRect::getSearchScore).reversed());
        final float scriptWidth = (width - 36.0f) / 3.0f;
        final float scriptHeight = this.compactMode.isEnabled() ? 38.0f : 90.0f;
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, width, height, 5.0f, ColorUtil.tripleColor(27, this.getAlpha()));
        StencilUtil.readStencilBuffer(1);
        int count = 0;
        int rectXSeparation = 0;
        int rectYSeparation = 0;
        for (final CloudScriptRect cloudScriptRect : this.cloudScriptRects) {
            final CloudScript cloudScript = cloudScriptRect.getScript();
            cloudScriptRect.setSearchScore(FuzzySearch.extractOne(search, (Collection)Arrays.asList(cloudScript.getName(), cloudScript.getAuthor())).getScore());
            cloudScriptRect.setAlpha(this.getAlpha());
            cloudScriptRect.setAccentColor(this.getAccentColor());
            if (count > 2) {
                rectXSeparation = 0;
                rectYSeparation += (int)(scriptHeight + 12.0f);
                count = 0;
            }
            cloudScriptRect.setX(x + 6.0f + rectXSeparation);
            cloudScriptRect.setY(y + 6.0f + rectYSeparation + this.searchScroll.getScroll());
            cloudScriptRect.setWidth(scriptWidth);
            cloudScriptRect.setHeight(scriptHeight);
            cloudScriptRect.setCompact(this.compactMode.isEnabled());
            if (cloudScriptRect.getY() + cloudScriptRect.getHeight() > y && cloudScriptRect.getY() < y + height) {
                cloudScriptRect.setClickable(true);
                cloudScriptRect.drawScreen(mouseX, mouseY);
            }
            else {
                cloudScriptRect.setClickable(false);
            }
            rectXSeparation += (int)(cloudScriptRect.getWidth() + 12.0f);
            ++count;
        }
        this.searchScroll.setMaxScroll((float)rectYSeparation);
        StencilUtil.uninitStencilBuffer();
    }
    
    public void drawConfigs(final float x, final float y, final float width, final float height, final float spacing, final int mouseX, final int mouseY, final String search) {
        if (Tenacity.INSTANCE.getCloudDataManager().isRefreshing()) {
            return;
        }
        this.cloudConfigRects.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)CloudConfigRect::getSearchScore).reversed());
        final float configWidth = (width - 36.0f) / 3.0f;
        final float configHeight = this.compactMode.isEnabled() ? 38.0f : 90.0f;
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, width, height, 5.0f, ColorUtil.tripleColor(27, this.getAlpha()));
        StencilUtil.readStencilBuffer(1);
        int count = 0;
        int rectXSeparation = 0;
        int rectYSeparation = 0;
        for (final CloudConfigRect cloudConfigRect : this.cloudConfigRects) {
            final CloudConfig cloudConfig = cloudConfigRect.getConfig();
            cloudConfigRect.setSearchScore(FuzzySearch.extractOne(search, (Collection)Arrays.asList(cloudConfig.getName(), cloudConfig.getServer(), cloudConfig.getAuthor())).getScore());
            cloudConfigRect.setAlpha(this.getAlpha());
            cloudConfigRect.setAccentColor(this.getAccentColor());
            if (count > 2) {
                rectXSeparation = 0;
                rectYSeparation += (int)(configHeight + 12.0f);
                count = 0;
            }
            cloudConfigRect.setX(x + 6.0f + rectXSeparation);
            cloudConfigRect.setY(y + 6.0f + rectYSeparation + this.searchScroll.getScroll());
            cloudConfigRect.setWidth(configWidth);
            cloudConfigRect.setHeight(configHeight);
            cloudConfigRect.setCompact(this.compactMode.isEnabled());
            if (cloudConfigRect.getY() + cloudConfigRect.getHeight() > y && cloudConfigRect.getY() < y + height) {
                cloudConfigRect.setClickable(true);
                cloudConfigRect.drawScreen(mouseX, mouseY);
            }
            else {
                cloudConfigRect.setClickable(false);
            }
            rectXSeparation += (int)(cloudConfigRect.getWidth() + 12.0f);
            ++count;
        }
        this.searchScroll.setMaxScroll((float)rectYSeparation);
        StencilUtil.uninitStencilBuffer();
    }
}
