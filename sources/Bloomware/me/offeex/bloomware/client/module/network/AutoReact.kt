package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventAttack
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import java.util.*

object AutoReact : Module("AutoReact", "Reacts on something...", Category.NETWORK) {
	private val msgsMode = setting("MessagesMode").enum("ClientSide", "Monkey")
	private val targetRange = setting("Range").number(12.0, 4.0, 64.0, 0.1)
	private val targetFriends = setting("TargetFriends").bool(true)
	private val actions = setting("Actions").group()
	private val kills = actions.setting("Kills").bool(true)
	private val pops = actions.setting("Pops").bool(true)
	private val logs = actions.setting("Kills").bool(true)
	private val delays = setting("Delays").group()
	private val killDelay = delays.setting("Kill").number(1.0, 0.0, 5.0, 0.05)
	private val popDelay = delays.setting("Pop").number(1.0, 0.0, 5.0, 0.05)
	private val logDelay = delays.setting("Log").number(1.0, 0.0, 5.0, 0.05)

	private val killMsgs = arrayOf(
		"just has been killed", "died to you", "has been put down", "was assassinated", "just got murdered"
	)
	private val popMsgs = arrayOf(
		"just got popped, keep going", "lost his totem, what a waste", "missed your attack"
	)
	private val logMsgs = arrayOf(
		"logged. What a bad player..",
		"decided to give up",
		"couldn't his fate, and logged",
		"did the worst. he logged in PvP"
	)
	private val monkeyKillMsgs = arrayOf(
		"is so ezzz, he just died to me",
		"ezzzz DIED",
		"SO BAD",
		"imagine being SO bad in PvP",
		"shitter lol",
		"stay mad"
	)
	private val monkeyPopMsgs = arrayOf(
		"EZ POP lol",
		"why are you popping",
		"imagine popping totem",
		"popped AGAIN",
		"literally just popped",
		"popped, cope harder"
	)
	private val monkeyLogMsgs = arrayOf(
		"EZZZ LOG",
		"keep logging slut",
		"just rage quit XDD",
		"i just made him log",
		"imagine logging",
		"is crying rn"
	)
	private var target: PlayerEntity? = null
	private val random: Random? = null
	private val isPopping = false

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is EntityStatusS2CPacket) {
			val packet = event.packet
			if (packet.status.toInt() == 35 && packet.getEntity(mc.world) === target) Action.KILL.act = true
		}
	}

	@Subscribe
	private fun onAttackEntity(event: EventAttack.Entity) {
		if (event.entity.isPlayer) target = event.entity as PlayerEntity
	}

	override fun onTick() {
		if (Action.KILL.act) sendMsg(Action.KILL)
		if (Action.POP.act) sendMsg(Action.POP)
		if (Action.LOG.act) sendMsg(Action.LOG)
	}

	private fun sendMsg(action: Action) {
		val arr = when (action) {
			Action.KILL -> if (msgsMode.like("Monkey")) monkeyKillMsgs.clone() else killMsgs.clone()
			Action.POP -> if (msgsMode.like("Monkey")) monkeyPopMsgs.clone() else popMsgs.clone()
			Action.LOG -> if (msgsMode.like("Monkey")) monkeyLogMsgs.clone() else logMsgs.clone()
		}
		//        sendMessage(arr[(int) (Math.random() * arr.length - 1)]);
		action.act = false
	}

	private enum class Action {
		KILL, POP, LOG;

		var act = false
	}
}