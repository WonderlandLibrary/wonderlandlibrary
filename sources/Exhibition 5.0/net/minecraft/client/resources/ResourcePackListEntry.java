// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiListExtended;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry
{
    private static final ResourceLocation field_148316_c;
    protected final Minecraft field_148317_a;
    protected final GuiScreenResourcePacks field_148315_b;
    private static final String __OBFID = "CL_00000821";
    
    public ResourcePackListEntry(final GuiScreenResourcePacks p_i45051_1_) {
        this.field_148315_b = p_i45051_1_;
        this.field_148317_a = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        this.func_148313_c();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        if ((this.field_148317_a.gameSettings.touchscreen || isSelected) && this.func_148310_d()) {
            this.field_148317_a.getTextureManager().bindTexture(ResourcePackListEntry.field_148316_c);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int var9 = mouseX - x;
            final int var10 = mouseY - y;
            if (this.func_148309_e()) {
                if (var9 < 32) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            else {
                if (this.func_148308_f()) {
                    if (var9 < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148314_g()) {
                    if (var9 < 32 && var9 > 16 && var10 < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148307_h()) {
                    if (var9 < 32 && var9 > 16 && var10 > 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
            }
        }
        String var11 = this.func_148312_b();
        final int var10 = this.field_148317_a.fontRendererObj.getStringWidth(var11);
        if (var10 > 157) {
            var11 = this.field_148317_a.fontRendererObj.trimStringToWidth(var11, 157 - this.field_148317_a.fontRendererObj.getStringWidth("...")) + "...";
        }
        this.field_148317_a.fontRendererObj.drawStringWithShadow(var11, x + 32 + 2, y + 1, 16777215);
        final List var12 = this.field_148317_a.fontRendererObj.listFormattedStringToWidth(this.func_148311_a(), 157);
        for (int var13 = 0; var13 < 2 && var13 < var12.size(); ++var13) {
            this.field_148317_a.fontRendererObj.drawStringWithShadow(var12.get(var13), x + 32 + 2, y + 12 + 10 * var13, 8421504);
        }
    }
    
    protected abstract String func_148311_a();
    
    protected abstract String func_148312_b();
    
    protected abstract void func_148313_c();
    
    protected boolean func_148310_d() {
        return true;
    }
    
    protected boolean func_148309_e() {
        return !this.field_148315_b.hasResourcePackEntry(this);
    }
    
    protected boolean func_148308_f() {
        return this.field_148315_b.hasResourcePackEntry(this);
    }
    
    protected boolean func_148314_g() {
        final List var1 = this.field_148315_b.func_146962_b(this);
        final int var2 = var1.indexOf(this);
        return var2 > 0 && var1.get(var2 - 1).func_148310_d();
    }
    
    protected boolean func_148307_h() {
        final List var1 = this.field_148315_b.func_146962_b(this);
        final int var2 = var1.indexOf(this);
        return var2 >= 0 && var2 < var1.size() - 1 && var1.get(var2 + 1).func_148310_d();
    }
    
    @Override
    public boolean mousePressed(final int p_148278_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        if (this.func_148310_d() && p_148278_5_ <= 32) {
            if (this.func_148309_e()) {
                this.field_148315_b.func_146962_b(this).remove(this);
                this.field_148315_b.func_146963_h().add(0, this);
                this.field_148315_b.func_175288_g();
                return true;
            }
            if (p_148278_5_ < 16 && this.func_148308_f()) {
                this.field_148315_b.func_146962_b(this).remove(this);
                this.field_148315_b.func_146964_g().add(0, this);
                this.field_148315_b.func_175288_g();
                return true;
            }
            if (p_148278_5_ > 16 && p_148278_6_ < 16 && this.func_148314_g()) {
                final List var7 = this.field_148315_b.func_146962_b(this);
                final int var8 = var7.indexOf(this);
                var7.remove(this);
                var7.add(var8 - 1, this);
                this.field_148315_b.func_175288_g();
                return true;
            }
            if (p_148278_5_ > 16 && p_148278_6_ > 16 && this.func_148307_h()) {
                final List var7 = this.field_148315_b.func_146962_b(this);
                final int var8 = var7.indexOf(this);
                var7.remove(this);
                var7.add(var8 + 1, this);
                this.field_148315_b.func_175288_g();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    static {
        field_148316_c = new ResourceLocation("textures/gui/resource_packs.png");
    }
}
