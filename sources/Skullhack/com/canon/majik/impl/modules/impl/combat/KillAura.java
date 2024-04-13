package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.PlayerUpdateEvent;
import com.canon.majik.api.event.events.Render3DEvent;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.api.utils.player.rotation.Rotation;
import com.canon.majik.api.utils.player.rotation.RotationUtil;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

import java.util.Comparator;

public class KillAura extends Module {

    BooleanSetting players = setting("Players", false);
    BooleanSetting coolDown = setting("CoolDown", false);
    BooleanSetting packet = setting("Packet", false);
    NumberSetting range = setting("Range", 5, 1, 6);
    BooleanSetting swordCheck = setting("SwordCheck", false);
    BooleanSetting render = setting("Render", false);
    ColorSetting color = setting("Color", new TColor(255,0,0,100));
    Rotation spoofed = new Rotation(0,0);
    public static KillAura instance;

    public KillAura(String name, Category category) {
        super(name, category);
        instance = this;
    }
    public Entity target;

    @EventListener
    public void onUpdate(PlayerUpdateEvent event){
        if(nullCheck()) return;
        target = mc.world.loadedEntityList.stream()
                .filter(entity -> entity != mc.player)
                .min(Comparator.comparing(entity -> entity.getDistance(mc.player)))
                .filter(entity -> (players.getValue() ? entity instanceof EntityPlayer : entity instanceof EntityAnimal))
                .filter(entity -> entity.getDistance(mc.player) <= range.getValue().floatValue())
                .orElse(null);

        if(target != null){
            if(target.isEntityAlive()){
                rotateOnly(event,target);
                if(swordCheck.getValue()){
                    if(weaponCheck(mc.player.inventory.getCurrentItem().getItem())){
                        attack(coolDown.getValue(),packet.getValue());
                    }
                }else {
                    attack(coolDown.getValue(),packet.getValue());
                }
            }
        }
    }

    @EventListener
    public void onRender(Render3DEvent event){
        if(target != null && render.getValue()){
            RenderUtils.renderBox(target.getEntityBoundingBox(),true,false,color.getValue());
        }
    }

    private void rotateOnly(PlayerUpdateEvent event, Entity safe) {
        spoofed = RotationUtil.faceEntity(safe, event, spoofed, 45);
    }

    public boolean weaponCheck(Item item){
        if(item instanceof ItemSword){
            return true;
        }else if(item instanceof ItemAxe){
            return true;
        }else{
            return false;
        }
    }

    public void attack(boolean cooldown, boolean packet){
        if(cooldown){
            if(mc.player.getCooledAttackStrength(0.5f) >= 1) {
                if (packet) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(target));
                    mc.player.connection.sendPacket(new CPacketAnimation());
                } else {
                    mc.playerController.attackEntity(mc.player, target);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }else{
            if (packet) {
                mc.player.connection.sendPacket(new CPacketUseEntity(target));
                mc.player.connection.sendPacket(new CPacketAnimation());
            } else {
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}
