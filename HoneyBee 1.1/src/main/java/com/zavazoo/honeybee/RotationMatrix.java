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
 * Constitutes a rotation matrix for orientation of objects in three-dimensional
 * space.<br/>
 * <br/>
 * A HoneyBee rotation matrix is immutable and thread-safe.<br/>
 * <br/>
 * All operations assume a right-handed coordinate system.<br/>
 * <br/>
 * All rotations around an axis are performed clockwise when looking up the axis
 * from the origin to infinity.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
public class RotationMatrix implements Serializable {

	/** The serialization version unique identifier. */
	private static final long serialVersionUID = 1l;

	/** The primitive representation of this rotation matrix. */
	private final double[][] matrix;

	/**
	 * Creates a rotation matrix that expresses perfect alignment with the
	 * global axes.
	 */
	public RotationMatrix() {

		double[][] transform = HoneyBee.createTransformForOrigin();

		double[][] rotation = HoneyBee.getRotationFromTransform(transform);

		this.matrix = rotation;

	}

	/**
	 * Creates a rotation matrix using the specified primitive representation
	 * that must comprise three rows, each of which represent a valid unit
	 * vector in the direction of the global X and Y and Z axes respectively,
	 * where each such row comprises three columns each of which represent the x
	 * and y and z coordinates of the respective unit vector. Similarly each
	 * column, vertically spanning three rows top to bottom, represents a valid
	 * unit vector in the direction of the local X and Y and Z axes.<br/>
	 * A valid unit vector must comprise three elements which represent the x
	 * and y and z coordinates of the unit vector respectively where {@code (x ^
	 * 2) + (y ^ 2) + (z ^ 2) = 1} according to Pythagoras' theorem when the
	 * result of that equation is rounded to 3 decimal places using the
	 * half-even rounding mode.
	 * 
	 * @param matrix
	 *            the primitive rotation matrix.
	 * @exception InvalidRotationMatrixException
	 *                if the rotation matrix does not comprise three rows each
	 *                with three columns where each row and column comprises a
	 *                valid unit vector.
	 * @see com.zavazoo.honeybee.UnitVector
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public RotationMatrix(double[][] matrix)
			throws InvalidRotationMatrixException {

		String rational = HoneyBee.assertRationalRotationMatrix(matrix, 3);

		if (rational != null) {

			throw new InvalidRotationMatrixException(matrix, rational);

		}

		// create a copy of the specified matrix in order to ensure that this
		// rotation matrix is immutable
		this.matrix = HoneyBee.copyMatrix(matrix);

	}

	/**
	 * Creates a rotation matrix using the specified primitive representation
	 * that must comprise three rows, each of which represent a valid unit
	 * vector in the direction of the global X and Y and Z axes respectively,
	 * where each such row comprises three columns each of which represent the x
	 * and y and z coordinates of the respective unit vector. Similarly each
	 * column, vertically spanning three rows top to bottom, represents a valid
	 * unit vector in the direction of the local X and Y and Z axes.<br/>
	 * A valid unit vector must comprise three elements which represent the x
	 * and y and z coordinates of the unit vector respectively where {@code (x ^
	 * 2) + (y ^ 2) + (z ^ 2) = 1} according to Pythagoras' theorem when the
	 * result of that equation is rounded to the specified number of decimal
	 * places using the half-even rounding mode.
	 * 
	 * @param matrix
	 *            the primitive rotation matrix.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @exception InvalidRotationMatrixException
	 *                if the rotation matrix does not comprise three rows each
	 *                with three columns where each row and column comprises a
	 *                valid unit vector.
	 * @exception InvalidUnitVectorException
	 *                if a given unit vector is not valid.
	 * @see com.zavazoo.honeybee.UnitVector
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public RotationMatrix(double[][] matrix, int decimalPlaces)
			throws InvalidRotationMatrixException, InvalidUnitVectorException {

		String rational = HoneyBee.assertRationalRotationMatrix(matrix,
				decimalPlaces);

		if (rational != null) {

			throw new InvalidRotationMatrixException(matrix, rational);

		}

		// create a copy of the specified matrix in order to ensure that this
		// rotation matrix is immutable
		this.matrix = HoneyBee.copyMatrix(matrix);

	}

	/**
	 * Creates a rotation matrix using the specified primitive representation
	 * that has already been validated.
	 * 
	 * @param matrix
	 *            the primitive rotation matrix.
	 * @param validated
	 *            indicates that the rotation matrix has already been validated.
	 */
	private RotationMatrix(double[][] matrix, boolean validated) {

		this.matrix = matrix;

	}

	/**
	 * Rotates this rotation matrix through the specified Euler angles, which
	 * represent rotations measured in degrees around the global axes, and
	 * yields a new rotation matrix that encapsulates the result.
	 * 
	 * @param angles
	 *            the Euler angles.
	 * @return the rotated rotation matrix.
	 */
	public RotationMatrix rotateAroundGlobalAxes(EulerAngles angles) {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[][] rotated = HoneyBee.rotateAroundGlobalAxes(transform,
				angles.getXAngle(), angles.getYAngle(), angles.getZAngle());

		double[][] rotation = HoneyBee.getRotationFromTransform(rotated);

		return new RotationMatrix(rotation, true);

	}

