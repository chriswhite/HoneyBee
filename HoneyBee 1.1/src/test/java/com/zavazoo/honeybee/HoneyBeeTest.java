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

import com.zavazoo.honeybee.HoneyBee;

import junit.framework.TestCase;

/**
 * Tests various combinations of rotation and translation of HoneyBee transform
 * matrices.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
public class HoneyBeeTest extends TestCase {

	/** The number of decimal places to which values are rounded for comparison. */
	private static int SCALE = 3;

	/** The sine and cosine functions applied to various angles. */
	private double SIN15 = Math.sin(Math.toRadians(15));
	private double COS15 = Math.cos(Math.toRadians(15));
	private double SIN20 = Math.sin(Math.toRadians(20));
	private double COS20 = Math.cos(Math.toRadians(20));
	private double SIN30 = Math.sin(Math.toRadians(30));
	private double COS30 = Math.cos(Math.toRadians(30));

	/**
	 * Tests global rotation of 0,0,-30 then global rotation of 0,-40,0 then
	 * local translation of 4,0,0
	 */
	public void testRotateGlobal0_0_m30RotateGlobal0_m40_0TranslateLocal4_0_0() {

		double[][] matrix = HoneyBee.createTransformForOrigin();

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, 0, -30);

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, -40, 0);

		matrix = HoneyBee.translateAlongLocalAxes(matrix, 4, 0, 0);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(matrix);

		double[] expectedTranslation = new double[] {
				4 - (8 * SIN15 * SIN15) - (8 * SIN20 * SIN20 * COS30),
				(-8 * SIN15 * COS15), 8 * SIN20 * COS20 * COS30 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(matrix);

		double[][] expectedRotation = new double[][] {
				new double[] { 0.663414001465, -0.499999940395, 0.556670427322 },
				new double[] { 0.38302218914, 0.866025447845, 0.321393728256 },
				new double[] { -0.642787575722, 1.60373581082e-08,
						0.766044437885 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

	/**
	 * Tests global rotation of 0,0,-30 then local rotation of 0,-40,0 then
	 * local translation of 4,0,0
	 */
	public void testRotateGlobal0_0_m30RotateLocal0_m40_0TranslateLocal4_0_0() {

		double[][] matrix = HoneyBee.createTransformForOrigin();

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, 0, -30);

		matrix = HoneyBee.rotateAroundLocalAxes(matrix, 0, -40, 0);

		matrix = HoneyBee.translateAlongLocalAxes(matrix, 4, 0, 0);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(matrix);

		double[] expectedTranslation = new double[] {
				4 - (8 * SIN15 * SIN15) - (8 * SIN20 * SIN20 * COS30),
				(-8 * SIN15 * COS15) + (8 * SIN20 * SIN20 * SIN30),
				8 * SIN20 * COS20 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(matrix);

		double[][] expectedRotation = new double[][] {
				new double[] { 0.663414001465, -0.383022248745, 0.642787575722 },
				new double[] { 0.5, 0.866025388241, 0.0 },
				new double[] { -0.556670367718, 0.321393787861, 0.76604449749 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

	/**
	 * Tests global rotation of 0,0,-30 then local rotation of 0,-40,0 then
	 * local translation of 4,0,0 then global translation of 2,3,0
	 */
	public void testRotateGlobal0_0_m30RotateLocal0_m40_0TranslateLocal4_0_0TranslateGlobal2_3_0() {

		double[][] matrix = HoneyBee.createTransformForOrigin();

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, 0, -30);

		matrix = HoneyBee.rotateAroundLocalAxes(matrix, 0, -40, 0);

		matrix = HoneyBee.translateAlongLocalAxes(matrix, 4, 0, 0);

		matrix = HoneyBee.translateAlongGlobalAxes(matrix, 2, 3, 0);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(matrix);

		double[] expectedTranslation = new double[] {
				2 + 4 - (8 * SIN15 * SIN15) - (8 * SIN20 * SIN20 * COS30),
				3 + (-8 * SIN15 * COS15) + (8 * SIN20 * SIN20 * SIN30),
				8 * SIN20 * COS20 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(matrix);

		double[][] expectedRotation = new double[][] {
				new double[] { 0.663414001465, -0.383022248745, 0.642787575722 },
				new double[] { 0.5, 0.866025388241, 0.0 },
				new double[] { -0.556670367718, 0.321393787861, 0.76604449749 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

	/**
	 * Tests global translation of 2,-3,0global rotation of 0,0,-30 then local
	 * rotation of 0,-40,0 then local translation of 4,0,0 then global
	 * translation of 2,3,0
	 */
	public void testTranslateGlobal2_m3_0RotateGlobal0_0_m30RotateLocal0_m40_0TranslateLocal4_0_0() {

		double[][] matrix = HoneyBee.createTransformForOrigin();

		matrix = HoneyBee.translateAlongGlobalAxes(matrix, 2, -3, 0);

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, 0, -30);

		matrix = HoneyBee.rotateAroundLocalAxes(matrix, 0, -40, 0);

		matrix = HoneyBee.translateAlongLocalAxes(matrix, 4, 0, 0);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(matrix);

		double[] expectedTranslation = new double[] {
				2 + 4 - (8 * SIN15 * SIN15) - (8 * SIN20 * SIN20 * COS30),
				-3 + (-8 * SIN15 * COS15) + (8 * SIN20 * SIN20 * SIN30),
				8 * SIN20 * COS20 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(matrix);

		double[][] expectedRotation = new double[][] {
				new double[] { 0.663414001465, -0.383022248745, 0.642787575722 },
				new double[] { 0.5, 0.866025388241, 0.0 },
				new double[] { -0.556670367718, 0.321393787861, 0.76604449749 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

	/**
	 * Tests global rotation of 0,0,-30 then local rotation of 0,-40,0 then
	 * local translation of 4,3,0
	 */
	public void testRotateGlobal0_0_m30RotateLocal0_m40_0TranslateLocal4_3_0() {

		double[][] matrix = HoneyBee.createTransformForOrigin();

		matrix = HoneyBee.rotateAroundGlobalAxes(matrix, 0, 0, -30);

		matrix = HoneyBee.rotateAroundLocalAxes(matrix, 0, -40, 0);

		matrix = HoneyBee.translateAlongLocalAxes(matrix, 4, 3, 0);

		double[] actualTranslation = HoneyBee
				.getTranslationFromTransform(matrix);

		double[] expectedTranslation = new double[] { 4.15365600586,
				1.06598711014, 2.57115054131 };

		boolean passed = HoneyBee.assertVectorsEqual(expectedTranslation,
				actualTranslation, SCALE);

		assertTrue(passed);

		double[][] actualRotation = HoneyBee.getRotationFromTransform(matrix);

		double[][] expectedRotation = new double[][] {
				new double[] { 0.663414001465, -0.383022248745, 0.642787575722 },
				new double[] { 0.5, 0.866025388241, 0.0 },
				new double[] { -0.556670367718, 0.321393787861, 0.76604449749 } };

		passed = HoneyBee.assertMatricesEqual(expectedRotation, actualRotation,
				SCALE);

		assertTrue(passed);

	}

}