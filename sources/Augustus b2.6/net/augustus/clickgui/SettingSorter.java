// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventClickGui;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class SettingSorter implements MC, MM, SM
{
    @EventTarget
    public void onEventClickGui(final EventClickGui eventClickGui) {
        SettingSorter.mm.arrayList.backGroundColor.setVisible(SettingSorter.mm.arrayList.backGround.getBoolean());
        SettingSorter.mm.arrayList.staticColor.setVisible(!SettingSorter.mm.arrayList.randomColor.getBoolean() && !SettingSorter.mm.arrayList.rainbow.getBoolean());
        SettingSorter.mm.arrayList.randomColor.setVisible(!SettingSorter.mm.arrayList.rainbow.getBoolean());
        SettingSorter.mm.arrayList.rainbow.setVisible(!SettingSorter.mm.arrayList.randomColor.getBoolean());
        SettingSorter.mm.arrayList.rainbowSpeed.setVisible(SettingSorter.mm.arrayList.rainbow.getBoolean());
        SettingSorter.mm.killAura.maxDelay.setValue(Math.max(SettingSorter.mm.killAura.minDelay.getValue(), SettingSorter.mm.killAura.maxDelay.getValue()));
        SettingSorter.mm.killAura.minDelay.setValue(Math.min(SettingSorter.mm.killAura.minDelay.getValue(), SettingSorter.mm.killAura.maxDelay.getValue()));
        if (SettingSorter.mm.killAura.mode.getSelected().equals("Advanced")) {
            SettingSorter.mm.killAura.coolDown.setVisible(true);
            SettingSorter.mm.killAura.targetRandom.setVisible(true);
            SettingSorter.mm.killAura.rangeMode.setVisible(true);
            SettingSorter.mm.killAura.blockMode.setVisible(true);
            SettingSorter.mm.killAura.attackMode.setVisible(true);
            SettingSorter.mm.killAura.yawSpeedMin.setVisible(true);
            SettingSorter.mm.killAura.yawSpeedMax.setVisible(true);
            SettingSorter.mm.killAura.pitchSpeedMin.setVisible(true);
            SettingSorter.mm.killAura.pitchSpeedMax.setVisible(true);
            SettingSorter.mm.killAura.randomStrength.setVisible(true);
            SettingSorter.mm.killAura.interpolation.setVisible(true);
            SettingSorter.mm.killAura.smoothBackRotate.setVisible(true);
            SettingSorter.mm.killAura.stopOnTarget.setVisible(true);
            SettingSorter.mm.killAura.smartAim.setVisible(true);
            SettingSorter.mm.killAura.inInv.setVisible(false);
            SettingSorter.mm.killAura.randomize.setVisible(false);
            SettingSorter.mm.killAura.multi.setVisible(false);
            SettingSorter.mm.killAura.targetRandom.setVisible(true);
            SettingSorter.mm.killAura.hitChance.setVisible(true);
            SettingSorter.mm.killAura.block.setVisible(false);
            SettingSorter.mm.killAura.moveFix.setVisible(true);
            SettingSorter.mm.killAura.heuristics.setVisible(true);
            SettingSorter.mm.killAura.preHit.setVisible(true);
            SettingSorter.mm.killAura.advancedRots.setVisible(true);
            SettingSorter.mm.killAura.hazeAdd.setVisible(SettingSorter.mm.killAura.hazeRange.getBoolean());
            SettingSorter.mm.killAura.hazeMax.setVisible(SettingSorter.mm.killAura.hazeRange.getBoolean());
            SettingSorter.mm.killAura.hazeRange.setVisible(true);
            SettingSorter.mm.killAura.perfectHit.setVisible(true);
            SettingSorter.mm.killAura.perfectHitGomme.setVisible(SettingSorter.mm.killAura.perfectHit.isVisible() && SettingSorter.mm.killAura.perfectHit.getBoolean());
            SettingSorter.mm.killAura.yawSpeedMax.setValue(Math.max(SettingSorter.mm.killAura.yawSpeedMin.getValue(), SettingSorter.mm.killAura.yawSpeedMax.getValue()));
            SettingSorter.mm.killAura.yawSpeedMin.setValue(Math.min(SettingSorter.mm.killAura.yawSpeedMin.getValue(), SettingSorter.mm.killAura.yawSpeedMax.getValue()));
            SettingSorter.mm.killAura.pitchSpeedMax.setValue(Math.max(SettingSorter.mm.killAura.pitchSpeedMin.getValue(), SettingSorter.mm.killAura.pitchSpeedMax.getValue()));
            SettingSorter.mm.killAura.pitchSpeedMin.setValue(Math.min(SettingSorter.mm.killAura.pitchSpeedMin.getValue(), SettingSorter.mm.killAura.pitchSpeedMax.getValue()));
            SettingSorter.mm.killAura.silentMoveFix.setVisible(SettingSorter.mm.killAura.moveFix.getBoolean());
            SettingSorter.mm.killAura.intave.setVisible(true);
            if (SettingSorter.mm.killAura.blockMode.getSelected().equalsIgnoreCase("Custom")) {
                SettingSorter.mm.killAura.unblockHit.setVisible(true);
                SettingSorter.mm.killAura.unblockHitOnly.setVisible(SettingSorter.mm.killAura.unblockHit.getBoolean());
                SettingSorter.mm.killAura.startBlock.setVisible(SettingSorter.mm.killAura.unblockHit.getBoolean() && !SettingSorter.mm.killAura.unblockHitOnly.getBoolean());
                SettingSorter.mm.killAura.endBlock.setVisible(SettingSorter.mm.killAura.unblockHit.getBoolean() && !SettingSorter.mm.killAura.unblockHitOnly.getBoolean());
                SettingSorter.mm.killAura.endBlockHitOnly.setVisible(SettingSorter.mm.killAura.unblockHitOnly.getBoolean() && SettingSorter.mm.killAura.unblockHit.getBoolean());
            }
            else {
                SettingSorter.mm.killAura.unblockHit.setVisible(false);
                SettingSorter.mm.killAura.unblockHitOnly.setVisible(false);
                SettingSorter.mm.killAura.startBlock.setVisible(false);
                SettingSorter.mm.killAura.endBlock.setVisible(false);
                SettingSorter.mm.killAura.endBlockHitOnly.setVisible(false);
            }
        }
        else {
            SettingSorter.mm.killAura.hazeAdd.setVisible(false);
            SettingSorter.mm.killAura.hazeMax.setVisible(false);
            SettingSorter.mm.killAura.hazeRange.setVisible(false);
            SettingSorter.mm.killAura.minDelay.setVisible(true);
            SettingSorter.mm.killAura.maxDelay.setVisible(true);
            SettingSorter.mm.killAura.perfectHit.setVisible(false);
            SettingSorter.mm.killAura.perfectHitGomme.setVisible(false);
            SettingSorter.mm.killAura.coolDown.setVisible(false);
            SettingSorter.mm.killAura.multi.setVisible(true);
            SettingSorter.mm.killAura.targetRandom.setVisible(false);
            SettingSorter.mm.killAura.rangeMode.setVisible(false);
            SettingSorter.mm.killAura.blockMode.setVisible(false);
            SettingSorter.mm.killAura.attackMode.setVisible(false);
            SettingSorter.mm.killAura.yawSpeedMin.setVisible(false);
            SettingSorter.mm.killAura.yawSpeedMax.setVisible(false);
            SettingSorter.mm.killAura.pitchSpeedMin.setVisible(false);
            SettingSorter.mm.killAura.pitchSpeedMax.setVisible(false);
            SettingSorter.mm.killAura.randomStrength.setVisible(false);
            SettingSorter.mm.killAura.interpolation.setVisible(false);
            SettingSorter.mm.killAura.smoothBackRotate.setVisible(false);
            SettingSorter.mm.killAura.stopOnTarget.setVisible(false);
            SettingSorter.mm.killAura.moveFix.setVisible(false);
            SettingSorter.mm.killAura.silentMoveFix.setVisible(false);
            SettingSorter.mm.killAura.smartAim.setVisible(false);
            SettingSorter.mm.killAura.inInv.setVisible(true);
            SettingSorter.mm.killAura.randomize.setVisible(true);
            SettingSorter.mm.killAura.targetRandom.setVisible(false);
            SettingSorter.mm.killAura.hitChance.setVisible(false);
            SettingSorter.mm.killAura.startBlock.setVisible(false);
            SettingSorter.mm.killAura.endBlock.setVisible(false);
            SettingSorter.mm.killAura.unblockHit.setVisible(false);
            SettingSorter.mm.killAura.unblockHitOnly.setVisible(false);
            SettingSorter.mm.killAura.endBlockHitOnly.setVisible(false);
            SettingSorter.mm.killAura.block.setVisible(true);
            SettingSorter.mm.killAura.heuristics.setVisible(false);
            SettingSorter.mm.killAura.preHit.setVisible(false);
            SettingSorter.mm.killAura.advancedRots.setVisible(false);
            SettingSorter.mm.killAura.minDelay.setVisible(true);
            SettingSorter.mm.killAura.maxDelay.setVisible(true);
            SettingSorter.mm.killAura.intave.setVisible(false);
        }
        SettingSorter.mm.velocity.ignoreExplosion.setVisible(SettingSorter.mm.velocity.mode.getSelected().equalsIgnoreCase("Basic"));
        SettingSorter.mm.velocity.YValue.setVisible(SettingSorter.mm.velocity.mode.getSelected().equalsIgnoreCase("Basic"));
        SettingSorter.mm.velocity.XZValue.setVisible(SettingSorter.mm.velocity.mode.getSelected().equalsIgnoreCase("Basic"));
        SettingSorter.mm.velocity.XZValueIntave.setVisible(SettingSorter.mm.velocity.mode.getSelected().equalsIgnoreCase("Intave"));
        SettingSorter.mm.velocity.jumpIntave.setVisible(SettingSorter.mm.velocity.mode.getSelected().equalsIgnoreCase("Intave"));
        SettingSorter.mm.velocity.pushStart.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Push"));
        SettingSorter.mm.velocity.pushEnd.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Push"));
        SettingSorter.mm.velocity.pushXZ.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Push"));
        SettingSorter.mm.velocity.pushOnGround.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Push"));
        SettingSorter.mm.velocity.reverseStart.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Reverse"));
        SettingSorter.mm.velocity.reverseStrafe.setVisible(SettingSorter.mm.velocity.mode.getSelected().equals("Reverse"));
        SettingSorter.mm.autoArmor.hotbar.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("OpenInv"));
        SettingSorter.mm.autoArmor.noMove.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("SpoofInv"));
        SettingSorter.mm.autoArmor.interactionCheck.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("SpoofInv"));
        if (SettingSorter.mm.autoArmor.hotbar.isVisible()) {
            SettingSorter.mm.autoArmor.gommeQSG.setVisible(SettingSorter.mm.autoArmor.hotbar.getBoolean());
            SettingSorter.mm.autoArmor.hotbarStartDelay.setVisible(SettingSorter.mm.autoArmor.hotbar.getBoolean());
            SettingSorter.mm.autoArmor.hotbarDelay.setVisible(SettingSorter.mm.autoArmor.hotbar.getBoolean());
        }
        else {
            SettingSorter.mm.autoArmor.gommeQSG.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("OpenInv"));
            SettingSorter.mm.autoArmor.hotbarStartDelay.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("OpenInv"));
            SettingSorter.mm.autoArmor.hotbarDelay.setVisible(SettingSorter.mm.autoArmor.mode.getSelected().equals("OpenInv"));
        }
        SettingSorter.mm.inventoryCleaner.noMove.setVisible(SettingSorter.mm.inventoryCleaner.mode.getSelected().equals("SpoofInv"));
        SettingSorter.mm.inventoryCleaner.interactionCheck.setVisible(SettingSorter.mm.inventoryCleaner.mode.getSelected().equals("SpoofInv"));
        SettingSorter.mm.noFall.fallDistance.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("OnGround"));
        SettingSorter.mm.noFall.lookRange.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("Legit"));
        SettingSorter.mm.noFall.yawSpeed.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("Legit"));
        SettingSorter.mm.noFall.pitchSpeed.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("Legit"));
        SettingSorter.mm.noFall.delay.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("Legit"));
        SettingSorter.mm.noFall.legitFallDistance.setVisible(SettingSorter.mm.noFall.mode.getSelected().equals("Legit"));
        SettingSorter.mm.jesus.speed.setVisible(SettingSorter.mm.jesus.mode.getSelected().equals("Speed"));
        SettingSorter.mm.nameTags.height.setVisible(!SettingSorter.mm.nameTags.mode.getSelected().equals("None"));
        SettingSorter.mm.nameTags.armor.setVisible(!SettingSorter.mm.nameTags.mode.getSelected().equals("None"));
        SettingSorter.mm.nameTags.scale.setVisible(!SettingSorter.mm.nameTags.mode.getSelected().equals("None"));
        SettingSorter.mm.tracers.color.setVisible(SettingSorter.mm.tracers.staticColor.getBoolean());
        SettingSorter.mm.hud.color.setVisible(!SettingSorter.mm.hud.mode.getSelected().equals("None"));
        SettingSorter.mm.hud.backGround.setVisible(!SettingSorter.mm.hud.mode.getSelected().equals("None"));
        SettingSorter.mm.hud.backGroundColor.setVisible(!SettingSorter.mm.hud.mode.getSelected().equals("None") && SettingSorter.mm.hud.backGround.getBoolean());
        SettingSorter.mm.hud.size.setVisible(SettingSorter.mm.hud.mode.getSelected().equals("Augustus"));
        SettingSorter.mm.speed.vanillaSpeed.setVisible(SettingSorter.mm.speed.mode.getSelected().equals("VOnGround") || SettingSorter.mm.speed.mode.getSelected().equals("VBhop"));
        SettingSorter.mm.speed.vanillaHeight.setVisible(SettingSorter.mm.speed.mode.getSelected().equals("VBhop"));
        SettingSorter.mm.speed.damageBoost.setVisible(SettingSorter.mm.speed.mode.getSelected().equals("Verus"));
        SettingSorter.mm.speed.dmgSpeed.setVisible(SettingSorter.mm.speed.mode.getSelected().equals("Verus"));
        if (!SettingSorter.mm.esp.rainbow.getBoolean()) {
            SettingSorter.mm.esp.color.setVisible(!SettingSorter.mm.esp.mode.getSelected().equals("Vanilla"));
            SettingSorter.mm.esp.outlineColor.setVisible(SettingSorter.mm.esp.mode.getSelected().equals("Vanilla"));
        }
        else {
            SettingSorter.mm.esp.color.setVisible(false);
            SettingSorter.mm.esp.outlineColor.setVisible(false);
        }
        SettingSorter.mm.esp.rainbowSpeed.setVisible(SettingSorter.mm.esp.rainbow.getBoolean());
        SettingSorter.mm.esp.rainbowAlpha.setVisible(SettingSorter.mm.esp.rainbow.getBoolean() && SettingSorter.mm.esp.mode.getSelected().equals("Box"));
        SettingSorter.mm.esp.lineWidth.setVisible(SettingSorter.mm.esp.mode.getSelected().equals("Box"));
        SettingSorter.mm.esp.otherColorOnHit.setVisible(!SettingSorter.mm.esp.mode.getSelected().equals("Vanilla"));
        SettingSorter.mm.esp.hitColor.setVisible(SettingSorter.mm.esp.otherColorOnHit.getBoolean() && !SettingSorter.mm.esp.mode.getSelected().equals("Vanilla"));
        SettingSorter.mm.scoreboard.yCord.setVisible(!SettingSorter.mm.scoreboard.remove.getBoolean() && !SettingSorter.mm.scoreboard.stick.getBoolean());
        SettingSorter.mm.scoreboard.xCord.setVisible(!SettingSorter.mm.scoreboard.remove.getBoolean());
        SettingSorter.mm.scoreboard.stick.setVisible(!SettingSorter.mm.scoreboard.remove.getBoolean());
        SettingSorter.mm.fucker.instant.setVisible(SettingSorter.mm.fucker.action.getSelected().equals("Break"));
        SettingSorter.mm.scaffoldWalk.adStrafe.setVisible(SettingSorter.mm.scaffoldWalk.mode.getSelected().equals("Legit"));
        SettingSorter.mm.antiBot.ticksExisted.setVisible(SettingSorter.mm.antiBot.mode.getSelected().equals("Custom"));
        SettingSorter.mm.autoSoup.autoClose.setVisible(SettingSorter.mm.autoSoup.fill.getBoolean());
        SettingSorter.mm.noSlow.swordForward.setVisible(SettingSorter.mm.noSlow.swordSlowdown.getBoolean());
        SettingSorter.mm.noSlow.swordStrafe.setVisible(SettingSorter.mm.noSlow.swordSlowdown.getBoolean());
        SettingSorter.mm.noSlow.bowForward.setVisible(SettingSorter.mm.noSlow.bowSlowdown.getBoolean());
        SettingSorter.mm.noSlow.bowStrafe.setVisible(SettingSorter.mm.noSlow.bowSlowdown.getBoolean());
        SettingSorter.mm.noSlow.restForward.setVisible(SettingSorter.mm.noSlow.restSlowdown.getBoolean());
        SettingSorter.mm.noSlow.restStrafe.setVisible(SettingSorter.mm.noSlow.restSlowdown.getBoolean());
        SettingSorter.mm.noSlow.timerSword.setVisible(SettingSorter.mm.noSlow.swordTimer.getBoolean());
        SettingSorter.mm.noSlow.timerBow.setVisible(SettingSorter.mm.noSlow.bowTimer.getBoolean());
        SettingSorter.mm.noSlow.timerRest.setVisible(SettingSorter.mm.noSlow.restTimer.getBoolean());
        SettingSorter.mm.line.lineWidth.setVisible(SettingSorter.mm.line.line.getBoolean());
        SettingSorter.mm.line.color.setVisible(SettingSorter.mm.line.line.getBoolean());
        SettingSorter.mm.line.lineTime.setVisible(SettingSorter.mm.line.line.getBoolean());
        SettingSorter.mm.line.killAuraLineTime.setVisible(SettingSorter.mm.line.killAura.getBoolean());
        SettingSorter.mm.line.killAuraColor.setVisible(SettingSorter.mm.line.killAura.getBoolean());
        SettingSorter.mm.line.killAuraLineWidth.setVisible(SettingSorter.mm.line.killAura.getBoolean());
        SettingSorter.mm.fly.autoJump.setVisible(SettingSorter.mm.fly.mode.getSelected().equals("AirJump"));
        SettingSorter.mm.fly.sendOnGroundPacket.setVisible(SettingSorter.mm.fly.mode.getSelected().equals("AirJump"));
        SettingSorter.mm.fly.speed.setVisible(SettingSorter.mm.fly.mode.getSelected().equals("Vanilla"));
        SettingSorter.mm.fly.verusspeed.setVisible(SettingSorter.mm.fly.mode.getSelected().equals("Verus"));
        SettingSorter.mm.spider.motion.setVisible(SettingSorter.mm.spider.mode.getSelected().equals("Basic") || (SettingSorter.mm.spider.mode.getSelected().equals("Jump") && SettingSorter.mm.spider.customJumpMotion.getBoolean()));
        SettingSorter.mm.spider.onGroundPacket.setVisible(SettingSorter.mm.spider.mode.getSelected().equals("Jump"));
        SettingSorter.mm.spider.motionToJump.setVisible(SettingSorter.mm.spider.mode.getSelected().equals("Jump"));
        SettingSorter.mm.spider.customJumpMotion.setVisible(SettingSorter.mm.spider.mode.getSelected().equals("Jump"));
        SettingSorter.mm.storageESP.color.setVisible(!SettingSorter.mm.storageESP.rainbow.getBoolean());
        SettingSorter.mm.storageESP.lineWidth.setVisible(SettingSorter.mm.storageESP.mode.getSelected().equals("Box") || SettingSorter.mm.storageESP.mode.getSelected().equals("OtherBox"));
        SettingSorter.mm.storageESP.rainbowAlpha.setVisible(SettingSorter.mm.storageESP.rainbow.getBoolean() && (SettingSorter.mm.storageESP.mode.getSelected().equals("Box") || SettingSorter.mm.storageESP.mode.getSelected().equals("OtherBox")));
        SettingSorter.mm.storageESP.rainbowSpeed.setVisible(SettingSorter.mm.storageESP.rainbow.getBoolean());
        SettingSorter.mm.blockESP.rainbowAlpha.setVisible(SettingSorter.mm.blockESP.rainbow.getBoolean());
        SettingSorter.mm.blockESP.rainbowSpeed.setVisible(SettingSorter.mm.blockESP.rainbow.getBoolean());
        SettingSorter.mm.blockESP.color.setVisible(!SettingSorter.mm.blockESP.rainbow.getBoolean());
        SettingSorter.mm.tracers.color.setVisible(!SettingSorter.mm.tracers.rainbow.getBoolean() && !SettingSorter.mm.tracers.staticColor.getBoolean());
        SettingSorter.mm.tracers.rainbow.setVisible(!SettingSorter.mm.tracers.staticColor.getBoolean());
        SettingSorter.mm.tracers.staticColor.setVisible(!SettingSorter.mm.tracers.rainbow.getBoolean());
        SettingSorter.mm.tracers.rainbowSpeed.setVisible(SettingSorter.mm.tracers.rainbow.getBoolean());
        SettingSorter.mm.crossHair.color.setVisible(!SettingSorter.mm.crossHair.rainbow.getBoolean());
        SettingSorter.mm.crossHair.rainbowSpeed.setVisible(SettingSorter.mm.crossHair.rainbow.getBoolean());
        SettingSorter.mm.blockFly.predict.setVisible(SettingSorter.mm.blockFly.latestRotate.getBoolean() && SettingSorter.mm.blockFly.rayCast.getBoolean() && SettingSorter.mm.blockFly.latestRotate.isVisible());
        SettingSorter.mm.blockFly.backupTicks.setVisible(SettingSorter.mm.blockFly.latestRotate.getBoolean() && SettingSorter.mm.blockFly.latestRotate.isVisible());
        SettingSorter.mm.blockFly.latestRotate.setVisible(SettingSorter.mm.blockFly.rayCast.getBoolean());
        SettingSorter.mm.blockFly.latestPlace.setVisible(SettingSorter.mm.blockFly.latestRotate.getBoolean() && SettingSorter.mm.blockFly.rayCast.getBoolean());
        SettingSorter.mm.blockFly.playerYaw.setVisible(SettingSorter.mm.blockFly.rayCast.getBoolean());
        SettingSorter.mm.blockFly.moonWalk.setVisible(SettingSorter.mm.blockFly.playerYaw.isVisible() && SettingSorter.mm.blockFly.playerYaw.getBoolean());
        SettingSorter.mm.blockFly.rotateToBlock.setVisible(!SettingSorter.mm.blockFly.rayCast.getBoolean());
        SettingSorter.mm.blockFly.correctSide.setVisible(SettingSorter.mm.blockFly.rotateToBlock.isVisible() && SettingSorter.mm.blockFly.rotateToBlock.getBoolean());
        SettingSorter.mm.blockFly.sneakDelayBool.setVisible(SettingSorter.mm.blockFly.sneak.getBoolean());
        SettingSorter.mm.blockFly.sneakDelay.setVisible(SettingSorter.mm.blockFly.sneakDelayBool.isVisible() && SettingSorter.mm.blockFly.sneakDelayBool.getBoolean());
        SettingSorter.mm.blockFly.sneakBlocks.setVisible(!SettingSorter.mm.blockFly.sneakDelayBool.getBoolean() && SettingSorter.mm.blockFly.sneak.getBoolean());
        SettingSorter.mm.blockFly.sneakBlocksDiagonal.setVisible(!SettingSorter.mm.blockFly.sneakDelayBool.getBoolean() && SettingSorter.mm.blockFly.sneak.getBoolean());
        SettingSorter.mm.blockFly.sneakTicks.setVisible(SettingSorter.mm.blockFly.sneak.getBoolean());
        SettingSorter.mm.blockFly.sneakOnPlace.setVisible(SettingSorter.mm.blockFly.sneak.getBoolean());
        SettingSorter.mm.blockFly.adStrafe.setVisible(SettingSorter.mm.blockFly.playerYaw.getBoolean() && !SettingSorter.mm.blockFly.moonWalk.getBoolean());
        SettingSorter.mm.blockFly.adStrafeLegit.setVisible(SettingSorter.mm.blockFly.adStrafe.getBoolean() && SettingSorter.mm.blockFly.playerYaw.getBoolean() && !SettingSorter.mm.blockFly.moonWalk.getBoolean());
        SettingSorter.mm.blockFly.spamClickDelay.setVisible(SettingSorter.mm.blockFly.spamClick.getBoolean() && !SettingSorter.mm.blockFly.intaveHit.getBoolean());
        SettingSorter.mm.blockFly.intaveHit.setVisible(SettingSorter.mm.blockFly.spamClick.getBoolean());
        SettingSorter.mm.blockFly.sameY.setVisible(SettingSorter.mm.blockFly.rayCast.getBoolean());
        SettingSorter.mm.blockFly.blockSafe.setVisible(SettingSorter.mm.blockFly.playerYaw.getBoolean());
        SettingSorter.mm.backTrack.targets.setVisible(!SettingSorter.mm.backTrack.onlyKillAura.getBoolean());
        SettingSorter.mm.backTrack.range.setVisible(!SettingSorter.mm.backTrack.onlyKillAura.getBoolean());
        SettingSorter.mm.customGlint.customColor.setVisible(!SettingSorter.mm.customGlint.removeGlint.getBoolean());
        SettingSorter.mm.customGlint.glintSpeed.setVisible(!SettingSorter.mm.customGlint.removeGlint.getBoolean());
        SettingSorter.mm.customGlint.color.setVisible(SettingSorter.mm.customGlint.customColor.getBoolean() && SettingSorter.mm.customGlint.customColor.isVisible());
        SettingSorter.mm.antiFireBall.range.setVisible(!SettingSorter.mm.antiFireBall.rotate.getBoolean());
        SettingSorter.mm.antiFireBall.yawSpeed.setVisible(SettingSorter.mm.antiFireBall.rotate.getBoolean());
        SettingSorter.mm.antiFireBall.pitchSpeed.setVisible(SettingSorter.mm.antiFireBall.rotate.getBoolean());
        SettingSorter.mm.antiFireBall.slowDown.setVisible(SettingSorter.mm.antiFireBall.rotate.getBoolean());
        SettingSorter.mm.antiFireBall.moveFix.setVisible(SettingSorter.mm.antiFireBall.rotate.getBoolean());
    }
}
