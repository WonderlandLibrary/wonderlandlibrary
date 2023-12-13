// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.augustusmanager;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class AugustusSounds extends GuiScreen
{
    private GuiScreen parent;
    public static String currentSound;
    private final ArrayList<String> sounds;
    
    public AugustusSounds(final GuiScreen parent) {
        this.sounds = new ArrayList<String>();
        this.parent = parent;
        this.sounds.add("Vanilla");
        this.sounds.add("Sigma");
    }
    
    public GuiScreen start(final GuiScreen parent) {
        this.parent = parent;
        return this;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int scaledWidth = sr.getScaledWidth();
        final int scaledHeight = sr.getScaledHeight();
        final int startHeight = Math.min(40 + scaledHeight / 7, 135);
        this.buttonList.add(new CustomButton(1, scaledWidth / 2 - 100, startHeight, 200, 20, Augustus.getInstance().getConverter().readSound(), Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(2, scaledWidth / 2 - 100, scaledHeight - scaledHeight / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glScaled(2.0, 2.0, 1.0);
        this.fontRendererObj.drawStringWithShadow("Augustus Sounds", sr.getScaledWidth() / 4.0f - this.fontRendererObj.getStringWidth("Augustus Sounds") / 2.0f, 10.0f, Color.lightGray.getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 1) {
            int id = this.sounds.indexOf(button.displayString) + 1;
            if (id >= this.sounds.size()) {
                id = 0;
            }
            button.displayString = this.sounds.get(id);
            Augustus.getInstance().getConverter().soundSaver(button.displayString);
            AugustusSounds.currentSound = button.displayString;
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && this.mc.theWorld == null) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
