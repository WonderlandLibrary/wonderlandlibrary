//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.gui.keybindmanager;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static info.sigmaclient.sigma.minimap.interfaces.InterfaceHandler.mc;
import static info.sigmaclient.sigma.modules.gui.TabGUI.smoothTrans;

public class KeyBindManager extends Screen {
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    JelloTextField textField;
    public KeyBindButton selectButton = null;
    public KeyBindButton nowSetGui = null;
    private long lastMS = 0L;
    private float progress = 0;
    private long lastMS2 = 0L;
    private float progress2 = 0;

    ArrayList<KeyBindButton> keyBindButtonArrayList = new ArrayList<KeyBindButton>();
    ArrayList<String> BlackKeys = new ArrayList<String>(Arrays.asList(
            "-","=","<-","Tab","[","]","\\","ctrl","WINDOWS","alt","SPACE","Alt Gr","RWINDOWS","LIST","RCTRL","RSHIFT"
            ,"/",",",".","Ctrl","shift","Enter",";","'","Caps Lock","`"
    ));
    String[] keys = new String[]{"`","1","2","3","4","5","6","7","8","9","0","-","=","<-","\n",
            "Tab","q","w","e","r","t","y","u","i","o","p","[","]","\\","\n",
            "Caps Lock","a","s","d","f","g","h","j","k","l",";","'","Enter","\n"
            ,"shift","z","x","c","v","b","n","m",",",".","/","RSHIFT","\n"
            ,"Ctrl","WINDOWS","alt","SPACE","Alt Gr","RWINDOWS","LIST","RCTRL"};
    public KeyBindManager() {
        super(new StringTextComponent("KeyBindManager"));
    }
    public boolean isCoinKey(int key){
        for(Module m : SigmaNG.getSigmaNG().moduleManager.modules)
            if (m.key == key)
                return true;
        return false;
    }
    public void reset(){
        this.keyBindButtonArrayList.clear();
        ScaledResolution sr = new ScaledResolution(mc);
        float mindX = sr.getScaledWidth() / 2F;
        float mindY = sr.getScaledHeight() / 2F;
        float x = mindX - 275 + 10;
        String prefix = "key.keyboard.";
        float y = mindY - 100 + 10;
        for (String key : keys) {
            if (key.equalsIgnoreCase("\n")) {
                x = mindX - 275 + 10;
                y += 35 + 1.5F + 1F;
                continue;
            }
            KeyBindButton temp;
            if (!BlackKeys.contains(key)) {
                temp = (new KeyBindButton(x, y, key, isCoinKey(
                        InputMappings.getInputByName("key.keyboard."+key.toLowerCase()).getKeyCode()
                )));
            } else {
                temp = (new KeyBindButton(x, y, key,
                        false, -1));
            }
            if (key.equalsIgnoreCase("tab")) {
                temp.height += 17F;
                x += 17F;
                temp.strOffset += 2F;
            }
            if (key.equalsIgnoreCase("Alt Gr")) {
                temp.height += 17F;
                x += 17F;
                temp.strOffset += 1F;
            }
            if (key.equalsIgnoreCase("Ctrl")) {
                temp.height += 17F;
                x += 17F;
                temp.strOffset += 4F;
            }
            if (key.equalsIgnoreCase("Alt")) {
                temp.height += 19F;
                x += 19F;
                temp.strOffset += 6F;
            }
            if (key.equalsIgnoreCase("SPACE")) {
                temp.height += 183F;
                x += 183F;
                temp.strOffset += 0;
            }
            if (key.equalsIgnoreCase("shift")) {
                temp.height += 45F;
                x += 45F;
                temp.strOffset += 17.5F;
            }
            if (key.equalsIgnoreCase("\\")) {
                temp.height += 8F;
                x += 8F;
                temp.strOffset += 4F;
            }
            if (key.equalsIgnoreCase("RCtrl")) {
                temp.height += 8F;
                x += 8F;
                temp.strOffset += 0F;
            }
            if (key.equalsIgnoreCase("<-")) {
                temp.height += 25F;
                x += 25F;
                temp.strOffset += 10F;
            }
            if (key.equalsIgnoreCase("Enter")) {
                temp.height += 35F;
                x += 35F;
                temp.strOffset += 8F;
            }
            if (key.equalsIgnoreCase("rshift")) {
                temp.height += 53F;
                x += 53F;
                temp.strOffset += 17F;
            }
            if (key.equalsIgnoreCase("Caps Lock")) {
                temp.height += 27F;
                x += 27F;
                temp.strOffset -= 6F;
            }
            x += 30 + 1.5F + 5;
            keyBindButtonArrayList.add(temp);
        }
    }
    public void initGui(){
        lastMS = 0L;
        progress = 0;
        lastMS2 = 0L;
        progress2 = 0;
        selectButton = null;
        nowSetGui = null;
        lastPercent = 0;
        percent = 0;
        percent2 = 0;
        lastPercent2 = 0;
        textField = null;
        reset();
    }
    TimerUtil timerUtil = new TimerUtil();
    float sqFloat = (float) (JelloFontUtil.jelloFont24.getStringWidth("Add") / 2F);
    float sqAnim = 0;
    AnimationUtils au = new AnimationUtils();

