/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt;
import liying.utils.info.Recorder;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ElementInfo(name="GameInfo")
public final class GameInfo
extends Element {
    private final ListValue GameInfo;
    private final SimpleDateFormat DATE_FORMAT;
    private FontValue fontValue;

    public final SimpleDateFormat getDATE_FORMAT() {
        return this.DATE_FORMAT;
    }

    public GameInfo() {
        this(0.0, 0.0, 0.0f, 7, null);
    }

    public GameInfo(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 10.0;
        }
        if ((n & 2) != 0) {
            d2 = 10.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        this(d, d2, f);
    }

    public GameInfo(double d, double d2, float f) {
        super(d, d2, f, null, 8, null);
        this.GameInfo = new ListValue("Mode", new String[]{"Normal"}, "Normal");
        this.fontValue = new FontValue("Font", Fonts.productSans35);
        this.DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public Border drawElement() {
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        IFontRenderer iFontRenderer = (IFontRenderer)this.fontValue.get();
        int n = iFontRenderer.getFontHeight() * 5 + (int)11.0;
        int n2 = (int)140.0;
        if (StringsKt.equals((String)((String)this.GameInfo.get()), (String)"normal", (boolean)true)) {
            RoundedUtil.drawGradientRound(-2.0f, -2.0f, n2, n, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.font40.drawCenteredString("Session Info", 31.5f, 3.0f, Color.WHITE.getRGB(), true);
            iFontRenderer.drawStringWithShadow("Play Time: " + this.DATE_FORMAT.format(new Date(System.currentTimeMillis() - Recorder.INSTANCE.getStartTime() - 28800000L)), 2, (int)((float)iFontRenderer.getFontHeight() + 8.0f), Color.WHITE.getRGB());
            iFontRenderer.drawStringWithShadow("Players Killed: " + Recorder.INSTANCE.getKillCounts(), 2, (int)((float)(iFontRenderer.getFontHeight() * 2) + 8.0f), Color.WHITE.getRGB());
            iFontRenderer.drawStringWithShadow("Win: " + Recorder.INSTANCE.getTotalPlayed(), 2, (int)((float)(iFontRenderer.getFontHeight() * 3) + 8.0f), Color.WHITE.getRGB());
            iFontRenderer.drawStringWithShadow("Total: " + Recorder.INSTANCE.getTotalPlayed(), 2, (int)((float)(iFontRenderer.getFontHeight() * 4) + 8.0f), Color.WHITE.getRGB());
        }
        return new Border(-2.0f, -2.0f, n2, n);
    }
}

