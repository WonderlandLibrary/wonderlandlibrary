/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.EventPostMotionUpdates;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.RotationUtils;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Aura
extends Module {
    private boolean players = true;
    private boolean monsters;
    private boolean animals = true;
    private boolean bats;
    private boolean friend;
    private boolean knockback;
    private boolean noArmor;
    private static boolean fakesprint;
    private boolean criticals = true;
    private boolean autoBlock = true;
    private boolean noSwing;
    private boolean dura;
    private boolean angle = true;
    private boolean lockview;
    private double speed = 8.0;
    private double range = 4.25;
    private double blockRange = 8.0;
    private Timer pseudoTimer = new Timer();
    private Timer angleTimer = new Timer();
    private static EntityLivingBase target;
    public static EntityLivingBase pseudoTarget;

    public Aura() {
        super("LucidAura", 0, Category.COMBAT, 0, true, "fuklife", new String[]{"aur"});
    }

    @EventTarget
    private void onTick(EventTick event) {
        Character colorFormatCharacter = new Character('\u00a7');
        this.setDisplayName("LucidAura\u00a77 - Tick");
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        event.setOnGround(true);
        target = null;
        ArrayList<EntityLivingBase> attackableEntities = new ArrayList<EntityLivingBase>();
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            EntityLivingBase entityLivingBase;
            if (!(o instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = entityLivingBase = (EntityLivingBase)o;
            --entityLivingBase.auraTicks;
            if (!this.checkValidity(entity) || (entity.auraTicks != 10 || this.dura) && entity.auraTicks != 9 && entity.auraTicks > 0) continue;
            attackableEntities.add(entity);
        }
        Collections.sort(attackableEntities, new Comparator<EntityLivingBase>(){

            @Override
            public int compare(EntityLivingBase o1, EntityLivingBase o2) {
                return o1.auraTicks - o2.auraTicks;
            }
        });
        for (EntityLivingBase entity2 : attackableEntities) {
            if ((pseudoTarget == null || pseudoTarget != entity2) && !this.angleTimer.delay(150.0f)) continue;
            if (pseudoTarget == null || pseudoTarget != entity2) {
                this.angleTimer.reset();
            }
            pseudoTarget = Aura.target = entity2;
            break;
        }
        if (pseudoTarget != null && !this.checkValidity(pseudoTarget)) {
            pseudoTarget = null;
        }
        if (pseudoTarget == null) {
            return;
        }
        float[] rotations = RotationUtils.getRotations(pseudoTarget);
        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);
        if (this.lockview) {
            Wrapper.mc.thePlayer.rotationYaw = rotations[0];
            Wrapper.mc.thePlayer.rotationPitch = rotations[1];
            return;
        }
    }

    @EventTarget
    public void postMotion(EventPostMotionUpdates update) {
        if (target != null) {
            boolean sprint;
            boolean fakeSprint;
            Cheese.getInstance();
            boolean bl2 = fakeSprint = Cheese.moduleManager.getModbyName("Sprint").getState() && fakesprint;
            if (Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            if (!fakeSprint) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            if (this.angle) {
                if (this.dura) {
                    if (Aura.target.auraTicks != 10) {
                        this.swap(9, Wrapper.mc.thePlayer.inventory.currentItem);
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        this.attack(target, false);
                        this.attack(target, false);
                        this.attack(target, true);
                        this.swap(9, Wrapper.mc.thePlayer.inventory.currentItem);
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        this.attack(target, false);
                        this.attack(target, true);
                    }
                } else {
                    this.attack(target, this.criticals);
                    this.attack(target, this.criticals);
                }
            } else if (this.dura) {
                this.attack(target, false);
                if (Aura.target.auraTicks != 10) {
                    this.attack(target, this.criticals);
                }
            } else {
                this.attack(target, this.criticals);
            }
            if (Aura.target.auraTicks != 10) {
                Aura.target.auraTicks = 20;
            }
            if ((sprint = Wrapper.mc.thePlayer.isSprinting()) && !fakeSprint) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Wrapper.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            }
        } else if (pseudoTarget != null && this.pseudoTimer.delay((float)(1000.0 / this.speed))) {
            this.fakeAttack(pseudoTarget);
            this.pseudoTimer.reset();
        }
        double oldRange = this.range;
        this.range = this.blockRange;
        int enemiesArmound = 0;
        for (Object o2 : Wrapper.mc.theWorld.loadedEntityList) {
            EntityLivingBase entity3;
            if (!(o2 instanceof EntityLivingBase) || !this.checkValidity(entity3 = (EntityLivingBase)o2)) continue;
            ++enemiesArmound;
        }
        this.range = oldRange;
        if (enemiesArmound > 0 && Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (Wrapper.mc.gameSettings.keyBindUseItem.pressed || this.autoBlock)) {
            Wrapper.mc.thePlayer.setItemInUse(Wrapper.mc.thePlayer.getHeldItem(), Wrapper.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
            return;
        }
    }

    protected void swap(int slot, int hotbarNum) {
        Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Wrapper.mc.thePlayer);
    }

    private void fakeAttack(EntityLivingBase ent) {
        boolean vanillaCrit;
        this.fakeSwingItem();
        float sharpLevel = EnchantmentHelper.func_152377_a(Wrapper.mc.thePlayer.getHeldItem(), ent.getCreatureAttribute());
        boolean bl2 = vanillaCrit = Wrapper.mc.thePlayer.fallDistance > 0.0f && !Wrapper.mc.thePlayer.onGround && !Wrapper.mc.thePlayer.isOnLadder() && !Wrapper.mc.thePlayer.isInWater() && !Wrapper.mc.thePlayer.isPotionActive(Potion.blindness) && Wrapper.mc.thePlayer.ridingEntity == null;
        if (this.criticals || vanillaCrit) {
            Wrapper.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            Wrapper.mc.thePlayer.onEnchantmentCritical(ent);
        }
        this.pseudoTimer.reset();
    }

    private void attack(EntityLivingBase ent, boolean crit) {
        this.swingItem();
        if (crit) {
            this.crit();
        } else {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
        }
        float sharpLevel = EnchantmentHelper.func_152377_a(Wrapper.mc.thePlayer.getHeldItem(), ent.getCreatureAttribute());
        boolean vanillaCrit = Wrapper.mc.thePlayer.fallDistance > 0.0f && !Wrapper.mc.thePlayer.onGround && !Wrapper.mc.thePlayer.isOnLadder() && !Wrapper.mc.thePlayer.isInWater() && !Wrapper.mc.thePlayer.isPotionActive(Potion.blindness) && Wrapper.mc.thePlayer.ridingEntity == null;
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)ent, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Wrapper.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            Wrapper.mc.thePlayer.onEnchantmentCritical(ent);
        }
    }

    private void fakeSwingItem() {
    }

    private void swingItem() {
        if (this.noSwing) {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        } else {
            Wrapper.mc.thePlayer.swingItem();
        }
    }

    private void crit() {
        double posY = Wrapper.mc.thePlayer.posY;
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY + 0.0625, Wrapper.mc.thePlayer.posZ, true));
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY, Wrapper.mc.thePlayer.posZ, false));
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY + 1.1E-5, Wrapper.mc.thePlayer.posZ, false));
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.mc.thePlayer.posX, posY, Wrapper.mc.thePlayer.posZ, false));
    }

    private boolean checkValidity(EntityLivingBase entity) {
        if (entity == Wrapper.mc.thePlayer) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }
        if ((double)Wrapper.mc.thePlayer.getDistanceToEntity(entity) > this.range) {
            return false;
        }
        if (!(entity instanceof EntityPlayer)) {
            if (!(this.monsters && entity instanceof EntityMob || this.animals && (entity instanceof EntityAnimal || entity instanceof EntitySquid) || this.bats && entity instanceof EntityBat)) {
                return false;
            }
            return true;
        }
        if (this.players) {
            EntityPlayer player = (EntityPlayer)entity;
            if (!(this.friend && FriendManager.isFriend(player.getName()) || !FriendManager.isFriend(player.getName()) && (!this.noArmor || this.hasArmor(player)))) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean hasArmor(EntityPlayer player) {
        ItemStack boots = player.inventory.armorInventory[0];
        ItemStack pants = player.inventory.armorInventory[1];
        ItemStack chest = player.inventory.armorInventory[2];
        ItemStack head = player.inventory.armorInventory[3];
        if (boots == null && pants == null && chest == null && head == null) {
            return false;
        }
        return true;
    }

}