    @Override
    public void tick() {
            //todo LLL
            lastPercent = percent;
            lastPercent2 = percent2;
            if (percent > .98) {
                percent += ((.98 - percent)) - 0.001;
            }
            if (percent <= .98) {
                if (percent2 < 1) {
                    percent2 += ((1 - percent2) / (1.9f)) + 0.001;
                }
            }

            for (KeyBindButton button : this.keyBindButtonArrayList) {
                button.anim();
            }
        super.tick();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        progress = (float) (System.currentTimeMillis() - lastMS) / 100F;
        if (progress >= 1) progress = 1;
        progress2 = (float) (System.currentTimeMillis() - lastMS2) / 100F;
        if (progress2 >= 1) progress2 = 1;

        if (selectButton != null) {
            if (ClickUtils.isClickable(selectButton.x + selectButton.height / 2 + 30 - 5, selectButton.y + 185 - 5,
                    selectButton.x + selectButton.height / 2 + 30 - 5 + sqFloat * 2 + 1, selectButton.y + 185 - 5 +
                            JelloFontUtil
                                    .jelloFont24.getHeight() + 1, mouseX, mouseY)) {
                sqAnim = (float) au.animate(sqFloat, sqAnim, 0.2);
            } else {
                sqAnim = (float) au.animate(0, sqAnim, 0.2);
            }
        }

        if (mouseClick) {
            for (KeyBindButton button : this.keyBindButtonArrayList) {
                boolean isHover = button.isHover(mouseX, mouseY);
                if (isHover) {
                    button.press = selectButton != button;
                }
            }
        } else {
            for (KeyBindButton button : this.keyBindButtonArrayList) {
                button.press = false;
            }
        }
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtils.drawRect(
                0, 0
                , width, height, new Color(
                        0, 0, 0, 100
                ).getRGB());
        float percent2 = smoothTrans(this.percent2, lastPercent2);
        GlStateManager.pushMatrix();
        if (percent2 <= 1) {
            GlStateManager.translate(sr.getScaledWidth() / 2.0, sr.getScaledHeight() / 2.0, 0);
            GlStateManager.scale(0.8f + percent2 * 0.2F, 0.8f + percent2 * 0.2F, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2.0, -sr.getScaledHeight() / 2.0, 0);
        }
        float mindX = width / 2F;
        float mindY = height / 2F;
        JelloFontUtil.jelloFontBold40.drawNoBSString("Keybind Manager", mindX - 265,
                mindY - 75 - 20 - 5 - 25 - 10 + 3,
                new Color(255, 255, 255).getRGB());
        RenderUtils.drawRoundedRect(mindX - 275, mindY - 100,
                mindX + 275, mindY + 100 + 3F, 8,
                new Color(255, 255, 255).getRGB());
        for (KeyBindButton button : this.keyBindButtonArrayList) {
            button.draw();
        }

        if (sqAnim < 0.5F) {
            sqAnim = 0;
        }
        if (this.selectButton != null) {
            RenderUtils.drawShadow(selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 2,
                    selectButton.y + 50 - 5 - 10 + 2
                    , selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20 - 3,
                    selectButton.y + 50 + 180 - 10 - 10 - 10 - 2, 1);
            RenderUtils.drawTexture(selectButton.x + selectButton.height / 2 - 227 * 0.8F * 0.5F, selectButton.y + 5,
                    227 * 0.8F, 327 * 0.8F, "keybind", progress);
            RenderUtils.drawRoundedRect(selectButton.x + selectButton.height / 2 - 47 - 5 - 13,
                    selectButton.y + 50 - 5 - 10
                    , selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20,
                    selectButton.y + 50 + 180 - 10 - 10 - 10, 5, new Color(244, 244, 244, (int) (
                            255 * progress
                    )).getRGB());
            JelloFontUtil.jelloFont24.drawNoBSString(selectButton.name.substring(0, 1).toUpperCase() + (selectButton.name.length() > 1 ? selectButton.name.substring(1) : "") + " Key",
                    selectButton.x + selectButton.height / 2 - 47 - 5, selectButton.y + 50 - 3, new Color(50, 50, 50, (int) (
                            255 * progress
                    )).getRGB());
            RenderUtils.drawRect(selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 10, selectButton.y + 50 - 3 + 20,
                    selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20 - 10, selectButton.y + 50 - 3 + 20 + 1,
                    new Color(233, 233, 233, (int) (
                            255 * progress
                    )).getRGB());
            float y2 = selectButton.y + 50 - 3 + 20 + 10;
            for (Module m : SigmaNG.getSigmaNG().moduleManager.modules) {
                if (m.key == selectButton.key) {
                    JelloFontUtil.jelloFont18.drawNoBSString(m.remapName, selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 5
                            , y2, new Color(100, 100, 100).getRGB());
                    JelloFontUtil.jelloFont14.drawNoBSString(m.category.name(), selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 5
                            , y2 + 10, new Color(200, 200, 200).getRGB());
                    JelloFontUtil.jelloFont16.drawNoBSString("x", selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 5 + 90
                            , y2 + 10, new Color(222, 90, 90).getRGB());
                    y2 += 20;
                }
            }
            JelloFontUtil.jelloFont24.drawNoBSString("Add",
                    selectButton.x + selectButton.height / 2 + 30 - 5, selectButton.y + 185 - 5, new Color(78, 162, 252, (int) (
                            255 * progress
                    )).getRGB());

            RenderUtils.drawRect(selectButton.x + selectButton.height / 2 + 30 - 5 + sqFloat - sqAnim, selectButton.y + 185 - 5 + JelloFontUtil
                            .jelloFont24.getHeight() + 1,
                    selectButton.x + selectButton.height / 2 + 30 - 5 + sqFloat + sqAnim, selectButton.y + 185 - 5 + JelloFontUtil
                            .jelloFont24.getHeight() + 1 + 1, new Color(78, 162, 252, (int) (
                            255 * progress
                    )).getRGB());
//            RenderUtils.drawRoundedRect(selectButton.x + selectButton.height / 2 - 8 - 55, selectButton.y + 36,
//                    selectButton.x + selectButton.height / 2 + 8 + 55, selectButton.y + 27 + 170,6,new Color(244,244,244,(int)(255 * progress)).getRGB());
            if (nowSetGui != null) {
                RenderUtils.drawRect(0, 0, width, height, new Color(
                        0, 0, 0, (int) (50 * progress2)
                ).getRGB());
                RenderUtils.drawRoundedRect(sr.getScaledWidth() / 2F - 120, sr.getScaledHeight() / 2F - 140,
                        sr.getScaledWidth() / 2F + 120, sr.getScaledHeight() / 2F + 140, 5,
                        new Color(254, 254, 254, (int) (255 * progress2)).getRGB());
                JelloFontUtil.jelloFont32.drawNoBSString("Select mod to bind", sr.getScaledWidth() / 2F - 120 + 15, sr.getScaledHeight() / 2F - 140 + 20,
                        new Color(100, 100, 100).getRGB());
                float sy = sr.getScaledHeight() / 2F - 140 + 20 + 45 + 1;
                float y = sr.getScaledHeight() / 2F - 140 + 20 + 45 + 10;
                for (Module in : SigmaNG.getSigmaNG().moduleManager.modules) {
                    if (textField.getText().isEmpty()) {
                        if (y < sy + 5 || y > sr.getScaledHeight() / 2F + 140 - 10) {
                            y += 20;
                            continue;
                        }
                        JelloFontUtil.jelloFont24.drawNoBSString(in.remapName, sr.getScaledWidth() / 2F - 120 + 15, y, new Color(20, 20, 20).getRGB());
                        y += 20;
                    } else {
                        if (!in.remapName.toLowerCase().contains(textField.getText().toLowerCase())) continue;
                        if (y < sy + 5 || y > sr.getScaledHeight() / 2F + 140 - 10) {
                            y += 20;
                            continue;
                        }
                        JelloFontUtil.jelloFont24.drawNoBSString(in.remapName, sr.getScaledWidth() / 2F - 120 + 15, y, new Color(20, 20, 20).getRGB());
                        y += 20;
                    }
                }
            }
        }
        if(textField != null){
            textField.drawTextBox();
        }
        GlStateManager.popMatrix();
    }
    boolean mouseClick = false;
    boolean blockExit = false;

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if(textField != null){
            textField.mouseClicked(x, y, button);
        }
        super.mouseClicked(x, y, button);

        ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
        if(button == 0){
            if(this.selectButton != null){
                float y21 = selectButton.y + 50 - 3 + 20 + 10;
                if(ClickUtils.isClickable(selectButton.x + selectButton.height / 2 - 47 - 5 - 13,
                        selectButton.y + 50 - 5 - 10
                        ,selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20,
                        selectButton.y + 50 + 180 - 10 - 10 - 10,x,y)){
                    blockExit = true;
                    for(Module m : SigmaNG.getSigmaNG().moduleManager.modules){
                        if (m.key == selectButton.key){
                            if(ClickUtils.isClickable(selectButton.x + selectButton.height / 2 - 47 - 5 - 13
                                    ,y21,selectButton.x + selectButton.height / 2 - 47 - 5 - 13 + 5 + 95 + 15
                                    ,y21 + 5 + 15,x, y)){
                                m.key = -1;
                                blockExit = true;
                                reset();
                                return false;
                            }
                            y21 += 20;
                        }
                    }
                }
                if(nowSetGui != null){
                    if (!ClickUtils.isClickable(sr.getScaledWidth() / 2F - 120, sr.getScaledHeight() / 2F - 140,
                            sr.getScaledWidth() / 2F + 120, sr.getScaledHeight() / 2F + 140, x, y)) {
                        nowSetGui = null;
                        blockExit = true;
                        textField.setVisible(false);
                        this.buttons.remove(textField);
                        return false;
                    }else{
                        float sy = sr.getScaledHeight() / 2F - 140 + 20 + 45 + 1;
                        float y2 = sr.getScaledHeight() / 2F - 140 + 20 + 45 + 10;
                        for(Module in : SigmaNG.getSigmaNG().moduleManager.modules){
                            if(textField.getText().isEmpty()){
                                if(y2 < sy + 5 || y2 > sr.getScaledHeight() / 2F + 140 - 10) {
                                    y2 += 20;
                                    continue;
                                }
                                if(ClickUtils.isClickable(sr.getScaledWidth() / 2F - 120 + 15,y2,sr.getScaledWidth() / 2F - 120 + 15 + 100,y2+20,
                                        x,y)){
                                    in.key = selectButton.key;
                                    nowSetGui = null;
                                    blockExit = true;
                                    textField.setVisible(false);
                                    this.buttons.remove(textField);
                                    this.reset();
                                    return false;
                                }
                                y2 += 20;
                            }else {
                                if (!in.remapName.toLowerCase().contains(textField.getText().toLowerCase())) continue;
                                if(y2 < sy + 5 || y2 > sr.getScaledHeight() / 2F + 140 - 10) {
                                    y2 += 20;
                                    continue;
                                }
                                if(ClickUtils.isClickable(sr.getScaledWidth() / 2F - 120 + 15,y2,sr.getScaledWidth() / 2F - 120 + 15 + 100,y2+20,
                                        x,y)){
                                    in.key = InputMappings.getInputByName("key.keyboard."+nowSetGui.name.toLowerCase()).getKeyCode();
                                    nowSetGui = null;
                                    blockExit = true;
                                    textField.setVisible(false);
                                    this.buttons.remove(textField);
                                    this.reset();
                                    return false;
                                }
                                y2 += 20;
                            }
                        }
                        //todo text
                        return false;
                    }
                }
                if(ClickUtils.isClickable(selectButton.x + selectButton.height / 2 - 47 - 5 - 13,
                        selectButton.y + 50 - 5 - 10
                        ,selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20,
                        selectButton.y + 50 + 180 - 10 - 10 - 10,x,y)){
                    //Add button
                    if(ClickUtils.isClickable(selectButton.x + selectButton.height / 2 + 30 - 5, selectButton.y + 185 - 5,
                            selectButton.x + selectButton.height / 2 + 30 - 5 + sqFloat * 2 + 1, selectButton.y + 185 - 5 + JelloFontUtil
                                    .jelloFont24.getHeight() + 1,x,y)){
                        if(selectButton != null){
                            this.nowSetGui = selectButton;
                            textField = new JelloTextField(114, mc.fontRenderer,
                                    (int) (sr.getScaledWidth() / 2F - 120 + 15), (int) (sr.getScaledHeight() / 2F - 140 + 30 + 10),
                                    200, 30,
                                    "Search...");
                            textField.setTextColor(new Color(100,100,100).getRGB());
                            blockExit = true;
                            lastMS2 = System.currentTimeMillis();
                        }
                    }
                    return false;
                }
            }
            mouseClick = true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(textField != null){
            textField.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if(blockExit) {
            blockExit = false;
            return false;
        }
        if(nowSetGui != null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
            if (ClickUtils.isClickable(sr.getScaledWidth() / 2F - 120, sr.getScaledHeight() / 2F - 140,
                    sr.getScaledWidth() / 2F + 120, sr.getScaledHeight() / 2F + 140, mouseX, mouseY)) {
//                textField.onRelease(mouseX,mouseY,button);
                return false;
                //todo text
            }
            if(selectButton != null && nowSetGui != null && ClickUtils.isClickable(selectButton.x + selectButton.height / 2 - 47 - 5 - 13,
                    selectButton.y + 50 - 5 - 10
                    ,selectButton.x + selectButton.height / 2 - 47 + 120 - 10 - 20 - 5 + 20,
                    selectButton.y + 50 + 180 - 10 - 10 - 10,mouseX,mouseY)){
                return false;
            }
            return false;
        }

        this.selectButton = null;
        for (KeyBindButton buttons : this.keyBindButtonArrayList){
            if (selectButton == buttons && buttons.press){
                this.selectButton = null;
                break;
            }
            if (buttons.press){
                this.selectButton = buttons;
                lastMS = System.currentTimeMillis();
            }
        }
        mouseClick = false;
        super.mouseReleased(mouseX, mouseY, state);
        return false;
    }

}
