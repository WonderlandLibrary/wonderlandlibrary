/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Teams", category=ModuleCategory.COMBAT, description="Prevents Killaura from attacking team mates.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Teams;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "armorValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorValue", "gommeSWValue", "scoreboardValue", "isInYourTeam", "", "entity", "Lnet/minecraft/entity/EntityLivingBase;", "KyinoClient"})
public final class Teams
extends Module {
    private final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    private final BoolValue colorValue = new BoolValue("Color", true);
    private final BoolValue gommeSWValue = new BoolValue("GommeSW", false);
    private final BoolValue armorValue = new BoolValue("ArmorColor", false);

    public final boolean isInYourTeam(@NotNull EntityLivingBase entity) {
        String clientName;
        String targetName;
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        if (Teams.access$getMc$p$s1046033730().field_71439_g == null) {
            return false;
        }
        if (((Boolean)this.scoreboardValue.get()).booleanValue()) {
            EntityPlayerSP entityPlayerSP = Teams.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_96124_cp() != null && entity.func_96124_cp() != null) {
                EntityPlayerSP entityPlayerSP2 = Teams.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (entityPlayerSP2.func_96124_cp().func_142054_a(entity.func_96124_cp())) {
                    return true;
                }
            }
        }
        if (((Boolean)this.gommeSWValue.get()).booleanValue()) {
            EntityPlayerSP entityPlayerSP = Teams.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_145748_c_() != null && entity.func_145748_c_() != null) {
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String string = iChatComponent.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string, "entity.displayName.formattedText");
                targetName = StringsKt.replace$default(string, "\u00a7r", "", false, 4, null);
                EntityPlayerSP entityPlayerSP3 = Teams.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                IChatComponent iChatComponent2 = entityPlayerSP3.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent2, "mc.thePlayer.displayName");
                String string2 = iChatComponent2.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string2, "mc.thePlayer.displayName.formattedText");
                clientName = StringsKt.replace$default(string2, "\u00a7r", "", false, 4, null);
                if (StringsKt.startsWith$default(targetName, "T", false, 2, null) && StringsKt.startsWith$default(clientName, "T", false, 2, null)) {
                    char c = targetName.charAt(1);
                    boolean bl = false;
                    if (Character.isDigit(c)) {
                        c = clientName.charAt(1);
                        bl = false;
                        if (Character.isDigit(c)) {
                            return targetName.charAt(1) == clientName.charAt(1);
                        }
                    }
                }
            }
        }
        if (((Boolean)this.armorValue.get()).booleanValue()) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (Teams.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70460_b[3] != null && entityPlayer.field_71071_by.field_70460_b[3] != null) {
                ItemStack myHead;
                ItemStack itemStack = myHead = Teams.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70460_b[3];
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "myHead");
                Item item = itemStack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
                }
                ItemArmor myItemArmor = (ItemArmor)item;
                ItemStack entityHead = entityPlayer.field_71071_by.field_70460_b[3];
                Item item2 = myHead.func_77973_b();
                if (item2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
                }
                ItemArmor entityItemArmor = (ItemArmor)item2;
                if (myItemArmor.func_82814_b(myHead) == entityItemArmor.func_82814_b(entityHead)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.colorValue.get()).booleanValue()) {
            EntityPlayerSP entityPlayerSP = Teams.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_145748_c_() != null && entity.func_145748_c_() != null) {
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String string = iChatComponent.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string, "entity.displayName.formattedText");
                targetName = StringsKt.replace$default(string, "\u00a7r", "", false, 4, null);
                EntityPlayerSP entityPlayerSP4 = Teams.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                IChatComponent iChatComponent3 = entityPlayerSP4.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent3, "mc.thePlayer.displayName");
                String string3 = iChatComponent3.func_150254_d();
                Intrinsics.checkExpressionValueIsNotNull(string3, "mc.thePlayer.displayName.formattedText");
                clientName = StringsKt.replace$default(string3, "\u00a7r", "", false, 4, null);
                return StringsKt.startsWith$default(targetName, "" + '\u00a7' + clientName.charAt(1), false, 2, null);
            }
        }
        return false;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

