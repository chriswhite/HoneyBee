HoneyBee
Games, Robotics and 3D Modelling

    HoneyBee operates directly on transform matrices, but also supports rotation matrices and co-ordinate vectors, for integration with a wide range of game engines, modelling environments and hardware interfaces

    HoneyBee provides an intuitive range of operations that bridge between everyday concepts, such as Euler angles and Cartesian coordinates, and higher mathematical constructs such as unit vectors and orientation matrices

    HoneyBee exposes two application interfaces to enable low-level programming with arrays or high-level development with objects

    HoneyBee is released under the GNU General Public License

Introduction
Working Example

Create a transform matrix for an object with a position and orientation equivalent to the global axes:

double[][] origin = HoneyBee.createTransformForOrigin();

Assign that position and orientation to a kitten that is chasing a ball:

double[][] kitten = origin;

Assign that orientation to a ball that is four units of global space in front of the kitten:

double[][] ball = HoneyBee.translateAlongGlobalAxes(origin, 4, 0, 0);

// prints
// 1  0  0  0
// 0  1  0  0
// 0  0  1  0
// 0  0  0  1
System.out.println(HoneyBee.representMatrix(kitten, 2));

// prints
// 1  0  0  4
// 0  1  0  0
// 0  0  1  0
// 0  0  0  1
System.out.println(HoneyBee.representMatrix(ball, 2));

Move the ball three units of global space to the right and therefore down the Z axis:

ball = HoneyBee.translateAlongGlobalAxes(ball, 0, 0, 3);

// prints
// 1  0  0  4
// 0  1  0  0
// 0  0  1  3
// 0  0  0  1
System.out.println(HoneyBee.representMatrix(ball, 2));

Draw a line from the kitten to the ball that will become the new local X axis of the kitten:

double[] xAxis = HoneyBee.createUnitVectorFromTransforms(kitten, ball);

Assign the orientation of the global Y axis to the local Y axis of the kitten so that the kitten is upright with respect to the global axes:

double[] yAxis = HoneyBee.GLOBAL_Y_AXIS;

Calculate the unit vector of the local Z axis of the kitten that must be perpendicular to the local X and Y axes:

double[] zAxis = HoneyBee.createPerpendicularUnitVector(yAxis, xAxis);

Derive the rotation and translation matrices of the kitten:

double[][] rotation = new double[][] { xAxis, yAxis, zAxis };
double[] translation = HoneyBee.getTranslationFromTransform(kitten);

Construct the new transform matrix of the kitten that is now facing the ball:

kitten = HoneyBee.createTransformForRotationAndTranslation(rotation, translation);

// prints
// 0.8  0    0.6  0
// 0    1    0    0
// 0.6  0   -0.8  0
// 0    0    0    1
System.out.println(HoneyBee.representMatrix(kitten, 2));

Calculate the distance between the kitten and the ball:

double distance = HoneyBee.measureGlobalDistance(kitten, ball);

// prints
// 5
System.out.println(distance);

Move the kitten five units of global space along its local X axis so the kitten runs to the ball:

kitten = HoneyBee.translateAlongLocalAxes(kitten, distance, 0, 0);

// prints
// 0.8  0    0.6  4
// 0    1    0    0
// 0.6  0   -0.8  3
// 0    0    0    1
System.out.println(HoneyBee.representMatrix(kitten, 2));

Note that the position of the kitten is now the same as the ball:

// prints
// 4 0 3
System.out.println(HoneyBee.representVector(HoneyBee.getTranslationFromTransform(kitten), 2));

// prints
// 4 0 3
System.out.println(HoneyBee.representVector(HoneyBee.getTranslationFromTransform(ball), 2));

Rotate the kitten by ten degrees around the global Y axis so that the kitten spins horizontally to the left:

kitten = HoneyBee.rotateAroundGlobalAxes(kitten, 0, 10, 0);

The slightly heavier ball spins a bit less the other way around the global Y axis because every action has an equal and opposite reaction:

ball = HoneyBee.rotateAroundGlobalAxes(ball, 0, -7, 0);

// prints
// 0.89  0     0.45  4
// 0     1     0     0
// 0.45  0    -0.89  3
// 0     0     0     1
System.out.println(HoneyBee.representMatrix(kitten, 2));

// prints
// 0.99  0     0.12  4
// 0     1     0     0
//-0.12  0     0.99  3
// 0     0     0     1
System.out.println(HoneyBee.representMatrix(ball, 2));

These 4x4 'transform matrices' may be passed to your game engine to move objects around.

Some game engines use 'rotation matrices' instead of transform matrices so alternatively use the HoneyBee helper methods to extract the 'rotation matrix' and 'translation matrix':

rotation = HoneyBee.getRotationFromTransform(kitten);
translation =  HoneyBee.getTranslationFromTransform(kitten);

// prints
// 0.89  0     0.45
// 0     1     0
// 0.45  0    -0.89
System.out.println(HoneyBee.representMatrix(rotation, 2));

// prints
// 4 0 3
System.out.println(HoneyBee.representVector(translation, 2));

These rotation and translation matrices may be passed to your game engine instead of transform matrices.

© 2010-2013 Chris White. All rights reserved
