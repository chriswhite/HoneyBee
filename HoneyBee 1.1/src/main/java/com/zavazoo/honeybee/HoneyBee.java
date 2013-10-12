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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

/**
 * Operates on transform matrices for orientation and translation of objects in
 * three-dimensional space.<br/>
 * <br/>
 * HoneyBee is abstract, immutable and thread-safe.<br/>
 * <br/>
 * All operations assume a right-handed coordinate system.<br/>
 * <br/>
 * All rotations around an axis are performed clockwise when looking up the axis
 * from the origin to infinity.
 * 
 * @author Chris White <chriswhitelondon@gmail.com>
 * @since JDK6
 */
abstract public class HoneyBee {

	/** The sequence of characters that represents a line break. */
	private static String NEWLINE = System.getProperty("line.separator");

	/** The 4x4 identity matrix. */
	private static final double[][] TRANSFORM_IDENTITY = new double[][] {
			new double[] { 1, 0, 0, 0 }, new double[] { 0, 1, 0, 0 },
			new double[] { 0, 0, 1, 0 }, new double[] { 0, 0, 0, 1 } };

	/** The 3x3 identity matrix. */
	private static final double[][] ROTATION_IDENTITY = new double[][] {
			new double[] { 1, 0, 0 }, new double[] { 0, 1, 0 },
			new double[] { 0, 0, 1 } };

	/** The origin of the global axes. */
	public static final double[] GLOBAL_ORIGIN = new double[] { 0, 0, 0 };

	/** The unit vector that points along the global X axis. */
	public static final double[] GLOBAL_X_AXIS = new double[] { 1, 0, 0 };

	/** The unit vector that points along the global Y axis. */
	public static final double[] GLOBAL_Y_AXIS = new double[] { 0, 1, 0 };

	/** The unit vector that points along the global Z axis. */
	public static final double[] GLOBAL_Z_AXIS = new double[] { 0, 0, 1 };

	/**
	 * Creates a 4x4 transform matrix for an object with a position equivalent
	 * to the origin of the global axes and with an orientation equivalent to
	 * the orientation of the global axes.
	 * 
	 * @return the 4x4 transform matrix.
	 */
	public static double[][] createTransformForOrigin() {

		return TRANSFORM_IDENTITY;

	}

	/**
	 * Creates a transform matrix for an object with a position equivalent to
	 * the origin of the global axes and with an orientation equivalent to the
	 * specified 3x3 rotation matrix.
	 * 
	 * @param rotation
	 *            the 3x3 rotation matrix.
	 * @return the 4x4 transform matrix.
	 */
	public static double[][] createTransformForRotation(double[][] rotation) {

		return createTransformForRotationAndTranslation(rotation, GLOBAL_ORIGIN);

	}

	/**
	 * Creates a 4x4 transform matrix for an object with a position equivalent
	 * to the specified 3x1 translation vector and with an orientation
	 * equivalent to the orientation of the global axes.
	 * 
	 * @param translation
	 *            the 3x1 translation vector.
	 * @return the 4x4 transform matrix.
	 */
	public static double[][] createTransformForTranslation(double[] translation) {

		return createTransformForRotationAndTranslation(ROTATION_IDENTITY,
				translation);

	}

	/**
	 * Creates a 4x4 transform matrix for an object with a position equivalent
	 * to the specified 3x1 translation vector and with an orientation
	 * equivalent to the specified 3x3 rotation matrix.
	 * 
	 * @param rotation
	 *            the 3x3 rotation matrix.
	 * @param translation
	 *            the 3x1 translation vector.
	 * @return the 4x4 transform matrix.
	 */
	public static double[][] createTransformForRotationAndTranslation(
			double[][] rotation, double[] translation) {

		double xx = rotation[0][0];
		double xy = rotation[0][1];
		double xz = rotation[0][2];
		double xt = translation[0];

		double yx = rotation[1][0];
		double yy = rotation[1][1];
		double yz = rotation[1][2];
		double yt = translation[1];

		double zx = rotation[2][0];
		double zy = rotation[2][1];
		double zz = rotation[2][2];
		double zt = translation[2];

		double[][] transform = new double[][] {
				new double[] { xx, xy, xz, xt },
				new double[] { yx, yy, yz, yt },
				new double[] { zx, zy, zz, zt }, new double[] { 0, 0, 0, 1 } };

		return transform;

	}

	/**
	 * Creates a 3x1 unit vector that expresses the orientation of a line drawn
	 * from the position equivalent to the translation vector comprised by the
	 * first specified 4x4 transform matrix to the position equivalent to the
	 * translation vector comprised by the second specified 4x4 transform
	 * matrix.
	 * 
	 * @param first
	 *            the first 4x4 transform matrix.
	 * @param second
	 *            the second 4x4 transform matrix.
	 * @return the 3x1 unit vector.
	 */
	public static double[] createUnitVectorFromTransforms(double[][] first,
			double[][] second) {

		double[] firstTranslation = getTranslationFromTransform(first);
		double[] secondTranslation = getTranslationFromTransform(second);

		return createUnitVectorFromTranslations(firstTranslation,
				secondTranslation);

	}

