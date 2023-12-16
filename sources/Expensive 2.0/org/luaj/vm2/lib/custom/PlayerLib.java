package org.luaj.vm2.lib.custom;

import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.jse.CoerceJavaToLua;
import org.luaj.vm2.customs.RayTraceHook;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import wtf.expensive.util.movement.MoveUtil;

public class PlayerLib extends TwoArgFunction {
    public PlayerLib() {
    }

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("getYaw", new yaw());
        library.set("setYaw", new OneArgFunction() {

            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.rotationYaw = arg.tofloat();
                return LuaValue.valueOf(0);
            }
        });
        library.set("setViewYaw", new OneArgFunction() {

            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.rotationYawHead = arg.tofloat();
                mc.player.renderYawOffset = arg.tofloat();
                return LuaValue.valueOf(0);
            }
        });
        library.set("getPitch", new pitch());
        library.set("setPitch", new OneArgFunction() {

            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.rotationPitch = arg.tofloat();
                return LuaValue.valueOf(0);
            }
        });
        library.set("setViewPitch", new OneArgFunction() {

            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.rotationPitchHead = arg.tofloat();
                return LuaValue.valueOf(0);
            }
        });
        library.set("getMoveForward", new moveForward());
        library.set("getMoveStrafe", new moveStrafe());
        library.set("setSpeed", new speed());
        library.set("addMotion", new addmotion());
        library.set("setMotion", new motion());
        library.set("getMotion", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                if (mc.player != null) {
                    return LuaValue.listOf(new LuaValue[]{
                            LuaValue.valueOf(mc.player.motion.x),
                            LuaValue.valueOf(mc.player.motion.y),
                            LuaValue.valueOf(mc.player.motion.z)
                    });
                }
                return LuaValue.listOf(new LuaValue[]{
                        LuaValue.valueOf(0),
                        LuaValue.valueOf(0),
                        LuaValue.valueOf(0)
                });
            }
        });
        library.set("jump", new jump());
        library.set("isOnGround", new ground());
        library.set("getPosition", new position());
        library.set("setPosition", new setposition());
        library.set("getHurtTime", new hurtTick());
        library.set("swingArm", new swingarm());
        library.set("getHealth", new heath());
        library.set("isCooldowned", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.getCooledAttackStrength(1) >= 0.93F);
            }
        });
        library.set("getMouseOver", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(new RayTraceHook(mc.objectMouseOver));
            }
        });
        library.set("setTimer", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                mc.timer.timerSpeed = arg.tofloat();
                return LuaValue.valueOf(0);
            }
        });
        library.set("getTimer", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(mc.timer.timerSpeed);
            }
        });
        library.set("getWidth", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.getWidth());
            }
        });
        library.set("getHeight", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.getHeight());
            }
        });
        library.set("getMaxHealth", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.getMaxHealth());
            }
        });
        library.set("prevPosX", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.prevPosX);
            }
        });
        library.set("prevPosY", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.prevPosY);
            }
        });
        library.set("prevPosZ", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.prevPosZ);
            }
        });
        library.set("isMoving", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(MoveUtil.isMoving());
            }
        });
        library.set("handActive", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isHandActive());
            }
        });
        library.set("ticksExisted", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.ticksExisted);
            }
        });
        library.set("swimming", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isSwimming());
            }
        });
        library.set("collidedVertically", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.collidedVertically);
            }
        });
        library.set("collidedHorizontally", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.collidedHorizontally);
            }
        });
        library.set("eyeHeight", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.getEyeHeight());
            }
        });
        library.set("isJumpPressed", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.gameSettings.keyBindJump.pressed);
            }
        });
        library.set("isSneakPressed", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.gameSettings.keyBindSneak.pressed);
            }
        });
        library.set("areEyesInFluid", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (arg.toint() == 2012) {
                    return LuaValue.valueOf(mc.player.areEyesInFluid(FluidTags.WATER));
                }
                if (arg.toint() == 2013) {
                    return LuaValue.valueOf(mc.player.areEyesInFluid(FluidTags.LAVA));
                }
                return LuaValue.valueOf(0);
            }
        });
        library.set("setFlying", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.abilities.isFlying = arg.toboolean();
                return LuaValue.valueOf(0);
            }
        });
        library.set("sendChatMessage", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                mc.player.sendChatMessage(arg.toString());
                return LuaValue.valueOf(0);
            }
        });
        library.set("isOnLadder", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isOnLadder());
            }
        });
        library.set("isOnLadder", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isOnLadder());
            }
        });

        library.set("isRidingHorse", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isRidingHorse());
            }
        });
        library.set("isElytraFlying", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(mc.player.isElytraFlying());
            }
        });
        library.set("getFallDistance", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                if (mc.player != null) {
                    return LuaValue.valueOf(mc.player.fallDistance);
                }
                return LuaValue.valueOf(0);
            }
        });
        library.set("isInWater", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                if (mc.player != null) {
                    return LuaValue.valueOf(mc.player.isInWater());
                }
                return LuaValue.valueOf(false);
            }
        });
        library.set("getLook", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                Vector3d vec = new Vector3d(0, 0, arg.todouble())
                        .rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch)))
                        .rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                return LuaValue.listOf(new LuaValue[]{
                        LuaValue.valueOf(vec.getX()),
                        LuaValue.valueOf(vec.getY()),
                        LuaValue.valueOf(vec.getZ())
                });
            }
        });
        env.set("player", library);
        return library;
    }

    static class heath extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.getHealth());
            }
            return LuaValue.valueOf(0);
        }
    }

    static class swingarm extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (mc.player != null) {
                if (arg.toint() == 341)
                    mc.player.swingArm(Hand.MAIN_HAND);
                if (arg.toint() == 351)
                    mc.player.swingArm(Hand.OFF_HAND);
            }
            return LuaValue.valueOf(0);
        }

    }


    static class motion extends ThreeArgFunction {
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            if (mc.player != null) {
                mc.player.motion = new Vector3d(x.todouble(), y.todouble(), z.todouble());
            }
            return LuaValue.valueOf(0);
        }
    }

    static class speed extends OneArgFunction {
        public LuaValue call(LuaValue val) {
            if (mc.player != null) {
                MoveUtil.setMotion(val.todouble());
            }
            return LuaValue.valueOf(0);
        }
    }


    static class addmotion extends ThreeArgFunction {
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            if (mc.player != null) {
                mc.player.motion = mc.player.motion.add(new Vector3d(x.todouble(), y.todouble(), z.todouble()));
            }
            return LuaValue.valueOf(0);
        }
    }

    static class yaw extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.rotationYaw);
            }
            return LuaValue.valueOf(0);
        }
    }

    static class hurtTick extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.hurtTime);
            }
            return LuaValue.valueOf(0);
        }
    }

    static class moveForward extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.moveForward);
            }
            return LuaValue.valueOf(0);
        }
    }

    static class moveStrafe extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.moveStrafing);
            }
            return LuaValue.valueOf(0);
        }
    }

    static class pitch extends ZeroArgFunction {
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.rotationPitch);
            }
            return LuaValue.valueOf(0);
        }
    }

    static class position extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.listOf(new LuaValue[]{
                        LuaValue.valueOf(mc.player.getPosX()),
                        LuaValue.valueOf(mc.player.getPosY()),
                        LuaValue.valueOf(mc.player.getPosZ())
                });
            }
            return LuaValue.listOf(new LuaValue[]{
                    LuaValue.valueOf(0),
                    LuaValue.valueOf(0),
                    LuaValue.valueOf(0)
            });
        }
    }

    static class setposition extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {

            if (mc.player != null) {
                mc.player.setPosition(arg1.todouble(), arg2.todouble(), arg3.todouble());
            }

            return LuaValue.valueOf(0);
        }
    }

    static class jump extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            if (mc.player != null) {
                mc.player.jump();
            }
            return LuaValue.valueOf(0);
        }
    }

    static class ground extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            if (mc.player != null) {
                return LuaValue.valueOf(mc.player.isOnGround());
            } else {
                return LuaValue.FALSE;
            }
        }
    }

}