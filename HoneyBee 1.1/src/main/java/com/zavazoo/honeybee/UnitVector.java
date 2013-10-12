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
 * Constitutes a unit vector for orientation of objects in three-dimensional
 * space.<br/>
 * <br/>
 * A HoneyBee unit vector is immutable and thread-safe.<br/>
 * <br/>
 * All operations assume a right-handed coordinate system.<br/>
 * <br/>
 * All rotations around an axis are performed clockwise when looking up the axis
 * from the origin to infinity.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
public class UnitVector implements Serializable {

	/** The serialization version unique identifier. */
	private static final long serialVersionUID = 1l;

	/** The primitive representation of this unit vector. */
	private final double[] vector;

	/**
	 * Creates a unit vector using the specified primitive representation that
	 * must comprise three elements which represent the x and y and z
	 * coordinates of the unit vector respectively where {@code (x ^ 2) + (y ^
	 * 2) + (z ^ 2) = 1} according to Pythagoras' theorem when the result of
	 * that equation is rounded to 3 decimal places using the half-even rounding
	 * mode.
	 * 
	 * @param vector
	 *            the primitive unit vector.
	 * @exception InvalidUnitVectorException
	 *                if the unit vector does not comprise three elements which
	 *                represent the x and y and z coordinates of a valid unit
	 *                vector that conforms with Pythagoras' theorem.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public UnitVector(double[] vector) throws InvalidUnitVectorException {

		String rational = HoneyBee.assertRationalUnitVector(vector, 3);

		if (rational != null) {

			throw new InvalidUnitVectorException(vector, rational);

		}

		// create a copy of the specified unit vector in order to ensure that
		// this unit vector is immutable
		this.vector = HoneyBee.copyVector(vector);

	}

	/**
	 * Creates a unit vector using the specified primitive representation that
	 * must comprise three elements which represent the x and y and z
	 * coordinates of the unit vector respectively where {@code (x ^ 2) + (y ^
	 * 2) + (z ^ 2) = 1} according to Pythagoras' theorem when the result of
	 * that equation is rounded to the specified number of decimal places using
	 * the half-even rounding mode.
	 * 
	 * @param vector
	 *            the primitive unit vector.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @exception InvalidUnitVectorException
	 *                if the unit vector does not comprise three elements which
	 *                represent the x and y and z coordinates of a valid unit
	 *                vector that conforms with Pythagoras' theorem.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public UnitVector(double[] vector, int decimalPlaces)
			throws InvalidUnitVectorException {

		String rational = HoneyBee.assertRationalUnitVector(vector,
				decimalPlaces);

		if (rational != null) {

			throw new InvalidUnitVectorException(vector, rational);

		}

		// create a copy of the specified unit vector in order to ensure that
		// this unit vector is immutable
		this.vector = HoneyBee.copyVector(vector);

	}

	/**
	 * Creates a unit vector using the specified primitive representation that
	 * has already been validated.
	 * 
	 * @param vector
	 *            the primitive unit vector.
	 * @param validated
	 *            indicates that the vector has already been validated.
	 */
	UnitVector(double[] vector, boolean validated) {

		this.vector = vector;

	}

	/**
	 * Yields the primitive representation of this unit vector.
	 * 
	 * @return the primitive unit vector.
	 */
	double[] getVector() {

		return vector;

	}

	/**
	 * Asserts that this unit vector is equal to the specified unit vector when
	 * each value comprised by both unit vectors is rounded to the specified
	 * number of decimal places using the half-even rounding mode.
	 * 
	 * @param vector
	 *            the unit vector.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return true if the unit vectors are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public boolean equals(UnitVector vector, int decimalPlaces) {

		return HoneyBee.assertVectorsEqual(this.vector, vector.vector,
				decimalPlaces);

	}

	/**
	 * Creates a human-readable representation of this unit vector where each
	 * value comprised by the unit vector is rounded to the specified number of
	 * decimal places using the half-up rounding mode.
	 * 
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_UP
	 */
	public String toString(int decimalPlaces) {

		return HoneyBee.representVector(vector, decimalPlaces);

	}

	/**
	 * Asserts that this unit vector is equal to the specified unit vector
	 * matrix when when each value comprised by both unit vectors is rounded to
	 * 3 decimal places using the half-even rounding mode.
	 * 
	 * @param vector
	 *            the unit vector.
	 * @return true if the unit vectors are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	@Override
	public boolean equals(Object vector) {

		return equals((UnitVector) vector, 3);

	}

	/**
	 * Creates a human-readable representation of this unit vector where each
	 * value comprised by the unit vector is rounded to 3 decimal places using
	 * the half-up rounding mode.
	 * 
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_UP
	 */
	@Override
	public String toString() {

		return toString(3);

	}

}