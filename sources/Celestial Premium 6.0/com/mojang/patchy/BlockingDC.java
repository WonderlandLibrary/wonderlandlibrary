/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.patchy;

import java.util.Hashtable;
import java.util.function.Predicate;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class BlockingDC
implements DirContext {
    private final Predicate<String> blockList;
    private final DirContext parent;

    public BlockingDC(Predicate<String> blockList, DirContext parent) {
        this.blockList = blockList;
        this.parent = parent;
    }

    @Override
    public Attributes getAttributes(String name, String[] attrIds) throws NamingException {
        if (this.blockList.test(name)) {
            return new BasicAttributes();
        }
        return this.parent.getAttributes(name, attrIds);
    }

    @Override
    public Attributes getAttributes(String name) throws NamingException {
        if (this.blockList.test(name)) {
            return new BasicAttributes();
        }
        return this.parent.getAttributes(name);
    }

    @Override
    public Attributes getAttributes(Name name) throws NamingException {
        return this.parent.getAttributes(name);
    }

    @Override
    public Attributes getAttributes(Name name, String[] attrIds) throws NamingException {
        return this.parent.getAttributes(name, attrIds);
    }

    @Override
    public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws NamingException {
        this.parent.modifyAttributes(name, mod_op, attrs);
    }

    @Override
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException {
        this.parent.modifyAttributes(name, mod_op, attrs);
    }

    @Override
    public void modifyAttributes(Name name, ModificationItem[] mods) throws NamingException {
        this.parent.modifyAttributes(name, mods);
    }

    @Override
    public void modifyAttributes(String name, ModificationItem[] mods) throws NamingException {
        this.parent.modifyAttributes(name, mods);
    }

    @Override
    public void bind(Name name, Object obj, Attributes attrs) throws NamingException {
        this.parent.bind(name, obj, attrs);
    }

    @Override
    public void bind(String name, Object obj, Attributes attrs) throws NamingException {
        this.parent.bind(name, obj, attrs);
    }

    @Override
    public void rebind(Name name, Object obj, Attributes attrs) throws NamingException {
        this.parent.rebind(name, obj, attrs);
    }

    @Override
    public void rebind(String name, Object obj, Attributes attrs) throws NamingException {
        this.parent.rebind(name, obj, attrs);
    }

    @Override
    public DirContext createSubcontext(Name name, Attributes attrs) throws NamingException {
        return this.parent.createSubcontext(name, attrs);
    }

    @Override
    public DirContext createSubcontext(String name, Attributes attrs) throws NamingException {
        return this.parent.createSubcontext(name, attrs);
    }

    @Override
    public DirContext getSchema(Name name) throws NamingException {
        return this.parent.getSchema(name);
    }

    @Override
    public DirContext getSchema(String name) throws NamingException {
        return this.parent.getSchema(name);
    }

    @Override
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        return this.parent.getSchemaClassDefinition(name);
    }

    @Override
    public DirContext getSchemaClassDefinition(String name) throws NamingException {
        return this.parent.getSchemaClassDefinition(name);
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return this.parent.search(name, matchingAttributes, attributesToReturn);
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return this.parent.search(name, matchingAttributes, attributesToReturn);
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, Attributes matchingAttributes) throws NamingException {
        return this.parent.search(name, matchingAttributes);
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes) throws NamingException {
        return this.parent.search(name, matchingAttributes);
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, String filter, SearchControls cons) throws NamingException {
        return this.parent.search(name, filter, cons);
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, String filter, SearchControls cons) throws NamingException {
        return this.parent.search(name, filter, cons);
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return this.parent.search(name, filterExpr, filterArgs, cons);
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return this.parent.search(name, filterExpr, filterArgs, cons);
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        return this.parent.lookup(name);
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return this.parent.lookup(name);
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        this.parent.bind(name, obj);
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        this.parent.bind(name, obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        this.parent.rebind(name, obj);
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        this.parent.rebind(name, obj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        this.parent.unbind(name);
    }

    @Override
    public void unbind(String name) throws NamingException {
        this.parent.unbind(name);
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        this.parent.rename(oldName, newName);
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        this.parent.rename(oldName, newName);
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return this.parent.list(name);
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return this.parent.list(name);
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return this.parent.listBindings(name);
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return this.parent.listBindings(name);
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        this.parent.destroySubcontext(name);
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        this.parent.destroySubcontext(name);
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return this.parent.createSubcontext(name);
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return this.parent.createSubcontext(name);
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return this.parent.lookupLink(name);
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return this.parent.lookupLink(name);
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        return this.parent.getNameParser(name);
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return this.parent.getNameParser(name);
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        return this.parent.composeName(name, prefix);
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        return this.parent.composeName(name, prefix);
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return this.parent.addToEnvironment(propName, propVal);
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        return this.parent.removeFromEnvironment(propName);
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return this.parent.getEnvironment();
    }

    @Override
    public void close() throws NamingException {
        this.parent.close();
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return this.parent.getNameInNamespace();
    }
}

