package me.aquavit.liquidsense.ui.client.clickgui.neverlose;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud.HUD;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.model.ModelMain;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.about.AboutMain;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue.Editbox;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.module.DrawModule;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.ui.font.GameFontRenderer;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl.*;
import static me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue.Editbox.newvalue;

public class Main extends GuiScreen {

    public List<Module> modules = Collections.emptyList();

    private ArrayList<Category> rendercategory = new ArrayList<>();

    public static boolean hovermove = false;
    private boolean ismove = false;

    private int lastmouseX = 0;
    private int lastmousey = 0;

    private float x = 0f;
    private float y = 0f;

    public Translate lwheeltranslate = new Translate(0f, 0f);

    public Translate rwheeltranslate = new Translate(0f, 0f);

    public boolean mouseLDown = false;
    public boolean mouseRDown = false;

    public Main() {
        int posy = 0;
        for (int the = 0; the < ModuleCategory.values().length; the++) {
            String[] categoryname = new String[]{"Ghost", "Misc", "HUD"};
            String[] expandName = new String[]{"Combat", "Utility", "Miscellaneous"};
            String expand = "";

            int y = 18;

            for (int index = 0; index < categoryname.length; index++) {
                if (ModuleCategory.values()[the].displayName.equals(categoryname[index])) {
                    expand = expandName[index];
                    y = 30;
                }
            }

            Category category = new Category(23f, 50f + posy, ModuleCategory.values()[the], expand);

            rendercategory.add(category);
            posy += y;
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.clickGuiConfig);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tick) {
        clickGuiDrag(mouseX, mouseY);
        hudDrag(mouseX, mouseY);

        RenderUtils.drawNLRect(coordinateX, coordinateY, coordinateX + 95f, coordinateY + 345f, 3f, LeftRectBackgroundColor()); //左侧矩形背景
        RenderUtils.drawNLRect(coordinateX + 95f, coordinateY, coordinateX + 460f, coordinateY + 345f, 3f, getRightColorOfLeftRectWithRightRectBackgroundColor()); //右侧矩形背景
        RenderUtils.drawRect(coordinateX + 93f, coordinateY, coordinateX + 94f, coordinateY + 345f, getLeftRectVerticalWithUnderColor()); //左侧矩形竖直线条
        RenderUtils.drawRect(coordinateX + 94f, coordinateY, coordinateX + 97f, coordinateY + 345f, getRightColorOfLeftRectWithRightRectBackgroundColor()); //左侧矩形竖直线条的右面
        RenderUtils.drawRect(coordinateX + 93f, coordinateY + 34f, coordinateX + 460f, coordinateY + 35f, getLeftRectVerticalWithUnderColor()); //设置条下面横线条

        switch(hue) {
            case "blue":
            case "black":
                Fonts.bold26.drawString("liquidsense".toUpperCase(), coordinateX + 4, coordinateY + 13f, new Color(11, 160, 255).getRGB());
                Fonts.font26.drawString("liquidsense".toUpperCase(), coordinateX + 5, coordinateY + 13f, new Color(255, 255, 255).getRGB());
                break;
            default:
                Fonts.font26.drawString("liquidsense".toUpperCase(), coordinateX + 5, coordinateY + 13f, new Color(51,51,51).getRGB());
                break;
        }

        int rectColor;
        int textColor;
        switch (hue) {
            case "blue":
            case "black":
                rectColor = new Color(4, 120, 176).getRGB();
                textColor = -1;
                break;
            default:
                rectColor = new Color(240, 245, 248).getRGB();
                textColor = new Color(1, 1, 1).getRGB();
                break;
        }

        float rectX1 = coordinateX + 5f;
        float rectY1 = coordinateY + 280f;
        float rectX2 = coordinateX + 89f;
        float rectY2 = coordinateY + 300f;

        RenderUtils.drawShader(rectX1, rectY1, 84f, 20f);
        RenderUtils.drawRect(rectX1, rectY1, rectX2, rectY2, rectColor);
        Fonts.font20.drawCenteredString("CustomHUD", coordinateX + 45, coordinateY + 287f, textColor, false);

        if (hoverConfig(rectX1, rectY1, rectX2, rectY2, mouseX, mouseY, true)) {
            mc.displayGuiScreen(new GuiHudDesigner());
        }

        renderHead();

        Fonts.font14.drawString("AquaVit", coordinateX + 37, coordinateY + 316, textColor);

        Fonts.font14.drawString("Till:", coordinateX + 37, coordinateY + 328, getTillColor());

        Fonts.font14.drawString(
                new SimpleDateFormat("dd.MM").format(System.currentTimeMillis()) + " " +
                        new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                coordinateX + 52, coordinateY + 328, new Color(2, 169, 245).getRGB()
        );

        for (Category render : rendercategory) {
            render.drawCategory(mouseX, mouseY, this);
        }

        //drawConfig.flie(mouseX, mouseY, this);
        DrawModule.drawModule(mouseX, mouseY, this);
        Fonts.csgo40.drawString("G" , coordinateX + 410 , coordinateY + 14 , textColor);
        Fonts.csgo40.drawString("E", coordinateX + 395, coordinateY + 14, textColor);
        if (!openSearch) AboutMain.drawAbout(mouseX, mouseY, this);
        if (!openSearch) ModelMain.drawModel(mouseX, mouseY, this);
        if (hoverConfig(coordinateX + 395, coordinateY + 14, coordinateX + 405, coordinateY + 26, mouseX, mouseY, true)) openSearch = !openSearch;

        if (openSearch) {
            display++;

            RenderUtils.drawRectBordered(
                    coordinateX + 290.0,
                    coordinateY + 8.0,
                    coordinateX + 420.0,
                    coordinateY + 25.0,
                    1.0,
                    getRectColor(),
                    new Color(16, 19, 26).getRGB());

            String search = display <= 100 ? Search + " |" : Search;
            drawText(
                    search,
                    30,
                    Fonts.font20,
                    (int) coordinateX + 292,
                    (int) coordinateY + 14,
                    getTextColor());

            if (display >= 200) display = 0;
        }

        mouseLDown = Mouse.isButtonDown(0);
        mouseRDown = Mouse.isButtonDown(1);
        midclick = Mouse.isButtonDown(2);
    }

