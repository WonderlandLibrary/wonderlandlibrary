package rosegoldclient.features;

import com.google.gson.Gson;
import gg.essential.api.utils.Multithreading;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PlayerMoveEvent;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.*;
import rosegoldclient.utils.pathfinding.Pathfinder;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoProfessions {

    public static EntityLivingBase target;
    public static EntityLivingBase farTarget;
    public static HashSet<BlockPos> trees = new HashSet<>();
    public static HashSet<BlockPos> usedTrees = new HashSet<>();
    public static HashSet<BlockPos> ores = new HashSet<>();
    public static HashSet<BlockPos> usedOres = new HashSet<>();
    public static HashSet<BlockPos> fish = new HashSet<>();
    public static HashSet<BlockPos> usedFish = new HashSet<>();
    public static HashSet<BlockPos> crops = new HashSet<>();
    public static HashSet<BlockPos> usedCrops = new HashSet<>();
    public static final HashSet<String> fishTypes = Stream.of(
            "Gudgeon", "Trout", "Salmon", "Carp", "Icefish", "Piranha", "Koi", "Gylia Fish", "Bass", "Molten Eel", "Starfish", "Dernic Fish"
    ).collect(Collectors.toCollection(HashSet::new));
    private static final CPacketPlayerTryUseItem use = new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND);
    private static final CPacketAnimation swing = new CPacketAnimation(EnumHand.MAIN_HAND);
    public static boolean waiting;
    public static long stuckTimer = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Main.autoProfessions || !Main.configFile.autoProfessions || Main.mc.player == null || Main.mc.world == null)
            return;
        target = getEntity();
        farTarget = getFarEntity();
        if (Main.configFile.autoProfessionsType == 1) {
            if (target != null && stuckTimer > 200) {
                stuckTimer = 0;
                Pathfinding.initWalk();
                Multithreading.runAsync(() -> Pathfinder.setup(
                        new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())),
                        new BlockPos(target.getPositionVector()),
                        0.0
                ));
                BlockPos save = target.getPosition();
                switch (Main.configFile.autoProfessionsSkillType) {
                    case 0: //woodcutting
                        if (!usedTrees.contains(save)) {
                            usedTrees.add(save);
                            Multithreading.runAsync(() -> {
                                try {
                                    Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                                    usedTrees.remove(save);
                                } catch (Exception ignored) {
                                }
                            });

                        }
                        break;
                    case 1: //mining
                        if (!usedOres.contains(save)) {
                            usedOres.add(save);
                            Multithreading.runAsync(() -> {
                                try {
                                    Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                                    usedOres.remove(save);
                                } catch (Exception ignored) {
                                }
                            });
                        }
                        break;
                    case 2: //fishing
                        if (!usedFish.contains(save)) {
                            usedFish.add(save);
                            Multithreading.runAsync(() -> {
                                try {
                                    Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                                    usedFish.remove(save);
                                } catch (Exception ignored) {
                                }
                            });
                        }
                        break;
                    case 3: //farming
                        if (!usedCrops.contains(save)) {
                            usedCrops.add(save);
                            Multithreading.runAsync(() -> {
                                try {
                                    Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                                    usedCrops.remove(save);
                                } catch (Exception ignored) {
                                }
                            });
                        }
                        break;
                }
            }
            if (farTarget != null) {
                stuckTimer++;
                switch (Main.configFile.autoProfessionsSkillType) {
                    case 0: //woodcutting
                        trees.add(farTarget.getPosition());
                        break;
                    case 1: //mining
                        ores.add(farTarget.getPosition());
                        break;
                    case 2: //fishing
                        fish.add(farTarget.getPosition());
                        break;
                    case 3: //farming
                        crops.add(farTarget.getPosition());
                        break;
                }
                if (target != farTarget) {
                    if (!waiting) {
                        waiting = true;
                        Utils.sendModMessage("walking to entity");
                        Pathfinding.initWalk();
                        Multithreading.runAsync(() -> Pathfinder.setup(
                                new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())),
                                new BlockPos(farTarget.getPositionVector()),
                                0.0
                        ));
                    }
                } else {
                    if (waiting) {
                        stuckTimer = 0;
                        Utils.sendModMessage("finished walking");
                        if (target != null) {
                            addBlockToUsed(target.getPosition());
                        }
                    }
                    waiting = false;
                }
            } else {
                BlockPos pos = null;
                switch (Main.configFile.autoProfessionsSkillType) {
                    case 0: //woodcutting
                        pos = getPosFromHashSet(trees, usedTrees);
                        break;
                    case 1: //mining
                        pos = getPosFromHashSet(ores, usedOres);
                        break;
                    case 2: //fishing
                        pos = getPosFromHashSet(fish, usedFish);
                        break;
                    case 3: //farming
                        pos = getPosFromHashSet(crops, usedCrops);
                        break;
                }
                if (target == null && pos != null) {
                    BlockPos finalPos = pos;
                    if (!waiting) {
                        waiting = true;
                        Utils.sendModMessage("walking to block");
                        Pathfinding.initWalk();
                        Multithreading.runAsync(() -> Pathfinder.setup(
                                new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())),
                                finalPos,
                                0.0
                        ));
                    }
                } else {
                    if (waiting) {
                        Utils.sendModMessage("finished walking");
                        addBlockToUsed(pos);
                        stuckTimer = 0;
                    }
                    waiting = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Main.autoProfessions || !Main.configFile.autoProfessions || Main.mc.player == null || Main.mc.world == null)
            return;
        if (target != null) {
            RenderUtils.drawEntityESP(target, ColorUtils.getChroma(3000.0f, (int) (target.posX + target.posY + target.posZ)), event.getPartialTicks());
        } else if (farTarget != null) {
            RenderUtils.drawEntityESP(farTarget, Color.WHITE, event.getPartialTicks());
        }
        switch (Main.configFile.autoProfessionsSkillType) {
            case 0: //woodcutting
                for (BlockPos blockPos : trees) {
                    if (usedTrees.contains(blockPos)) {
                        RenderUtils.drawBlockESP(blockPos, Color.WHITE, event.getPartialTicks());
                    } else {
                        RenderUtils.drawBlockESP(blockPos, Color.BLUE, event.getPartialTicks());
                    }
                }
                break;
            case 1: //mining
                for (BlockPos blockPos : ores) {
                    if (usedOres.contains(blockPos)) {
                        RenderUtils.drawBlockESP(blockPos, Color.WHITE, event.getPartialTicks());
                    } else {
                        RenderUtils.drawBlockESP(blockPos, Color.BLUE, event.getPartialTicks());
                    }
                }
                break;
            case 2: //fishing
                for (BlockPos blockPos : fish) {
                    if (usedFish.contains(blockPos)) {
                        RenderUtils.drawBlockESP(blockPos, Color.WHITE, event.getPartialTicks());
                    } else {
                        RenderUtils.drawBlockESP(blockPos, Color.BLUE, event.getPartialTicks());
                    }
                }
                break;
            case 3: //farming
                for (BlockPos blockPos : crops) {
                    if (usedCrops.contains(blockPos)) {
                        RenderUtils.drawBlockESP(blockPos, Color.WHITE, event.getPartialTicks());
                    } else {
                        RenderUtils.drawBlockESP(blockPos, Color.BLUE, event.getPartialTicks());
                    }
                }
                break;
        }
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if (!Main.autoProfessions || !Main.configFile.autoProfessions || Main.mc.player == null || Main.mc.world == null)
            return;
        FontUtils.drawScaledCenteredString("Stuck Ticks: " + stuckTimer, 0.9f, Main.mc.displayWidth / 4, (int) (Main.mc.displayHeight * 0.1), true);
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        try {
            if (trees != null)
                Files.write(Paths.get("./config/rosegoldclient/treeLocations.json"), new Gson().toJson(trees).getBytes(StandardCharsets.UTF_8));
            if (ores != null)
                Files.write(Paths.get("./config/rosegoldclient/oreLocations.json"), new Gson().toJson(ores).getBytes(StandardCharsets.UTF_8));
            if (fish != null)
                Files.write(Paths.get("./config/rosegoldclient/fishLocations.json"), new Gson().toJson(fish).getBytes(StandardCharsets.UTF_8));
            if (crops != null)
                Files.write(Paths.get("./config/rosegoldclient/cropLocations.json"), new Gson().toJson(crops).getBytes(StandardCharsets.UTF_8));
        } catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if (!Main.autoProfessions) {
            target = null;
            return;
        }
        if (!Main.configFile.autoProfessions || Main.mc.player == null || Main.mc.world == null) return;
        if (target != null) {
            RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdatePost(PlayerMoveEvent.Post post) {
        if (!Main.autoProfessions || !Main.configFile.autoProfessions || Main.mc.player == null || Main.mc.world == null)
            return;
        if (Main.mc.getConnection() == null) return;
        if (target == null) return;
        if (Main.mc.player.ticksExisted % 2 != 0) return;
        if (Main.configFile.autoProfessionsGatherType == 0) { //right click
            Main.mc.getConnection().getNetworkManager().sendPacket(use);
        } else {
            Main.mc.getConnection().getNetworkManager().sendPacket(swing);
        }
    }

    private static void addBlockToUsed(BlockPos save) {
        switch (Main.configFile.autoProfessionsSkillType) {
            case 0: //woodcutting
                if (!usedTrees.contains(save)) {
                    usedTrees.add(save);
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                            usedTrees.remove(save);
                        } catch (Exception ignored) {
                        }
                    });

                }
                break;
            case 1: //mining
                if (!usedOres.contains(save)) {
                    usedOres.add(save);
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                            usedOres.remove(save);
                        } catch (Exception ignored) {
                        }
                    });
                }
                break;
            case 2: //fishing
                if (!usedFish.contains(save)) {
                    usedFish.add(save);
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                            usedFish.remove(save);
                        } catch (Exception ignored) {
                        }
                    });
                }
                break;
            case 3: //farming
                if (!usedCrops.contains(save)) {
                    usedCrops.add(save);
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(Main.configFile.autoProfessionsNodeRegrowTime * 1000L);
                            usedCrops.remove(save);
                        } catch (Exception ignored) {
                        }
                    });
                }
                break;
        }
    }

    private static BlockPos getPosFromHashSet(HashSet<BlockPos> set, HashSet<BlockPos> usedSet) {
        List<BlockPos> blockPosList = set.stream().filter(
                blockPos -> !usedSet.contains(blockPos)
        ).sorted(
                Comparator.comparingDouble(blockPos -> blockPos.getDistance(
                        Main.mc.player.getPosition().getX(),
                        Main.mc.player.getPosition().getY(),
                        Main.mc.player.getPosition().getZ()
                ))
        ).collect(Collectors.toList());

        Iterator<BlockPos> iterator = blockPosList.iterator();
        if (iterator.hasNext()) {
            BlockPos next = iterator.next();
            if (next.getDistance(
                    Main.mc.player.getPosition().getX(),
                    Main.mc.player.getPosition().getY(),
                    Main.mc.player.getPosition().getZ())
                    <= Main.configFile.autoProfessionsNodeMaxDistance) return next;
        }
        return null;
    }

    private static EntityLivingBase getFarEntity() {
        if (Main.mc.world == null) return null;
        List<Entity> entityList = Main.mc.world.getLoadedEntityList().stream().filter(
                entity -> entity instanceof EntityLivingBase
        ).filter(
                entity -> isValidFar((EntityLivingBase) entity)
        ).sorted(
                Comparator.comparingDouble(e -> e.getDistance(Main.mc.player))
        ).collect(Collectors.toList());

        Iterator<Entity> iterator = entityList.iterator();
        if (iterator.hasNext()) {
            Entity en = iterator.next();
            return (EntityLivingBase) en;
        }
        return null;
    }

    private static EntityLivingBase getEntity() {
        if (Main.mc.world == null) return null;
        List<Entity> entityList = Main.mc.world.getLoadedEntityList().stream().filter(
                entity -> entity instanceof EntityLivingBase
        ).filter(
                entity -> isValid((EntityLivingBase) entity, 5)
        ).sorted(
                Comparator.comparingDouble(e -> e.getDistance(Main.mc.player))
        ).collect(Collectors.toList());

        Iterator<Entity> iterator = entityList.iterator();
        if (iterator.hasNext()) {
            Entity en = iterator.next();
            return (EntityLivingBase) en;
        }
        return null;
    }

    private static boolean isValidFar(EntityLivingBase entity) {
        switch (Main.configFile.autoProfessionsSkillType) {
            case 0: //woodcutting
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition().add(0, 1, 0);
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.LOG ||
                            Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.LOG2) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
            case 1: //mining
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition();
                    BlockPos above = entity.getPosition().add(0, 1, 0);
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.BARRIER &&
                            Main.mc.world.getBlockState(above).getBlock() != Blocks.LOG &&
                            Main.mc.world.getBlockState(above).getBlock() != Blocks.LOG2 &&
                            Main.mc.world.getBlockState(above).getBlock() != Blocks.FARMLAND) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
            case 2: //fishing
                if (entity instanceof EntityArmorStand) {
                    ItemStack itemStack = entity.getHeldItemMainhand();
                    if (itemStack.isEmpty()) return false;
                    return fishTypes.contains(itemStack.getDisplayName());
                }
                break;
            case 3: //farming
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition().add(0, 1, 0);
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.FARMLAND) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
        }
        return false;
    }

    private static boolean isValid(EntityLivingBase entity, float range) {
        if (entity.getDistance(Main.mc.player) > range) return false;
        switch (Main.configFile.autoProfessionsSkillType) {
            case 0: //woodcutting
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition().add(0, 1, 0);
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.LOG ||
                            Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.LOG2) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
            case 1: //mining
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition();
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.BARRIER) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
            case 2: //fishing
                if (entity instanceof EntityArmorStand) {
                    ItemStack itemStack = entity.getHeldItemMainhand();
                    if (itemStack.isEmpty()) return false;
                    return fishTypes.contains(itemStack.getDisplayName());
                }
                break;
            case 3: //farming
                if (entity instanceof EntityZombie) {
                    BlockPos blockPos = entity.getPosition().add(0, 1, 0);
                    if (Main.mc.world.getBlockState(blockPos).getBlock() == Blocks.FARMLAND) {
                        return entity.isChild() && entity.motionX == 0D && entity.motionY == 0D && entity.motionZ == 0D;
                    }
                }
                break;
        }
        return false;
    }
}
