package Method.Client.managers;

import Method.Client.Main;
import Method.Client.module.misc.PlayerHistory;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Method.Client.utils.visual.RenderUtils.RenderBlock;

public class OtherPlayerManager {

    public List<EntityofIntrest> Entities = new CopyOnWriteArrayList<>();

    private static ExecutorService executor;

    public OtherPlayerManager() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static void execute(Runnable task) {
        executor.execute(task);
    }


    public void LivingDeathEvent(LivingDeathEvent event, PlayerHistory module) {
        execute(() -> {
            if (event.getEntityLiving() instanceof EntityOtherPlayerMP) {
                if (!event.getEntityLiving().getName().equals(Minecraft.getMinecraft().player.getName())) {
                    for (EntityofIntrest player : Entities) {
                        player.pops = 0;
                        if (module.TotemPopLogs.getValBoolean())
                            ChatUtils.message(ChatFormatting.RED + event.getEntityLiving().getName() + " died after popping " + ChatFormatting.GREEN + player.pops + " totems!");
                        return;
                    }
                }
            }
        });
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event, PlayerHistory module) {
        execute(() -> {
            if (event.getEntity() instanceof EntityPlayer)
                if (!Minecraft.getMinecraft().player.equals(event.getEntity()))
                    for (EntityofIntrest player : Entities) {
                        if (player.Entity.getName().equals(event.getEntity().getName())) {
                            if (player.lastseen != null) {
                                if (module.playSound.getValBoolean())
                                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
                                if (module.LeaveJoin.getValBoolean())
                                    ChatUtils.message((FriendManager.isFriend(event.getEntity().getName()) ? ChatFormatting.GREEN.toString() : ChatFormatting.RED
                                            + event.getEntity().getName() + ChatFormatting.RESET +
                                            (module.ShowCoords.getValBoolean() ? " Rejoined at, x: " + event.getEntity().getPosition().getX() + " y: "
                                                    + event.getEntity().getPosition().getY() + " z: " + event.getEntity().getPosition().getZ() : " Joined!")));
                                player.lastseen = null;
                            }
                            break;
                        }

                    }

        });
    }