	/**
	 * Creates a 3x1 unit vector that expresses the orientation of a line drawn
	 * from the position equivalent to the first specified 3x1 translation
	 * vector to the position equivalent to the second specified 3x1 translation
	 * vector.
	 * 
	 * @param first
	 *            the first 3x1 translation matrix.
	 * @param second
	 *            the second 3x1 translation matrix.
	 * @return the 3x1 unit vector.
	 */
	public static double[] createUnitVectorFromTranslations(double[] first,
			double[] second) {

		double xfirst = first[0];
		double yfirst = first[1];
		double zfirst = first[2];

		double xsecond = second[0];
		double ysecond = second[1];
		double zsecond = second[2];

		double xextended = xsecond - xfirst;
		double yextended = ysecond - yfirst;
		double zextended = zsecond - zfirst;

		double[] extended = new double[] { xextended, yextended, zextended };

		double[] normalised = normaliseVector(extended);

		return normalised;

	}

	/**
	 * Calculates the cross product of the specified 3x1 unit vectors in order
	 * to yield the 3x1 unit vector that is perpendicular to both unit vectors.<br/>
	 * The direction of the resultant perpendicular unit vector may be
	 * determined using the 'right-hand rule' where the forefinger points in the
	 * direction of the first specified unit vector, the middle finger points in
	 * the direction of the second specified unit vector and the thumb points in
	 * the direction of the resultant perpendicular unit vector.
	 * 
	 * @param first
	 *            the first 3x1 unit vector.
	 * @param second
	 *            the second 3x1 unit vector.
	 * @return the perpendicular 3x1 unit vector.
	 */
	public static double[] createPerpendicularUnitVector(double[] first,
			double[] second) {

		double x = (first[1] * second[2]) - (first[2] * second[1]);
		double y = (first[2] * second[0]) - (first[0] * second[2]);
		double z = (first[0] * second[1]) - (first[1] * second[0]);

		double[] perpendicular = new double[] { x, y, z };

		double[] normalised = normaliseVector(perpendicular);

		return normalised;

	}

	/**
	 * Rotates an object with an orientation equivalent to the rotation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * Euler angles around the global axes and yields a 4x4 transform matrix
	 * that comprises a rotation matrix equivalent to the resultant orientation
	 * and that also comprises the original translation matrix comprised by the
	 * specified transform matrix therefore the object will have the same
	 * position following the rotation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xangle
	 *            the Euler angle in degrees around the global X axis.
	 * @param yangle
	 *            the Euler angle in degrees around the global Y axis.
	 * @param zangle
	 *            the Euler angle in degrees around the global Z axis.
	 * @return the rotated 4x4 transform matrix.
	 */
	public static double[][] rotateAroundGlobalAxes(double[][] matrix,
			double xangle, double yangle, double zangle) {

		return rotateAroundForeignAxes(matrix, xangle, yangle, zangle,
				TRANSFORM_IDENTITY);

	}

	/**
	 * Rotates an object with an orientation equivalent to the rotation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * Euler angles around the local axes of the object and yields a 4x4
	 * transform matrix that comprises a rotation matrix equivalent to the
	 * resultant orientation and that also comprises the original translation
	 * matrix comprised by the specified transform matrix therefore the object
	 * will have the same position following the rotation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xangle
	 *            the Euler angle in degrees around the local X axis.
	 * @param yangle
	 *            the Euler angle in degrees around the local Y axis.
	 * @param zangle
	 *            the Euler angle in degrees around the local Z axis.
	 * @return the rotated 4x4 transform matrix.
	 */
	public static double[][] rotateAroundLocalAxes(double[][] matrix,
			double xangle, double yangle, double zangle) {

		return rotateAroundForeignAxes(matrix, xangle, yangle, zangle, matrix);

	}

	/**
	 * Rotates an object with an orientation equivalent to the rotation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * Euler angles around the local axes of a different object with an
	 * orientation equivalent to the rotation matrix comprised by the specified
	 * foreign 4x4 transform matrix and yields a 4x4 transform matrix that
	 * comprises a rotation matrix equivalent to the resultant orientation and
	 * that also comprises the original translation matrix comprised by the
	 * specified transform matrix therefore the object will have the same
	 * position following the rotation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xangle
	 *            the Euler angle in degrees around the foreign X axis.
	 * @param yangle
	 *            the Euler angle in degrees around the foreign Y axis.
	 * @param zangle
	 *            the Euler angle in degrees around the foreign Z axis.
	 * @param foreign
	 *            the foreign 4x4 transform matrix.
	 * @return the rotated 4x4 transform matrix.
	 */
	public static double[][] rotateAroundForeignAxes(double[][] matrix,
			double xangle, double yangle, double zangle, double[][] foreign) {

		double[][] rotation = getRotationFromTransform(foreign);

		double[][] rotated = matrix;
		boolean copy = true;

		if (xangle != 0d) {

			rotated = rotateAroundArbitraryAxis(rotated, xangle, rotation[0]);
			copy = false;

		}

		if (yangle != 0d) {

			rotated = rotateAroundArbitraryAxis(rotated, yangle, rotation[1]);
			copy = false;

		}

		if (zangle != 0d) {

			rotated = rotateAroundArbitraryAxis(rotated, zangle, rotation[2]);
			copy = false;

		}

		if (copy) {

			// copy the specified matrix because the caller may otherwise
			// inadvertently modify the specified matrix were it returned by
			// default
			rotated = copyMatrix(rotated);

		}

		return rotated;

	}

