/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class RemoteViewCommand
extends Command {
    public RemoteViewCommand() {
        super("remoteview", "rv");
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length < 2) {
            if (MinecraftInstance.mc.getRenderViewEntity().equals(MinecraftInstance.mc.getThePlayer()) ^ true) {
                MinecraftInstance.mc.setRenderViewEntity(MinecraftInstance.mc.getThePlayer());
                return;
            }
            this.chatSyntax("remoteview <username>");
            return;
        }
        String string = stringArray[1];
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            if (!string.equals(iEntity.getName())) continue;
            MinecraftInstance.mc.setRenderViewEntity(iEntity);
            this.chat("Now viewing perspective of \u00a78" + iEntity.getName() + "\u00a73.");
            this.chat("Execute \u00a78" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "remoteview \u00a73again to go back to yours.");
            break;
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public List tabComplete(String[] var1_1) {
        var2_2 = var1_1;
        var3_3 = false;
        if (((String[])var2_2).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (var1_1.length) {
            case 1: {
                v0 = MinecraftInstance.mc.getTheWorld();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                var2_2 = v0.getPlayerEntities();
                var3_3 = false;
                var4_4 = var2_2;
                var5_5 = new ArrayList<E>();
                var6_6 = false;
                var7_7 = var4_4.iterator();
                while (var7_7.hasNext()) {
                    var8_8 = var7_7.next();
                    var9_9 = (IEntityPlayer)var8_8;
                    var10_10 = false;
                    if (var9_9.getName() == null) ** GOTO lbl-1000
                    v1 = var9_9.getName();
                    if (v1 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (StringsKt.startsWith((String)v1, (String)var1_1[0], (boolean)true)) {
                        v2 = true;
                    } else lbl-1000:
                    // 2 sources

                    {
                        v2 = false;
                    }
                    if (!v2) continue;
                    var5_5.add(var8_8);
                }
                var2_2 = (List)var5_5;
                var3_3 = false;
                var4_4 = var2_2;
                var5_5 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)var2_2, (int)10));
                var6_6 = false;
                var7_7 = var4_4.iterator();
                while (var7_7.hasNext()) {
                    var8_8 = var7_7.next();
                    var9_9 = (IEntityPlayer)var8_8;
                    var11_11 = var5_5;
                    var10_10 = false;
                    if (var9_9.getName() == null) {
                        Intrinsics.throwNpe();
                    }
                    var11_11.add(var12_12);
                }
                return (List)var5_5;
            }
        }
        return CollectionsKt.emptyList();
    }
}

