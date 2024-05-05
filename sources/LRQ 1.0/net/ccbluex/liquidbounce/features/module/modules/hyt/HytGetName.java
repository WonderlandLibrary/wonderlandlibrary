/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 *  net.minecraft.network.play.server.SPacketChat
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.server.SPacketChat;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytGetName", description="\u5df2\u4fee\u590d:)", category=ModuleCategory.HYT)
public final class HytGetName
extends Module {
    private final ListValue mode = new ListValue("GetNameMode", new String[]{"4V4/1V1", "32/64", "16V16"}, "4V4/1V1");

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onPacket(PacketEvent event) {
        String name;
        boolean bl;
        String string;
        Object packet;
        block18: {
            String name2;
            boolean bl2;
            String string2;
            IPacket $this$unwrap$iv = event.getPacket();
            boolean $i$f$unwrap = false;
            packet = ((PacketImpl)$this$unwrap$iv).getWrapped();
            if (!(packet instanceof SPacketChat)) return;
            String string3 = (String)this.mode.get();
            $i$f$unwrap = false;
            String string4 = string3;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string3 = string4.toLowerCase();
            switch (string3.hashCode()) {
                case 48636014: {
                    if (!string3.equals("32/64")) return;
                    break;
                }
                case 46976214: {
                    if (!string3.equals("16v16")) return;
                    break block18;
                }
                case -1961702257: {
                    String name3;
                    boolean bl3;
                    String string5;
                    if (!string3.equals("4v4/1v1")) return;
                    Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
                    Matcher matcher2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
                    if (matcher.find()) {
                        string5 = matcher.group(1);
                        bl3 = false;
                        String string6 = string5;
                        if (string6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        name3 = ((Object)StringsKt.trim((CharSequence)string6)).toString();
                        if (name3.equals("") ^ true) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name3);
                            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name3);
                            new Thread(new Runnable(name3){
                                final /* synthetic */ String $name;

                                public final void run() {
                                    try {
                                        Thread.sleep(5000L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                                    }
                                    catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                {
                                    this.$name = string;
                                }
                            }).start();
                        }
                    }
                    if (!matcher2.find()) return;
                    string5 = matcher2.group(1);
                    bl3 = false;
                    String string7 = string5;
                    if (string7 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    name3 = ((Object)StringsKt.trim((CharSequence)string7)).toString();
                    if (!(name3.equals("") ^ true)) return;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name3);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name3);
                    new Thread(new Runnable(name3){
                        final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(5000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.$name = string;
                        }
                    }).start();
                    return;
                }
            }
            Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
            Matcher matcher2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
            if (matcher.find()) {
                string2 = matcher.group(1);
                bl2 = false;
                String string8 = string2;
                if (string8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                name2 = ((Object)StringsKt.trim((CharSequence)string8)).toString();
                if (name2.equals("") ^ true) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name2);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name2);
                    new Thread(new Runnable(name2){
                        final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(10000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.$name = string;
                        }
                    }).start();
                }
            }
            if (!matcher2.find()) return;
            string2 = matcher2.group(1);
            bl2 = false;
            String string9 = string2;
            if (string9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name2 = ((Object)StringsKt.trim((CharSequence)string9)).toString();
            if (!(name2.equals("") ^ true)) return;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name2);
            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name2);
            new Thread(new Runnable(name2){
                final /* synthetic */ String $name;

                public final void run() {
                    try {
                        Thread.sleep(10000L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                {
                    this.$name = string;
                }
            }).start();
            return;
        }
        Matcher matcher = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
        Matcher matcher2 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
        if (matcher.find()) {
            string = matcher.group(1);
            bl = false;
            String string10 = string;
            if (string10 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name = ((Object)StringsKt.trim((CharSequence)string10)).toString();
            if (name.equals("") ^ true) {
                LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name);
                new Thread(new Runnable(name){
                    final /* synthetic */ String $name;

                    public final void run() {
                        try {
                            Thread.sleep(10000L);
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    {
                        this.$name = string;
                    }
                }).start();
            }
        }
        if (!matcher2.find()) return;
        string = matcher2.group(1);
        bl = false;
        String string11 = string;
        if (string11 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        name = ((Object)StringsKt.trim((CharSequence)string11)).toString();
        if (!(name.equals("") ^ true)) return;
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fAdd HYT Bot:" + name);
        new Thread(new Runnable(name){
            final /* synthetic */ String $name;

            public final void run() {
                try {
                    Thread.sleep(10000L);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76LRQ" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            {
                this.$name = string;
            }
        }).start();
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    private final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }
}

