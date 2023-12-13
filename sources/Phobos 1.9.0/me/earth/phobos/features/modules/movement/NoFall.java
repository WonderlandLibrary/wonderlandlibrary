package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.Timer;
import me.earth.phobos.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall
        extends Module {
    private static final Timer bypassTimer = new Timer();
    private static int ogslot = -1;
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKET));
    private final Setting<Integer> distance = this.register(new Setting<Object>("Distance", Integer.valueOf(15), Integer.valueOf(0), Integer.valueOf(50), v -> this.mode.getValue() == Mode.BUCKET));
    private final Setting<Boolean> glide = this.register(new Setting<Object>("Glide", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.ELYTRA));
    private final Setting<Boolean> silent = this.register(new Setting<Object>("Silent", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.ELYTRA));
    private final Setting<Boolean> bypass = this.register(new Setting<Object>("Bypass", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.ELYTRA));
    private final Timer timer = new Timer();
    private boolean equipped = false;
    private boolean gotElytra = false;
    private State currentState = State.FALL_CHECK;

    public NoFall() {
        super("NoFall", "Prevents fall damage.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        ogslot = -1;
        this.currentState = State.FALL_CHECK;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (NoFall.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.ELYTRA) {
            if (this.bypass.getValue().booleanValue()) {
                this.currentState = this.currentState.onSend(event);
            } else if (!this.equipped && event.getPacket() instanceof CPacketPlayer && NoFall.mc.player.fallDistance >= 3.0f) {
                RayTraceResult result = null;
                if (!this.glide.getValue().booleanValue()) {
                    result = NoFall.mc.world.rayTraceBlocks(NoFall.mc.player.getPositionVector(), NoFall.mc.player.getPositionVector().add(0.0, -3.0, 0.0), true, true, false);
                }
                if (this.glide.getValue().booleanValue() || result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (NoFall.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)) {
                        NoFall.mc.player.connection.sendPacket(new CPacketEntityAction(NoFall.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    } else if (this.silent.getValue().booleanValue()) {
                        int slot = InventoryUtil.getItemHotbar(Items.ELYTRA);
                        if (slot != -1) {
                            NoFall.mc.playerController.windowClick(NoFall.mc.player.inventoryContainer.windowId, 6, slot, ClickType.SWAP, NoFall.mc.player);
                            NoFall.mc.player.connection.sendPacket(new CPacketEntityAction(NoFall.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        }
                        ogslot = slot;
                        this.equipped = true;
                    }
                }
            }
        }
        if (this.mode.getValue() == Mode.PACKET && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            packet.onGround = true;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (NoFall.fullNullCheck()) {
            return;
        }
        if ((this.equipped || this.bypass.getValue().booleanValue()) && this.mode.getValue() == Mode.ELYTRA && (event.getPacket() instanceof SPacketWindowItems || event.getPacket() instanceof SPacketSetSlot)) {
            if (this.bypass.getValue().booleanValue()) {
                this.currentState = this.currentState.onReceive(event);
            } else {
                this.gotElytra = true;
            }
        }
    }

    @Override
    public void onUpdate() {
        if (NoFall.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.ELYTRA) {
            int slot;
            if (this.bypass.getValue().booleanValue()) {
                this.currentState = this.currentState.onUpdate();
            } else if (this.silent.getValue().booleanValue() && this.equipped && this.gotElytra) {
                NoFall.mc.playerController.windowClick(NoFall.mc.player.inventoryContainer.windowId, 6, ogslot, ClickType.SWAP, NoFall.mc.player);
                NoFall.mc.playerController.updateController();
                this.equipped = false;
                this.gotElytra = false;
            } else if (this.silent.getValue().booleanValue() && InventoryUtil.getItemHotbar(Items.ELYTRA) == -1 && (slot = InventoryUtil.findStackInventory(Items.ELYTRA)) != -1 && ogslot != -1) {
                System.out.println(String.format("Moving %d to hotbar %d", slot, ogslot));
                NoFall.mc.playerController.windowClick(NoFall.mc.player.inventoryContainer.windowId, slot, ogslot, ClickType.SWAP, NoFall.mc.player);
                NoFall.mc.playerController.updateController();
            }
        }
    }

    @Override
    public void onTick() {
        Vec3d posVec;
        RayTraceResult result;
        if (NoFall.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.BUCKET && NoFall.mc.player.fallDistance >= (float) this.distance.getValue().intValue() && !EntityUtil.isAboveWater(NoFall.mc.player) && this.timer.passedMs(100L) && (result = NoFall.mc.world.rayTraceBlocks(posVec = NoFall.mc.player.getPositionVector(), posVec.add(0.0, -5.33f, 0.0), true, true, false)) != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumHand hand = EnumHand.MAIN_HAND;
            if (NoFall.mc.player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET) {
                hand = EnumHand.OFF_HAND;
            } else if (NoFall.mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET) {
                for (int i = 0; i < 9; ++i) {
                    if (NoFall.mc.player.inventory.getStackInSlot(i).getItem() != Items.WATER_BUCKET) continue;
                    NoFall.mc.player.inventory.currentItem = i;
                    NoFall.mc.player.rotationPitch = 90.0f;
                    this.timer.reset();
                    return;
                }
                return;
            }
            NoFall.mc.player.rotationPitch = 90.0f;
            NoFall.mc.playerController.processRightClick(NoFall.mc.player, NoFall.mc.world, hand);
            this.timer.reset();
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public enum State {
        FALL_CHECK {
            @Override
            public State onSend(PacketEvent.Send event) {
                RayTraceResult result = Util.mc.world.rayTraceBlocks(Util.mc.player.getPositionVector(), Util.mc.player.getPositionVector().add(0.0, -3.0, 0.0), true, true, false);
                if (event.getPacket() instanceof CPacketPlayer && Util.mc.player.fallDistance >= 3.0f && result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    int slot = InventoryUtil.getItemHotbar(Items.ELYTRA);
                    if (slot != -1) {
                        Util.mc.playerController.windowClick(Util.mc.player.inventoryContainer.windowId, 6, slot, ClickType.SWAP, Util.mc.player);
                        ogslot = slot;
                        Util.mc.player.connection.sendPacket(new CPacketEntityAction(Util.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        return WAIT_FOR_ELYTRA_DEQUIP;
                    }
                    return this;
                }
                return this;
            }
        },
        WAIT_FOR_ELYTRA_DEQUIP {
            @Override
            public State onReceive(PacketEvent.Receive event) {
                if (event.getPacket() instanceof SPacketWindowItems || event.getPacket() instanceof SPacketSetSlot) {
                    return REEQUIP_ELYTRA;
                }
                return this;
            }
        },
        REEQUIP_ELYTRA {
            @Override
            public State onUpdate() {
                Util.mc.playerController.windowClick(Util.mc.player.inventoryContainer.windowId, 6, ogslot, ClickType.SWAP, Util.mc.player);
                Util.mc.playerController.updateController();
                int slot = InventoryUtil.findStackInventory(Items.ELYTRA, true);
                if (slot == -1) {
                    Command.sendMessage("\u00a7cElytra not found after regain?");
                    return WAIT_FOR_NEXT_REQUIP;
                }
                Util.mc.playerController.windowClick(Util.mc.player.inventoryContainer.windowId, slot, ogslot, ClickType.SWAP, Util.mc.player);
                Util.mc.playerController.updateController();
                bypassTimer.reset();
                return RESET_TIME;
            }
        },
        WAIT_FOR_NEXT_REQUIP {
            @Override
            public State onUpdate() {
                if (bypassTimer.passedMs(250L)) {
                    return REEQUIP_ELYTRA;
                }
                return this;
            }
        },
        RESET_TIME {
            @Override
            public State onUpdate() {
                if (Util.mc.player.onGround || bypassTimer.passedMs(250L)) {
                    Util.mc.player.connection.sendPacket(new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, new ItemStack(Blocks.BEDROCK), (short) 1337));
                    return FALL_CHECK;
                }
                return this;
            }
        };


        public State onSend(PacketEvent.Send e) {
            return this;
        }

        public State onReceive(PacketEvent.Receive e) {
            return this;
        }

        public State onUpdate() {
            return this;
        }
    }

    public enum Mode {
        PACKET,
        BUCKET,
        ELYTRA

    }
}

