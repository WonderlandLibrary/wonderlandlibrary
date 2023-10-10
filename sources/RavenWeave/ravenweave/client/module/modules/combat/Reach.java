package ravenweave.client.module.modules.combat;

import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DoubleSliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class Reach extends Module {
    public static DoubleSliderSetting reach;
    public static TickSetting weapon_only;
    public static TickSetting moving_only;
    public static TickSetting sprint_only;
    public static KillAura killAura;

    public Reach() {
        super("Reach", ModuleCategory.combat);
        this.registerSetting(reach = new DoubleSliderSetting("Reach (Blocks)", 3.1, 3.3, 3, 6, 0.05));
        this.registerSetting(weapon_only = new TickSetting("Weapon only", false));
        this.registerSetting(moving_only = new TickSetting("Moving only", false));
        this.registerSetting(sprint_only = new TickSetting("Sprint only", false));
    }

    @Override
    public void postApplyConfig() {
       killAura = (KillAura) Raven.moduleManager.getModuleByClazz(KillAura.class);
    }

    public static double getReach() {
        killAura = (KillAura) Raven.moduleManager.getModuleByClazz(KillAura.class);
        if(killAura != null && killAura.isEnabled()) {
            return KillAura.reach.getInput();
        }

        double normal = mc.playerController.extendedReach()? 5 : 3;

        if (!Utils.Player.isPlayerInGame() || (weapon_only.isToggled() && !Utils.Player.isPlayerHoldingWeapon()))
            return normal;

        if (moving_only.isToggled() && ((double) mc.thePlayer.moveForward == 0.0D)
                && ((double) mc.thePlayer.moveStrafing == 0.0D))
            return normal;

        if (sprint_only.isToggled() && !mc.thePlayer.isSprinting())
            return normal;

        return Utils.Client.ranModuleVal(reach, Utils.Java.rand()) + (mc.playerController.extendedReach()? 2 : 0);
    }
}
