/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="PlayerInfo")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\b\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/PlayerInfo;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "H", "", "getH", "()I", "setH", "(I)V", "HM", "getHM", "setHM", "M", "getM", "setM", "S", "getS", "setS", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class PlayerInfo
extends Element {
    private int HM;
    private int S;
    private int M;
    private int H;

    public final int getHM() {
        return this.HM;
    }

    public final void setHM(int n) {
        this.HM = n;
    }

    public final int getS() {
        return this.S;
    }

    public final void setS(int n) {
        this.S = n;
    }

    public final int getM() {
        return this.M;
    }

    public final void setM(int n) {
        this.M = n;
    }

    public final int getH() {
        return this.H;
    }

    public final void setH(int n) {
        this.H = n;
    }

    @Override
    @NotNull
    public Border drawElement() {
        long durationInMillis = System.currentTimeMillis() - LiquidBounce.INSTANCE.getPlayTimeStart();
        long second = durationInMillis / (long)1000 % (long)60;
        long minute = durationInMillis / (long)60000 % (long)60;
        long hour = durationInMillis / (long)3600000 % (long)24;
        String time = null;
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = "%02dh %02dm %02ds";
        Object[] objectArray = new Object[]{hour, minute, second};
        boolean bl = false;
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull(string2, "java.lang.String.format(format, *args)");
        time = string2;
        ++this.HM;
        if (this.HM == 120) {
            ++this.S;
            this.HM = 0;
        }
        if (this.S == 60) {
            ++this.M;
            this.S = 0;
        }
        if (this.M == 60) {
            ++this.H;
            this.M = 0;
        }
        RenderUtils.drawBorderedRect(0.0f, -10.0f, 120.0f, 45.0f, 4.0f, new Color(255, 200, 255, 255).getRGB(), new Color(255, 190, 255, 80).getRGB());
        RenderUtils.drawBorderedRect(0.0f, -10.0f, 120.0f, 45.0f, 1.5f, new Color(30, 30, 30, 180).getRGB(), new Color(255, 180, 255, 0).getRGB());
        RenderUtils.drawRect(0.0f, 2.2f, 120.0f, 3.0f, new Color(255, 190, 255, 230).getRGB());
        RenderUtils.drawRect(0.0f, 2.5f, 120.0f, 2.0f, new Color(30, 30, 30, 180).getRGB());
        EntityPlayerSP entityPlayerSP = PlayerInfo.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = entityPlayerSP.field_70165_t;
        EntityPlayerSP entityPlayerSP2 = PlayerInfo.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = Math.pow(d - entityPlayerSP2.field_70169_q, 2.0);
        EntityPlayerSP entityPlayerSP3 = PlayerInfo.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = entityPlayerSP3.field_70161_v;
        EntityPlayerSP entityPlayerSP4 = PlayerInfo.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double BpsFloat = Math.sqrt(d2 + Math.pow(d3 - entityPlayerSP4.field_70166_s, 2.0)) * (double)20 * (double)PlayerInfo.access$getMc$p$s1046033730().field_71428_T.field_74278_d;
        double BpsDouble = (BpsFloat * (double)100 - BpsFloat * (double)100 % 1.0) / (double)100;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2884);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)9);
        GlStateManager.func_179131_c((float)0.0f, (float)0.0f, (float)0.0f, (float)0.23529412f);
        GL11.glVertex2d((double)0.0f, (double)-10.0f);
        GL11.glVertex2d((double)120.0f, (double)-10.0f);
        GlStateManager.func_179131_c((float)0.0f, (float)0.0f, (float)0.0f, (float)0.09803922f);
        GL11.glVertex2d((double)120.0f, (double)44.0f);
        GL11.glVertex2d((double)0.0f, (double)44.0f);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        Color color = Color.WHITE;
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
        Fonts.font35.drawString("My Info", 40.0f, -5.0f, color.getRGB());
        Fonts.font30.func_175065_a("  PlayTime\u00a77:\u00a7f" + time, 1.0f, 6.0f, new Color(255, 255, 255, 200).getRGB(), true);
        Fonts.font30.func_175065_a("  You killed\u00a77:\u00a7f" + KillAura.CombatListener.INSTANCE.getKillCounts(), 1.0f, 16.0f, new Color(255, 255, 255, 200).getRGB(), true);
        Fonts.font30.func_175065_a("  " + BpsDouble + " Blocks\u00a77/\u00a7fS", 1.0f, 27.0f, new Color(255, 255, 255, 200).getRGB(), true);
        Fonts.font30.func_175065_a("  FPS\u00a77:\u00a7f" + String.valueOf(Minecraft.func_175610_ah()) + 2, 1.0f, 37.0f, new Color(255, 255, 255, 200).getRGB(), true);
        return new Border(0.0f, 0.0f, 100.0f, 40.0f);
    }

    public PlayerInfo() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

