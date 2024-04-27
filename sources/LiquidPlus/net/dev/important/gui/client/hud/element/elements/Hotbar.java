/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.HotbarUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="Hotbar")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Hotbar;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "(DD)V", "lastSlot", "", "slotlist", "", "Lnet/dev/important/utils/HotbarUtils;", "getSlotlist", "()Ljava/util/List;", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "LiquidBounce"})
public final class Hotbar
extends Element {
    @NotNull
    private final List<HotbarUtils> slotlist;
    private int lastSlot;

    public Hotbar(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        this.slotlist = new ArrayList();
        this.lastSlot = -1;
        int n = 0;
        while (n < 9) {
            int i = n++;
            HotbarUtils slot = new HotbarUtils();
            this.slotlist.add(slot);
        }
    }

    public /* synthetic */ Hotbar(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 40.0;
        }
        if ((n & 2) != 0) {
            d2 = 100.0;
        }
        this(d, d2);
    }

    @NotNull
    public final List<HotbarUtils> getSlotlist() {
        return this.slotlist;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Border drawElement() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        Iterable $this$forEachIndexed$iv = this.slotlist;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            void hotbarutil;
            int n = index$iv;
            index$iv = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            HotbarUtils hotbarUtils = (HotbarUtils)item$iv;
            int index = n;
            boolean bl = false;
            boolean hover = index == MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c && MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a[index] != null;
            float scale = hotbarutil.getTranslate().getX();
            float positionX = (float)(index * 25) / scale - (float)5;
            ItemStack currentitem = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a[MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c];
            hotbarutil.setSize(hover ? 1.5f : 1.0f);
            hotbarutil.getTranslate().translate(hotbarutil.getSize(), 0.0f, 2.0);
            if (hover) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179152_a((float)(scale - 0.5f), (float)(scale - 0.5f), (float)(scale - 0.5f));
                try {
                    List list = currentitem.func_82840_a((EntityPlayer)MinecraftInstance.mc.field_71439_g, MinecraftInstance.mc.field_71474_y.field_82882_x);
                    ArrayList infolist = new ArrayList();
                    int n2 = 0;
                    int n3 = list.size();
                    while (n2 < n3) {
                        int i;
                        if (infolist.contains(list.get(i = n2++)) || ((String)list.get(i)).length() <= 2) continue;
                        infolist.add(list.get(i));
                    }
                    float posy = 0.0f;
                    posy = -13.0f;
                    Iterable $this$forEachIndexed$iv2 = infolist;
                    boolean $i$f$forEachIndexed2 = false;
                    int index$iv2 = 0;
                    for (Object item$iv2 : $this$forEachIndexed$iv2) {
                        int n4 = index$iv2;
                        index$iv2 = n4 + 1;
                        if (n4 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        String string = (String)item$iv2;
                        int index2 = n4;
                        boolean bl2 = false;
                        GameFontRenderer font = Intrinsics.areEqual(ColorUtils.stripColor((String)infolist.get(index2)), currentitem.func_82833_r()) ? Fonts.Poppins : Fonts.fontSmall;
                        Object e = infolist.get(index2);
                        Intrinsics.checkNotNullExpressionValue(e, "infolist[index]");
                        font.func_175065_a((String)e, positionX * 1.5f, -(8.5f * (float)infolist.size()) + posy, Intrinsics.areEqual(ColorUtils.stripColor((String)infolist.get(index2)), currentitem.func_82833_r()) ? -1 : new Color(175, 175, 175).getRGB(), true);
                        posy += (float)font.field_78288_b + 2.0f;
                    }
                    infolist.clear();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a((float)scale, (float)scale, (float)scale);
            RenderHelper.func_74520_c();
            hotbarutil.renderHotbarItem(index, positionX, -10.0f, MinecraftInstance.mc.field_71428_T.field_74281_c);
            RenderHelper.func_74518_a();
            GlStateManager.func_179121_F();
        }
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        return new Border(0.0f, 0.0f, 180.0f, 17.0f);
    }

    public Hotbar() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }
}

