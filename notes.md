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