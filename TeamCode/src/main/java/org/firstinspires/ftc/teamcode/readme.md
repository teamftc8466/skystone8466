**Package definitions**
##<code>auto package</code>
Contains all of the autonomous modes for the robot,
including the test modes. It also contains the main
autonomous mode, which acts as a template for other
autonomous modes.

##<code>teleOP package</code>
Contains all of the teleOP modes, including test mo
-des. The main teleOP mode is in the class also, wh
-ich acts as a template for any test classes. Each
class must extend the robot hardware class and crea
-te the object(s) that it uses.

**Class definitons**
##<code>RobotHardware class</code>
The collection of all the robot subsystem classes,
it is stored outside of the package structure so
that the main teleop modes or autonomous modes can
extend it and use its methods. The methods in the
RobotHardware class has all of the subsystem class
objects, along with creation and return methods.

##Subsystem classes
Each subsystem class contains methods to be used in
both teleOP and autonomous respectively.

//Todo: add subsystem test definition
//Todo: add full class formatting info

