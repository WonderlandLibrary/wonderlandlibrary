// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.gui;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiOptionSlider;

public class GuiOptionSliderOF extends GuiOptionSlider implements IOptionControl
{
    private GameSettings.Options option;
    
    public GuiOptionSliderOF(final int id, final int x, final int y, final GameSettings.Options option) {
        super(id, x, y, option);
        this.option = null;
        this.option = option;
    }
    
    @Override
    public GameSettings.Options getOption() {
        return this.option;
    }
}
