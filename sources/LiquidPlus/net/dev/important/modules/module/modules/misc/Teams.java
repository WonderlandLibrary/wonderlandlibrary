/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Info(name="Teams", description="Prevents Killaura from attacking team mates.", category=Category.MISC, cnName="\u961f\u4f0d\u540d\u5355")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lnet/dev/important/modules/module/modules/misc/Teams;", "Lnet/dev/important/modules/module/Module;", "()V", "armorColorValue", "Lnet/dev/important/value/BoolValue;", "armorIndexValue", "Lnet/dev/important/value/IntegerValue;", "colorValue", "gommeSWValue", "scoreboardValue", "isInYourTeam", "", "entity", "Lnet/minecraft/entity/EntityLivingBase;", "LiquidBounce"})
public final class Teams
extends Module {
    @NotNull
    private final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    @NotNull
    private final BoolValue colorValue = new BoolValue("Color", true);
    @NotNull
    private final BoolValue gommeSWValue = new BoolValue("GommeSW", false);
    @NotNull
    private final BoolValue armorColorValue = new BoolValue("ArmorColor", false);
    @NotNull
    private final IntegerValue armorIndexValue = new IntegerValue("ArmorIndex", 3, 0, 3, new Function0<Boolean>(this){
        final /* synthetic */ Teams this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)Teams.access$getArmorColorValue$p(this.this$0).get();
        }
    });

    public final boolean isInYourTeam(@NotNull EntityLivingBase entity) {
        String clientName;
        String string;
        String targetName;
        String myHead;
        Intrinsics.checkNotNullParameter(entity, "entity");
        if (MinecraftInstance.mc.field_71439_g == null) {
            return false;
        }
        if (((Boolean)this.scoreboardValue.get()).booleanValue() && MinecraftInstance.mc.field_71439_g.func_96124_cp() != null && entity.func_96124_cp() != null && MinecraftInstance.mc.field_71439_g.func_96124_cp().func_142054_a(entity.func_96124_cp())) {
            return true;
        }
        if (((Boolean)this.armorColorValue.get()).booleanValue()) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (MinecraftInstance.mc.field_71439_g.field_71071_by.field_70460_b[((Number)this.armorIndexValue.get()).intValue()] != null && entityPlayer.field_71071_by.field_70460_b[((Number)this.armorIndexValue.get()).intValue()] != null) {
                String string2 = myHead = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70460_b[((Number)this.armorIndexValue.get()).intValue()];
                Intrinsics.checkNotNull(string2);
                Item item = string2.func_77973_b();
                Intrinsics.checkNotNull(item);
                ItemArmor myItemArmor = (ItemArmor)item;
                ItemStack entityHead = entityPlayer.field_71071_by.field_70460_b[((Number)this.armorIndexValue.get()).intValue()];
                Item item2 = myHead.func_77973_b();
                Intrinsics.checkNotNull(item2);
                ItemArmor entityItemArmor = (ItemArmor)item2;
                int n = myItemArmor.func_82814_b((ItemStack)myHead);
                ItemStack itemStack = entityHead;
                Intrinsics.checkNotNull(itemStack);
                if (n == entityItemArmor.func_82814_b(itemStack)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.gommeSWValue.get()).booleanValue() && MinecraftInstance.mc.field_71439_g.func_145748_c_() != null && entity.func_145748_c_() != null) {
            myHead = entity.func_145748_c_().func_150254_d();
            Intrinsics.checkNotNullExpressionValue(myHead, "entity.displayName.formattedText");
            targetName = StringsKt.replace$default(myHead, "\u00a7r", "", false, 4, null);
            string = MinecraftInstance.mc.field_71439_g.func_145748_c_().func_150254_d();
            Intrinsics.checkNotNullExpressionValue(string, "mc.thePlayer.displayName.formattedText");
            clientName = StringsKt.replace$default(string, "\u00a7r", "", false, 4, null);
            if (StringsKt.startsWith$default(targetName, "T", false, 2, null) && StringsKt.startsWith$default(clientName, "T", false, 2, null) && Character.isDigit(targetName.charAt(1)) && Character.isDigit(clientName.charAt(1))) {
                return targetName.charAt(1) == clientName.charAt(1);
            }
        }
        if (((Boolean)this.colorValue.get()).booleanValue() && MinecraftInstance.mc.field_71439_g.func_145748_c_() != null && entity.func_145748_c_() != null) {
            clientName = entity.func_145748_c_().func_150254_d();
            Intrinsics.checkNotNullExpressionValue(clientName, "entity.displayName.formattedText");
            targetName = StringsKt.replace$default(clientName, "\u00a7r", "", false, 4, null);
            string = MinecraftInstance.mc.field_71439_g.func_145748_c_().func_150254_d();
            Intrinsics.checkNotNullExpressionValue(string, "mc.thePlayer.displayName.formattedText");
            clientName = StringsKt.replace$default(string, "\u00a7r", "", false, 4, null);
            return StringsKt.startsWith$default(targetName, Intrinsics.stringPlus("\u00a7", Character.valueOf(clientName.charAt(1))), false, 2, null);
        }
        return false;
    }

    public static final /* synthetic */ BoolValue access$getArmorColorValue$p(Teams $this) {
        return $this.armorColorValue;
    }
}

