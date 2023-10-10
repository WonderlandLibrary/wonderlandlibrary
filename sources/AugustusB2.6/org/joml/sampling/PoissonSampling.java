// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

import org.joml.Math;
import java.util.ArrayList;
import org.joml.Random;
import org.joml.Vector2f;

public class PoissonSampling
{
    public static class Disk
    {
        private final Vector2f[] grid;
        private final float diskRadius;
        private final float diskRadiusSquared;
        private final float minDist;
        private final float minDistSquared;
        private final float cellSize;
        private final int numCells;
        private final Random rnd;
        private final ArrayList processList;
        
        public Disk(final long seed, final float diskRadius, final float minDist, final int k, final Callback2d callback) {
            this.diskRadius = diskRadius;
            this.diskRadiusSquared = diskRadius * diskRadius;
            this.minDist = minDist;
            this.minDistSquared = minDist * minDist;
            this.rnd = new Random(seed);
            this.cellSize = minDist / (float)Math.sqrt(2.0);
            this.numCells = (int)(diskRadius * 2.0f / this.cellSize) + 1;
            this.grid = new Vector2f[this.numCells * this.numCells];
            this.processList = new ArrayList();
            this.compute(k, callback);
        }
        
        private void compute(final int k, final Callback2d callback) {
            float x;
            float y;
            do {
                x = this.rnd.nextFloat() * 2.0f - 1.0f;
                y = this.rnd.nextFloat() * 2.0f - 1.0f;
            } while (x * x + y * y > 1.0f);
            final Vector2f initial = new Vector2f(x, y);
            this.processList.add(initial);
            callback.onNewSample(initial.x, initial.y);
            this.insert(initial);
            while (!this.processList.isEmpty()) {
                final int i = this.rnd.nextInt(this.processList.size());
                final Vector2f sample = this.processList.get(i);
                boolean found = false;
                for (int s = 0; s < k; ++s) {
                    final float angle = this.rnd.nextFloat() * 6.2831855f;
                    final float radius = this.minDist * (this.rnd.nextFloat() + 1.0f);
                    x = (float)(radius * org.joml.sampling.Math.sin_roquen_9(angle + 1.5707963267948966));
                    y = (float)(radius * org.joml.sampling.Math.sin_roquen_9(angle));
                    x += sample.x;
                    y += sample.y;
                    if (x * x + y * y <= this.diskRadiusSquared) {
                        if (!this.searchNeighbors(x, y)) {
                            found = true;
                            callback.onNewSample(x, y);
                            final Vector2f f = new Vector2f(x, y);
                            this.processList.add(f);
                            this.insert(f);
                            break;
                        }
                    }
                }
                if (!found) {
                    this.processList.remove(i);
                }
            }
        }
        
        private boolean searchNeighbors(final float px, final float py) {
            final int row = (int)((py + this.diskRadius) / this.cellSize);
            final int col = (int)((px + this.diskRadius) / this.cellSize);
            if (this.grid[row * this.numCells + col] != null) {
                return true;
            }
            final int minX = Math.max(0, col - 1);
            final int minY = Math.max(0, row - 1);
            final int maxX = Math.min(col + 1, this.numCells - 1);
            for (int maxY = Math.min(row + 1, this.numCells - 1), y = minY; y <= maxY; ++y) {
                for (int x = minX; x <= maxX; ++x) {
                    final Vector2f v = this.grid[y * this.numCells + x];
                    if (v != null) {
                        final float dx = v.x - px;
                        final float dy = v.y - py;
                        if (dx * dx + dy * dy < this.minDistSquared) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        private void insert(final Vector2f p) {
            final int row = (int)((p.y + this.diskRadius) / this.cellSize);
            final int col = (int)((p.x + this.diskRadius) / this.cellSize);
            this.grid[row * this.numCells + col] = p;
        }
    }
}
