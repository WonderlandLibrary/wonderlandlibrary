package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.FloatProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.exploits.Blink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

public final class Freecam extends AbstractModule {

    /* fields */
    private EntityOtherPlayerMP freecamEntity;

    /* properties @off */
    @Property("freecam-speed")
    private final FloatProperty freecamSpeed = PropertyFactory.createFloat(4.0F).minimum(1.0F).maximum(5.0F);

    /* constructors @on */
    public Freecam(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Freecam", "Freecam", Keyboard.KEY_NONE, EnumModuleType.MOVEMENT, "Ghost Walking");
        Manager.put(new Setting("FC_SPEED", "Speed", SettingType.SLIDER, this, this.freecamSpeed, 1.0F));
    }

    /* methods */
    @Override
    public void onDisable() {
        if (this.freecamEntity != null) {
            this.mc.player.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ,
                    this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
            this.mc.world.removeEntityFromWorld(this.freecamEntity.getEntityID());
        }
        this.mc.player.noClip = false;
    }

    @Override
    public void onEnable() {
        if (this.mc.player == null) return;
        checkModule(Blink.class);
        this.freecamEntity = new EntityOtherPlayerMP(this.mc.world, mc.player.getGameProfile());
        this.freecamEntity.inventory = this.mc.player.inventory;
        this.freecamEntity.inventoryContainer = this.mc.player.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(this.mc.player.posX, this.mc.player.posY,
                this.mc.player.posZ, this.mc.player.rotationYaw, this.mc.player.rotationPitch);
        this.freecamEntity.rotationYawHead = this.mc.player.rotationYawHead;
        this.freecamEntity.setEntityUniqueID(this.mc.player.getEntityUniqueID());
        this.mc.world.addEntityToWorld(this.freecamEntity.getEntityID(), this.freecamEntity);
    }

    @EventTarget
    public void onEvent(MotionUpdateEvent event) {
        if(event.getState().equals(MotionUpdateEvent.State.PRE)) {
            this.mc.player.noClip = true;
        }
    }

    @EventTarget
    public void onEvent(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onEvent(CollideWithBlockEvent event) {
        event.setBoundingBox(null);
    }

    @EventTarget
    public void onEvent(MoveEvent event) {
        float speed = this.freecamSpeed.get();
        if (this.mc.player.movementInput().jump()) {
            event.setY(this.mc.player.motionY = speed);
        } else if (this.mc.player.movementInput().sneak()) {
            event.setY(this.mc.player.motionY = -speed);
        } else {
            event.setY(this.mc.player.motionY = 0.0D);
        }

        event.setMoveSpeed(speed);
    }

    @EventTarget
    public void onEvent(PushBlockEvent event) {
        event.setCancelled(true);
    }

    //region Lombok
    public EntityOtherPlayerMP getFreecamEntity() {
        return this.freecamEntity;
    }
    //endregion

}
