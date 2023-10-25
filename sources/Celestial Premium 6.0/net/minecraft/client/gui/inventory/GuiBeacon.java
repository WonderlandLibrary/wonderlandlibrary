/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiBeacon
extends GuiContainer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation BEACON_GUI_TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
    private final IInventory tileBeacon;
    private ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;

    public GuiBeacon(InventoryPlayer playerInventory, IInventory tileBeaconIn) {
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
        int i = this.tileBeacon.getField(0);
        Potion potion = Potion.getPotionById(this.tileBeacon.getField(1));
        Potion potion1 = Potion.getPotionById(this.tileBeacon.getField(2));
        if (this.buttonsNotDrawn && i >= 0) {
            this.buttonsNotDrawn = false;
            int j = 100;
            for (int k = 0; k <= 2; ++k) {
                int l = TileEntityBeacon.EFFECTS_LIST[k].length;
                int i1 = l * 22 + (l - 1) * 2;
                for (int j1 = 0; j1 < l; ++j1) {
                    Potion potion2 = TileEntityBeacon.EFFECTS_LIST[k][j1];
                    PowerButton guibeacon$powerbutton = new PowerButton(j++, this.guiLeft + 76 + j1 * 24 - i1 / 2, this.guiTop + 22 + k * 25, potion2, k);
                    this.buttonList.add(guibeacon$powerbutton);
                    if (k >= i) {
                        guibeacon$powerbutton.enabled = false;
                        continue;
                    }
                    if (potion2 != potion) continue;
                    guibeacon$powerbutton.setSelected(true);
                }
            }
            int k1 = 3;
            int l1 = TileEntityBeacon.EFFECTS_LIST[3].length + 1;
            int i2 = l1 * 22 + (l1 - 1) * 2;
            for (int j2 = 0; j2 < l1 - 1; ++j2) {
                Potion potion3 = TileEntityBeacon.EFFECTS_LIST[3][j2];
                PowerButton guibeacon$powerbutton2 = new PowerButton(j++, this.guiLeft + 167 + j2 * 24 - i2 / 2, this.guiTop + 47, potion3, 3);
                this.buttonList.add(guibeacon$powerbutton2);
                if (3 >= i) {
                    guibeacon$powerbutton2.enabled = false;
                    continue;
                }
                if (potion3 != potion1) continue;
                guibeacon$powerbutton2.setSelected(true);
            }
            if (potion != null) {
                PowerButton guibeacon$powerbutton1 = new PowerButton(j++, this.guiLeft + 167 + (l1 - 1) * 24 - i2 / 2, this.guiTop + 47, potion, 3);
                this.buttonList.add(guibeacon$powerbutton1);
                if (3 >= i) {
                    guibeacon$powerbutton1.enabled = false;
                } else if (potion == potion1) {
                    guibeacon$powerbutton1.setSelected(true);
                }
            }
        }
        this.beaconConfirmButton.enabled = !this.tileBeacon.getStackInSlot(0).isEmpty() && potion != null;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == -2) {
            this.mc.player.connection.sendPacket(new CPacketCloseWindow(this.mc.player.openContainer.windowId));
            this.mc.displayGuiScreen(null);
        } else if (button.id == -1) {
            String s = "MC|Beacon";
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.tileBeacon.getField(1));
            packetbuffer.writeInt(this.tileBeacon.getField(2));
            this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|Beacon", packetbuffer));
            this.mc.player.connection.sendPacket(new CPacketCloseWindow(this.mc.player.openContainer.windowId));
            this.mc.displayGuiScreen(null);
        } else if (button instanceof PowerButton) {
            PowerButton guibeacon$powerbutton = (PowerButton)button;
            if (guibeacon$powerbutton.isSelected()) {
                return;
            }
            int i = Potion.getIdFromPotion(guibeacon$powerbutton.effect);
            if (guibeacon$powerbutton.tier < 3) {
                this.tileBeacon.setField(1, i);
            } else {
                this.tileBeacon.setField(2, i);
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 0xE0E0E0);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 0xE0E0E0);
        for (GuiButton guibutton : this.buttonList) {
            if (!guibutton.isMouseOver()) continue;
            guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
            break;
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0f;
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.EMERALD), i + 42, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.DIAMOND), i + 42 + 22, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.GOLD_INGOT), i + 42 + 44, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.IRON_INGOT), i + 42 + 66, j + 109);
        this.itemRender.zLevel = 0.0f;
    }

    class PowerButton
    extends Button {
        private final Potion effect;
        private final int tier;

        public PowerButton(int buttonId, int x, int y, Potion effectIn, int tierIn) {
            super(buttonId, x, y, GuiContainer.INVENTORY_BACKGROUND, effectIn.getStatusIconIndex() % 8 * 18, 198 + effectIn.getStatusIconIndex() / 8 * 18);
            this.effect = effectIn;
            this.tier = tierIn;
        }

        @Override
        public void drawButtonForegroundLayer(int mouseX, int mouseY) {
            String s = I18n.format(this.effect.getName(), new Object[0]);
            if (this.tier >= 3 && this.effect != MobEffects.REGENERATION) {
                s = s + " II";
            }
            GuiBeacon.this.drawCreativeTabHoveringText(s, mouseX, mouseY);
        }
    }

    class ConfirmButton
    extends Button {
        public ConfirmButton(int buttonId, int x, int y) {
            super(buttonId, x, y, BEACON_GUI_TEXTURES, 90, 220);
        }

        @Override
        public void drawButtonForegroundLayer(int mouseX, int mouseY) {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }

    class CancelButton
    extends Button {
        public CancelButton(int buttonId, int x, int y) {
            super(buttonId, x, y, BEACON_GUI_TEXTURES, 112, 220);
        }

        @Override
        public void drawButtonForegroundLayer(int mouseX, int mouseY) {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }

    static class Button
    extends GuiButton {
        private final ResourceLocation iconTexture;
        private final int iconX;
        private final int iconY;
        private boolean selected;

        protected Button(int buttonId, int x, int y, ResourceLocation iconTextureIn, int iconXIn, int iconYIn) {
            super(buttonId, x, y, 22, 22, "");
            this.iconTexture = iconTextureIn;
            this.iconX = iconXIn;
            this.iconY = iconYIn;
        }

        @Override
        public void drawButton(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
            if (this.visible) {
                p_191745_1_.getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
                int i = 219;
                int j = 0;
                if (!this.enabled) {
                    j += this.width * 2;
                } else if (this.selected) {
                    j += this.width * 1;
                } else if (this.hovered) {
                    j += this.width * 3;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, j, 219, this.width, this.height);
                if (!BEACON_GUI_TEXTURES.equals(this.iconTexture)) {
                    p_191745_1_.getTextureManager().bindTexture(this.iconTexture);
                }
                this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.iconX, this.iconY, 18, 18);
            }
        }

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selectedIn) {
            this.selected = selectedIn;
        }
    }
}

