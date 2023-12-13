package vestige.module.impl.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import vestige.Vestige;
import vestige.anticheat.ACPlayer;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.util.misc.LogUtil;

public class Antibot extends Module {

    private final IntegerSetting ticksExisted = new IntegerSetting("Ticks existed", 30, 0, 100, 5);
    public final BooleanSetting advancedDetection = new BooleanSetting("Advanced detection", true);

    public final BooleanSetting debug = new BooleanSetting("Debug", false);

    private Killaura killauraModule;

    public Antibot() {
        super("Antibot", Category.COMBAT);
        this.addSettings(ticksExisted, advancedDetection, debug);
    }

    @Override
    public void onClientStarted() {
        killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    public boolean canAttack(EntityLivingBase entity, Module module) {
        if(!this.isEnabled()) return true;

        if(entity.ticksExisted < ticksExisted.getValue()) {
            if(debug.isEnabled() && module == killauraModule) {
                LogUtil.addChatMessage("Ticks existed antibot : prevented from hitting : " + entity.ticksExisted);
            }

            return false;
        }

        if(entity instanceof EntityPlayer) {
            ACPlayer player = Vestige.instance.getAnticheat().getACPlayer((EntityPlayer) entity);

            if(player != null && player.isBot()) {
                if(debug.isEnabled() && module == killauraModule) {
                    LogUtil.addChatMessage("Advanced antibot : prevented from hitting");
                }
                return false;
            }
        }

        return true;
    }

}