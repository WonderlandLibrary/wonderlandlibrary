// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.gui.components;

import vip.Resolute.util.font.MinecraftFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import vip.Resolute.ui.login.system.Account;
import com.mojang.realmsclient.gui.ChatFormatting;
import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.Resolute;
import net.minecraft.client.Minecraft;
import vip.Resolute.ui.login.gui.GuiAltManager;
import net.minecraft.client.gui.GuiSlot;

public class GuiAccountList extends GuiSlot
{
    public int selected;
    private GuiAltManager parent;
    
    public GuiAccountList(final GuiAltManager parent) {
        super(Minecraft.getMinecraft(), GuiAltManager.width, GuiAltManager.height, 36, GuiAltManager.height - 56, 40);
        this.selected = -1;
        this.parent = parent;
    }
    
    public int getSize() {
        return Resolute.getAccountManager().getAccounts().size();
    }
    
    public void elementClicked(final int i, final boolean b, final int i1, final int i2) {
        this.selected = i;
        if (b) {
            this.parent.login(this.getAccount(i));
        }
    }
    
    @Override
    protected boolean isSelected(final int i) {
        return i == this.selected;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int i, final int i1, final int i2, final int i3, final int i4, final int i5) {
        final Account account = this.getAccount(i);
        final Minecraft minecraft = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        final FontRenderer fontRenderer = minecraft.fontRendererObj;
        final int x = i1 + 2;
        final int y = i2;
        if (y >= scaledResolution.getScaledHeight() || y < 0) {
            return;
        }
        GL11.glTranslated((double)x, (double)y, 0.0);
        RenderUtils.drawRect(0.0, 6.0, 24.0, 24.0, 3);
        final MinecraftFontRenderer fr = FontUtil.moon;
        fr.drawStringWithShadow(account.getName(), 30.0, 6.0f, -1);
        fr.drawStringWithShadow(ChatFormatting.GRAY + account.getEmail(), 30.0, (float)(6 + fontRenderer.FONT_HEIGHT + 2), -1);
        GL11.glTranslated((double)(-x), (double)(-y), 0.0);
    }
    
    public Account getAccount(final int i) {
        return Resolute.getAccountManager().getAccounts().get(i);
    }
    
    public void removeSelected() {
        if (this.selected == -1) {
            return;
        }
        Resolute.getAccountManager().getAccounts().remove(this.getAccount(this.selected));
        Resolute.getAccountManager().save();
    }
    
    public Account getSelectedAccount() {
        return this.getAccount(this.selected);
    }
}
