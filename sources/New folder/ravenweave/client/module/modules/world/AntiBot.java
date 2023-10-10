package ravenweave.client.module.modules.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.player.Freecam;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

import java.util.HashMap;

public class AntiBot extends Module {
    private static final HashMap<EntityPlayer, Long> newEnt = new HashMap<>();
    private final long ms = 4000L;
    public static TickSetting a, dead;

    public AntiBot() {
        super("AntiBot", ModuleCategory.world);
        withEnabled(true);

        this.registerSetting(a = new TickSetting("Wait 80 ticks", false));
        this.registerSetting(dead = new TickSetting("Remove dead", true));
    }

    @Override
    public void onDisable() {
        newEnt.clear();
    }


    @SubscribeEvent
    public void onTick(TickEvent ev) {
        if (a.isToggled() && !newEnt.isEmpty()) {
            long now = System.currentTimeMillis();
            newEnt.values().removeIf(e -> e < now - 4000L);
        }

    }

    public static boolean bot(Entity en) {
        if (!Utils.Player.isPlayerInGame() || mc.currentScreen != null)
            return false;
        if (Freecam.en != null && Freecam.en == en) {
            return true;
        }
        Module antiBot = Raven.moduleManager.getModuleByClazz(AntiBot.class);
        if ((antiBot != null && !antiBot.isEnabled()) || !Utils.Client.isHyp()) {
        } else if ((a.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) || en.getName().startsWith("§c")) {
            return true;
        } else if(en.isDead && dead.isToggled()) {
            return true;
        } else {
            String n = en.getDisplayName().getUnformattedText();
            if (n.contains("§")) {
                return n.contains("[NPC] ");
            }
            if (n.isEmpty() && en.getName().isEmpty()) {
                return true;
            }

            if (n.length() == 10) {
                int num = 0;
                int let = 0;
                char[] var4 = n.toCharArray();

                for (char c : var4) {
                    if (Character.isLetter(c)) {
                        if (Character.isUpperCase(c)) {
                            return false;
                        }

                        ++let;
                    } else {
                        if (!Character.isDigit(c)) {
                            return false;
                        }

                        ++num;
                    }
                }

                return num >= 2 && let >= 2;
            }
        }
        return false;
    }
}
