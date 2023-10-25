package tech.dort.dortware.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.SliderUnit;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.PacketEvent;
import tech.dort.dortware.impl.events.enums.PacketDirection;

public class Velocity extends Module {

    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, Velocity.Mode.values());
    private final NumberValue vertical = new NumberValue("Vertical", this, 0, 0, 100, SliderUnit.PERCENT, true);
    private final NumberValue horizontal = new NumberValue("Horizontal", this, 0, 0, 100, SliderUnit.PERCENT, true);

    public Velocity(ModuleData moduleData) {
        super(moduleData);
        register(enumValue, vertical, horizontal);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        try {
            Mode mode = enumValue.getValue();
            switch (mode) {
                case PACKET:
                    if (mc.playerController.isSpectatorMode()) {
                        return;
                    }
                    if (event.getPacketDirection() == PacketDirection.INBOUND) {
                        int hPercent = horizontal.getValue().intValue();
                        int vPercent = vertical.getValue().intValue();
                        if (event.getPacket() instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = event.getPacket();
                            if (mc.theWorld != null && mc.theWorld.getEntityByID(packet.func_149412_c()) == mc.thePlayer) {
                                if (hPercent > 0) {
                                    packet.field_149415_b *= hPercent / 100.0f;
                                    packet.field_149414_d *= hPercent / 100.0f;
                                }
                                if (vPercent > 0) {
                                    packet.field_149416_c *= vPercent / 100.0f;
                                }
                                if (hPercent == 0 && vPercent == 0) {
                                    event.setCancelled(true);
                                }  //                        PacketSleepThread.delayPacket(event.getPacket(), 120L);
                            }
                        } else if (event.getPacket() instanceof S27PacketExplosion) {
                            final S27PacketExplosion s27PacketExplosion = event.getPacket();
                            if (hPercent > 0.0) {
                                s27PacketExplosion.field_149152_f *= hPercent / 100.0f;
                                s27PacketExplosion.field_149159_h *= hPercent / 100.0f;
                            }
                            if (vPercent > 0) {
                                s27PacketExplosion.field_149153_g *= vPercent / 100.0f;
                            }
                            if (hPercent == 0 && vPercent == 0) {
                                event.setCancelled(true);
                            }
                        }
                    }
                    break;
                case PULL:
                    if (mc.playerController.isSpectatorMode()) {
                        return;
                    }
                    if (event.getPacketDirection() == PacketDirection.INBOUND) {
                        if (event.getPacket() instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = event.getPacket();
                            if (mc.theWorld.getEntityByID(packet.func_149412_c()) == mc.thePlayer) {
                                packet.field_149414_d = -packet.field_149414_d;
                                packet.field_149415_b = -packet.field_149415_b;
                                event.setPacket(packet);
                            }
                        }
                    } else if (event.getPacket() instanceof S27PacketExplosion) {
                        final S27PacketExplosion s27PacketExplosion = event.getPacket();
                        s27PacketExplosion.field_149159_h = -s27PacketExplosion.field_149159_h;
                        s27PacketExplosion.field_149152_f = -s27PacketExplosion.field_149152_f;
                        event.setPacket(s27PacketExplosion);
                    }
                    break;
                case AGC:
                    if (mc.playerController.isSpectatorMode()) {
                        return;
                    }
                    if (event.getPacketDirection() == PacketDirection.INBOUND) {
                        if (event.getPacket() instanceof S12PacketEntityVelocity) {
                            S12PacketEntityVelocity packet = event.getPacket();
                            if (mc.theWorld.getEntityByID(packet.func_149412_c()) == mc.thePlayer) {
                                packet.field_149414_d = -packet.field_149414_d / 2;
                                packet.field_149416_c = -packet.field_149416_c;
                                packet.field_149415_b = -packet.field_149415_b / 2;
                                event.setPacket(packet);
                            }
                        }
                    } else if (event.getPacket() instanceof S27PacketExplosion) {
                        final S27PacketExplosion s27PacketExplosion = event.getPacket();
                        s27PacketExplosion.field_149159_h = -s27PacketExplosion.field_149159_h / 2;
                        s27PacketExplosion.field_149153_g = -s27PacketExplosion.field_149153_g;
                        s27PacketExplosion.field_149152_f = -s27PacketExplosion.field_149152_f / 2;
                        event.setPacket(s27PacketExplosion);
                    }
                    break;
                case FLOAT:
                    if (mc.thePlayer.hurtTime >= 5 && !mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.0F;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSuffix() {
        return " \2477" + enumValue.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        PACKET("Packet"), PULL("Pull"), FLOAT("Float"), AGC("Old AGC");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}