	/**
	 * Rotates an object with an orientation equivalent to the rotation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * Euler angle around an arbitrary axis that points in the direction of the
	 * specified unit vector and yields a 4x4 transform matrix that comprises a
	 * rotation matrix equivalent to the resultant orientation and that also
	 * comprises the original translation matrix comprised by the specified
	 * transform matrix therefore the object will have the same position
	 * following the rotation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param angle
	 *            the Euler angle in degrees around the axis.
	 * @param vector
	 *            the 3x1 unit vector.
	 * @return the rotated 4x4 transform matrix.
	 */
	public static double[][] rotateAroundArbitraryAxis(double[][] matrix,
			double angle, double[] vector) {

		double x = vector[0];
		double y = vector[1];
		double z = vector[2];

		double s = Math.sin(Math.toRadians(angle));
		double c = Math.cos(Math.toRadians(angle));
		double m = 1 - c;

		double sx = s * x;
		double sy = s * y;
		double sz = s * z;
		double mxx = m * x * x;
		double myy = m * y * y;
		double mzz = m * z * z;
		double mxy = m * x * y;
		double mxz = m * x * z;
		double myz = m * y * z;

		double xx = mxx + c;
		double xy = mxy + sz;
		double xz = mxz - sy;

		double yx = mxy - sz;
		double yy = myy + c;
		double yz = myz + sx;

		double zx = mxz + sy;
		double zy = myz - sx;
		double zz = mzz + c;

		double[][] second = new double[][] { new double[] { xx, xy, xz },
				new double[] { yx, yy, yz }, new double[] { zx, zy, zz } };

		double[][] first = getRotationFromTransform(matrix);

		double[][] rotation = multiplyRotations(first, second);

		double[] translation = getTranslationFromTransform(matrix);

		double[][] transform = createTransformForRotationAndTranslation(
				rotation, translation);

		return transform;

	}

	/**
	 * Moves an object with a position equivalent to the translation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * units of global space along the global axes and yields a 4x4 transform
	 * matrix that comprises a translation matrix equivalent to the resultant
	 * position and that also comprises the original rotation matrix comprised
	 * by the specified transform matrix therefore the object will have the same
	 * orientation following the translation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xunits
	 *            the units of global space moved along the global X axis.
	 * @param yunits
	 *            the units of global space moved along the global Y axis.
	 * @param zunits
	 *            the units of global space moved along the global Z axis.
	 * @return the translated 4x4 transform matrix.
	 */
	public static double[][] translateAlongGlobalAxes(double[][] matrix,
			double xunits, double yunits, double zunits) {

		return translateAlongForeignAxes(matrix, xunits, yunits, zunits,
				TRANSFORM_IDENTITY);

	}

	/**
	 * Moves an object with a position equivalent to the translation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * units of global space along the local axes of the object and yields a 4x4
	 * transform matrix that comprises a translation matrix equivalent to the
	 * resultant position and that also comprises the original rotation matrix
	 * comprised by the specified transform matrix therefore the object will
	 * have the same orientation following the translation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xunits
	 *            the units of global space moved along the local X axis.
	 * @param yunits
	 *            the units of global space moved along the local Y axis.
	 * @param zunits
	 *            the units of global space moved along the local Z axis.
	 * @return the translated 4x4 transform matrix.
	 */
	public static double[][] translateAlongLocalAxes(double[][] matrix,
			double xunits, double yunits, double zunits) {

		return translateAlongForeignAxes(matrix, xunits, yunits, zunits, matrix);

	}

	/**
	 * Moves an object with a position equivalent to the translation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * units of global space along the local axes of a different object with a
	 * position equivalent to the translation matrix comprised by the specified
	 * foreign 4x4 transform matrix and yields a 4x4 transform matrix that
	 * comprises a translation matrix equivalent to the resultant position and
	 * that also comprises the original rotation matrix comprised by the
	 * specified transform matrix therefore the object will have the same
	 * orientation following the translation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param xunits
	 *            the units of global space moved along the foreign X axis.
	 * @param yunits
	 *            the units of global space moved along the foreign Y axis.
	 * @param zunits
	 *            the units of global space moved along the foreign Z axis.
	 * @param foreign
	 *            the foreign 4x4 transform matrix.
	 * @return the translated 4x4 transform matrix.
	 */
	public static double[][] translateAlongForeignAxes(double[][] matrix,
			double xunits, double yunits, double zunits, double[][] foreign) {

		double[][] rotation = getRotationFromTransform(foreign);

		double[][] translated = matrix;
		boolean copy = true;

		if (xunits != 0d) {

			translated = translateAlongArbitraryAxis(translated, xunits,
					rotation[0]);
			copy = false;

		}

		if (yunits != 0d) {

			translated = translateAlongArbitraryAxis(translated, yunits,
					rotation[1]);
			copy = false;

		}

		if (zunits != 0d) {

			translated = translateAlongArbitraryAxis(translated, zunits,
					rotation[2]);
			copy = false;

		}

		if (copy) {

			// copy the specified matrix because the caller may otherwise
			// inadvertently modify the specified matrix were it returned by
			// default
			translated = copyMatrix(translated);

		}

		return translated;

	}

