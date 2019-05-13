package lib.gintec_rdl.jini.extenstion;


import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * <p>Converts a string value to a {@link Date} object using current locale.</p>
 * <p>For supported formats refer to {@link java.text.SimpleDateFormat}</p>
 * <p>To set the date format pattern to be used by all date types, use {@link TypeMapperFactory#setDateFormat(String)}</p>
 * <p>If {@link TypeMapperFactory#getDateFormat()} returns null, the method calls {@link DateFormat#getInstance()}</p>
 * @see java.text.SimpleDateFormat
 */
public class DateMapper implements TypeMapper {
    @Override
    public void map(Object instance, Field field, String value) throws MapException {
        try {
            if("java.util.Date".equalsIgnoreCase(field.getType().getName())){
                Date date;
                if(value.matches("^[0-9]+$")){
                    date = new Date(Long.valueOf(value));
                }else{
                    DateFormat dateFormat = TypeMapperFactory.getInstance().getDateFormat();
                    if(dateFormat == null){
                        dateFormat = DateFormat.getInstance();
                    }
                    date = dateFormat.parse(value);
                }
                field.set(instance, date);
            }else{
                throw new IllegalArgumentException("Unsupported type " + field.getType().getName());
            }
        }catch (ParseException|IllegalAccessException e){
            throw new MapException(e);
        }
    }
}
