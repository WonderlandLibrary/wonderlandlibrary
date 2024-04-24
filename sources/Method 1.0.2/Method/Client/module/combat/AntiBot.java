package Method.Client.module.combat;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static Method.Client.Main.setmgr;

public class AntiBot extends Module {


    public Setting level = setmgr.add(new Setting("level", this, 0, 0, 6, false));
    public Setting tick = setmgr.add(new Setting("tick", this, 0, 0, 999, true));

    public Setting ifInAir = setmgr.add(new Setting("InAir", this, false));
    public Setting ifGround = setmgr.add(new Setting("OnGround", this, false));
    public Setting ifZeroHealth = setmgr.add(new Setting("ZeroHealth", this, false));
    public Setting ifInvisible = setmgr.add(new Setting("Invisible", this, false));
    public Setting ifEntityId = setmgr.add(new Setting("EntityId", this, false));
    public Setting ifTabName = setmgr.add(new Setting("OutTabName", this, false));
    public Setting ifPing = setmgr.add(new Setting("PingCheck", this, false));
    public Setting remove = setmgr.add(new Setting("RemoveBots", this, false));
    public Setting Friend = setmgr.add(new Setting("Friend", this, true));
    public Setting gwen = setmgr.add(new Setting("Gwen", this, false));
    public Setting canEntityBeSeen = setmgr.add(new Setting("canEntityBeSeen", this, false));

    public AntiBot() {
        super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, "Does not hit bots");
    }

    @Override
    public void onEnable() {
        bots.clear();
        super.onEnable();
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (gwen.getValBoolean() && packet instanceof SPacketSpawnPlayer) {
            SPacketSpawnPlayer spawn = (SPacketSpawnPlayer) packet;
            double posX = spawn.getX() / 32.0D;
            double posY = spawn.getY() / 32.0D;
            double posZ = spawn.getZ() / 32.0D;

            double dist = Math.sqrt(Math.pow(mc.player.posX - posX, 2) + Math.pow(mc.player.posY - posY, 2) + Math.pow(mc.player.posZ - posZ, 2));
            return dist > 17.0 || posX == mc.player.posX || posY == mc.player.posY || posZ == mc.player.posZ;
        }
        return true;
    }

    public static ArrayList<EntityBot> bots = new ArrayList<>();

    @Subscribe
    public void onClientTick(ClientTickEvent event) {
        for (Object object : mc.world.loadedEntityList) {
            if (object instanceof EntityLivingBase && !(object instanceof EntityPlayerSP)) {
                if (!isBotBase((EntityLivingBase) object)) {
                    if (level.getValDouble() > 0.0 && botPercentage((EntityLivingBase) object) > level.getValDouble())
                        addBot((EntityLivingBase) object);
                    else if (level.getValDouble() < 1 && botPercentage((EntityLivingBase) object) >= 1)
                        addBot((EntityLivingBase) object);
                } else {
                    addBot((EntityLivingBase) object);
                    if (remove.getValBoolean())
                        mc.world.removeEntity((EntityLivingBase) object);
                }
            }
        }
    }

    void addBot(EntityLivingBase entity) {
        if (Friend.getValBoolean() && ValidUtils.isFriendEnemy(entity))
            return;
        if (!isBot(entity)) {
            bots.add(new EntityBot(entity));
        }
    }

    public static boolean isBot(EntityLivingBase livingBase) {
        for (EntityBot bot : bots) {
            if (bot.getName().equals((livingBase.getName()))) {
                if (livingBase.isInvisible() != bot.isInvisible())
                    return livingBase.isInvisible();
                return true;
            } else {
                if (bot.getId() == livingBase.getEntityId() || bot.getUuid().equals(livingBase.getUniqueID())) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isBotTabName(EntityLivingBase bot) {
        for (NetworkPlayerInfo npi : Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getPlayerInfoMap()) {
            if (npi.getGameProfile().getName().contains(bot.getName())) {
                return false;
            }
        }
        return true;
    }

    int botPercentage(EntityLivingBase bot) {
        int percentage = 0;
        if (tick.getValDouble() > 0.0 && bot.ticksExisted < tick.getValDouble()) {
            percentage++;
        }
        if (ifInAir.getValBoolean()
                && bot.isInvisible()
                && bot.posY > mc.player.posY + 1.0
                && Utils.isBlockMaterial(new BlockPos(bot).down(), Blocks.AIR)) {
            percentage++;
        }
        if (ifGround.getValBoolean()
                && bot.motionY == 0.0
                && !bot.collidedVertically
                && bot.onGround
                && bot.posY % 1.0 != 0.0
                && bot.posY % 0.5 != 0.0) {
            percentage++;
        }
        if (ifZeroHealth.getValBoolean() && (bot.getHealth() <= 0 || bot.isDead)) {
            percentage++;
        }
        if (ifInvisible.getValBoolean() && bot.isInvisible()) {
            percentage++;
        }

        if (canEntityBeSeen.getValBoolean() && !mc.player.canEntityBeSeen(bot)) {
            percentage++;
        }

        if (ifPing.getValBoolean()) {
            Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getPlayerInfo(bot.getUniqueID());
            if (Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getPlayerInfo(bot.getUniqueID()).getResponseTime() <= 5)
                percentage++;
        }

        if (ifEntityId.getValBoolean() && bot.getEntityId() >= 1000000000) {
            percentage++;
        }
        if (ifTabName.getValBoolean() && isBotTabName(bot)) {
            percentage++;
        }
        return percentage;
    }

    boolean isBotBase(EntityLivingBase entity) {
        if (isBot(entity))
            return true;
        if (entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).getGameProfile();
            GameProfile botProfile = ((EntityPlayer) entity).getGameProfile();
            entity.getUniqueID();
            if (botProfile.getName() == null) {
                return true;
            }
            String botName = botProfile.getName();
            return botName.contains("Body #") || botName.contains("NPC")
                    || botName.equalsIgnoreCase(Utils.getEntityNameColor(entity));
        }
        return false;
    }

    public static class EntityBot {
        private final String name;
        private final int id;
        private final UUID uuid;
        private final boolean invisible;

        public EntityBot(EntityLivingBase entity) {
            this.name = entity.getName();
            this.id = entity.getEntityId();
            this.uuid = entity.getUniqueID();
            this.invisible = entity.isInvisible();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public UUID getUuid() {
            return uuid;
        }

        public boolean isInvisible() {
            return invisible;
        }
    }
}
