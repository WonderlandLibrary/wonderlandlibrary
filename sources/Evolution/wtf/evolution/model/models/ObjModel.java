package wtf.evolution.model.models;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.*;
import java.util.*;

public abstract class ObjModel extends Model {

    public List<ObjObject> objObjects;
    protected String filename;

    ObjModel() {
        this.objObjects = new ArrayList();
    }

    public ObjModel(String classpathElem) {
        this();
        this.filename = classpathElem;
        if (this.filename.contains("/")) {
            this.setID(this.filename.substring(this.filename.lastIndexOf("/") + 1));
        } else {
            this.setID(this.filename);
        }

    }

    protected byte[] read(InputStream resource) throws IOException {
        byte[] buffer = new byte[65565];
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int i;
        while ((i = resource.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, i);
        }

        out.flush();
        out.close();
        return out.toByteArray();
    }

    public void renderGroup(ObjObject group) {
        if (this.fireEvent((new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_GROUP)).setData(new Object[]{group, group}))) {
            this.renderGroupImpl(group);
        }

        this.fireEvent((new ObjEvent(this, ObjEvent.EventType.POST_RENDER_GROUP)).setData(new Object[]{group, group}));
    }

    public void renderGroups(String groupsName) {
        if (this.fireEvent((new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_GROUPS)).setData(new Object[]{groupsName}))) {
            this.renderGroupsImpl(groupsName);
        }

        this.fireEvent((new ObjEvent(this, ObjEvent.EventType.POST_RENDER_GROUPS)).setData(new Object[]{groupsName}));
    }

    public void render() {
        if (this.fireEvent(new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_ALL))) {
            this.renderImpl();
        }

        this.fireEvent(new ObjEvent(this, ObjEvent.EventType.POST_RENDER_ALL));
    }

    protected abstract void renderGroupsImpl(String var1);

    protected abstract void renderGroupImpl(ObjObject var1);

    protected abstract void renderImpl();

    public abstract boolean fireEvent(ObjEvent var1);
}
