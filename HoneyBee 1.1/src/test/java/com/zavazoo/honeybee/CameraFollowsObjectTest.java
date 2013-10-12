/*
 * Zavazoo HoneyBee 1.0 - Java API for 3D game development with transform
 * matrices
 * Copyright (C) 2012-2013 Chris White <chriswhitelondon@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.zavazoo.honeybee;

import com.zavazoo.honeybee.BeeColony;
import com.zavazoo.honeybee.HoneyBee;

import junit.framework.TestCase;

/**
 * Tests the utilisation of HoneyBee transform matrices and BeeColony operations
 * to simulate the classic scenario of the camera following an object such as
 * the protagonist in a video game.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
public class CameraFollowsObjectTest extends TestCase {

	/** The number of decimal places to which values are rounded for comparison. */
	private static int SCALE = 3;

	/**
	 * The number of units of global space that the camera is behind the object.
	 */
	private static double BEHIND = 3;

	/** The number of units of global space that the camera is above the object. */
	private static double ABOVE = 1;

	/**
	 * Simulates and tests the classic scenario of the camera following an
	 * object where the camera is always 3 units of space behind and 1 unit of
	 * space above the object.
	 */
	public void testCameraFollowsObject() {

		// object at the origin of the global axes

		double[][] object = HoneyBee.createTransformForOrigin();

		double[][] camera = BeeColony
				.cameraFollowsObject(object, BEHIND, ABOVE);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(camera);

		double[] expectedTranslation = new double[] { 0.0, -3.0, 1.0 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(camera);

		double[][] expectedRotation = new double[][] {
				new double[] { 1.0, 0.0, -0.0 },
				new double[] { 0.0, 0.316228151321, 0.948683142662 },
				new double[] { 0.0, -0.948683142662, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform global rotation of 0,0,20

		object = HoneyBee.rotateAroundGlobalAxes(object, 0, 0, 20);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { 1.02606034279, -2.8190779686, 1.0 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.939692616463, 0.342020094395, 0.0 },
				new double[] { -0.108156383038, 0.297157257795, 0.948683142662 },
				new double[] { 0.32446873188, -0.891470611095, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local translation of 0,3,0

		object = HoneyBee.translateAlongLocalAxes(object, 0, 3, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -4.60703063254e-07,
				-3.93938734078e-07, 1.0 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.939692616463, 0.342020094395, 0.0 },
				new double[] { -0.108156383038, 0.297157257795, 0.948683142662 },
				new double[] { 0.32446873188, -0.891470611095, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local rotation of 0,10,0

		object = HoneyBee.rotateAroundLocalAxes(object, 0, 10, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -4.60703063254e-07,
				-3.93938734078e-07, 1.0 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.939692616463, 0.342020094395, 0.0 },
				new double[] { -0.108156383038, 0.297157257795, 0.948683142662 },
				new double[] { 0.32446873188, -0.891470611095, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local translation of 0.5,0,0

		object = HoneyBee.translateAlongLocalAxes(object, 0.5, 0, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { 0.462707787752, 0.168411776423,
				0.913175880909 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.939692616463, 0.342020094395, 0.0 },
				new double[] { -0.108156383038, 0.297157257795, 0.948683142662 },
				new double[] { 0.32446873188, -0.891470611095, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform global rotation of 0,0,-32.4

		object = HoneyBee.rotateAroundGlobalAxes(object, 0, 0, -32.4);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -1.20755887032, 0.0574729442596,
				0.913175821304 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local rotation of 13.7,0,0

		object = HoneyBee.rotateAroundLocalAxes(object, 13.7, 0, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -1.20755887032, 0.0574729442596,
				0.913175821304 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local translation of 0,0,1.9

		object = HoneyBee.translateAlongLocalAxes(object, 0, 0, 1.9);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -0.9911211133, -0.450854361057,
				2.73107528687 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform local translation of -2.3,0,0

		object = HoneyBee.translateAlongLocalAxes(object, -2.3, 0, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -3.20334076881, 0.0355333723128,
				3.13046622276 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform global translation of 1.2,2.3,0

		object = HoneyBee.translateAlongGlobalAxes(object, 1.2, 2.3, 0);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -2.00334072113, 2.33553338051,
				3.13046622276 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

		// perform global translation of -6,-7,-4

		object = HoneyBee.translateAlongGlobalAxes(object, -6, -7, -4);

		camera = BeeColony.cameraFollowsObject(object, BEHIND, ABOVE);

		actualTranslation = HoneyBee.getTranslationFromTransform(camera);

		expectedTranslation = new double[] { -8.00334072113, -4.66446685791,
				-0.869533836842 };

		passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		actualRotation = HoneyBee.getRotationFromTransform(camera);

		expectedRotation = new double[][] {
				new double[] { 0.976672232151, -0.214735463262,
						-1.65507589855e-08 },
				new double[] { 0.0679054111242, 0.308851242065, 0.948683142662 },
				new double[] { -0.203715905547, -0.926552534103, 0.316228151321 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

}