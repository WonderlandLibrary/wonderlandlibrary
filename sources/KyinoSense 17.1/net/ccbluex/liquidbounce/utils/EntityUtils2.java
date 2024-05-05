/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.monster.EntityGolem
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.combat.Teams;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u001aJ\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0017J\u0010\u0010\u001c\u001a\u00020\u00042\b\u0010\u001d\u001a\u0004\u0018\u00010\u0017R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0006\"\u0004\b\u0014\u0010\b\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/utils/EntityUtils2;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "targetAnimals", "", "getTargetAnimals", "()Z", "setTargetAnimals", "(Z)V", "targetDead", "getTargetDead", "setTargetDead", "targetInvisible", "getTargetInvisible", "setTargetInvisible", "targetMobs", "getTargetMobs", "setTargetMobs", "targetPlayer", "getTargetPlayer", "setTargetPlayer", "canRayCast", "entity", "Lnet/minecraft/entity/Entity;", "isAnimal", "isFriend", "", "isMob", "isRendered", "entityToCheck", "KyinoClient"})
public final class EntityUtils2
extends MinecraftInstance {
    private static boolean targetInvisible;
    private static boolean targetPlayer;
    private static boolean targetMobs;
    private static boolean targetAnimals;
    private static boolean targetDead;
    public static final EntityUtils2 INSTANCE;

    public final boolean getTargetInvisible() {
        return targetInvisible;
    }

    public final void setTargetInvisible(boolean bl) {
        targetInvisible = bl;
    }

    public final boolean getTargetPlayer() {
        return targetPlayer;
    }

    public final void setTargetPlayer(boolean bl) {
        targetPlayer = bl;
    }

    public final boolean getTargetMobs() {
        return targetMobs;
    }

    public final void setTargetMobs(boolean bl) {
        targetMobs = bl;
    }

    public final boolean getTargetAnimals() {
        return targetAnimals;
    }

    public final void setTargetAnimals(boolean bl) {
        targetAnimals = bl;
    }

    public final boolean getTargetDead() {
        return targetDead;
    }

    public final void setTargetDead(boolean bl) {
        targetDead = bl;
    }

    public final boolean canRayCast(@NotNull Entity entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        if (entity instanceof EntityLivingBase) {
            if (entity instanceof EntityPlayer) {
                Module teams;
                Module module = teams = LiquidBounce.INSTANCE.getModuleManager().getModule(Teams.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                return !module.getState();
            }
            return this.isMob(entity) || this.isAnimal(entity);
        }
        return false;
    }

    public final boolean isFriend(@NotNull Entity entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        return entity instanceof EntityPlayer && entity.func_70005_c_() != null && LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(ColorUtils.stripColor(entity.func_70005_c_()));
    }

    public final boolean isFriend(@NotNull String entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        return LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(entity);
    }

    public final boolean isAnimal(@NotNull Entity entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityVillager || entity instanceof EntityBat;
    }

    public final boolean isMob(@NotNull Entity entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        return entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }

    public final boolean isRendered(@Nullable Entity entityToCheck) {
        return MinecraftInstance.mc.field_71441_e != null && MinecraftInstance.mc.field_71441_e.func_72910_y().contains(entityToCheck);
    }

    private EntityUtils2() {
    }

    static {
        EntityUtils2 entityUtils2;
        INSTANCE = entityUtils2 = new EntityUtils2();
        targetPlayer = true;
        targetMobs = true;
    }
}

