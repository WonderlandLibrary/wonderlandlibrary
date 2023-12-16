package wtf.expensive.ui.alt;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wtf.expensive.managment.Managment;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.*;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static wtf.expensive.util.IMinecraft.mc;

public class AltManager extends Screen {

    public AltManager() {
        super(new StringTextComponent(""));

    }

    public ArrayList<Account> accounts = new ArrayList<>();


    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (!altName.isEmpty())
                altName = altName.substring(0, altName.length() - 1);
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            if (!altName.isEmpty())
                accounts.add(new Account(altName));
            typing = false;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        altName += Character.toString(codePoint);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        float x = 220 / 2f, y = 327 / 2f;

        if (RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 664 / 2f, 249 / 2f, 46 / 2f)) {
            AltConfig.updateFile();
            accounts.add(new Account(RandomStringUtils.randomAlphabetic(8)));
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 723 / 2f, 249 / 2f, 46 / 2f)) {
            if (!altName.isEmpty() && altName.length() < 20) {
                accounts.add(new Account(altName));
                AltConfig.updateFile();
            }

            typing = false;
        }
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 21, y + 25 + 30, 419 / 2f, 23)) {

            typing = !typing;
        }

        float altX = 778 / 2f, altY = 298 / 2f;

        float iter = scrollAn;
        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            float panWidth = 197 / 2f;
            float acX = altX + 15 + (iter * (panWidth + 10));

            if (RenderUtil.isInRegion(mouseX, mouseY, acX, 442 / 2f, panWidth, 261 / 2f)) {
                if (button == 0) {
                    mc.session = new Session(account.accountName, "", "", "mojang");
                } else {
                    iterator.remove(); // Безопасное удаление элемента
                    AltConfig.updateFile();
                }
            }

            iter++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public float scroll;
    public float scrollAn;

    public boolean hoveredFirst;
    public boolean hoveredSecond;

    public float hoveredFirstAn;
    public float hoveredSecondAn;

    private String altName = "";
    private boolean typing;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll += delta * 1;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        scrollAn = AnimationMath.lerp(scrollAn, scroll, 5);
        hoveredFirst = RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 664 / 2f, 249 / 2f, 46 / 2f);
        hoveredSecond = RenderUtil.isInRegion(mouseX, mouseY, 345 / 2f, 723 / 2f, 249 / 2f, 46 / 2f);
        hoveredFirstAn = AnimationMath.lerp(hoveredFirstAn, hoveredFirst ? 1 : 0, 10);
        hoveredSecondAn = AnimationMath.lerp(hoveredSecondAn, hoveredSecond ? 1 : 0, 10);

        mc.gameRenderer.setupOverlayRendering(2);


        float width = mc.getMainWindow().scaledWidth();
        float height = mc.getMainWindow().scaledHeight();

        float x = (220 / 2f), y = (327 / 2f);

        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/backmenu.png"), 0, 0, width, height, -1);

        RenderUtil.Render2D.drawRoundedRect(220 / 2f, 327 / 2f, 502 / 2f, 481 / 2f, 5, ColorUtil.rgba(19, 21, 27, 255 * 0.33));

        RenderUtil.Render2D.drawRoundOutline(220 / 2f, 327 / 2f, 502 / 2f, 481 / 2f, 5, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                ColorUtil.rgba(127, 132, 150, 255 * 0.33)
        ));

        GaussianBlur.startBlur();
        RenderUtil.Render2D.drawRoundOutline(220 / 2f, 327 / 2f, 502 / 2f, 481 / 2f, 5, 0f, ColorUtil.rgba(25, 26, 33, 255), new Vector4i(
                ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0)
        ));
        GaussianBlur.endBlur(20, 2);

        Fonts.msRegular[14].drawCenteredString(matrixStack, "Вы успешно зашли под никнеймом " + mc.getSession().getUsername(), x + ((502 / 2f) / 2f), y + 481 / 2f + 10, ColorUtil.rgba(142, 145, 157, 255));

        Fonts.msSemiBold[20].drawString(matrixStack, "Менеджер аккаунтов", x + 21, y + 25, -1);
        Fonts.msRegular[14].drawString(matrixStack, "Никнейм", x + 21, y + 25 + 22, ColorUtil.rgba(161, 164, 177, 255));
        RenderUtil.Render2D.drawRoundedRect(x + 21, y + 25 + 30, 419 / 2f, 23, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));
        Fonts.msRegular[14].drawString(matrixStack, "Пароль", x + 21, y + 20 + 66, ColorUtil.rgba(161, 164, 177, 255));
        Fonts.msRegular[14].drawString(matrixStack, "(если требуется)", x + 23 + Fonts.msRegular[14].getWidth("Пароль"), y + 20 + 66, ColorUtil.rgba(54, 55, 59, 255));
        RenderUtil.Render2D.drawRoundedRect(x + 21, y + 20 + 66 + 7, 419 / 2f, 23, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x + 21, y + 25 + 30, 419 / 2f - 5, 23);
        Fonts.msSemiBold[15].drawString(matrixStack, altName + (typing ? (System.currentTimeMillis() % 1000 > 500 ? "_" : "") : ""), x + 28, y + 25 + 38, ColorUtil.rgba(161, 164, 177, 255));
        SmartScissor.unset();
        SmartScissor.pop();

        RenderUtil.Render2D.drawRoundedRect(345 / 2f, 664 / 2f, 249 / 2f, 46 / 2f, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));
        RenderUtil.Render2D.drawRoundedRect(345 / 2f, 723 / 2f, 249 / 2f, 46 / 2f, 5, ColorUtil.rgba(0, 0, 0, 255 * 0.22));

        BloomHelper.registerRenderCall(() -> {
            Fonts.msRegular[18].drawCenteredString(matrixStack, "Рандомный ник", 345 / 2f + (249 / 2f) / 2f, 664 / 2f + 8, ColorUtil.interpolateColor(ColorUtil.rgba(161, 164, 177, 0), -1, hoveredFirstAn));
            Fonts.msRegular[18].drawCenteredString(matrixStack, "Войти", 345 / 2f + (249 / 2f) / 2f, 722 / 2f + 8, ColorUtil.interpolateColor(ColorUtil.rgba(161, 164, 177, 0), -1, hoveredSecondAn));
        });


        Fonts.msRegular[18].drawCenteredString(matrixStack, "Рандомный ник", 345 / 2f + (249 / 2f) / 2f, 664 / 2f + 8, ColorUtil.interpolateColor(ColorUtil.rgba(161, 164, 177, 255), -1, hoveredFirstAn));
        Fonts.msRegular[18].drawCenteredString(matrixStack, "Войти", 345 / 2f + (249 / 2f) / 2f, 722 / 2f + 8, ColorUtil.interpolateColor(ColorUtil.rgba(161, 164, 177, 255), -1, hoveredSecondAn));

        float altX = 778 / 2f, altY = 298 / 2f;

        RenderUtil.Render2D.drawRoundedRect(778 / 2f, 298 / 2f, 923 / 2f, 539 / 2f, 5, ColorUtil.rgba(19, 21, 27, 255 * 0.33));

        RenderUtil.Render2D.drawRoundOutline(778 / 2f, 298 / 2f, 923 / 2f, 539 / 2f, 5, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                ColorUtil.rgba(127, 132, 150, 255 * 0.33)
        ));

        GaussianBlur.startBlur();
        RenderUtil.Render2D.drawRoundOutline(778 / 2f, 298 / 2f, 923 / 2f, 539 / 2f, 5, 0f, ColorUtil.rgba(25, 26, 33, 255), new Vector4i(
                ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0), ColorUtil.rgba(25, 26, 33, 0)
        ));
        GaussianBlur.endBlur(20, 2);

        Fonts.msSemiBold[20].drawString(matrixStack, "Список аккаунтов", altX + 15, altY + 15, -1);
        float iter = scrollAn;
        float size = 0;
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(778 / 2f, 298 / 2f, 923 / 2f, 539 / 2f);
        for (Account account : accounts) {
            float panWidth = 197 / 2f;

            float acX = altX + 15 + (iter * (panWidth + 10));
            float acY = 442 / 2f;

            if (account.accountName.equalsIgnoreCase(mc.session.getUsername())) {

                StencilUtil.initStencilToWrite();
                RenderUtil.Render2D.drawRoundedRect(acX + 12.5F, acY + 10, 148 / 2f, 148 / 2f, 7, Color.BLACK.getRGB());
                StencilUtil.readStencilBuffer(1);
                mc.getTextureManager().bindTexture(account.skin);
                AbstractGui.drawScaledCustomSizeModalRect(acX + 12.5F, acY + 10, 8F, 8F, 8F, 8F, 148 / 2f, 148 / 2f, 64, 64);
                StencilUtil.uninitStencilBuffer();

                GaussianBlur.startBlur();
                RenderUtil.Render2D.drawRoundedRect(acX, 442 / 2f, panWidth, 261 / 2f, 8, ColorUtil.rgba(0, 0, 0, 255 * 0.22));
                GaussianBlur.endBlur(20, 1);
            }
            RenderUtil.Render2D.drawRoundedRect(acX, 442 / 2f, panWidth, 261 / 2f, 8, ColorUtil.rgba(0, 0, 0, 255 * 0.22));
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates(acX + 5, 442 / 2f, panWidth - 8, 261 / 2f);
            BloomHelper.registerRenderCall(() -> {
                SmartScissor.push();
                SmartScissor.setFromComponentCoordinates(acX + 5, 442 / 2f, panWidth - 8, 261 / 2f);
                Fonts.msSemiBold[14].drawCenteredString(matrixStack, account.accountName, acX + panWidth / 2f, 629 / 2f, ColorUtil.rgba(161, 164, 177, account.accountName.equalsIgnoreCase(mc.session.getUsername()) ? 255 : 0));
                SmartScissor.unset();
                SmartScissor.pop();
            });

            Fonts.msSemiBold[14].drawCenteredString(matrixStack, account.accountName, acX + panWidth / 2f, 629 / 2f, ColorUtil.rgba(161, 164, 177, 255));

            Date dateAdded = new Date(account.dateAdded);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(dateAdded);

            Fonts.msRegular[14].drawCenteredString(matrixStack, formattedDate, acX + panWidth / 2f, 629 / 2f + 13, ColorUtil.rgba(100, 100, 100, 255));
        SmartScissor.unset();
        SmartScissor.pop();

            StencilUtil.initStencilToWrite();
            RenderUtil.Render2D.drawRoundedRect(acX + 12.5F, acY + 10, 148 / 2f, 148 / 2f, 7, Color.BLACK.getRGB());
            StencilUtil.readStencilBuffer(1);
            mc.getTextureManager().bindTexture(account.skin);
            AbstractGui.drawScaledCustomSizeModalRect(acX + 12.5F, acY + 10, 8F, 8F, 8F, 8F, 148 / 2f, 148 / 2f, 64, 64);
            StencilUtil.uninitStencilBuffer();
            iter++;
            size++;
        }
        scroll = MathHelper.clamp(scroll, size > 4 ? -size + 4 : 0, 0);
        BloomHelper.draw(10, 1.5f, false);
        SmartScissor.unset();
        SmartScissor.pop();

        mc.gameRenderer.setupOverlayRendering();
    }


}
