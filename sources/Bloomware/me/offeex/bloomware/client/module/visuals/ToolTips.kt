package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object ToolTips : Module("ToolTips", "Changes default mc tooltips", Category.RENDER) {
	val itemInfo = setting("Item").bool(true)
	val maps = setting("Maps").bool(true)
}