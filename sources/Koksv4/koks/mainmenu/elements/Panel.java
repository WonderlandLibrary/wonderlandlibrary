package koks.mainmenu.elements;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import koks.Koks;
import koks.api.utils.Animation;
import koks.api.utils.TimeHelper;
import koks.mainmenu.Windows;
import koks.mainmenu.builder.WindowBuilder;
import koks.mainmenu.interfaces.Element;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.client.gui.Gui.drawRect;

public class Panel extends WindowBuilder.Window implements Element {

    private static Tab currentTab = Tab.HOME;
    private static final Animation tabAnimation = new Animation();
    private static final Animation panelScaleAnimation = new Animation();
    private static final Animation textAnimation = new Animation();
    private static final Animation addAnimation = new Animation();
    private static final Animation addResizeAnimation = new Animation();

    private static ServerList serverList;

    private static boolean changed, addingServer;
    private static int selected = -1, clicks;

    private static final TimeHelper timeHelper = new TimeHelper();

    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
    private static final OldServerPinger oldServerPinger = new OldServerPinger();
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());

    private static final Color color = new Color(Integer.MIN_VALUE);
    private static GuiTextField textField;

    public Panel(GuiScreen guiScreen) {
        super(-1, null, 350, 250, true, new DrawnContent() {
            @Override
            public void draw(int mouseX, int mouseY, int x, int y, int width, int height) {
                window.maxScroll = 0;
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                renderUtil.drawOutlineRect(window.x, window.y, window.x + window.width, window.y + window.height, 1F, outlineColor.getRGB(), insideColor.getRGB());
                final boolean canInteract = window.isMouseOver(mouseX, mouseY) && Windows.isFront(window, mouseX, mouseY) && Windows.activeWindow == window;
                int indexX = 0;
                int yPos = 0;
                int size = 35;
                if (currentTab == Tab.HOME) {
                    size = 40;
                    indexX = window.width / 2 - (((Tab.values().length - 1) / 2) * size) - size / 2;
                    yPos = window.height - size;
                }

                int beforeIndexX = indexX;

                boolean canDrag = true;

                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glTranslated(x, y + size, 1);
                if (window.currentScreen != null) {
                    window.currentScreen.width = width;
                    window.currentScreen.height = height - size;
                    window.currentScreen.drawScreen(mouseX, mouseY, Minecraft.getMinecraft().timer.renderPartialTicks);
                }
                GL11.glTranslated(-x, -(y + size), 1);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);

                for (Tab tab : Tab.values()) {
                    if (currentTab == tab && currentTab != Tab.HOME) {
                        if (tabAnimation.getX() < 0)
                            tabAnimation.setX(0);
                        if (tabAnimation.getX() > width)
                            tabAnimation.setX(width);
                        tabAnimation.setGoalX(indexX);
                        tabAnimation.setSpeed(Math.max(Math.abs(tabAnimation.getGoalX() - tabAnimation.getX()), 50));
                        drawRect((int) (x + tabAnimation.getAnimationX()), window.y + yPos, (int) (x + tabAnimation.getAnimationX() + size), window.y + yPos + size, insideColor.darker().getRGB());
                    } else if (currentTab == Tab.HOME) {
                        tabAnimation.setX(0);
                    }
                    indexX += size;
                }

                indexX = beforeIndexX;

                for (Tab tab : Tab.values()) {
                    if (currentTab != Tab.HOME || tab != Tab.HOME) {
                        renderUtil.drawPicture(window.x + indexX, window.y + yPos, size, size, new ResourceLocation("koks/textures/icons/mainmenu/windows/" + tab.texture + ".png"));
                        indexX += size;
                        if (mouseX >= window.x + indexX - size && mouseX <= window.x + indexX && mouseY >= window.y + yPos && mouseY <= window.y + yPos + size && Mouse.isButtonDown(0) && canInteract) {
                            canDrag = false;
                            currentTab = tab;
                            changed = true;
                            addingServer = false;
                            if (tab == Tab.HOME) {
                                textAnimation.setY(0);
                            }
                        }
                    }
                }

                if (currentTab != Tab.HOME)
                    y += size;

                if (panelScaleAnimation.isFinished()) {
                    if (currentTab == Tab.HOME) {
                        textAnimation.setGoalY(Math.abs((window.y + 60 / 2F) - (window.y + height / 2F)));
                    }
                }

                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                switch (currentTab) {
                    case HOME -> {
                        if (changed) {
                            window.displayScreen(null);
                        }
                        panelScaleAnimation.setGoalX(350);
                        panelScaleAnimation.setGoalY(200);
                        textAnimation.setSpeed(100);
                        if (panelScaleAnimation.isFinished())
                            titleFont.drawCenteredString("§bWelcome back " + Koks.getKoks().clManager.getUser().getName() + "!", x + width / 2F, y + 60 / 2F + textAnimation.getAnimationY(), Color.white, true);
                        else
                            textAnimation.setY(textAnimation.getGoalY() - titleFont.getStringHeight("W"));
                        renderUtil.drawPicture(x + width / 2 - 150 / 2, y + 60 / 2, 150, 60, new ResourceLocation("koks/textures/icons/logo.png"));
                    }
                    case SINGLE_PLAYER -> {

                    }
                    case MULTIPLAYER -> {
                        renderUtil.scissor(window.x, window.y + size, window.x + width, window.y + height + 2);
                        if (changed) {
                            serverList.loadServerList();
                            window.makeScrollable();
                        }
                        int indexY = 0;
                        int index = 0;
                        int toRemove = -1;
                        for (ServerData data : serverList.servers) {
                            int yData = y + indexY;
                            if (yData >= window.y && yData <= window.y + height) {
                                field_148302_b.submit(() -> {
                                    try {
                                        if (data.serverMOTD == null)
                                            oldServerPinger.ping(data);
                                    } catch (UnknownHostException var2) {
                                        data.pingToServer = -1L;
                                        data.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                                    } catch (Exception var3) {
                                        data.pingToServer = -1L;
                                        data.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                                    }
                                });
                                final boolean hover = mouseX >= x && mouseX <= x + width && mouseY > yData && mouseY < yData + 30 && mouseY >= window.y + size && window.isMouseOver(mouseX, mouseY) && Windows.isFront(window, mouseX, mouseY);
                                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                drawRect(x, yData, x + width, yData + 30, hover ? color.darker().getRGB() : Integer.MIN_VALUE);

                                if (hover) {
                                    canDrag = false;
                                    final boolean hoverX = mouseX >= x + width - closeFont.getStringWidth(window.closeChar) * 2 - 1 && mouseX <= x + width && mouseY >= y + indexY + 15 - closeFont.getStringHeight(window.closeChar) / 2 && mouseY <= y + indexY + 15 + closeFont.getStringHeight(window.closeChar) / 2;
                                    closeFont.drawCenteredString(window.closeChar, x + width - closeFont.getStringWidth(window.closeChar) - 1, y + indexY + 15, hoverX ? Color.red : Color.white, true);
                                    if (hoverX) {
                                        if (Mouse.isButtonDown(0) && canInteract) {
                                            toRemove = index;
                                        }
                                    } else {
                                        while (Mouse.next()) {
                                            int i = Mouse.getEventButton();

                                            if (i == 0 && clicks < 2) {
                                                clicks++;
                                                if (selected == index && clicks == 2) {
                                                    this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, data));
                                                    selected = -1;
                                                    clicks = 0;
                                                    return;
                                                } else if (selected != index)
                                                    clicks = 1;
                                                selected = index;
                                            } else {
                                                selected = -1;
                                            }
                                        }
                                    }
                                }

                                final String serverMOTD = data.serverMOTD != null ? data.serverMOTD : "Pinging...";

                                List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(serverMOTD, window.width - 25 - 40);
                                int dataHeight = Math.min(list.size(), 2) * mc.fontRendererObj.FONT_HEIGHT;
                                for (int i = 0; i < Math.min(list.size(), 2); ++i) {
                                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                    this.mc.fontRendererObj.drawString((String) list.get(i), x + 40, yData + mc.fontRendererObj.FONT_HEIGHT * i + dataHeight / 2 - 2, 8421504);
                                }

                                //mc.fontRendererObj.drawString(data.serverMOTD, x + 3, yData, -1);
                                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                GlStateManager.enableBlend();
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
                                    Gui.drawModalRectWithCustomSizedTexture(x + 2, yData + 2, 0.0F, 0.0F, 25, 25, 25.0F, 25.0F);
                                } else {
                                    this.mc.getTextureManager().bindTexture(UNKNOWN_SERVER);
                                    Gui.drawModalRectWithCustomSizedTexture(x + 2, yData + 2, 0.0F, 0.0F, 25, 25, 25.0F, 25.0F);
                                }
                                GlStateManager.disableBlend();
                            }
                            indexY += 30;
                            index++;
                        }

                        if (toRemove != -1) {
                            serverList.removeServerData(toRemove);
                            serverList.saveServerList();
                            serverList.loadServerList();
                        }

                        if (!addingServer)
                            addAnimation.setGoalY(15);
                        else
                            addAnimation.setGoalY(30);

                        addAnimation.setSpeed(150);
                        int currentSize = (int) addAnimation.getAnimationY();
                        boolean hover = mouseX >= x && mouseX <= x + width && mouseY > y + indexY && mouseY < y + indexY + currentSize;
                        drawRect(x, y + indexY, x + width, y + indexY + currentSize, hover ? color.darker().getRGB() : Integer.MIN_VALUE);

                        final boolean beforeAddingServer = addingServer;

                        if (hover && Mouse.isButtonDown(0) && addAnimation.isFinished() && canInteract) {
                            if (!addingServer) {
                                addingServer = true;
                            } else {
                                if (mouseX > textField.xPosition + textField.width + 2) {
                                    if (!textField.getText().isEmpty()) {
                                        addServer();
                                    }
                                    addingServer = false;
                                } else {
                                    textField.setFocused(true);
                                }
                            }
                        } else if (Mouse.isButtonDown(0)) {
                            textField.setFocused(false);
                        }

                        if (addAnimation.isFinished() && addingServer && hover && !textField.isFocused()) {
                            if (mouseX < textField.xPosition + textField.width + 2) {
                                textField.setFocused(true);
                            }
                        }

                        if (addingServer) {
                            textField.xPosition = x + 5;
                            textField.yPosition = y + indexY + 5;
                            textField.width = window.width - 40;
                            textField.height = currentSize - 10;
                            textField.drawTextBox();
                            final int dist = Math.abs((textField.xPosition + textField.width) - (x + width));
                            infoFont.drawCenteredString("+", (x + width) - dist / 2F, y + indexY + currentSize / 2F, Color.white, true);
                        }

                        if (addAnimation.isFinished() && !addingServer && addAnimation.getGoalY() == 15) {
                            if (hover) {
                                addResizeAnimation.setGoalX(110);
                            } else {
                                addResizeAnimation.setGoalX(80);
                            }
                            addResizeAnimation.setSpeed(150);
                            final float infoX = x + width / 2F;
                            final float infoY = y + indexY + currentSize / 2F;

                            final float scale = addResizeAnimation.getAnimationX() / 100;
                            GlStateManager.scale(scale, scale, 1);
                            infoFont.drawCenteredString("+", infoX / scale, infoY / scale, Color.white, true);
                            GlStateManager.scale(1 / scale, 1 / scale, 1);
                        }

                        window.maxScroll = serverList.servers.size() * 30 + currentSize - (height - size) - 2;
                        if (!addAnimation.isFinished())
                            window.scrollY = (int) (-window.maxScroll);
                    }
                    case ALT_MANAGER -> {
                    }
                    case SETTINGS -> {
                        panelScaleAnimation.setGoalX(350);
                        panelScaleAnimation.setGoalY(150);
                    }
                    case EXIT -> {
                        currentTab = Tab.HOME;
                        System.exit(-1);
                    }
                }

                if (canDrag && Mouse.isButtonDown(0) && window.isMouseOver(mouseX, mouseY) && canInteract) {
                    window.beginDrag(mouseX, mouseY);
                }

                panelScaleAnimation.setSpeed((float) MathHelper.clamp_float((float) Math.abs(Math.pow((panelScaleAnimation.getGoalX() - panelScaleAnimation.getX()), 2) + Math.pow((panelScaleAnimation.getGoalY() - panelScaleAnimation.getY()), 2)), 50, 150));
                window.width = (int) panelScaleAnimation.getAnimationX();
                window.height = (int) panelScaleAnimation.getAnimationY();
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                changed = false;
            }

            @Override
            public void input(char typedChar, int keyCode) {
                if (textField.isFocused()) {
                    if (keyCode == Keyboard.KEY_RETURN) {
                        if (!textField.getText().isEmpty())
                            addServer();
                        else
                            addingServer = false;
                        return;
                    } else if (keyCode == Keyboard.KEY_ESCAPE) {
                        textField.setFocused(false);
                        return;
                    }
                    textField.textboxKeyTyped(typedChar, keyCode);
                }
            }
        });
        addAnimation.setY(15);
        serverList = new ServerList(Minecraft.getMinecraft());
        draw = false;
        textField = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, 0, 0, width, height);
    }

    private static void addServer() {
        serverList.addServerData(new ServerData(textField.getText(), textField.getText(), false));
        serverList.saveServerList();
        serverList.loadServerList();
        textField.setText("");
        textField.setFocused(false);
    }

    @AllArgsConstructor
    enum Tab {
        HOME("home"), SINGLE_PLAYER("single_player"), MULTIPLAYER("multiplayer"), ALT_MANAGER("alt_manager"), SETTINGS("settings"), EXIT("exit");

        final String texture;
    }
}
