package dev.tenacity.ui.mainmenu;

import dev.tenacity.Tenacity;
import dev.tenacity.intent.api.account.IntentAccount;
import dev.tenacity.intent.cloud.Cloud;
import dev.tenacity.ui.Screen;
import dev.tenacity.ui.altmanager.helpers.Alt;
import dev.tenacity.ui.altmanager.panels.LoginPanel;
import dev.tenacity.ui.mainmenu.particles.ParticleEngine;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.misc.*;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.render.blur.GaussianBlur;
import lombok.Getter;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CustomMainMenu extends GuiScreen {
    private ParticleEngine particleEngine;

    public static boolean animatedOpen = false;

    private final List<MenuButton> buttons = new ArrayList() {{
        add(new MenuButton("Singleplayer"));
        add(new MenuButton("Multiplayer"));
        add(new MenuButton("Alt Manager"));
        add(new MenuButton("Settings"));
        add(new MenuButton("Exit"));
    }};

    private final List<TextButton> textButtons = new ArrayList() {{
        add(new TextButton("Scripting"));
        add(new TextButton("Discord"));
    }};

    private final ResourceLocation backgroundResource = new ResourceLocation("Tenacity/MainMenu/funny.png");
    private final ResourceLocation blurredRect = new ResourceLocation("Tenacity/MainMenu/rect-test.png");
    private final ResourceLocation hoverCircle = new ResourceLocation("Tenacity/MainMenu/hover-circle.png");

    private static boolean firstInit = false;

    @Override
    public void initGui() {
        if (!firstInit) {
            NetworkingUtils.bypassSSL();
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                Tenacity.INSTANCE.setDiscordRPC(new DiscordRPC());
            }
            firstInit = true;
        }

        if (particleEngine == null) particleEngine = new ParticleEngine();
        if (mc.gameSettings.guiScale != 2) {
            Tenacity.prevGuiScale = mc.gameSettings.guiScale;
            Tenacity.updateGuiScale = true;
            mc.gameSettings.guiScale = 2;
            mc.resize(mc.displayWidth - 1, mc.displayHeight);
            mc.resize(mc.displayWidth + 1, mc.displayHeight);
        }
        buttons.forEach(MenuButton::initGui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        width = sr.getScaledWidth();
        height = sr.getScaledHeight();


        RenderUtil.resetColor();
        RenderUtil.drawImage(backgroundResource, 0, 0, width, height);

        particleEngine.render();

        float rectWidth = 277;
        float rectHeight = 275.5f;

        GaussianBlur.startBlur();
        RoundedUtil.drawRound(width / 2f - rectWidth / 2f, height / 2f - rectHeight / 2f,
                rectWidth, rectHeight, 10, Color.WHITE);
        GaussianBlur.endBlur(40, 2);


        float outlineImgWidth = 688 / 2f;
        float outlineImgHeight = 681 / 2f;
        GLUtil.startBlend();
        RenderUtil.color(-1);
        RenderUtil.drawImage(blurredRect, width / 2f - outlineImgWidth / 2f, height / 2f - outlineImgHeight / 2f,
                outlineImgWidth, outlineImgHeight);


        if (animatedOpen) {
            //    tenacityFont80.drawCenteredString("Tenacity", width / 2f, height / 2f - 110, Color.WHITE.getRGB());
            //    tenacityFont32.drawString(Tenacity.VERSION, width / 2f + tenacityFont80.getStringWidth("Tenacity") / 2f - (tenacityFont32.getStringWidth(Tenacity.VERSION) / 2f), height / 2f - 113, Color.WHITE.getRGB());
        }

        GL11.glEnable(GL11.GL_BLEND);


        StencilUtil.initStencilToWrite();

        RenderUtil.setAlphaLimit(13);
        buttons.forEach(MenuButton::drawOutline);

        RenderUtil.setAlphaLimit(0);
        StencilUtil.readStencilBuffer(1);


        float circleW = 174 / 2f;
        float circleH = 140 / 2f;
        ResourceLocation rs = new ResourceLocation("Tenacity/MainMenu/circle-funny.png");
        mc.getTextureManager().bindTexture(rs);
        GLUtil.startBlend();
        RenderUtil.drawImage(rs, mouseX - circleW / 2f, mouseY - circleH / 2f, circleW, circleH);

        StencilUtil.uninitStencilBuffer();


        float buttonWidth = 140;
        float buttonHeight = 25;

        int count = 0;
        for (MenuButton button : buttons) {
            button.x = width / 2f - buttonWidth / 2f;
            button.y = ((height / 2f - buttonHeight / 2f) - 25) + count;
            button.width = buttonWidth;
            button.height = buttonHeight;
            button.clickAction = () -> {
                switch (button.text) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Alt Manager":
                        mc.displayGuiScreen(Tenacity.INSTANCE.getAltManager());
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                    case "Exit":
                        mc.shutdown();
                        break;
                }
            };
            button.drawScreen(mouseX, mouseY);
            count += buttonHeight + 5;
        }


        float buttonCount = 0;
        float buttonsWidth = (float) textButtons.stream().mapToDouble(TextButton::getWidth).sum();
        int buttonsSize = textButtons.size();
        buttonsWidth += tenacityFont16.getStringWidth(" | ") * (buttonsSize - 1);

        int buttonIncrement = 0;
        for (TextButton button : textButtons) {
            button.x = width / 2f - buttonsWidth / 2f + buttonCount;
            button.y = (height / 2f) + 120;
            switch (button.text) {
                case "Scripting":
                    button.clickAction = () -> {
                        IOUtils.openLink("https://scripting.tenacity.dev");
                    };
                    break;
                case "Discord":
                    button.clickAction = () -> {
                        IOUtils.openLink("https://tenacity.dev/discord");
                    };
                    break;
            }

            button.addToEnd = (buttonIncrement != (buttonsSize - 1));

            button.drawScreen(mouseX, mouseY);


            buttonCount += button.getWidth() + tenacityFont14.getStringWidth(" | ");
            buttonIncrement++;
        }

        tenacityBoldFont80.drawCenteredString("Tenacity", width / 2f, height / 2f - 110, Color.WHITE.getRGB());
        tenacityFont32.drawString(Tenacity.VERSION, width / 2f + tenacityBoldFont80.getStringWidth("Tenacity") / 2f - (tenacityFont32.getStringWidth(Tenacity.VERSION) / 2f), height / 2f - 113, Color.WHITE.getRGB());
        tenacityFont18.drawCenteredString("by cedo, senoe, and tear", width / 2f, height / 2f - 68, Color.WHITE.getRGB());

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        LoginPanel.cracked = Cloud.getApiKey() == null;
        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        textButtons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void onGuiClosed() {
        if (Tenacity.updateGuiScale) {
            mc.gameSettings.guiScale = Tenacity.prevGuiScale;
            Tenacity.updateGuiScale = false;
        }
    }

    private static class TextButton implements Screen {
        public float x, y;
        @Getter
        private final float width, height;
        public Runnable clickAction;
        private final String text;

        private final Animation hoverAnimation = new DecelerateAnimation(150, 1);

        public boolean addToEnd;

        public TextButton(String text) {
            this.text = text;
            width = tenacityFont16.getStringWidth(text);
            height = tenacityFont16.getHeight();
        }

        @Override
        public void initGui() {

        }

        @Override
        public void keyTyped(char typedChar, int keyCode) {

        }

        @Override
        public void drawScreen(int mouseX, int mouseY) {
            boolean hovered = HoveringUtil.isHovering(x, y, width, height, mouseX, mouseY);
            hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
            tenacityFont16.drawString(text, x, y - (height / 2f * hoverAnimation.getOutput().floatValue()), Color.WHITE.getRGB());
            if (addToEnd) {
                tenacityFont16.drawString(" | ", x + width, y, Color.WHITE.getRGB());
            }
        }

        @Override
        public void mouseClicked(int mouseX, int mouseY, int button) {
            boolean hovered = HoveringUtil.isHovering(x, y, width, height, mouseX, mouseY);
            if (hovered && button == 0) {
                clickAction.run();
            }
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY, int state) {

        }
    }

}
