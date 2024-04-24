package me.crimp.claudius.mod.modules.misc;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class McfSync
        extends Module {
    public static final McfSync INSTANCE = new McfSync();
    private boolean clicked = false;
    public Setting<Boolean> one = register(new Setting<>("1", true));
    public Setting<Boolean> two = register(new Setting<>("2", true));
    public Setting<Boolean> three = register(new Setting<>("3", false));
    public Setting<String> syncadd1 = register(new Setting<String>("Syncadd1",".Friend", v -> this.one.getValue()));
    public Setting<String> syncadd2 = register(new Setting<String>("Syncadd2","*Friend", v -> this.two.getValue()));
    public Setting<String> syncdel1 = register(new Setting<String>("Syncdel1",".Friend", v -> this.one.getValue()));
    public Setting<String> syncdel2 = register(new Setting<String>("Syncdel2","*Friend", v -> this.two.getValue()));
    public Setting<String> syncadd3 = register(new Setting<String>("Syncadd3","@Friend", v -> this.three.getValue()));
    public Setting<String> syncdel3 = register(new Setting<String>("Syncdel3","@Friend", v -> this.three.getValue()));
    public McfSync() {
        super("McfSync", "Middle click Friend sync", Category.Misc, false, false);
    }

    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && McfSync.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = McfSync.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (claudius.friendManager.isFriend(entity.getName())) {
                claudius.friendManager.removeFriend(entity.getName());
                Command.sendMessage(entity.getName() + " has been unfriended.");
                if (one.getValue()) {
                    mc.player.sendChatMessage(this.syncdel1.getValue() + " del " + entity.getName());
                } if (two.getValue()) {
                    mc.player.sendChatMessage(this.syncdel2.getValue() + " del " + entity.getName());
                }  if (three.getValue()) {
                    mc.player.sendChatMessage(this.syncdel3.getValue() + " del " + entity.getName());
                }
            } else {
                claudius.friendManager.addFriend(entity.getName());
                Command.sendMessage(entity.getName() + " has been friended.");
                if (one.getValue()) {
                    mc.player.sendChatMessage(this.syncadd1.getValue() + " add " + entity.getName());
                } if (two.getValue()) {
                    mc.player.sendChatMessage(this.syncadd2.getValue() + " add " + entity.getName());
                }  if (three.getValue()) {
                    mc.player.sendChatMessage(this.syncadd3.getValue() + " add " + entity.getName());
                }
            }
        }
        this.clicked = true;
    }
}

