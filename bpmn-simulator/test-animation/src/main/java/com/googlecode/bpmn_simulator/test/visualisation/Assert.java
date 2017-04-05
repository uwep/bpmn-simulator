/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.test.visualisation;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.imageio.ImageIO;

public final class Assert {

	private static final int[] EQUAL_PIXEL = { 0 };
	private static final int[] UNEQUAL_PIXEL = { 1 };

	private Assert() {
	}

	private static BufferedImage loadImage(final File file) {
		final BufferedImage image;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (image == null) {
			throw new RuntimeException("no applicable ImageReader registered");
		}
		return image;
	}

	private static void storeImage(final File file, final RenderedImage image) {
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static BufferedImage createImage(final int type, final Component component) {
		final BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), type);
		component.printAll(image.getGraphics());
		return image;
	}

	private static void compareRaster(final Raster expected, final Raster actual) {
		final int width = expected.getWidth();
		final int height = expected.getHeight();
		assertEquals("different width", width, actual.getWidth());
		assertEquals("different height", height, actual.getHeight());
		final int[] expectedPixel = new int[expected.getNumDataElements()];
		final int[] actualPixel = new int[actual.getNumDataElements()];
		final BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		final WritableRaster diffRaster = diffImage.getRaster();
		int diffCount = 0;
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (!Arrays.equals(expected.getPixel(x, y, expectedPixel),
						expected.getPixel(x, y, actualPixel))) {
					diffRaster.setPixel(x, y, EQUAL_PIXEL);
				} else {
					diffRaster.setPixel(x, y, UNEQUAL_PIXEL);
				}
			}
		}
		storeImage(new File("diff.png"), diffImage);
		if (diffCount > 0) {
			final int sumCount = width * height;
			fail(MessageFormat.format("{0} from {1} pixels differ ({2,percent})", diffCount, sumCount, (100. / sumCount) * diffCount));
		}
	}

	private static void compareImages(final RenderedImage expected, final RenderedImage actual) {
		compareRaster(expected.getData(), actual.getData());
	}

	public static void assertEqualImages(final File expected, final Component actual) {
		final BufferedImage expectedImage = loadImage(expected);
		final BufferedImage actualImage = createImage(expectedImage.getType(), actual);
		compareImages(expectedImage, actualImage);
	}

}
