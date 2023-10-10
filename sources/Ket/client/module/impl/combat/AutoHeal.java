package client.module.impl.combat;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.PacketSendEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.PlayerUtil;
import client.util.liquidbounce.ItemUtils;
import client.value.impl.NumberValue;
import javafx.util.Pair;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

@ModuleInfo(name = "Auto Heal", description = "Automatically eats golden apples when your low hp", category = Category.COMBAT)
public class AutoHeal extends Module {

}