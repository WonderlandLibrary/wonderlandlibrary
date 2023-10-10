package rosegoldclient.features;

import gg.essential.api.utils.Multithreading;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.Utils;

import java.util.ArrayList;

public class SpellCaster {

    public static boolean cancelServerSwing = false;
    public static ArrayList<Packet> packetList = new ArrayList<>();
    private static int totalTicks = 0;

    private static final CPacketPlayerTryUseItem use = new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND);
    private static final CPacketAnimation swing = new CPacketAnimation(EnumHand.MAIN_HAND);

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if(Main.mc.player == null || Main.mc.world == null || Main.mc.getConnection() == null) return;
        int eventKey = Keyboard.getEventKey();
        if(!Keyboard.isKeyDown(eventKey)) return;
        if(!isWynncraftWeapon()) return;
        if(packetList.size() != 0) return;
        boolean flip = shouldFlipClicks();
        if(eventKey == Main.keybinds.get(4).getKeyCode()) { //cast RRR
            RRR(flip);
            return;
        }
        if(eventKey == Main.keybinds.get(5).getKeyCode()) { //cast RLR
            RLR(flip);
            return;
        }
        if(eventKey == Main.keybinds.get(6).getKeyCode()) { //cast RLL
            RLL(flip);
            return;
        }
        if(eventKey == Main.keybinds.get(7).getKeyCode()) { //cast RRL
            RRL(flip);
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        totalTicks++;
        if(packetList.size() == 0) return;
        if(totalTicks % (Main.configFile.spellCastSpeed + 2) != 0) return;
        processPackets();
    }

    public static void processPackets() {
        assert Main.mc.getConnection() != null;
        if(packetList.get(0) == use) {
            if(AutoSneak.abilityReady) {
                Multithreading.runAsync(() -> {
                    try {
                        Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                        Thread.sleep(100);
                        Main.mc.getConnection().getNetworkManager().sendPacket(packetList.get(0));
                        Thread.sleep(100);
                        Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    } catch (Exception ignored) {
                    }
                });
            } else {
                Main.mc.getConnection().getNetworkManager().sendPacket(packetList.get(0));
            }
        } else {
            Main.mc.getConnection().getNetworkManager().sendPacket(packetList.get(0));
        }
        packetList.remove(0);
    }

    public static boolean isWynncraftWeapon() {
        ItemStack itemStack = Main.mc.player.getHeldItemMainhand();
        if(itemStack.getTagCompound() == null || itemStack.getTagCompound().hasNoTags()) return false;
        String nbt = itemStack.getTagCompound().toString();
        return nbt.contains("§a✔§7 Class Req:");
    }

    public static boolean shouldFlipClicks() {
        ItemStack itemStack = Main.mc.player.getHeldItemMainhand();
        if(isWynncraftWeapon()) {
            assert itemStack.getTagCompound() != null;
            String nbt = itemStack.getTagCompound().toString();
            return nbt.contains("Class Req: Archer");
        }
        return false;
    }

    public static void RRR(boolean flip) {
        if(flip) {
            packetList.add(swing);
            packetList.add(swing);
            packetList.add(swing);
        } else {
            packetList.add(use);
            packetList.add(use);
            packetList.add(use);
        }
    }

    public static void RRR() {
        RRR(shouldFlipClicks());
    }

    public static void RLR(boolean flip) {
        if(flip) {
            packetList.add(swing);
            packetList.add(use);
            packetList.add(swing);
        } else {
            packetList.add(use);
            packetList.add(swing);
            packetList.add(use);
        }
    }

    public static void RLR() {
        RLR(shouldFlipClicks());
    }

    public static void RLL(boolean flip) {
        if(flip) {
            packetList.add(swing);
            packetList.add(use);
            packetList.add(use);
        } else {
            packetList.add(use);
            packetList.add(swing);
            packetList.add(swing);
        }
    }

    public static void RLL() {
        RLL(shouldFlipClicks());
    }

    public static void RRL(boolean flip) {
        if(flip) {
            packetList.add(swing);
            packetList.add(swing);
            packetList.add(use);
        } else {
            packetList.add(use);
            packetList.add(use);
            packetList.add(swing);
        }
    }

    public static void RRL() {
        RRL(shouldFlipClicks());
    }

    public static void L(boolean flip) {
        if(flip) {
            packetList.add(use);
        } else {
            packetList.add(swing);
        }
    }

    public static void L() {
        L(shouldFlipClicks());
    }
}
