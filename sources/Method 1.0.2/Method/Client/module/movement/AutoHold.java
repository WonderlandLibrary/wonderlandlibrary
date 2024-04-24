package Method.Client.module.movement;


import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.MoveOverride;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public class AutoHold extends Module {
    private static boolean Toggled;
    private static boolean Unloaded = false;
    public Setting unloadedChunk;
    public static Setting w;
    public static Setting a;
    public static Setting s;
    public static Setting d;
    public static Setting lc;
    public static Setting rc;
    public static Setting Space;

    public static boolean runthething() {
        if (Toggled) {
            if (Unloaded)
                return false;
            mc.player.movementInput.moveStrafe = 0.0F;
            mc.player.movementInput.moveForward = 0.0F;


            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), w.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
            if (w.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                ++mc.player.movementInput.moveForward;
                mc.player.movementInput.forwardKeyDown = true;
            } else {
                mc.player.movementInput.forwardKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), s.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            if (s.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                --mc.player.movementInput.moveForward;
                mc.player.movementInput.backKeyDown = true;
            } else {
                mc.player.movementInput.backKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), a.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
            if (a.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                ++mc.player.movementInput.moveStrafe;
                mc.player.movementInput.leftKeyDown = true;
            } else {
                mc.player.movementInput.leftKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), d.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            if (d.getValBoolean() || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                --mc.player.movementInput.moveStrafe;
                mc.player.movementInput.rightKeyDown = true;
            } else {
                mc.player.movementInput.rightKeyDown = false;
            }

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
            mc.player.movementInput.jump = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());


            mc.player.movementInput.jump = Space.getValBoolean();
            return true;
        }
        return false;
    }


    @Override
    public void setup() {
        setmgr.add(unloadedChunk = new Setting("Stop for unloaded", this, false));
        setmgr.add(w = new Setting("w", this, true));
        setmgr.add(a = new Setting("a", this, false));
        setmgr.add(s = new Setting("s", this, false));
        setmgr.add(d = new Setting("d", this, false));
        setmgr.add(lc = new Setting("lc", this, false));
        setmgr.add(rc = new Setting("rc", this, false));
        setmgr.add(Space = new Setting("Space", this, false));
    }

    public AutoHold() {
        super("AutoHold", Keyboard.KEY_NONE, Category.MOVEMENT, "Auto Walk + More!");
    }

    @Override
    public void onEnable() {
        MoveOverride.toggle();
        Toggled = true;
    }

    @Override
    public void onDisable() {
        Toggled = false;
    }


    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (unloadedChunk.getValBoolean()) {
            Unloaded = !mc.world.getChunk(mc.player.getPosition()).isLoaded();
        }
    }


}
