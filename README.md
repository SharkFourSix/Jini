### Jini 1.0

A Java library to load and map `.ini` properties to java objects.

##### Adding to project

Add JitPack repository
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Link to GitHub repository
```xml
<dependency>
    <groupId>com.github.SharkFourSix</groupId>
    <artifactId>Jini</artifactId>
    <version>1.0</version>
</dependency>
```

##### Usage

Suppose you have the following `.ini` file:

```
# This is a comment
firstName = John

# Another comment
lastName = Doe

age = 25
```
1. Create your type and annotate fields as needed

```java
import lib.gintec_rdl.jini.annotations.Property;

public class Person {
    @Property(name = "firstName")
    public String firstName;
    
    @Property(name = "lastName")
    public String lastName;
    
    @Property(name = "age")
    public int age;
}
```

2. Map the file to an instance of the object. Note that the class must 
have an empty-argument constructor.

```java
import lib.gintec_rdl.jini.Jini;

public class Example {
    public static void main(String[] args){
        Person person = Jini.load(".ini", Person.class);
        System.out.println("First Name: " + person.firstName);
    }
}
```

3. You can also extend the functionality of the library if you have custom 
types.

Suppose this is your *.ini* file

```
Title = CEO
person = John Doe/56
```

```java
import lib.gintec_rdl.jini.Jini;
import lib.gintec_rdl.jini.annotations.Property;
import lib.gintec_rdl.jini.extenstion.MapException;
import lib.gintec_rdl.jini.extenstion.TypeMapper;
import lib.gintec_rdl.jini.extenstion.TypeMapperFactory;
import java.lang.reflect.Field;
public class Example {
    
    public static class PersonMapper implements TypeMapper {
        @Override public void map(Object instance, Field field, String value)throws MapException {
            try{
                Person person = new Person();
                person.firstName = value.substring(0, value.indexOf(' '));
                person.lastName = value.substring(value.indexOf(' ')+1, value.indexOf('/'));
                person.age = Integer.valueOf(value.substring(value.indexOf('/')+1));
                field.set(instance, person);
            }catch (IllegalAccessException e){
                
            }
        }
    }
    
    public class Employee {
        @Property(name = "person", mapper = PersonMapper.class)
        public Person person;
        
        @Property(name = "Title")
        public String Title;
    }
    
    public static void main(String[] args){
        Employee employee = Jini.load(".ini", Employee.class);
    }
}
```

For more refer to the [test file](src/test/java/lib/gintec_rdl/Jini/JiniTest.java)