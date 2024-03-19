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

## Try with Resources
Opens the file for the try block. Automatically closes the file when you leave the try block.
Allows you to see the original exception that happens rather than the exception from trying to close the file.
```java
try(FileReader fr = new FileReader (file)){
  // Code
}
```
## Throw an Exception
```java
if (methodParam < 0) {
  throw new IllegalArgumentException("The parameter cannot be negative");
}
```
## Rethrowing an Exception
```java
try {
} catch (RuntimeException ex) {
  throw ex;
} catch (Exception ex){}
```
## Overridden Method Exception Rule
<ul>
<li>Overriding methods can't throw checked exceptions that aren't expected by callers of the overridden method.</li>
<li>Overriding methods can throw</li>
<ul><li>Fewer exceptions than the overridden method.</li>
<li>The same exceptions</li>
<li>Subclasses of exceptions thrown by the overridden method</li>
<li>RuntimeExceptions and errors</li></ul>
<li>
Can't throw anything else</li></ul>

## Create own exception classes
```java
public class ImageEditorException extends Exception{
  public ImageEditorException(){}
  public ImageEditorException(String message){
    super(message);
  }
  public ImageEditorException(String message, Throwable cause){
    super(message, cause);
  }
  public ImageEditorException(Throwable cause){
    super(cause);
  }
}
```