	/**
	 * Moves an object with a position equivalent to the translation matrix
	 * comprised by the specified 4x4 transform matrix through the specified
	 * units of global space along an arbitrary axis that points in the
	 * direction of the specified unit vector and yields a 4x4 transform matrix
	 * that comprises a translation matrix equivalent to the resultant position
	 * and that also comprises the original rotation matrix comprised by the
	 * specified transform matrix therefore the object will have the same
	 * orientation following the translation.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @param units
	 *            the units of global space moved along the axis.
	 * @param vector
	 *            the 3x1 unit vector.
	 * @return the translated 4x4 transform matrix.
	 */
	public static double[][] translateAlongArbitraryAxis(double[][] matrix,
			double units, double[] vector) {

		double[] translation = getTranslationFromTransform(matrix);

		double xtranslation = translation[0];
		double ytranslation = translation[1];
		double ztranslation = translation[2];

		double xvector = vector[0];
		double yvector = vector[1];
		double zvector = vector[2];

		double xtranslated = xtranslation + (units * xvector);
		double ytranslated = ytranslation + (units * yvector);
		double ztranslated = ztranslation + (units * zvector);

		double[] translated = new double[] { xtranslated, ytranslated,
				ztranslated };

		double[][] rotation = getRotationFromTransform(matrix);

		double[][] transform = createTransformForRotationAndTranslation(
				rotation, translated);

		return transform;

	}

	/**
	 * Measures the distance in units of global space between the positions of
	 * two objects defined by the specified 4x4 transform matrices.
	 * 
	 * @param first
	 *            the first 4x4 transform matrix.
	 * @param second
	 *            the second 4x4 transform matrix.
	 * @return the distance in units of global space.
	 */
	public static double measureGlobalDistance(double[][] first,
			double[][] second) {

		double[] firstTranslation = getTranslationFromTransform(first);
		double[] secondTranslation = getTranslationFromTransform(second);

		return measureGlobalDistance(firstTranslation, secondTranslation);

	}

	/**
	 * Measures the distance in units of global space between the positions of
	 * two objects defined by the specified 3x1 translation matrices.
	 * 
	 * @param first
	 *            the first 3x1 translation matrix.
	 * @param second
	 *            the second 3x1 translation matrix.
	 * @return the distance in units of global space.
	 */
	public static double measureGlobalDistance(double[] first, double[] second) {

		double x = first[0] - second[0];
		double y = first[1] - second[1];
		double z = first[2] - second[2];

		double xx = x * x;
		double yy = y * y;
		double zz = z * z;

		double sum = xx + yy + zz;

		double distance = Math.sqrt(sum);

		return distance;

	}

	/**
	 * Yields the 3x3 rotation matrix comprised by the specified 4x4 transform
	 * matrix.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @return the 3x3 rotation matrix.
	 */
	public static double[][] getRotationFromTransform(double[][] matrix) {

		double xx = matrix[0][0];
		double xy = matrix[0][1];
		double xz = matrix[0][2];

		double yx = matrix[1][0];
		double yy = matrix[1][1];
		double yz = matrix[1][2];

		double zx = matrix[2][0];
		double zy = matrix[2][1];
		double zz = matrix[2][2];

		double[][] rotation = new double[][] { new double[] { xx, xy, xz },
				new double[] { yx, yy, yz }, new double[] { zx, zy, zz } };

		return rotation;

	}

	/**
	 * Yields the 3x1 translation matrix comprised by the specified 4x4
	 * transform matrix.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @return the 3x1 translation matrix.
	 */
	public static double[] getTranslationFromTransform(double[][] matrix) {

		double x = matrix[0][3];
		double y = matrix[1][3];
		double z = matrix[2][3];

		double[] translation = new double[] { x, y, z };

		return translation;

	}

	/**
	 * Yields the 3x1 unit vector comprised by the specified 4x4 transform
	 * matrix that points in the direction of the local X axis of an object with
	 * an orientation equivalent to the rotation matrix comprised by the
	 * transform matrix.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @return the 3x1 unit vector.
	 */
	public static double[] getLocalXAxisUnitVector(double[][] matrix) {

		double[][] rotation = getRotationFromTransform(matrix);

		double[] vector = rotation[0];

		return vector;

	}

	/**
	 * Yields the 3x1 unit vector comprised by the specified 4x4 transform
	 * matrix that points in the direction of the local Y axis of an object with
	 * an orientation equivalent to the rotation matrix comprised by the
	 * transform matrix.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @return the 3x1 unit vector.
	 */
	public static double[] getLocalYAxisUnitVector(double[][] matrix) {

		double[][] rotation = getRotationFromTransform(matrix);

		double[] vector = rotation[1];

		return vector;

	}

