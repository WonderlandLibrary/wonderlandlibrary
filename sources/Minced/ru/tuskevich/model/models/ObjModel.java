// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import java.io.IOException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ObjModel extends Model
{
    public List<ObjObject> objObjects;
    protected String filename;
    
    ObjModel() {
        this.objObjects = new ArrayList<ObjObject>();
    }
    
    public ObjModel(final String classpathElem) {
        this();
        this.filename = classpathElem;
        if (this.filename.contains("/")) {
            this.setID(this.filename.substring(this.filename.lastIndexOf("/") + 1));
        }
        else {
            this.setID(this.filename);
        }
    }
    
    protected byte[] read(final InputStream resource) throws IOException {
        final byte[] buffer = new byte[65565];
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = resource.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, i);
        }
        out.flush();
        out.close();
        return out.toByteArray();
    }
    
    public void renderGroup(final ObjObject group) {
        if (this.fireEvent(new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_GROUP).setData(group, group))) {
            this.renderGroupImpl(group);
        }
        this.fireEvent(new ObjEvent(this, ObjEvent.EventType.POST_RENDER_GROUP).setData(group, group));
    }
    
    @Override
    public void renderGroups(final String groupsName) {
        if (this.fireEvent(new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_GROUPS).setData(groupsName))) {
            this.renderGroupsImpl(groupsName);
        }
        this.fireEvent(new ObjEvent(this, ObjEvent.EventType.POST_RENDER_GROUPS).setData(groupsName));
    }
    
    @Override
    public void render() {
        if (this.fireEvent(new ObjEvent(this, ObjEvent.EventType.PRE_RENDER_ALL))) {
            this.renderImpl();
        }
        this.fireEvent(new ObjEvent(this, ObjEvent.EventType.POST_RENDER_ALL));
    }
    
    protected abstract void renderGroupsImpl(final String p0);
    
    protected abstract void renderGroupImpl(final ObjObject p0);
    
    protected abstract void renderImpl();
    
    public abstract boolean fireEvent(final ObjEvent p0);
}
