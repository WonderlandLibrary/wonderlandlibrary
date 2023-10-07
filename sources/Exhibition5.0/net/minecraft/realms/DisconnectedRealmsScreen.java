// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;
import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen extends RealmsScreen
{
    private String title;
    private IChatComponent reason;
    private List lines;
    private final RealmsScreen parent;
    private static final String __OBFID = "CL_00002145";
    
    public DisconnectedRealmsScreen(final RealmsScreen p_i45742_1_, final String p_i45742_2_, final IChatComponent p_i45742_3_) {
        this.parent = p_i45742_1_;
        this.title = RealmsScreen.getLocalizedString(p_i45742_2_);
        this.reason = p_i45742_3_;
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 12, RealmsScreen.getLocalizedString("gui.back")));
        this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
    }
    
    @Override
    public void keyPressed(final char p_keyPressed_1_, final int p_keyPressed_2_) {
        if (p_keyPressed_2_ == 1) {
            Realms.setScreen(this.parent);
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton p_buttonClicked_1_) {
        if (p_buttonClicked_1_.id() == 0) {
            Realms.setScreen(this.parent);
        }
    }
    
    @Override
    public void render(final int p_render_1_, final int p_render_2_, final float p_render_3_) {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - 50, 11184810);
        int var4 = this.height() / 2 - 30;
        if (this.lines != null) {
            for (final String var6 : this.lines) {
                this.drawCenteredString(var6, this.width() / 2, var4, 16777215);
                var4 += this.fontLineHeight();
            }
        }
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
