package com.enjoytheban.utils.render.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_SECONDARY_COLOR_ARRAY;
import static org.lwjgl.opengl.GL15.GL_FOG_COORD_ARRAY;

/**
 * Contains all valid GLenum ClientState caps.
 *
 * @see <a href="https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glEnableClientState.xml">glEnableClientState</a>
 * @see <a href="https://www.khronos.org/registry/OpenGL-Refpages/gl2.1/xhtml/glDisableClientState.xml">glDisableClientState</a>
 */
public enum GLClientState implements GLenum {

    COLOR("GL_COLOR_ARRAY", GL_COLOR_ARRAY),
    EDGE("GL_EDGE_FLAG_ARRAY", GL_EDGE_FLAG_ARRAY),
    FOG("GL_FOG_COORD_ARRAY", GL_FOG_COORD_ARRAY),
    INDEX("GL_INDEX_ARRAY", GL_INDEX_ARRAY),
    NORMAL("GL_NORMAL_ARRAY", GL_NORMAL_ARRAY),
    SECONDARY_COLOR("GL_SECONDARY_COLOR_ARRAY", GL_SECONDARY_COLOR_ARRAY),
    TEXTURE("GL_TEXTURE_COORD_ARRAY", GL_TEXTURE_COORD_ARRAY),
    VERTEX("GL_VERTEX_ARRAY", GL_VERTEX_ARRAY);

    private final String name;
    private final int cap;

    GLClientState(String name, int cap) {
        this.name = name;
        this.cap = cap;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getCap() {
        return this.cap;
    }
}
