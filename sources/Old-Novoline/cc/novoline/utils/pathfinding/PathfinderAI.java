package cc.novoline.utils.pathfinding;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PathfinderAI {

    private static int getDiffX(@NonNull Entity e1, @NonNull Entity e2) {
        return (int) Math.abs(e1.posX - e2.posX);
    }

    private static int getDiffZ(@NonNull Entity e1, @NonNull Entity e2) {
        return (int) Math.abs(e1.posZ - e2.posZ);
    }

    private static int getDiffX(BlockPos pos, BlockPos pos2) {
        return pos.getX() - pos2.getX();
    }

    private static int getDiffZ(BlockPos pos, BlockPos pos2) {
        return pos.getZ() - pos2.getZ();
    }

    private static int getDiffY(BlockPos pos, BlockPos pos2) {
        return pos.getY() - pos2.getY();
    }


    public static List<Point> getPath(Entity e1, Entity e2) {
        List<Point> points = new ObjectArrayList<>();
        double period = 2D;
        double distance = Math.pow(Math.pow(e2.posX - e1.posX, 2) + Math.pow(e2.posY - e1.posY, 2) + Math.pow(e2.posZ - e1.posZ, 2), 0.5D);
        double stepCount = distance / period;

        final double stepX = (e2.posX - e1.posX) / stepCount, stepY = (e2.posY - e1.posY) / stepCount, stepZ = (e2.posZ - e1.posZ) / stepCount;
        double x = 0, y = 0, z = 0;

        while (true) {
            final double n = stepCount;
            stepCount = n - 1.0;

            if (n <= 0.0) {
                break;
            }
            Point e = new Point((int) (e1.posX + (x += stepX)), (int) (e1.posY + (y += stepY)), (int) (e1.posZ + (z += stepZ)));
            points.add(e);
        }
        return points.stream().distinct().collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private static int lastPosX, lastPosZ;

    public static List<Point> getPathFromPosToPos(BlockPos pos, BlockPos pos2) {
        List<Point> points = new ObjectArrayList<>();
        double interval = 10D;
        if (getDiffX(pos, pos2) > 0) {
            for (int i = 0; i <= getDiffX(pos, pos2); i++) {
                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ()))) break;
                if (i % interval == 0) {
                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ()));
                    if (i == getDiffX(pos, pos2) - (getDiffX(pos, pos2) - (int) (getDiffX(pos, pos2) / interval) * interval)) {
                        if (getDiffZ(pos, pos2) > 0) {
                            for (int b = 0; b <= getDiffZ(pos, pos2); b++) {
                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b))) break;
                                if (b % interval == 0) {
                                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b));
                                    if (b == getDiffZ(pos, pos2) - (getDiffZ(pos, pos2) - (int) (getDiffZ(pos, pos2) / interval) * interval)) {
                                        if (getDiffY(pos, pos2) > 0) {
                                            for (int c = 0; c <= getDiffY(pos, pos2); c++) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        } else {
                                            for (int c = 0; c >= getDiffY(pos, pos2); c--) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int b = 0; b >= getDiffZ(pos, pos2); b--) {
                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b))) break;
                                if (b % interval == 0) {
                                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b));
                                    if (b == getDiffZ(pos, pos2) - (getDiffZ(pos, pos2) - (int) (getDiffZ(pos, pos2) / interval) * interval)) {
                                        if (getDiffY(pos, pos2) > 0) {
                                            for (int c = 0; c <= getDiffY(pos, pos2); c++) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        } else {
                                            for (int c = 0; c >= getDiffY(pos, pos2); c--) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        } else {
            for (int i = 0; i >= getDiffX(pos, pos2); i--) {
                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ()))) break;
                if (i % interval == 0) {
                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ()));
                    if (i == getDiffX(pos, pos2) - (getDiffX(pos, pos2) - (int) (getDiffX(pos, pos2) / interval) * interval)) {
                        if (getDiffZ(pos, pos2) > 0) {
                            for (int b = 0; b <= getDiffZ(pos, pos2); b++) {
                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b))) break;
                                if (b % interval == 0) {
                                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b));
                                    if (b == getDiffZ(pos, pos2) - (getDiffZ(pos, pos2) - (int) (getDiffZ(pos, pos2) / interval) * interval)) {
                                        if (getDiffY(pos, pos2) > 0) {
                                            for (int c = 0; c <= getDiffY(pos, pos2); c++) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        } else {
                                            for (int c = 0; c >= getDiffY(pos, pos2); c--) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0)
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int b = 0; b >= getDiffZ(pos, pos2); b--) {
                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b))) break;
                                if (b % interval == 0) {
                                    points.add(new Point(pos.getX() - i, pos.getY(), pos.getZ() - b));
                                    if (b == getDiffZ(pos, pos2) - (getDiffZ(pos, pos2) - (int) (getDiffZ(pos, pos2) / interval) * interval)) {
                                        if (getDiffY(pos, pos2) > 0) {
                                            for (int c = 0; c <= getDiffY(pos, pos2); c++) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        } else {
                                            for (int c = 0; c >= getDiffY(pos, pos2); c--) {
                                                if (isPointNotOkay(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b)))
                                                    break;
                                                if (c % 2 == 0) {
                                                    points.add(new Point(pos.getX() - i, pos.getY() - c, pos.getZ() - b));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return points.stream().distinct().collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private static boolean isPointNotOkay(Point point) {
        final Block block = Minecraft.getInstance().world.getBlock(point.getPosX(), point.getPosY(), point.getPosZ());
        return !(block instanceof BlockAir) && !(block instanceof BlockGrass) && !(block instanceof BlockTallGrass);
    }

    public static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getInstance().world.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder
                || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    public static ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }

        AStarCustomPathfinder pathfinder = new AStarCustomPathfinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();

        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }

                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 5 * 5) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:

                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathfinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

}
