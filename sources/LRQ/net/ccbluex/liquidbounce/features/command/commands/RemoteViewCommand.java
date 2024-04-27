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
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            if (MinecraftInstance.mc.getRenderViewEntity().equals(MinecraftInstance.mc.getThePlayer()) ^ true) {
                MinecraftInstance.mc.setRenderViewEntity(MinecraftInstance.mc.getThePlayer());
                return;
            }
            this.chatSyntax("remoteview <username>");
            return;
        }
        String targetName = args[1];
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
            if (!targetName.equals(entity.getName())) continue;
            MinecraftInstance.mc.setRenderViewEntity(entity);
            this.chat("Now viewing perspective of \u00a78" + entity.getName() + "\u00a73.");
            this.chat("Execute \u00a78" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "remoteview \u00a73again to go back to yours.");
            break;
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public List<String> tabComplete(String[] args) {
        var2_2 = args;
        var3_3 = false;
        if (var2_2.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                v0 = MinecraftInstance.mc.getTheWorld();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                $this$filter$iv = v0.getPlayerEntities();
                $i$f$filter = false;
                var4_4 = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    it = (IEntityPlayer)element$iv$iv;
                    $i$a$-filter-RemoteViewCommand$tabComplete$1 = false;
                    if (it.getName() == null) ** GOTO lbl-1000
                    v1 = it.getName();
                    if (v1 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (StringsKt.startsWith((String)v1, (String)args[0], (boolean)true)) {
                        v2 = true;
                    } else lbl-1000:
                    // 2 sources

                    {
                        v2 = false;
                    }
                    if (!v2) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                $this$map$iv = (List)destination$iv$iv;
                $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                $i$f$mapTo = false;
                for (T item$iv$iv : $this$mapTo$iv$iv) {
                    it = (IEntityPlayer)item$iv$iv;
                    var11_11 = destination$iv$iv;
                    $i$a$-map-RemoteViewCommand$tabComplete$2 = false;
                    if (it.getName() == null) {
                        Intrinsics.throwNpe();
                    }
                    var11_11.add(var12_12);
                }
                return (List)destination$iv$iv;
            }
        }
        return CollectionsKt.emptyList();
    }

    public RemoteViewCommand() {
        super("remoteview", "rv");
    }
}

