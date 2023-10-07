package net.minecraft.client.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import koks.Koks;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import koks.api.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
    private final GuiScreen field_146303_a;
    private final ServerData field_146301_f;
    private GuiTextField field_146302_g;

    public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_) {
        this.field_146303_a = p_i1031_1_;
        this.field_146301_f = p_i1031_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.field_146302_g.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        this.field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
        this.field_146302_g.setMaxStringLength(128);
        this.field_146302_g.setFocused(true);
        this.field_146302_g.setText(this.mc.gameSettings.lastServer);
        ((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.field_146302_g.getText();
        this.mc.gameSettings.saveOptions();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.field_146303_a.confirmClicked(false, 0);
            } else if (button.id == 0) {
                this.field_146301_f.serverIP = this.field_146302_g.getText();
                this.field_146303_a.confirmClicked(true, 0);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.field_146302_g.textboxKeyTyped(typedChar, keyCode)) {
            ((GuiButton) this.buttonList.get(0)).enabled = this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0;
        } else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_146302_g.mouseClicked(mouseX, mouseY, mouseButton);
    }

    final TimeHelper timeHelper = new TimeHelper();
    public static ServerData data;
    private final OldServerPinger oldServerPinger = new OldServerPinger();

    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (!mc.developerMode) {
            final Resolution resolution = Resolution.getResolution();
            final RenderUtil renderUtil = RenderUtil.getInstance();
            final int slotWidth = 200 + 85 + 120;
            final int slotY = this.height / 4 + 96 + 12 - 50;
            final long delay = 4000;
            final double percent = timeHelper.getMs() * 100D / delay;
            final double width = (resolution.getWidth() / 3D + slotWidth - 50 - 2) - (this.width / 2D - 100 - 50 + 2);
            final double right = width * percent / 100;
            if (timeHelper.hasReached(delay) || init) {
                data = new ServerData(field_146302_g.getText(), field_146302_g.getText(), false);
                final Thread thread = new Thread(() -> {
                    try {
                        data.pingToServer = -2L;
                        oldServerPinger.ping(data);
                    } catch (UnknownHostException var2) {
                        data.pingToServer = -1L;
                        data.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                    } catch (Exception var3) {
                        data.pingToServer = -1L;
                        data.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    }
                });
                thread.start();
                timeHelper.reset();
            }
            final Color start = new Color(0x1E1D1E);
            final Color end = new Color(0x161616);
            renderUtil.drawOutlineRect(this.width / 2D - 100 - 50, slotY - 2, resolution.getWidth() / 3D + slotWidth - 50, slotY + 32, 2, start.getRGB(), end.getRGB());
            renderUtil.drawRect(this.width / 2D - 100 - 50 - 2, slotY + 32, Math.max(this.width / 2D - 100 - 50 - 2, this.width / 2D - 100 - 50 + width - right), slotY + 32 + 2, Koks.getKoks().clientColor.getRGB());
            if (data != null) {
                if (data.serverMOTD != null) {
                    List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(data.serverMOTD, slotWidth);
                    for (int i = 0; i < Math.min(list.size(), 2); ++i) {
                        this.mc.fontRendererObj.drawString((String) list.get(i), this.width / 2 - 100 + 2 + 32 - 50 + 2, slotY + fontRendererObj.FONT_HEIGHT * (i + 1) + 1, 8421504);
                    }
                }
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                if (data.getBase64EncodedIconData() != null) {
                    ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence) data.getBase64EncodedIconData(), Charsets.UTF_8);
                    ByteBuf bytebuf1 = Base64.decode(bytebuf);
                    BufferedImage bufferedimage;
                    label101:
                    {
                        try {
                            bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf1));
                            Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                            Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                            break label101;
                        } catch (Throwable throwable) {
                            data.setBase64EncodedIconData((String) null);
                        } finally {
                            bytebuf.release();
                            bytebuf1.release();
                        }

                        return;
                    }
                    ResourceLocation resourceLocation = new ResourceLocation("servers/" + data.serverIP + "/icon");
                    DynamicTexture dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                    this.mc.getTextureManager().loadTexture(resourceLocation, dynamicTexture);

                    bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
                    dynamicTexture.updateDynamicTexture();

                    this.mc.getTextureManager().bindTexture(resourceLocation);
                    Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 100 - 50, slotY - 2, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
                } else {
                    this.mc.getTextureManager().bindTexture(UNKNOWN_SERVER);
                    Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 100 - 50, slotY - 2, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
                }
                int l = 5;
                int k = 0;
                if (data.pingToServer != -2) {
                    if (data.pingToServer < 0L) {
                        l = 5;
                    } else if (data.pingToServer < 150L) {
                        l = 0;
                    } else if (data.pingToServer < 300L) {
                        l = 1;
                    } else if (data.pingToServer < 600L) {
                        l = 2;
                    } else if (data.pingToServer < 1000L) {
                        l = 3;
                    } else {
                        l = 4;
                    }
                } else {
                    k = 1;
                    l = (int) (Minecraft.getSystemTime() / 100L & 7L);

                    if (l > 4) {
                        l = 8 - l;
                    }
                }

                boolean flag = data.version > 47;
                boolean flag1 = data.version < 47;
                boolean flag2 = flag || flag1;
                String s2 = flag2 ? EnumChatFormatting.DARK_RED + data.gameVersion : data.populationInfo;

                this.mc.getTextureManager().bindTexture(Gui.icons);
                Gui.drawModalRectWithCustomSizedTexture((int) (resolution.getWidth() / 3D + slotWidth - 50 - 12), slotY - 1, (float) (k * 10), (float) (176 + l * 8), 10, 8, 256.0F, 256.0F);
                drawString(fontRendererObj, s2, (int) (resolution.getWidth() / 3D + slotWidth - 50 - fontRendererObj.getStringWidth(s2) - 14), slotY, -1);
            }
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 100, 10526880);
        this.field_146302_g.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
