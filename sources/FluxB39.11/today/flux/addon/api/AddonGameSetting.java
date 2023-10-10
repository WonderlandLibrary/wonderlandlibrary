package today.flux.addon.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import today.flux.addon.api.utils.Keybind;

/**
 * 游戏状态与设置获取
 */
public class AddonGameSetting {
    public static AddonGameSetting getGameSettings() {
        return new AddonGameSetting();
    }

    public static Minecraft mc = Minecraft.getMinecraft();

    /**
     * 获取玩家视角状态
     *
     * @return 视角状态ID
     */

    public int getThirdPersonViewState() {
        return mc.gameSettings.thirdPersonView;
    }

    /**
     * 设定玩家视角状态
     *
     * @param personView 视角状态ID 1=第一人称视角 2=第二人称视角 3=第三人称视角
     */

    public void setThirdPersonView(int personView) {
        mc.gameSettings.thirdPersonView = personView;
    }

    /**
     * 判断按钮按下
     *
     * @param key 按键类型
     * @return boolean类型判断结果
     */

    public boolean isPressed(Keybind key) {
        GameSettings gameSettings = mc.gameSettings;
        if (key == Keybind.ATTACK) {
            return gameSettings.keyBindAttack.pressed;
        } else if (key == Keybind.USE_ITEM) {
            return gameSettings.keyBindUseItem.pressed;
        } else if (key == Keybind.FORWARD) {
            return gameSettings.keyBindForward.pressed;
        } else if (key == Keybind.BACK) {
            return gameSettings.keyBindBack.pressed;
        } else if (key == Keybind.LEFT) {
            return gameSettings.keyBindLeft.pressed;
        } else if (key == Keybind.RIGHT) {
            return gameSettings.keyBindRight.pressed;
        } else if (key == Keybind.PLAYER_LIST) {
            return gameSettings.keyBindPlayerList.pressed;
        } else if (key == Keybind.SPRINT) {
            return gameSettings.keyBindSprint.pressed;
        } else if (key == Keybind.JUMP) {
            return gameSettings.keyBindJump.pressed;
        } else if (key == Keybind.SNEAK) {
            return gameSettings.keyBindSneak.pressed;
        } else {
            return false;
        }
    }

    /**
     * 设置某个按钮的状态
     *
     * @param key     按键类型
     * @param pressed 设定值
     */

    public void setPressed(Keybind key, boolean pressed) {
        GameSettings gameSettings = mc.gameSettings;
        if (key == Keybind.ATTACK) {
            gameSettings.keyBindAttack.pressed = pressed;
        } else if (key == Keybind.USE_ITEM) {
            gameSettings.keyBindUseItem.pressed = pressed;
        } else if (key == Keybind.FORWARD) {
            gameSettings.keyBindForward.pressed = pressed;
        } else if (key == Keybind.BACK) {
            gameSettings.keyBindBack.pressed = pressed;
        } else if (key == Keybind.LEFT) {
            gameSettings.keyBindLeft.pressed = pressed;
        } else if (key == Keybind.RIGHT) {
            gameSettings.keyBindRight.pressed = pressed;
        } else if (key == Keybind.PLAYER_LIST) {
            gameSettings.keyBindPlayerList.pressed = pressed;
        } else if (key == Keybind.SPRINT) {
            gameSettings.keyBindSprint.pressed = pressed;
        } else if (key == Keybind.JUMP) {
            gameSettings.keyBindJump.pressed = pressed;
        } else if (key == Keybind.SNEAK) {
            gameSettings.keyBindSneak.pressed = pressed;
        }
    }

    /**
     * 获取鼠标灵敏度
     *
     * @return float类型灵敏度值
     */

    public float getMouseSensitivity() {
        return mc.gameSettings.mouseSensitivity;
    }

    /**
     * 设置鼠标灵敏度
     *
     * @param sensitivity float类型灵敏度值
     */

    public void setMouseSensitivity(float sensitivity) {
        mc.gameSettings.mouseSensitivity = sensitivity;
    }

    /**
     * 获取是否开启视角摇晃
     *
     * @return boolean类型的判断值
     */

    public boolean isViewBobbing() {
        return mc.gameSettings.viewBobbing;
    }

    /**
     * 设置是否开启视角摇晃
     *
     * @param stage boolean类型的值
     */

    public void setViewBobbing(boolean stage) {
        mc.gameSettings.viewBobbing = stage;
    }

    /**
     * 设置当前TimerSpeed
     *
     * @param speed 要设置的TimerSpeed
     */

    public void setTimerSpeed(float speed) {
        mc.timer.timerSpeed = speed;
    }

    /**
     * 获取当前TimerSpeed
     *
     * @return 当前TimerSpeed
     */

    public float getTimerSpeed() {
        return mc.timer.timerSpeed;
    }
}
