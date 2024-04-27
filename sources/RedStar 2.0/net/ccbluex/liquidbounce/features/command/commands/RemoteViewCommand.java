package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/RemoteViewCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class RemoteViewCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length < 2) {
            if (Intrinsics.areEqual(MinecraftInstance.mc.getRenderViewEntity(), MinecraftInstance.mc.getThePlayer()) ^ true) {
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
            if (!Intrinsics.areEqual(targetName, entity.getName())) continue;
            MinecraftInstance.mc.setRenderViewEntity(entity);
            this.chat("Now viewing perspective of §8" + entity.getName() + "§3.");
            this.chat("Execute §8" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "remoteview §3again to go back to yours.");
            break;
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
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
                    if (StringsKt.startsWith(v1, args[0], true)) {
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
                destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
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
