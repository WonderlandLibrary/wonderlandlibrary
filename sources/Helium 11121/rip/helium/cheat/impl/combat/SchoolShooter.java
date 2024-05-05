package rip.helium.cheat.impl.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.FriendManager;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.RotUtils;
import rip.helium.utils.RotationUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.*;
import java.util.function.Predicate;

public class SchoolShooter extends Cheat {
    int index;
    float yaw;
    float pitch;
    EntityLivingBase targ;
    StringsProperty mode;
    Stopwatch timer = new Stopwatch();
    private final boolean aimbot = true;
    private Optional<EntityPlayer> target;
    private float velocity;
    private final double range;
    private final Random random;


    public SchoolShooter() {
        super("FastBow", "Makes your bow fast (FastBow)", CheatCategory.COMBAT);
        this.range = 50.0;
        this.random = new Random();
        this.mode = new StringsProperty("Mode", "", null, false, true, new String[]{"Multi", "Viper", "Ghostly"}, new Boolean[]{true, false, false});
        registerProperties(mode);
    }

    @Collect
    public void updateevent(PlayerUpdateEvent event) {
        if (mode.getValue().get("Multi")) {
            if (event.isPre()) {
                if (mc.thePlayer.rotationPitch <= -80.0f || mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
                    this.target = null;
                    return;
                }
                this.target = this.findNextTarget();
                final int bowCurrentCharge = 20;
                this.velocity = bowCurrentCharge / 20.0f;
                this.velocity = (this.velocity * this.velocity + this.velocity * 2.0f) / 3.0f;
                if (this.velocity < 0.1) {
                    return;
                }
                if (this.velocity > 1.0f) {
                    this.velocity = 1.0f;
                }

                this.target.ifPresent(target -> {
                    final double distanceToEnt;
                    final double predictX;
                    final double predictZ;
                    final double x;
                    final double z;
                    final double h;
                    final double h2;
                    final double h3;
                    final float yaw;
                    float pitch;
                    int index;
                    distanceToEnt = mc.thePlayer.getDistanceToEntity(target);
                    predictX = target.posX + (target.posX - target.lastTickPosX) * (distanceToEnt / this.getVelocity() + this.getPingMoveTicks(target));
                    predictZ = target.posZ + (target.posZ - target.lastTickPosZ) * (distanceToEnt / this.getVelocity() + this.getPingMoveTicks(target));
                    x = predictX - mc.thePlayer.posX;
                    z = predictZ - mc.thePlayer.posZ;
                    h = target.posY + target.getEyeHeight() - (mc.thePlayer.posY + 0.9 + mc.thePlayer.getEyeHeight());
                    h2 = Math.sqrt(x * x + z * z);
                    h3 = Math.sqrt(h2 * h2 + h * h);
                    yaw = (float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
                    pitch = -this.getTrajAngleSolutionLow((float) h2, (float) h, this.velocity);
                    if (Float.isNaN(pitch)) {
                        pitch = mc.thePlayer.rotationPitch;
                    }
                    if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                        for (index = 0; index < 20 + this.random.nextInt(1642); ++index) {
                            if (target != null) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, yaw, pitch, true));
                            }
                        }
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                });
            }
        } else if (mode.getValue().get("Viper")) {
            if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                //mc.timer.timerSpeed = 1.2f;
                if (timer.hasPassed(195)) {
                    //mc.timer.timerSpeed = 1.5f;
                    mc.playerController.sendUseItem(mc.thePlayer, Minecraft.getMinecraft().theWorld, mc.thePlayer.inventory.getCurrentItem());
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

                    //targ = getTarg();

                    //final float[] rotations = RotUtils.getBowAngles(targ);
                    //event.setYaw(rotations[0]);
                    //event.setPitch(rotations[1]);
                    for (int i = 0; i < 20; i++) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
                    }
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, mc.thePlayer, 10);
                    timer.reset();
                }
            } else {
                Timer.timerSpeed = 1f;
            }
        } else if (mode.getValue().get("Ghostly")) {
            if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                if (mc.thePlayer.ticksExisted % 4 == 0) {
                    double d = mc.thePlayer.posX;
                    double d2 = mc.thePlayer.posY + 1.0E-9;
                    double d3 = mc.thePlayer.posZ;
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

                    for (int i = 0; i < 20; i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(d, d2, d3, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                    }
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, mc.thePlayer, 10);
                }
            }
        }
    }

    @Collect
    public void customKKK(PlayerUpdateEvent event) {
        if (mode.getValue().get("Multi")) {
            EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
            if (p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem().getItem() instanceof ItemBow
                    && Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed && p.onGround) {
                Minecraft.getMinecraft().playerController.sendUseItem(p, Minecraft.getMinecraft().theWorld, p.inventory.getCurrentItem());
                p.inventory.getCurrentItem().getItem().onItemRightClick(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p);
                for (int i = 0; i < 20; i++)
                    p.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                Minecraft.getMinecraft().getNetHandler()
                        .addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                p.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(p.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, p, 10);
            }
        }
    }

    private float getTrajAngleSolutionLow(final float d3, final float d1, final float velocity) {
        final float g = 0.006f;
        final float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float) Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }

    private EntityLivingBase getTarg() {
        final List<EntityLivingBase> loaded = new ArrayList<EntityLivingBase>();
        for (final Object o3 : mc.theWorld.getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase) o3;
                if (!(ent instanceof EntityPlayer) || ent == mc.thePlayer || !mc.thePlayer.canEntityBeSeen(ent) || mc.thePlayer.getDistanceToEntity(ent) >= 65.0f || FriendManager.isFriend(ent.getName())) {
                    continue;
                }
                loaded.add(ent);
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        final float[][] rot1 = new float[1][1];
        final float[][] rot2 = new float[1][1];
        loaded.sort((o1, o2) -> {
            rot1[0] = RotUtils.getRotations(o1);
            rot2[0] = RotUtils.getRotations(o2);
            return (int) (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot1[0][0]) + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot1[0][1]) - (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot2[0][0]) + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot2[0][1])));
        });
        final EntityLivingBase target = loaded.get(0);
        return target;
    }


    private Optional<EntityPlayer> findNextTarget() {
        return mc.theWorld.playerEntities.stream().filter(this.valid()).sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity))).findFirst();
    }

    private float getVelocity() {
        final float vel = this.velocity;
        return vel * 2.0f;
    }

    private double getPingMoveTicks(final EntityLivingBase e) {
        if (e instanceof EntityOtherPlayerMP) {
            final EntityOtherPlayerMP player = (EntityOtherPlayerMP) e;
            return this.getPlayerPing(player.getName()) / 50;
        }
        return 0.0;
    }

    public int getPlayerPing(final String name) {
        final EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
        if (player instanceof EntityOtherPlayerMP) {
            try {
                return ((EntityOtherPlayerMP) player).playerInfo.getResponseTime();
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private Predicate<EntityLivingBase> valid() {
        return entity -> entity != null && entity instanceof EntityPlayer && entity != mc.thePlayer && entity instanceof EntityPlayer && mc.thePlayer.canEntityBeSeen(entity) && entity.getDistanceToEntity(mc.thePlayer) <= 20.0f && !FriendManager.friends.contains(entity.getName());
    }

}