    public void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event, PlayerHistory module) {
        Entity entity = event.player;
        if (entity != null)
            for (EntityofIntrest entityofIntrest : Entities) {
                if (entityofIntrest.Entity.getName().equals(entity.getName())) {
                    entityofIntrest.lastseen = entity.getPositionVector();
                    entityofIntrest.locationsSeen.add(entityofIntrest.lastseen);
                    if (module.playSound.getValBoolean())
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
                    ChatUtils.message((FriendManager.isFriend(entityofIntrest.Entity.getName()) ? ChatFormatting.GREEN.toString() :
                            ChatFormatting.RED + entityofIntrest.Entity.getName() +
                                    ChatFormatting.RESET +
                                    (module.ShowCoords.getValBoolean() ? " Left at, x: " +
                                            entityofIntrest.Entity.getPosition().getX() + " y: " +
                                            entityofIntrest.Entity.getPosition().getY() + " z: " +
                                            entityofIntrest.Entity.getPosition().getZ() : " Left!")));
                }
            }
    }


    public void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event, PlayerHistory module) {
        execute(() -> {
            if (event.getEntityLiving() instanceof EntityOtherPlayerMP) {
                if (!event.getEntityLiving().getName().equals(Minecraft.getMinecraft().player.getName())) {
                    for (EntityofIntrest player : Entities) {
                        if (player.Entity.getName().equals(event.getEntityLiving().getName())) {
                            if (event.getEntityLiving().getDistance(
                                    player.locationsSeen.get(player.locationsSeen.size() - 1).x,
                                    player.locationsSeen.get(player.locationsSeen.size() - 1).y,
                                    player.locationsSeen.get(player.locationsSeen.size() - 1).z) > module.MoveDistance.getValDouble()) {
                                player.locationsSeen.add(event.getEntityLiving().getPositionVector());
                            }
                            return;
                        }
                    }
                    if (module.playSound.getValBoolean())
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
                    if (module.LeaveJoin.getValBoolean())
                        ChatUtils.message((FriendManager.isFriend(event.getEntityLiving().getName()) ? ChatFormatting.GREEN.toString() : ChatFormatting.RED
                                + event.getEntityLiving().getName() + ChatFormatting.RESET +
                                (module.ShowCoords.getValBoolean() ? " Joined at, x: " + event.getEntityLiving().getPosition().getX() + " y: "
                                        + event.getEntityLiving().getPosition().getY() + " z: " + event.getEntityLiving().getPosition().getZ() : " Joined!")));

                    Entities.add(new EntityofIntrest(event.getEntityLiving(), true));
                }
            }
        });
    }


    public void onPacket(Object packet, PlayerHistory module) {
        execute(() -> {
            if (packet instanceof SPacketEffect) {
                final SPacketEffect sPacketEffect = (SPacketEffect) packet;
                switch (sPacketEffect.getSoundType()) {
                    case 1023: {
                        EntityofIntrest wither = new EntityofIntrest(new EntityWither(Minecraft.getMinecraft().world), true);
                        wither.setpos(new BlockPos(sPacketEffect.getSoundPos().x, sPacketEffect.getSoundPos().y, sPacketEffect.getSoundPos().z));
                        Entities.add(wither);
                        if (module.GlobalSound.getValBoolean())
                            ChatUtils.message("Wither Spawned See Entity Manager");
                        break;
                    }
                    case 1028: {
                        EntityofIntrest Dragon = new EntityofIntrest(new EntityDragon(Minecraft.getMinecraft().world), true);
                        Dragon.setpos(new BlockPos(sPacketEffect.getSoundPos().x, sPacketEffect.getSoundPos().y, sPacketEffect.getSoundPos().z));
                        Entities.add(Dragon);
                        if (module.GlobalSound.getValBoolean())
                            ChatUtils.message("Ender Dragon Defeated See Entity Manager");
                        break;
                    }
                    case 1038: {
                        EntityofIntrest EndPortal = new EntityofIntrest(new EntityEnderEye(Minecraft.getMinecraft().world), true);
                        EndPortal.setpos(new BlockPos(sPacketEffect.getSoundPos().x, sPacketEffect.getSoundPos().y, sPacketEffect.getSoundPos().z));
                        Entities.add(EndPortal);
                        if (module.GlobalSound.getValBoolean())
                            ChatUtils.message("End Portal Activated See Entity Manager");
                        break;
                    }
                }
            }

            if (packet instanceof SPacketEntityStatus) {
                final SPacketEntityStatus packet2 = (SPacketEntityStatus) packet;
                if (packet2.getOpCode() == 35) {
                    final Entity entity = packet2.getEntity(Minecraft.getMinecraft().world);
                    for (EntityofIntrest player : Entities) {
                        if (player.Entity.getName().equals(entity.getName())) {
                            player.pops++;
                            if (module.TotemPopLogs.getValBoolean())
                                ChatUtils.message(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " popped " + ChatFormatting.YELLOW + player.pops + ChatFormatting.RED + " totems!");
                            break;
                        }
                    }
                }
            }
            if (packet instanceof SPacketSoundEffect) {
                SPacketSoundEffect packet2 = (SPacketSoundEffect) packet;

                if (packet2.getSound() != SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                    return;
                }

                BlockPos pos = new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ());
                if (module.GlobalSound.getValBoolean())
                    if (pastDistance(Wrapper.mc.player, pos, module.TeleportDis.getValDouble())) {
                        ChatUtils.warning("Lightning strike See Entity Manager");
                        EntityofIntrest lightning = new EntityofIntrest(new EntityLightningBolt(Minecraft.getMinecraft().world, pos.x, pos.y, pos.z, true), false);
                        Entities.add(lightning);
                    }
            } else if (packet instanceof SPacketEntityTeleport) {
                SPacketEntityTeleport sPacketEntityTeleport = (SPacketEntityTeleport) packet;
                if (sPacketEntityTeleport.getEntityId() != Wrapper.mc.player.getEntityId()) {
                    Entity teleporting = Wrapper.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId());
                    if (teleporting != null) {

                        BlockPos pos = new BlockPos(sPacketEntityTeleport.posX, sPacketEntityTeleport.posY, sPacketEntityTeleport.posZ);

                        if (teleporting instanceof EntityWolf || teleporting instanceof EntityOcelot || teleporting instanceof EntityHorse) {
                            if (module.Teleport.getValBoolean())
                                if (pastDistance(Wrapper.mc.player, pos, module.TeleportDis.getValDouble())) {
                                    EntityofIntrest animal = new EntityofIntrest(teleporting, true);
                                    Entities.add(animal);
                                    ChatUtils.warning("Animal Teleport  See Entity Manager");
                                }
                        } else if (teleporting instanceof EntityOtherPlayerMP) {
                            if (module.Teleport.getValBoolean())
                                if (pastDistance(Wrapper.mc.player, pos, module.TeleportDis.getValDouble())) {
                                    // check if player is in the list
                                    boolean found = false;
                                    for (EntityofIntrest entity : Entities) {
                                        if (entity.Entity.getName().equals(teleporting.getName())) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        EntityofIntrest player = new EntityofIntrest(teleporting, true);
                                        Entities.add(player);
                                        ChatUtils.warning(teleporting.getName() + " Teleported See Entity Manager");
                                    }
                                    if (found) {
                                        for (EntityofIntrest entity : Entities) {
                                            if (entity.Entity.getName().equals(teleporting.getName())) {
                                                entity.TeleportedLocation.add(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                                                ChatUtils.warning(teleporting.getName() + " Teleported See Entity Manager");
                                                break;
                                            }
                                        }
                                    }
                                }
                        }


                    }
                }
            }

        });
    }

    private static boolean pastDistance(EntityPlayer player, BlockPos pos, double dist) {
        return player.getDistanceSqToCenter(pos) >= Math.pow(dist, 2);
    }

    public double getPops(EntityPlayer player) {
        for (EntityofIntrest entity : Entities) {
            if (entity.Entity.getName().equals(player.getName())) {
                return entity.pops;
            }
        }
        return 0;
    }

    public void onWorldLoad(WorldEvent.Load event, PlayerHistory playerHistory) {
        // clear the list
        Entities.clear();
    }

    public void onRenderWorldLast(RenderWorldLastEvent event, PlayerHistory playerHistory) {
        if (playerHistory.Box.getValBoolean()) {
            for (OtherPlayerManager.EntityofIntrest entity : Main.PlayerManager.Entities) {
                if (entity.getLastseen() == null)
                    continue;
                double renderPosX = entity.getLastseen().x - Minecraft.getMinecraft().getRenderManager().viewerPosX;
                double renderPosY = entity.getLastseen().y - Minecraft.getMinecraft().getRenderManager().viewerPosY;
                double renderPosZ = entity.getLastseen().z - Minecraft.getMinecraft().getRenderManager().viewerPosZ;

                final AxisAlignedBB bb = new AxisAlignedBB(renderPosX, renderPosY, renderPosZ,
                        renderPosX + 1,
                        renderPosY + 2,
                        renderPosZ + 1);
                RenderUtils.SimpleNametag(entity.getLastseen(), entity.getEntity().getName() + (playerHistory.ShowCoords.getValBoolean() ? "X: " + (int) entity.getEntity().posX + " Y: " + (int) entity.getEntity().posY + " Z: " + (int) entity.getEntity().posZ : ""));
                RenderBlock(playerHistory.Mode.getValString(), bb, FriendManager.isFriend(entity.getEntity().getName()) ? ColorUtils.rainbow().getRGB() : playerHistory.color.getcolor(), playerHistory.LineWidth.getValDouble());
            }
        }
    }


    public static class EntityofIntrest {

        public net.minecraft.entity.Entity getEntity() {
            return Entity;
        }

        public ArrayList<Vec3d> getLocationsSeen() {
            return locationsSeen;
        }

        public ArrayList<Vec3d> getTeleportedLocation() {
            return TeleportedLocation;
        }

        public boolean isPlayer() {
            return player;
        }

        public BlockPos getLocation() {
            return location;
        }

        Entity Entity;
        ArrayList<Vec3d> locationsSeen = new ArrayList<>();
        ArrayList<Vec3d> TeleportedLocation = new ArrayList<>();

        public Vec3d getLastseen() {
            return lastseen;
        }

        Vec3d lastseen = null;
        boolean player;
        BlockPos location;
        public int pops;

        EntityofIntrest(Entity entity, boolean addpos) {
            this.player = entity instanceof EntityOtherPlayerMP;
            this.Entity = entity;
            if (addpos)
                this.locationsSeen.add(new Vec3d(entity.posX, entity.posY, entity.posZ));
        }

        public void setpos(BlockPos pos) {
            this.location = pos;
        }
    }


}
