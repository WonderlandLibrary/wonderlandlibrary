package vip.radium.module.impl.misc.hackerdetect;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.RadiumClient;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.impl.misc.hackerdetect.check.Check;
import vip.radium.module.impl.misc.hackerdetect.check.impl.NoSlowCheck;
import vip.radium.module.impl.misc.hackerdetect.check.impl.OmniSprintCheck;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.utils.Wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ModuleInfo(label = "Hacker Detector", category = ModuleCategory.MISCELLANEOUS)
public final class HackerDetector extends Module {

    private final DoubleProperty vlToAlertProperty = new DoubleProperty("VL To Alert", 20, 1, 100, 1);
    private final MultiSelectEnumProperty<CheckType> checksProperty = new MultiSelectEnumProperty<>(
            "Checks", CheckType.NO_SLOW);

    private final Map<UUID, Flag> flagMap = new HashMap<>();
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            for (EntityPlayer player : Wrapper.getLoadedPlayers()) {
                if (player instanceof EntityPlayerSP)
                    continue;
                for (CheckType check : checksProperty.getValues()) {
                    final Check type = check.getCheck();
                    if (type.flag(player)) {
                        UUID uuid = player.getUniqueID();
                        if (flagMap.containsKey(uuid)) {
                            Flag flag = flagMap.get(uuid);
                            if (flag.checkClass == type.getClass()) {
                                flag.vl++;

                                if (shouldAlert(flag.vl))
                                    flag(flag);
                            }
                        } else {
                            Flag flag = new Flag(player, type.getClass(), 1);
                            flagMap.put(uuid, flag);

                            if (shouldAlert(1))
                                flag(flag);
                        }
                    }
                }
            }
        }
    };

    private boolean shouldAlert(int vl) {
        return vlToAlertProperty.getValue().intValue() <= vl;
    }

    private void flag(Flag flag) {
        final EntityPlayer flaggedPlayer = flag.player;
        if (flag.vl % vlToAlertProperty.getValue().intValue() == 0)
            RadiumClient.getInstance().getNotificationManager().add(
                    new Notification("Hacker Detected",
                            String.format("%s flagged for %s (VL - %s)",
                                    flaggedPlayer.getGameProfile().getName(),
                                    flag.checkClass.getSimpleName(),
                                    flag.vl),
                            flag.vl * 30L,
                            NotificationType.WARNING));
    }

    private enum CheckType {
        NO_SLOW(new NoSlowCheck()),
        OMNI_SPRINT(new OmniSprintCheck());

        private final Check check;

        CheckType(Check check) {
            this.check = check;
        }

        public Check getCheck() {
            return check;
        }
    }

    private static final class Flag {
        private final EntityPlayer player;
        private final Class<?> checkClass;
        private int vl;

        public Flag(EntityPlayer player, Class<?> checkClass, int vl) {
            this.player = player;
            this.checkClass = checkClass;
            this.vl = vl;
        }
    }

}