    private int getTextColor(){
        int textColor;
        switch (hue) {
            case "blue":
            case "black":
                textColor = new Color(255, 255, 255).getRGB();
                break;
            default:
                textColor = new Color(2, 5, 12).getRGB();
        }
        return textColor;
    }
    private int getRectColor(){
        int rectColor;
        switch (hue) {
            case "white":
                rectColor = new Color(255, 255, 255).getRGB();
                break;
            case "black":
                rectColor = new Color(8, 8, 8).getRGB();
                break;
            default:
                rectColor = new Color(2, 5, 12).getRGB();
        }
        return rectColor;
    }

    private int getTillColor() {
        int tillColor;
        switch (hue) {
            case "white":
                tillColor = new Color(140, 140, 140).getRGB();
                break;
            case "black":
                tillColor = new Color(70, 70, 70).getRGB();
                break;
            default:
                tillColor = new Color(46, 67, 81).getRGB();
                break;
        }
        return tillColor;
    }
    private int LeftRectBackgroundColor() {
        int hueColor;
        switch (hue) {
            case "white":
                hueColor = new Color(240, 245, 248).getRGB();
                break;
            case "black":
                hueColor = new Color(11, 11, 11).getRGB();
                break;
            default:
                hueColor = new Color(6, 16, 28).getRGB();
                break;
        }
        return hueColor;
    }

    private int getRightColorOfLeftRectWithRightRectBackgroundColor() {
        int hueColor;
        switch (hue) {
            case "white":
                hueColor = new Color(255, 255, 255).getRGB();
                break;
            case "black":
                hueColor = new Color(8, 8, 8).getRGB();
                break;
            default:
                hueColor = new Color(7, 7, 11).getRGB();
                break;
        }
        return hueColor;
    }

    private int getLeftRectVerticalWithUnderColor() {
        int hueColor;
        switch (hue) {
            case "white":
                hueColor = new Color(213, 213, 213).getRGB();
                break;
            case "black":
                hueColor = new Color(29, 29, 29).getRGB();
                break;
            default:
                hueColor = new Color(16, 31, 33).getRGB();
                break;
        }
        return hueColor;
    }