	/**
	 * Yields the 3x1 unit vector comprised by the specified 4x4 transform
	 * matrix that points in the direction of the local Z axis of an object with
	 * an orientation equivalent to the rotation matrix comprised by the
	 * transform matrix.
	 * 
	 * @param matrix
	 *            the 4x4 transform matrix.
	 * @return the 3x1 unit vector.
	 */
	public static double[] getLocalZAxisUnitVector(double[][] matrix) {

		double[][] rotation = getRotationFromTransform(matrix);

		double[] vector = rotation[2];

		return vector;

	}

	/**
	 * Asserts that the specified NxN matrices are equal when each value
	 * comprised by both matrices is rounded to the specified number of decimal
	 * places using the half-even rounding mode.
	 * 
	 * @param first
	 *            the first NxN matrix.
	 * @param second
	 *            the second NxN matrix.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return true if the matrices are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static boolean assertMatricesEqual(double[][] first,
			double[][] second, int decimalPlaces) {

		int firstRows = first.length;
		int secondRows = second.length;

		if (firstRows != secondRows) {

			return false;

		}

		for (int row = 0; row < firstRows; row++) {

			double[] firstRow = first[row];
			double[] secondRow = second[row];

			if (!assertVectorsEqual(firstRow, secondRow, decimalPlaces)) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Asserts that the specified Nx1 vectors are equal when each value
	 * comprised by both vectors is rounded to the specified number of decimal
	 * places using the half-even rounding mode.
	 * 
	 * @param first
	 *            the first Nx1 vector.
	 * @param second
	 *            the second Nx1 vector.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return true if the vectors are equal, false otherwise.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static boolean assertVectorsEqual(double[] first, double[] second,
			int decimalPlaces) {

		int firstColumns = first.length;
		int secondColumns = second.length;

		if (firstColumns != secondColumns) {

			return false;

		}

		for (int column = 0; column < firstColumns; column++) {

			double firstColumn = first[column];
			double secondColumn = second[column];

			BigDecimal firstValue = new BigDecimal(firstColumn).divide(
					BigDecimal.ONE, decimalPlaces, RoundingMode.HALF_EVEN);

			BigDecimal secondValue = new BigDecimal(secondColumn).divide(
					BigDecimal.ONE, decimalPlaces, RoundingMode.HALF_EVEN);

			if (firstValue.compareTo(secondValue) != 0) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Asserts that the specified NxN matrix is square with N rows and that
	 * every row has N columns where N > 1.
	 * 
	 * @param matrix
	 *            the NxN matrix.
	 * @return null if the matrix is square with N rows and that every row has N
	 *         columns where N > 1, otherwise an error message.
	 */
	public static String assertRationalMatrix(double[][] matrix) {

		if (matrix == null) {

			return "The matrix is null";

		}

		int rows = matrix.length;

		if (rows <= 1) {

			return "The matrix does not have more than one row:" + NEWLINE
					+ representMatrix(matrix, 3);

		}

		double[] firstRow = matrix[0];

		if (firstRow == null) {

			return "The first row of the matrix is null:" + NEWLINE
					+ representMatrix(matrix, 3);

		}

		int columns = firstRow.length;

		if (columns <= 1) {

			return "The first row of the matrix does not have more than one column:"
					+ NEWLINE + representMatrix(matrix, 3);

		}

		if (rows != columns) {

			return "The matrix does not have an equal number of rows and columns:"
					+ NEWLINE + representMatrix(matrix, 3);

		}

		for (int row = 1; row < rows; row++) {

			double[] currentRow = matrix[row];

			if (currentRow == null) {

				return "Row " + (row + 1) + " of the matrix is null:" + NEWLINE
						+ representMatrix(matrix, 3);

			}

			int rowColumns = currentRow.length;

			if (rowColumns != columns) {

				return "Row "
						+ (row + 1)
						+ " of the matrix does not have the same number of columns as the first row:"
						+ NEWLINE + representMatrix(matrix, 3);

			}

		}

		return null;

	}

	/**
	 * Asserts that both of the specified NxN matrices are square with N rows
	 * and that every row of both matrices has N columns where N > 1.
	 * 
	 * @param first
	 *            the first NxN matrix.
	 * @param second
	 *            the second NxN matrix.
	 * @return null if both matrices are square with N rows and that every row
	 *         of both matrices has N columns where N > 1, otherwise an error
	 *         message.
	 */
	public static String assertRationalMatrices(double[][] first,
			double[][] second) {

		String rational = assertRationalMatrix(first);

		if (rational != null) {

			return rational;

		}

		rational = assertRationalMatrix(second);

		if (rational != null) {

			return rational;

		}

		if (first.length != second.length) {

			return "The matrices do not have the same number of rows:"
					+ NEWLINE + NEWLINE + representMatrix(first, 3) + NEWLINE
					+ NEWLINE + representMatrix(second, 3);

		}

		if (first[0].length != second[0].length) {

			return "The matrices do not have the same number of columns:"
					+ NEWLINE + NEWLINE + representMatrix(first, 3) + NEWLINE
					+ NEWLINE + representMatrix(second, 3);

		}

		return null;

	}

