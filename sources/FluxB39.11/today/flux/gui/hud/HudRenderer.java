package today.flux.gui.hud;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.BlurBuffer;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.gui.hud.Themes.*;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.gui.hud.window.HudWindow;
import today.flux.irc.IRCClient;
import today.flux.irc.ReconnectThread;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Misc.disabler.Hypixel;
import today.flux.module.implement.Render.Hud;
import today.flux.module.implement.World.AutoGG;
import today.flux.module.implement.World.Scaffold;
import today.flux.utility.*;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HudRenderer extends GuiIngame {
    private int rainbowindex = 0;
    private final DelayTimer rainbowTimer = new DelayTimer();
    private final List<java.awt.Color> rainbow = new ArrayList<>();
    private final List<Theme> themes = new ArrayList<>();

    public static java.awt.Color currentRainbow;
    public static final TabGuiRenderer tabGuiRenderer = new TabGuiRenderer();

    public static float animationY = 0;
    private float index;

    private DelayTimer delayTimer = new DelayTimer();

    //FluxSense
    public float hue = 0;

    //gaytap
    public SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public HudRenderer(Minecraft mcIn) {
        super(mcIn);

        //Add Default ColorUtils
        for (int r = 0; r < 100; r++) rainbow.add(new java.awt.Color(r * 255 / 100, 255, 0));
        for (int g = 100; g > 0; g--) rainbow.add(new java.awt.Color(255, g * 255 / 100, 0));
        for (int b = 0; b < 100; b++) rainbow.add(new java.awt.Color(255, 0, b * 255 / 100));
        for (int r = 100; r > 0; r--) rainbow.add(new java.awt.Color(r * 255 / 100, 0, 255));
        for (int g = 0; g < 100; g++) rainbow.add(new java.awt.Color(0, g * 255 / 100, 255));
        for (int b = 100; b > 0; b--) rainbow.add(new java.awt.Color(0, 255, b * 255 / 100));

        themes.add(new Rainbow());
        themes.add(new Nostalgia());
        themes.add(new ColorHud());
        themes.add(new Fade());
        themes.add(new Gradient());
        themes.add(new Rainbow2());
        themes.add(new Test());
    }

    //render UI!
    @Override
    public void renderGameOverlay(float particalTicks) {
        super.renderGameOverlay(particalTicks);
        float scale = 2;

        if (BlurBuffer.blurEnabled()) {
            BlurBuffer.updateBlurBuffer(true);
        }

        ModuleManager.killAuraMod.doRenderTargetHUD();

        GLUtils.INSTANCE.rescale(scale); //Start scale change
        float curWidth = mc.displayWidth / scale;
        float curHeight = mc.displayHeight / scale;

        //RAINBOW
        if (rainbowTimer.hasPassed(1)) {
            rainbowindex += 5;
            if (rainbowindex > rainbow.size() - 1)
                rainbowindex = 0;
            rainbowTimer.reset();

            currentRainbow = rainbow.get(rainbowindex);
        }

        if (index > 1.0f)
            index = 0.0f;

        index += 0.001f * (delayTimer.getPassed() * 0.3f);

        delayTimer.reset();

        int color = 0;
        Flux.rainbow = java.awt.Color.getHSBColor(index, Hud.arraylistColor1.getColorHSB()[1], Hud.arraylistColor1.getColorHSB()[2]);

        color = Hud.positionColour.getColorInt();
        if (Hud.Theme.isCurrentMode("Rainbow") || Hud.Theme.isCurrentMode("Rainbow2"))
            color = Flux.rainbow.getRGB();
        if (Hud.Theme.isCurrentMode("Fade"))
            color = ColorUtils.fadeBetween(Hud.arraylistColor1.getColorInt(), Hud.arraylistColor1.getColor().darker().darker().darker().getRGB());
        if(Hud.Theme.isCurrentMode("Gradient"))
            color = ColorUtils.fadeBetween(Hud.arraylistColor1.getColorInt(), Hud.arraylistColor2.getColorInt());

        String notice = "";

        if (IRCClient.hasOffline) {
            notice = String.format((IRCClient.isChina ? "Flux与服务器断开连接，已尝试重新连接%d次！" : "Flux has disconnected from the IRC server! Reconnected %d times."), ReconnectThread.times);
        }

        if (ServerUtils.INSTANCE.isOnHypixel()) {
            if (!ModuleManager.disabler.isEnabled()) {
                notice = IRCClient.isChina ? "您正在以关闭Disabler的状态游玩Hypixel!" : "You are playing on Hypixel without Disabler!";
            } else if (!Hypixel.hasDisabled) {
                if (ServerUtils.isHypixelLobby()) {
                    notice = IRCClient.isChina ? "检测到您可能在大厅，Disabler将不会生效。" : "Disabler will be disabled when you are in lobbies.";
                } else {
                    notice = IRCClient.isChina ? "Disabler未生效, 请稍等..." : "Trying to disable watchdog, please wait...";
                }
            }
        }

        if (!notice.equals("")) {
            float xStart = curWidth / 2 - FontManager.wqy18.getStringWidth(notice) / 2;
            float width = FontManager.wqy18.getStringWidth(notice) + 5;

            GuiRenderUtils.drawRect(xStart - 2, 40 - 2, width + 2, 12, new java.awt.Color(0, 0, 0, 160).getRGB());
            FontManager.wqy18.drawStringWithShadow(notice, xStart - 1, 40 - 2, new java.awt.Color(0xFEFF00).getRGB());
        }

        if (!ModuleManager.hudMod.isEnabled())
            return;

        if (mc.gameSettings.showDebugInfo)
            return;

        String theme = Hud.Theme.getValue();
        //render tabgui
        if (Hud.tabgui.getValue()) {
            GL11.glPushMatrix();
            tabGuiRenderer.render();
            GL11.glPopMatrix();
        }

        //render hud
        for (Theme item : themes) {
            if (item.getName().equalsIgnoreCase(theme)) {
                if (Hud.waterMarkMode.isCurrentMode("Text")) {
                    item.renderWatermark();
                }

                if (Hud.arraylist.getValue()) {
                    animationY = AnimationUtils.getAnimationState(animationY, ModuleManager.autoGGMod.display ? 42 : 0, (float) (Math.max(10, (Math.abs(animationY - (ModuleManager.autoGGMod.display ? 42 : 0))) * 35) * 0.3));
                    item.render(curWidth, curHeight);
                }
                break;
            }
        }

        if (Hud.waterMarkMode.isCurrentMode("Flux Sense")) {
            GL11.glPushMatrix();
            NetworkPlayerInfo info = mc.thePlayer.sendQueue.getPlayerInfo(mc.thePlayer.getUniqueID());

            if (this.hue > 255.0F) {
                this.hue = 0.0F;
            }

            float h = this.hue;
            float h2 = this.hue + 85.0F;
            float h3 = this.hue + 170.0F;

            if (h2 > 255.0F) {
                h2 -= 255.0F;
            }

            if (h3 > 255.0F) {
                h3 -= 255.0F;
            }

            java.awt.Color a = java.awt.Color.getHSBColor(h / 255.0F, 0.4F, 1.0F);
            java.awt.Color b = java.awt.Color.getHSBColor(h2 / 255.0F, 0.4F, 1.0F);
            java.awt.Color c = java.awt.Color.getHSBColor(h3 / 255.0F, 0.4F, 1.0F);
            int color1 = a.getRGB();
            int color2 = b.getRGB();
            int color3 = c.getRGB();
            this.hue = this.hue + 0.05f;
            String ping;
            ping = ServerUtils.INSTANCE.isOnHypixel() ? String.valueOf(Flux.INSTANCE.curPing) : info != null ? info.getResponseTime() + "" : "0";

            String str = "flux";
            String str2 = "sense";
            String str3 = " | " + IRCClient.loggedPacket.getRealUsername() + " | " + (mc.isSingleplayer() ? "localhost:25565" : ServerUtils.INSTANCE.isOnHypixel() ? "mc.hypixel.net:25565" : !mc.getCurrentServerData().serverIP.contains(":") ? mc.getCurrentServerData().serverIP + ":25565" : mc.getCurrentServerData().serverIP) + " | " + Minecraft.getDebugFPS() + "fps | " + ping + "ms | " + (Math.round(Flux.INSTANCE.lastTPS * 10) / 10d) + "ticks";
            ESP2D.INSTANCE.rectangle(5, 5, 14 + FontManager.tahoma13.getStringWidth(str + str2 + str3), 20, ColorUtils.getColor(30));
            ESP2D.INSTANCE.rectangleBordered(5, 5, 14 + FontManager.tahoma13.getStringWidth(str + str2 + str3), 21, 0.5d, ColorUtils.getColor(0, 0), ColorUtils.getColor(10));
            ESP2D.INSTANCE.rectangleBordered(5.5f, 5.5f, 13.5f + FontManager.tahoma13.getStringWidth(str + str2 + str3), 20.5f, 0.5d, ColorUtils.getColor(0, 0), ColorUtils.getColor(100));
            ESP2D.INSTANCE.rectangleBordered(6f, 6f, 13f + FontManager.tahoma13.getStringWidth(str + str2 + str3), 20f, 1d, ColorUtils.getColor(0, 0), ColorUtils.getColor(60));
            ESP2D.INSTANCE.rectangleBordered(7f, 7f, 12f + FontManager.tahoma13.getStringWidth(str + str2 + str3), 19f, 0.5d, ColorUtils.getColor(0, 0), ColorUtils.getColor(100));
            drawGradientSideways(7.5f, 7.5f, 12f + (FontManager.tahoma13.getStringWidth(str + str2 + str3) / 2), 8f, color1, color2);
            drawGradientSideways(12f + (FontManager.tahoma13.getStringWidth(str + str2 + str3) / 2), 7.5f, 11.5f + FontManager.tahoma13.getStringWidth(str + str2 + str3), 8f, color2, color3);

            FontManager.tahoma13.drawString(str, 10, 9f, ColorUtils.WHITE.c);
            FontManager.tahoma13.drawString(str2, 10 + FontManager.tahoma13.getStringWidth(str), 9f, Hud.watermarkColour.getColorInt());
            FontManager.tahoma13.drawString(str3, 10 + FontManager.tahoma13.getStringWidth(str + str2), 9f, ColorUtils.WHITE.c);

            FontManager.tahoma13.drawString(str, 10, 9f, ColorUtils.WHITE.c);
            FontManager.tahoma13.drawString(str2, 10 + FontManager.tahoma13.getStringWidth(str), 9f, Hud.watermarkColour.getColorInt());
            FontManager.tahoma13.drawString(str3, 10 + FontManager.tahoma13.getStringWidth(str + str2), 9f, ColorUtils.WHITE.c);

            GL11.glPopMatrix();
        } else if (Hud.waterMarkMode.isCurrentMode("Onetap")) {
            String serverip = mc.isSingleplayer() ? "localhost:25565" : ServerUtils.INSTANCE.isOnHypixel() ? "mc.hypixel.net:25565" : !mc.getCurrentServerData().serverIP.contains(":") ? mc.getCurrentServerData().serverIP + ":25565" : mc.getCurrentServerData().serverIP;
            String info = "flux | " + IRCClient.loggedPacket.getRealUsername() + " | " + Minecraft.getDebugFPS() + " fps | " + serverip + " | " + formatter.format(new Date());

            GuiRenderUtils.drawRect(5, 5, FontManager.tahoma13.getStringWidth(info) + 4, 12, new java.awt.Color(40, 40, 40));
            GuiRenderUtils.drawRoundedRect(5, 5, FontManager.tahoma13.getStringWidth(info) + 4, 2, 1, color, 1, color);
            FontManager.tahoma13.drawStringWithShadow(info, 7, 7.5f, ColorUtils.WHITE.c);
        } else if (Hud.waterMarkMode.isCurrentMode("Icon")) {
            if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
                if (!Hud.NoShader.getValue()) {
                    BlurBuffer.blurArea(4, 2, 54, 14, true);
                }
            }
            RenderUtil.drawRect(4, 2, 58, 16, HudWindow.frameBGColor.getRGB());
            FontManager.icon20.drawString("q", 8, 4, 0xffffffff);
            FontManager.vision30.drawString(Flux.NAME.toUpperCase(), 20f, 2f, 0xffffffff);
        }

        String pos = null;
        String bps = null;

        if (mc.thePlayer != null) {

            double xDiff = (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * 2;
            double zDiff = (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * 2;
            BigDecimal bg = new BigDecimal(MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff) * 10d * mc.timer.timerSpeed);

            BlockPos blockPos = mc.thePlayer.getPosition();

            pos = " XYZ:\2477 " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
            bps = " b/s:\2477 " + (bg.setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        float scaleHei = 13 + (12f * (mc.gameSettings.guiScale / 2));

        float width = Math.max(9, mc.fontRendererObj.getStringWidth(pos));

        if (Hud.isMinecraftFont) {
            if (Hud.position.getValue())
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(pos, 0, curHeight - (mc.currentScreen instanceof GuiChat ? scaleHei : 10), color);
            if (Hud.bps.getValue())
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(bps, Hud.bps.getValue() && !Hud.position.getValue() ? 0 : width + 10, curHeight - (mc.currentScreen instanceof GuiChat ? scaleHei : 10), color);
        } else {
            if (Hud.position.getValue())
                FontManager.tahomaArrayList.drawStringWithShadow(pos, 0, curHeight - (mc.currentScreen instanceof GuiChat ? scaleHei : 11) - 2, color);
            if (Hud.bps.getValue())
                FontManager.tahomaArrayList.drawStringWithShadow(bps, Hud.bps.getValue() && Hud.position.getValue() ? width - 3: Hud.bps.getValue() && !Hud.position.getValue() ? 0 : width - 8, curHeight - (mc.currentScreen instanceof GuiChat ? scaleHei : 11) - 2, color);
        }

        if (Hud.effect.getValue()) {
            int x = WorldRenderUtils.getScaledWidth() / 2 + 95;
            int y = (int) (WorldRenderUtils.getScaledHeight() - (Hud.isMinecraftFont ? mc.fontRendererObj.FONT_HEIGHT : FontManager.wqy18.FONT_HEIGHT) - (mc.currentScreen instanceof GuiChat ? (12f * (mc.gameSettings.guiScale / 2) + 1) : 0));

            if (!mc.thePlayer.getActivePotionEffects().isEmpty()) {
                for (PotionEffect o : mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[o.getPotionID()];

                    String potionName = I18n.format(potion.getName());
                    String amplifierName;

                    if (o.getAmplifier() == 0) {
                        amplifierName = "";
                    } else if (o.getAmplifier() == 1) {
                        amplifierName = " " + I18n.format("enchantment.level.2");
                    } else if (o.getAmplifier() == 2) {
                        amplifierName = " " + I18n.format("enchantment.level.3");
                    } else if (o.getAmplifier() == 3) {
                        amplifierName = " " + I18n.format("enchantment.level.4");
                    } else {
                        amplifierName = " " + o.getAmplifier();
                    }

                    String text = potionName + EnumChatFormatting.WHITE + amplifierName + EnumChatFormatting.GRAY + " : " + Potion.getDurationString(o);

                    if (Hud.isMinecraftFont) {
                        mc.fontRendererObj.drawStringWithShadow(text, x, y - 2, potion.getLiquidColor());
                    } else {
                        FontManager.wqy18.drawStringWithShadow(text, x, y - 2, new java.awt.Color(potion.getLiquidColor()).getRGB());
                    }
                    y -= (Hud.isMinecraftFont ? mc.fontRendererObj.FONT_HEIGHT : FontManager.wqy18.FONT_HEIGHT);
                }
            }
        }

        renderBlockCount(curWidth, curHeight);

        autoPlay(curWidth, curHeight);
        NotificationManager.doRender(curWidth, curHeight);

        GLUtils.INSTANCE.rescaleMC(); //End scale change

        if (Hud.armor.getValue()) {
            int x = WorldRenderUtils.getScaledWidth() / 2 + 7;
            int y = WorldRenderUtils.getScaledHeight() - 55;

            for (int i = 3; i >= 0; i--) {
                ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);

                this.renderItem(itemStack, x, y);

                x += 17;
            }

            ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();
            this.renderItem(itemStack, x, y);
        }

        GLUtils.INSTANCE.rescale(scale); //Start scale change

        if (!mc.isSingleplayer()) {
            if (Flux.laggTimer.getElapsedTime() > 1000) {
                GuiRenderUtils.drawRect(curWidth / 2 - 70, 100, 140, 25, 0xcc000000);
                FontManager.icon35.drawString("a", curWidth / 2 - 65, 103, ColorUtils.WHITE.c);
                FontManager.wqy18.drawCenteredString("Connection issue", curWidth / 2 + 8, 106, ColorUtils.WHITE.c);
            }
        }

        Flux.INSTANCE.getHudWindowMgr().

                draw();
        GLUtils.INSTANCE.rescaleMC(); //End scale change
    }

    public float alphaAnimation = 0;
    public float yAxisAnimation = 0;
    public int blockCount = 0;

    public void autoPlay(float wid, float hei) {
        AutoGG m = ModuleManager.autoGGMod;
        if (!m.playCommand.equals("") && m.isEnabled()) {
            if (m.autoplay.getValueState()) {
                m.animationY = AnimationUtils.getAnimationState(m.animationY, m.display ? 0 : -40, (float) (Math.max(10, (Math.abs(m.animationY - (m.display ? 0 : -40))) * 35) * 0.3));
                if (m.animationY > -40) {
                    m.content = "Sending you to next game in " + (m.display ? (int) (((m.autoPlayDelay.getValueState() + m.delay.getValueState() + m.timer.getLastMs()) - System.currentTimeMillis()) / 1000) : "0") + "s";
                    GuiRenderUtils.drawRoundedRect(wid - 142, 5 + m.animationY, 138, 35, 2, RenderUtil.reAlpha(Hud.isLightMode ? ColorUtils.WHITE.c : ColorUtils.BLACK.c, 0.65f), 1, RenderUtil.reAlpha(Hud.isLightMode ? ColorUtils.WHITE.c : ColorUtils.BLACK.c, 0.65f));
                    FontManager.icon30.drawString("t", wid - 140, 9 + m.animationY, Hud.isLightMode ? ColorUtils.GREY.c : RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.9f));
                    FontManager.sans24.drawString("Info", wid - 124, 8 + m.animationY, Hud.isLightMode ? ColorUtils.GREY.c : RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.9f));
                    FontManager.sans16.drawString(m.content, wid - 138, 25 + m.animationY, Hud.isLightMode ? ColorUtils.GREY.c : RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.9f));
                }
            } else {
                m.animationY = -40;
            }
        }
    }

    public void renderBlockCount(float width, float height) {
        float scale = 2;
        float curWidth = mc.displayWidth / scale;
        float curHeight = mc.displayHeight / scale;

        boolean state = ModuleManager.scaffoldMod.isEnabled();
        alphaAnimation = AnimationUtils.getAnimationState(alphaAnimation, state ? 0.8f : 0, 10f);
        yAxisAnimation = AnimationUtils.getAnimationState(this.yAxisAnimation, state ? 0 : 20, (float) Math.max(10, (Math.abs(this.yAxisAnimation - (state ? 0 : 20)) * 50) * 0.5));

        float trueHeight = 20 + (24f * mc.gameSettings.guiScale);

        blockCount = this.getBlocksCount();

        //渲染用
        if (!Scaffold.blockCount.isCurrentMode("Simple")) {
            if (alphaAnimation > 0.2f) {
                blockCount = this.getBlocksCount();
                String cunt = "block" + (blockCount > 1 ? "s" : "");
                FontUtils font = FontManager.sans16;
                FontUtils font2 = FontManager.sans13;
                float length = font.getStringWidth(blockCount + "  ") + font2.getStringWidth(cunt);
                GuiRenderUtils.drawRoundedRect(width / 2 - (length / 2), height - trueHeight + yAxisAnimation, length, 15, 2, RenderUtil.reAlpha(ColorUtils.BLACK.c, alphaAnimation), 0.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, alphaAnimation));
                this.drawArrowRect(width / 2 - 5, height - (trueHeight - 15) + yAxisAnimation, width / 2 + 5, height - (trueHeight - 15) + yAxisAnimation + 5, RenderUtil.reAlpha(ColorUtils.BLACK.c, alphaAnimation));

                font.drawString(blockCount + "", width / 2 - (length / 2 - 1.5f), height - (trueHeight - 2) + yAxisAnimation, RenderUtil.reAlpha(ColorUtils.WHITE.c, MathUtils.clampValue(alphaAnimation + 0.25f, 0f, 1f)));
                font2.drawString(cunt, width / 2 - (length / 2 - .5f) + font.getStringWidth(blockCount + " "), height - (trueHeight - 4) + yAxisAnimation, RenderUtil.reAlpha(ColorUtils.WHITE.c, MathUtils.clampValue(alphaAnimation - 0.1f, 0f, 1f)));
            }
        } else {
            Color color;

            if(blockCount >= 127) {
                color = Color.GREEN;
            } else if (blockCount >= 64){
                color = Color.YELLOW;
            } else {
                color = Color.RED;
            }

            if (state)
                mc.fontRendererObj.drawOutlinedString(blockCount + "", curWidth / 2 - mc.fontRendererObj.getStringWidth(blockCount + "") / 2f, curHeight / 2 - 25, color.getRGB(), Color.BLACK.getRGB());
        }
    }

    public void drawArrowRect(float left, float top, float right, float bottom, int color) {
        float e;

        if (left < right) {
            e = left;
            left = right;
            right = e;
        }

        if (top < bottom) {
            e = top;
            top = bottom;
            bottom = e;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float b = (float) (color >> 16 & 255) / 255.0F;
        float c = (float) (color >> 8 & 255) / 255.0F;
        float d = (float) (color & 255) / 255.0F;
        Tessellator tes = Tessellator.getInstance();
        WorldRenderer bufferBuilder = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(b, c, d, a);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right + 5, top, 0.0D).endVertex();
        bufferBuilder.pos(left - 5, top, 0.0D).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getBlocksCount() {
        int result = 0;
        int i = 9;
        while (i < 45) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (Scaffold.isScaffoldBlock(stack)) {
                result += stack.stackSize;
            }
            ++i;
        }
        return result;
    }

    private void renderItem(ItemStack itemStack, int x, int y) {
        if (itemStack == null)
            return;

        GlStateManager.pushMatrix();

        RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, x, y);

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    public static void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void updateTick() {
        super.updateTick();
    }
}
