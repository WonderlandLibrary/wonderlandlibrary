package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.properties.*;
import cow.milkgod.cheese.module.*;
import cow.milkgod.cheese.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.potion.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import cow.milkgod.cheese.commands.*;

public class Criticals extends Module
{
    public static Property<Boolean> oldMode;
    public static Property<Boolean> newMode;
    private static boolean next;
    public static boolean attacking;
    
    public Criticals() {
        super("Criticals", 25, Category.COMBAT, 10712391, true, "Always critical.", new String[] { "crits", "crts", "crit" });
        Criticals.oldMode = new Property<Boolean>(this, "oldMode", true);
        Criticals.newMode = new Property<Boolean>(this, "newMode", false);
    }
    
    @EventTarget
    public void onPacketSent(final EventPacketSent e) {
        if (!Criticals.oldMode.value) {
            return;
        }
        if (e.getPacket() instanceof C03PacketPlayer) {
            ((C03PacketPlayer)e.getPacket()).field_149480_h = false;
            ((C03PacketPlayer)e.getPacket()).field_149474_g = false;
            if (Wrapper.isOnLiquid()) {
                return;
            }
            Cheese.getInstance();
            if (!Cheese.moduleManager.getModState("Glide")) {
                Cheese.getInstance();
                if (!Cheese.commandManager.getCommandbyName("Damage").getState()) {
                    Cheese.getInstance();
                    if (!Cheese.moduleManager.getModState("Phase")) {
                        if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                            final C03PacketPlayer.C04PacketPlayerPosition var6 = (C03PacketPlayer.C04PacketPlayerPosition)e.getPacket();
                            var6.field_149474_g = false;
                        }
                        else if (e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
                            final C03PacketPlayer.C05PacketPlayerLook var7 = (C03PacketPlayer.C05PacketPlayerLook)e.getPacket();
                            var7.field_149474_g = false;
                        }
                        else if (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                            final C03PacketPlayer.C06PacketPlayerPosLook var8 = (C03PacketPlayer.C06PacketPlayerPosLook)e.getPacket();
                            var8.field_149474_g = false;
                        }
                    }
                }
            }
            ((C03PacketPlayer)e.getPacket()).field_149480_h = false;
            ((C03PacketPlayer)e.getPacket()).field_149474_g = false;
        }
    }
    
    @Override
    public void toggleModule() {
        super.toggleModule();
        if (!this.getState()) {
            Criticals.next = false;
        }
    }
    
    @EventTarget
    public void onPreAttack(final EventPlayerAttack e) {
        System.out.println("Criticalseatapples");
        doCrits();
        Cheese.getInstance();
        if (!Cheese.moduleManager.getModState("KillAura") || Killaura.attackList.isEmpty()) {
            System.out.println("Criticalseatapples");
            doCrits();
        }
    }
    
    public static void attack(final Entity ent, final boolean crit) {
        Wrapper.mc.thePlayer.swingItem();
        if (crit) {
            doCrits();
        }
        else {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
        }
        final float sharpLevel = EnchantmentHelper.func_152377_a(Wrapper.mc.thePlayer.getHeldItem(), ((EntityLivingBase)ent).getCreatureAttribute());
        final boolean vanillaCrit = Wrapper.mc.thePlayer.fallDistance > 0.0f && !Wrapper.mc.thePlayer.onGround && !Wrapper.mc.thePlayer.isOnLadder() && !Wrapper.mc.thePlayer.isInWater() && !Wrapper.mc.thePlayer.isPotionActive(Potion.blindness) && Wrapper.mc.thePlayer.ridingEntity == null;
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Wrapper.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            Wrapper.mc.thePlayer.onEnchantmentCritical(ent);
        }
    }
    
    public static void doCrits() {
        Cheese.getInstance();
        if (!Cheese.moduleManager.getModState("Criticals") || !Criticals.newMode.value) {
            return;
        }
        if (Criticals.next = !Criticals.next) {
            Criticals.attacking = true;
            final double posY = Wrapper.mc.thePlayer.posY;
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY + 0.0625, Wrapper.mc.thePlayer.posZ, true));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY, Wrapper.mc.thePlayer.posZ, false));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY + 1.1E-5, Wrapper.mc.thePlayer.posZ, false));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY, Wrapper.mc.thePlayer.posZ, false));
            Criticals.attacking = false;
        }
    }
    
    public boolean isFalling() {
        return !Wrapper.mc.thePlayer.onGround && (Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, 1.1).getMaterial() == Material.air || Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, 1.1) instanceof BlockStairs || Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, 1.1) instanceof BlockSlab);
    }
    
    @Override
    protected void addCommand() {
        Cheese.getInstance();
        final CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Criticals", "Unknown Option! ", new String[] { "crits" }, "<old | new>", "Manages Criticals") {
            @EventTarget
            public void onTick(final EventTick ev) {
                Label_0236: {
                    try {
                        final String mode = EventChatSend.getMessage().split(" ")[1];
                        Label_0172: {
                            Label_0144: {
                                Label_0116: {
                                    final String s;
                                    final String s2;
                                    switch (s2 = (s = mode)) {
                                        case "New": {
                                            break Label_0116;
                                        }
                                        case "Old": {
                                            break Label_0144;
                                        }
                                        case "new": {
                                            break Label_0116;
                                        }
                                        case "old": {
                                            break Label_0144;
                                        }
                                        default:
                                            break;
                                    }
                                    break Label_0172;
                                }
                                Criticals.newMode.value = true;
                                Criticals.oldMode.value = false;
                                Logger.logChat("Set Criticals mode to §eNew");
                                break Label_0236;
                            }
                            Criticals.newMode.value = false;
                            Criticals.oldMode.value = true;
                            Logger.logChat("Set Criticals mode to §eOld");
                            break Label_0236;
                        }
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                    catch (Exception ex) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                this.Toggle();
            }
        });
    }
}
