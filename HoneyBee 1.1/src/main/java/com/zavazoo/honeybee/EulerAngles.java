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

import java.io.Serializable;

/**
 * Constitutes a set of Euler angles for orientation of objects in
 * three-dimensional space.<br/>
 * <br/>
 * A set of HoneyBee Euler angles is immutable and thread-safe.<br/>
 * <br/>
 * All operations assume a right-handed coordinate system.<br/>
 * <br/>
 * All rotations around an axis are performed clockwise when looking up the axis
 * from the origin to infinity. Callers may invoke the negate() operation to
 * convert that standard 'positive' mode of rotation to the alternative
 * 'negative' mode of rotation.<br/>
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
public class EulerAngles implements Serializable {

	/** The serialization version unique identifier. */
	private static final long serialVersionUID = 1l;

	/** The primitive representation of the set of Euler angles. */
	private final double[] angles;

	/**
	 * Creates a set of Euler angles using the specified primitive
	 * representation that must comprise three elements which represent
	 * rotations in degrees around arbitrary X and Y and Z axes respectively.
	 * 
	 * @param angles
	 *            the primitive Euler angles.
	 */
	public EulerAngles(double[] angles) {

		// create a copy of the specified euler angles in order to ensure that
		// this set of euler angles is immutable
		this.angles = HoneyBee.copyVector(angles);

	}

	/**
	 * Creates a set of Euler angles using the specified primitive
	 * representation that has already been validated.
	 * 
	 * @param angles
	 *            the primitive Euler angles.
	 * @param validated
	 *            indicates that the set of Euler angles has already been
	 *            validated.
	 */
	private EulerAngles(double[] angles, boolean validated) {

		this.angles = angles;

	}

	/**
	 * Negates each of this set of Euler angles in order to convert the standard
	 * 'positive' mode of rotation to the alternative 'negative' mode of
	 * rotation around an arbitrary axis in a right-handed coordinate system and
	 * yields a new set of Euler angles that encapsulates the result.
	 * 
	 * @return the negated Euler angles.
	 */
	public EulerAngles negate() {

		double[] negated = new double[] { -angles[0], -angles[1], -angles[2] };

		return new EulerAngles(negated, true);

	}

	/**
	 * Yields the Euler angle that represents rotation in degrees around the X
	 * axis.
	 * 
	 * @return the Euler angle.
	 */
	public double getXAngle() {

		return angles[0];

	}

	/**
	 * Yields the Euler angle that represents rotation in degrees around the Y
	 * axis.
	 * 
	 * @return the Euler angle.
	 */
	public double getYAngle() {

		return angles[1];

	}

	/**
	 * Yields the Euler angle that represents rotation in degrees around the Z
	 * axis.
	 * 
	 * @return the Euler angle.
	 */
	public double getZAngle() {

		return angles[2];

	}

	/**
	 * Yields the primitive representation of this set of Euler angles.
	 * 
	 * @return the primitive Euler angles.
	 */
	double[] getAngles() {

		return angles;

	}

	/**
	 * Asserts that this set of Euler angles is equal to the specified set of
	 * Euler angles when each value comprised by both sets of Euler angles is
	 * rounded to the specified number of decimal places using the half-even
	 * rounding mode.
	 * 
	 * @param angles
	 *            the Euler angles.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return true if the sets of Euler angles are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public boolean equals(EulerAngles angles, int decimalPlaces) {

		return HoneyBee.assertVectorsEqual(this.angles, angles.angles,
				decimalPlaces);

	}

	/**
	 * Creates a human-readable representation of this set of Euler angles where
	 * each value comprised by the set of Euler angles is rounded to the
	 * specified number of decimal places using the half-up rounding mode.
	 * 
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_UP
	 */
	public String toString(int decimalPlaces) {

		return HoneyBee.representVector(angles, decimalPlaces);

	}

	/**
	 * Asserts that this set of Euler angles is equal to the specified set of
	 * Euler angles matrix when when each value comprised by both sets of Euler
	 * angles is rounded to 3 decimal places using the half-even rounding mode.
	 * 
	 * @param angles
	 *            the Euler angles.
	 * @return true if the sets of Euler angles are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	@Override
	public boolean equals(Object angles) {

		return equals((EulerAngles) angles, 3);

	}

	/**
	 * Creates a human-readable representation of this set of Euler angles where
	 * each value comprised by the set of Euler angles is rounded to 3 decimal
	 * places using the half-up rounding mode.
	 * 
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_UP
	 */
	@Override
	public String toString() {

		return toString(3);

	}

}