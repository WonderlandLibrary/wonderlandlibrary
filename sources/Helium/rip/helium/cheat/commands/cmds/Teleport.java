package rip.helium.cheat.commands.cmds;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.RunTickEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.Stopwatch;

public class Teleport extends Command {

    /*/
    Credit to oHare for making this.
     */

    private int x, y, z;
    private boolean gotonigga, niggay;
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean packet = true;
    private int moveUnder;
    private Stopwatch timerUtility = new Stopwatch();


    public Teleport() {
        super("tp", "teleport");
    }

    @Override
    public void run(String[] args) {
        switch (args.length) {
            case 2:
                if (args[1].toLowerCase().equals("stop")) {
                    if (gotonigga) {
                        stopTP();
                        ChatUtil.chat("Stopped.");
                    } else {
                        ChatUtil.chat("Not running.");
                    }
                    break;
                }
                if (args[1].toLowerCase().equals("help")) {
                    ChatUtil.chat(".teleport stop/packet/xz/xyz/waypointname/playername/factionname");
                    break;
                }
                if (args[1].toLowerCase().equals("packet")) {
                    packet ^= true;
                    ChatUtil.chat("Packet set to " + packet);
                    break;
                }
                if (gotonigga) {
                    ChatUtil.chat("Already going.");
                    break;
                }

                for (EntityPlayer e : mc.theWorld.playerEntities) {
                    if (e.getName().toLowerCase().equals(args[1].toLowerCase())) {
                        startTP(MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posY), MathHelper.floor_double(e.posZ), true);
                        return;
                    }
                }
                Helium.eventBus.register(this);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C01PacketChatMessage("/f who " + args[1]));
                break;
            case 3:
                if (gotonigga) {
                    ChatUtil.chat("Already going.");
                    break;
                }
                if (NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2])) {
                    if (!isUnderBlock() || packet) {
                        startTP(Integer.parseInt(args[1]), 255, Integer.parseInt(args[2]), true);
                    } else {
                        ChatUtil.chat("You are under a block!");
                    }
                } else {
                    ChatUtil.chat("Invalid arguments.");
                }
                break;
            case 4:
                if (gotonigga) {
                    ChatUtil.chat("Already going.");
                    break;
                }
                if (NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2]) && NumberUtils.isNumber(args[3])) {
                    if (!isUnderBlock() || packet) {
                        startTP(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), true);
                    } else {
                        ChatUtil.chat("You are under a block!");
                    }
                } else {
                    ChatUtil.chat("Invalid arguments.");
                }
                break;
            default:
                ChatUtil.chat("Invalid arguments.");
                break;
        }
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent event) {
        if (gotonigga && !packet) {
            final float storedangles = getRotationFromPosition(x, z);
            final double distancex = -4 * Math.sin(storedangles);
            final double distancez = 4 * Math.cos(storedangles);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                if (mc.thePlayer.posY < 250) {
                    mc.thePlayer.motionY = 5;
                } else {
                    mc.thePlayer.motionY = 0;
                    niggay = true;
                }
                if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) >= 32) {
                    if (niggay) {
                        mc.thePlayer.motionX = distancex;
                        mc.thePlayer.motionZ = distancez;
                    }
                } else {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    ChatUtil.chat("Finished you have arrived at x:" + (int) mc.thePlayer.posX + " z:" + (int) mc.thePlayer.posZ);
                    gotonigga = false;
                    niggay = false;
                    mc.renderGlobal.loadRenderers();
                    Helium.eventBus.unregister(this);
                }
            }
        }
    }

    @Collect
    public void onPacket(SendPacketEvent event) {
        if (packet) {
            if (gotonigga) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                    if (!niggay) {
                        packet.setY(y);
                        packet.setX(x);
                        packet.setZ(z);
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                        mc.thePlayer.setPosition(x,y,z);
                        niggay = true;
                        moveUnder = 2;
                    }
                }
                if (timerUtility.hasPassed(500)) {
                    ChatUtil.chat("Finished you have arrived at x:" + x + " z:" + z);
                    gotonigga = false;
                    niggay = false;
                    mc.renderGlobal.loadRenderers();
                    Helium.eventBus.unregister(this);
                    timerUtility.reset();
                }
            }
        } else {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                String text = packet.getChatComponent().getUnformattedText();
                if (text.contains("You cannot go past the border.")) {
                    event.setCancelled(true);
                }
                if (text.contains("Home: ")) {
                    if (text.contains("Not set")) {
                        stopTP();
                        ChatUtil.chat("Player or faction found but f home was not set.");
                        return;
                    }
                    try {
                        int x = Integer.parseInt(StringUtils.substringBetween(text, "Home: ", ", "));
                        int z = Integer.parseInt(text.split(", ")[1]);
                        startTP(x, 255, z, false);
                    } catch (Exception e) {
                        stopTP();
                    }
                } else {
                    if (text.contains(" not found.")) {
                        stopTP();
                        ChatUtil.chat("Player or faction not found.");
                    }
                }
            }
        }
    }

    @Collect
    public void onTick(RunTickEvent event) {
        if (mc.thePlayer != null && moveUnder == 1 && packet) {
            if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) > 1) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, y, Double.NEGATIVE_INFINITY, true));
                moveUnder = 0;
            }
        }
    }

    private void startTP(final int x, final int y, final int z, boolean register) {
        if (gotonigga) {
            ChatUtil.chat("Already active!");
            return;
        }
        this.x = x;
        this.y = y;
        this.z = z;
        gotonigga = true;
        ChatUtil.chat("Teleporting to x:" + x + " y:" + y + " z:" + z + ".");
        if (register) {
            Helium.eventBus.register(this);
        }
        timerUtility.reset();
    }

    private void stopTP() {
        x = y = z = 0;
        gotonigga = false;
        niggay = false;
        Helium.eventBus.unregister(this);
    }

    private boolean isUnderBlock() {
        for (int i = (int) (Minecraft.getMinecraft().thePlayer.posY + 2); i < 255; ++i) {
            BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ);
            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockFenceGate || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockSign || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockButton)
                continue;
            return true;
        }
        return false;
    }

    private float getRotationFromPosition(final double x, final double z) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final float yaw = (float) Math.atan2(zDiff, xDiff) - 1.57079632679f;
        return yaw;
    }
}