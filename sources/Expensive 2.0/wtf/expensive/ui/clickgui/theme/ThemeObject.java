package wtf.expensive.ui.clickgui.theme;

import com.mojang.blaze3d.matrix.MatrixStack;
import jhlabs.image.PixelUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.GaussianBlur;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

public class ThemeObject {

    public float x, y, width, height;

    public Style style;
    public float anim;



    public ThemeObject(Style style) {
        this.style = style;
    }



    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        anim = AnimationMath.lerp(anim,  Managment.STYLE_MANAGER.getCurrentStyle() == style ? 1 : RenderUtil.isInRegion(mouseX,mouseY, x,y,width,height) ? 0.7f : 0, 5);
        RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 5f, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                ColorUtil.rgba(63, 72, 103, 255),
                ColorUtil.rgba(19, 23, 39, 255),
                ColorUtil.rgba(63, 72, 103, 255),
                ColorUtil.rgba(19, 23, 39, 255)
        ));

        Vector4i colors = new Vector4i(
                style.colors[0],
                style.colors[0],
                style.colors[1],
                style.colors[1]
        );

        if (style.name.equalsIgnoreCase("Разно цветный")) {
            colors = new Vector4i(
                    style.getColor(0),
                    style.getColor(0),
                    style.getColor(90),
                    style.getColor(90)
            );
        }




        float off = 0;
        for (String ss : style.name.split(" ")) {
            Fonts.msSemiBold[16].drawString(stack, ss, x + 7, y + 7 + off, -1);
            off += 10;
        }

        String hexFirst = "#" + Integer.toHexString(colors.x).substring(2).toUpperCase();
        String hexTwo = "#" + Integer.toHexString(colors.z).substring(2).toUpperCase();
        Fonts.msLight[10].drawString(stack, hexFirst, x + 5, y + height - 14, -1);
        Fonts.msLight[10].drawString(stack, hexTwo, x + 5, y + height - 7, -1);


        Vector4i finalColors = colors;
        BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/rect.png"), x + width - 42 - 0.5f, y + height - 16 - 0.5f, 42, 16, new Vector4i(
                RenderUtil.reAlphaInt(finalColors.x, (int) (255 * anim)),
                RenderUtil.reAlphaInt(finalColors.y, (int) (255 * anim)),
                RenderUtil.reAlphaInt(finalColors.z, (int) (255 * anim)),
                RenderUtil.reAlphaInt(finalColors.w, (int) (255 * anim))
        )));

        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/rect.png"), x + width - 42 - 0.5f, y + height - 16 - 0.5f, 42, 16, colors);
    }
}