	/**
	 * Asserts that the specified 3x3 rotation matrix is a 'normalised' rotation
	 * matrix that comprises three rows where each row represents a valid unit
	 * vector. Asserts similarly that each column, vertically spanning three
	 * rows top to bottom, also represents a valid unit vector.<br/>
	 * A valid unit vector must comprise three elements which represent the x
	 * and y and z coordinates of the unit vector respectively where {@code (x ^
	 * 2) + (y ^ 2) + (z ^ 2) = 1} according to Pythagoras' theorem when the
	 * result of that equation is rounded to the specified number of decimal
	 * places using the half-even rounding mode.
	 * 
	 * @param matrix
	 *            the 3x3 rotation matrix.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return null if the rotation matrix comprises three rows each with three
	 *         columns where each row and column comprises a valid unit vector,
	 *         otherwise an error message.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static String assertRationalRotationMatrix(double[][] matrix,
			int decimalPlaces) {

		String rational = assertRationalMatrix(matrix);

		if (rational != null) {

			return rational;

		}

		if (matrix.length != 3) {

			return "The rotation matrix does not comprise three rows:"
					+ NEWLINE + representMatrix(matrix, decimalPlaces);

		}

		if (matrix[0].length != 3) {

			return "The rotation matrix does not comprise three columns:"
					+ NEWLINE + representMatrix(matrix, decimalPlaces);

		}

		rational = assertRationalUnitVector(matrix[0], decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the first row: "
					+ rational;

		}

		rational = assertRationalUnitVector(matrix[1], decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the second row: "
					+ rational;

		}

		rational = assertRationalUnitVector(matrix[2], decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the third row: "
					+ rational;

		}

		rational = assertRationalUnitVector(new double[] { matrix[0][0],
				matrix[1][0], matrix[2][0] }, decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the first column: "
					+ rational;

		}

		rational = assertRationalUnitVector(new double[] { matrix[0][1],
				matrix[1][1], matrix[2][1] }, decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the second column: "
					+ rational;

		}

		rational = assertRationalUnitVector(new double[] { matrix[0][2],
				matrix[1][2], matrix[2][2] }, decimalPlaces);

		if (rational != null) {

			return "The rotation matrix does not comprise a rational unit vector along the third column: "
					+ rational;

		}

		return null;

	}

	/**
	 * Asserts that the specified 3x1 unit vector has three elements which
	 * represent the x and y and z coordinates of a unit vector respectively
	 * where {@code (x ^ 2) + (y ^ 2) + (z ^ 2) = 1} according to Pythagoras'
	 * theorem when the result of that equation is rounded to the specified
	 * number of decimal places using the half-even rounding mode.
	 * 
	 * @param vector
	 *            the 3x1 unit vector.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return null if the unit vector has three elements and conforms with
	 *         Pythagoras' theorem, otherwise an error message.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static String assertRationalUnitVector(double[] vector,
			int decimalPlaces) {

		if (vector == null) {

			return "The unit vector is null";

		}

		if (vector.length != 3) {

			return "The unit vector does not comprise three elements: "
					+ representVector(vector, decimalPlaces);

		}

		double x = vector[0];
		double y = vector[1];
		double z = vector[2];

		BigDecimal xsquared = new BigDecimal(x).pow(2);
		BigDecimal ysquared = new BigDecimal(y).pow(2);
		BigDecimal zsquared = new BigDecimal(z).pow(2);

		BigDecimal sum = xsquared.add(ysquared).add(zsquared);

		BigDecimal rounded = sum.divide(BigDecimal.ONE, decimalPlaces,
				RoundingMode.HALF_EVEN);

		if (rounded.compareTo(BigDecimal.ONE) == 0) {

			return null;

		}

		return "The unit vector does not conform with Pythagoras' theorem: "
				+ representVector(vector, decimalPlaces);

	}

	/**
	 * Creates a human-readable representation of the specified NxN matrix where
	 * each value comprised by the matrix is rounded to the specified number of
	 * decimal places using the half-even rounding mode.
	 * 
	 * @param matrix
	 *            the NxN matrix.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static String representMatrix(double[][] matrix, int decimalPlaces) {

		if (matrix == null) {

			return "null";

		}

		int longest = 0;

		List<List<String>> values = new LinkedList<List<String>>();

		int rows = matrix.length;

		for (int row = 0; row < rows; row++) {

			List<String> rowValues = new LinkedList<String>();

			double[] currentRow = matrix[row];

			if (currentRow == null) {

				rowValues.add(" null");

				if (longest < 5) {

					// ensure that the padding is set to the length of the
					// longest represented value
					longest = 5;

				}

			} else {

				int columns = currentRow.length;

				for (int column = 0; column < columns; column++) {

					// do not perform null check on value because java does not
					// allow the assignment of null to primitives

					double value = currentRow[column];

					String rounded = new BigDecimal(value)
							.divide(BigDecimal.ONE, decimalPlaces,
									RoundingMode.HALF_EVEN)
							.stripTrailingZeros().toPlainString();

					rounded = rationaliseRepresentation(rounded);

					if (value >= 0d) {

						// shift positive values one space to the right in order
						// to align digits with other digits and not with any
						// minus sign
						rounded = " " + rounded;

					}

					int length = rounded.length();

					if (length > longest) {

						// ensure that the padding is set to the length of the
						// longest represented value
						longest = length;

					}

					rowValues.add(rounded);

				}

			}

			values.add(rowValues);

		}

		StringBuilder representation = new StringBuilder();

		int rowsLastIndex = rows - 1;

		for (int row = 0; row < rows; row++) {

			List<String> rowValues = values.get(row);

			int columns = rowValues.size();

			int columnsLastIndex = rowValues.size() - 1;

			for (int column = 0; column < columns; column++) {

				String value = rowValues.get(column);

				representation.append(value);

				if (column < columnsLastIndex) {

					int length = value.length();

					int difference = longest - length;

					// add an extra space to separate values
					difference++;

					StringBuilder spaces = new StringBuilder();

					for (int space = 0; space < difference; space++) {

						spaces.append(' ');

					}

					representation.append(spaces);

				}

			}

			if (row < rowsLastIndex) {

				representation.append(NEWLINE);

			}

		}

		return representation.toString();

	}

	/**
	 * Creates a human-readable representation of the specified Nx1 vector where
	 * each value comprised by the vector is rounded to the specified number of
	 * decimal places using the half-even rounding mode.
	 * 
	 * @param vector
	 *            the vector.
	 * @param decimalPlaces
	 *            the number of decimal places.
	 * @return the representation.
	 * @see java.math.RoundingMode#HALF_EVEN
	 */
	public static String representVector(double[] vector, int decimalPlaces) {

		if (vector == null) {

			return "null";

		}

		StringBuilder representation = new StringBuilder();

		int elements = vector.length;

		int elementsLastIndex = elements - 1;

		for (int element = 0; element < elements; element++) {

			// do not perform null check on value because java does not allow
			// the assignment of null to primitives

			double value = vector[element];

			String rounded = new BigDecimal(value)
					.divide(BigDecimal.ONE, decimalPlaces,
							RoundingMode.HALF_EVEN).stripTrailingZeros()
					.toPlainString();

			rounded = rationaliseRepresentation(rounded);

			representation.append(rounded);

			if (element < elementsLastIndex) {

				representation.append(' ');

			}

		}

		return representation.toString();

	}

