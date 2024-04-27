/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
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
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/FriendCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiKingSense"})
public final class FriendCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        if (args.length > 1) {
            FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
            if (StringsKt.equals((String)args[1], (String)"add", (boolean)true)) {
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
                        this.chat("\u00a7a\u00a7l" + name + "\u00a73 was added to your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("The name is already in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend add <name> [alias]");
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"remove", (boolean)true)) {
                if (args.length > 2) {
                    String name = args[2];
                    if (friendsConfig.removeFriend(name)) {
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                        this.chat("\u00a7a\u00a7l" + name + "\u00a73 was removed from your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("This name is not in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend remove <name>");
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"clear", (boolean)true)) {
                FriendsConfig friendsConfig2 = friendsConfig;
                Intrinsics.checkExpressionValueIsNotNull((Object)friendsConfig2, (String)"friendsConfig");
                int friends = friendsConfig2.getFriends().size();
                friendsConfig.clearFriends();
                LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                this.chat("Removed " + friends + " friend(s).");
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
                this.chat("Your Friends:");
                FriendsConfig friendsConfig3 = friendsConfig;
                Intrinsics.checkExpressionValueIsNotNull((Object)friendsConfig3, (String)"friendsConfig");
                for (FriendsConfig.Friend friend : friendsConfig3.getFriends()) {
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77> \u00a7a\u00a7l");
                    FriendsConfig.Friend friend2 = friend;
                    Intrinsics.checkExpressionValueIsNotNull((Object)friend2, (String)"friend");
                    this.chat(stringBuilder.append(friend2.getPlayerName()).append(" \u00a7c(\u00a77\u00a7l").append(friend.getAlias()).append("\u00a7c)").toString());
                }
                this.chat("You have \u00a7c" + friendsConfig.getFriends().size() + "\u00a73 friends.");
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
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        Object object = args;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf((Object[])new String[]{"add", "remove", "list", "clear"});
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
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
                Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"(this as java.lang.String).toLowerCase()");
                switch (string) {
                    case "add": {
                        void $this$mapTo$iv$iv;
                        IEntityPlayer it;
                        Iterable $this$filterTo$iv$iv;
                        Iterable $this$filter$iv = MinecraftInstance.mc.getTheWorld().getPlayerEntities();
                        boolean $i$f$filter2 = false;
                        Iterable destination$iv$iv = $this$filter$iv;
                        Collection destination$iv$iv2 = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (IEntityPlayer)element$iv$iv;
                            boolean bl3 = false;
                            String string2 = it.getName();
                            if (!(string2 != null ? StringsKt.startsWith((String)string2, (String)args[1], (boolean)true) : false)) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        Iterable $this$map$iv = (List)destination$iv$iv2;
                        boolean $i$f$map = false;
                        $this$filterTo$iv$iv = $this$map$iv;
                        destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            it = (IEntityPlayer)item$iv$iv;
                            Collection collection = destination$iv$iv2;
                            boolean bl4 = false;
                            String string3 = it.getName();
                            collection.add(string3);
                        }
                        return (List)destination$iv$iv2;
                    }
                    case "remove": {
                        void $this$filterTo$iv$iv;
                        Object it;
                        FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
                        Intrinsics.checkExpressionValueIsNotNull((Object)friendsConfig, (String)"LiquidBounce.fileManager.friendsConfig");
                        List<FriendsConfig.Friend> list2 = friendsConfig.getFriends();
                        Intrinsics.checkExpressionValueIsNotNull(list2, (String)"LiquidBounce.fileManager.friendsConfig.friends");
                        Iterable $this$map$iv = list2;
                        boolean $i$f$map = false;
                        Iterable $this$mapTo$iv$iv = $this$map$iv;
                        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            it = (FriendsConfig.Friend)item$iv$iv;
                            Collection collection = destination$iv$iv;
                            boolean bl5 = false;
                            FriendsConfig.Friend friend = it;
                            Intrinsics.checkExpressionValueIsNotNull((Object)friend, (String)"it");
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
                            Intrinsics.checkExpressionValueIsNotNull((Object)object3, (String)"it");
                            if (!StringsKt.startsWith((String)object3, (String)args[1], (boolean)true)) continue;
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

