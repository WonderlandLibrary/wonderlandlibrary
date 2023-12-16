package wtf.expensive.ui.clickgui.objects;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.joml.Vector4i;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.ui.clickgui.binds.BindWindow;
import wtf.expensive.ui.clickgui.objects.sets.*;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.GaussianBlur;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static wtf.expensive.ui.clickgui.Window.*;

public class ModuleObject extends Object {

    public Function function;
    public ArrayList<Object> object = new ArrayList<>();
    public float animation;
    public BindWindow bindWindow;

    @Override
    public void exit() {
        super.exit();
        for (Object object1 : object) {
            object1.exit();
        }
    }

    public ModuleObject(Function function) {
        this.function = function;
        for (Setting setting : function.settingList) {
            if (setting instanceof BooleanOption option) {
                object.add(new BooleanObject(this, option));
            }
            if (setting instanceof SliderSetting option) {
                object.add(new SliderObject(this, option));
            }
            if (setting instanceof ModeSetting option) {
                object.add(new ModeObject(this, option));
            }
            if (setting instanceof MultiBoxSetting option) {
                object.add(new MultiObject(this, option));
            }
            if (setting instanceof BindSetting option) {
                object.add(new BindObject(this, option));
            }
            if (setting instanceof ButtonSetting option) {
                object.add(new ButtonObject(this, option));
            }
        }
        bindWindow = new BindWindow(this);
        bindWindow.x = 10 + ThreadLocalRandom.current().nextFloat(0, 200);
        bindWindow.y = 10 + ThreadLocalRandom.current().nextFloat(0, 200);
        bindWindow.width = 178 / 2f;
        bindWindow.height = 73 / 2f;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        Managment.CLICK_GUI.searching = false;
        for (Object object1 : object) {
            object1.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (isHovered(mouseX,mouseY, 23)) {
            if (mouseButton == 0)
                function.toggle();
            if (mouseButton == 2)
                bindWindow.openAnimation = !bindWindow.openAnimation;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Object object1 : object) {
            object1.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        super.draw(stack, mouseX, mouseY);
        RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 3, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                ColorUtil.rgba(63, 72, 103, 255),
                ColorUtil.rgba(19, 23, 39, 255),
                ColorUtil.rgba(63, 72, 103, 255),
                ColorUtil.rgba(19, 23, 39, 255)
        ));
        animation = AnimationMath.fast(animation, function.state ? 1 : 0, 5);
        GaussianBlur.startBlur();
        RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 3, 0f, ColorUtil.rgba(25, 26, 33, 255), new Vector4i(
                ColorUtil.rgba(25, 26, 33, 0),ColorUtil.rgba(25, 26, 33, 0),ColorUtil.rgba(25, 26, 33, 0),ColorUtil.rgba(25, 26, 33, 0)
        ));
        GaussianBlur.endBlur(10, 3);

        BloomHelper.registerRenderCall(() -> {
            Fonts.msSemiBold[15].drawString(stack, function.name, x + 10, y + 10, RenderUtil.reAlphaInt(-1, (int) (255 * animation)));
        });
        Fonts.msSemiBold[15].drawString(stack, function.name, x + 10, y + 10, ColorUtil.interpolateColor(light,-1, animation));
        if (!function.settingList.isEmpty()) {
            //Fonts.icons1[15].drawString(stack, "E", x + width - 17, y + 25 / 2f - 1, light);
            RenderUtil.Render2D.drawRect(x + 10, y + 22, width - 20, 0.5f, ColorUtil.rgba(32, 35, 57,255));
        }
        drawObjects(stack, mouseX, mouseY);
    }

    public void drawObjects(MatrixStack stack, int mouseX, int mouseY) {
        float offset = 3;
        for (Object object : object) {
            if (object.setting.visible()) {
                object.x = x;
                object.y = y + 22 + offset;
                object.width = 160;
                object.height = 16;
                object.draw(stack, mouseX, mouseY);
                offset += object.height;
            }
        }
    }

}
