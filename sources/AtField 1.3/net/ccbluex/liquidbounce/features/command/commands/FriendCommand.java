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
import java.util.Iterator;
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
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                object = CollectionsKt.listOf((Object[])new String[]{"add", "remove", "list", "clear"});
                bl = false;
                Object object2 = object;
                Collection collection = new ArrayList();
                boolean bl2 = false;
                Iterator iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    Object t = iterator2.next();
                    String string = (String)t;
                    boolean bl3 = false;
                    if (!StringsKt.startsWith((String)string, (String)stringArray[0], (boolean)true)) continue;
                    collection.add(t);
                }
                list = (List)collection;
                break;
            }
            case 2: {
                object = stringArray[0];
                bl = false;
                Object object3 = object;
                if (object3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                switch (((String)object3).toLowerCase()) {
                    case "add": {
                        boolean bl4;
                        IEntityPlayer iEntityPlayer;
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        Iterable iterable = iWorldClient.getPlayerEntities();
                        boolean bl5 = false;
                        Iterable iterable2 = iterable;
                        Collection collection = new ArrayList();
                        boolean bl6 = false;
                        for (Object t : iterable2) {
                            iEntityPlayer = (IEntityPlayer)t;
                            bl4 = false;
                            String string = iEntityPlayer.getName();
                            if (!(string != null ? StringsKt.startsWith((String)string, (String)stringArray[1], (boolean)true) : false)) continue;
                            collection.add(t);
                        }
                        iterable = (List)collection;
                        bl5 = false;
                        iterable2 = iterable;
                        collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
                        bl6 = false;
                        for (Object t : iterable2) {
                            String string;
                            iEntityPlayer = (IEntityPlayer)t;
                            Collection collection2 = collection;
                            bl4 = false;
                            if (iEntityPlayer.getName() == null) {
                                Intrinsics.throwNpe();
                            }
                            collection2.add(string);
                        }
                        return (List)collection;
                    }
                    case "remove": {
                        boolean bl7;
                        Object object4;
                        Iterable iterable = LiquidBounce.INSTANCE.getFileManager().friendsConfig.getFriends();
                        boolean bl8 = false;
                        Iterable iterable3 = iterable;
                        Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
                        boolean bl9 = false;
                        for (Object t : iterable3) {
                            object4 = (FriendsConfig.Friend)t;
                            Collection collection3 = collection;
                            bl7 = false;
                            String string = ((FriendsConfig.Friend)object4).getPlayerName();
                            collection3.add(string);
                        }
                        iterable = (List)collection;
                        bl8 = false;
                        iterable3 = iterable;
                        collection = new ArrayList();
                        bl9 = false;
                        for (Object t : iterable3) {
                            object4 = (String)t;
                            bl7 = false;
                            if (!StringsKt.startsWith((String)object4, (String)stringArray[1], (boolean)true)) continue;
                            collection.add(t);
                        }
                        return (List)collection;
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

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
            if (StringsKt.equals((String)stringArray[1], (String)"add", (boolean)true)) {
                if (stringArray.length > 2) {
                    String string = stringArray[2];
                    CharSequence charSequence = string;
                    boolean bl = false;
                    if (charSequence.length() == 0) {
                        this.chat("The name is empty.");
                        return;
                    }
                    if (stringArray.length > 3 ? friendsConfig.addFriend(string, StringUtils.toCompleteString(stringArray, 3)) : friendsConfig.addFriend(string)) {
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                        this.chat("\u00a7a\u00a7l" + string + "\u00a73 was added to your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("The name is already in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend add <name> [alias]");
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"remove", (boolean)true)) {
                if (stringArray.length > 2) {
                    String string = stringArray[2];
                    if (friendsConfig.removeFriend(string)) {
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                        this.chat("\u00a7a\u00a7l" + string + "\u00a73 was removed from your friend list.");
                        this.playEdit();
                    } else {
                        this.chat("This name is not in the list.");
                    }
                    return;
                }
                this.chatSyntax("friend remove <name>");
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"clear", (boolean)true)) {
                int n = friendsConfig.getFriends().size();
                friendsConfig.clearFriends();
                LiquidBounce.INSTANCE.getFileManager().saveConfig(friendsConfig);
                this.chat("Removed " + n + " friend(s).");
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"list", (boolean)true)) {
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

    public FriendCommand() {
        super("friend", "friends");
    }
}

