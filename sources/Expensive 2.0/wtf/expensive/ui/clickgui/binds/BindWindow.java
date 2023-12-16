package wtf.expensive.ui.clickgui.binds;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.clickgui.objects.ModuleObject;
import wtf.expensive.ui.clickgui.objects.Object;
import wtf.expensive.ui.clickgui.objects.sets.BindObject;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

public class BindWindow {

    public float x, y, width, height;

    public boolean openAnimation;
    public float animation;
    public boolean binding = false;
    public float bindAnimation;

    public Object object;

    public int preX, preY;
    public boolean drag;

    public BindWindow(Object object) {
        this.object = object;
    }

    public void render(MatrixStack stack, int mouseX, int mouseY) {

        if (drag) {
            x = (mouseX + preX);
            y = (mouseY + preY);
        }

        animation = AnimationMath.lerp(animation, openAnimation ? 1 : 0, 20);
        bindAnimation = AnimationMath.lerp(bindAnimation, binding ? 1 : 0, 20);

        GlStateManager.pushMatrix();

        GlStateManager.translatef(x + width / 2f, y + height / 2f, 0);
        GlStateManager.scaled(animation, animation, 1);
        GlStateManager.translatef(-(x + width / 2f), -(y + height / 2f), 0);
        RenderUtil.Render2D.drawShadow(x - 1f, y - 1f, width + 2, height + 2, 15, ColorUtil.rgba(127, 132, 150, 255 * bindAnimation));
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("expensive/images/bind.png"));
        RenderUtil.Render2D.drawRoundedCorner(x - 1f, y - 1f, width + 2, height + 2, 7f, ColorUtil.rgba(127, 132, 150, 255));
        RenderUtil.Render2D.drawTexture(x, y, width, height, 5, 1);

        RenderUtil.Render2D.drawRoundedCorner(x, y, width, 31 / 2f, new Vector4f(5, 0, 5, 0), ColorUtil.rgba(0, 0, 0, 255 * 0.33));
        String name = "";
        int bind = -1;
        if (object instanceof ModuleObject m) {
            name = m.function.name;
            bind = m.function.bind;
        } else if (object instanceof BindObject m) {
            name = m.set.getName();
            bind = m.set.getKey();
        }

        if (name.length() > 13) {
            name = name.substring(0, 13) + "...";
        }

        Fonts.msBold[13].drawString(stack, name, x + 7, y + 6, -1);
        Fonts.msSemiBold[13].drawString(stack, "Бинд", x + 5, y + height / 2f + 5, ColorUtil.rgba(161, 164, 177, 255));
        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/cross.png"), x + width - 12, y + 5, 5, 5, -1);
        String key = ClientUtil.getKey(bind);

        if (key == null) {
            key = "";
        }
        if (key.length() > 6) {
            key = key.substring(0, 6) + "..";
        }
        float wwidth = Math.max(25 / 2f, Fonts.msLight[13].getWidth(key) + 4);

        RenderUtil.Render2D.drawRoundedRect(x + width - 5 - wwidth, y + 19, wwidth, 25 / 2f, 2, ColorUtil.rgba(0, 0, 0, 255 * 0.33));


        Fonts.msSemiBold[14].drawCenteredString(stack, key.toUpperCase(), x + width - 5 - wwidth + wwidth / 2f, y + 19 + 4, -1);
        GlStateManager.popMatrix();
    }

    public void mouseClick(int x, int y, int button) {
        int bind = -1;
        if (object instanceof ModuleObject m) {
            bind = m.function.bind;
        } else if (object instanceof BindObject m) {
            bind = m.set.getKey();
        }
        if (openAnimation) {

            String key = ClientUtil.getKey(bind);

            if (key == null) {
                key = "";
            }
            float wwidth = Math.max(25 / 2f, Fonts.msLight[13].getWidth(key) + 4);

            if (RenderUtil.isInRegion(x, y, this.x + width - 5 - wwidth, this.y + 19, wwidth, 25 / 2f) && button == 0) {
                binding = !binding;
            }

            if (binding && button > 2) {
                if (object instanceof ModuleObject m) {
                    m.function.bind = -100 + button;
                    Managment.NOTIFICATION_MANAGER.add("Модуль " + TextFormatting.GRAY + m.function.name + TextFormatting.WHITE + " был забинжен на кнопку " + ClientUtil.getKey(-100 + button).toUpperCase(), "Module", 5);
                } else if (object instanceof BindObject m) {
                    m.set.setKey(-100 + button);
                    Managment.NOTIFICATION_MANAGER.add("Функция " + TextFormatting.GRAY + m.object.function.name + TextFormatting.WHITE + " была забинжена на кнопку " + ClientUtil.getKey(-100 + button).toUpperCase(), "Module", 5);
                }
                binding = false;
            }

            if (RenderUtil.isInRegion(x, y, this.x + width - 12 - 1f, this.y + 5 - 1f, 5, 5)) {
                openAnimation = false;
            }
            if (RenderUtil.isInRegion(x, y, this.x, this.y, width, 31 / 2f) && button == 0) {
                drag = true;
                preX = (int) (this.x - x);
                preY = (int) (this.y - y);
            }
        }
    }


    public void mouseUn() {
        drag = false;
    }

    public void keyPress(int key) {

        if (binding) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                if (object instanceof ModuleObject m) {
                    m.function.bind = 0;
                } else if (object instanceof BindObject m) {
                    m.set.setKey(0);
                }

                binding = false;
                return;
            }
            if (object instanceof ModuleObject m) {
                m.function.bind = key;
                Managment.NOTIFICATION_MANAGER.add("Модуль " + TextFormatting.GRAY + m.function.name + TextFormatting.WHITE + " был забинжен на кнопку " + ClientUtil.getKey(key).toUpperCase(), "Module", 5);

            } else if (object instanceof BindObject m) {
                m.set.setKey(key);
                Managment.NOTIFICATION_MANAGER.add("Функция " + TextFormatting.GRAY + m.object.function.name + TextFormatting.WHITE + " была забинжена на кнопку " + ClientUtil.getKey(key).toUpperCase(), "Module", 5);

            }
            binding = false;
        }
    }

}
