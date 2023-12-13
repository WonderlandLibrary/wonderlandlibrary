package me.aidanmees.trivia.client.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Combat.AutoBlock;
import me.aidanmees.trivia.client.modules.Combat.Criticals;
import me.aidanmees.trivia.client.target.AuraUtils;
import me.aidanmees.trivia.client.settings.ClientSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandEntityData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ColorUtil {

	
	
//	static ArrayList<Vec3> positions = new ArrayList<Vec3>();
//	static ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	
	


}