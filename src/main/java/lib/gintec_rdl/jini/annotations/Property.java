package lib.gintec_rdl.jini.annotations;

import lib.gintec_rdl.jini.extenstion.DefaultTypeMapper;
import lib.gintec_rdl.jini.extenstion.TypeMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this interface to annotate fields to be mapped from an INI file to Java objects.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
    /**
     *
     * @return The name of the property as defined in the INI file.
     */
    String name();

    /**
     *
     * @return Default value if the property does not exist
     */
    String default_value() default "";

    /**
     *
     * @return Whether or not this property is required. An exception will be thrown if the property is required
     * and missing in the source (INI) file.
     */
    boolean required() default true;

    /**
     * Custom regular expression used to validate the value.
     * @return .
     */
    String regex() default "";

    /**
     * Sets the class to be used for mapping this property. Default is {@link DefaultTypeMapper}
     * @return A mapper class for this property
     */
    Class<? extends TypeMapper> mapper() default DefaultTypeMapper.class;
}