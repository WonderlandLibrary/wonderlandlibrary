package sudo.ui.screens.clickgui.setting;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;
import sudo.utils.render.RenderUtils;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

import java.awt.*;

public class ColorBox extends Component{

    private ColorSetting colorSet = (ColorSetting)setting;
    GlyphPageFontRenderer textRend = IFont.CONSOLAS;
    private boolean lmDown = false, rmDown = false;
    public boolean open = false;
    public float h, s, v;

    public ColorBox(Setting setting, ModuleButton parent, int offset) {
        super(setting, parent, offset);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        colorSet.name = colorSet.name;
        int sx = parent.parent.x +5, sy = parent.parent.y + 12 + parent.offset + offset, ex = parent.parent.x + 87, ey = parent.parent.y + getHeight(122)+ parent.offset+ offset;
        DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y+parent.offset+offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset+offset + (parent.parent.height*(open ? 6 : 1)) + (open ? 3 : 0), 0xff1f1f1f);
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, parent.parent.y + parent.offset+offset + (parent.parent.height*(open ? 6 : 1)) + (open ? 3 : 0), ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());

        textRend.drawString(matrices, colorSet.name, (int) sx, (int) sy - 12, -1, 1);
        textRend.drawString(matrices, "#" + colorSet.getHex().substring(1), (int) sx + textRend.getStringWidth(colorSet.name) + 3, (int) sy - 12, colorSet.getRGB(),1);
        
        if (hovered((int)mouseX, (int)mouseY, sx + (open ? 83 : 83), sy - 12, sx + 91, sy - (open? 2:2)) && rmDown) {
            open = !open;
            parent.parent.updateButton();
            rmDown = false;
        }

        RenderUtils.fill(matrices, sx + (open ? 83 : 83), sy - 10, sx + 91, sy - (open? 2:2), colorSet.getColor().getRGB()); //right clicked rect 

        if (hovered((int)mouseX, (int)mouseY, sx + (open ? 83 : 83), sy - 12, sx + 91, sy - 2)) {
            RenderUtils.setup2DRender(true);
            RenderUtils.fill(matrices, mouseX, mouseY+3, mouseX + textRend.getStringWidth("Right click to toggle color picker") + 6, mouseY - 9, new Color(0, 0, 0, 200).getRGB());
            textRend.drawString(matrices, "Right click to toggle color picker", mouseX + 2, mouseY - 10, -1, 1);
            RenderUtils.end2DRender();
        }
        
        if (hovered(mouseX, mouseY, sx + 3 + (int)textRend.getStringWidth(colorSet.name + colorSet.getRGB()) + 17, sy - 12, sx + 27 + (int)textRend.getStringWidth(colorSet.name + colorSet.getRGB()), sy - 4)) {
            RenderUtils.setup2DRender(true);
            RenderUtils.end2DRender();
        }

        if (!open) {
            return;
        }

        RenderUtils.fill(matrices, sx, sy, ex, ey, -1);
        int satColor = MathHelper.hsvToRgb(colorSet.hue, 1f, 1f);
        int red = satColor >> 16 & 255;
        int green = satColor >> 8 & 255;
        int blue = satColor & 255;


        RenderSystem.disableBlend();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        //Draw the color
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(ex, sy, 0).color(red, green, blue, 255).next();
        bufferBuilder.vertex(sx, sy, 0).color(red, green, blue, 0).next();
        bufferBuilder.vertex(sx, ey, 0).color(red, green, blue, 0).next();
        bufferBuilder.vertex(ex, ey, 0).color(red, green, blue, 255).next();
        tessellator.draw();

        //Draw the black stuff
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(ex, sy, 0).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(sx, sy, 0).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(sx, ey, 0).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(ex, ey, 0).color(0, 0, 0, 255).next();
        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();

        //Set the color
        if (hovered(mouseX, mouseY, sx, sy, ex, ey) && lmDown && open) {
            colorSet.bri = 1f - 1f / ((float) (ey - sy) / (mouseY - sy));
            colorSet.sat = 1f / ((float) (ex - sx) / (mouseX - sx));
        }

        int briY = (int) (ey - (ey - sy) * colorSet.bri);
        int satX = (int) (sx + (ex - sx) * colorSet.sat);

        sx = ex + 5;
        ex = ex + 12;

        for (int i = sy; i < ey; i++) {
            float curHue = 1f / ((float) (ey - sy) / (i - sy));
            DrawableHelper.fill(matrices, sx-2, i, ex-2, i + 1, 0xff000000 | MathHelper.hsvToRgb(curHue, 1f, 1f));
        }

        if (hovered(mouseX, mouseY, sx-2, sy, ex-2, ey) && lmDown) {
            colorSet.hue = 1f / ((float) (ey - sy) / (mouseY - sy));
        }

        int hueY = (int) (sy + (ey - sy) * colorSet.hue);
        RenderUtils.fill(matrices, sx-2, hueY-0.8, ex-2, hueY + 1, -1);
        RenderUtils.fill(matrices, satX-1, briY-1, satX+1, briY+1, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public int getHeight(int len) {
        return len - len / 4 - 1;
    }

    public boolean hovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) lmDown = true;
        if (button == 1) rmDown = true;
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) lmDown = false;
        if (button == 1) rmDown = false;
        super.mouseReleased(mouseX, mouseY, button);
    }
}
