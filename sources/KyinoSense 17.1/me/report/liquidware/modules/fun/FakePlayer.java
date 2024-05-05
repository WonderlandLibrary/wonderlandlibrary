/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package me.report.liquidware.modules.fun;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@ModuleInfo(name="FakePlayer", description="Fake Player", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lme/report/liquidware/modules/fun/FakePlayer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fakePlayer", "Lnet/minecraft/client/entity/EntityOtherPlayerMP;", "onDisable", "", "onEnable", "KyinoClient"})
public final class FakePlayer
extends Module {
    private EntityOtherPlayerMP fakePlayer;

    @Override
    public void onEnable() {
        World world = (World)FakePlayer.access$getMc$p$s1046033730().field_71441_e;
        EntityPlayerSP entityPlayerSP = FakePlayer.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        EntityOtherPlayerMP entityOtherPlayerMP = this.fakePlayer = new EntityOtherPlayerMP(world, entityPlayerSP.func_146103_bH());
        if (entityOtherPlayerMP == null) {
            Intrinsics.throwNpe();
        }
        entityOtherPlayerMP.func_71049_a((EntityPlayer)FakePlayer.access$getMc$p$s1046033730().field_71439_g, true);
        if (this.fakePlayer == null) {
            Intrinsics.throwNpe();
        }
        this.fakePlayer.field_70759_as = FakePlayer.access$getMc$p$s1046033730().field_71439_g.field_70759_as;
        EntityOtherPlayerMP entityOtherPlayerMP2 = this.fakePlayer;
        if (entityOtherPlayerMP2 == null) {
            Intrinsics.throwNpe();
        }
        entityOtherPlayerMP2.func_82149_j((Entity)FakePlayer.access$getMc$p$s1046033730().field_71439_g);
        FakePlayer.access$getMc$p$s1046033730().field_71441_e.func_73027_a(-1000, (Entity)this.fakePlayer);
    }

    @Override
    public void onDisable() {
        WorldClient worldClient = FakePlayer.access$getMc$p$s1046033730().field_71441_e;
        EntityOtherPlayerMP entityOtherPlayerMP = this.fakePlayer;
        if (entityOtherPlayerMP == null) {
            Intrinsics.throwNpe();
        }
        worldClient.func_73028_b(entityOtherPlayerMP.func_145782_y());
        this.fakePlayer = null;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

