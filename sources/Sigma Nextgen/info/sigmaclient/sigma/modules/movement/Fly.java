package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.CustomModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.movement.flys.impl.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class Fly extends Module {
    public CustomModeValue mode = new CustomModeValue("Type", "Vanilla", new Module[]{
            new SpartanFly(this),
            new VanillaFly(this),
            new VeltPVPFly(this),
            new VulcanFly(this),
            new OldNCPFly(this),
            new PotPVPFly(this),
            new CubecraftFly(this),
            new MadcraftFly(this),
            new NCPFly(this),
            new ColdPVPFly(this)
    });
    public NumberValue vanillaSpeed = new NumberValue("Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !mode.is("Vanilla");}};
    public NumberValue vanillaUpSpeed = new NumberValue("Vertical - Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !mode.is("Vanilla");}};
    //
    public NumberValue vulcanSpeed = new NumberValue("Speed", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !mode.is("Vulcan") && !mode.is("VulcanZoom");}};
    //
    public ModeValue oldNCPMode = new ModeValue("Mode", "Fast", new String[]{"Basic", "Fast"}){@Override public boolean isHidden() {return !mode.is("Hypixel");}};
    public NumberValue oldNCPSpeed = new NumberValue("Speed", 1, 0.1, 1.5, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !mode.is("Hypixel");}};
    public NumberValue oldNCPTimer = new NumberValue("Timer", 7, 0, 9, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !mode.is("Hypixel");}};

    //
    public NumberValue madCraftTimer = new NumberValue("Timer", 0.2, 0, 1, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !(mode.is("Madcraft"));}};
    public NumberValue madCraftSpeed = new NumberValue("Speed", 5, 0, 9, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !(mode.is("Madcraft"));}};

    public NumberValue codePVPTimer = new NumberValue("Timer", 0.2, 0, 1, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !(mode.is("ColdPVP"));}};
    public NumberValue codePVPSpeed = new NumberValue("Speed", 5, 0, 30, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !(mode.is("ColdPVP"));}};
    public ModeValue NCPMode = new ModeValue("Mode", "Fast", new String[]{"Basic", "Fast"}){@Override public boolean isHidden() {return !mode.is("NCP");}};
    public Fly() {
        super("Fly", Category.Movement, "Allows you to fly like a bird");
     registerValue(mode);
     registerValue(vanillaSpeed);
     registerValue(vanillaUpSpeed);
     registerValue(vulcanSpeed);
     registerValue(oldNCPMode);
     registerValue(oldNCPSpeed);
     registerValue(oldNCPTimer);
     registerValue(madCraftTimer);
     registerValue(madCraftSpeed);
     registerValue(codePVPTimer);
     registerValue(codePVPSpeed);
     registerValue(NCPMode);
        mode.setPremiumModes(new String[]{"Vulcan", "NCP"});
        vanillaSpeed.id = "1";
        vulcanSpeed.id = "2";
        oldNCPSpeed.id = "3";
        madCraftSpeed.id = "4";
        madCraftTimer.id = "5";
        oldNCPMode.id = "6";
        codePVPTimer.id = "7";
        codePVPSpeed.id = "8";
        NCPMode.id = "9";
        oldNCPTimer.id = "10";
    }
    public double cacheX = 0;
    public double cacheY = 0;
    public double cacheZ = 0;
    @Override
    public void onEnable() {
        cacheX = mc.player.getPosX();
        cacheY = mc.player.getPosY();
        cacheZ = mc.player.getPosZ();
        mode.getCurrent().onEnable();
        super.onEnable();
    }
    @Override
    public void onMoveEvent(MoveEvent event) {
        mode.getCurrent().onMoveEvent(event);
        super.onMoveEvent(event);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        mode.getCurrent().onUpdateEvent(event);
        super.onUpdateEvent(event);
    }
    @Override
    public void onPacketEvent(PacketEvent event) {
        mode.getCurrent().onPacketEvent(event);
        super.onPacketEvent(event);
    }

    @Override
    public void onDisable() {
        mode.getCurrent().onDisable();
        mc.timer.setTimerSpeed(1.0F);
        super.onDisable();
    }
}
