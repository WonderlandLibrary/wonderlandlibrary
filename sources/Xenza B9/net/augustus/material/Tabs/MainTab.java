// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.Tabs;

import java.awt.Font;
import java.awt.Color;
import net.augustus.material.Main;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.Tab;

public class MainTab extends Tab
{
    private static UnicodeFontRenderer arial24;
    private static UnicodeFontRenderer arial18;
    
    public MainTab() {
        this.name = "Main Menu";
    }
    
    @Override
    public void render(final float mouseX, final float mouseY) {
        super.render(mouseX, mouseY);
        MainTab.arial24.drawString("Welcome!", Main.windowX + Main.animListX + 50.0f, Main.windowY + 100.0f, new Color(50, 50, 50).getRGB());
    }
    
    @Override
    public void mouseClicked(final float mouseX, final float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
    
    static {
        try {
            MainTab.arial24 = new UnicodeFontRenderer(Font.createFont(0, MainTab.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(24.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            MainTab.arial18 = new UnicodeFontRenderer(Font.createFont(0, MainTab.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
