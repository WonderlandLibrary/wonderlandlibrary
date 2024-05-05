// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop;

import vip.Resolute.util.render.Colors;
import java.awt.Color;

public enum Palette
{
    DEFAULT(new Color(0, 0, 0, 200), new Color(-13619152).darker(), new Color(-13619152), new Color(-14474461).darker(), new Color(-14079703), new Color(Colors.BLUE), new Color(-13619152), new Color(16777215)), 
    PINK(new Color(0, 0, 0, 200), new Color(-2139062144, true), new Color(0, 0, 0, 200), new Color(-2139062144, true), new Color(Colors.PINK), new Color(Colors.PINK), new Color(-1), new Color(-1));
    
    private final Color panelBackgroundColor;
    private final Color hoveredBackgroundColor;
    private final Color secondaryBackgroundColor;
    private final Color hoveredSecondaryBackgroundColor;
    private final Color panelHeaderColor;
    private final Color enabledModuleColor;
    private final Color disabledModuleColor;
    private final Color panelHeaderTextColor;
    
    private Palette(final Color panelBackgroundColor, final Color hoveredBackgroundColor, final Color secondaryBackgroundColor, final Color hoveredSecondaryBackgroundColor, final Color panelHeaderColor, final Color enabledModuleColor, final Color disabledModuleColor, final Color panelHeaderTextColor) {
        this.panelBackgroundColor = panelBackgroundColor;
        this.hoveredBackgroundColor = hoveredBackgroundColor;
        this.secondaryBackgroundColor = secondaryBackgroundColor;
        this.hoveredSecondaryBackgroundColor = hoveredSecondaryBackgroundColor;
        this.panelHeaderColor = panelHeaderColor;
        this.enabledModuleColor = enabledModuleColor;
        this.disabledModuleColor = disabledModuleColor;
        this.panelHeaderTextColor = panelHeaderTextColor;
    }
    
    public Color getHoveredSecondaryBackgroundColor() {
        return this.hoveredSecondaryBackgroundColor;
    }
    
    public Color getHoveredBackgroundColor() {
        return this.hoveredBackgroundColor;
    }
    
    public Color getSecondaryBackgroundColor() {
        return this.secondaryBackgroundColor;
    }
}
