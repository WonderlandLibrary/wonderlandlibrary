/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HYTGetName", description="Whale :)", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007J\u0012\u0010\u000b\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/HytGetName;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "mode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "clearAll", "", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "LiKingSense"})
public final class HytGetName
extends Module {
    public final ListValue mode = new ListValue("GetNameMode", new String[]{"4V4/1V1", "32/64", "16V16"}, "4V4/1V1");

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
    public final void onPacket(@NotNull PacketEvent event) {
        String name;
        String string;
        Object packet;
        block19: {
            String name2;
            String string2;
            String string3;
            IPacket $this$unwrap$iv;
            Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
            IPacket iPacket = $this$unwrap$iv = event.getPacket();
            if (iPacket == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.PacketImpl<*>");
            }
            packet = ((PacketImpl)iPacket).getWrapped();
            if (!(packet instanceof SPacketChat)) return;
            String string4 = string3 = (String)this.mode.get();
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string5 = string4.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string5, (String)"(this as java.lang.String).toLowerCase()");
            string3 = string5;
            switch (string3.hashCode()) {
                case 48636014: {
                    if (!string3.equals("32/64")) return;
                    break;
                }
                case 46976214: {
                    if (!string3.equals("16v16")) return;
                    break block19;
                }
                case -1961702257: {
                    String name3;
                    String string6;
                    if (!string3.equals("4v4/1v1")) return;
                    Pattern pattern = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(");
                    ITextComponent iTextComponent = ((SPacketChat)packet).func_148915_c();
                    Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"packet.chatComponent");
                    Matcher matcher = pattern.matcher(iTextComponent.func_150260_c());
                    Pattern pattern2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))");
                    ITextComponent iTextComponent2 = ((SPacketChat)packet).func_148915_c();
                    Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent2, (String)"packet.chatComponent");
                    Matcher matcher2 = pattern2.matcher(iTextComponent2.func_150260_c());
                    if (matcher.find()) {
                        String string7 = matcher.group(1);
                        Intrinsics.checkExpressionValueIsNotNull((Object)string7, (String)"matcher.group(1)");
                        String string8 = string6 = string7;
                        if (string8 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        name3 = ((Object)StringsKt.trim((CharSequence)string8)).toString();
                        if ((Intrinsics.areEqual((Object)name3, (Object)"") ^ 1) != 0) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name3);
                            ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name3 + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                            new Thread(new Runnable(name3){
                                public final /* synthetic */ String $name;

                                public final void run() {
                                    try {
                                        Thread.sleep(118L - 200L + 114L + 4968L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
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
                    String string9 = matcher2.group(1);
                    Intrinsics.checkExpressionValueIsNotNull((Object)string9, (String)"matcher2.group(1)");
                    String string10 = string6 = string9;
                    if (string10 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    name3 = ((Object)StringsKt.trim((CharSequence)string10)).toString();
                    if ((Intrinsics.areEqual((Object)name3, (Object)"") ^ 1) == 0) return;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name3);
                    ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name3 + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                    new Thread(new Runnable(name3){
                        public final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(169L - 285L + 263L + 4853L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
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
            Pattern pattern = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(");
            ITextComponent iTextComponent = ((SPacketChat)packet).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"packet.chatComponent");
            Matcher matcher = pattern.matcher(iTextComponent.func_150260_c());
            Pattern pattern3 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))");
            ITextComponent iTextComponent3 = ((SPacketChat)packet).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent3, (String)"packet.chatComponent");
            Matcher matcher2 = pattern3.matcher(iTextComponent3.func_150260_c());
            if (matcher.find()) {
                String string11 = matcher.group(1);
                Intrinsics.checkExpressionValueIsNotNull((Object)string11, (String)"matcher.group(1)");
                String string12 = string2 = string11;
                if (string12 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                name2 = ((Object)StringsKt.trim((CharSequence)string12)).toString();
                if ((Intrinsics.areEqual((Object)name2, (Object)"") ^ 1) != 0) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name2);
                    ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name2 + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                    new Thread(new Runnable(name2){
                        public final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(32L - 57L + 31L - 9L + 10003L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
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
            String string13 = matcher2.group(1);
            Intrinsics.checkExpressionValueIsNotNull((Object)string13, (String)"matcher2.group(1)");
            String string14 = string2 = string13;
            if (string14 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name2 = ((Object)StringsKt.trim((CharSequence)string14)).toString();
            if ((Intrinsics.areEqual((Object)name2, (Object)"") ^ 1) == 0) return;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name2);
            ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name2 + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
            new Thread(new Runnable(name2){
                public final /* synthetic */ String $name;

                public final void run() {
                    try {
                        Thread.sleep(219L - 304L + 187L - 23L + 9921L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
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
        Pattern pattern = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!");
        ITextComponent iTextComponent = ((SPacketChat)packet).func_148915_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"packet.chatComponent");
        Matcher matcher = pattern.matcher(iTextComponent.func_150260_c());
        Pattern pattern4 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01");
        ITextComponent iTextComponent4 = ((SPacketChat)packet).func_148915_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent4, (String)"packet.chatComponent");
        Matcher matcher2 = pattern4.matcher(iTextComponent4.func_150260_c());
        if (matcher.find()) {
            String string15 = matcher.group(1);
            Intrinsics.checkExpressionValueIsNotNull((Object)string15, (String)"matcher.group(1)");
            String string16 = string = string15;
            if (string16 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name = ((Object)StringsKt.trim((CharSequence)string16)).toString();
            if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) != 0) {
                LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                new Thread(new Runnable(name){
                    public final /* synthetic */ String $name;

                    public final void run() {
                        try {
                            Thread.sleep(55L - 58L + 49L + 9954L);
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                            ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
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
        String string17 = matcher2.group(1);
        Intrinsics.checkExpressionValueIsNotNull((Object)string17, (String)"matcher2.group(1)");
        String string18 = string = string17;
        if (string18 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        name = ((Object)StringsKt.trim((CharSequence)string18)).toString();
        if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) == 0) return;
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
        ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + name + "\u00a7b\u88abWhale\u5403\u6389\u54af~Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
        new Thread(new Runnable(name){
            public final /* synthetic */ String $name;

            public final void run() {
                try {
                    Thread.sleep(89L - 105L + 61L + 9955L);
                    ClientUtils.displayChatMessage("\u00a7b\u00a7l[LiKingSense]\u00a7a" + this.$name + "\u00a7b\u88abWhale\u5410\u51fa\u6765\u54afCiallo(\u2220\u30fb\u03c9< )\u2312\u2606~");
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

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    public final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }
}

