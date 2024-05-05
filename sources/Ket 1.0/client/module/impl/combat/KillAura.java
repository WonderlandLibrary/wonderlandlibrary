package client.module.impl.combat;

import client.Client;
import client.bot.BotManager;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.UpdateEvent;
import client.event.impl.other.WorldLoadEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.ModuleManager;
import client.module.impl.other.AntiBot;
import client.util.ChatUtil;
import client.util.RotationUtil;
import client.value.impl.BooleanValue;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import client.value.impl.SubMode;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.util.vector.Vector2f;
import tv.twitch.chat.Chat;

import java.util.concurrent.atomic.AtomicBoolean;

@ModuleInfo(name = "Kill Aura", description = "Automatically attacks nearby entities", category = Category.COMBAT)
public class KillAura extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Single"))
            .add(new SubMode("Multi"))
            .setDefault("Single");
    private final ModeValue clickMode = new ModeValue("Click Mode", this)
            .add(new SubMode("Normal"))
            .add(new SubMode("Hit Select"))
            .setDefault("Normal");
    private final NumberValue range = new NumberValue("Range", this, 3, 0.1, 6, 0.1);
    private final BooleanValue throughWalls = new BooleanValue("Through Walls", this, false);
    @Getter
    private final ModeValue blockMode = new ModeValue("Block Mode", this)
            .add(new SubMode("None"))
            .add(new SubMode("Fake"))
            .add(new SubMode("Vanilla"))
            .setDefault("None");
    @Getter
    private boolean blocking;
    @Getter
    private final BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, false);
    @Getter
    private EntityLivingBase target;
    private final ModeValue rotationMode = new ModeValue("Rotation Mode", this)
            .add(new SubMode("None"))
            .add(new SubMode("Normal"))
            .add(new SubMode("GCD"))
            .setDefault("GCD");
    private final BooleanValue players = new BooleanValue("Players", this, true), mobs = new BooleanValue("Mobs", this, false), animals = new BooleanValue("Animals", this, false);

    private int attackTicks;

    @Override
    protected void onEnable() {
        attackTicks = 0;
    }

    @Override
    protected void onDisable() {
        RotationUtil.setRotation(null);
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        target = null;
        blocking = false;
        final AtomicBoolean b = new AtomicBoolean();
        mc.theWorld.loadedEntityList.stream().filter(entity -> {
            final double deltaX = mc.thePlayer.posX - entity.posX, deltaY = mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight() - entity.getEntityBoundingBox().minY - mc.thePlayer.getEyeHeight(), deltaZ = mc.thePlayer.posZ - entity.posZ;
            final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
            final BotManager botManager = Client.INSTANCE.getBotManager();
            return entity != mc.thePlayer && (entity instanceof EntityPlayer && players.getValue() || entity instanceof EntityMob && mobs.getValue() || entity instanceof EntityAnimal && animals.getValue()) && entity.isEntityAlive() && !entity.isInvisible() && Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) <= range.getValue().doubleValue() && (throughWalls.getValue() || ((EntityLivingBase) entity).canEntityBeSeen(mc.thePlayer)) && (!moduleManager.get(AntiBot.class).isEnabled() || !botManager.contains(entity)) && (!moduleManager.get(Friends.class).isEnabled() || !botManager.getFriends().contains(entity.getName())) && (mode.getValue().getName().equals("Multi") || !b.get());
        }).forEach(entity -> {
            target = (EntityLivingBase) entity;
            switch (clickMode.getValue().getName()) {
                case "Normal": {
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, entity);
                }
                case "Hit Select": {
                    if (attackTicks == 0) {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer, entity);
                        attackTicks = 10;
                    } else attackTicks--;
                    break;
                }
            }
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                switch (blockMode.getValue().getName()) {
                    case "Fake": {
                        blocking = true;
                        break;
                    }
                    case "Vanilla": {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                        break;
                    }
                }
            }
            b.set(true);
        });
        if (!b.get()) RotationUtil.setRotation(null);
    };

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (rotationMode.getValue().getName().equals("None") || target == null) return;
        final Vector2f rotation;
        switch (rotationMode.getValue().getName()) {
            case "Normal": {
                rotation = RotationUtil.get(target);

                break;
            }
            case "GCD": {
                rotation = RotationUtil.getGCD(target);
                break;
            }
            default: {
                rotation = null;
                break;
            }
        }
        RotationUtil.setRotation(rotation);
        assert rotation != null;
        event.setPitch(rotation.x);
        event.setYaw(rotation.y);
    };

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> toggle();
}
