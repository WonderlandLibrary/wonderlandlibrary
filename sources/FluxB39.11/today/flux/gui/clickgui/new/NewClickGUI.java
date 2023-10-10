package today.flux.gui.crink;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.AbstractGuiScreen;
import today.flux.gui.clickgui.classic.BlurUtil;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.configMgr.ConfigUI;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.irc.IRCClient;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.CGUI;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.*;
import today.flux.utility.AnimationUtils;
import today.flux.utility.ColorUtils;
import today.flux.utility.SmoothAnimationTimer;

import java.awt.*;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class NewClickGUI extends AbstractGuiScreen {
    public static int config_X = -1;
    public static int config_Y = -1;
    public static float config_W = -1;
    public static float config_H = -1;

    public int x = -1;
    public int y = -1;

    public int x2;
    public int y2;
    public boolean drag = false;
    public boolean drag_resize = false;
    public boolean hoveredResizeArea = false;
    private final MouseHandler handler = new MouseHandler(0);

    // 窗口大小
    public float windowWidth = -1;
    public float windowHeight = -1;

    // 动画
    public float categorySelectorY = -1;
    public float modeButtonAnimation = 0;
    public Module lastModule = null;
    public CategoryButton currentCatButton = null;
    public Module currentModule = null;
    public ArrayList<CategoryButton> categoryButtons = new ArrayList<CategoryButton>();
    public ArrayList<CategoryModuleList> categoryModuleLists = new ArrayList<CategoryModuleList>();
    public ConfigUI cfgui;

    public NumberFormat nf = new DecimalFormat("0000");

    public NewClickGUI() {
        for (Category c : Category.values()) {
            // 新建Category Button
            CategoryButton button = new CategoryButton(c);
            this.categoryButtons.add(button);

            // 新建Category Module List
            CategoryModuleList list = new CategoryModuleList(c);
            this.categoryModuleLists.add(list);
        }

        cfgui = new ConfigUI(this);
    }

    public void initGui() {
        ScaledResolution res = new ScaledResolution(mc);

        if (windowWidth < 270) {
            if (config_W < 270) {
                config_W = 395;
            }
            windowWidth = config_W;
        }
        if (windowHeight < 250) {
            if (config_H < 250) {
                config_H = 280;
            }
            windowHeight = config_H;
        }

        if (x < 0) {
            if (config_X < 0) {
                config_X = (int) ((res.getScaledWidth() - windowWidth) / 2);
            }
            x = config_X;
        }

        if (y < 0) {
            if (config_Y < 0) {
                config_Y = (int) ((res.getScaledHeight() - windowHeight) / 2);
            }
            y = config_Y;
        }

        try {
            int min = org.lwjgl.input.Cursor.getMinCursorSize();
            IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
            Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            Mouse.setNativeCursor(emptyCursor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cfgui.init();
    }

    @Override
    public void drawScr(int mouseX, int mouseY, float partialTicks) {
        this.scale = 2;
        if (!Hud.NoShader.getValueState() && !Config.isFastRender()) {
            BlurUtil.blurAll(Hud.blurIntensity.getValue());
        }

        // 配置窗口
        cfgui.render(mouseX, mouseY, partialTicks, this);

        // 标题渲染
        RenderUtil.drawRoundedRect(x, y, x + windowWidth, y + windowHeight, Hud.isLightMode ? 0xFFF1F1F1 : 0xff32353b);
        this.drawRoundedRect(x, y, x + 100, y + windowHeight, Hud.isLightMode ? 0xFFFFFFFF : 0xff2f3136);

        FontManager.icon25.drawString("q", x + 8f, y + 11f, Hud.isLightMode ? 0xFF465CFF : new Color(255, 255, 255).getRGB());
        FontManager.vision30.drawString(Flux.NAME.toUpperCase(), x + 24f, y + 10f, Hud.isLightMode ? 0xFF465CFF : new Color(255, 255, 255).getRGB());
        FontManager.baloo16.drawString("b" + Flux.VERSION, x + 62f, y + 13.5f, Hud.isLightMode ? 0XFFC4C4C4 : new Color(220, 221, 222).getRGB());

        this.drawGradientSideways(x + 100, y - 0.5f, x + 110, y + windowHeight + 0.5f,
                RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.15f), RenderUtil.reAlpha(ColorUtils.BLACK.c, 0f));

        // 空按钮处理
        if (currentCatButton == null) {
            currentCatButton = categoryButtons.get(0);
        }

        // 按钮选中动画处理
        if (drag || categorySelectorY <= -1) {
            this.categorySelectorY = this.currentCatButton.y;
        } else {
            this.categorySelectorY = AnimationUtils.getAnimationState(this.categorySelectorY, this.currentCatButton.y,
                    (float) (Math.max(10, (Math.abs(this.categorySelectorY - this.currentCatButton.y)) * 35) * 0.3));
        }

        // 选中Category四边形
        RenderUtil.drawRect(x - 0.5f, categorySelectorY, x + 100f, categorySelectorY + 25, 0xff4286f5);

        // Category底色
        float startY = y + 35;
        for (CategoryButton catButton : this.categoryButtons) {
            catButton.drawBackground(x, startY);
            startY += 25;
        }

        // Category已选中色
        float startY2 = y + 35;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        doGlScissor(x, (int) categorySelectorY, 99, 25);
        for (CategoryButton catButton : this.categoryButtons) {
            catButton.draw(x, startY2);
            startY2 += 25;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        //Totally shit
        int col1 = Hud.isLightMode ? 0xffe9ebed : 0xff292b2f;
        int col2 = Hud.isLightMode ? 0xff000000 : 0xffffffff;
        int col3 = Hud.isLightMode ? 0xff636b6d : 0xffb0b2ba;
        int col4 = Hud.isLightMode ? 0xffb0b2ba : 0xff636b6d;
        float bottom = y + windowHeight;

        RenderUtil.drawRoundedRect(x, bottom - 25, x + 99, bottom, col1);
        GuiRenderUtils.drawRect(x - .5f, bottom - 25.5f, 100.5f, 1.5f, col1);
        GuiRenderUtils.drawRect(x + 99, bottom - 25f, 1f, 25.5f, col1);

        RenderUtil.arc(x + 14, bottom - 12f, 0, 360, 10.5f, col4);
        RenderUtil.drawCircleWithTexture(x + 14, bottom - 12f, 0, 360, 10, Flux.avatarLocation.getResourceLocation(), ColorUtils.WHITE.c);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        this.doGlScissor(x, (int) (bottom - 25), 100, 25);
        FontManager.sans16.drawString(IRCClient.loggedPacket.getRealUsername(), x + 30, bottom - 22f, col2);
        FontManager.sans14.drawString("#" + nf.format(IRCClient.loggedPacket.getUid()), x + 30, bottom - 12f, col3);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        cfgui.drawGradientSideways(x + 60, bottom - 25, x + 100, bottom, RenderUtil.reAlpha(col1, 0f), RenderUtil.reAlpha(col1, 1f));
        //End

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        doGlScissor(x - 1, y, windowWidth, windowHeight);

        // 画Module List
        for (CategoryModuleList list : categoryModuleLists) {
            if (list.cat == currentCatButton.cat) {
                list.draw(x + 100, y, mouseX, mouseY);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        this.move(mouseX, mouseY);

        final float xx = mouseX - 0.5f;
        final float yy = mouseY - 0.5f;

        float shadowPosX = hoveredResizeArea ? xx - 1.8f - 1 : xx + 0.8f - 1;
        float shadowPosY = hoveredResizeArea ? yy - 4.5f : yy + 0.5f;
        float cursorPosX = hoveredResizeArea ? xx - 4 : xx - 1;
        float cursurPosY = hoveredResizeArea ? yy - 5 : yy;
        FontManager.icon18.drawString(hoveredResizeArea ? "o" : "p", shadowPosX, shadowPosY, RenderUtil.reAlpha(0xff000000, 0.45f));
        FontManager.icon18.drawString(hoveredResizeArea ? "o" : "p", cursorPosX, cursurPosY, CGUI.cursorColours.getColor().getRGB());
    }

    private void move(int mouseX, int mouseY) {
        if (!Mouse.isButtonDown(0) && (this.drag || this.drag_resize)) {
            this.drag = false;
            this.drag_resize = false;
        }

        // Window Resize

        // RenderUtil.drawRect(x + this.windowWidth - 6, y + this.windowHeight - 6, x +
        // this.windowWidth, y + this.windowHeight, ColorUtils.BLACK.c);
        FontManager.icon10.drawString("l", x + this.windowWidth - 6, y + this.windowHeight - 6, ColorUtils.GREY.c);
        if (isHovering(mouseX, mouseY, x + this.windowWidth - 6, y + this.windowHeight - 6, x + this.windowWidth, y + this.windowHeight)) {
            hoveredResizeArea = true;
            if (this.handler.canExcecute()) {
                this.drag_resize = true;
                this.x2 = mouseX;
                this.y2 = mouseY;
            }
        } else {
            hoveredResizeArea = false;
        }

        if (this.drag_resize) {
            hoveredResizeArea = true;
            this.windowWidth += mouseX - x2;
            if (windowWidth < 270) {
                windowWidth = 270;
            } else {
                this.x2 = mouseX;
            }
            this.windowHeight += mouseY - y2;
            if (windowHeight < 285) {
                windowHeight = 285;
            } else {
                this.y2 = mouseY;
            }
        }

        // Window Drag
        if (isHovering(mouseX, mouseY, x, y, x + 99, y + 34) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }

        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        for (CategoryButton c : this.categoryButtons) {
            c.onClick(mouseX, mouseY);
        }

        cfgui.onMouseClicked(mouseX, mouseY, mouseButton, this);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {

        try {
            Mouse.setNativeCursor(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cfgui.onClose();

        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawRoundedRect(float left, float top, float right, float bottom, int color) {
        //Left
        RenderUtil.drawRect(left - 0.5f, top + 0.5f, left, bottom - 0.5f, color);
        //Top
        RenderUtil.drawRect(left + 0.5f, top + 0.5f, right - 0.5f, top, color);
        //Bottom
        RenderUtil.drawRect(left + 0.5f, bottom, right - 0.5f, bottom + 0.5f, color);
        RenderUtil.drawRect(left, top, right, bottom, color);
    }

    public class CategoryButton {

        public Category cat;

        public float x;
        public float y;

        public CategoryButton(Category category) {
            cat = category;
        }

        public void draw(float x, float y) {
            FontManager.baloo16.drawString(cat.name(), x + 25f, y + 6.5f, ColorUtils.WHITE.c);
            FontManager.icon15.drawString(cat.icon, x + 10f, y + 8f, ColorUtils.WHITE.c);
        }

        public void drawBackground(float x, float y) {
            this.x = x;
            this.y = y;

            FontManager.baloo16.drawString(cat.name(), x + 25f, y + 6.5f, ColorUtils.GREY.c);
            FontManager.icon15.drawString(cat.icon, x + 10f, y + 8f, ColorUtils.GREY.c);
        }

        public void onClick(int mouseX, int mouseY) {
            if (isHovering(mouseX, mouseY, x - 0.5f, y, x + 99.5f, y + 25)) {
                if (Flux.INSTANCE.getNewClickGUI().currentCatButton != this) {
                    Flux.INSTANCE.getNewClickGUI().currentCatButton = this;
                    for (Module module : ModuleManager.getModList()) {
                        module.toggleButtonAnimation = module.isEnabled() ? 218 : 222;
                        module.ySmooth.setValue(0);
                    }
                }
            }
        }
    }

    public boolean isHovering(float mouseX, float mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > x && mouseX < x + windowWidth && mouseY > y && mouseY < y + windowHeight && mouseX > xLeft
                && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    public class CategoryModuleList {
        public boolean isReleased = true;
        FloatValue draggingFloat = null;

        public Category cat;
        public float x;
        public float y;

        public float minY = -100;

        public float scrollY = 0;
        public SmoothAnimationTimer scrollAnimation = new SmoothAnimationTimer(0);

        public CategoryModuleList(Category category) {
            cat = category;
        }

        public void draw(float x, float y, float mouseX, float mouseY) {
            if (!Mouse.isButtonDown(0)) {
                draggingFloat = null;
                isReleased = true;
            }

            FontUtils font = FontManager.baloo16;

            this.x = x;
            this.y = y;

            if(RenderUtil.isHovering(mouseX, mouseY, x, y, x + (windowWidth - 100), y + windowHeight)) {
                int wheel = Mouse.getDWheel() / 2;

                scrollY += wheel;
                if (scrollY <= minY)
                    scrollY = minY;
                if (scrollY >= 0)
                    scrollY = 0;

                minY = windowHeight - 10;
                scrollAnimation.setTarget(scrollY);
                boolean isScrolling = !scrollAnimation.update(true);
            } else {
                Mouse.getDWheel(); //用于刷新滚轮数据
            }

            x += 30;
            y += 25 + scrollAnimation.getValue();

            for (Module module : ModuleManager.getModList()) {
                if (module.getCategory() == cat || cat == Category.Global) {
                    if (isHovering(mouseX, mouseY, x - 10f, y - 12.5f, x + windowWidth - 150f, y + 12.5f)
                            && !isHovering(mouseX, mouseY, x + windowWidth - 400f + 216f, y - 5,
                            x + windowWidth - 400f + 232f, y + 5)
                            && (Mouse.isButtonDown(0) && isReleased) && currentModule != module
                            && cat != Category.Global) {
                        isReleased = false;
                        currentModule = module;
                        if (currentModule.getMode() != null) {
                            currentModule.getMode().getAlphaTimer().setValue(0);
                        }

                        for (Value value : ValueManager.getValues()) {
                            if (value instanceof FloatValue) {
                                ((FloatValue) value).getAnimationTimer().setValue(0);
                            }
                        }
                    }

                    module.toggleButtonAnimation = AnimationUtils.getAnimationState(module.toggleButtonAnimation,
                            module.isEnabled() ? 222f : 218f,
                            (float) (Math.max(10,
                                    (Math.abs(module.toggleButtonAnimation - (module.isEnabled() ? 222f : 218f))) * 35)
                                    * 0.3));
                    if (module == currentModule || cat == Category.Global) { // TODO: 被展开
                        float moduleYShouldBe = 0;

                        if (module != lastModule) {
                            modeButtonAnimation = 0;
                            lastModule = module;
                        }

                        // Module窗口
                        RenderUtil.drawRoundedRect(x - 10.5f, y - 13f, x + windowWidth - 149.5f,
                                y + 13f + module.ySmooth.getValue(), 0xff4286f5);
                        RenderUtil.drawRoundedRect(x - 10f, y - 12.5f, x + windowWidth - 150f,
                                y + 12.5f + module.ySmooth.getValue(),
                                Hud.isLightMode ? 0xFFFFFFFF : 0xff40444b);
                        FontManager.icon10.drawString("n", x, y - 3f, 0xff4286f5);
                        font.drawString(cat == Category.Global ? "Global" : module.getName(), x + 10, y - 6,
                                0xff4286f5);
                        FontManager.icon15.drawString("h", x + windowWidth - 150 - 12, y - 4, 0xff4286f5);

                        if (cat != Category.Global && !module.cantToggle) {
                            RenderUtil.drawRoundedRect(x + windowWidth - 400f + 218f, y - 3,
                                    x + windowWidth - 400f + 230f, y + 3, 2.5f, 0xffe5e5e5);
                            GuiRenderUtils.drawRoundedRect(x + windowWidth - 400f + module.toggleButtonAnimation,
                                    y - 4f, 8, 8, 360, module.isEnabled() ? 0xff4286f5 : 0xffffffff, 1f,
                                    module.isEnabled() ? 0xff4286f5 : 0xff9f9f9f);

                            if (isHovering(mouseX, mouseY, x + windowWidth - 400f + 216f, y - 5,
                                    x + windowWidth - 400f + 232f, y + 5) && (Mouse.isButtonDown(0) && isReleased)) {
                                isReleased = false;
                                module.setEnabled(!module.isEnabled());
                            }
                        }

                        // Render Other value
                        int sliderCount = 0;
                        int sliderX = 0;
                        int booleanCount = 0;
                        int booleanX = 5;

                        for (Value value : ValueManager.getValueByModName(cat == Category.Global ? "Global" : module.getName())) {
                            // Render Mode Value
                            int modeX = 5;
                            int modeCount = 0;
                            if (value instanceof ModeValue) {
                                moduleYShouldBe += 10;
                                ModeValue modeValue = (ModeValue) value;
                                font.drawString(value.getKey(), x - 1, y + moduleYShouldBe, 0xff4286f5);
                                for (String mode : modeValue.getModes()) {
                                    boolean isCurrentMode = modeValue.isCurrentMode(mode);
                                    if (modeX + font.getStringWidth(mode) + 20 >= windowWidth - 140) {
                                        modeX = 5;
                                        moduleYShouldBe += 15;
                                        modeCount = 0;
                                    }

                                    if (isHovering(mouseX, mouseY, x + modeX - 3, y + moduleYShouldBe + 8,
                                            x + modeX + font.getStringWidth(mode) + 5, y + moduleYShouldBe + 23)
                                            && (Mouse.isButtonDown(0) && isReleased) && !isCurrentMode) {
                                        isReleased = false;
                                        modeValue.setValue(mode);
                                        modeValue.getAlphaTimer().setValue(0);
                                    }

                                    modeValue.getAlphaTimer().update(true);
                                    float curX = x + modeX;
                                    float curY = y + moduleYShouldBe;

                                    // GuiRenderUtils.drawRoundedRect(curX - 5f, curY + 13f, 6, 6, 6,0xff1a73e8, 1f,
                                    // 0xff1a73e8);
                                    FontManager.icon15.drawString("n", curX - 6f, curY + 12f, 0xff4286f5);
                                    if (isCurrentMode) {
                                        modeButtonAnimation = AnimationUtils.getAnimationState(modeButtonAnimation, 2.1f, (float) (Math.max(1, (Math.abs(modeButtonAnimation - 2.1f)) * 25) * 0.3));
                                        RenderUtil.drawCircle(curX - 2.2f, curY + 16.3f, 0, 360, modeButtonAnimation, 0xff4286f5);
                                        //FontManager.icon10.drawString("k", curX - 4.8f, curY + 13.4f, 0xff4286f5);
                                    }

                                    font.drawString(mode, x + modeX + 4, y + moduleYShouldBe + 10f, 0xff4286f5);
                                    modeCount++;
                                    modeX += font.getStringWidth(mode) + 20;
                                }
                                if (modeCount >= 1)
                                    moduleYShouldBe += 15;
                            }
                        }

                        for (Value value : ValueManager
                                .getValueByModName(cat == Category.Global ? "Global" : module.getName())) {
                            // 滑条
                            if (value instanceof FloatValue) {
                                FloatValue floatValue = (FloatValue) value;

                                float x1 = x + sliderX;

                                float dymX = 110;

                                float percentShould = (floatValue.getValueState() - floatValue.getMin())
                                        / (floatValue.getMax() - floatValue.getMin());

                                floatValue.getAnimationTimer().setTarget(percentShould * 10);
                                floatValue.getAnimationTimer().update(true);

                                float percent = floatValue.getAnimationTimer().getValue() / 10;

                                float y1 = y + moduleYShouldBe + 23;
                                float y2 = y + moduleYShouldBe + 24;

                                font.drawString(value.getKey(), x + sliderX, y + 9 + moduleYShouldBe, 0xff9d9d9d);
                                String round = Math.round(floatValue.getValueState() * 100f) / 100f + "";
                                font.drawString(round + (floatValue.getUnit() == null ? "" : floatValue.getUnit()),
                                        x + sliderX + dymX
                                                - font.getStringWidth(round
                                                + (floatValue.getUnit() == null ? "" : floatValue.getUnit())),
                                        y + 9 + moduleYShouldBe, 0xff9d9d9d);

                                RenderUtil.drawRect(x1, y1, x1 + dymX, y2, 0xffe3e3e3);
                                RenderUtil.drawRect(x1, y1, x1 + dymX * percent, y2, 0xff4286f5);
                                RenderUtil.circle(x1 + dymX * percent, (y1 + y2) / 2, 2f, 0xff4286f5);

                                if (isHovering(mouseX, mouseY, x1, y1 - 4, x1 + dymX, y2 + 4) && Mouse.isButtonDown(0)
                                        && isReleased) {
                                    isReleased = false;
                                    draggingFloat = floatValue;
                                }

                                if (draggingFloat == floatValue) {
                                    float draggingValue = mouseX - x1;
                                    if (draggingValue > 110)
                                        draggingValue = 110;
                                    if (draggingValue < 0)
                                        draggingValue = 0;
                                    float curValue = Math
                                            .round(((draggingValue / 110f) * (floatValue.getMax() - floatValue.getMin())
                                                    + floatValue.getMin()) / floatValue.getIncrement())
                                            * floatValue.getIncrement();
                                    floatValue.setValue(curValue);
                                }
                                sliderX += 125;
                                sliderCount++;
                                if (sliderX + dymX >= windowWidth - 150) {
                                    moduleYShouldBe += 20;
                                    sliderCount = 0;
                                    sliderX = 0;
                                }

                            }
                        }

                        if (sliderCount > 0) {
                            moduleYShouldBe += 25;
                        }

                        // Render Boolean Value
                        for (Value value : ValueManager
                                .getValueByModName(cat == Category.Global ? "Global" : module.getName())) {
                            if (value instanceof BooleanValue) {
                                BooleanValue booleanValue = (BooleanValue) value;
                                if (booleanX + font.getStringWidth(booleanValue.getKey()) + 20 >= windowWidth - 140) {
                                    booleanX = 5;
                                    moduleYShouldBe += 15;
                                    booleanCount = 0;
                                }

                                boolean isChecked = booleanValue.getValueState();
                                if (isHovering(mouseX, mouseY, x + booleanX - 3, y + moduleYShouldBe + 8,
                                        x + booleanX + font.getStringWidth(booleanValue.getKey()) + 5,
                                        y + moduleYShouldBe + 23) && (Mouse.isButtonDown(0) && isReleased)) {
                                    isReleased = false;
                                    booleanValue.setValue(!booleanValue.getValueState());
                                }

                                FontManager.icon15.drawString("j", x + booleanX - 5f, y + moduleYShouldBe + 11.5f,
                                        isChecked ? 0xff4286f5 : 0xffcdcdcd);

                                font.drawString(booleanValue.getKey(), x + booleanX + 4, y + 10f + moduleYShouldBe,
                                        isChecked ? 0xff4286f5 : 0xff9c9c9c);
                                booleanCount++;
                                booleanX += font.getStringWidth(booleanValue.getKey()) + 20;
                            }
                        }

                        if (booleanCount >= 1)
                            moduleYShouldBe += 15;

                        if (moduleYShouldBe == 0) {
                            moduleYShouldBe = 15;
                            font.drawString("No Settings.", x, y + 12, 0xff9d9d9d);
                        }

                        // Do Smooth
                        module.ySmooth.setTarget(moduleYShouldBe);
                        module.ySmooth.update(true);
                        y += module.ySmooth.getValue();
                        minY -= moduleYShouldBe + 40;

                        if (cat == Category.Global)
                            break;
                    } else {

                        RenderUtil.drawRoundedRect(x - 10f, y - 12.5f, x + windowWidth - 150f, y + 12.5f + module.ySmooth.getValue(), Hud.isLightMode ? 0xFFFFFFFF : 0xff40444b);
                        FontManager.icon10.drawString("k", x, y - 3f, 0xffd2d2d2);
                        FontManager.baloo16.drawString(module.getName(), x + 10, y - 6, 0xff9d9d9d);
                        FontManager.icon15.drawString("i", x + windowWidth - 150f - 12, y - 4, 0xff9d9d9d);

                        if (!module.cantToggle) {
                            RenderUtil.drawRoundedRect(x + windowWidth - 400f + 218f, y - 3, x + windowWidth - 400f + 230f,
                                    y + 3, 2.5f, 0xffe5e5e5);
                            GuiRenderUtils.drawRoundedRect(x + windowWidth - 400f + module.toggleButtonAnimation, y - 4f, 8,
                                    8, 360, module.isEnabled() ? 0xff4286f5 : 0xffffffff, 1f,
                                    module.isEnabled() ? 0xff4286f5 : 0xff9f9f9f);

                            if (isHovering(mouseX, mouseY, x + windowWidth - 400f + 216f, y - 5,
                                    x + windowWidth - 400f + 232f, y + 5) && (Mouse.isButtonDown(0) && isReleased)) {
                                isReleased = false;
                                module.setEnabled(!module.isEnabled());
                            }
                        }

                        // Do Smooth
                        module.ySmooth.update(false);
                        y += module.ySmooth.getValue();
                        minY -= 40;
                    }
                    y += 40;
                }
            }
        }
    }

    public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
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
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, this.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(left, top, this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static class MouseHandler {
        public boolean clicked;
        private final int button;

        public MouseHandler(int key) {
            this.button = key;
        }

        public boolean canExcecute() {
            if (Mouse.isButtonDown(this.button)) {
                if (!this.clicked) {
                    this.clicked = true;
                    return true;
                }
            } else {
                this.clicked = false;
            }
            return false;
        }
    }
}
