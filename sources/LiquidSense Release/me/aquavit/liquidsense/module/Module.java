package me.aquavit.liquidsense.module;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.event.Listenable;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Print;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Type;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Module extends MinecraftInstance implements Listenable {
    protected String name;
    protected String description;
    protected ModuleCategory category;
    protected String arrayListName;
    protected boolean state;
    private int keyBind;
    private final boolean canEnable;
    private final float hue;
    private boolean array;
    private float slide;
    private float higt;
    private float slideStep;
    private int outvalue;
    private Translate openValue;
    private Translate clickAnimation;
    private boolean showSettings;
    private String finalname;
    private float guiposY;
    private float click;
    private float openValueposy;
    private float suckDown;
    private Translate translate;
    private Translate keytranslate;
    private double defaultx;
    private double defaulty;
    private double prevMouseX;
    private double prevMouseY;
    private double renderx;
    private double rendery;
    private float borderX;
    private float borderY;
    private float borderWidth;
    private float borderHeight;
    private boolean drag;
    private boolean useConfig;

    public boolean isLeft;
    public boolean isUp;

    public Module() {
        this.finalname = this.getClass().getAnnotation(ModuleInfo.class).name();
        this.name = this.getClass().getAnnotation(ModuleInfo.class).name();
        this.arrayListName = name;
        this.description = this.getClass().getAnnotation(ModuleInfo.class).description();
        this.category = this.getClass().getAnnotation(ModuleInfo.class).category();
        this.keyBind = this.getClass().getAnnotation(ModuleInfo.class).keyBind();
        this.canEnable = this.getClass().getAnnotation(ModuleInfo.class).canEnable();
        this.hue = (float) Math.random();
        this.array = this.getClass().getAnnotation(ModuleInfo.class).array();
        this.outvalue = 0;
        this.openValue = new Translate(0f , 0f);
        this.clickAnimation = new Translate(0f , 0f);
        this.showSettings = false;
        this.guiposY = 0f;
        this.click = 0f;
        this.openValueposy = 0f;
        this.suckDown = 0f;
        this.translate = new Translate(0f, 0f);
        this.keytranslate = new Translate(0f, 0f);
        this.useConfig = false;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;

        if (!LiquidSense.isStarting)
            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.modulesConfig);
    }


    public void setState(final boolean state) {
        try {
            if (this.getState() == state) {
                return;
            }
            this.onToggle(state);

            if (!LiquidSense.isStarting) {
                LiquidSense.hud.addPrint(new Print(" " + name + (state ? " Enabled" : " Disabled"),3000f, Type.state));
                LiquidSense.hud.addNotification(new Notification(" " + name + (state ? " Enabled" : " Disabled"), description, ColorType.INFO,1500,500));
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.click"),
                        1F));
            }

            if (state) {
                this.onEnable();
                if (this.canEnable) {
                    this.state = true;
                }
            }
            else {
                this.onDisable();
                this.state = false;
            }
            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.modulesConfig);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTag() {
        return null;
    }

    public final String getTagName() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7" + this.getTag());
    }

    public final String getTagName2() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7[" + this.getTag()+"]");
    }

    public final String getTagName3() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7(" + this.getTag()+")");
    }

    public final String getTagName4() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7<" + this.getTag()+">");
    }

    public final String getTagName5() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7- " + this.getTag());
    }

    public final String getTagName6() {
        return this.arrayListName + (this.getTag() == null ? "" : " §7§l" + this.getTag());
    }

    public final String getTagName7() {
        return this.arrayListName + (this.getTag() == null ? "" : " §f" + this.getTag());
    }

    public final String getColorlessTagName() {
        return this.arrayListName + (this.getTag() == null ? "" : " " + ColorUtils.stripColor(this.getTag()));
    }

    public void toggle() {
        this.setState(!this.getState());
    }

    public void toggleShowSettings() {
        this.showSettings = !showSettings;
    }

    public final void setNameCommand(String namecommand) {
        this.arrayListName = namecommand;
    }

    public void onToggle(final boolean state) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Value<?> getValue(final String valueName) {
        for (final Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if (o instanceof Value) {
                    final Value value = (Value)o;
                    if (value.name.equalsIgnoreCase(valueName)) {
                        return value;
                    }
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Value<?>> getValues() {
        final List<Value<?>> values = new ArrayList<Value<?>>();
        for (final Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object o = field.get(this);
                if (o instanceof Value && ((Value<?>) o).isDisplayable()) {
                    values.add((Value<?>)o);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    @Override
    public boolean handleEvents() {
        return this.getState();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ModuleCategory getCategory() {
        return this.category;
    }

    public boolean getState() {
        return this.state;
    }

    public int getKeyBind() {
        return this.keyBind;
    }

    public final String getArrayListName() {
        return this.arrayListName;
    }

    public final void setArrayListName(String string) {
        this.arrayListName = string;
    }

    public final float getSlideStep() {
        return this.slideStep;
    }

    public final void setSlideStep(float slide) { this.slideStep = slide; }

    public final double getPrevMouseX() {
        return prevMouseX;
    }

    public final void setPrevMouseX(double x){
        this.prevMouseX = x;
    }

    public double getDefaultx() {
        return defaultx;
    }

    public double getDefaulty() {
        return defaulty;
    }

    public void setDefaultx(double defaultx) {
        this.defaultx = defaultx;
    }

    public void setDefaulty(double defaulty) {
        this.defaulty = defaulty;
    }

    public double getHUDX(boolean left, boolean middle, boolean right) {
        return left ? this.renderx :
                (middle ? (float) new ScaledResolution(mc).getScaledWidth() / 2 - this.renderx :
                        (right ? (float) new ScaledResolution(mc).getScaledWidth() - this.renderx : this.renderx));
    }

    public double getHUDY(boolean up, boolean middle, boolean down) {
        return up ? this.rendery :
                (middle ? (float) (new ScaledResolution(mc).getScaledHeight() / 2) - this.rendery :
                        (down ? new ScaledResolution(mc).getScaledHeight() - this.rendery : this.rendery));
    }

    public void setHUDX(double renderx) {
        this.renderx = renderx;
    }

    public void setHUDY(double rendery) {
        this.rendery = rendery;
    }

    public double getRenderx() {
        return renderx;
    }

    public double getRendery() {
        return rendery;
    }

    public void setRenderx(double renderx) {
        this.renderx = renderx;
    }

    public void setRendery(double rendery) {
        this.rendery = rendery;
    }

    public double getBorderHeight() {
        return borderHeight;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public final double getPrevMouseY() {
        return prevMouseY;
    }

    public final void setPrevMouseY(double y){
        this.prevMouseY = y;
    }

    public final boolean getDrag() {
        return this.drag;
    }

    public final void setDrag(boolean state) {
        this.drag = state;
    }

    public boolean getUseConfig() {
        return useConfig;
    }

    public void setUseConfig(boolean useConfig) {
        this.useConfig = useConfig;
    }

    public final float getHue() {
        return this.hue;
    }

    public final float getSlide() { return this.slide; }

    public final void setSlide(float slide) { this.slide = slide; }

    public final boolean getShowSettings() { return this.showSettings; }

    public final void setShowSettings(boolean state) { this.showSettings = state; }

    public final int getOutvalue() { return this.outvalue; }

    public final void setOutvalue(int value) { this.outvalue = value; }

    public final Translate getOpenValue() { return this.openValue; }

    public final void setOpenValue(Translate value) { this.openValue = value; }

    public final Translate getTranslate() {
        return this.translate;
    }

    public final Translate getKeytranslate() {return this.keytranslate;}

    public final void setTranslate(Translate translate) {
       this.translate = translate;
    }

    public final Translate getClickAnimation() { return this.clickAnimation; }

    public final void setClickAnimation(Translate value) { this.clickAnimation = value; }

    public final boolean getArray() {
        return this.array;
    }

    public final void setArray(boolean state) { this.array = state; }

    public final float getHigt() {
        return this.higt;
    }

    public final void setHigt(float higt) {
        this.higt = higt;
    }

    public final String getFinalname() {
        return this.finalname;
    }

    public final void setFinalname(String name) {
        this.finalname = name;
    }

    public final float getGuiposY() {
        return this.guiposY;
    }

    public final void setGuiposY(float y) {
        this.guiposY = y;
    }

    public final float getClick() {
        return this.click;
    }

    public final void setClick(float speed) {
        this.click = speed;
    }

    public final float getOpenValueposy() {
        return this.openValueposy;
    }

    public final void setOpenValueposy(float y) {
        this.openValueposy = y;
    }

    public final float getSuckDown() {
        return this.suckDown;
    }

    public final void setSuckDown(float time) {
        this.suckDown = time;
    }

    public float getBorderX() {
        return borderX;
    }

    public double getBorderY() {
        return borderY;
    }

    public void drawBorder(float x, float y, float width, float height) {
        this.borderX = x;
        this.borderY = y;
        this.borderWidth = x+width;
        this.borderHeight = y+height;
        if (getDrag()) RenderUtils.drawRectBordered(borderX, borderY, borderWidth, borderHeight, 1.0F, new Color(15 , 15 , 15 , 40).getRGB(), Integer.MIN_VALUE);
    }

    public boolean isInBorder(double x, double y) {

        float minX = Math.min(this.borderX, this.borderWidth);
        float minY = Math.min(this.borderY, this.borderHeight);

        float maxX = Math.max(this.borderX, this.borderWidth);
        float maxY = Math.max(this.borderY, this.borderHeight);

        return minX <= x && minY <= y && maxX >= x && maxY >= y;
    }
}