	/**
	 * Rotates this rotation matrix through the specified Euler angles, which
	 * represent rotations measured in degrees around the local axes comprised
	 * by this rotation matrix, and yields a new rotation matrix that
	 * encapsulates the result.
	 * 
	 * @param angles
	 *            the Euler angles.
	 * @return the rotated rotation matrix.
	 */
	public RotationMatrix rotateAroundLocalAxes(EulerAngles angles) {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[][] rotated = HoneyBee.rotateAroundLocalAxes(transform,
				angles.getXAngle(), angles.getYAngle(), angles.getZAngle());

		double[][] rotation = HoneyBee.getRotationFromTransform(rotated);

		return new RotationMatrix(rotation, true);

	}

	/**
	 * Rotates this rotation matrix through the specified Euler angles, which
	 * represent rotations measured in degrees around the local axes comprised
	 * by the specified (foreign) rotation matrix, and yields a new rotation
	 * matrix that encapsulates the result.
	 * 
	 * @param angles
	 *            the Euler angles.
	 * @param matrix
	 *            the rotation matrix.
	 * @return the rotated rotation matrix.
	 */
	public RotationMatrix rotateAroundForeignAxes(EulerAngles angles,
			RotationMatrix matrix) {

		double x = angles.getXAngle();
		double y = angles.getYAngle();
		double z = angles.getZAngle();

		RotationMatrix rotated = null;

		if (x != 0d) {

			UnitVector xvector = matrix.getXAxis();

			rotated = this.rotateAroundAxis(x, xvector);

		}

		if (y != 0d) {

			UnitVector yvector = matrix.getYAxis();

			if (rotated == null) {

				rotated = this.rotateAroundAxis(y, yvector);

			} else {

				rotated = rotated.rotateAroundAxis(y, yvector);

			}

		}

		if (z != 0d) {

			UnitVector zvector = matrix.getZAxis();

			if (rotated == null) {

				rotated = this.rotateAroundAxis(z, zvector);

			} else {

				rotated = rotated.rotateAroundAxis(z, zvector);

			}

		}

		if (rotated == null) {

			return this;

		}

		return rotated;

	}

	/**
	 * Rotates this rotation matrix through the specified Euler angle, which
	 * represents a rotation measured in degrees around an arbitrary axis that
	 * points in the direction of the specified unit vector, and yields a new
	 * rotation matrix that encapsulates the result.
	 * 
	 * @param angle
	 *            the Euler angle.
	 * @param vector
	 *            the unit vector.
	 * @return the rotated rotation matrix.
	 */
	public RotationMatrix rotateAroundAxis(double angle, UnitVector vector) {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[][] rotated = HoneyBee.rotateAroundArbitraryAxis(transform,
				angle, vector.getVector());

		double[][] rotation = HoneyBee.getRotationFromTransform(rotated);

		return new RotationMatrix(rotation, true);

	}

	/**
	 * Yields the unit vector that points in the direction of the local X axis
	 * comprised by this rotation matrix.
	 * 
	 * @return the unit vector.
	 */
	public UnitVector getXAxis() {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[] vector = HoneyBee.getLocalXAxisUnitVector(transform);

		return new UnitVector(vector, true);

	}

	/**
	 * Yields the unit vector that points in the direction of the local Y axis
	 * comprised by this rotation matrix.
	 * 
	 * @return the unit vector.
	 */
	public UnitVector getYAxis() {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[] vector = HoneyBee.getLocalYAxisUnitVector(transform);

		return new UnitVector(vector, true);

	}

	/**
	 * Yields the unit vector that points in the direction of the local Z axis
	 * comprised by this rotation matrix.
	 * 
	 * @return the unit vector.
	 */
	public UnitVector getZAxis() {

		double[][] transform = HoneyBee.createTransformForRotation(this.matrix);

		double[] vector = HoneyBee.getLocalZAxisUnitVector(transform);

		return new UnitVector(vector, true);

	}

	/**
	 * Multiplies this rotation matrix by the specified rotation matrix and
	 * yields a new rotation matrix that encapsulates the result.
	 * 
	 * @param matrix
	 *            the rotation matrix.
	 * @return the multiplied rotation matrix.
	 */
	public RotationMatrix multiply(RotationMatrix matrix) {

		double[][] product = HoneyBee.multiplyRotations(this.matrix,
				matrix.matrix);

		return new RotationMatrix(product, true);

	}

	/**
	 * Asserts that this rotation matrix is equal to the specified rotation
	 * matrix when each value comprised by both matrices is rounded to the
	 * specified number of decimal places using the half-even rounding mode.
	 * 
	 * @param matrix
	 *            the rotation matrix.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return true if the matrices are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public boolean equals(RotationMatrix matrix, int decimalPlaces) {

		return HoneyBee.assertMatricesEqual(this.matrix, matrix.matrix,
				decimalPlaces);

	}

	/**
	 * Creates a human-readable representation of this rotation matrix where
	 * each value comprised by the matrix is rounded to the specified number of
	 * decimal places using the half-up rounding mode.
	 * 
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_UP
	 */
	public String toString(int decimalPlaces) {

		return HoneyBee.representMatrix(this.matrix, decimalPlaces);

	}

	/**
	 * Asserts that this rotation matrix is equal to the specified rotation
	 * matrix when when each value comprised by both matrices is rounded to 3
	 * decimal places using the half-even rounding mode.
	 * 
	 * @param matrix
	 *            the rotation matrix.
	 * @return true if the matrices are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	@Override
	public boolean equals(Object matrix) {

		return equals((RotationMatrix) matrix, 3);

	}

	/**
	 * Creates a human-readable representation of this rotation matrix where
	 * each value comprised by the matrix is rounded to 3 decimal places using
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