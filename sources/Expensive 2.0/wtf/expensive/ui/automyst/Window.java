package wtf.expensive.ui.automyst;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.managment.Managment;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.GaussianBlur;
import wtf.expensive.util.render.RenderUtil;

public class Window extends Screen {
    public Window(ITextComponent titleIn) {
        super(titleIn);
    }

    public boolean openedAdd;

    public String name = "";

    public int count = 8;

    public boolean nameTyping;

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (nameTyping) {
            name += codePoint;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (nameTyping) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!name.isEmpty())
                    name = name.substring(0, name.length() - 1);
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                nameTyping = false;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (Managment.FUNCTION_MANAGER.clickGui.blur.get()) {
            GaussianBlur.startBlur();
            RenderUtil.Render2D.drawRect(0, 0, width, height, -1);
            GaussianBlur.endBlur(Managment.FUNCTION_MANAGER.clickGui.blurVal.getValue().floatValue(), 1);
        }

        float width = 318 / 2F;
        float heigth = 339 / 2F;
        float x = this.width / 2f - width / 2f + (openedAdd ? (width / 2f + 10) : 0);
        float y = this.height / 2f - heigth / 2f;

        if (openedAdd) {
            float xA = this.width / 2f - width / 2f - (openedAdd ? (width / 2f + 10) : 0);
            RenderUtil.Render2D.drawShadow(xA,y,width,heigth, 10, ColorUtil.rgba(25,25,25, 64));

            RenderUtil.Render2D.drawRoundedRect(xA,y,width,heigth, 4, ColorUtil.rgba(25,25,25, 150));
            RenderUtil.Render2D.drawShadow(xA,y,5,heigth, 15, ColorUtil.getColorStyle(0),
                    ColorUtil.getColorStyle(100),
                    ColorUtil.getColorStyle(0),
                    ColorUtil.getColorStyle(100));
            RenderUtil.Render2D.drawRect(xA,y + 15,width,1, ColorUtil.getColorStyle((100 / heigth) * 20));
            RenderUtil.Render2D.drawRoundedCorner(xA,y,5,heigth, new Vector4f(4,4,0,0), new Vector4i(
                    ColorUtil.getColorStyle(0),
                    ColorUtil.getColorStyle(100),
                    ColorUtil.getColorStyle(0),
                    ColorUtil.getColorStyle(100)
            ));

            Fonts.msSemiBold[16].drawString(matrixStack, "Добавить",xA + 10,y + 5, -1);
            RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/cross.png"), xA + width - 12, y + 5, 5, 5, -1);
            RenderUtil.Render2D.drawRect(xA + 15, y + 45, width - 25, 1, ColorUtil.rgba(255,255,255,128));
            Fonts.msSemiBold[13].drawString(matrixStack, "Название", xA + 17, y + 28, ColorUtil.rgba(255,255,255,128));
            Fonts.msSemiBold[15].drawString(matrixStack, name + (nameTyping ? System.currentTimeMillis() % 1000 > 500 ? "" : "_" : ""), xA + 17, y + 38, -1);

            Fonts.msSemiBold[13].drawString(matrixStack, "Количество", xA + 17, y + 62, ColorUtil.rgba(255,255,255,128));


        }

        RenderUtil.Render2D.drawShadow(x,y,width,heigth, 10, ColorUtil.rgba(25,25,25, 64));

        RenderUtil.Render2D.drawRoundedRect(x,y,width,heigth, 4, ColorUtil.rgba(25,25,25, 150));
        RenderUtil.Render2D.drawShadow(x,y,5,heigth, 15, ColorUtil.getColorStyle(0),
                ColorUtil.getColorStyle(100),
                ColorUtil.getColorStyle(0),
                ColorUtil.getColorStyle(100));
        RenderUtil.Render2D.drawRect(x,y + 15,width,1, ColorUtil.getColorStyle((100 / heigth) * 20));
        RenderUtil.Render2D.drawRoundedCorner(x,y,5,heigth, new Vector4f(4,4,0,0), new Vector4i(
                ColorUtil.getColorStyle(0),
                ColorUtil.getColorStyle(100),
                ColorUtil.getColorStyle(0),
                ColorUtil.getColorStyle(100)
        ));


        Fonts.msSemiBold[16].drawString(matrixStack, "Auto Buy",x + 10,y + 5, -1);
        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/cross.png"), x + width - 12, y + 5, 5, 5, -1);

        RenderUtil.Render2D.drawRoundedRect(x + 15, y + heigth - (27 / 2f) - 7, 114 / 2f, 27 / 2f, 2.5F, ColorUtil.rgba(0,0,0,128));
        Fonts.msSemiBold[14].drawCenteredString(matrixStack, "Добавить", x + 15 + (114 / 2f) / 2f, y + heigth - (27 / 2f) - 2.5F, -1);
        RenderUtil.Render2D.drawRoundedRect(x + width - (114 / 2f) - 15, y + heigth - (27 / 2f) - 7, 114 / 2f, 27 / 2f, 2.5F, ColorUtil.rgba(0,0,0,128));
        Fonts.msSemiBold[14].drawCenteredString(matrixStack, "Удалить", x + width - (114 / 2f) - 15 + (114 / 2f) / 2f, y + heigth - (27 / 2f) - 2.5F, -1);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float width = 318 / 2F;
        float heigth = 339 / 2F;
        float x = this.width / 2f - width / 2f + (openedAdd ? (width / 2f + 10) : 0);
        float y = this.height / 2f - heigth / 2f;
        if (RenderUtil.isInRegion(mouseX,mouseY, x + 15, y + heigth - (27 / 2f) - 7 + 5, 114 / 2f, 27 / 2f)) {
            openedAdd = !openedAdd;
        }
        if (openedAdd) {
            float xA = this.width / 2f - width / 2f - (openedAdd ? (width / 2f + 10) : 0);
            if (RenderUtil.isInRegion(mouseX,mouseY,xA + 15, y + 30, width - 25, 15)) {
                nameTyping = !nameTyping;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
