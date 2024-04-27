package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import java.util.*;

@ModuleInfo(name = "Disabler", description = "test", category = ModuleCategory.MISC)
public class Disabler extends Module {
    private final IntegerValue c0fBaseDelay = new IntegerValue("C0F-Base", 1500, 100, 2500);
    private final IntegerValue c0fRandomDelay = new IntegerValue("C0F-Random", 50, 0, 1000);
    private final IntegerValue c00BaseDelay = new IntegerValue("C00-Base", 750, 100, 2500);
    private final IntegerValue c00RandomDelay = new IntegerValue("C00-Random", 20, 0, 1000);

    private final Queue<C0FPacketConfirmTransaction> c0fList = new LinkedList<>();
    private final Queue<C00PacketKeepAlive> c00List = new LinkedList<>();
    //budget but maybe its working

    private final MSTimer timer = new MSTimer(), timer1 = new MSTimer();
    public boolean abc, working;
    private int keepAliveId, delay, all;

    private final Random random = new Random();

    @Override
    public void onDisable() {
        super.onDisable();
        abc = working = false;
        c0fList.clear();
        c00List.clear();
        while (!c0fList.isEmpty())
            mc.thePlayer.sendQueue.addToSendQueue(c0fList.poll());

        while (!c00List.isEmpty())
            mc.thePlayer.sendQueue.addToSendQueue(c00List.poll());
    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (mc.isSingleplayer())
            working = true;

        if (mc.thePlayer.ticksExisted <= 1) {
            abc = working = false;
            c0fList.clear();
            c00List.clear();
            all = 0;
        }

        if (abc && timer.hasTimePassed(delay)) {
            int count = 0, total = (delay % 2 == 0 || delay % 3 == 0) && c0fList.size() > 100 ? c0fList.size() : MathHelper.getRandomIntegerInRange(random, 3, 9);
            synchronized (c0fList) {
                while (!c0fList.isEmpty() && count < total) {
                    C0FPacketConfirmTransaction packet = c0fList.peek();
                    if (packet == null)
                        break;
                    mc.getNetHandler().getNetworkManager().sendPacket(packet);
                    c0fList.remove(packet);
                    count++;
                }
            }

            C00PacketKeepAlive packetKeepAlive = new C00PacketKeepAlive(keepAliveId++);
            synchronized (c00List)  {
                c00List.add(packetKeepAlive);
                mc.getNetHandler().getNetworkManager().sendPacket(packetKeepAlive);
                c00List.remove(packetKeepAlive);
            }

            all += count;
            if (!working) {
                working = true;
                mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidSense.CLIENT_NAME+ "§8] §3"+ "Watchdoge left the game!"));
            }
            delay = MathHelper.getRandomIntegerInRange(random, c0fBaseDelay.get(), c0fBaseDelay.get() + c0fRandomDelay.get());
            timer.reset();
        }

        if (timer1.hasTimePassed(MathHelper.getRandomIntegerInRange(random, c00BaseDelay.get(), c00BaseDelay.get() + c00RandomDelay.get()))) {
            synchronized (c00List) {
                C00PacketKeepAlive packet = c00List.peek();
                if (packet != null) {
                    if (!abc)
                        keepAliveId = packet.getKey();
                    mc.getNetHandler().getNetworkManager().sendPacket(packet);
                    c00List.remove(packet);
                    timer1.reset();
                }
            }
        }

        if (abc && mc.thePlayer.ticksExisted % 155 == 0)
            mc.getNetHandler().getNetworkManager().sendPacket(new C14PacketTabComplete("/" + MathHelper.getRandomDoubleInRange(random, 10e19, 10e20)));
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C00PacketKeepAlive && !c00List.contains(e.getPacket())) {
            c00List.add((C00PacketKeepAlive) e.getPacket());
            e.cancelEvent();
        }

        if (e.getPacket() instanceof C0FPacketConfirmTransaction && !c0fList.contains(e.getPacket())) {
            int count = ("" + Math.abs(((C0FPacketConfirmTransaction) e.getPacket()).getUid())).length();
            if (!abc && count == 3) {
                abc = true;
                c0fList.clear();
                timer.reset();
                c0fList.add((C0FPacketConfirmTransaction) e.getPacket());
                e.cancelEvent();
            } else if (abc) {
                if (all < 10 && count > 3) {
                    while (!c0fList.isEmpty())
                        mc.getNetHandler().getNetworkManager().sendPacket(c0fList.poll());
                    timer.reset();
                    return;
                }
                c0fList.add((C0FPacketConfirmTransaction) e.getPacket());
                e.cancelEvent();
            }
        }
    }
}
