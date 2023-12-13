/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.ArrayList;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.navmesh.NavMesh;
import org.newdawn.slick.util.pathfinding.navmesh.Space;

public class NavMeshBuilder
implements PathFindingContext {
    private int sx;
    private int sy;
    private float smallestSpace = 0.2f;
    private boolean tileBased;

    public NavMesh build(TileBasedMap map) {
        return this.build(map, true);
    }

    public NavMesh build(TileBasedMap map, boolean tileBased) {
        this.tileBased = tileBased;
        ArrayList<Space> spaces = new ArrayList<Space>();
        if (tileBased) {
            for (int x2 = 0; x2 < map.getWidthInTiles(); ++x2) {
                for (int y2 = 0; y2 < map.getHeightInTiles(); ++y2) {
                    if (map.blocked(this, x2, y2)) continue;
                    spaces.add(new Space(x2, y2, 1.0f, 1.0f));
                }
            }
        } else {
            Space space = new Space(0.0f, 0.0f, map.getWidthInTiles(), map.getHeightInTiles());
            this.subsection(map, space, spaces);
        }
        while (this.mergeSpaces(spaces)) {
        }
        this.linkSpaces(spaces);
        return new NavMesh(spaces);
    }

    private boolean mergeSpaces(ArrayList spaces) {
        for (int source = 0; source < spaces.size(); ++source) {
            Space a2 = (Space)spaces.get(source);
            for (int target = source + 1; target < spaces.size(); ++target) {
                Space b2 = (Space)spaces.get(target);
                if (!a2.canMerge(b2)) continue;
                spaces.remove(a2);
                spaces.remove(b2);
                spaces.add(a2.merge(b2));
                return true;
            }
        }
        return false;
    }

    private void linkSpaces(ArrayList spaces) {
        for (int source = 0; source < spaces.size(); ++source) {
            Space a2 = (Space)spaces.get(source);
            for (int target = source + 1; target < spaces.size(); ++target) {
                Space b2 = (Space)spaces.get(target);
                if (!a2.hasJoinedEdge(b2)) continue;
                a2.link(b2);
                b2.link(a2);
            }
        }
    }

    public boolean clear(TileBasedMap map, Space space) {
        if (this.tileBased) {
            return true;
        }
        float x2 = 0.0f;
        boolean donex = false;
        while (x2 < space.getWidth()) {
            float y2 = 0.0f;
            boolean doney = false;
            while (y2 < space.getHeight()) {
                this.sx = (int)(space.getX() + x2);
                this.sy = (int)(space.getY() + y2);
                if (map.blocked(this, this.sx, this.sy)) {
                    return false;
                }
                if (!((y2 += 0.1f) > space.getHeight()) || doney) continue;
                y2 = space.getHeight();
                doney = true;
            }
            if (!((x2 += 0.1f) > space.getWidth()) || donex) continue;
            x2 = space.getWidth();
            donex = true;
        }
        return true;
    }

    private void subsection(TileBasedMap map, Space space, ArrayList spaces) {
        if (!this.clear(map, space)) {
            float width2 = space.getWidth() / 2.0f;
            float height2 = space.getHeight() / 2.0f;
            if (width2 < this.smallestSpace && height2 < this.smallestSpace) {
                return;
            }
            this.subsection(map, new Space(space.getX(), space.getY(), width2, height2), spaces);
            this.subsection(map, new Space(space.getX(), space.getY() + height2, width2, height2), spaces);
            this.subsection(map, new Space(space.getX() + width2, space.getY(), width2, height2), spaces);
            this.subsection(map, new Space(space.getX() + width2, space.getY() + height2, width2, height2), spaces);
        } else {
            spaces.add(space);
        }
    }

    public Mover getMover() {
        return null;
    }

    public int getSearchDistance() {
        return 0;
    }

    public int getSourceX() {
        return this.sx;
    }

    public int getSourceY() {
        return this.sy;
    }
}

