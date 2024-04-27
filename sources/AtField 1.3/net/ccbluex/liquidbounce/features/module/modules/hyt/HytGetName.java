/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketChat
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
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

    @EventTarget
    public final void onWorld(@Nullable WorldEvent worldEvent) {
        this.clearAll();
    }

    private final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        String string;
        boolean bl;
        String string2;
        Packet packet;
        block18: {
            String string3;
            boolean bl2;
            String string4;
            Object object = packetEvent.getPacket();
            boolean bl3 = false;
            packet = ((PacketImpl)object).getWrapped();
            if (!(packet instanceof SPacketChat)) return;
            object = (String)this.mode.get();
            bl3 = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            object = ((String)object2).toLowerCase();
            switch (((String)object).hashCode()) {
                case 48636014: {
                    if (!((String)object).equals("32/64")) return;
                    break;
                }
                case 46976214: {
                    if (!((String)object).equals("16v16")) return;
                    break block18;
                }
                case -1961702257: {
                    String string5;
                    boolean bl4;
                    String string6;
                    if (!((String)object).equals("4v4/1v1")) return;
                    Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
                    Matcher matcher2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
                    if (matcher.find()) {
                        string6 = matcher.group(1);
                        bl4 = false;
                        String string7 = string6;
                        if (string7 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        string5 = ((Object)StringsKt.trim((CharSequence)string7)).toString();
                        if (string5.equals("") ^ true) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string5);
                            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string5);
                            new Thread(new Runnable(string5){
                                final String $name;
                                {
                                    this.$name = string;
                                }

                                static {
                                }

                                public final void run() {
                                    try {
                                        Thread.sleep(5000L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                                    }
                                    catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                    if (!matcher2.find()) return;
                    string6 = matcher2.group(1);
                    bl4 = false;
                    String string8 = string6;
                    if (string8 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    string5 = ((Object)StringsKt.trim((CharSequence)string8)).toString();
                    if (!(string5.equals("") ^ true)) return;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string5);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string5);
                    new Thread(new Runnable(string5){
                        final String $name;

                        public final void run() {
                            try {
                                Thread.sleep(5000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                            }
                            catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        {
                            this.$name = string;
                        }

                        static {
                        }
                    }).start();
                    return;
                }
            }
            Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
            Matcher matcher3 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
            if (matcher.find()) {
                string4 = matcher.group(1);
                bl2 = false;
                String string9 = string4;
                if (string9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                string3 = ((Object)StringsKt.trim((CharSequence)string9)).toString();
                if (string3.equals("") ^ true) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string3);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string3);
                    new Thread(new Runnable(string3){
                        final String $name;

                        public final void run() {
                            try {
                                Thread.sleep(10000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                            }
                            catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }

                        static {
                        }
                        {
                            this.$name = string;
                        }
                    }).start();
                }
            }
            if (!matcher3.find()) return;
            string4 = matcher3.group(1);
            bl2 = false;
            String string10 = string4;
            if (string10 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            string3 = ((Object)StringsKt.trim((CharSequence)string10)).toString();
            if (!(string3.equals("") ^ true)) return;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string3);
            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string3);
            new Thread(new Runnable(string3){
                final String $name;

                static {
                }
                {
                    this.$name = string;
                }

                public final void run() {
                    try {
                        Thread.sleep(10000L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }).start();
            return;
        }
        Matcher matcher = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
        Matcher matcher4 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
        if (matcher.find()) {
            string2 = matcher.group(1);
            bl = false;
            String string11 = string2;
            if (string11 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            string = ((Object)StringsKt.trim((CharSequence)string11)).toString();
            if (string.equals("") ^ true) {
                LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string);
                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string);
                new Thread(new Runnable(string){
                    final String $name;
                    {
                        this.$name = string;
                    }

                    public final void run() {
                        try {
                            Thread.sleep(10000L);
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                        }
                        catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }

                    static {
                    }
                }).start();
            }
        }
        if (!matcher4.find()) return;
        string2 = matcher4.group(1);
        bl = false;
        String string12 = string2;
        if (string12 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        string = ((Object)StringsKt.trim((CharSequence)string12)).toString();
        if (!(string.equals("") ^ true)) return;
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string);
        ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fAdd HYT Bot:" + string);
        new Thread(new Runnable(string){
            final String $name;
            {
                this.$name = string;
            }

            public final void run() {
                try {
                    Thread.sleep(10000L);
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a76AtField" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

            static {
            }
        }).start();
        return;
    }
}

