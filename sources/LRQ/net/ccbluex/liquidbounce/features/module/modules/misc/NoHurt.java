/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoHurt", description="faq", category=ModuleCategory.HYT)
public final class NoHurt
extends Module {
    private final List<String> playerName = new ArrayList();
    public static final Companion Companion = new Companion(null);

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketChat(packet)) {
            String name;
            ISPacketChat chatMessage = packet.asSPacketChat();
            Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(chatMessage.getGetChat().func_150260_c());
            Matcher matcher2 = Pattern.compile("> (.*?)\\(").matcher(chatMessage.getGetChat().func_150260_c());
            if (matcher.find() && (name = matcher.group(1)).equals("") ^ true && !this.playerName.contains(name)) {
                this.playerName.add(name);
                new Thread(new Runnable(this, name){
                    final /* synthetic */ NoHurt this$0;
                    final /* synthetic */ String $name;

                    public final void run() {
                        try {
                            Thread.sleep(6000L);
                            NoHurt.access$getPlayerName$p(this.this$0).remove(this.$name);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    {
                        this.this$0 = noHurt;
                        this.$name = string;
                    }
                }).start();
            }
            if (matcher2.find() && (name = matcher2.group(1)).equals("") ^ true && !name.equals("[") && !this.playerName.contains(name)) {
                new Thread(new Runnable(this, name){
                    final /* synthetic */ NoHurt this$0;
                    final /* synthetic */ String $name;

                    public final void run() {
                        try {
                            Thread.sleep(6000L);
                            NoHurt.access$getPlayerName$p(this.this$0).remove(this.$name);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    {
                        this.this$0 = noHurt;
                        this.$name = string;
                    }
                }).start();
            }
        }
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    private final void clearAll() {
        this.playerName.clear();
    }

    @JvmStatic
    public static final boolean isBot(IEntityLivingBase entity) {
        return Companion.isBot(entity);
    }

    public static final class Companion {
        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @JvmStatic
        public final boolean isBot(IEntityLivingBase entity) {
            if (!MinecraftInstance.classProvider.isEntityPlayer(entity)) {
                return false;
            }
            NoHurt antiBot = (NoHurt)LiquidBounce.INSTANCE.getModuleManager().getModule(NoHurt.class);
            if (antiBot == null) return false;
            if (!antiBot.getState()) {
                return false;
            }
            if (CollectionsKt.contains((Iterable)antiBot.playerName, (Object)entity.getName())) {
                return true;
            }
            String string = entity.getName();
            if (string == null) {
                Intrinsics.throwNpe();
            }
            CharSequence charSequence = string;
            boolean bl = false;
            if (charSequence.length() == 0) {
                return true;
            }
            boolean bl2 = false;
            if (bl2) return true;
            String string2 = entity.getName();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!string2.equals(iEntityPlayerSP.getName())) return false;
            return true;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

