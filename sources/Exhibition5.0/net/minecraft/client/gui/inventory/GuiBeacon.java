// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.RenderHelper;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiBeacon extends GuiContainer
{
    private static final Logger logger;
    private static final ResourceLocation beaconGuiTextures;
    private IInventory tileBeacon;
    private ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;
    private static final String __OBFID = "CL_00000739";
    
    public GuiBeacon(final InventoryPlayer p_i45507_1_, final IInventory p_i45507_2_) {
        super(new ContainerBeacon(p_i45507_1_, p_i45507_2_));
        this.tileBeacon = p_i45507_2_;
        this.xSize = 230;
        this.ySize = 219;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(this.beaconConfirmButton = new ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107));
        this.buttonList.add(new CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final int var1 = this.tileBeacon.getField(0);
        final int var2 = this.tileBeacon.getField(1);
        final int var3 = this.tileBeacon.getField(2);
        if (this.buttonsNotDrawn && var1 >= 0) {
            this.buttonsNotDrawn = false;
            for (int var4 = 0; var4 <= 2; ++var4) {
                final int var5 = TileEntityBeacon.effectsList[var4].length;
                final int var6 = var5 * 22 + (var5 - 1) * 2;
                for (int var7 = 0; var7 < var5; ++var7) {
                    final int var8 = TileEntityBeacon.effectsList[var4][var7].id;
                    final PowerButton var9 = new PowerButton(var4 << 8 | var8, this.guiLeft + 76 + var7 * 24 - var6 / 2, this.guiTop + 22 + var4 * 25, var8, var4);
                    this.buttonList.add(var9);
                    if (var4 >= var1) {
                        var9.enabled = false;
                    }
                    else if (var8 == var2) {
                        var9.func_146140_b(true);
                    }
                }
            }
            final byte var10 = 3;
            final int var5 = TileEntityBeacon.effectsList[var10].length + 1;
            final int var6 = var5 * 22 + (var5 - 1) * 2;
            for (int var7 = 0; var7 < var5 - 1; ++var7) {
                final int var8 = TileEntityBeacon.effectsList[var10][var7].id;
                final PowerButton var9 = new PowerButton(var10 << 8 | var8, this.guiLeft + 167 + var7 * 24 - var6 / 2, this.guiTop + 47, var8, var10);
                this.buttonList.add(var9);
                if (var10 >= var1) {
                    var9.enabled = false;
                }
                else if (var8 == var3) {
                    var9.func_146140_b(true);
                }
            }
            if (var2 > 0) {
                final PowerButton var11 = new PowerButton(var10 << 8 | var2, this.guiLeft + 167 + (var5 - 1) * 24 - var6 / 2, this.guiTop + 47, var2, var10);
                this.buttonList.add(var11);
                if (var10 >= var1) {
                    var11.enabled = false;
                }
                else if (var2 == var3) {
                    var11.func_146140_b(true);
                }
            }
        }
        this.beaconConfirmButton.enabled = (this.tileBeacon.getStackInSlot(0) != null && var2 > 0);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == -2) {
            this.mc.displayGuiScreen(null);
        }
        else if (button.id == -1) {
            final String var2 = "MC|Beacon";
            final PacketBuffer var3 = new PacketBuffer(Unpooled.buffer());
            var3.writeInt(this.tileBeacon.getField(1));
            var3.writeInt(this.tileBeacon.getField(2));
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(var2, var3));
            this.mc.displayGuiScreen(null);
        }
        else if (button instanceof PowerButton) {
            if (((PowerButton)button).func_146141_c()) {
                return;
            }
            final int var4 = button.id;
            final int var5 = var4 & 0xFF;
            final int var6 = var4 >> 8;
            if (var6 < 3) {
                this.tileBeacon.setField(1, var5);
            }
            else {
                this.tileBeacon.setField(2, var5);
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        for (final GuiButton var4 : this.buttonList) {
            if (var4.isMouseOver()) {
                var4.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0f;
        this.itemRender.func_180450_b(new ItemStack(Items.emerald), var4 + 42, var5 + 109);
        this.itemRender.func_180450_b(new ItemStack(Items.diamond), var4 + 42 + 22, var5 + 109);
        this.itemRender.func_180450_b(new ItemStack(Items.gold_ingot), var4 + 42 + 44, var5 + 109);
        this.itemRender.func_180450_b(new ItemStack(Items.iron_ingot), var4 + 42 + 66, var5 + 109);
        this.itemRender.zLevel = 0.0f;
    }
    
    static {
        logger = LogManager.getLogger();
        beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    }
    
    static class Button extends GuiButton
    {
        private final ResourceLocation field_146145_o;
        private final int field_146144_p;
        private final int field_146143_q;
        private boolean field_146142_r;
        private static final String __OBFID = "CL_00000743";
        
        protected Button(final int p_i1077_1_, final int p_i1077_2_, final int p_i1077_3_, final ResourceLocation p_i1077_4_, final int p_i1077_5_, final int p_i1077_6_) {
            super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
            this.field_146145_o = p_i1077_4_;
            this.field_146144_p = p_i1077_5_;
            this.field_146143_q = p_i1077_6_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
                final short var4 = 219;
                int var5 = 0;
                if (!this.enabled) {
                    var5 += this.width * 2;
                }
                else if (this.field_146142_r) {
                    var5 += this.width * 1;
                }
                else if (this.hovered) {
                    var5 += this.width * 3;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var4, this.width, this.height);
                if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o)) {
                    mc.getTextureManager().bindTexture(this.field_146145_o);
                }
                this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
            }
        }
        
        public boolean func_146141_c() {
            return this.field_146142_r;
        }
        
        public void func_146140_b(final boolean p_146140_1_) {
            this.field_146142_r = p_146140_1_;
        }
    }
    
    class CancelButton extends Button
    {
        private static final String __OBFID = "CL_00000740";
        
        public CancelButton(final int p_i1074_2_, final int p_i1074_3_, final int p_i1074_4_) {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }
    
    class ConfirmButton extends Button
    {
        private static final String __OBFID = "CL_00000741";
        
        public ConfirmButton(final int p_i1075_2_, final int p_i1075_3_, final int p_i1075_4_) {
            super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }
    
    class PowerButton extends Button
    {
        private final int field_146149_p;
        private final int field_146148_q;
        private static final String __OBFID = "CL_00000742";
        
        public PowerButton(final int p_i1076_2_, final int p_i1076_3_, final int p_i1076_4_, final int p_i1076_5_, final int p_i1076_6_) {
            super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
            this.field_146149_p = p_i1076_5_;
            this.field_146148_q = p_i1076_6_;
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            String var3 = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);
            if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id) {
                var3 += " II";
            }
            GuiBeacon.this.drawCreativeTabHoveringText(var3, mouseX, mouseY);
        }
    }
}
