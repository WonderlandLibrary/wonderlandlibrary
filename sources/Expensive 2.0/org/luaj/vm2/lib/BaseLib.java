/*******************************************************************************
 * Copyright (c) 2009 Luaj.org. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package org.luaj.vm2.lib;

import java.io.IOException;
import java.io.InputStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import wtf.expensive.util.ClientUtil;

/**
 * Subclass of {@link LibFunction} which implements the lua basic library functions.
 * <p>
 * This contains all library functions listed as "basic functions" in the lua documentation for JME.
 * The functions dofile and loadfile use the
 * {@link Globals#finder} instance to find resource files.
 * Since JME has no file system by default, {@link BaseLib} implements
 * {@link ResourceFinder} using {@link Class#getResource(String)},
 * which is the closest equivalent on JME.
 * The default loader chain in {@link PackageLib} will use these as well.
 * <p>
 * To use basic library functions that include a {@link ResourceFinder} based on
 * directory lookup, use {@link org.luaj.vm2.lib.jse.JseBaseLib} instead.
 * <p>
 * Typically, this library is included as part of a call to either
 * {@link org.luaj.vm2.lib.jse.JsePlatform#standardGlobals()} or
 * {@link org.luaj.vm2.lib.jme.JmePlatform#standardGlobals()}
 * <pre> {@code
 * Globals globals = JsePlatform.standardGlobals();
 * globals.get("print").call(LuaValue.valueOf("hello, world"));
 * } </pre>
 * <p>
 * For special cases where the smallest possible footprint is desired,
 * a minimal set of libraries could be loaded
 * directly via {@link Globals#load(LuaValue)} using code such as:
 * <pre> {@code
 * Globals globals = new Globals();
 * globals.load(new JseBaseLib());
 * globals.get("print").call(LuaValue.valueOf("hello, world"));
 * } </pre>
 * Doing so will ensure the library is properly initialized
 * and loaded into the globals table.
 * <p>
 * This is a direct port of the corresponding library in C.
 * @see org.luaj.vm2.lib.jse.JseBaseLib
 * @see ResourceFinder
 * @see Globals#finder
 * @see LibFunction
 * @see org.luaj.vm2.lib.jse.JsePlatform
 * @see org.luaj.vm2.lib.jme.JmePlatform
 * @see <a href="http://www.lua.org/manual/5.2/manual.html#6.1">Lua 5.2 Base Lib Reference</a>
 */
public class BaseLib extends TwoArgFunction implements ResourceFinder {

	Globals globals;


	/** Perform one-time initialization on the library by adding base functions
	 * to the supplied environment, and returning it as the return value.
	 * @param modname the module name supplied if this is loaded via 'require'.
	 * @param env the environment to load into, which must be a Globals instance.
	 */
	public LuaValue call(LuaValue modname, LuaValue env) {
		globals = env.checkglobals();
		globals.finder = this;
		globals.baselib = this;
		env.set("print", new print(this));
		env.set("tonumber", new tonumber());
		env.set("tostring", new tostring());
		env.set("error", new error());
		return env;
	}

	/** ResourceFinder implementation
	 *
	 * Tries to open the file as a resource, which can work for JSE and JME.
	 */
	public InputStream findResource(String filename) {
		return getClass().getResourceAsStream(filename.startsWith("/")? filename: "/"+filename);
	}


	// "assert", // ( v [,message] ) -> v, message | ERR
	static final class _assert extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			if ( !args.arg1().toboolean() )
				error( args.narg()>1? args.optjstring(2,"assertion failed!"): "assertion failed!" );
			return args;
		}
	}

	// "collectgarbage", // ( opt [,arg] ) -> value
	static final class collectgarbage extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			String s = args.optjstring(1, "collect");
			if ( "collect".equals(s) ) {
				System.gc();
				return ZERO;
			} else if ( "count".equals(s) ) {
				Runtime rt = Runtime.getRuntime();
				long used = rt.totalMemory() - rt.freeMemory();
				return varargsOf(valueOf(used/1024.), valueOf(used%1024));
			} else if ( "step".equals(s) ) {
				System.gc();
				return LuaValue.TRUE;
			} else {
				argerror(1, "invalid option '" + s + "'");
			}
			return NIL;
		}
	}

	// "error", // ( message [,level] ) -> ERR
	static final class error extends TwoArgFunction {
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
			if (arg1.isnil()) throw new LuaError(NIL);
			if (!arg1.isstring() || arg2.optint(1) == 0) throw new LuaError(arg1);
			throw new LuaError(arg1.tojstring(), arg2.optint(1));
		}
	}




	// "print", // (...) -> void
	final class print extends VarArgFunction {
		final BaseLib baselib;
		print(BaseLib baselib) {
			this.baselib = baselib;
		}
		public Varargs invoke(Varargs args) {
			LuaValue tostring = globals.get("tostring");
			for ( int i=1, n=args.narg(); i<=n; i++ ) {
				LuaString s = tostring.call( args.arg(i) ).strvalue();
				ClientUtil.sendMesage(s.tojstring());
			}
			return NONE;
		}
	}


	// "tonumber", // (e [,base]) -> value
	static final class tonumber extends LibFunction {
		public LuaValue call(LuaValue e) {
			return e.tonumber();
		}
		public LuaValue call(LuaValue e, LuaValue base) {
			if (base.isnil())
				return e.tonumber();
			final int b = base.checkint();
			if ( b < 2 || b > 36 )
				argerror(2, "base out of range");
			return e.checkstring().tonumber(b);
		}
	}

	// "tostring", // (e) -> value
	static final class tostring extends LibFunction {
		public LuaValue call(LuaValue arg) {
			LuaValue h = arg.metatag(TOSTRING);
			if ( ! h.isnil() )
				return h.call(arg);
			LuaValue v = arg.tostring();
			if ( ! v.isnil() )
				return v;
			return valueOf(arg.tojstring());
		}
	}



	public Varargs loadStream(InputStream is, String chunkname, String mode, LuaValue env) {
		try {
			if ( is == null )
				return varargsOf(NIL, valueOf("not found: "+chunkname));
			return globals.load(is, chunkname, mode, env);
		} catch (Exception e) {
			return varargsOf(NIL, valueOf(e.getMessage()));
		}
	}
}
