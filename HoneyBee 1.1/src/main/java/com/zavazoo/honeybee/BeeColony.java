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

/**
 * Constitutes a decorator that extends the functionality of HoneyBee to address
 * the concern of relationships between objects represented by transform
 * matrices and therefore prosaically represents a colony of honey bees.<br/>
 * <br/>
 * BeeColony is abstract, immutable and thread-safe.<br/>
 * <br/>
 * All operations assume a right-handed coordinate system.<br/>
 * <br/>
 * All rotations around an axis are performed clockwise when looking up the axis
 * from the origin to infinity.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
abstract public class BeeColony {

	/**
	 * Simulates the classic scenario of the camera following an object where
	 * the camera is always the specified number of units of global space behind
	 * and the specified number of units of global space above the object and
	 * where the local Z axis of the camera, that corresponds to the reverse
	 * line of sight of the camera, is always negatively pointed directly at the
	 * origin of the local axes of the object and where the local Z axis of the
	 * camera is always oriented along the plane of the global Z axis and the
	 * idealised local Y axis of the object, if the object was fully upright,
	 * because that plane corresponds to the line of sight of the object were it
	 * a simulated observer such as a protagonist in a video game.
	 * 
	 * @param object
	 *            the object transform matrix.
	 * @param behind
	 *            the number of units of global space that the camera is behind
	 *            the object.
	 * @param above
	 *            the number of units of global space that the camera is above
	 *            the object.
	 * @return the camera transform matrix.
	 */
	public static double[][] cameraFollowsObject(double[][] object,
			double behind, double above) {

		// get the unit vector that points along the local x axis of the object
		double[] objectXAxis = HoneyBee.getLocalXAxisUnitVector(object);

		// calculate the unit vector that would point along the local y axis of
		// the object if the object was fully upright
		double[] objectIdealYAxis = HoneyBee.createPerpendicularUnitVector(
				objectXAxis, HoneyBee.GLOBAL_Z_AXIS);

		// translate the object along the ideal y axis by behind units and then
		// along the global z axis by above units
		double[][] translated = HoneyBee.translateAlongArbitraryAxis(object,
				behind, objectIdealYAxis);
		translated = HoneyBee.translateAlongArbitraryAxis(translated, above,
				HoneyBee.GLOBAL_Z_AXIS);

		// calculate the unit vector from the object to the camera that is
		// equivalent to the local z axis of the camera
		double[] cameraZAxis = HoneyBee.createUnitVectorFromTransforms(object,
				translated);

		// calculate the unit vectors of the local x and y axes of the camera
		// when the local x axis of the camera is level with the global horizon
		double[] cameraXAxis = HoneyBee.createPerpendicularUnitVector(
				HoneyBee.GLOBAL_Z_AXIS, cameraZAxis);
		double[] cameraYAxis = HoneyBee.createPerpendicularUnitVector(
				cameraZAxis, cameraXAxis);

		// derive the rotation and translation matrices of the camera
		double[][] cameraRotation = new double[][] { cameraXAxis, cameraYAxis,
				cameraZAxis };
		double[] cameraTranslation = HoneyBee
				.getTranslationFromTransform(translated);

		// construct the transform matrix of the camera
		double[][] camera = HoneyBee.createTransformForRotationAndTranslation(
				cameraRotation, cameraTranslation);

		return camera;

	}

}