    int display = 0;

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE && Editbox.value == null) {
            mc.displayGuiScreen(null);
        } else if (isCtrlKeyDown() && keyCode == Keyboard.KEY_F) {
            openSearch = !openSearch;
        } else if (openSearch) {
            switch (keyCode) {
                case 14: // Backspace
                    if (checkall) {
                        Search = "";
                        checkall = false;
                    } else if (!Search.isEmpty()) {
                        Search = Search.substring(0, Search.length() - 1);
                    }
                    break;
                case 47: // V key
                    if (isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown()) {
                        Search += getClipboardString();
                    }
                    break;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        Search += typedChar;
                        lwheel = 0f;
                        rwheel = 0f;
                    }
                    break;
            }
        }

        if (isCtrlKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_A) && Search.length() > 1) checkall = !checkall;

        try {
            // 数值编辑器 intvalue flaotvalue
            if (Editbox.value != null) {
                // 删除字符串中的空格
                if (!newvalue.isEmpty()) newvalue = newvalue.replace(" ", "");

                boolean reset = keyCode == Keyboard.KEY_RETURN;

                if (Editbox.value instanceof IntegerValue) {
                    IntegerValue value = (IntegerValue) Editbox.value;
                    int max = value.maximum;
                    int min = value.minimum;
                    if (reset) {
                        //判断设置的值 不是空的
                        if (!newvalue.isEmpty()) {
                            int intValue = Integer.parseInt(newvalue);
                            //不能超过最大 不能小于最小
                            value.value = intValue > max ? max : Math.max(intValue, min);
                        }
                        //设定成功自动关闭
                        //如果是空的不设置 并且关闭
                        Editbox.value = null;
                    }
                }

                if (Editbox.value instanceof FloatValue) {
                    FloatValue value = (FloatValue) Editbox.value;
                    float max = value.maximum;
                    float min = value.minimum;
                    if (reset) {
                        //判断设置的值 不是空的
                        if (!newvalue.isEmpty()) {
                            float floatValue = Float.parseFloat(newvalue);
                            //不能超过最大 不能小于最小
                            value.value = floatValue > max ? max : Math.max(floatValue, min);
                        }
                        //设定成功自动关闭
                        //如果是空的不设置 并且关闭
                        Editbox.value = null;
                    }
                }

                //排除按键
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    // 绕过是floatvalue 就给打.
                    String enabler = Editbox.value instanceof FloatValue ? "0123456789.-" : "0123456789-";
                    for (char e : enabler.toCharArray()) {
                        //不给打第二个 . 号
                        if (newvalue.indexOf(".") >= 1 && e == '.') continue;
                        // 排除不是特定按键
                        if (typedChar == e) {
                            // 判断这个字符串里面是不是有 . 号 没有就不执行 否则空指针
                            if (newvalue.contains(".")) {
                                // 截取发现 . 号的位置到最后面
                                char[] string = newvalue.substring(newvalue.indexOf("."), newvalue.length()).toCharArray();
                                // -1是排除.号 floatvalue 后面的精确数值不能超过2位
                                if (string.length - 1 < 2) {
                                    newvalue += typedChar;
                                }
                            }
                            else {
                                newvalue += typedChar;
                            }
                        }
                    }
                }

                //删除
                if (keyCode == 14) {
                    int length = newvalue.length();
                    if (length != 0) newvalue = newvalue.substring(0, length - 1);
                }

                //取消
                if (keyCode == Keyboard.KEY_ESCAPE) {
                    Editbox.value = null;
                }
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("数值编辑器出错");
            e.printStackTrace();
        }

    }

    private boolean checkall = false;
    private boolean hoverSearch = false;

    public boolean midclick = false;
    public boolean openConfig = false;
    public boolean openSearch = false;

    public void renderHead() {
        String hue = Impl.hue;
        Color[] circleColors = {new Color(240, 245, 248), new Color(11, 11, 11), new Color(6, 16, 28)};
        int color = hue.equals("white") ? new Color(213, 213, 213).getRGB()
                : hue.equals("black") ? new Color(29, 29, 29).getRGB()
                : new Color(16, 31, 33).getRGB();

        RenderUtils.drawRect(coordinateX, coordinateY + 305, coordinateX + 94, coordinateY + 306, color); // 头像上条

        GL11.glPushMatrix();
        GL11.glTranslated(coordinateX + 4.0, coordinateY + 310.0, 0.0);
        GL11.glColor3f(1f, 1f, 1f);

        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        if (playerInfo != null) {
            ResourceLocation locationSkin = playerInfo.getLocationSkin();
            mc.getTextureManager().bindTexture(locationSkin);
            RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, 25, 25, 64F, 64F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }

        float baseRadius = 12.5f;
        float radiusIncrement = 0.275f;
        for (int i = 0; i <= 18; i++) {
            Color circleColor = circleColors[hue.equals("white") ? 0 : hue.equals("black") ? 1 : 2];
            RenderUtils.drawCircle(14.5f, 14.75f, baseRadius + (radiusIncrement * i), -180, 180, circleColor); //MC头像包围圈
            GlStateManager.resetColor();
        }

        GL11.glPopMatrix();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        HUD.handleMouseReleased();
        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.clickGuiConfig);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.clickGuiConfig);
        HUD.canDrag(false);
        HUD.isdrag = false;
        super.onGuiClosed();
    }

    public void clickGuiDrag(int mouseX, int mouseY) {
        boolean mouseDown = Mouse.isButtonDown(0);
        boolean hoverSystem = hovertoFloatL(coordinateX, coordinateY, coordinateX + 95f, coordinateY + 35f, mouseX, mouseY, false);

        if (hoverSystem && mouseDown && !HUD.isdrag) {
            hovermove = true;
            if (!ismove) {
                lastmouseX = mouseX;
                lastmousey = mouseY;
                x = coordinateX;
                y = coordinateY;
                ismove = true;
            }
        } else if (hovermove && !mouseDown) {
            hovermove = false;
            ismove = false;
        } else if (hovermove && mouseDown) {
            coordinateX = mouseX - (lastmouseX - x);
            coordinateY = mouseY - (lastmousey - y);
        }

        // 防止 clickgui 移除屏幕
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        coordinateX = Math.max(0f, Math.min(coordinateX, scaledResolution.getScaledWidth() - 460f));
        coordinateY = Math.max(0f, Math.min(coordinateY, scaledResolution.getScaledHeight() - 345f));
    }

    public void hudDrag(int mouseX, int mouseY) {
        boolean mouseClick = Mouse.isButtonDown(0);

        if (mouseClick) HUD.handleMouseClick(mouseX, mouseY);
        HUD.handleMouseMove(mouseX, mouseY);
    }



    public void drawText(String name, int size, GameFontRenderer font, int positionX, int positionY, int color) {
        StringBuilder newstring = new StringBuilder();
        char[] oldstring = name.toCharArray();
        for (int i = 0; i <= oldstring.length - 1; i++) {
            if (i < size) {
                newstring.append(oldstring[i]);
            }
        }
        font.drawString(newstring.toString(), positionX, positionY, color);
    }

    public void drawText(String name, int size, GameFontRenderer font, float positionX, float positionY, int color) {
        StringBuilder newstring = new StringBuilder();
        char[] oldstring = name.toCharArray();
        for (int i = 0; i <= oldstring.length - 1; i++) {
            if (i < size) {
                newstring.append(oldstring[i]);
            }
        }
        font.drawString(newstring.toString(), positionX, positionY, color);
    }

    public boolean hovertoFloatL(float xOne, float yOne, float xTwo, float yTwo, int mouseX, int mouseY, boolean click) {
        boolean hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo;
        return ((click && !mouseLDown && Mouse.isButtonDown(0) && hoverSystem) || (!click && hoverSystem)) && !openConfig && !openmidmanger;
    }

    public boolean hovertoFloatR(float xOne, float yOne, float xTwo, float yTwo, int mouseX, int mouseY, boolean click) {
        boolean hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo;
        return ((click && !mouseRDown && Mouse.isButtonDown(1) && hoverSystem) || (!click && hoverSystem)) && !openConfig && !openmidmanger;
    }

    public boolean hoverConfig(float xOne, float yOne, float xTwo, float yTwo, int mouseX, int mouseY, boolean click) {
        boolean hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo;
        return ((click && !mouseLDown && Mouse.isButtonDown(0) && hoverSystem) || (!click && hoverSystem));
    }
}
