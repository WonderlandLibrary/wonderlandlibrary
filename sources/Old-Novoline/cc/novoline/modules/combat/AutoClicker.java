package cc.novoline.modules.combat;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.utils.Timer;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;

public final class AutoClicker extends AbstractModule {

    private final Timer timer = new Timer();

    /* properties @off */
    @Property("minimum-cps")
    private final IntProperty minimumCps = createInt(8).minimum(1).maximum(20);
    @Property("maximum-cps")
    private final IntProperty maximumCps = createInt(10).minimum(1).maximum(20);
    @Property("only-swords-tools")
    private final BooleanProperty onlySwordsTools = PropertyFactory.booleanTrue();

    /* constructors @on */
    public AutoClicker(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AutoClicker", "Auto Clicker", Keyboard.KEY_NONE, EnumModuleType.COMBAT);
        Manager.put(new Setting("MINRAND", "Min CPS", SettingType.SLIDER, this, this.minimumCps, 1));
        Manager.put(new Setting("MAXRAND", "Max CPS", SettingType.SLIDER, this, this.maximumCps, 1));
/*        Manager.put(new Setting("BLOCK_HIT", "Block Hit", SettingType.CHECKBOX, this, this.blockHit));
        Manager.put(new Setting("DESTROY_BLOCKS", "Destroy Blocks", SettingType.CHECKBOX, this, this.destroyBlocks));*/
        Manager.put(new Setting("ONLY_SWORDAXE", "Only Swords/Tools", SettingType.CHECKBOX, this, this.onlySwordsTools));
    }

    /* events */
    @EventTarget
    public void onTick(TickUpdateEvent event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (!onlySwordsTools.get() || mc.player.getHeldItem() != null && (mc.player.getHeldItem().getItem() instanceof ItemTool || mc.player.getHeldItem().getItem() instanceof ItemSword)) {
                if (timer.delay(1000 / ThreadLocalRandom.current().nextInt(minimumCps.get(), maximumCps.get() + 1))) {
                    mc.player.swingItem();

                    if (mc.objectMouseOver.entityHit != null) {
                        mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
                    }

                    timer.reset();
                }
            }
        }
    }
}
