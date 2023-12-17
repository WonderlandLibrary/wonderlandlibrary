// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.init.MobEffects;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.RenderHelper;
import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.potion.Potion;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiBeacon extends GuiContainer
{
    private static final Logger LOGGER;
    private static final ResourceLocation BEACON_GUI_TEXTURES;
    private final IInventory tileBeacon;
    private ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;
    
    public GuiBeacon(final InventoryPlayer playerInventory, final IInventory tileBeaconIn) {
        super(new ContainerBeacon(playerInventory, tileBeaconIn));
        this.tileBeacon = tileBeaconIn;
        this.xSize = 230;
        this.ySize = 219;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.beaconConfirmButton = new ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107);
        this.buttonList.add(this.beaconConfirmButton);
        this.buttonList.add(new CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final int i = this.tileBeacon.getField(0);
        final Potion potion = Potion.getPotionById(this.tileBeacon.getField(1));
        final Potion potion2 = Potion.getPotionById(this.tileBeacon.getField(2));
        if (this.buttonsNotDrawn && i >= 0) {
            this.buttonsNotDrawn = false;
            int j = 100;
            for (int k = 0; k <= 2; ++k) {
                final int l = TileEntityBeacon.EFFECTS_LIST[k].length;
                final int i2 = l * 22 + (l - 1) * 2;
                for (int j2 = 0; j2 < l; ++j2) {
                    final Potion potion3 = TileEntityBeacon.EFFECTS_LIST[k][j2];
                    final PowerButton guibeacon$powerbutton = new PowerButton(j++, this.guiLeft + 76 + j2 * 24 - i2 / 2, this.guiTop + 22 + k * 25, potion3, k);
                    this.buttonList.add(guibeacon$powerbutton);
                    if (k >= i) {
                        guibeacon$powerbutton.enabled = false;
                    }
                    else if (potion3 == potion) {
                        guibeacon$powerbutton.setSelected(true);
                    }
                }
            }
            final int k2 = 3;
            final int l2 = TileEntityBeacon.EFFECTS_LIST[3].length + 1;
            final int i3 = l2 * 22 + (l2 - 1) * 2;
            for (int j3 = 0; j3 < l2 - 1; ++j3) {
                final Potion potion4 = TileEntityBeacon.EFFECTS_LIST[3][j3];
                final PowerButton guibeacon$powerbutton2 = new PowerButton(j++, this.guiLeft + 167 + j3 * 24 - i3 / 2, this.guiTop + 47, potion4, 3);
                this.buttonList.add(guibeacon$powerbutton2);
                if (3 >= i) {
                    guibeacon$powerbutton2.enabled = false;
                }
                else if (potion4 == potion2) {
                    guibeacon$powerbutton2.setSelected(true);
                }
            }
            if (potion != null) {
                final PowerButton guibeacon$powerbutton3 = new PowerButton(j++, this.guiLeft + 167 + (l2 - 1) * 24 - i3 / 2, this.guiTop + 47, potion, 3);
                this.buttonList.add(guibeacon$powerbutton3);
                if (3 >= i) {
                    guibeacon$powerbutton3.enabled = false;
                }
                else if (potion == potion2) {
                    guibeacon$powerbutton3.setSelected(true);
                }
            }
        }
        this.beaconConfirmButton.enabled = (!this.tileBeacon.getStackInSlot(0).isEmpty() && potion != null);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == -2) {
            final Minecraft mc = GuiBeacon.mc;
            final NetHandlerPlayClient connection = Minecraft.player.connection;
            final Minecraft mc2 = GuiBeacon.mc;
            connection.sendPacket(new CPacketCloseWindow(Minecraft.player.openContainer.windowId));
            GuiBeacon.mc.displayGuiScreen(null);
        }
        else if (button.id == -1) {
            final String s = "MC|Beacon";
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.tileBeacon.getField(1));
            packetbuffer.writeInt(this.tileBeacon.getField(2));
            GuiBeacon.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|Beacon", packetbuffer));
            final Minecraft mc3 = GuiBeacon.mc;
            final NetHandlerPlayClient connection2 = Minecraft.player.connection;
            final Minecraft mc4 = GuiBeacon.mc;
            connection2.sendPacket(new CPacketCloseWindow(Minecraft.player.openContainer.windowId));
            GuiBeacon.mc.displayGuiScreen(null);
        }
        else if (button instanceof PowerButton) {
            final PowerButton guibeacon$powerbutton = (PowerButton)button;
            if (guibeacon$powerbutton.isSelected()) {
                return;
            }
            final int i = Potion.getIdFromPotion(guibeacon$powerbutton.effect);
            if (guibeacon$powerbutton.tier < 3) {
                this.tileBeacon.setField(1, i);
            }
            else {
                this.tileBeacon.setField(2, i);
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRenderer, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRenderer, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton.isMouseOver()) {
                guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiBeacon.mc.getTextureManager().bindTexture(GuiBeacon.BEACON_GUI_TEXTURES);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0f;
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.EMERALD), i + 42, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.DIAMOND), i + 42 + 22, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.GOLD_INGOT), i + 42 + 44, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.IRON_INGOT), i + 42 + 66, j + 109);
        this.itemRender.zLevel = 0.0f;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BEACON_GUI_TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
    }
    
    static class Button extends GuiButton
    {
        private final ResourceLocation iconTexture;
        private final int iconX;
        private final int iconY;
        private boolean selected;
        
        protected Button(final int buttonId, final int x, final int y, final ResourceLocation iconTextureIn, final int iconXIn, final int iconYIn) {
            super(buttonId, x, y, 22, 22, "");
            this.iconTexture = iconTextureIn;
            this.iconX = iconXIn;
            this.iconY = iconYIn;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiBeacon.BEACON_GUI_TEXTURES);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
                final int i = 219;
                int j = 0;
                if (!this.enabled) {
                    j += this.width * 2;
                }
                else if (this.selected) {
                    j += this.width * 1;
                }
                else if (this.hovered) {
                    j += this.width * 3;
                }
                this.drawTexturedModalRect(this.x, this.y, j, 219, this.width, this.height);
                if (!GuiBeacon.BEACON_GUI_TEXTURES.equals(this.iconTexture)) {
                    mc.getTextureManager().bindTexture(this.iconTexture);
                }
                this.drawTexturedModalRect(this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
            }
        }
        
        public boolean isSelected() {
            return this.selected;
        }
        
        public void setSelected(final boolean selectedIn) {
            this.selected = selectedIn;
        }
    }
    
    class CancelButton extends Button
    {
        public CancelButton(final int buttonId, final int x, final int y) {
            super(buttonId, x, y, GuiBeacon.BEACON_GUI_TEXTURES, 112, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiBeacon.this.drawHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }
    
    class ConfirmButton extends Button
    {
        public ConfirmButton(final int buttonId, final int x, final int y) {
            super(buttonId, x, y, GuiBeacon.BEACON_GUI_TEXTURES, 90, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiBeacon.this.drawHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }
    
    class PowerButton extends Button
    {
        private final Potion effect;
        private final int tier;
        
        public PowerButton(final int buttonId, final int x, final int y, final Potion effectIn, final int tierIn) {
            super(buttonId, x, y, GuiContainer.INVENTORY_BACKGROUND, effectIn.getStatusIconIndex() % 8 * 18, 198 + effectIn.getStatusIconIndex() / 8 * 18);
            this.effect = effectIn;
            this.tier = tierIn;
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            String s = I18n.format(this.effect.getName(), new Object[0]);
            if (this.tier >= 3 && this.effect != MobEffects.REGENERATION) {
                s += " II";
            }
            GuiBeacon.this.drawHoveringText(s, mouseX, mouseY);
        }
    }
}
