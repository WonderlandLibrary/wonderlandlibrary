package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.CustomModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.movement.speeds.impl.*;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Speed extends Module {
    public CustomModeValue mode = new CustomModeValue("Type", "Strafe", new Module[]{
            new HypixelSpeed(this),
            new LegitSpeed(this),
            new NCPSpeed(this),
            new VerusSpeed(this),
            new VulcanSpeed(this),
            new StrafeSpeed(this),
            new VanillaSpeed(this),
            new CubecraftSpeed(this),
            new BlocksMCSpeed(this),
            new GrimSpeed(this)
    });
    public BooleanValue flagDisable = new BooleanValue("Lag back checker", false);
    public BooleanValue lowHop = new BooleanValue("Low Hop", false){
        @Override
        public boolean isHidden() {
            return !(mode.is("BlocksMC") || mode.is("Vulcan"));
        }
    };
    public BooleanValue timer = new BooleanValue("Timer", false){
        @Override
        public boolean isHidden() {
            return !(mode.is("BlocksMC") || mode.is("Vulcan"));
        }
    };
    public ModeValue hypixelMode = new ModeValue("Mode", "Ground", new String[]{"Ground", "FakeStrafe", "EternityGS", "EternityF", "Real"}){
        @Override
        public boolean isHidden() {
            return !mode.is("Hypixel");
        }
    };
    public NumberValue speed = new NumberValue("Speed", 1, 0, 10, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Vanilla");
        }
    };
    public NumberValue cubeSpeed = new NumberValue("Multi", 1.5f, 1, 2, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Cubecraft");
        }
    };
    public BooleanValue Speed = new BooleanValue("Speed", false){
        @Override
        public boolean isHidden() {
            return !(mode.is("BSpeed"));
        }
    };
    public BooleanValue sprint = new BooleanValue("sprint", true){
        @Override
        public boolean isHidden() {
            return !(mode.is("BSpeed"));
        }
    };
    public BooleanValue blink = new BooleanValue("blink", true){
        @Override
        public boolean isHidden() {
            return !(mode.is("BSpeed"));
        }
    };

    public Speed() {
        super("Speed", Category.Movement, "Vroom Vroom");
     registerValue(mode);
     registerValue(flagDisable);
     registerValue(lowHop);
     registerValue(timer);
     registerValue(hypixelMode);
     registerValue(speed);
     registerValue(cubeSpeed);
        speed.id = "1";
        cubeSpeed.id = "2";
        mode.setPremiumModes(new String[]{
                "Grim"
        });
        hypixelMode.setPremiumModes(new String[]{
                "FakeStrafe"
        });
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(flagDisable.isEnable()){
            if(event.packet instanceof SPlayerPositionLookPacket){
                flagDisable();
                return;
            }
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onEnable() {
        mode.getCurrent().onEnable();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mode.getCurrent().onDisable();
        mc.timer.setTimerSpeed(1.0f);
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        mode.getCurrent().onUpdateEvent(event);
        super.onUpdateEvent(event);
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        suffix = mode.getValue();
        mode.getCurrent().onMoveEvent(event);
        super.onMoveEvent(event);
    }
}
