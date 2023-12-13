package koks.module.debug;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.pathfinder.PathFinderHelper;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.TimeHelper;
import koks.event.NoClipEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import koks.event.UpdateMotionEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kroko
 * @created on 22.01.2021 : 17:52
 */

@Module.Info(name = "Debug", description = "Debug", category = Module.Category.DEBUG)
public class Debug extends Module {

    boolean phased = false, hitler = false;
    int jumps = 0, fliegen = 0;
    double moveSpeed = 0;
    final TimeHelper timeHelper = new TimeHelper();
    Packet<?> packet = null;
    public final PathFinderHelper pathFinderHelper = new PathFinderHelper();

    final ArrayList<Packet<?>> packetList = new ArrayList<>();
    final CopyOnWriteArrayList<Packet<?>> copy = new CopyOnWriteArrayList<>();

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof final C0EPacketClickWindow inventoryAction) {
                //Window id: 15
                //slot id: 4
                //action = 1
            }
            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                /*if(getPlayer().fallDistance > 3.5 && !hitler) {
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), Double.NEGATIVE_INFINITY, getZ(), true));
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));
                    getPlayer().fallDistance = 0;
                    hitler = true;
                }*/
            }
            /*if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive) {
                if(packetList.size() == 20) {
                    try {
                        packetList.forEach(this::sendPacketUnlogged);
                        packetList.clear();
                    } catch (Exception e) {}
                }
                packetList.add(packet);
                event.setCanceled(true);
            }*/

            /*if(packet instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook posLook = (S08PacketPlayerPosLook) packet;
                if (getPlayer().ticksExisted % 40 == 0 && !getPlayer().isSwingInProgress) {
                    event.setCanceled(true);
                    sendPacketUnlogged(new C03PacketPlayer.C06PacketPlayerPosLook(posLook.getX(), posLook.getY(), posLook.getZ(), posLook.getYaw(), posLook.getPitch(), false));
                }
            }*/
        }


        if (event instanceof UpdateMotionEvent) {
            final UpdateMotionEvent updateMotionEvent = (UpdateMotionEvent) event;
            //updateMotionEvent.setYaw(getPlayer().rotationYaw + 180);
        }

        if (event instanceof NoClipEvent) {

        }

        if (event instanceof UpdateEvent) {
            /*if (mc.isSingleplayer()) return;
            sendPacketUnlogged(new C01PacketChatMessage("/ac"));
            if (getPlayer().openContainer != null) {
                if (getPlayer().openContainer.inventorySlots.size() >= 10)
                    for (int i = 0; i < 10; i++)
                        getPlayerController().windowClick(getPlayer().openContainer.windowId, i, 0, 0, getPlayer());
            }*/
            final MovementUtil movementUtil = MovementUtil.getInstance();

            /*if(getPlayer().onGround) {
                hitler = false;
            }*/

            /*if (!getPlayer().isSwingInProgress) {
                if (getPlayer().ticksExisted % 40 == 0) {
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 12, getZ(), false));
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));
                }
            }*/

            //TODO: Rots to entity
            //sendPacketUnlogged(new C00Handshake(ViaFabric.clientSideVersion, mc.getCurrentServerData().serverIP, jumps, EnumConnectionState.LOGIN));
            /*if (!hitler && timeHelper.hasReached(150, true)) {
                final EntityLivingBase base = getNearest();
                if (base != null) {
                    if (base.getDistanceToEntity(getPlayer()) < 4) {
                        final float[] rots = RotationUtil.getInstance().facePlayer(base, false, false, false, false, false, 5,true, 0.0, false, 180);
                        ((RotationEvent) event).setYaw(rots[0]);
                        ((RotationEvent) event).setPitch(rots[1]);

                        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                            sendPacketUnlogged(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.INTERACT));
                            sendMessage("You can fly");
                            hitler = true;
                        }
                    }
                } else {
                    sendMessage("Stop kid!");
                }
                if(hitler) {
                    getPlayer().capabilities.isFlying = true;
                    getPlayer().capabilities.setFlySpeed(2);
                    if(timeHelper.hasReached(3000, true)) {
                        hitler = false;
                        getPlayer().capabilities.isFlying = false;
                    }
                }
            }*/

            /*if (getPlayer().ticksExisted % 2 == 0)
                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + (double) 0.84F, getZ(), true));*/

            /*if(getPlayer().fallDistance > 3) {
                if(getBlockUnderPlayer(2) != Blocks.air) {
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), -4, getZ(), true));
                    sendPacket(new C03PacketPlayer(false));
                }
                getPlayer().fallDistance = 0;
            }*/

            /*if (getPlayer().fallDistance > 3) {
                sendPacketUnlogged(new C03PacketPlayer(true));
                movementUtil.blinkTo(0.3);
            }*/

           /* getTimer().timerSpeed = 0.4F;
            if(!isMoving()) {
                getTimer().timerSpeed = 1;
                return;
            }
            getPlayer().setSprinting(true);
            if (getPlayer().onGround) {
                getPlayer().motionY = 0.2;
                getPlayer().fallDistance = 1.15F;
                getPlayer().motionY -= 0.2;
            } else {
                movementUtil.setSpeed(0.17);
                getTimer().timerSpeed = 5;
            }*/

            /*getPlayer().motionY = 0;
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity.isInvisible() && entity.getDistanceToEntity(getPlayer()) < 3 && isValidEntityName(entity) && entity instanceof EntityPlayer) {
                    if (getPlayer().ticksExisted % 4 == 0)
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(entity.posX, entity.posY, entity.posZ, false));
                }
            }*/

            /*if(getPlayer().fallDistance > 4) {
                sendPacket(new C03PacketPlayer(true));
                getPlayer().fallDistance = 0;
                getPlayer().motionY = 0.6;
            }*/

            /*for (int x = -4; x < 4; x++)
                for (int y = -4; y < 4; y++)
                    for (int z = -4; z < 4; z++) {
                        final BlockPos pos = getPlayer().getPosition().add(x, y, z);
                        if (getWorld().getBlockState(pos).getBlock() != Blocks.air) {
                            if(getPlayer().ticksExisted % 10 == 0) {
                                sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                                sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                                getPlayerController().onPlayerDestroyBlock(pos, EnumFacing.DOWN);
                            }
                        }
                    }*/
            /*if (!getPlayer().onGround)
                for (int i = 0; i < 30; i++)
                    sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), true));*/
            /* final BlockPos pos = searchPos(getPlayer().getPosition().add(0,-1,0));
            System.out.println(pos);
            if (pos != null)
                getPlayerController().onPlayerRightClick(getPlayer(), getWorld(), getPlayer().inventory.getItemStack(), pos, EnumFacing.getFacingFromVector(pos.getX(), pos.getY(), pos.getZ()), new Vec3(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));*/
        }

        /*if(event instanceof BoatMotionEvent) {
            final BoatMotionEvent motionEvent = (BoatMotionEvent) event;
            motionEvent.setMotionY(0);
        }*/
        /*if (event instanceof NoClipEvent) {
         *//*getGameSettings().keyBindSneak.pressed = true;
            movementUtil.setSpeed(0.175, getYaw());*//*
            final NoClipEvent noClipEvent = (NoClipEvent) event;
            if (getWorld().getBlockState(getPlayer().getPosition()).getBlock() != Blocks.air) {
                noClipEvent.setNoClip(true);
                getPlayer().motionY = 0;
                if (getGameSettings().keyBindJump.pressed)
                    getPlayer().motionY = 0.25;
                else if (getGameSettings().keyBindSneak.pressed)
                    getPlayer().motionY = -0.25;
            }
        }*/
        /*if (event instanceof TickEvent) {
            if (getPlayer().hurtTime == 5) {
                setToggled(false);
            }
            if (getPlayer().hurtTime == 0) {
                if (getPlayer().onGround) {
                    getPlayer().jump();
                } else {
                    if(getPlayer().fallDistance > 1) {
                        boolean ground = false;
                        for (int i = 0; i < 30; i++) {
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), ground));
                            ground = !ground;
                        }
                    }
                }
            }
        }*/
    }

    public EntityLivingBase getNearest() {
        AtomicReference<EntityLivingBase> nearest = new AtomicReference<>();
        getWorld().loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && !entity.isInvisible()) {
                if (nearest.get() == null || nearest.get().getDistanceToEntity(getPlayer()) > entity.getDistanceToEntity(getPlayer()))
                    nearest.set((EntityLivingBase) entity);
            }
        });
        return nearest.get();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public BlockPos searchPos(BlockPos pos) {
        for (int x = -1; x < 1; x++)
            for (int z = -1; z < 1; z++) {
                final BlockPos find = pos.add(x, 0, z);
                if (getWorld().getBlockState(find).getBlock() == Blocks.air) {
                    return find;
                }
            }
        return null;
    }

    @Override
    public void onEnable() {
        phased = false;
        hitler = false;
        jumps = 0;
        fliegen = 0;
        packetList.clear();

        /* final PlayerCapabilities capabilities = new PlayerCapabilities();
        capabilities.isFlying = true;
        capabilities.allowFlying = true;
        capabilities.isCreativeMode = true;

        sendPacket(new C13PacketPlayerAbilities(capabilities));*/

        /*for (int i = 1; i < 6; i++)
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 5.5 * i, getZ(), false));
        for (int i = 1; i < 6; i++)
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 5.5 * 6 - 5.5 * i, getZ(), false));
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), false));
        sendPacket(new C03PacketPlayer(true));*/
    }

    @Override
    public void onDisable() {
        getTimer().timerSpeed = 1;
       /* final HashMap<String, Integer> map = new HashMap<>();
        for (Packet<?> packet : packetList) {
            final String simple = packet.getClass().getSimpleName();
            final int amount = map.containsKey(simple) ? map.get(simple) + 1 : 1;
            map.put(packet.getClass().getSimpleName(), amount);
        }
        map.forEach((s, integer) -> {
            System.out.println(s + " " + integer);
        });*/
    }
}
