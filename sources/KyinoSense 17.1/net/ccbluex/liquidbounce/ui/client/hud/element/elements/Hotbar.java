/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.hotbarutil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="Hotbar")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Hotbar;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "(DD)V", "lastSlot", "", "slotlist", "", "Lnet/ccbluex/liquidbounce/utils/hotbarutil;", "getSlotlist", "()Ljava/util/List;", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class Hotbar
extends Element {
    @NotNull
    private final List<hotbarutil> slotlist;
    private int lastSlot;

    @NotNull
    public final List<hotbarutil> getSlotlist() {
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
            void hotbarutil2;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            hotbarutil hotbarutil3 = (hotbarutil)item$iv;
            int index = n2;
            boolean bl2 = false;
            boolean hover = index == Hotbar.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c && Hotbar.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[index] != null;
            float scale = hotbarutil2.getTranslate().getX();
            float positionX = (float)(index * 25) / scale - (float)5;
            ItemStack currentitem = Hotbar.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[Hotbar.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c];
            hotbarutil2.setSize(hover ? 1.5f : 1.0f);
            hotbarutil2.getTranslate().translate(hotbarutil2.getSize(), 0.0f, 2.0);
            if (hover) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179152_a((float)(scale - 0.5f), (float)(scale - 0.5f), (float)(scale - 0.5f));
                try {
                    float f = -13.0f;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                GlStateManager.func_179121_F();
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a((float)scale, (float)scale, (float)scale);
            RenderHelper.func_74520_c();
            hotbarutil2.renderHotbarItem(index, positionX, -10.0f, Hotbar.access$getMc$p$s1046033730().field_71428_T.field_74281_c);
            RenderHelper.func_74518_a();
            GlStateManager.func_179121_F();
        }
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        return new Border(0.0f, 0.0f, 180.0f, 17.0f);
    }

    /*
     * WARNING - void declaration
     */
    public Hotbar(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        List list;
        Hotbar hotbar = this;
        int n = 0;
        hotbar.slotlist = list = (List)new ArrayList();
        this.lastSlot = -1;
        n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            hotbarutil slot = new hotbarutil();
            this.slotlist.add(slot);
            ++i;
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

    public Hotbar() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

