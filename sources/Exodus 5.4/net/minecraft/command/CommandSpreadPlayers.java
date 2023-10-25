/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandSpreadPlayers
extends CommandBase {
    private Position[] func_110670_a(Random random, int n, double d, double d2, double d3, double d4) {
        Position[] positionArray = new Position[n];
        int n2 = 0;
        while (n2 < positionArray.length) {
            Position position = new Position();
            position.func_111097_a(random, d, d2, d3, d4);
            positionArray[n2] = position;
            ++n2;
        }
        return positionArray;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        }
        int n = 0;
        BlockPos blockPos = iCommandSender.getPosition();
        double d = CommandSpreadPlayers.parseDouble(blockPos.getX(), stringArray[n++], true);
        double d2 = CommandSpreadPlayers.parseDouble(blockPos.getZ(), stringArray[n++], true);
        double d3 = CommandSpreadPlayers.parseDouble(stringArray[n++], 0.0);
        double d4 = CommandSpreadPlayers.parseDouble(stringArray[n++], d3 + 1.0);
        boolean bl = CommandSpreadPlayers.parseBoolean(stringArray[n++]);
        ArrayList arrayList = Lists.newArrayList();
        while (n < stringArray.length) {
            Object object;
            String string;
            if (PlayerSelector.hasArguments(string = stringArray[n++])) {
                object = PlayerSelector.matchEntities(iCommandSender, string, Entity.class);
                if (object.size() == 0) {
                    throw new EntityNotFoundException();
                }
                arrayList.addAll(object);
                continue;
            }
            object = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(string);
            if (object == null) {
                throw new PlayerNotFoundException();
            }
            arrayList.add(object);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, arrayList.size());
        if (arrayList.isEmpty()) {
            throw new EntityNotFoundException();
        }
        iCommandSender.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.spreading." + (bl ? "teams" : "players"), arrayList.size(), d4, d, d2, d3));
        this.func_110669_a(iCommandSender, arrayList, new Position(d, d2), d3, d4, ((Entity)arrayList.get((int)0)).worldObj, bl);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length >= 1 && stringArray.length <= 2 ? CommandSpreadPlayers.func_181043_b(stringArray, 0, blockPos) : null;
    }

    private double func_110671_a(List<Entity> list, World world, Position[] positionArray, boolean bl) {
        double d = 0.0;
        int n = 0;
        HashMap hashMap = Maps.newHashMap();
        int n2 = 0;
        while (n2 < list.size()) {
            Position position;
            Entity entity = list.get(n2);
            if (bl) {
                Team team;
                Team team2 = team = entity instanceof EntityPlayer ? ((EntityPlayer)entity).getTeam() : null;
                if (!hashMap.containsKey(team)) {
                    hashMap.put(team, positionArray[n++]);
                }
                position = (Position)hashMap.get(team);
            } else {
                position = positionArray[n++];
            }
            entity.setPositionAndUpdate((float)MathHelper.floor_double(position.field_111101_a) + 0.5f, position.func_111092_a(world), (double)MathHelper.floor_double(position.field_111100_b) + 0.5);
            double d2 = Double.MAX_VALUE;
            int n3 = 0;
            while (n3 < positionArray.length) {
                if (position != positionArray[n3]) {
                    double d3 = position.func_111099_a(positionArray[n3]);
                    d2 = Math.min(d3, d2);
                }
                ++n3;
            }
            d += d2;
            ++n2;
        }
        return d /= (double)list.size();
    }

    private int func_110667_a(List<Entity> list) {
        HashSet hashSet = Sets.newHashSet();
        for (Entity entity : list) {
            if (entity instanceof EntityPlayer) {
                hashSet.add(((EntityPlayer)entity).getTeam());
                continue;
            }
            hashSet.add(null);
        }
        return hashSet.size();
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.spreadplayers.usage";
    }

    @Override
    public String getCommandName() {
        return "spreadplayers";
    }

    private void func_110669_a(ICommandSender iCommandSender, List<Entity> list, Position position, double d, double d2, World world, boolean bl) throws CommandException {
        Random random = new Random();
        double d3 = position.field_111101_a - d2;
        double d4 = position.field_111100_b - d2;
        double d5 = position.field_111101_a + d2;
        double d6 = position.field_111100_b + d2;
        Position[] positionArray = this.func_110670_a(random, bl ? this.func_110667_a(list) : list.size(), d3, d4, d5, d6);
        int n = this.func_110668_a(position, d, world, random, d3, d4, d5, d6, positionArray, bl);
        double d7 = this.func_110671_a(list, world, positionArray, bl);
        CommandSpreadPlayers.notifyOperators(iCommandSender, (ICommand)this, "commands.spreadplayers.success." + (bl ? "teams" : "players"), positionArray.length, position.field_111101_a, position.field_111100_b);
        if (positionArray.length > 1) {
            iCommandSender.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.info." + (bl ? "teams" : "players"), String.format("%.2f", d7), n));
        }
    }

    private int func_110668_a(Position position, double d, World world, Random random, double d2, double d3, double d4, double d5, Position[] positionArray, boolean bl) throws CommandException {
        boolean bl2 = true;
        double d6 = 3.4028234663852886E38;
        int n = 0;
        while (n < 10000 && bl2) {
            Object object;
            int n2;
            bl2 = false;
            d6 = 3.4028234663852886E38;
            int n3 = 0;
            while (n3 < positionArray.length) {
                Position position2 = positionArray[n3];
                n2 = 0;
                object = new Position();
                int n4 = 0;
                while (n4 < positionArray.length) {
                    if (n3 != n4) {
                        Position position3 = positionArray[n4];
                        double d7 = position2.func_111099_a(position3);
                        d6 = Math.min(d7, d6);
                        if (d7 < d) {
                            ++n2;
                            ((Position)object).field_111101_a += position3.field_111101_a - position2.field_111101_a;
                            ((Position)object).field_111100_b += position3.field_111100_b - position2.field_111100_b;
                        }
                    }
                    ++n4;
                }
                if (n2 > 0) {
                    ((Position)object).field_111101_a /= (double)n2;
                    ((Position)object).field_111100_b /= (double)n2;
                    double d8 = ((Position)object).func_111096_b();
                    if (d8 > 0.0) {
                        ((Position)object).func_111095_a();
                        position2.func_111094_b((Position)object);
                    } else {
                        position2.func_111097_a(random, d2, d3, d4, d5);
                    }
                    bl2 = true;
                }
                if (position2.func_111093_a(d2, d3, d4, d5)) {
                    bl2 = true;
                }
                ++n3;
            }
            if (!bl2) {
                object = positionArray;
                n2 = positionArray.length;
                int n5 = 0;
                while (n5 < n2) {
                    Position position4 = object[n5];
                    if (!position4.func_111098_b(world)) {
                        position4.func_111097_a(random, d2, d3, d4, d5);
                        bl2 = true;
                    }
                    ++n5;
                }
            }
            ++n;
        }
        if (n >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (bl ? "teams" : "players"), positionArray.length, position.field_111101_a, position.field_111100_b, String.format("%.2f", d6));
        }
        return n;
    }

    static class Position {
        double field_111101_a;
        double field_111100_b;

        float func_111096_b() {
            return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
        }

        public boolean func_111093_a(double d, double d2, double d3, double d4) {
            boolean bl = false;
            if (this.field_111101_a < d) {
                this.field_111101_a = d;
                bl = true;
            } else if (this.field_111101_a > d3) {
                this.field_111101_a = d3;
                bl = true;
            }
            if (this.field_111100_b < d2) {
                this.field_111100_b = d2;
                bl = true;
            } else if (this.field_111100_b > d4) {
                this.field_111100_b = d4;
                bl = true;
            }
            return bl;
        }

        public boolean func_111098_b(World world) {
            BlockPos blockPos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (blockPos.getY() > 0) {
                Material material = world.getBlockState(blockPos = blockPos.down()).getBlock().getMaterial();
                if (material == Material.air) continue;
                return !material.isLiquid() && material != Material.fire;
            }
            return false;
        }

        void func_111095_a() {
            double d = this.func_111096_b();
            this.field_111101_a /= d;
            this.field_111100_b /= d;
        }

        public void func_111097_a(Random random, double d, double d2, double d3, double d4) {
            this.field_111101_a = MathHelper.getRandomDoubleInRange(random, d, d3);
            this.field_111100_b = MathHelper.getRandomDoubleInRange(random, d2, d4);
        }

        public void func_111094_b(Position position) {
            this.field_111101_a -= position.field_111101_a;
            this.field_111100_b -= position.field_111100_b;
        }

        public int func_111092_a(World world) {
            BlockPos blockPos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (blockPos.getY() > 0) {
                if (world.getBlockState(blockPos = blockPos.down()).getBlock().getMaterial() == Material.air) continue;
                return blockPos.getY() + 1;
            }
            return 257;
        }

        double func_111099_a(Position position) {
            double d = this.field_111101_a - position.field_111101_a;
            double d2 = this.field_111100_b - position.field_111100_b;
            return Math.sqrt(d * d + d2 * d2);
        }

        Position() {
        }

        Position(double d, double d2) {
            this.field_111101_a = d;
            this.field_111100_b = d2;
        }
    }
}

