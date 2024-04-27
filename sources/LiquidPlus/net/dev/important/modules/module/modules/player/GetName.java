/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.file.configs.FriendsConfig;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Info(name="GetName", spacedName="Get Name", description="?", category=Category.MISC, cnName="\u53cd\u590d\u6d3b\u65e0\u654c")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0016J\u0010\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0012\u0010\u0014\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0015H\u0007R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/dev/important/modules/module/modules/player/GetName;", "Lnet/dev/important/modules/module/Module;", "()V", "ground", "", "", "getGround", "()Ljava/util/List;", "modeValue", "Lnet/dev/important/value/ListValue;", "getModeValue", "()Lnet/dev/important/value/ListValue;", "playerName", "", "clearAll", "", "onDisable", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "Companion", "LiquidBounce"})
public final class GetName
extends Module {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final List<String> playerName = new ArrayList();
    @NotNull
    private final List<Integer> ground = new ArrayList();
    @NotNull
    private final ListValue modeValue;

    public GetName() {
        String[] stringArray = new String[]{"4V4/1V1", "32V32/64V64"};
        this.modeValue = new ListValue("Mode", stringArray, "4V4/1V1");
    }

    @NotNull
    public final List<Integer> getGround() {
        return this.ground;
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Entity entity;
        Packet<?> packetEntity;
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String name;
            Packet<?> chatMessage = packet;
            Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(((S02PacketChat)chatMessage).func_148915_c().func_150260_c());
            Matcher matcher2 = Pattern.compile("> (.*?)\\(").matcher(((S02PacketChat)chatMessage).func_148915_c().func_150260_c());
            FriendsConfig friendsConfig = Client.INSTANCE.getFileManager().friendsConfig;
            if (matcher.find() && !Intrinsics.areEqual(name = matcher.group(1), "") && !this.playerName.contains(name)) {
                Intrinsics.checkNotNullExpressionValue(name, "name");
                this.playerName.add(name);
                friendsConfig.addFriend(name);
                new Thread(() -> GetName.onPacket$lambda-0(this, name, friendsConfig)).start();
            }
            if (matcher2.find() && !Intrinsics.areEqual(name = matcher2.group(1), "")) {
                Intrinsics.checkNotNullExpressionValue(name, "name");
                if (!StringsKt.contains$default((CharSequence)name, "[", false, 2, null) && !this.playerName.contains(name)) {
                    this.playerName.add(name);
                    friendsConfig.addFriend(name);
                    new Thread(() -> GetName.onPacket$lambda-1(this, name, friendsConfig)).start();
                }
            }
        }
        if (Intrinsics.areEqual(this.modeValue.get(), "4V4/1V1") && packet instanceof S14PacketEntity) {
            packetEntity = packet;
            S14PacketEntity s14PacketEntity = (S14PacketEntity)packetEntity;
            WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
            Intrinsics.checkNotNull(worldClient);
            entity = s14PacketEntity.func_149065_a((World)worldClient);
            if (entity instanceof EntityPlayer && ((S14PacketEntity)packetEntity).func_179742_g() && !this.ground.contains(((EntityPlayer)entity).func_145782_y())) {
                this.ground.add(((EntityPlayer)entity).func_145782_y());
            }
        }
        if (Intrinsics.areEqual(this.modeValue.get(), "32V32/64V64") && packet instanceof S14PacketEntity) {
            packetEntity = packet;
            S14PacketEntity s14PacketEntity = (S14PacketEntity)packetEntity;
            WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
            Intrinsics.checkNotNull(worldClient);
            entity = s14PacketEntity.func_149065_a((World)worldClient);
            if (entity instanceof EntityPlayer && ((S14PacketEntity)packetEntity).func_179742_g() && !this.ground.contains(((EntityPlayer)entity).func_145782_y())) {
                this.ground.add(((EntityPlayer)entity).func_145782_y());
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

    private static final void onPacket$lambda-0(GetName this$0, String $name, FriendsConfig $friendsConfig) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        try {
            Thread.sleep(6000L);
            this$0.playerName.remove($name);
            $friendsConfig.removeFriend($name);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static final void onPacket$lambda-1(GetName this$0, String $name, FriendsConfig $friendsConfig) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        try {
            Thread.sleep(6000L);
            this$0.playerName.remove($name);
            $friendsConfig.removeFriend($name);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @JvmStatic
    public static final boolean isBot(@NotNull EntityLivingBase entity) {
        return Companion.isBot(entity);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/player/GetName$Companion;", "", "()V", "isBot", "", "entity", "Lnet/minecraft/entity/EntityLivingBase;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @JvmStatic
        public final boolean isBot(@NotNull EntityLivingBase entity) {
            Intrinsics.checkNotNullParameter(entity, "entity");
            if (!(entity instanceof EntityPlayer)) {
                return false;
            }
            GetName antiBot = (GetName)Client.INSTANCE.getModuleManager().getModule(GetName.class);
            if (antiBot == null) return false;
            if (!antiBot.getState()) {
                return false;
            }
            if (Intrinsics.areEqual(antiBot.getModeValue().get(), "4V4/1V1") && !antiBot.getGround().contains(((EntityPlayer)entity).func_145782_y())) {
                return true;
            }
            if (Intrinsics.areEqual(antiBot.getModeValue().get(), "4V4/1V1") && entity.field_70173_aa < 18) {
                return true;
            }
            if (Intrinsics.areEqual(antiBot.getModeValue().get(), "32V32/64V64") && !antiBot.getGround().contains(((EntityPlayer)entity).func_145782_y())) {
                return true;
            }
            if (Intrinsics.areEqual(antiBot.getModeValue().get(), "4V4/1V1")) {
                EntityLivingBase player = entity;
                if (((EntityPlayer)player).field_71071_by.field_70460_b[0] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[1] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[2] == null && ((EntityPlayer)player).field_71071_by.field_70460_b[3] == null) {
                    return true;
                }
            }
            String string = ((EntityPlayer)entity).func_70005_c_();
            Intrinsics.checkNotNull(string);
            if (((CharSequence)string).length() == 0) {
                return true;
            }
            boolean bl = false;
            if (bl) return true;
            String string2 = ((EntityPlayer)entity).func_70005_c_();
            EntityPlayerSP entityPlayerSP = GetName.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            if (!Intrinsics.areEqual(string2, entityPlayerSP.func_70005_c_())) return false;
            return true;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

