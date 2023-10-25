/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class ResourcePackListEntry
implements GuiListExtended.IGuiListEntry {
    protected final Minecraft mc;
    private static final IChatComponent field_183020_d;
    private static final IChatComponent field_183021_e;
    private static final IChatComponent field_183022_f;
    protected final GuiScreenResourcePacks resourcePacksGUI;
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE;

    protected abstract String func_148312_b();

    @Override
    public void setSelected(int n, int n2, int n3) {
    }

    protected boolean func_148307_h() {
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int n = list.indexOf(this);
        return n >= 0 && n < list.size() - 1 && list.get(n + 1).func_148310_d();
    }

    protected abstract int func_183019_a();

    protected boolean func_148314_g() {
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int n = list.indexOf(this);
        return n > 0 && list.get(n - 1).func_148310_d();
    }

    @Override
    public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        int n8;
        int n9 = this.func_183019_a();
        if (n9 != 1) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawRect(n2 - 1, n3 - 1, n2 + n4 - 9, n3 + n5 + 1, -8978432);
        }
        this.func_148313_c();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        String string = this.func_148312_b();
        String string2 = this.func_148311_a();
        if ((Minecraft.gameSettings.touchscreen || bl) && this.func_148310_d()) {
            this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(n2, n3, n2 + 32, n3 + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            n8 = n6 - n2;
            int n10 = n7 - n3;
            if (n9 < 1) {
                string = field_183020_d.getFormattedText();
                string2 = field_183021_e.getFormattedText();
            } else if (n9 > 1) {
                string = field_183020_d.getFormattedText();
                string2 = field_183022_f.getFormattedText();
            }
            if (this.func_148309_e()) {
                if (n8 < 32) {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(n2, n3, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            } else {
                if (this.func_148308_f()) {
                    if (n8 < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 32.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 32.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148314_g()) {
                    if (n8 < 32 && n8 > 16 && n10 < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148307_h()) {
                    if (n8 < 32 && n8 > 16 && n10 > 16) {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    } else {
                        Gui.drawModalRectWithCustomSizedTexture(n2, n3, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
            }
        }
        if ((n8 = Minecraft.fontRendererObj.getStringWidth(string)) > 157) {
            string = String.valueOf(Minecraft.fontRendererObj.trimStringToWidth(string, 157 - Minecraft.fontRendererObj.getStringWidth("..."))) + "...";
        }
        Minecraft.fontRendererObj.drawStringWithShadow(string, n2 + 32 + 2, n3 + 1, 0xFFFFFF);
        List<String> list = Minecraft.fontRendererObj.listFormattedStringToWidth(string2, 157);
        int n11 = 0;
        while (n11 < 2 && n11 < list.size()) {
            Minecraft.fontRendererObj.drawStringWithShadow(list.get(n11), n2 + 32 + 2, n3 + 12 + 10 * n11, 0x808080);
            ++n11;
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    protected abstract void func_148313_c();

    protected boolean func_148309_e() {
        return !this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected abstract String func_148311_a();

    static {
        RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
        field_183020_d = new ChatComponentTranslation("resourcePack.incompatible", new Object[0]);
        field_183021_e = new ChatComponentTranslation("resourcePack.incompatible.old", new Object[0]);
        field_183022_f = new ChatComponentTranslation("resourcePack.incompatible.new", new Object[0]);
    }

    protected boolean func_148308_f() {
        return this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    @Override
    public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
        if (this.func_148310_d() && n5 <= 32) {
            if (this.func_148309_e()) {
                this.resourcePacksGUI.markChanged();
                int n7 = this.func_183019_a();
                if (n7 != 1) {
                    String string = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
                    String string2 = I18n.format("resourcePack.incompatible.confirm." + (n7 > 1 ? "new" : "old"), new Object[0]);
                    this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(){

                        @Override
                        public void confirmClicked(boolean bl, int n) {
                            List<ResourcePackListEntry> list = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
                            ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
                            if (bl) {
                                list.remove(ResourcePackListEntry.this);
                                ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, ResourcePackListEntry.this);
                            }
                        }
                    }, string, string2, 0));
                } else {
                    this.resourcePacksGUI.getListContaining(this).remove(this);
                    this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
                }
                return true;
            }
            if (n5 < 16 && this.func_148308_f()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (n5 > 16 && n6 < 16 && this.func_148314_g()) {
                List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
                int n8 = list.indexOf(this);
                list.remove(this);
                list.add(n8 - 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (n5 > 16 && n6 > 16 && this.func_148307_h()) {
                List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
                int n9 = list.indexOf(this);
                list.remove(this);
                list.add(n9 + 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
        }
        return false;
    }

    public ResourcePackListEntry(GuiScreenResourcePacks guiScreenResourcePacks) {
        this.resourcePacksGUI = guiScreenResourcePacks;
        this.mc = Minecraft.getMinecraft();
    }

    protected boolean func_148310_d() {
        return true;
    }
}

