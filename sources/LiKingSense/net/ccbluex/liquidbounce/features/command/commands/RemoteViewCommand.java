/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/RemoteViewCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiKingSense"})
public final class RemoteViewCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        if (args.length < 2) {
            if (Intrinsics.areEqual((Object)MinecraftInstance.mc.getRenderViewEntity(), (Object)MinecraftInstance.mc.getThePlayer()) ^ true) {
                MinecraftInstance.mc.setRenderViewEntity(MinecraftInstance.mc.getThePlayer());
                return;
            }
            this.chatSyntax("remoteview <username>");
            return;
        }
        String targetName = args[1];
        for (IEntity entity : MinecraftInstance.mc.getTheWorld().getLoadedEntityList()) {
            if (!Intrinsics.areEqual((Object)targetName, (Object)entity.getName())) continue;
            MinecraftInstance.mc.setRenderViewEntity(entity);
            this.chat("Now viewing perspective of \u00a78" + entity.getName() + "\u00a73.");
            this.chat("Execute \u00a78" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "remoteview \u00a73again to go back to yours.");
            break;
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$mapTo$iv$iv;
                IEntityPlayer it;
                Iterable $this$filterTo$iv$iv;
                Iterable $this$filter$iv = MinecraftInstance.mc.getTheWorld().getPlayerEntities();
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (IEntityPlayer)element$iv$iv;
                    boolean bl2 = false;
                    if (!(it.getName() != null && StringsKt.startsWith((String)it.getName(), (String)args[0], (boolean)true))) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$map$iv = (List)destination$iv$iv;
                boolean $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    it = (IEntityPlayer)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl3 = false;
                    String string = it.getName();
                    collection.add(string);
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

