package lib.gintec_rdl.jini.extenstion;

import java.lang.reflect.Field;

/**
 * Type mappers provide an extension facility by allowing mapping of custom objects.
 */
public interface TypeMapper {
    /**
     * Map the given string value to a Java type
     * @param instance An instance the field is a member of
     * @param field The field whose type is to be mapped
     * @param value The value to be transformed or mapped to the desired Java object.
     * @throws MapException If the implementing extension throws it.
     */
    void map(Object instance, Field field, String value) throws MapException;
}
