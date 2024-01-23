Notes for cs 240
# Java Basics

## Primitive Data Types
```java
long long1 = 10;
long long2 = 10L;
```
The difference is '10L' will be slightly faster because it won't have to pad it into a long later on.

```java
System.out.println(anInt + ", " + aByte + ", " + long1 + ", " + long2);
System.out.println("%d, %d, %d\n", anInt, aByte, long1, long2);
```
In the second example, we are passing what it out of the string into the strings for %d as parameters.
```java
float Float = 2.5f;
double aDouble = 1.0/3.0;
```
Have to put the f at the end of the float or it will assume the number is a double and will give you an error.
```java
System.out.println("%.2f" aFloat);
```
Tells it to print out the float to 2 decimal places.
```java
char char1 = 'a';
char char2 = <askii value>
```
Can assign a char using the character's askii value.
Booleans are the other primitive data times

## Strings
Converting a string to an integer. Can be done with the other primitive data types.
```java
int Integer.parseInt(String value);
```
String declaration and assignment (both work). Always use 1st example, it's more efficient.
```java
String s = "Hello";
String s = new String("Hello");
```
String concatenation. Since strings are immutable, concatenation always creates a new string.
```java
String s3 = s1 + " " + s2;
```
String formatting. Puts s1 and s2 in as parameters a '%s' in our string.
```java
String s3 = String.format("%s %s", s1, s2);
```
### String Builder
Can append any data type to a stringBuilder. Much more efficient if than concatenation.
```java
StringBuilder builder = new StringBuilder();
builder.append("Using a");
builder.append(" StringBuilder.");
String str = builder.toString();
```
## Arrays
Declare an int array
```java
int [] intArray;
```
Create array. Automatically fills it with 0 (or whatever the equivalent would be for that data type).
```java
intArray = new int[10];
```
Initialize arrays:
```java
intArray[0] = 500;
```
Declare, create and initialize an array:
```java
int [] intArray2 = {2, 7, 36, 543};
```
Get array length:
```java
intArray2.length;
```
For each loop:
```java
for(int value : intArray2)
```
Nested arrays:
```java
char[][] ticTacToeBoard = new char[3][3];
```
## Command-line Arguments
```java
public static void main(Stirng [] args)
```
arg[0] is the first argument. The filename isn't at index 0.
## Packages
A logical grouping of classes. Package name becomes part of the class name.
### Import
Provides a shorthand to allow us to refer to a class by just its class name.

# Classes and objects
Classes have to be in order.
1. Package statements first
2. Import statements next (will be a dependency list of the class)
3. Class
All code in Java is written in a class. An object is an instance of a class.
## Object References
When you create an object, you allocate space for the instance variables.
The heap is the overall memory space of your computer that is shared by all of your programs.
The stack is the allocated memory for a specific program.<ul>
<li>Refer to (allow access to) objects</li>
<li>Don't allow pointer arithmetic (unlike C++). Don't allow you to accidentally go to the wrong place in memory.</li>
<li>Creation of a reference doesn't create an object.</li>
<li>References and objects are closely related but not the same thing.</li>
<li>Multiple references can refer to the same object.</li></ul>
References are equal when they point to the same object. Objects are equal when their content (instance variables) are equal.

## Instance vs. Static Variables
### Instance variables
<ul>
<li>Each obj get its own copy of all the instance vars defined in the class</li>
<li>Most vars should be instance vars</li>
<li>EX: Allows two dates objs to have different dates</li></ul>

### Static Vars
<ul>
<li>Static vars are associated w/ the class not w/ instances</li>
<li>Use in special cases where you won't create instances of a class or all instances should hsare the same values</li>
<li>EX: If the var of a date class were static, all dates in a program would represent same date.</li></ul>

## Instance vs. Static methods
### Instance methods
<ul>
<li>Methods are ossociated w/ a specific instnace</li>
<li>Invoked from a reference that refers to an instance.</li>
<li>When invoked (on an obj), the vars they access are that obj's instance vars</li></ul>

### Static Methods
<ul>
<li>Methods are associated w/ a class (not an instance)</li>
<li>Invoked by using the class name</li></ul>

## Getters / Setters (Accessors / Mutators)
<ul>
<li>Methods for getting and setting instance vars</li>
<li>Allow you to control access to instance vars. Make vars private and only allow access thru getters and setters.</li>
<li>Not required to provide getters and setters for all vars.</li>
<li>Can use your IDE to generate them from var declarations.</li></ul>

## Constructor Methods
<ul>
<li>Code executed at obj creation time.</li>
<li>Must match the class name</li>
<li>Like a method w/out a return type</li>
<li>All classes have at least one. Default constructors are written by the compiler if you don't write one.</li>
<li>Classes can have multiple constructors w/ different parameter types.</li>
<li>Constructors can invoke each other w/ 'this(...)'. Useful to provide default instance var values when none are provided.</li>
<li>Constructors invoke parent constructor w/ 'super(...)'</li>
<li>'this(...)' or 'super(...)' HAS to be the first statement.</li></ul>

# Java Records
Boiler plate code: obvious what the code is/should be, but it has to be written <br>
Easier way to write a class that has getters, setters, equals, toString and hashCode methods.
```java
public record Pet(int id, String name, String type) {}
```
## Features
<ul>
<li>Immutability (all fields are final)</li>
<li>Simplified constructor syntax</li>
<li>Automatic getters (access by using field name w/out get (p.name();)</li>
<li>Automatic equals that compares all the fields.</li>
<li>Automatic toString that represents all the fields.</li></ul>

Can add other methods to it.
```java
public record Pet(int id, String name, String type){
  Pet rename(String newName) {
    return new Pet(id, newName, type);
  }
}
```

# Exceptions
Abnormal conditions that can occur in a Java class.<br>
Errors are things that your program can't fix (such as your computer running out of RAM)
## Exceptions
Things that could be fixed by the program<br>
RuntimeExceptions are your fault (bugs in the code).<br>
Checked exceptions would be reasonable for your to address within your code (exception handling).
## Try/Catch Block
Syntax:
```java
try{
  //Code that may throw an exception
} catch (SomeExceptionType ex) {
  //Code to handle the exception
} catch (OtherExceptionType ex) {
  //Code to handle the exception
}
```
Can catch multiple exceptions at once that will execut same code:
```java
try{
  //Code that may throw an exception
} catch (SomeExceptionType | OtherExceptionType ex) {
  //Code to handle the exception
}
```
### Checked Exceptions
Have to write a catch block or explicitly say that you aren't writing it.
(Handle or Declare Rule)<br>
Ex of declare:
```java
public void method throws Exception{}
```

## Finally Blocks
Always executed no matter what.
```java
try {
  
} catch {
  
} finally{}
```
