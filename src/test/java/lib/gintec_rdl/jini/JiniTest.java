package lib.gintec_rdl.jini;

import junit.framework.TestCase;
import lib.gintec_rdl.jini.annotations.Property;
import lib.gintec_rdl.jini.extenstion.MapException;
import lib.gintec_rdl.jini.extenstion.TypeMapper;
import lib.gintec_rdl.jini.extenstion.TypeMapperFactory;
import lib.gintec_rdl.jini.types.ByteArray;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

public class JiniTest extends TestCase {

    public static class Person {
        public String firstName;
        public String lastName;
        public int age;
    }

    public static class PersonMapper implements TypeMapper {

        @Override
        public void map(Object instance, Field field, String value) throws MapException {
            final Class type = field.getType();
            try {
                if(Person.class == type){
                    Person person = new Person();
                    person.firstName = value.substring(0, value.indexOf(' '));
                    person.lastName = value.substring(value.indexOf(' ')+1, value.indexOf('/'));
                    person.age = Integer.valueOf(value.substring(value.indexOf('/')+1));
                    field.set(instance, person);
                }else{
                    throw new IllegalArgumentException("Unsupported type " + type);
                }
            }catch (IllegalAccessException e){
                throw new MapException(e);
            }
        }
    }

    public static class TestClass {

        @Property(name = "LONG")
        public long LONG;

        @Property(name = "INT")
        public int INT;

        @Property(name = "STRING")
        public String STRING;

        @Property(name = "BOOLEAN")
        public boolean BOOLEAN;

        @Property(name = "DOUBLE")
        public double DOUBLE;

        @Property(name = "DATE")
        public Date DATE;

        @Property(name = "PERSON", mapper = PersonMapper.class)
        public Person PERSON;

        @Property(name = "BYTES")
        public byte[] BYTES;

        @Property(name = "BASE64")
        public ByteArray BASE64;

        public String SKIP_ME = "Skip me";
    }

    public void testMap() throws ParseException {
        double delta = 0.0D;

        Properties properties = new Properties();
        properties.put("LONG", Long.toString(Long.MAX_VALUE));
        properties.put("INT", Long.toString(Integer.MAX_VALUE));
        properties.put("STRING", "Hello World!");
        properties.put("BOOLEAN", "true");
        properties.put("DOUBLE", Double.toString(Double.MAX_VALUE));
        properties.put("DATE", "10/31/2019 00:00:59");
        properties.put("PERSON", "John Doe/25");
        properties.put("BASE64", "SGVsbG8gV29ybGQh");
        properties.put("BYTES", "AA");

        TypeMapperFactory.getInstance().setDateFormat("MM/dd/yyyy HH:mm:ss");
        TestClass testClass = Jini.map(properties, TestClass.class);

        Date date = new Date("2019/10/31 00:00:59");
        final byte[] bytes = {65, 65};

        assertEquals(Long.MAX_VALUE, testClass.LONG);
        assertEquals(Integer.MAX_VALUE, testClass.INT);
        assertEquals(Double.MAX_VALUE, testClass.DOUBLE, delta);
        assertTrue(testClass.BOOLEAN);
        assertEquals("Hello World!", testClass.STRING);
        assertEquals("Skip me", testClass.SKIP_ME);
        assertEquals(date, testClass.DATE);
        assertEquals("John", testClass.PERSON.firstName);
        assertEquals("Doe", testClass.PERSON.lastName);
        assertEquals(25, testClass.PERSON.age);
        assertEquals("Hello World!", testClass.BASE64.fromBase64ToString());

        // Kind of sucks to have to do this
        assertEquals(bytes[0], testClass.BYTES[0]);
        assertEquals(bytes[1], testClass.BYTES[1]);
    }
}