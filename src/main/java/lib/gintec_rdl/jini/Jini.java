package lib.gintec_rdl.jini;


import lib.gintec_rdl.jini.annotations.Property;
import lib.gintec_rdl.jini.extenstion.TypeMapper;
import lib.gintec_rdl.jini.extenstion.TypeMapperFactory;
import lib.gintec_rdl.utils.StringUtils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public final class Jini {

    /**
     * <p>Maps the string values in the given properties container to the given target class type.</p>
     * @param properties Properties containing string values to be mapped to individual fields in the target class.
     * @param type Target type to be mapped
     * @param <T> Generic type extension
     * @return Returns an instance of the mapped target class
     * @throws LoadException If an errors were encountered during the mapping operation.
     * @see #load(String, Class)
     */
    public static <T> T map(Properties properties, Class<T> type) throws LoadException {
        final ArrayList<Field> fields = getTypeFields(type, false);
        try {
            final T instance = type.getDeclaredConstructor().newInstance();
            for (Field field: fields){
                final Property property;
                field.setAccessible(true);
                if((property = field.getDeclaredAnnotation(Property.class)) == null){
                    continue;
                }
                final Class<? extends TypeMapper> typeMapperImplClass = property.mapper();
                final TypeMapper typeMapper = Objects.requireNonNull(TypeMapperFactory.getInstance()
                        .getMapper(typeMapperImplClass), "Cannot load mapper instance " + typeMapperImplClass);
                final String propertyName = property.name();
                if(!properties.containsKey(propertyName)){
                    if(StringUtils.isEmpty(property.default_value())){
                        if(property.required()){
                            throw new Exception("Required property '" + propertyName + "' missing.");
                        }
                    }else{
                        typeMapper.map(instance, field, property.default_value());
                    }
                    continue;
                }
                final String propertyValue = properties.getProperty(propertyName);
                if(StringUtils.isEmpty(propertyValue) && StringUtils.isNotEmpty(property.default_value())){
                    typeMapper.map(instance, field, property.default_value());
                    continue;
                }
                if(StringUtils.isNotEmpty(property.regex())){
                    if(!propertyValue.matches(property.regex())){
                        throw new Exception("Value of property " + propertyName + " does not match regex.");
                    }
                }
                typeMapper.map(instance, field, propertyValue);
            }
            return instance;
        }catch (Exception e){
            throw new LoadException(e);
        }
    }

    /**
     * <p>Load an INI properties file and </p>
     * @param file The INI file to load.
     * @param type Target class type
     * @param <T> Generic type extension
     * @return Returns an instance of the mapped target class
     * @throws LoadException If there was an error loading or mapping the properties.
     * @see #map(Properties, Class)
     */
    public static <T> T load(String file, Class<T> type) throws LoadException {
        try(FileInputStream fis = new FileInputStream(file)){
            Properties properties = new Properties();
            properties.load(fis);
            return map(properties, type);
        }catch (Exception e){
            throw new LoadException(e);
        }
    }

    private static ArrayList<Field> getTypeFields(Class c, boolean includeAncestorFields){
        final ArrayList<Field> fields = new ArrayList<>(Arrays.asList(c.getDeclaredFields()));
        if(includeAncestorFields){
            fields.addAll(Arrays.asList(c.getFields()));
        }
        return fields;
    }

    public static class LoadException extends RuntimeException {
        private LoadException(Exception e){
            super(e);
        }

    }
}