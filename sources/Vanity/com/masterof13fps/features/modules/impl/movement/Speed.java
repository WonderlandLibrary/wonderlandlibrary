package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.entity.PlayerUtil;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ModuleInfo(name = "Speed", category = Category.MOVEMENT, description = "Lets you speed like a sonic")
public class Speed extends Module {
    public static boolean canStep;
    public double speed;
    public double moveSpeed;
    public int stage;
    private boolean legitHop = false;
    private boolean move;
    private boolean hop;
    private double prevY;
    private float prevYaw;
    private int motionTicks;
    private int ticks = 0;
    private int tick;
    private int speedTick;

    public Setting mode = new Setting("Mode", this, "NCP BHop", new String[]{"AAC 3.3.11", "AAC 3.3.10", "AAC 3.3.9", "AAC 3.3.1", "AAC 1.9.10", "AAC 1.9.8", "AAC Packet", "NCP BHop", "NCP Fast", "NCP Slow",
            "MineSecure", "MineSucht", "Rewinside", "CubeCraft", "Hive SkyGiants", "MiniHop", "Race", "Motion", "Jump", "Frames", "Ground", "Timer"});
    public Setting aac3311Speed = new Setting("AAC 3.3.11 Speed", this, 3, 2, 6, false);
    public Setting raceSpeed = new Setting("Race Speed", this, 0.5, 0.05, 2, false);
    public Setting framesSpeed = new Setting("Frames Speed", this, 4.25, 0, 50, true);
    public Setting timerSpeed = new Setting("Timer Speed", this, 4.25, 0, 50, true);

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0F;
        if (angle > 180.0F) {
            angle = 360.0F - angle;
        }
        return angle;
    }

    @Override
    public void onToggle() {

    }

    public void onEnable() {
        motionTicks = 0;
        move = false;
        hop = false;
        prevY = 0.0D;
        ticks = 0;
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        getPlayer().motionX = 0.0D;
        getPlayer().motionZ = 0.0D;
        getPlayer().setVelocity(0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setDisplayName("Speed §7" + mode.getCurrentMode());

            if (state()) {

                switch (mode.getCurrentMode()) {
                    case "Ground": {
                        doGround();
                        break;
                    }
                    case "AAC 1.9.8": {
                        doAAC198();
                        break;
                    }
                    case "Frames": {
                        doFrames(framesSpeed.getCurrentValue());
                        break;
                    }
                    case "Motion": {
                        doMotion();
                        break;
                    }
                    case "Jump": {
                        doJump();
                        break;
                    }
                    case "Timer": {
                        doTimer();
                        break;
                    }
                    case "NCP Slow": {
                        doNCPYPortSlow();
                        break;
                    }
                    case "AAC 1.9.10": {
                        doAAC1910();
                        break;
                    }
                    case "AAC 3.3.10": {
                        doAAC3310();
                        break;
                    }
                    case "Hive SkyGiants": {
                        doHiveSkyGiants();
                        break;
                    }
                    case "NCP Fast": {
                        doNCPYPortFast();
                        break;
                    }
                    case "AAC 3.3.1": {
                        doAACYPort331();
                        break;
                    }
                    case "MineSecure": {
                        doMineSecure();
                        break;
                    }
                    case "AAC 3.3.9": {
                        doAACLowHop339();
                        break;
                    }
                    case "AAC 3.3.11": {
                        doAAC3311();
                        break;
                    }
                    case "MineSucht": {
                        doMineSucht();
                        break;
                    }
                    case "NCP BHop": {
                        doNCPBHop();
                        break;
                    }
                    case "Rewinside": {
                        doRewinside();
                        break;
                    }
                    case "CubeCraft": {
                        doCubeCraft();
                        break;
                    }
                    case "Race": {
                        doRace();
                        break;
                    }
                    case "AAC Packet": {
                        doAACPacket();
                        break;
                    }
                    case "MiniHop": {
                        doMiniHop();
                        break;
                    }
                }
            }
        }
    }

    private void doAACPacket() {
        double[] posY = new double[]{0.0D, 0.3875000000000002D, 0.6743500058650973D, 0.8625630155582433D, 0.9541117671215256D, 0.9541117671215256D, 0.8612117655956464D, 0.6772697608024796D, 0.40410659107088165D, 0.04350667799786301D};
        double[] speed = new double[]{0.20149000298023223D, 0.18106395212314025D, 0.18944500042887916D, 0.1970717546069033D, 0.20401210110892484D, 0.2103278166077823D, 0.21607511787737882D, 0.22130516218344062D, 0.22606450263912023D, 0.23039550257860736D, 0.43433671561735787D, 0.30819827198999616D, 0.30513723484194827D, 0.3023516909569458D, 0.29981684594853975D, 0.2975101369244113D, 0.29541103165195853D, 0.2935008457989753D, 0.2917625766226639D, 0.29018075162663265D, 0.48874129381899145D, 0.3379031751383907D, 0.3321686974860294D, 0.3269503226719879D, 0.3222016014543529D, 0.3178802650217648D, 0.3139478487547781D, 0.31036934984868847D, 0.30711291575029703D, 0.3041495606353575D, 0.5014529103832777D, 0.3448437185886533D, 0.33848459220779137D, 0.33269778703443226D, 0.3274317941749105D, 0.3226397405346395D, 0.3182789715963163D, 0.3143106717480765D, 0.3106995187821054D, 0.30741336948836545D, 0.5044229765251116D, 0.34646537489045426D, 0.33996029948495987D, 0.3340406806953576D, 0.3286538274415713D, 0.3237517908393499D, 0.3192909374027674D, 0.31523156065848684D, 0.31153752771473014D, 0.30817595763903166D, 0.5051169317622176D, 0.34684427449392435D, 0.3403050981340547D, 0.33435444747507653D, 0.3289393552193444D, 0.32401162112461174D, 0.31952738296917005D, 0.31544672613011426D, 0.311733328299554D, 0.3083541361763565D, 0.5052790742358562D, 0.34693280429481393D, 0.34038566025518596D, 0.33442775900741883D, 0.3290060687156986D, 0.3240723304080437D, 0.31958262841868523D, 0.31549699949062193D, 0.3117790770589345D, 0.3083957675485925D, 0.5053169587856827D, 0.3469534892614219D, 0.3404044835753417D, 0.3344448882292542D, 0.329021656308018D, 0.3240865151174631D, 0.31959553650462896D, 0.31550874584916927D, 0.3117897662455206D, 0.30840549470866624D, 0.505325810501605D, 0.3469583222988768D, 0.3404088816395524D, 0.3344488904678013D, 0.3290252983452008D, 0.32408982937139497D, 0.31959855247579383D, 0.3155114903830084D, 0.3117922637713862D, 0.3084077674572694D, 0.5053278787028934D, 0.34695945153691143D, 0.3404099092461936D, 0.33444982558987174D, 0.3290261493063094D, 0.32409060374602616D, 0.31959925715672854D, 0.3155121316426775D, 0.3117928473177019D, 0.30840829848443196D, 0.5053283619376252D, 0.34695971538310566D, 0.3404101493462372D, 0.3344500440809177D, 0.329026348133167D, 0.32409078467847174D, 0.3195994218052588D, 0.3155122814728443D, 0.3117929836631576D, 0.30840842255880024D, 0.5053284748453036D, 0.34695977703070524D, 0.34041020544555445D, 0.33445009513129786D, 0.3290263945890143D, 0.324090826953294D, 0.31959946027534814D, 0.3155123164806266D, 0.3117930155202404D, 0.3084084515487464D, 0.5053285012261555D, 0.346959791434652D, 0.3404102185531464D, 0.3344501070592069D, 0.32902640544341183D, 0.32409083683079604D, 0.3195994692638752D, 0.3155123246601865D, 0.31179302296364014D, 0.3084084583222404D, 0.5053285073900351D, 0.34695979480013067D, 0.34041022161573203D, 0.3344501098461599D, 0.32902640797953914D, 0.324090839138672D, 0.31959947136404243D, 0.3155123265713387D, 0.3117930247027887D, 0.3084084599048656D, 0.5053285088302241D, 0.34695979558647394D, 0.34041022233130447D, 0.33445011049733087D, 0.3290264085721047D, 0.32409083967790664D, 0.31959947185474596D, 0.31551232701787896D, 0.3117930251091403D, 0.3084084602746456D, 0.5053285091667239D, 0.34695979577020286D, 0.3404102224984978D, 0.3344501106494768D, 0.3290264087105575D, 0.3240908398038987D, 0.31959947196939875D, 0.315512327122213D, 0.3117930252040843D, 0.30840846036104463D, 0.505328509245347D, 0.3469597958131311D, 0.3404102225375625D, 0.3344501106850256D, 0.32902640874290695D, 0.3240908398333367D, 0.3195994719961873D, 0.3155123271465906D, 0.31179302522626795D, 0.30840846038123176D, 0.5053285092637173D, 0.3469597958231613D, 0.34041022254668996D, 0.33445011069333164D, 0.3290264087504654D, 0.3240908398402149D, 0.31959947200244654D, 0.3155123271522865D, 0.3117930252314512D, 0.3084084603859485D, 0.5053285092680095D, 0.34695979582550485D, 0.3404102225488226D, 0.3344501106952723D};
        if (!(getPlayer().isBlocking()) && getPlayer().onGround) {
            if (mc.gameSettings.keyBindForward.pressed) {
                getPlayer().posY += posY[tick];
                getPlayer().onGround = tick == 0;
                if (getPlayer().hurtTime == 0) {
                    PlayerUtil.setSpeed(speed[speedTick]);
                }

                ++tick;
                if (tick > posY.length - 1) {
                    tick = 0;
                }

                ++speedTick;
                if (speedTick > speed.length - 1) {
                    speedTick = speed.length - 10;
                }

                if (PlayerUtil.getSpeed() == 0.0D || getPlayer().hurtTime != 0) {
                    speedTick = 0;
                }
            } else {
                tick = speedTick = 0;
            }

        }
    }

    private void doRace() {
        if (getPlayer().onGround && isMoving()) {
            PlayerUtil.setSpeed(Client.main().setMgr().settingByName("Race Speed", this).getCurrentValue());
        }
    }

    private void doCubeCraft() {
        double d, blocks;
        d = Math.sqrt(getPlayer().motionX * getPlayer().motionX + getPlayer().motionZ * getPlayer().motionZ);
        if (isMoving() && getPlayer().hurtTime == 0 && !(getPlayer().isCollidedHorizontally)) {
            if (getPlayer().onGround) {
                if (timeHelper.isDelayComplete(180L)) {
                    blocks = 1.0D;
                    float x = (float) (-((double) MathHelper.sin(PlayerUtil.getDirection()) * blocks));
                    float z = (float) ((double) MathHelper.cos(PlayerUtil.getDirection()) * blocks);
                    getPlayer().setPositionAndUpdate(getPlayer().posX + (double) x, getPlayer().posY, getPlayer().posZ + (double) z);
                    timeHelper.reset();
                }
            }
        } else {
            getPlayer().motionX = getPlayer().motionZ = 0.0D;
        }

    }

    private void doRewinside() {
        double blocks;
        int tpspeed = 50;
        int packets = tpspeed;
        blocks = getPlayer().posX + Math.cos(Math.toRadians((double) (getPlayer().rotationYaw + 90.0F))) * (double) tpspeed / 3.5D;
        double posZ = getPlayer().posZ + Math.sin(Math.toRadians((double) (getPlayer().rotationYaw + 90.0F))) * (double) tpspeed / 3.5D;
        double dist = getPlayer().getDistance(blocks, getPlayer().posY, posZ);
        double plX = getPlayer().posX;
        double plZ = getPlayer().posZ;

        for (int i = 0; i < packets; ++i) {
            double pX = Math.cos(Math.toRadians((double) (getPlayer().rotationYaw + 90.0F))) * (dist / (double) packets);
            double pZ = Math.sin(Math.toRadians((double) (getPlayer().rotationYaw + 90.0F))) * (dist / (double) packets);
            plX += pX;
            plZ += pZ;
            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(plX, getPlayer().posY, plZ, true));
        }

    }

    private void doNCPBHop() {
        if (getPlayer().onGround && isMoving() && !getPlayer().isInWater()) {
            mc.timer.timerSpeed = 1.0F;
            getPlayer().jump();
        } else if (isMoving() && !getPlayer().isInWater()) {
            mc.timer.timerSpeed = 1.17F;
            PlayerUtil.setSpeed(0.26D);
        }

        if (!isMoving()) {
            getPlayer().motionX = getPlayer().motionZ = 0.0D;
        }

    }

    private void doMineSucht() {
        float x;
        if (isMoving() && timeHelper.isDelayComplete(500L)) {
            x = -(MathHelper.sin(PlayerUtil.getDirection()) * 2.0F);
            float z = MathHelper.cos(PlayerUtil.getDirection()) * 2.0F;
            getPlayer().setPositionAndUpdate(getPlayer().posX + (double) x, getPlayer().posY, getPlayer().posZ + (double) z);
            timeHelper.reset();
        }

    }

    private void doAAC3311() {
        mc.timer.timerSpeed = (float) Client.main().setMgr().settingByName("AAC 3.3.11 Speed", this).getCurrentValue();
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY, getPlayer().posZ, true));

    }

    private void doAACLowHop339() {
        if (isMoving()) {
            if (getPlayer().hurtTime == 0) {
                if (getPlayer().onGround) {
                    getPlayer().jump();
                    getPlayer().motionY = 0.3875D;
                } else {
                    getPlayer().motionY -= 0.0145D;
                }

                PlayerUtil.toFwd(0.00149D);
            }
        } else {
            int tick;
            getPlayer().motionX = getPlayer().motionZ = tick = 0;
        }
    }

    private void doMineSecure() {
        if (getPlayer().onGround && isMoving() && !getPlayer().isInWater()) {
            getPlayer().jump();
        } else if (isMoving() && !getPlayer().isInWater()) {
            PlayerUtil.setSpeed(1.0D);
        }

        if (!isMoving()) {
            getPlayer().motionX = getPlayer().motionZ = 0.0D;
        }
    }

    private void doAACYPort331() {
        if (getPlayer().onGround && isMoving()) {
            getPlayer().jump();
        } else {
            getPlayer().motionY = -0.21D;
        }
    }

    private void doNCPYPortFast() {
        if (isMoving()) {
            getPlayer().motionY = 0.399399995003033D;
            moveSpeed = 1.35D * getBaseMoveSpeed() - 1.0E-4D;
            if (getPlayer().onGround) {
                getPlayer().jump();
            }

            mc.timer.timerSpeed = 1.5F;
        }

        if (!getPlayer().onGround) {
            getPlayer().motionY = -0.5D;
        }

    }

    private void doHiveSkyGiants() {
        double d, blocks;
        if (isMoving()) {
            if (getPlayer().onGround) {
                mc.timer.timerSpeed = 2.0F;
                getPlayer().jump();
            } else {
                mc.timer.timerSpeed = 1.0F;
                getPlayer().jumpMovementFactor = 0.026F;
                d = 1.0157D;
                blocks = PlayerUtil.getSpeed();
                getPlayer().motionX = -Math.sin((double) PlayerUtil.getDirection()) * d * blocks;
                getPlayer().motionZ = Math.cos((double) PlayerUtil.getDirection()) * d * blocks;
            }
        }
    }

    private void doAAC3310() {
        if (!getPlayer().isBlocking()) {
            if (getPlayer().hurtTime == 0) {
                if (isMoving()) {
                    if (legitHop) {
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                            getPlayer().onGround = false;
                            legitHop = false;
                        }

                        return;
                    }

                    if (getPlayer().onGround) {
                        getPlayer().onGround = false;
                        PlayerUtil.setSpeed(0.375D);
                        getPlayer().jump();
                        getPlayer().motionY = 0.41D;
                    } else {
                        getPlayer().speedInAir = 0.0211F;
                    }
                } else {
                    getPlayer().motionX = getPlayer().motionZ = 0.0D;
                    legitHop = true;
                }

                if (getPlayer().isAirBorne) {
                    PlayerUtil.setSpeed(PlayerUtil.getSpeed());
                }
            }

        }
    }

    private void doAAC1910() {
        BlockPos bp = new BlockPos(getPlayer().posX, getPlayer().posY + 2.0, getPlayer().posZ);
        if (mc.gameSettings.keyBindForward.pressed && !getPlayer().isInWater()) {
            BlockPos below = new BlockPos(getPlayer().posX, getPlayer().posY - 1.0, getPlayer().posZ);
            if (mc.theWorld.getBlockState(bp).getBlock() == Blocks.air) {
                if (getPlayer().isCollidedVertically) {
                    getPlayer().jump();
                } else if (!(mc.theWorld.getBlockState(below).getBlock() instanceof BlockAir)) {
                    getPlayer().motionY = -0.2149999886751175;
                }
            } else {
                EntityPlayerSP thePlayer = getPlayer();
                thePlayer.motionX *= 1.7;
                EntityPlayerSP thePlayer2 = getPlayer();
                thePlayer2.motionZ *= 1.7;
                getPlayer().jump();
                EntityPlayerSP thePlayer3 = getPlayer();
                --thePlayer3.motionY;
            }
        }
    }

    private void doNCPYPortSlow() {
        if (getPlayer().onGround) {
            getPlayer().jump();
        } else {
            getPlayer().motionY = -0.42;
        }
    }

    private void doTimer() {
        mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
    }

    private void doJump() {
        if (!(isMoving())) {
            return;
        }
        if (getPlayer().moveForward > 0.0F) {
            float var1 = getPlayer().rotationYaw * 0.017453292F;
            getPlayer().motionX -= MathHelper.sin(var1) * 0.2D;
            getPlayer().motionZ += MathHelper.cos(var1) * 0.2D;
        }
    }

    private void doMotion() {
        double speed = 3.15D;
        double slow = 1.49D;
        double offset = 4.9D;
        if ((mc.gameSettings.keyBindForward.isPressed())
                || (mc.gameSettings.keyBindBack.isPressed())
                || (mc.gameSettings.keyBindLeft.isPressed())
                || (mc.gameSettings.keyBindRight.isPressed())) {
            boolean shouldOffset = true;
            if ((getPlayer().onGround) && (prevYaw < 1.0F)) {
                prevYaw += 0.2F;
            }
            if (!getPlayer().onGround) {
                prevYaw = 0.0F;
            }
            if ((prevYaw == 1.0F) && (shouldSpeedUp())) {
                if (!getPlayer().isSprinting()) {
                    offset += 0.8D;
                }
                if (getPlayer().moveStrafing != 0.0F) {
                    speed -= 0.1D;
                    offset += 0.5D;
                }
                if (getPlayer().isInWater()) {
                    speed -= 0.1D;
                }
                double ncT = 1.15D;
                ticks += 1;
                if (ticks == 1) {
                    getPlayer().motionX *= speed;
                    getPlayer().motionZ *= speed;
                } else if (ticks == 2) {
                    mc.timer.timerSpeed = 1.0F;
                    getPlayer().motionX /= slow;
                    getPlayer().motionZ /= slow;
                } else if (ticks == 4) {
                    mc.timer.timerSpeed = 1.0F;
                    getPlayer().setPosition(
                            getPlayer().posX + getPlayer().motionX / offset,
                            getPlayer().posY,
                            getPlayer().posZ + getPlayer().motionZ / offset);
                    ticks = 0;
                }
            }
        }
    }

    private void doFrames(double speed) {
        if ((getPlayer().movementInput.moveForward > 0.0F)
                || (getPlayer().movementInput.moveStrafe > 0.0F)) {
            if (getPlayer().onGround) {
                prevY = getPlayer().posY;
                hop = true;
                getPlayer().jump();
                if (motionTicks == 1) {
                    timeHelper.reset();
                    if (move) {
                        getPlayer().motionX /= speed * 2.0D;
                        getPlayer().motionZ /= speed * 2.0D;
                        move = false;
                    }
                    motionTicks = 0;
                } else {
                    motionTicks = 1;
                }
            } else if ((!move) && (motionTicks == 1) && (timeHelper.isDelayComplete(450L))) {
                getPlayer().motionX *= speed;
                getPlayer().motionZ *= speed;
                move = true;
            }
        }
    }

    private void doAAC198() {
        if ((hop) && (getPlayer().posY >= prevY + 0.399994D)) {
            getPlayer().motionY = -0.9D;
            getPlayer().posY = prevY;
            hop = false;
        }
        if ((getPlayer().moveForward != 0.0F) && (!getPlayer().isCollidedHorizontally)
                && (!getPlayer().isEating())) {
            if ((getPlayer().moveForward == 0.0F) && (getPlayer().moveStrafing == 0.0F)) {
                getPlayer().motionX = 0.0D;
                getPlayer().motionZ = 0.0D;
                if (getPlayer().isCollidedVertically) {
                    getPlayer().jump();
                    move = true;
                }
                if ((move) && (getPlayer().isCollidedVertically)) {
                    move = false;
                }
            }
            if (getPlayer().isCollidedVertically) {
                getPlayer().motionX *= 1.2D;
                getPlayer().motionZ *= 1.2D;
                doMiniHop();
            }
            if ((hop) && (!move) && (getPlayer().posY >= prevY + 0.399994D)) {
                getPlayer().motionY = -100.0D;
                getPlayer().posY = prevY;
                hop = false;
            }
        }
    }

    private void doMiniHop() {
        if (isMoving()) {
            if (getPlayer().onGround) {
                speedTick += 1;
                getPlayer().jump();
                getPlayer().motionY = 0.01D;
            }
            if (speedTick > 25) {
                speedTick = 25;
            }
            PlayerUtil.setSpeed(speedTick * 0.05D);
        }
    }

    private void doGround() {
        if ((hop) && (getPlayer().posY >= prevY + 0.399994D)) {
            getPlayer().motionY = -0.9D;
            getPlayer().posY = prevY;
            hop = false;
        }
        if ((getPlayer().moveForward != 0.0F) && (!getPlayer().isCollidedHorizontally)
                && (!getPlayer().isEating())) {
            if ((getPlayer().moveForward == 0.0F) && (getPlayer().moveStrafing == 0.0F)) {
                getPlayer().motionX = 0.0D;
                getPlayer().motionZ = 0.0D;
                if (getPlayer().isCollidedVertically) {
                    getPlayer().jump();
                    move = true;
                }
                if ((move) && (getPlayer().isCollidedVertically)) {
                    move = false;
                }
            }
            if (getPlayer().isCollidedVertically) {
                getPlayer().motionX *= 1.0379D;
                getPlayer().motionZ *= 1.0379D;
                doMiniHop();
            }
            if ((hop) && (!move) && (getPlayer().posY >= prevY + 0.399994D)) {
                getPlayer().motionY = -100.0D;
                getPlayer().posY = prevY;
                hop = false;
            }
        }
    }

    public boolean shouldSpeedUp() {
        boolean b = (getPlayer().movementInput.moveForward != 0.0F)
                || (getPlayer().movementInput.moveStrafe != 0.0F);
        return (!getPlayer().isInWater()) && (!PlayerUtil.isOnLiquid())
                && (!getPlayer().isCollidedHorizontally) && (!getPlayer().isSneaking())
                && (getPlayer().onGround) && (b);
    }

    public boolean defaultChecks() {
        return !getPlayer().isSneaking() && !getPlayer().isCollidedHorizontally && (getPlayer().moveForward != 0.0F || getPlayer().moveStrafing != 0.0F) && !mc.gameSettings.keyBindJump.isPressed() && !PlayerUtil.isOnLiquid() && !PlayerUtil.isInLiquid();
    }

    public Block getBlock(AxisAlignedBB bb) {
        int y = (int) bb.minY;
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null) {
                    return block;
                }
            }
        }
        return null;
    }

    public void onNewMove() {
        if (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) {

            getPlayer().motionX *= 0.4;
            getPlayer().motionZ *= 0.4;
        }
    }

    public boolean checks() {
        return !getPlayer().isSneaking() && !getPlayer().isCollidedHorizontally && (getPlayer().moveForward != 0.0F || getPlayer().moveStrafing != 0.0F) && !mc.gameSettings.keyBindJump.isPressed() && !PlayerUtil.isOnLiquid() && !PlayerUtil.isInLiquid();
    }

    public void onJumpUpdate() {
        if (((getPlayer().moveForward == 0.0F) && (getPlayer().moveStrafing == 0.0F))
                || (getPlayer().isCollidedHorizontally)) {
            return;
        }
        if (getPlayer().isCollidedVertically) {
            getPlayer().motionY = 0.145D;
            getPlayer().isAirBorne = true;
        } else if (getPlayer().isSprinting()) {
            float var1 = getPlayer().rotationYaw * 0.017453292F;
            getPlayer().motionX -= MathHelper.sin(var1) * 0.2D;
            getPlayer().motionZ += MathHelper.cos(var1) * 0.2D;
        }
    }

    public void onNewUpdate() {
        double speed = 3.15D;
        double slow = 1.49D;
        double offset = 4.9D;
        if ((mc.gameSettings.keyBindForward.isPressed())
                || (mc.gameSettings.keyBindBack.isPressed())
                || (mc.gameSettings.keyBindLeft.isPressed())
                || (mc.gameSettings.keyBindRight.isPressed())) {
            boolean shouldOffset = true;
            if ((getPlayer().onGround) && (prevYaw < 1.0F)) {
                prevYaw += 0.2F;
            }
            if (!getPlayer().onGround) {
                prevYaw = 0.0F;
            }
            if ((prevYaw == 1.0F) && (shouldSpeedUp())) {
                if (!getPlayer().isSprinting()) {
                    offset += 0.8D;
                }
                if (getPlayer().moveStrafing != 0.0F) {
                    speed -= 0.1D;
                    offset += 0.5D;
                }
                if (getPlayer().isInWater()) {
                    speed -= 0.1D;
                }
                double ncT = 1.15D;
                ticks += 1;
                if (ticks == 1) {
                    getPlayer().motionX *= speed;
                    getPlayer().motionZ *= speed;
                } else if (ticks == 2) {
                    mc.timer.timerSpeed = 1.0F;
                    getPlayer().motionX /= slow;
                    getPlayer().motionZ /= slow;
                } else if (ticks == 4) {
                    mc.timer.timerSpeed = 1.0F;
                    getPlayer().setPosition(getPlayer().posX + getPlayer().motionX / offset,
                            getPlayer().posY,
                            getPlayer().posZ + getPlayer().motionZ / offset);
                    ticks = 0;
                }
            }
        }
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
