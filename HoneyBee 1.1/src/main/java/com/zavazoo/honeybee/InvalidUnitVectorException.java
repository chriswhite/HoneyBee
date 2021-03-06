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
 * Exception used to indicate that a rotation matrix does not comprise three
 * elements which represent the x and y and z coordinates of the unit vector
 * respectively where {@code (x ^ 2) + (y ^ 2) + (z ^ 2) = 1} according to
 * Pythagoras' theorem.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 * @see com.zavazoo.honeybee.UnitVector
 */
public class InvalidUnitVectorException extends RuntimeException {

	/** The serialization version unique identifier. */
	private static final long serialVersionUID = 1l;

	/** The sequence of characters that inserts a line break. */
	private static String NEWLINE = System.getProperty("line.separator");

	/** The unit vector. */
	private double[] vector;

	/**
	 * Creates an invalid unit vector exception using the specified unit vector
	 * and supplementary error message.
	 * 
	 * @param vector
	 *            the vector.
	 * @param message
	 *            the error message.
	 */
	InvalidUnitVectorException(double[] vector, String message) {

		super(
				"Unit vector does not comprise three elements which represent the x and y and z coordinates of a valid unit vector:"
						+ NEWLINE
						+ HoneyBee.representVector(vector, 10)
						+ NEWLINE + message);

		this.vector = vector;

	}

	/**
	 * Gets the erroneous unit vector.
	 * 
	 * @return the vector.
	 */
	public double[] getVector() {

		return vector;

	}

}