package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiBeacon
extends GuiContainer {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    private IInventory tileBeacon;
    private ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;

    public GuiBeacon(InventoryPlayer playerInventory, IInventory tileBeaconIn) {
        super((Container)new ContainerBeacon((IInventory)playerInventory, tileBeaconIn));
        this.tileBeacon = tileBeaconIn;
        this.xSize = 230;
        this.ySize = 219;
    }

    public void initGui() {
        super.initGui();
        this.beaconConfirmButton = new ConfirmButton(this, -1, this.guiLeft + 164, this.guiTop + 107);
        this.buttonList.add((Object)this.beaconConfirmButton);
        this.buttonList.add((Object)new CancelButton(this, -2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }

    public void updateScreen() {
        super.updateScreen();
        int i = this.tileBeacon.getField(0);
        int j = this.tileBeacon.getField(1);
        int k = this.tileBeacon.getField(2);
        if (this.buttonsNotDrawn && i >= 0) {
            this.buttonsNotDrawn = false;
            for (int l = 0; l <= 2; ++l) {
                int i1 = TileEntityBeacon.effectsList[l].length;
                int j1 = i1 * 22 + (i1 - 1) * 2;
                for (int k1 = 0; k1 < i1; ++k1) {
                    int l1 = TileEntityBeacon.effectsList[l][k1].id;
                    PowerButton guibeacon$powerbutton = new PowerButton(this, l << 8 | l1, this.guiLeft + 76 + k1 * 24 - j1 / 2, this.guiTop + 22 + l * 25, l1, l);
                    this.buttonList.add((Object)guibeacon$powerbutton);
                    if (l >= i) {
                        guibeacon$powerbutton.enabled = false;
                        continue;
                    }
                    if (l1 != j) continue;
                    guibeacon$powerbutton.func_146140_b(true);
                }
            }
            int i2 = 3;
            int j2 = TileEntityBeacon.effectsList[i2].length + 1;
            int k2 = j2 * 22 + (j2 - 1) * 2;
            for (int l2 = 0; l2 < j2 - 1; ++l2) {
                int i3 = TileEntityBeacon.effectsList[i2][l2].id;
                PowerButton guibeacon$powerbutton2 = new PowerButton(this, i2 << 8 | i3, this.guiLeft + 167 + l2 * 24 - k2 / 2, this.guiTop + 47, i3, i2);
                this.buttonList.add((Object)guibeacon$powerbutton2);
                if (i2 >= i) {
                    guibeacon$powerbutton2.enabled = false;
                    continue;
                }
                if (i3 != k) continue;
                guibeacon$powerbutton2.func_146140_b(true);
            }
            if (j > 0) {
                PowerButton guibeacon$powerbutton1 = new PowerButton(this, i2 << 8 | j, this.guiLeft + 167 + (j2 - 1) * 24 - k2 / 2, this.guiTop + 47, j, i2);
                this.buttonList.add((Object)guibeacon$powerbutton1);
                if (i2 >= i) {
                    guibeacon$powerbutton1.enabled = false;
                } else if (j == k) {
                    guibeacon$powerbutton1.func_146140_b(true);
                }
            }
        }
        this.beaconConfirmButton.enabled = this.tileBeacon.getStackInSlot(0) != null && j > 0;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == -2) {
            this.mc.displayGuiScreen((GuiScreen)null);
        } else if (button.id == -1) {
            String s = "MC|Beacon";
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.tileBeacon.getField(1));
            packetbuffer.writeInt(this.tileBeacon.getField(2));
            this.mc.getNetHandler().addToSendQueue((Packet)new C17PacketCustomPayload(s, packetbuffer));
            this.mc.displayGuiScreen((GuiScreen)null);
        } else if (button instanceof PowerButton) {
            if (((PowerButton)button).func_146141_c()) {
                return;
            }
            int j = button.id;
            int k = j & 0xFF;
            int i = j >> 8;
            if (i < 3) {
                this.tileBeacon.setField(1, k);
            } else {
                this.tileBeacon.setField(2, k);
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"tile.beacon.primary", (Object[])new Object[0]), 62, 10, 0xE0E0E0);
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"tile.beacon.secondary", (Object[])new Object[0]), 169, 10, 0xE0E0E0);
        for (GuiButton guibutton : this.buttonList) {
            if (!guibutton.isMouseOver()) continue;
            guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
            break;
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(beaconGuiTextures);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0f;
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.emerald), i + 42, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.diamond), i + 42 + 22, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.gold_ingot), i + 42 + 44, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.iron_ingot), i + 42 + 66, j + 109);
        this.itemRender.zLevel = 0.0f;
    }

    static /* synthetic */ ResourceLocation access$000() {
        return beaconGuiTextures;
    }

    static /* synthetic */ void access$100(GuiBeacon x0, String x1, int x2, int x3) {
        x0.drawCreativeTabHoveringText(x1, x2, x3);
    }

    static /* synthetic */ void access$200(GuiBeacon x0, String x1, int x2, int x3) {
        x0.drawCreativeTabHoveringText(x1, x2, x3);
    }

    static /* synthetic */ void access$300(GuiBeacon x0, String x1, int x2, int x3) {
        x0.drawCreativeTabHoveringText(x1, x2, x3);
    }
}
