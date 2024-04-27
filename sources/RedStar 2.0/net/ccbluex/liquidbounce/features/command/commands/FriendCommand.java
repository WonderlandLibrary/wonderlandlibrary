package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/FriendCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class FriendCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
            if (StringsKt.equals(args[1], "add", true)) {
                if (args.length > 2) {
                    String name = args[2];
                    CharSequence charSequence = name;
                    boolean bl = false;
                    if (charSequence.length() == 0) {
                        this.chat("The name is empty.");
                        return;
                    }
                    if (args.length > 3 ? friendsConfig.addFriend(name, StringUtils.toCompleteString(args, 3)) : friendsConfig.addFriend(name)) {
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                        this.chat("§a§l" + name + "§3 was added to your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("The name is already in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend add <name> [alias]");
                return;
            }
            if (StringsKt.equals(args[1], "remove", true)) {
                if (args.length > 2) {
                    String name = args[2];
                    if (friendsConfig.removeFriend(name)) {
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                        this.chat("§a§l" + name + "§3 was removed from your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("This name is not in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend remove <name>");
                return;
            }
            if (StringsKt.equals(args[1], "clear", true)) {
                FriendsConfig friendsConfig2 = friendsConfig;
                Intrinsics.checkExpressionValueIsNotNull(friendsConfig2, "friendsConfig");
                int friends = friendsConfig2.getFriends().size();
                friendsConfig.clearFriends();
                LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                this.chat("Removed " + friends + " friend(s).");
                return;
            }
            if (StringsKt.equals(args[1], "list", true)) {
                this.chat("Your Friends:");
                FriendsConfig friendsConfig3 = friendsConfig;
                Intrinsics.checkExpressionValueIsNotNull(friendsConfig3, "friendsConfig");
                for (FriendsConfig.Friend friend : friendsConfig3.getFriends()) {
                    StringBuilder stringBuilder = new StringBuilder().append("§7> §a§l");
                    FriendsConfig.Friend friend2 = friend;
                    Intrinsics.checkExpressionValueIsNotNull(friend2, "friend");
                    this.chat(stringBuilder.append(friend2.getPlayerName()).append(" §c(§7§l").append(friend.getAlias()).append("§c)").toString());
                }
                this.chat("You have §c" + friendsConfig.getFriends().size() + "§3 friends.");
                return;
            }
        }
        this.chatSyntax("friend <add/remove/list/clear>");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull(args, "args");
        Object object = args;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf("add", "remove", "list", "clear");
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith(it, args[0], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                object = args[0];
                boolean $i$f$filter = false;
                Object object2 = object;
                if (object2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string = ((String)object2).toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string, "(this as java.lang.String).toLowerCase()");
                switch (string) {
                    case "add": {
                        void $this$mapTo$iv$iv;
                        IEntityPlayer it;
                        Iterable $this$filterTo$iv$iv;
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        Iterable $this$filter$iv = iWorldClient.getPlayerEntities();
                        boolean $i$f$filter2 = false;
                        Iterable destination$iv$iv = $this$filter$iv;
                        Collection destination$iv$iv2 = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (IEntityPlayer)element$iv$iv;
                            boolean bl3 = false;
                            String string2 = it.getName();
                            if (!(string2 != null ? StringsKt.startsWith(string2, args[1], true) : false)) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        Iterable $this$map$iv = (List)destination$iv$iv2;
                        boolean $i$f$map = false;
                        $this$filterTo$iv$iv = $this$map$iv;
                        destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            String string3;
                            it = (IEntityPlayer)item$iv$iv;
                            Collection collection = destination$iv$iv2;
                            boolean bl4 = false;
                            if (it.getName() == null) {
                                Intrinsics.throwNpe();
                            }
                            collection.add(string3);
                        }
                        return (List)destination$iv$iv2;
                    }
                    case "remove": {
                        void $this$filterTo$iv$iv;
                        Object it;
                        FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
                        Intrinsics.checkExpressionValueIsNotNull(friendsConfig, "LiquidBounce.fileManager.friendsConfig");
                        List<FriendsConfig.Friend> list2 = friendsConfig.getFriends();
                        Intrinsics.checkExpressionValueIsNotNull(list2, "LiquidBounce.fileManager.friendsConfig.friends");
                        Iterable $this$map$iv = list2;
                        boolean $i$f$map = false;
                        Iterable $this$mapTo$iv$iv = $this$map$iv;
                        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            it = (FriendsConfig.Friend)item$iv$iv;
                            Collection collection = destination$iv$iv;
                            boolean bl5 = false;
                            FriendsConfig.Friend friend = it;
                            Intrinsics.checkExpressionValueIsNotNull(friend, "it");
                            String string4 = friend.getPlayerName();
                            collection.add(string4);
                        }
                        Iterable $this$filter$iv = (List)destination$iv$iv;
                        boolean $i$f$filter3 = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl6 = false;
                            Object object3 = it;
                            Intrinsics.checkExpressionValueIsNotNull(object3, "it");
                            if (!StringsKt.startsWith((String)object3, args[1], true)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv;
                    }
                }
                return CollectionsKt.emptyList();
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public FriendCommand() {
        super("friend", "friends");
    }
}
