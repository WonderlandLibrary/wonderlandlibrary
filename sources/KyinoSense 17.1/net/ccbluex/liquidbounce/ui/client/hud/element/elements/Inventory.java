/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="InventoryHUD")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J \u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0002\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\tH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Inventory;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "(DDF)V", "inventoryRows", "", "lowerInv", "Lnet/minecraft/inventory/IInventory;", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "renderInventory1", "", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "renderInventory2", "renderInventory3", "renderItemStack", "stack", "Lnet/minecraft/item/ItemStack;", "KyinoClient"})
public final class Inventory
extends Element {
    private int inventoryRows;
    private final IInventory lowerInv;

    @Override
    @NotNull
    public Border drawElement() {
        if (this.lowerInv != null) {
            this.inventoryRows = this.lowerInv.func_70302_i_();
        }
        EntityPlayerSP entityPlayerSP = Inventory.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        this.renderInventory1((EntityPlayer)entityPlayerSP);
        EntityPlayerSP entityPlayerSP2 = Inventory.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        this.renderInventory2((EntityPlayer)entityPlayerSP2);
        EntityPlayerSP entityPlayerSP3 = Inventory.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
        this.renderInventory3((EntityPlayer)entityPlayerSP3);
        return new Border(0.0f, (float)this.inventoryRows * 18.0f + 17.0f, 176.0f, 96.0f);
    }

    /*
     * WARNING - void declaration
     */
    private final void renderInventory1(EntityPlayer player) {
        ItemStack armourStack = null;
        ItemStack[] renderStack = player.field_71071_by.field_70462_a;
        int xOffset = 8;
        renderStack = player.field_71071_by.field_70462_a;
        int n = 9;
        int n2 = 17;
        while (n <= n2) {
            void index;
            armourStack = renderStack[index];
            if (armourStack != null) {
                this.renderItemStack(armourStack, xOffset, 30);
            }
            xOffset += 18;
            ++index;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void renderInventory2(EntityPlayer player) {
        ItemStack armourStack = null;
        ItemStack[] renderStack = player.field_71071_by.field_70462_a;
        int xOffset = 8;
        renderStack = player.field_71071_by.field_70462_a;
        int n = 18;
        int n2 = 26;
        while (n <= n2) {
            void index;
            armourStack = renderStack[index];
            if (armourStack != null) {
                this.renderItemStack(armourStack, xOffset, 48);
            }
            xOffset += 18;
            ++index;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void renderInventory3(EntityPlayer player) {
        ItemStack armourStack = null;
        ItemStack[] renderStack = player.field_71071_by.field_70462_a;
        int xOffset = 8;
        renderStack = player.field_71071_by.field_70462_a;
        int n = 27;
        int n2 = 35;
        while (n <= n2) {
            void index;
            armourStack = renderStack[index];
            if (armourStack != null) {
                this.renderItemStack(armourStack, xOffset, 66);
            }
            xOffset += 18;
            ++index;
        }
    }

    private final void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        Minecraft minecraft = Inventory.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_175599_af().func_180450_b(stack, x, y);
        Minecraft minecraft2 = Inventory.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        minecraft2.func_175599_af().func_175030_a(Inventory.access$getMc$p$s1046033730().field_71466_p, stack, x, y);
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public Inventory(double x, double y, float scale) {
        super(x, y, scale, null, 8, null);
    }

    public /* synthetic */ Inventory(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public Inventory() {
        this(0.0, 0.0, 0.0f, 7, null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

