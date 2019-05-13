package lib.gintec_rdl.jini.extenstion;

import lib.gintec_rdl.jini.types.ByteArray;

import java.lang.reflect.Field;

/**
 * This is the rather minuscule default type mapper. Only supports <strong>MOST</strong> primitive types.
 */
public class DefaultTypeMapper implements TypeMapper {

    @Override
    public void map(Object instance, Field field, String value) throws MapException {
        final Class type = field.getType();
        try {
            switch (type.getName()) {
                case "int":
                case "java.lang.Integer":
                    field.setInt(instance, Integer.valueOf(value));
                    break;
                case "char":
                case "java.lang.Character":
                    field.setChar(instance, value.charAt(0));
                    break;
                case "java.lang.String":
                    field.set(instance, value);
                    break;
                case "byte":
                case "java.lang.Byte":
                    field.setByte(instance, value.getBytes()[0]);
                    break;
                case "short":
                case "java.lang.Short":
                    field.setShort(instance, Short.valueOf(value));
                    break;
                case "float":
                case "java.lang.Float":
                    field.setFloat(instance, Float.valueOf(value));
                    break;
                case "long":
                case "java.lang.Long":
                    field.setLong(instance, Long.valueOf(value));
                    break;
                case "double":
                case "java.lang.Double":
                    field.setDouble(instance, Double.valueOf(value));
                    break;
                case "boolean":
                case "java.lang.Boolean":
                    field.setBoolean(instance, Boolean.valueOf(value));
                    break;
                case "java.util.Date":
                    TypeMapperFactory.getInstance().getMapper(DateMapper.class).map(instance, field, value);
                    break;
                case "lib.gintec_rdl.jini.types.ByteArray":
                    field.set(instance, new ByteArray(value.getBytes()));
                    break;
                case "[B":
                    field.set(instance, value.getBytes());
                    break;
                default:
                    throw new MapException("Unsupported type: " + type.getName());
            }
        } catch (Exception e) {
            throw new MapException("Error when mapping type " + type, e);
        }
    }
}
