package tech.atani.client.feature.module.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Mouse;

import tech.atani.client.feature.combat.CombatManager;
import tech.atani.client.feature.combat.interfaces.IgnoreList;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;

import java.util.ArrayList;
import java.util.List;

@ModuleData(name = "MCF", description = "Friend other players with a mouse click", category = Category.MISCELLANEOUS)
public class MCF extends Module implements IgnoreList {
    private final List<Entity> friends = new ArrayList<>();

    private boolean clicked;

    public MCF() {
        CombatManager.getInstance().addIgnoreList(this);
    }

    @Listen
    public void onUpdateMotion(UpdateMotionEvent updateMotionEvent) {
        if(Mouse.isButtonDown(2)) {
            if(Methods.mc.pointedEntity != null && !clicked && Methods.mc.pointedEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) Methods.mc.pointedEntity;

                String message;

                if(friends.contains(Methods.mc.pointedEntity)) {
                    friends.remove(player);
                    message = "§c" + player.getCommandSenderName() + " is no longer a friend!";
                } else {
                    friends.add(player);
                    message = "§a" + player.getCommandSenderName() + " is now a friend!";
                }

                this.sendMessage(message, true);
            }
            clicked = true;
        } else {
            clicked = false;
        }
    }

    @Override
    public void onEnable() {
        this.clicked = false;
    }

    @Override
    public void onDisable() {
        this.clicked = false;
    }

    @Override
    public boolean shouldSkipEntity(Entity entity) {
        return friends.contains(entity);
    }
}
