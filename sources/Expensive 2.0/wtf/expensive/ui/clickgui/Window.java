package wtf.expensive.ui.clickgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.Type;
import wtf.expensive.ui.clickgui.configs.ConfigDrawing;
import wtf.expensive.ui.clickgui.objects.ModuleObject;
import wtf.expensive.ui.clickgui.objects.Object;
import wtf.expensive.ui.clickgui.objects.sets.BindObject;
import wtf.expensive.ui.clickgui.theme.ThemeDrawing;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.*;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static wtf.expensive.util.IMinecraft.mc;

public class Window extends Screen {

    private Vector2f position = new Vector2f(0, 0);

    public static Vector2f size = new Vector2f(500, 400);

    public static int dark = new Color(18, 19, 25).getRGB();
    public static int medium = new Color(18, 19, 25).brighter().getRGB();
    public static int light = new Color(129, 134, 153).getRGB();

    private Type currentCategory;

    private ThemeDrawing themeDrawing = new ThemeDrawing();
    private ConfigDrawing configDrawing = new ConfigDrawing();

    private ArrayList<ModuleObject> objects = new ArrayList<>();

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrolling += (float) (delta * 30);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public Window(ITextComponent titleIn) {
        super(titleIn);
        scrolling = 0;
        for (Function function : Managment.FUNCTION_MANAGER.getFunctions()) {
            objects.add(new ModuleObject(function));
        }

    }

    @Override
    protected void init() {
        super.init();
        size = new Vector2f(450, 350);
        position = new Vector2f(mc.getMainWindow().scaledWidth() / 2f - size.x / 2f, mc.getMainWindow().scaledHeight() / 2f - size.y / 2f);
    }

    public static float scrolling;
    public static float scrollingOut;

    public boolean searching;
    private String searchText = "";

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();

        mouseY = fixed.getY();

        if (Managment.FUNCTION_MANAGER.clickGui.blur.get()) {
            GaussianBlur.startBlur();
            RenderUtil.Render2D.drawRect(0, 0, width, height, -1);
            GaussianBlur.endBlur(Managment.FUNCTION_MANAGER.clickGui.blurVal.getValue().floatValue(), 1);
        }
        float bar = 100;
        mc.gameRenderer.setupOverlayRendering(2);
        RenderUtil.Render2D.drawRoundedCorner(position.x + bar, position.y, size.x - bar, size.y, new Vector4f(0, 0, 8, 8), dark);
        mc.getTextureManager().bindTexture(new ResourceLocation("expensive/images/ui/menu.jpg"));
        RenderUtil.Render2D.drawRoundedCorner(position.x + bar, position.y, size.x - bar, size.y, new Vector4f(0, 0, 8, 8));
        RenderUtil.Render2D.drawRoundedCorner(position.x, position.y, bar, size.y, new Vector4f(8, 8, 0, 0), RenderUtil.reAlphaInt(medium, 200));
        RenderUtil.Render2D.drawRect(position.x + bar, position.y, 0.5f, size.y, RenderUtil.reAlphaInt(light, 50));

        RenderUtil.Render2D.drawRoundedCorner(position.x + 7.5f, position.y + 30, bar - 15, 17, 5, dark);
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(position.x + 7.5f, position.y + 30, bar - 15, 17);
        if (!searching && searchText.isEmpty()) {
            Fonts.gilroy[14].drawString(matrixStack, "Поиск", position.x + 12.5f + 10, position.y + 37, RenderUtil.reAlphaInt(light, 200));
        } else {
            Fonts.gilroy[14].drawString(matrixStack, searchText + (searching ? (System.currentTimeMillis() % 1000 > 500 ? "_" : "") : ""), position.x + 12.5f + 10, position.y + 37, RenderUtil.reAlphaInt(light, 200));
        }
        SmartScissor.unset();
        SmartScissor.pop();
        Fonts.icons1[12].drawString(matrixStack, "I", position.x + 13f, position.y + 38, RenderUtil.reAlphaInt(light, 200));
        Fonts.icons1[15].drawString(matrixStack, "H", position.x + bar / 2 - Fonts.sora[18].getWidth("expensive") / 2f - 8, position.y + 15.5, -1);
        Fonts.sora[18].drawCenteredString(matrixStack, "expensive", position.x + bar / 2 + 8 / 2f, position.y + 13, -1);

