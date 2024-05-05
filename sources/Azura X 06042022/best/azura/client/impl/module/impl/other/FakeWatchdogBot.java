package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventFastTick;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;

import java.util.UUID;

@ModuleInfo(name = "Fake Watchdog Bot", description = "Spawns a fake Watchdog aura bot that spins around you", category = Category.OTHER)
public class FakeWatchdogBot extends Module {

    private EntityOtherPlayerMP bot;
    private long botSpawnTime, botDeathTime;
    private double lastExisted;

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (e.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity c02 = e.getPacket();
            if (c02.getAction() == C02PacketUseEntity.Action.ATTACK && Math.random() < 0.2 &&
                    System.currentTimeMillis() - this.botDeathTime > 3000 && this.bot == null) {
                this.bot = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.randomUUID(), this.randomString()));
                this.botSpawnTime = System.currentTimeMillis();
                this.bot.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ);
                mc.theWorld.addEntityToWorld(-5, this.bot);
                ChatUtil.sendChat("Spawned random watchdog bot");
                this.bot.setLocationSkin(this.mc.thePlayer.getLocationSkin());
                byte i = 0;
                for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                    i |= enumplayermodelparts.getPartMask();
                }
                this.bot.getDataWatcher().setObjectWatched(10);
                this.bot.getDataWatcher().updateObject(10, i);
                this.lastExisted = this.bot.ticksExisted;
                final Item[] helmets = {Items.leather_helmet, Items.chainmail_helmet, Items.golden_helmet, Items.iron_helmet, null},
                        chestplates = {Items.leather_chestplate, Items.chainmail_chestplate, Items.golden_chestplate, Items.iron_chestplate, null},
                        leggings = {Items.leather_leggings, Items.chainmail_leggings, Items.golden_leggings, Items.iron_leggings, null},
                        boots = {Items.leather_boots, Items.chainmail_boots, Items.golden_boots, Items.iron_boots, null};
                final int helmetIndex = MathUtil.getRandom_int(0, helmets.length),
                        chestplateIndex = MathUtil.getRandom_int(0, chestplates.length),
                        leggingsIndex = MathUtil.getRandom_int(0, leggings.length),
                        bootsIndex = MathUtil.getRandom_int(0, boots.length);
                this.bot.inventory.armorInventory[0] = boots[bootsIndex] == null ? null : new ItemStack(boots[bootsIndex]);
                this.bot.inventory.armorInventory[1] = leggings[leggingsIndex] == null ? null : new ItemStack(leggings[leggingsIndex]);
                this.bot.inventory.armorInventory[2] = chestplates[chestplateIndex] == null ? null : new ItemStack(chestplates[chestplateIndex]);
                this.bot.inventory.armorInventory[3] = helmets[helmetIndex] == null ? null : new ItemStack(helmets[helmetIndex]);
                this.bot.rotationYaw = MathUtil.getRandom_int(-180, 180);
                this.bot.rotationPitch = MathUtil.getRandom_int(-45, 45);
            }
        }
    };

    @EventHandler
    public final Listener<EventFastTick> eventFastTickListener = e -> {
        if (this.bot != null) {
            this.lastExisted += 0.1;
        }
    };

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        if (this.bot != null && System.currentTimeMillis() - this.botSpawnTime > 5000) {
            this.bot = null;
            this.botDeathTime = System.currentTimeMillis();
            mc.theWorld.removeEntityFromWorld(-5);
        }
        if (this.bot != null) {
            final double oldX = mc.thePlayer.posX, oldY = mc.thePlayer.posY, oldZ = mc.thePlayer.posZ;
            this.bot.setPositionAndRotation2(oldX - Math.sin(this.lastExisted) * 2.0, oldY + 0.5, oldZ + Math.cos(this.lastExisted) * 2.0, this.bot.rotationYaw, this.bot.rotationPitch, 3, true);
            this.bot.rotationYawHead = this.bot.prevRotationYaw + (this.bot.rotationYaw - this.bot.prevRotationYaw);
            if (this.bot.ticksExisted % 8 == 0)
                this.bot.swingItem();
        }
    };

    private String randomString() {
        final char[] abc123 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        final StringBuilder builder = new StringBuilder("§c");
        for (int i = 0; i < MathUtil.getRandom_int(5, 16); i++)
            builder.append(abc123[MathUtil.getRandom_int(0, abc123.length)]);
        return builder.toString();
    }

}