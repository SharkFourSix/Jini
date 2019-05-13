package lib.gintec_rdl.jini.extenstion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Class for instantiating and caching {@link TypeMapper type mapper}s
 */
public final class TypeMapperFactory {
    private static TypeMapperFactory instance;
    private final Set<TypeMapper> mapperSet;

    private DateFormat mDateFormat;

    private TypeMapperFactory(){
        mapperSet = Collections.synchronizedSet(new LinkedHashSet<>());
        mDateFormat = SimpleDateFormat.getInstance();
    }

    public synchronized static TypeMapperFactory getInstance() {
        synchronized (TypeMapperFactory.class){
            return (instance == null ? (instance = new TypeMapperFactory()) : instance);
        }
    }

    public void setDateFormat(String dateFormat){
        mDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

    /**
     *
     * @param type The class type of the mapper to return
     * @return Returns a {@link TypeMapper} instance of the given class, instantiating and caching it if is wasn't before.
     */
    public TypeMapper getMapper(Class type){
        TypeMapper mapper;
        synchronized (mapperSet){
            for (final TypeMapper typeMapper : mapperSet) {
                if (typeMapper.getClass() == type) {
                    return typeMapper;
                }
            }
            try{
                mapperSet.add(mapper = (TypeMapper) type.newInstance());
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            return mapper;
        }
    }
}