	/**
	 * Calculates the dot product of the specified 3x3 rotation matrices and
	 * yields the resultant 3x3 rotation matrix.
	 * 
	 * @param first
	 *            the first 3x3 rotation matrix.
	 * @param second
	 *            the second 3x3 rotation matrix.
	 * @return the multiplied 3x3 rotation matrix.
	 */
	static double[][] multiplyRotations(double[][] first, double[][] second) {

		double xx1 = first[0][0];
		double xy1 = first[0][1];
		double xz1 = first[0][2];

		double yx1 = first[1][0];
		double yy1 = first[1][1];
		double yz1 = first[1][2];

		double zx1 = first[2][0];
		double zy1 = first[2][1];
		double zz1 = first[2][2];

		double xx2 = second[0][0];
		double xy2 = second[0][1];
		double xz2 = second[0][2];

		double yx2 = second[1][0];
		double yy2 = second[1][1];
		double yz2 = second[1][2];

		double zx2 = second[2][0];
		double zy2 = second[2][1];
		double zz2 = second[2][2];

		double xx = (xx1 * xx2) + (xy1 * yx2) + (xz1 * zx2);
		double xy = (xx1 * xy2) + (xy1 * yy2) + (xz1 * zy2);
		double xz = (xx1 * xz2) + (xy1 * yz2) + (xz1 * zz2);

		double yx = (yx1 * xx2) + (yy1 * yx2) + (yz1 * zx2);
		double yy = (yx1 * xy2) + (yy1 * yy2) + (yz1 * zy2);
		double yz = (yx1 * xz2) + (yy1 * yz2) + (yz1 * zz2);

		double zx = (zx1 * xx2) + (zy1 * yx2) + (zz1 * zx2);
		double zy = (zx1 * xy2) + (zy1 * yy2) + (zz1 * zy2);
		double zz = (zx1 * xz2) + (zy1 * yz2) + (zz1 * zz2);

		double[][] multiplied = new double[][] { new double[] { xx, xy, xz },
				new double[] { yx, yy, yz }, new double[] { zx, zy, zz } };

		return multiplied;

	}

	/**
	 * Normalises the specified 3x1 vector, dividing each member by the extent
	 * of the vector calculated using {@code extent = (x ^ 2 + y ^ 2 + z ^ 2) ^
	 * 1/2}, and yields the resultant 3x1 unit vector.
	 * 
	 * @param vector
	 *            the 3x1 vector.
	 * @return the 3x1 unit vector.
	 */
	static double[] normaliseVector(double[] vector) {

		double xvector = vector[0];
		double yvector = vector[1];
		double zvector = vector[2];

		double extent = Math.sqrt(Math.pow(xvector, 2) + Math.pow(yvector, 2)
				+ Math.pow(zvector, 2));

		double xnormalised = xvector / extent;
		double ynormalised = yvector / extent;
		double znormalised = zvector / extent;

		double[] normalised = new double[] { xnormalised, ynormalised,
				znormalised };

		return normalised;

	}

