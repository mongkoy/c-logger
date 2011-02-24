/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id: ImageManager.java,v 1.5 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.ui.image;

import java.awt.MediaTracker;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Manages loading and caching images
 */
public class ImageManager {

	/**
	 * Singleton instance
	 */
	private static ImageManager instance = new ImageManager();
	/**
	 * Cache of images
	 */
	private HashMap images = null;

	/**
	 * Private constructor to support singleton
	 */
	private ImageManager() {
		images = new HashMap();
	}

	/**
	 * Singleton accessor
	 * @return the singleton instance
	 */
	public static ImageManager getInstance() {
		return instance;
	}

	/**
	 * Loads a cached image from a class offset from the ImageManager
	 * waiting for it to be completely loading using a MediaTracker
	 * @param imageName the name of the image to load
	 * @return the cached image
	 */
	public ImageIcon getImage(String imageName) {
		ImageIcon image = null;

		if (images.containsKey(imageName)) {
			image = (ImageIcon) images.get(imageName);
		} else {
			URL url = getClass().getResource(imageName);

			if (url == null) {
				//System.out.println("ImageManager.getImage() could not find image: " + imageName);
			} else {
				image = new ImageIcon(getClass().getResource(imageName));

				while (image.getImageLoadStatus() == MediaTracker.LOADING) {
					try {
						Thread.currentThread().wait(100);
					} catch (InterruptedException e) {
					}
				}

				if (image != null && image.getImageLoadStatus() == MediaTracker.COMPLETE) {
					images.put(imageName, image);
				} else {
					//System.out.println("ImageManager.getImage() failed to load image: " + imageName);
				}
			}
		}

		return image;
	}
}
