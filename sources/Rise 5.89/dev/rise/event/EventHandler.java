package dev.rise.event;

import dev.rise.Rise;
import dev.rise.event.api.Event;
import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.motion.TeleportEvent;
import dev.rise.event.impl.other.*;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.*;
import dev.rise.hidden.Disabler;
import dev.rise.module.Module;
import dev.rise.module.impl.movement.Fly;
import dev.rise.module.impl.render.Interface;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.clickgui.impl.strikeless.StrikeGUI;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.Objects;

public final class EventHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayer target = null;
    public static boolean canUpdateDeaths;

    public static void handle(final Event e) {
        if (Rise.INSTANCE.destructed) return;
        final Module[] modules = Rise.INSTANCE.getModuleManager().getModuleList();

        if (e instanceof Render2DEvent) {
            final Render2DEvent event = ((Render2DEvent) e);

            RenderUtil.delta2DFrameTime = (System.currentTimeMillis() - RenderUtil.last2DFrame) / 10F;
            RenderUtil.last2DFrame = System.currentTimeMillis();

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onRender2DEvent(event);
                }
            }
        } else if (e instanceof Render3DEvent) {
            final Render3DEvent event = ((Render3DEvent) e);

            RenderUtil.delta3DFrameTime = (System.currentTimeMillis() - RenderUtil.last3DFrame) / 10F;
            RenderUtil.last3DFrame = System.currentTimeMillis();

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onRender3DEvent(event);
                }
            }
        } else if (e instanceof BlurEvent) {
            final BlurEvent event = ((BlurEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlur(event);
                }
            }
        } else if (e instanceof FadingOutlineEvent) {
            final FadingOutlineEvent event = ((FadingOutlineEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onFadingOutline(event);
                }
            }
        } else if (e instanceof Shader3DEvent) {
            final Shader3DEvent event = ((Shader3DEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onShader3DEvent(event);
                }
            }
        } else if (e instanceof PreBlurEvent) {
            final PreBlurEvent event = ((PreBlurEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPreBlur(event);
                }
            }
        } else if (e instanceof PacketReceiveEvent) {
            final PacketReceiveEvent event = ((PacketReceiveEvent) e);

            if (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("AntiCheat")).isEnabled())
                Rise.INSTANCE.getAntiCheat().handle(event.getPacket());

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPacketReceive(event);
                }
            }

            Disabler.onReceive(event);
        } else if (e instanceof PacketSendEvent) {
            final PacketSendEvent event = ((PacketSendEvent) e);

            if (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("AntiCheat")).isEnabled())
                Rise.INSTANCE.getAntiCheat().handle(event.getPacket());

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPacketSend(event);
                }
            }

            Disabler.onSend(event);
        } else if (e instanceof PostMotionEvent) {
            final PostMotionEvent event = ((PostMotionEvent) e);

            //Statistics
            if (target != null && !mc.theWorld.playerEntities.contains(target) && mc.thePlayer.getDistance(target.posX, mc.thePlayer.posY, target.posZ) < 30) {
                Rise.totalKills++;
                target = null;
            }

            if (mc.thePlayer.ticksExisted == 1) {
                PlayerUtil.worldChanges++;
            }

            final double d = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);

            Rise.amountOfModulesOn = 0;

            if (mc.thePlayer.onGround)
                Rise.distanceRan += d;
            else if (!Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Fly")).isEnabled())
                Rise.distanceJumped += d;

            if (mc.thePlayer.getHealth() <= 0) {
                if (canUpdateDeaths) {
                    Rise.totalDeaths++;
                    canUpdateDeaths = false;
                }
            } else
                canUpdateDeaths = true;

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    if (module instanceof Fly)
                        Rise.distanceFlew += d;

                    Rise.amountOfModulesOn++;

                    module.onPostMotion(event);
                }
            }
        } else if (e instanceof PreMotionEvent) {
            final PreMotionEvent event = ((PreMotionEvent) e);

            /* Used to reset PlayerUtil.isOnServer() */
            if (mc.thePlayer.ticksExisted == 1) {
                PlayerUtil.serverResponses.clear();
                PlayerUtil.sentEmail = false;
            }

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onPreMotion(event);
                }

                /* Calls events that are always used called whether the module is on or not*/
                if (mc.currentScreen instanceof ClickGUI || mc.currentScreen instanceof StrikeGUI) {
                    module.onUpdateAlwaysInGui();
                }
                module.onUpdateAlways();
            }
            Disabler.onUpdate(event);
        } else if (e instanceof KeyEvent) {
            final KeyEvent event = ((KeyEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onKey(event);
                }

                if (module.getKeyBind() == event.getKey()) {
                    module.toggleModule();
                }
            }
        } else if (e instanceof StrafeEvent) {
            final StrafeEvent event = ((StrafeEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onStrafe(event);
                }
            }
        } else if (e instanceof CanPlaceBlockEvent) {
            final CanPlaceBlockEvent event = ((CanPlaceBlockEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onCanPlaceBlockEvent(event);
                }
            }
        } else if (e instanceof BlockBreakEvent) {
            final BlockBreakEvent event = ((BlockBreakEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlockBreak(event);
                }
            }
        } else if (e instanceof AttackEvent) {
            final AttackEvent event = ((AttackEvent) e);

            //Statistics
            final Entity entity = event.getTarget();
            if (entity instanceof EntityPlayer) {
                target = (EntityPlayer) entity;
            }

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onAttackEvent(event);
                }
            }
        } else if (e instanceof MoveButtonEvent) {
            final MoveButtonEvent event = ((MoveButtonEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onMoveButton(event);
                }
            }
        } else if (e instanceof MoveEvent) {
            final MoveEvent event = ((MoveEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onMove(event);
                }
            }
        } else if (e instanceof WorldChangedEvent) {
            final WorldChangedEvent event = ((WorldChangedEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onWorldChanged(event);
                }
            }
        } else if (e instanceof UpdateEvent) {
            final UpdateEvent event = ((UpdateEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onUpdate(event);
                }
            }
        } else if (e instanceof BlockCollideEvent) {
            final BlockCollideEvent event = ((BlockCollideEvent) e);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onBlockCollide(event);
                }
            }
        } else if (e instanceof TeleportEvent) {
            final TeleportEvent event = ((TeleportEvent) e);

            Disabler.onTeleport(event);

            for (final Module module : modules) {
                if (module.isEnabled()) {
                    module.onTeleportEvent(event);
                }
            }
        }

        final int r = Interface.red0;
        final int g = Interface.green0;
        final int b = Interface.blue0;

        final Color bright = new Color(Math.min(r + 26, 255), Math.min(g + 45, 255), Math.min(b + 13, 255));

        Rise.CLIENT_THEME_COLOR = new Color(r, g, b, 255).getRGB();
        Rise.CLIENT_THEME_COLOR_BRIGHT = bright.hashCode();
        Rise.CLIENT_THEME_COLOR_BRIGHT_COLOR = bright;
    }
}
