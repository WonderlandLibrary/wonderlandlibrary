/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HYTGetname", description="HuaYuTing GetName", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0003J\b\u0010\u0010\u001a\u00020\u000fH\u0017J\u0010\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0012\u0010\u0014\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0015H\u0007R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lme/report/liquidware/modules/fun/GetName;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "ground", "", "", "getGround", "()Ljava/util/List;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "playerName", "", "clearAll", "", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Companion", "KyinoClient"})
public final class GetName
extends Module {
    private final List<String> playerName = new ArrayList();
    @NotNull
    private final List<Integer> ground;
    @NotNull
    private final ListValue modeValue;
    public static final Companion Companion = new Companion(null);

    @NotNull
    public final List<Integer> getGround() {
        return this.ground;
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Entity entity;
        Packet<?> packetEntity;
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String name;
            Packet<?> chatMessage = packet;
            Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((S02PacketChat)chatMessage).func_148915_c().func_150260_c());
            Matcher matcher2 = Pattern.compile("> (.*?)\\(").matcher(((S02PacketChat)chatMessage).func_148915_c().func_150260_c());
            FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
            if (matcher.find() && Intrinsics.areEqual(name = matcher.group(1), "") ^ true && !this.playerName.contains(name)) {
                String string = name;
                Intrinsics.checkExpressionValueIsNotNull(string, "name");
                this.playerName.add(string);
                friendsConfig.addFriend(name);
                Minecraft minecraft = GetName.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a7lKyinoClient" + "\u00a77]\u00a7fDeleted HYT Bot:" + name);
                new Thread(new Runnable(this, name, friendsConfig){
                    final /* synthetic */ GetName this$0;
                    final /* synthetic */ String $name;
                    final /* synthetic */ FriendsConfig $friendsConfig;

                    public final void run() {
                        try {
                            Thread.sleep(6000L);
                            GetName.access$getPlayerName$p(this.this$0).remove(this.$name);
                            this.$friendsConfig.removeFriend(this.$name);
                            Minecraft minecraft = GetName.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                            minecraft.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
                            ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a7lKyinoClient" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    {
                        this.this$0 = getName;
                        this.$name = string;
                        this.$friendsConfig = friendsConfig;
                    }
                }).start();
            }
            if (matcher2.find() && Intrinsics.areEqual(name = matcher2.group(1), "") ^ true) {
                String string = name;
                Intrinsics.checkExpressionValueIsNotNull(string, "name");
                if (!StringsKt.contains$default((CharSequence)string, "[", false, 2, null) && !this.playerName.contains(name)) {
                    this.playerName.add(name);
                    friendsConfig.addFriend(name);
                    Minecraft minecraft = GetName.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
                    ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a7lKyinoClient" + "\u00a77]\u00a7fDeleted HYT Bot:" + name);
                    new Thread(new Runnable(this, name, friendsConfig){
                        final /* synthetic */ GetName this$0;
                        final /* synthetic */ String $name;
                        final /* synthetic */ FriendsConfig $friendsConfig;

                        public final void run() {
                            try {
                                Thread.sleep(6000L);
                                GetName.access$getPlayerName$p(this.this$0).remove(this.$name);
                                this.$friendsConfig.removeFriend(this.$name);
                                Minecraft minecraft = GetName.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                                minecraft.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
                                ClientUtils.displayChatMessage("\u00a77[\u00a78\u00a7lKyinoClient" + "\u00a77]\u00a7fDeleted HYT Bot:" + this.$name);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.this$0 = getName;
                            this.$name = string;
                            this.$friendsConfig = friendsConfig;
                        }
                    }).start();
                }
            }
        }
        if (Intrinsics.areEqual((String)this.modeValue.get(), "4V4/1V1") && packet instanceof S14PacketEntity) {
            packetEntity = packet;
            S14PacketEntity s14PacketEntity = (S14PacketEntity)packetEntity;
            WorldClient worldClient = GetName.access$getMc$p$s1046033730().field_71441_e;
            if (worldClient == null) {
                Intrinsics.throwNpe();
            }
            if ((entity = s14PacketEntity.func_149065_a((World)worldClient)) instanceof EntityPlayer && ((S14PacketEntity)packetEntity).func_179742_g() && !this.ground.contains(((EntityPlayer)entity).func_145782_y())) {
                this.ground.add(((EntityPlayer)entity).func_145782_y());
            }
        }
        if (Intrinsics.areEqual((String)this.modeValue.get(), "32V32/64V64") && packet instanceof S14PacketEntity) {
            packetEntity = packet;
            S14PacketEntity s14PacketEntity = (S14PacketEntity)packetEntity;
            WorldClient worldClient = GetName.access$getMc$p$s1046033730().field_71441_e;
            if (worldClient == null) {
                Intrinsics.throwNpe();
            }
            if ((entity = s14PacketEntity.func_149065_a((World)worldClient)) instanceof EntityPlayer && ((S14PacketEntity)packetEntity).func_179742_g() && !this.ground.contains(((EntityPlayer)entity).func_145782_y())) {
                this.ground.add(((EntityPlayer)entity).func_145782_y());
            }
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    private final void clearAll() {
        this.playerName.clear();
    }

    public GetName() {
        List list;
        GetName getName = this;
        boolean bl = false;
        getName.ground = list = (List)new ArrayList();
        this.modeValue = new ListValue("Mode", new String[]{"4V4/1V1", "32V32/64V64"}, "4V4/1V1");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ List access$getPlayerName$p(GetName $this) {
        return $this.playerName;
    }

    @JvmStatic
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public static final boolean isBot(@NotNull EntityLivingBase entity) {
        return Companion.isBot(entity);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lme/report/liquidware/modules/fun/GetName$Companion;", "", "()V", "isBot", "", "entity", "Lnet/minecraft/entity/EntityLivingBase;", "KyinoClient"})
    public static final class Companion {
        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @JvmStatic
        @NativeMethod.Obfuscation(flags="+native,+tiger-black")
        public final boolean isBot(@NotNull EntityLivingBase entity) {
            Intrinsics.checkParameterIsNotNull(entity, "entity");
            if (!(entity instanceof EntityPlayer)) {
                return false;
            }
            GetName antiBot = (GetName)LiquidBounce.INSTANCE.getModuleManager().getModule(GetName.class);
            if (antiBot == null) return false;
            if (!antiBot.getState()) {
                return false;
            }
            if (Intrinsics.areEqual((String)antiBot.getModeValue().get(), "4V4/1V1") && !antiBot.getGround().contains(((EntityPlayer)entity).func_145782_y())) {
                return true;
            }
            if (Intrinsics.areEqual((String)antiBot.getModeValue().get(), "4V4/1V1") && entity.field_70173_aa < 18) {
                return true;
            }
            if (Intrinsics.areEqual((String)antiBot.getModeValue().get(), "32V32/64V64") && !antiBot.getGround().contains(((EntityPlayer)entity).func_145782_y())) {
                return true;
            }
            if (Intrinsics.areEqual((String)antiBot.getModeValue().get(), "4V4/1V1")) {
                EntityLivingBase player = entity;
                if (((EntityPlayer)player).field_71071_by.field_70460_b[0] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[1] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[2] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[3] == null) {
                    return true;
                }
            }
            String string = ((EntityPlayer)entity).func_70005_c_();
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
            String string2 = ((EntityPlayer)entity).func_70005_c_();
            EntityPlayerSP entityPlayerSP = GetName.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!Intrinsics.areEqual(string2, entityPlayerSP.func_70005_c_())) return false;
            return true;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

