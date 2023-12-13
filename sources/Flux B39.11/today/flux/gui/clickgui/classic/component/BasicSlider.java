package today.flux.gui.clickgui.classic.component;

import org.lwjgl.input.Mouse;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.FloatValue;
import today.flux.utility.AnimationUtils;
import today.flux.utility.ColorUtils;
import today.flux.utility.MathUtils;

import java.awt.*;
import java.math.BigDecimal;

public class BasicSlider extends Component {
    public float min;
    public float max;
    public float value;
    public float increment;
    protected boolean isDragging;
    public float animation = 0;
    public FloatValue INSTANCE;
    public String unit;

    public BasicSlider(Window parent, FloatValue val, int offX, int offY, String title, float min, float max, float increment) {
        super(parent, offX, offY, title);
        this.INSTANCE = val;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.width = ClickGUI.settingsWidth;
        this.height = 19;
        this.type = "BasicSlider";
    }

    public void render(int mouseX, int mouseY) {
        if (INSTANCE.anotherShit) {
            final float slideRange = this.width - 10;
            final float multiplier = (value - min) / (max - min);

            if(this.animation == 0) {
                this.animation = multiplier;
            }

            this.animation = AnimationUtils.smoothAnimation(this.animation, (multiplier * 100), 50, 0.3f);

            //label
            FontManager.tiny.drawString(this.title, this.x + 9, this.y + 2, Hud.isLightMode ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());

            //line
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 14, this.x + this.width, this.y + 14, 2.0f, Hud.isLightMode ? new Color(0xFFE5E4E5).darker().getRGB() : new Color(0xBFBFBF).getRGB());
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 14, this.x + 10 + (slideRange * (animation / 100)), this.y + 14, 2.0f, Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor);

            //value display
            String valueText;
            if (this.value % 1.0f != 0.0f) {
                valueText = new BigDecimal(this.value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
            } else {
                valueText = new BigDecimal(this.value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
            }

            if (this.unit != null) {
                valueText += this.unit;
            }

            FontManager.tiny.drawString(valueText, this.x + this.width - FontManager.tiny.getStringWidth(valueText) + 1, this.y + 2, Hud.isLightMode ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());

            //status
            GuiRenderUtils.drawRoundedRect(this.x + 8 + (slideRange * (animation / 100)), this.y + 11f, 6f, 6f, 10, Hud.isLightMode ? 0xFF1DA0FF : 0xff9B59B6, 1.0f, Hud.isLightMode ? 0xFF1DA0FF : 0xff9B59B6);
        } else {
            final float slideRange = this.width - 22;
            final float multiplier = (value - min) / (max - min);

            if(this.animation == 0) {
                this.animation = multiplier;
            }

            this.animation = AnimationUtils.smoothAnimation(this.animation, (multiplier * 100), 50, 0.3f);

            //label
            FontManager.tiny.drawString(this.title, this.x + 9, this.y + 3, Hud.isLightMode ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());

            //line
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 16, this.x + this.width - 10, this.y + 16, 2.0f, Hud.isLightMode ? new Color(0xFFE5E4E5).darker().getRGB() : new Color(0xBFBFBF).getRGB());
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 16, this.x + 10 + (slideRange * (animation / 100)), this.y + 16, 2.0f, Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor);

            //value display
            String valueText;
            if (this.value % 1.0f != 0.0f) {
                valueText = new BigDecimal(this.value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
            } else {
                valueText = new BigDecimal(this.value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
            }

            if (this.unit != null) {
                valueText += this.unit;
            }

            FontManager.tiny.drawString(valueText, this.x + this.width - FontManager.tiny.getStringWidth(valueText) - 9, this.y + 3, Hud.isLightMode ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());

            //status
            GuiRenderUtils.drawRoundedRect(this.x + 8 + (slideRange * (animation / 100)), this.y + 13f, 6.0f, 6.0f, 10, Hud.isLightMode ? 0xFF1DA0FF : 0xff9B59B6, 1f, Hud.isLightMode ? 0xFF1DA0FF : 0xff9B59B6);
        }
    }

    public static BigDecimal round(float f, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(decimalPlace, 4);
        return bd;
    }

    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        if (this.isDragging) {
            this.value = MathUtils.map((float) (mouseX - (this.x + 10)), 0.0F, (float) (this.width - (INSTANCE.anotherShit ? 10 : 22)), this.min, this.max);
            this.value -= this.value % this.increment;
            if (this.value > this.max) {
                this.value = this.max;
            }

            if (this.value < this.min) {
                this.value = this.min;
            }
        }
    }

    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        if (isPressed && !this.wasMousePressed && this.isHovered) {
            this.isDragging = true;
        }

        if (!isPressed) {
            this.isDragging = false;
        }

        this.wasMousePressed = isPressed;
    }

    public void noMouseUpdates() {
        super.noMouseUpdates();
        if (!Mouse.isButtonDown(0)) {
            this.isDragging = false;
        }
    }
}
