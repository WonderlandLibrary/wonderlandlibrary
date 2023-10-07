/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.network.Packet
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="PacketCounter")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B-\u0012\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\n\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0007R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0016j\b\u0012\u0004\u0012\u00020\u0014`\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0016j\b\u0012\u0004\u0012\u00020\u0014`\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/PacketCounter;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "Timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "packetCounterHeight", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "packetCounterMessage", "Lnet/ccbluex/liquidbounce/value/ListValue;", "packetCounterUpdateDelay", "packetCounterWidth", "receivedPackets", "", "receivedPacketsList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sentPackets", "sentPacketsList", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "handleEvents", "", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class PacketCounter
extends Element
implements Listenable {
    private int sentPackets;
    private int receivedPackets;
    private ArrayList<Integer> sentPacketsList;
    private ArrayList<Integer> receivedPacketsList;
    private MSTimer Timer;
    private final IntegerValue packetCounterHeight;
    private final IntegerValue packetCounterWidth;
    private final IntegerValue packetCounterUpdateDelay;
    private final ListValue packetCounterMessage;

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        String string = packet.getClass().getName();
        Intrinsics.checkExpressionValueIsNotNull(string, "packet.javaClass.name");
        if (StringsKt.contains((CharSequence)string, "net.minecraft.network.play.client.", true)) {
            ++this.sentPackets;
        }
        String string2 = packet.getClass().getName();
        Intrinsics.checkExpressionValueIsNotNull(string2, "packet.javaClass.name");
        if (StringsKt.contains((CharSequence)string2, "net.minecraft.network.play.server.", true)) {
            ++this.receivedPackets;
        }
    }

    @Override
    @Nullable
    public Border drawElement() {
        int n;
        float f;
        float y1;
        float y;
        int i;
        IntegerValue height = this.packetCounterHeight;
        int width = ((Number)this.packetCounterWidth.get()).intValue();
        int delay = ((Number)this.packetCounterUpdateDelay.get()).intValue();
        int tickdelay = delay / 50;
        String messageMode = (String)this.packetCounterMessage.get();
        if (this.Timer.hasTimePassed(delay)) {
            this.Timer.reset();
            this.sentPacketsList.add(this.sentPackets);
            this.receivedPacketsList.add(this.receivedPackets);
            this.sentPackets = 0;
            this.receivedPackets = 0;
            while (this.sentPacketsList.size() > width) {
                this.sentPacketsList.remove(0);
            }
            while (this.receivedPacketsList.size() > width) {
                this.receivedPacketsList.remove(0);
            }
        }
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        int sentSize = this.sentPacketsList.size();
        int receivedSize = this.receivedPacketsList.size();
        int sentStart = sentSize > width ? sentSize - width : 0;
        int receivedStart = receivedSize > width ? receivedSize - width : 0;
        int n2 = sentStart;
        int n3 = sentSize - 1;
        while (n2 < n3) {
            y = (float)(((Number)this.sentPacketsList.get(i)).intValue() * 10) * 0.25f / (float)tickdelay;
            y1 = (float)(((Number)this.sentPacketsList.get(i + 1)).intValue() * 10) * 0.25f / (float)tickdelay;
            RenderUtils.glColor(new Color(255, 0, 0, 255));
            GL11.glVertex2d((double)((double)i - (double)sentStart), (double)((double)(((Number)height.get()).intValue() + 1) - (double)RangesKt.coerceAtMost(y, (float)((Number)height.get()).intValue())));
            GL11.glVertex2d((double)((double)i + 1.0 - (double)sentStart), (double)((double)(((Number)height.get()).intValue() + 1) - (double)RangesKt.coerceAtMost(y1, (float)((Number)height.get()).intValue())));
            ++i;
        }
        n3 = receivedSize - 1;
        for (i = receivedStart; i < n3; ++i) {
            int n4;
            int n5;
            y = (float)(((Number)this.receivedPacketsList.get(i)).intValue() * 10) * 0.03f / (float)tickdelay;
            y1 = (float)(((Number)this.receivedPacketsList.get(i + 1)).intValue() * 10) * 0.03f / (float)tickdelay;
            RenderUtils.glColor(new Color(0, 255, 0, 255));
            double d = (double)i - (double)receivedStart;
            double d2 = (double)(((Number)height.get()).intValue() * 2 + 1) - (double)RangesKt.coerceAtMost(y, (float)((Number)height.get()).intValue());
            if (StringsKt.equals(messageMode, "Up", true)) {
                GameFontRenderer gameFontRenderer = Fonts.font35;
                if (gameFontRenderer == null) {
                    Intrinsics.throwNpe();
                }
                n5 = gameFontRenderer.field_78288_b;
            } else {
                n5 = 0;
            }
            GL11.glVertex2d((double)d, (double)(d2 + (double)n5));
            double d3 = (double)i + 1.0 - (double)receivedStart;
            double d4 = (double)(((Number)height.get()).intValue() * 2 + 1) - (double)RangesKt.coerceAtMost(y1, (float)((Number)height.get()).intValue());
            if (StringsKt.equals(messageMode, "Up", true)) {
                GameFontRenderer gameFontRenderer = Fonts.font35;
                if (gameFontRenderer == null) {
                    Intrinsics.throwNpe();
                }
                n4 = gameFontRenderer.field_78288_b;
            } else {
                n4 = 0;
            }
            GL11.glVertex2d((double)d3, (double)(d4 + (double)n4));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GlStateManager.func_179117_G();
        if (!StringsKt.equals(messageMode, "None", true)) {
            GL11.glPushMatrix();
            GL11.glScaled((double)0.6, (double)0.6, (double)0.6);
            if (StringsKt.equals(messageMode, "Right", true)) {
                float y12 = (float)(((Number)CollectionsKt.last((List)this.sentPacketsList)).intValue() * 10) * 0.25f / (float)tickdelay;
                float y122 = (float)(((Number)CollectionsKt.last((List)this.receivedPacketsList)).intValue() * 10) * 0.03f / (float)tickdelay;
                GameFontRenderer gameFontRenderer = Fonts.font35;
                if (gameFontRenderer == null) {
                    Intrinsics.throwNpe();
                }
                gameFontRenderer.drawString("Sent " + ((Number)CollectionsKt.last((List)this.sentPacketsList)).intValue() + " packets in the past " + delay + " MS.", ((float)CollectionsKt.getLastIndex((List)this.sentPacketsList) + 4.0f - (float)sentStart) / 0.6f, ((float)(((Number)height.get()).intValue() + 1) - RangesKt.coerceAtMost(y12, (float)((Number)height.get()).intValue())) / 0.6f, new Color(255, 0, 0, 255).getRGB());
                GameFontRenderer gameFontRenderer2 = Fonts.font35;
                if (gameFontRenderer2 == null) {
                    Intrinsics.throwNpe();
                }
                gameFontRenderer2.drawString("Received " + ((Number)CollectionsKt.last((List)this.receivedPacketsList)).intValue() + " packets in the past " + delay + " MS.", ((float)CollectionsKt.getLastIndex((List)this.receivedPacketsList) + 4.0f - (float)receivedStart) / 0.6f, ((float)(((Number)height.get()).intValue() * 2 + 1) - RangesKt.coerceAtMost(y122, (float)((Number)height.get()).intValue())) / 0.6f, new Color(0, 255, 0, 255).getRGB());
            } else if (StringsKt.equals(messageMode, "Up", true)) {
                GameFontRenderer gameFontRenderer = Fonts.font35;
                if (gameFontRenderer == null) {
                    Intrinsics.throwNpe();
                }
                String string = "Sent " + ((Number)CollectionsKt.last((List)this.sentPacketsList)).intValue() + " packets in the past " + delay + " MS.";
                GameFontRenderer gameFontRenderer3 = Fonts.font35;
                if (gameFontRenderer3 == null) {
                    Intrinsics.throwNpe();
                }
                gameFontRenderer.drawString(string, 0.0f, (float)(-gameFontRenderer3.field_78288_b / 2) / 0.6f, new Color(255, 0, 0, 255).getRGB());
                GameFontRenderer gameFontRenderer4 = Fonts.font35;
                if (gameFontRenderer4 == null) {
                    Intrinsics.throwNpe();
                }
                String string2 = "Received " + ((Number)CollectionsKt.last((List)this.receivedPacketsList)).intValue() + " packets in the past " + delay + " MS.";
                int n6 = ((Number)height.get()).intValue();
                GameFontRenderer gameFontRenderer5 = Fonts.font35;
                if (gameFontRenderer5 == null) {
                    Intrinsics.throwNpe();
                }
                gameFontRenderer4.drawString(string2, 0.0f, (float)(n6 + gameFontRenderer5.field_78288_b / 2) / 0.6f, new Color(0, 255, 0, 255).getRGB());
            }
            GL11.glPopMatrix();
        }
        if (!StringsKt.equals(messageMode, "Up", true)) {
            GameFontRenderer gameFontRenderer = Fonts.font35;
            if (gameFontRenderer == null) {
                Intrinsics.throwNpe();
            }
            f = gameFontRenderer.func_78256_a("Received " + ((Number)CollectionsKt.last((List)this.receivedPacketsList)).intValue() + " packets in the past " + delay + " MS.");
        } else {
            f = this.receivedPacketsList.size() - receivedStart;
        }
        float x2 = f;
        float f2 = (float)(((Number)height.get()).intValue() * 2) + 2.0f;
        if (!StringsKt.equals(messageMode, "Up", true)) {
            GameFontRenderer gameFontRenderer = Fonts.font35;
            if (gameFontRenderer == null) {
                Intrinsics.throwNpe();
            }
            n = gameFontRenderer.field_78288_b;
        } else {
            n = 0;
        }
        float y2 = f2 + (float)n;
        return new Border(0.0f, 0.0f, x2, y2);
    }

    public PacketCounter(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
        this.sentPacketsList = new ArrayList();
        this.receivedPacketsList = new ArrayList();
        this.Timer = new MSTimer();
        this.packetCounterHeight = new IntegerValue("PacketCounter Height", 50, 30, 150);
        this.packetCounterWidth = new IntegerValue("PacketCounter Width", 100, 100, 300);
        this.packetCounterUpdateDelay = new IntegerValue("PacketCounter Update Delay", 1000, 0, 2000);
        this.packetCounterMessage = new ListValue("PacketCounterMessage Mode", new String[]{"None", "Right", "Up"}, "Right");
    }

    public /* synthetic */ PacketCounter(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 100.0;
        }
        if ((n & 2) != 0) {
            d2 = 30.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.LEFT, Side.Vertical.UP);
        }
        this(d, d2, f, side);
    }

    public PacketCounter() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

