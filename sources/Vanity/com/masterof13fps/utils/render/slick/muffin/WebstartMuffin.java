package com.masterof13fps.utils.render.slick.muffin;

import java.io.IOException;
import java.util.HashMap;

/**
 * A muffin load/save implementation based on using Webstart Muffins (a bit like cookies only 
 * for webstart)
 * 
 * @author kappaOne
 */
public class WebstartMuffin implements Muffin {

	/**
	 * @see Muffin#saveFile(HashMap, String)
	 */
	public void saveFile(HashMap scoreMap, String fileName) throws IOException {

	}

	/**
	 * @see Muffin#loadFile(String)
	 */
	public HashMap loadFile(String fileName) throws IOException {
		return null;
	}
}