	/**
	 * Creates an identical copy of the specified NxN matrix.
	 * 
	 * @param matrix
	 *            the NxN matrix.
	 * @return the copied NxN matrix.
	 */
	static double[][] copyMatrix(double[][] matrix) {

		int rows = matrix.length;
		int columns = matrix[0].length;

		double[][] copy = new double[rows][columns];

		for (int row = 0; row < rows; row++) {

			for (int column = 0; column < columns; column++) {

				copy[row][column] = matrix[row][column];

			}

		}

		return copy;

	}

	/**
	 * Creates an identical copy of the specified Nx1 vector.
	 * 
	 * @param vector
	 *            the Nx1 vector.
	 * @return the copied Nx1 vector.
	 */
	public static double[] copyVector(double[] vector) {

		int elements = vector.length;

		double[] copy = new double[elements];

		for (int element = 0; element < elements; element++) {

			copy[element] = vector[element];

		}

		return copy;

	}

	/**
	 * Removes all leading zeros from the integer part and all trailing zeros
	 * from the fraction part, and any resultant trailing decimal point, from
	 * the specified representation of a number in order to rationalise the
	 * representation of the number.
	 * 
	 * @param representation
	 *            the representation.
	 * @return the rationalised representation.
	 */
	private static String rationaliseRepresentation(String representation) {

		char[] characters = representation.toCharArray();

		characters = reduceCharacterArrayCapacityAtStart(characters, '0');

		if (representation.contains(".")) {

			characters = reduceCharacterArrayCapacity(characters, '0');
			characters = reduceCharacterArrayCapacity(characters, '.');

		}

		if (characters.length == 0) {

			characters = new char[] { '0' };

		} else if (characters[0] == '.') {

			characters = joinCharacterArrays(new char[] { '0' }, characters);

		}

		return new String(characters);

	}

	/**
	 * Joins the specified arrays of characters by appending the specified
	 * second array of characters to the end of the specified first array of
	 * characters.
	 * 
	 * @param first
	 *            the first characters.
	 * @param second
	 *            the second characters.
	 * @return the joined array.
	 */
	private static char[] joinCharacterArrays(char[] first, char[] second) {

		int firstLength = first.length;
		int secondLength = second.length;

		char[] joined = new char[firstLength + secondLength];

		for (int index = 0; index < firstLength; index++) {

			joined[index] = first[index];

		}

		for (int secondIndex = 0, joinedIndex = firstLength; secondIndex < secondLength; secondIndex++, joinedIndex++) {

			joined[joinedIndex] = second[secondIndex];

		}

		return joined;

	}

	/**
	 * Pseudo-iteratively decrements the capacity of the specified array of
	 * characters at the end of the array while the last character in the array
	 * is equal to the specified character.
	 * 
	 * @param characters
	 *            the characters.
	 * @param character
	 *            the character.
	 * @return the reduced array.
	 */
	private static char[] reduceCharacterArrayCapacity(char[] characters,
			char character) {

		int charactersLength = characters.length;

		int reducedLength = -1;

		locateCharacters: for (int index = charactersLength - 1; index > -1; index--) {

			char lastCharacter = characters[index];

			if (lastCharacter == character) {

				reducedLength = index;

			} else {

				break locateCharacters;

			}

		}

		if (reducedLength == -1) {

			return characters;

		}

		if (reducedLength == 0) {

			return new char[0];

		}

		char[] reduced = new char[reducedLength];

		for (int index = 0; index < reducedLength; index++) {

			reduced[index] = characters[index];

		}

		return reduced;

	}

	/**
	 * Pseudo-iteratively decrements the capacity of the specified array of
	 * characters at the beginning of the array while the first character in the
	 * array is equal to the specified character.
	 * 
	 * @param characters
	 *            the characters.
	 * @param character
	 *            the character.
	 * @return the reduced array.
	 */
	private static char[] reduceCharacterArrayCapacityAtStart(
			char[] characters, char character) {

		int charactersLength = characters.length;

		int reducedOffset = 0;

		locateCharacters: for (int index = 0; index < charactersLength; index++) {

			char firstCharacter = characters[index];

			if (firstCharacter == character) {

				reducedOffset = index + 1;

			} else {

				break locateCharacters;

			}

		}

		if (reducedOffset == 0) {

			return characters;

		}

		if (reducedOffset == charactersLength) {

			return new char[0];

		}

		int reducedLength = charactersLength - reducedOffset;

		char[] reduced = new char[reducedLength];

		for (int reducedIndex = 0, charactersIndex = reducedOffset; reducedIndex < reducedLength; reducedIndex++, charactersIndex++) {

			reduced[reducedIndex] = characters[charactersIndex];

		}

		return reduced;

	}

}