        float len = 0;
        for (Type type : Type.values()) {
            len = type.ordinal() * 20;

            if (type == Type.Theme) {
                RenderUtil.Render2D.drawRect(position.x + 5, position.y + 55 + len, bar - 10, 0.5f, RenderUtil.reAlphaInt(light, 50));
            }

            if (type == Type.Theme || type == Type.Configs) {
                len += 10;
            }

            type.anim = AnimationMath.fast((float) type.anim, type == currentCategory ? 1 : 0, 10);
            if (type.anim > 0.001) {

                RenderUtil.Render2D.drawShadow(position.x + 5f, position.y + 55 + len, (float) bar - 10, 15F, (int) (15),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(0), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(90), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(180), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(270), (int) (100 * type.anim)));

                RenderUtil.Render2D.drawGradientRound(position.x + 5f, position.y + 55 + len, (float) bar - 10, 15, 3,
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(0), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(90), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(180), (int) (100 * type.anim)),
                        RenderUtil.reAlphaInt(ColorUtil.getColorStyle(270), (int) (100 * type.anim)));
            }
            Fonts.icons1[15].drawString(matrixStack, type.image, position.x + 9f, position.y + 61 + len, light);
            Fonts.sora[15].drawString(matrixStack, type.name(), position.x + 9f + 12, position.y + 60 + len, light);
        }
        RenderUtil.Render2D.drawRect(position.x + 5, position.y + size.y - 35, bar - 10, 0.5f, RenderUtil.reAlphaInt(light, 50));
        RenderUtil.Render2D.drawShadow(position.x + 5, position.y + size.y - 30, 25, 25, 15, ColorUtil.rgba(0, 0, 0, 64));
        if (ClientUtil.me != null) {
            GlStateManager.color4f(0, 0, 0, 0);
            GlStateManager.bindTexture(RenderUtil.Render2D.downloadImage(ClientUtil.me.getAvatarUrl()));
            RenderUtil.Render2D.drawTexture(position.x + 5, position.y + size.y - 30, 25, 25, 5, 1);
        } else {
            mc.getTextureManager().bindTexture(new ResourceLocation("expensive/images/ui/profile/image.png"));
            RenderUtil.Render2D.drawTexture(position.x + 5, position.y + size.y - 30, 25, 25, 5, 1);
        }
        Fonts.gilroyBold[15].drawString(matrixStack, Managment.USER_PROFILE.getName(), position.x + 35, position.y + size.y - 25, -1);
        Fonts.gilroy[13].drawString(matrixStack, "UID: " + Managment.USER_PROFILE.getUid(), position.x + 35, position.y + size.y - 16, light);
        if (currentCategory == Type.Theme) {
            themeDrawing.draw(matrixStack, mouseX, mouseY, position.x + 100, position.y, size.x - bar, size.y);
            BloomHelper.draw(25, 1.5f, false);
        }
        if (currentCategory == Type.Configs) {
            configDrawing.draw(matrixStack, mouseX, mouseY, position.x + 100, position.y, size.x - bar, size.y);
            BloomHelper.draw(10, 1f, false);
        }
        if (currentCategory != Type.Theme && currentCategory != Type.Configs)
            drawObjects(matrixStack, mouseX, mouseY);
        if (currentCategory != Type.Theme && currentCategory != Type.Configs) {
            StencilUtil.initStencilToWrite();
            RenderUtil.Render2D.drawRoundedCorner(position.x, position.y, size.x, size.y, new Vector4f(8.5f, 8.5f, 8.5f, 8.5f), -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.Render2D.drawVertical(position.x + bar, position.y + size.y - 30, size.x - bar, 30, ColorUtil.rgba(16, 17, 21, 255), ColorUtil.rgba(16, 17, 21, 0));
            RenderUtil.Render2D.drawVertical(position.x + bar, position.y, size.x - bar, 30, ColorUtil.rgba(16, 17, 21, 0), ColorUtil.rgba(16, 17, 21, 255));

            StencilUtil.uninitStencilBuffer();
        }

        if (currentCategory == Type.Configs) {
            StencilUtil.initStencilToWrite();
            RenderUtil.Render2D.drawRoundedCorner(position.x, position.y, size.x, size.y, new Vector4f(8.5f, 8.5f, 8.5f, 8.5f), -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.Render2D.drawVertical(position.x + bar, position.y + size.y - 30, size.x - bar, 30, ColorUtil.rgba(16, 17, 21, 255), ColorUtil.rgba(16, 17, 21, 0));

            StencilUtil.uninitStencilBuffer();
        }

        scrollingOut = AnimationMath.fast(scrollingOut, scrolling, 15);

        StencilUtil.initStencilToWrite();
        RenderUtil.Render2D.drawRoundedCorner(position.x, position.y, size.x, size.y, new Vector4f(8.5f, 8.5f, 8.5f, 8.5f), -1);
        StencilUtil.readStencilBuffer(0);
        if (Managment.FUNCTION_MANAGER.clickGui.glow.get()) {
            RenderUtil.Render2D.drawShadow(position.x, position.y, size.x, size.y, 50, ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(90), ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));
            RenderUtil.Render2D.drawGradientRound(position.x - 0.25F, position.y - 0.25f, size.x + 0.5f, size.y + 0.5f, 7F, ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(90), ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));
        }
        StencilUtil.uninitStencilBuffer();

        for (Object object : objects) {
            if (object instanceof ModuleObject m) {
                m.bindWindow.render(matrixStack, mouseX, mouseY);
                for (Object object1 : m.object) {
                    if (object1 instanceof BindObject no) {
                        no.bindWindow.render(matrixStack, mouseX, mouseY);
                    }
                }
            }
        }
        mc.gameRenderer.setupOverlayRendering();

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (currentCategory == Type.Configs)
            configDrawing.charTyped(codePoint);
        if (configDrawing.searching && configDrawing.search.length() < 23) {
            configDrawing.search += codePoint;
        }
        if (searching && searchText.length() < 13) {
            searchText += codePoint;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleObject m : objects) {
            if (m.function.category == currentCategory)
                m.keyTyped(keyCode, scanCode, modifiers);
        }
        if (currentCategory == Type.Configs)
            configDrawing.keyTyped(keyCode);
        if (configDrawing.searching) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!configDrawing.search.isEmpty())
                    configDrawing.search = configDrawing.search.substring(0, configDrawing.search.length() - 1);
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                configDrawing.searching = false;
            }
        }
        if (searching) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (!searchText.isEmpty())
                    searchText = searchText.substring(0, searchText.length() - 1);
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                searching = false;
            }
        }
        for (Object object : objects) {
            if (object instanceof ModuleObject m) {
                m.bindWindow.keyPress(keyCode);
                for (Object object1 : m.object) {
                    if (object1 instanceof BindObject no) {
                        no.bindWindow.keyPress(keyCode);

                        if (no.bindWindow.binding && keyCode == GLFW.GLFW_KEY_ESCAPE) {
                            return false;
                        }
                    }
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        for (ModuleObject m : objects) {
            if (searching || !searchText.isEmpty()) {
                if (!searchText.isEmpty())
                    if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
                m.mouseReleased((int) mouseX, (int) mouseY, button);
            } else {
                if (m.function.category == currentCategory)
                    m.mouseReleased((int) mouseX, (int) mouseY, button);
            }
        }
        for (Object object : objects) {
            if (object instanceof ModuleObject m) {
                m.bindWindow.mouseUn();
                for (Object object1 : m.object) {
                    if (object1 instanceof BindObject no) {
                        no.bindWindow.mouseUn();
                    }
                }
            }

        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        super.onClose();
        searching = false;
        for (ModuleObject m : objects) {
            m.exit();
        }
    }

    public void drawObjects(MatrixStack stack, int mouseX, int mouseY) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        List<ModuleObject> first = objects.stream()
                .filter(moduleObject -> (!searchText.isEmpty()) || moduleObject.function.category == currentCategory)
                .filter(moduleObject -> objects.indexOf(moduleObject) % 2 == 0)
                .toList();

        List<ModuleObject> second = objects.stream()
                .filter(moduleObject -> (!searchText.isEmpty()) || moduleObject.function.category == currentCategory)
                .filter(moduleObject -> objects.indexOf(moduleObject) % 2 != 0)
                .toList();


        RenderUtil.SmartScissor.push();
        RenderUtil.SmartScissor.setFromComponentCoordinates(position.x, position.y, size.x, size.y - 1);
        float offset = scrollingOut;
        float sizePanel = 0;
        for (ModuleObject object : first) {
            if (!searchText.isEmpty())
                if (!object.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
            object.x = position.x + 110;
            object.y = position.y + 10 + offset;
            object.width = 160;
            object.height = 22;
            for (Object object1 : object.object) {
                if (object1.setting.visible()) {
                    object.height += object1.height;
                }
            }
            object.height += 3;
            if (!(object.y - object.height - 50 > size.y))
                object.draw(stack, mouseX, mouseY);
            offset += object.height += 5;
            sizePanel += object.height += 5;
        }
        float sizePanel1 = 0;
        offset = scrollingOut;
        for (ModuleObject object : second) {
            if (!searchText.isEmpty())
                if (!object.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
            object.x = position.x + 280;
            object.y = position.y + 10 + offset;
            object.width = 160;
            object.height = 22;
            for (Object object1 : object.object) {
                if (object1.setting.visible()) {
                    object.height += object1.height;
                }
            }
            object.height += 3;
            if (!(object.y - object.height - 50 > size.y))
                object.draw(stack, mouseX, mouseY);
            offset += object.height += 5;
            sizePanel1 += object.height += 5;
        }
        float max = Math.max(sizePanel, sizePanel1);
        if (max < size.y) {
            scrolling = 0;
        } else {
            scrolling = MathHelper.clamp(scrolling, -(max - size.y), 0);
        }
        BloomHelper.draw(6, 1.5f, false);
        RenderUtil.SmartScissor.unset();
        RenderUtil.SmartScissor.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        if (currentCategory == Type.Theme) {
            themeDrawing.click((int) mouseX, (int) mouseY, button);
        }
        if (currentCategory == Type.Configs) {
            configDrawing.click((int) mouseX, (int) mouseY, button);
        }
        float bar = 100;

        float len = 0;
        for (Type type : Type.values()) {
            len = type.ordinal() * 20;
            if (type == Type.Theme || type == Type.Configs) {
                len += 10;
            }
            if (RenderUtil.isInRegion(mouseX, mouseY, position.x + 5f, position.y + 55 + len, bar - 10, 15)) {
                currentCategory = type;
                searching = false;
            }
        }

        for (ModuleObject m : objects) {
            if (searching || !searchText.isEmpty()) {
                if (!searchText.isEmpty())
                    if (!m.function.name.toLowerCase().contains(searchText.toLowerCase())) continue;
                m.mouseClicked((int) mouseX, (int) mouseY, button);
            } else {
                if (m.function.category == currentCategory)
                    m.mouseClicked((int) mouseX, (int) mouseY, button);
            }

        }
        for (Object object : objects) {
            if (object instanceof ModuleObject m) {
                m.bindWindow.mouseClick((int) mouseX, (int) mouseY, button);
                for (Object object1 : m.object) {
                    if (object1 instanceof BindObject no) {
                        no.bindWindow.mouseClick((int) mouseX, (int) mouseY, button);
                    }
                }
            }
        }

        if (RenderUtil.isInRegion(mouseX, mouseY, position.x + 7.5f, position.y + 30, bar - 15, 17)) {
            searching = !searching;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
