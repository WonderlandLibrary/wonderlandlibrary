// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiListExtended;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry
{
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE;
    private static final ITextComponent INCOMPATIBLE;
    private static final ITextComponent INCOMPATIBLE_OLD;
    private static final ITextComponent INCOMPATIBLE_NEW;
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;
    
    public ResourcePackListEntry(final GuiScreenResourcePacks resourcePacksGUIIn) {
        this.resourcePacksGUI = resourcePacksGUIIn;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float partialTicks) {
        final int i = this.getResourcePackFormat();
        if (i != 3) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawRect((float)(x - 1), (float)(y - 1), (float)(x + listWidth - 9), (float)(y + slotHeight + 1), -8978432);
        }
        this.bindResourcePackIcon();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
        String s = this.getResourcePackName();
        String s2 = this.getResourcePackDescription();
        if (this.showHoverOverlay() && (this.mc.gameSettings.touchscreen || isSelected)) {
            this.mc.getTextureManager().bindTexture(ResourcePackListEntry.RESOURCE_PACKS_TEXTURE);
            Gui.drawRect((float)x, (float)y, (float)(x + 32), (float)(y + 32), -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int j = mouseX - x;
            final int k = mouseY - y;
            if (i < 3) {
                s = ResourcePackListEntry.INCOMPATIBLE.getFormattedText();
                s2 = ResourcePackListEntry.INCOMPATIBLE_OLD.getFormattedText();
            }
            else if (i > 3) {
                s = ResourcePackListEntry.INCOMPATIBLE.getFormattedText();
                s2 = ResourcePackListEntry.INCOMPATIBLE_NEW.getFormattedText();
            }
            if (this.canMoveRight()) {
                if (j < 32) {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                }
            }
            else {
                if (this.canMoveLeft()) {
                    if (j < 16) {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 32.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 32.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                }
                if (this.canMoveUp()) {
                    if (j < 32 && j > 16 && k < 16) {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 96.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                }
                if (this.canMoveDown()) {
                    if (j < 32 && j > 16 && k > 16) {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 64.0f, 32.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 64.0f, 0.0f, 32.0f, 32.0f, 256.0f, 256.0f);
                    }
                }
            }
        }
        final int i2 = this.mc.fontRenderer.getStringWidth(s);
        if (i2 > 157) {
            s = this.mc.fontRenderer.trimStringToWidth(s, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }
        this.mc.fontRenderer.drawStringWithShadow(s, (float)(x + 32 + 2), (float)(y + 1), 16777215);
        final List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(s2, 157);
        for (int l = 0; l < 2 && l < list.size(); ++l) {
            this.mc.fontRenderer.drawStringWithShadow(list.get(l), (float)(x + 32 + 2), (float)(y + 12 + 10 * l), 8421504);
        }
    }
    
    protected abstract int getResourcePackFormat();
    
    protected abstract String getResourcePackDescription();
    
    protected abstract String getResourcePackName();
    
    protected abstract void bindResourcePackIcon();
    
    protected boolean showHoverOverlay() {
        return true;
    }
    
    protected boolean canMoveRight() {
        return !this.resourcePacksGUI.hasResourcePackEntry(this);
    }
    
    protected boolean canMoveLeft() {
        return this.resourcePacksGUI.hasResourcePackEntry(this);
    }
    
    protected boolean canMoveUp() {
        final List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        final int i = list.indexOf(this);
        return i > 0 && list.get(i - 1).showHoverOverlay();
    }
    
    protected boolean canMoveDown() {
        final List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        final int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && list.get(i + 1).showHoverOverlay();
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
        if (this.showHoverOverlay() && relativeX <= 32) {
            if (this.canMoveRight()) {
                this.resourcePacksGUI.markChanged();
                final int j = this.resourcePacksGUI.getSelectedResourcePacks().get(0).isServerPack() ? 1 : 0;
                final int l = this.getResourcePackFormat();
                if (l == 3) {
                    this.resourcePacksGUI.getListContaining(this).remove(this);
                    this.resourcePacksGUI.getSelectedResourcePacks().add(j, this);
                }
                else {
                    final String s = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
                    final String s2 = I18n.format("resourcePack.incompatible.confirm." + ((l > 3) ? "new" : "old"), new Object[0]);
                    this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                        @Override
                        public void confirmClicked(final boolean result, final int id) {
                            final List<ResourcePackListEntry> list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
                            ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
                            if (result) {
                                list2.remove(ResourcePackListEntry.this);
                                ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(j, ResourcePackListEntry.this);
                            }
                        }
                    }, s, s2, 0));
                }
                return true;
            }
            if (relativeX < 16 && this.canMoveLeft()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (relativeX > 16 && relativeY < 16 && this.canMoveUp()) {
                final List<ResourcePackListEntry> list1 = this.resourcePacksGUI.getListContaining(this);
                final int k = list1.indexOf(this);
                list1.remove(this);
                list1.add(k - 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (relativeX > 16 && relativeY > 16 && this.canMoveDown()) {
                final List<ResourcePackListEntry> list2 = this.resourcePacksGUI.getListContaining(this);
                final int i = list2.indexOf(this);
                list2.remove(this);
                list2.add(i + 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updatePosition(final int slotIndex, final int x, final int y, final float partialTicks) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
    
    public boolean isServerPack() {
        return false;
    }
    
    static {
        RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
        INCOMPATIBLE = new TextComponentTranslation("resourcePack.incompatible", new Object[0]);
        INCOMPATIBLE_OLD = new TextComponentTranslation("resourcePack.incompatible.old", new Object[0]);
        INCOMPATIBLE_NEW = new TextComponentTranslation("resourcePack.incompatible.new", new Object[0]);
    }
}
