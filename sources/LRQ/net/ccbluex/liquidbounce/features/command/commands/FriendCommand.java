/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

public final class FriendCommand
extends Command {
    @Override
    public void execute(String[] args) {
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
                int friends = friendsConfig.getFriends().size();
                friendsConfig.clearFriends();
                LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                this.chat("Removed " + friends + " friend(s).");
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
                this.chat("Your Friends:");
                for (FriendsConfig.Friend friend : friendsConfig.getFriends()) {
                    this.chat("\u00a77> \u00a7a\u00a7l" + friend.getPlayerName() + " \u00a7c(\u00a77\u00a7l" + friend.getAlias() + "\u00a7c)");
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
    public List<String> tabComplete(String[] args) {
        List list;
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
                switch (((String)object2).toLowerCase()) {
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
                            String string = it.getName();
                            if (!(string != null ? StringsKt.startsWith((String)string, (String)args[1], (boolean)true) : false)) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        Iterable $this$map$iv = (List)destination$iv$iv2;
                        boolean $i$f$map = false;
                        $this$filterTo$iv$iv = $this$map$iv;
                        destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            String string;
                            it = (IEntityPlayer)item$iv$iv;
                            Collection collection = destination$iv$iv2;
                            boolean bl4 = false;
                            if (it.getName() == null) {
                                Intrinsics.throwNpe();
                            }
                            collection.add(string);
                        }
                        return (List)destination$iv$iv2;
                    }
                    case "remove": {
                        void $this$filterTo$iv$iv;
                        Object it;
                        Iterable $this$map$iv = LiquidBounce.INSTANCE.getFileManager().friendsConfig.getFriends();
                        boolean $i$f$map = false;
                        Iterable $this$mapTo$iv$iv = $this$map$iv;
                        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            it = (FriendsConfig.Friend)item$iv$iv;
                            Collection collection = destination$iv$iv;
                            boolean bl5 = false;
                            String string = ((FriendsConfig.Friend)it).getPlayerName();
                            collection.add(string);
                        }
                        Iterable $this$filter$iv = (List)destination$iv$iv;
                        boolean $i$f$filter3 = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl6 = false;
                            if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
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