# Java Collections
## List
<ul>
<li>A sequence of elements accessed by index. get(index), set(index, value)</li>
<li>ArrayList(resizable array implementation</li>
<li>LinkedList(doubly-linked list implementation</li></ul>

## Set
<ul>
<li>A collection that contains no duplicates. add(value), contains(value), remove(value)</li>
<li>HashSeet (hash table implementation)</li>
<li>TreeSet (bst implementation)</li>
<li>LinkedHashSet (hash table + linked list implementation)</li></ul>

## Queue
<ul>
<li>A collection designed for holding elements prior to processing</li>
<li>ArrayDeque (fifo, resizable array implementation)</li>
<li>LinkedList (fifo, linkedlist implementation)</li>
<li>PriorityQueue (priority queue, binary heap implementation</li></ul>

## Deque
<ul>
<li>A queue that supports efficient insertion and removal at both ends</li>
<li>Use a deque instead of a stack.</li>
<li>ArrayDeque</li>
<li>LinkedList</li></ul>

## Map
<ul>
<li>A collection that maps keys to values</li>
<li>HashMap (hash table implementation)</li>
<li>TreeMap (bst implementation)</li>
<li>LinkedHashMap (hash table + linked list implementation)</li></ul>

## Iterable Interface
<ul>
<li>All collections (but not maps) implement the iterable interface</li>
<li>This allows them to work with Java's "for each" loop.</li>
</ul>

```java
Set <String> words;
for (String w: words) {...}
```

## Hashing-Based Collections
RULE: if equals is based on identity, so should hashCode. If it's based on value, so should hashCode. Equals and HashCode should use the same fields in their equality and hashCode equalities.<br>
Can't change info in objects that are used as keys in data structures (must remove and re-insert).
## Sorted Collections
Use a comparable method to decide how you want things to be compared when they are sorted.<br>
-1 means it goes after. 0 means they're equal. 1 means it goes first.
# Copying Objects
You can either do a shallow copy or a deep copy.<br>
Shallow copy - Copy the variable values from the original object to the copy. Any change to the copy changes the original.<br>
Deep copy - Copy the object and all the objects it references (a change doesn't change the original)<br><br>
You do not need to do a deep copy of an immutable object (e.g. string, boolean, etc.)<br>
Built in clone method is the best way to do this.
## Cloning
Gives a shallow copy:
```java
public class Person implements Cloneable{
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
```

Have to copy each mutable variable to get a deep copy:
```java
import java.util.ArrayList;

public class Person implements Cloneable {
  @Override
  public Object clone() throws CloneNotSupportedException {
    Team clone=(Team) super.clone();

    List<Person> cloneMembers=new ArrayList<>();
    for (Person person : members) {
      Person personClone = (Person) person.clone();
      cloneMembers.add(personClone);
    }
    clone.members = cloneMembers;
    
    return super.clone();
  }
}
```
# Inner classes
<ul>
<li>Static Inner classes (public static ClassName): doesn't have any special access to the class it's contained in.</li>
<li>Inner Class (public ClassName): Has access to the instance variables and methods of the class it's contained in.</li>
<li>Local Inner Class: Class that is contained inside a method of the outer class. Can access the local variables of the method it's contained in. Can only use variables that are final or effectively final (won't be changed).</li>
<li>Anonymous Class: A method in the containing class returns an iterator class that is defined in the return statement.</li></ul>

Valid moves is limited. Can't make a move that puts you in check. If you're in check, your move has to get you out of check.
Maybe make a method that finds all the moves a team can make. Then, you can check if one will get your king, get you out of check, etc.
Check to see if you're in check, take all the moves, and try them one at a time to see if you're in check. If there aren't any, then you are in check mate.
Could clone the board for every move and call is in check for that move.
Could make a move, check is in check and then undo the move.

Castling Condition 4:
It would be the equivalent to the king not being in check in the original, the moving spot and the final spot. That would mean that the Rook would end up safe.

# Software Design Principles
## Intro
<ul><li>Create systems that work</li>
<li>Easy to understand, debug and maintain</li>
<li>Hold up well under changes</li>
<li>Have reusable components</li></ul>

## Design is Inherently Iterative
<ul><li>Design, implement, test, design, implement, test, ...</li>
<li>Feedback loop from implementation back into design provides valuable knowledge</li>
<li>Designing everything before beginning implementation doesn't work</li>
<li>Beginning implementation without doing any design also doesn't work</li>
<li>The appropriate balance is achieved by interleaving design and implementation activities in relatively short iterations.</li></ul>

## Abstraction
<ul><li>Abstraction is 1 of software designer's primary tools for coping w/ complexity</li>
<li>Programming languages and OSes provide abstractions that model the underlying machine</li>
<li>Software designers must create higher-level abstraction to make it easier to understand.</li>
<li>Each abstraction is represented as a class in Java</li>
<li>Each class has carefully designed public interface that defines how the rest of the system interacts w/ it.</li>
<li>A client can invoke operations on an obj w/out understanding how it internally works.</li></ul>

## Naming
<ul><li>Class, method and var names should clearly convey their function or purpose</li></ul>

## Single Responsibility/Cohesion
<ul><li>Each abstraction should have a single responsibility</li>
<li>Each class should represent 1, well-defined concept</li>
<li>Each method should perfom one, well defined task</li>
<li>Cohesive classes and methods should be easy to name. (if they aren't, maybe it should be split up).</li></ul>

## Decomposition
<ul><li>Large problems subdivided into smaller sub-problems</li>
<li>Subdivision continues until leaf-level problems are simple enough to solve directly</li>
<li>Solutions to sub-problems are recombined into solutions to larger problems.</li>
<li>Helps us to discover the abstractions that we need</li></ul>

## Good Algorithm and Data Structure Selection
<ul><li>No amount of decomposition or abstraction will hide a fundamentally flawed selection of algorithm or data structure.</li>
</ul>

## Information Hiding
<ul><li>Many languages provide "public", "private" and protected" access levels</li>
<li>All internal implementation is "private" unless there's a good reason to make it "protected" or "public"</li>
<li>A class' public interface should be as simple as possible.</li>
<li>Don't let internal details "leak out" of a class. e.g. call a class ClassRoll instead of StudentLinkedList which specifies how it was implemented.</li>
<li>Maintain a strict separation between a class' interace and its implementation</li>
<li>Program to interfaces instead of concrete classes. (Say that a method returns a Collection rather than a linked list so that you can change the implementation after.)</li></ul>

## Code Duplication
DRY principle: Don't repeat yourself. If you repeat yourself, anytime you repeat code, you have to make fixes in multiple places.

# Streams and Files
## Ways to Read/Write Files
Streams - Read or write a file (or other source or destination of bytes) sequentially<br>
Scanner Class = Tokenize stream input (read one token at a time)<br>
Files Class - Read, copy, etc. whole files<br>
RandomAccessFile Class - Use a file pointer to read from/write to any location in a file.

### The File Class
Used to represent, create, or delete a file but not read one.
### Java I/O Streams (overview)
<ul><li>Writing data to/reading data from files</li>
<li>2 Choices: Binary-formatted or text-formatted data</li>
<li>InputStream and OutputStream (read/write bytes and binary-formatted data</li>
<li>Reader and Writer - reading/writing characters and text-formatted data</li></ul>

### Scanner class
<ul><li>Can read "tokens" one at a time from a source of characters</li>
<li>Read from a File, InputStream, </li></ul>

# JSON
## Structure of JSON documents
<ul><li>Objects deliminted by {...} with comma -separated properites in between</li>
<li>Array's delimited by [...] with comma-separated elements in between</li>
</ul>

## Parsing JSON Data
### DOM Parsers
<ul><li>Convert JSON text to an in-memory tree data structure.</li><li>After running the parser to create a DOM, traverse the DOM to extract the data you want</li></ul>

### Stream Parsers
Tokenizers that return one token at a time from the JSON data file. More efficient memory usage because it doesn't use the whole file.

### Serializer/Deserializers (best way)
<ul><li>Use a library to convert from JSON to Java Objs and vice versa</li>
<li>Gson and Jackson are both popular libraries.</li></ul>

# Chess Server Design
A Server is a program that can accept requests from some other program.<br>
Has a Web API which is a set of urls with accept some json and return a json<br>
Serves the web application to the browser.
## Design
<ul><li>Single Responsibility principle (SRP)</li>
<li>Avoid Code duplication</li>
<li>Information hiding</li></ul>

# HTTP Overview
## Client connects to Server
- Client establishes a network connection w/ the server.
- A connection allows the client to send bites to the server and vice versa.
- Have to have IP addresses for both client and server machine to connect.
- Normally IP addresses are specified by using a domain name
- The client uses the "domain name service" (DNS) to conver the server's domain name to an IP address
- Each server communicates on a particular port (an unsigned integer in the range 1-65535)
- Client must know both server program's IP and port number to connect
## HTTP GET
Request:
- Protocol://domain name:Port Number/Path (URL)
- GET urlPath HTTP Version
  - Headers go under the GET method
Response:
- HTTP Vversion  Status Code  Reason Phrase
  - Headers go under followed by an empty line
  - Response body
- Status codes:
  - 200-299 = request was handled correctly
  - 300-399 = Redirect
  - 400-499 = Errors
  - 500-599 = Server messed up

## HTTP Post
- Post  urlPath  HTTP Version
  - Headers followed by empty line
  - Request Body (data sent in)
Response looks the same but the Response body has a JSON string.

## Other Methods
- Put
  - An update operation in a REST API
  - Body included in request
- Delete
  - Delete the specified resource
  - A delete operation in a REST API
  - Body should normally not be included in request
- Others: head, options, trace, patch

# cURL
- Easy experimentation w/ HTTP endpoints
- Available everywhere
## Commands
- -x : HTTP method (GET is default) (-X POST)
- -v : verbose
- -d : body data (ed '{"name":"joe"})
- -H : Provide an HTTP header
- -o : Output response to a file
- --data-binary : Body data from file
- -D : Dump headers to the given file 

# Spark Java
Default Spark port: 4567
A simple spark server:
```java
import spark.Spark;

public class SimpleHelloBYUServer {
  public static void main(String[] args) {
    Spark.get("/hello", (req,res) -> "Hello BYU!");
  }
}
```
## Specifying the Port from the command line
```java
int port = Integer.parseInt(args[0]);
Spart.port(port);

Spark.awaitInitialization();
```
## Spark Routes
Routes are matched in the order they are defined. The first route that matches the request is invoked.
### Useful Request and Response Methods
Request
- body() - retrieve the request body
- headers() - retrieve all headers (as a set of strings)
- header("...") - retrieve the specified header

Response
- body("...") - set the response body (i.e. "Hello" sets the response body to "Hello")
- status(404) - sets the status code to 404 (not found)

### Serving static files
- Access w/ a url that doesn't include /public
- Spark expects the file(s) to be place in a subdirectory of some directory that is available on the classpath (i.e./public goes in a directory on the classpath)
- A file named index.html will be served from the base url
- src/main/java directory is the sources root (code)
- src/main/resources is the resources root (image, files, etc.)
### Overriding the Default Not Found Page
Add in the HTML that you want shown if there is a 404 error.
```java
Spark.notFound("<html><body>My custon 404 page</body></html>");
```

## Filters
- Provide a way to execute common code for multiple routes w/out code duplication
```java
before((request, response) -> {
  boolena authenticated;
//   check if authenticated
        Code if they are or aren't authenticated'
        })

```
- Filters take an optional pattern to restrict the routes to which they are applied
- There's also after filters
- You can have multiple before and/or after filters, which are executed in the order in which they appear.

## Installation
3 ways:
1. Add the dependency from File/Project structure
   - Search for com.sparkjava and select the latest version (don't select Apache Spark)
2. Create a Maven project and add the dependency to your pom.xml file
3. Create a Gradle project and add the dependency to your build.gradle file

# Writing Quality Code
## Methods
### Strong Cohesion
- Just like classes, methods should be highly cohesive.
- A cohesive method does one and only one thing, and has a name that describes effectively what it does
- Methods that do too much become obvious if we name them properly
### Good method names
- Name should be a verb or verb phrase if it doesn't return anything
- Name could be a name if it returns something, or it says what it returns
- Make method names long enough to be easily understood (don't abbreviate too much)
- Establish conventions for naming methods (e.g. boolean functions isReady, isLeapYear, etc.)
### REasons for creating methods
- Classes are abstractions that represent "things" in a system
- Methods are abstractions that represent the "algoritms" in a system
#### Top-down decomposition of algorithms
- Long or complex methods can be simplified by factoring out meaningful sections of code into well-named sub-methods
- The original becomes the "driver" method
- Avoid code duplication
- Avoid deep nesting
### Avoid Code Duplication
- Write methods for code that you would otherwise be repeating
### Deep nesting
- Excessive nesting of statements is one of the chief culprits of confusing code
- You should avoid nesting more than 3 or levels (of if statements or loop)
- Creating additional sub-methods is the best way to remove deep nesting
### Parameters
- Use all parameters
- The more parameters it has, the harder to uunderstand
- Try not to go more than 7
- Order parameters as follows: in, in-out, out
## Guidelines for initializing Data
- Initialize variables when they're declared
- Declare variables close to where they're used
## Code Layout
- Pick a style and consistently use it
## Whitespace
- Organize methods into "paragraphs"
- Use indentation to show logical structure
## Expressions
- Use parenthesize to made your expressions easier to understand
- Use spaces between operands, operators, and parentheses.
## Pseudo Code
- When writing an algorithmically complex method, write an outline of the method before starting to code
- Use English-like statements to describe the steps in teh algorithm
- Avoid syntatic elements from the target programming language
- Write pseudo-code at the level of intent (what rather than how)
- Write pseudo-code at a low enough level that generating the code should be fairly simple.
## Naming conventions
- Separating words in identifiers (camel-case or separate words w/ underscores)
- First char of class name is upper-case
- First char of method name is lower case
- First char of var name is lower case
- Constant names are usually separated by underscores

# Unit Testing
Three important types of software testing:
1. Unit testing (test units in isolation)
2. Integration Testing (test integrated units)
3. System Testing (test entire system that is fully integrated)

## What do they do?
- Create objects, call methods, and verify that the returned results are correct
- Actual results vs expected results
- Should be automated so they can be run frequently to ensure that changes, additions, bug fixes, etc. have not broken the code
- Notifies you when changes have introduced bugs, and helps to avoid destabilizing the system.
## Test Driver Program
- runts all the tests
- Must be easy to add new tests to
- Tells you that everything worked, or gives you a list of failed tests

# Databases
## Database Management Systems (DBMS)
- Implements databases
- Store data in files in a way that scales to large amounts of data and allows data to be accessed efficiently.
## Relational Databases
- Relations = tables
- Tuples = rows
- In the relational data model, data is stored in tables consisting of columns and rows.
  - Tables are like classes
  - Each row in a table stores the data you may think of as belonging to an object
  - Columns in a row store the object's attributes (instance variables)
- Each row has a "Primary Key" which is a unique identifier for that row. Relationships between rows in different tables are represent using keys
- All together it is called a schema.
- Primary key is a unique identifier
  - When that key is used in another table to relate two tables, it is called a foreign key in that table.
- Artificial primary key: a key that has no meaning outside the relational model (e.g. book with the id 3 in a table)
- Natural key: a key that has meaning outside the table (e.g. a genre with the key "nonFiction")
### Modeling Object Relationships
- One to one: A person has one Social security record; a social security record belongs to one Person
  - Can combine into 1 table.
- One to many: A book has one category; a category has many books
  - Put key of one side and put it in many
- Many to many: A person can read many books; a book can be read by many people
## SQL (Structures Query Language)
### SQL Data Types
- Character Strings
  - CHARACTER(n) or CHAR(n) - Fixed-width n-character string, padded w/ spaces as needed
  - CHARACTER VARYING(n) or VARCHAR(n) - Variable-width string with a max size of n characters
- Bit strings
  - BIT(n) - an array of n bits
  - BIT VARYING(n) - an array of max n bits
- Numbers
  - INTEGER and SMALLINT
  - FLOAT, REAL and DOUBLE PRECISION
  - NUMERIC (precision, scale) or DECIMATE(precision, scale)
- Large Objects
  - BLOB - binary large obj (images, sound, video, etc.)
  - CLOB - character large object (text documents)
- DATE, TIME, TIME WITH TIME ZONE (TIMETZ), TIMESTAMP, TIMESTAMP WITH TIME ZONE
### Creating tables
1. Primary keys
2. Null / not null (nullable by default)
3. Autoincrement
4. foreign keys
Order doesn't matter
```roomsql
create table book
(
    id integer not null primary key auto_increment,
    title varchar(255) not null,
    author varchar(255) not null,
    genre varchar(32) not null,
    category_id integer not null,
    foreign key(genre) references genre(genre),
    foreign key(category_id) references category(id)
)
```
#### Foreign key constraints
- not required - can query without them
- Enforce that values used as foreign keys exist in their parent tables
- Disallow deletes of the parent table row when referenced as a foreign key in another table
- Dissallow updats of the parent row primary key value if that would "orphan" the foreign keys
- Can specify that deletes and/or updates to the primary keys automatically affect the foreign key rows
### Dropping Tables
- Drop table
  - drop table book (throws an error if it doesn't exist)
  - drop table if exists book
- When using foreign key constraints, order of deletes matters
  - Can't delete a table with values being used as foreign keys in another table (delete the tables with the foreign keys first)
### Inserting, updating and deleting data in tables
- INSERT
  - INSERT INTO book (title, author, genre, category_id) values ('The Work and the Glory', 'Gerald Lund', "Historical Fiction', 3);
- Updates
```roomsql
UPDATE member
SET name = 'Christ Jones', email_address = 'christ@gmail.com'
WHERE id = 3
```
- Deletes
```roomsql
DELETE FROM member
WHERE id = 3
```
## Retrieving data w/ SQL Queries
SELECT Column, Column,
FROM Table, Table, ...
WHERE Condition (rows)

SELECT *
FROM Book means select everything from book

SELECT author, title
FROM book
WHERE genre = "NonFiction"

### Queries - Join
SELECT member.name, book.title
FROM member, books_read, book
WHERE member.id = books_read.member_id AND book.id = books_read.book_id (Finds all the members with all the books they've read)
could add AND genre="NonFiction" to find only the nonfiction books they've read. 

SELECT member.name, book.title
FROM member
INSERT JOIN books_read ON member.id = books_read.member_id
INNER JOIN book ON books_read.book_id = book.id
WHERE genre = "NonFiction" (does same thing)

## Database Transactions
- By default, each SQL statement is executed in a transaction by itself
- Transactions are useful when tey consist of multiple SQL statements, since you want to make sure that either all fo them or none of them succed
Multi-statement transaction,
  - Begin Transaction;
  - SQL statement 1;
  - SQL statement 2;
  - ...
  - COMMIT TRANSACTION; or ROLLBACK TRANSACTION;

## Java Database Access from Java
- load db driver
- Open a db connection
- Start a transaction
- Execute queries and/or updates
- commit or rollback the transaction
- close the db connection
- Retrieving auto-increment ids

### Open a db Connection/start a transaction
```java
import java.sql.*;

String connectionURL = "jdbc connection string" + "user=username&password=password";

Connection connection = null;
try(Connection c = DriverManager.getConection(conecctionURL)) {  // Try with resources will automatically close it.
  connection = c;
  // Start a transaction
  connection.setAutoCommit(false);
} catch(SQLException ex){
  // Error
        }
```
SELECT COUNT(*) AS count FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ? (returns 1 if db already exists and 0 if it doesn't)
in DAO's put an if statement, and only create the DB if it isn't already created.

# Terminal control codes
Draw chess board (pass in multi-dim array of chess board)
Draw Header (for black or white (param))
Draw row (x8) (Access chess piece from matrix at that pos.)
  Draw left header
  Draw squares (first square white or black?)
    Draw square
  Draw right header
Draw footer

Set a read timeout on your connection (5000 milliseconds), otherwise it will be trying to make a connection forever.

.getInputStream() is how you get the response from the server.

serverfacade.ServerFacade has login, createGame, register, etc. method.

Client Communicator class (add it) with get, post, delete method that accepts parameters, and call those methods from server facade.