// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import net.minecraft.client.gui.ScaledResolution;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Welcome extends Element
{
    private final Value<Boolean> rainbow;
    private final Value<Boolean> dynamic;
    private final Value<Boolean> holiday;
    private final Value<Boolean> center;
    public static String text;
    
    public Welcome() {
        super("Welcome");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.dynamic = this.register(new Value<Boolean>("Dynamic", this, false));
        this.holiday = this.register(new Value<Boolean>("Holiday", this, true));
        this.center = this.register(new Value<Boolean>("Center", this, false));
    }
    
    @Override
    public void onRender() {
        if (Welcome.mc.player == null) {
            return;
        }
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        String display = Welcome.text.replaceAll("NAME", Welcome.mc.player.getName());
        if (this.dynamic.getValue()) {
            display = this.getTimeMessage().replaceAll("NAME", Welcome.mc.player.getName());
        }
        if (this.holiday.getValue() && this.getHoliday() != null) {
            display = this.getHoliday().replaceAll("NAME", Welcome.mc.player.getName());
        }
        this.width = Welcome.fontRenderer.getStringWidth(display) + 2;
        this.height = Welcome.fontRenderer.FONT_HEIGHT + 2;
        final String test = display.replaceAll("&", String.valueOf('§'));
        if (this.center.getValue()) {
            final ScaledResolution sr = new ScaledResolution(Welcome.mc);
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(test, sr.getScaledWidth() / 2.0f - Xulu.cFontRenderer.getStringWidth(test) / 2.0f + 1.0f, this.y + 1.0, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
            else {
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(test, sr.getScaledWidth() / 2.0f - Xulu.cFontRenderer.getStringWidth(test) / 2.0f + 1.0f, (float)this.y + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
        }
        else if (Xulu.CustomFont) {
            Xulu.cFontRenderer.drawStringWithShadow(test, this.x + 1.0, this.y + 1.0, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(test, (float)this.x + 1.0f, (float)this.y + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
    }
    
    public static void handleWelcome(final String stringIn) {
        Welcome.text = stringIn;
    }
    
    @Override
    public void onMiddleClick() {
        WelcomeNotes.initTextBox();
    }
    
    private String getTimeMessage() {
        final String date = new SimpleDateFormat("k").format(new Date());
        final int hour = Integer.valueOf(date);
        if (hour < 6) {
            return "Good Night NAME";
        }
        if (hour < 12) {
            return "Good Morning NAME";
        }
        if (hour < 17) {
            return "Good Afternoon NAME";
        }
        if (hour < 20) {
            return "Good Evening NAME";
        }
        return "Good Night NAME";
    }
    
    private String getHoliday() {
        final int month = Integer.valueOf(new SimpleDateFormat("MM").format(new Date()));
        final int day = Integer.valueOf(new SimpleDateFormat("dd").format(new Date()));
        switch (month) {
            case 1: {
                if (day == 1) {
                    return "Happy New Years NAME!";
                }
                break;
            }
            case 2: {
                if (day == 14) {
                    return "Happy Valentines Day NAME!";
                }
                break;
            }
            case 10: {
                if (day == 31) {
                    return "Happy Halloween NAME! (spooky)";
                }
                break;
            }
            case 11: {
                final LocalDate thanksGiving = Year.of(Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()))).atMonth(Month.NOVEMBER).atDay(1).with(TemporalAdjusters.lastInMonth(DayOfWeek.WEDNESDAY));
                if (thanksGiving.getDayOfMonth() == day) {
                    return "Happy Thanksgiving NAME!";
                }
            }
            case 12: {
                if (day == 25) {
                    return "Happy X-mas NAME!";
                }
                break;
            }
        }
        return null;
    }
    
    static {
        Welcome.text = "Welcome NAME";
    }
}
