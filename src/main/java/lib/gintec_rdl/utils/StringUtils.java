package lib.gintec_rdl.utils;

public final class StringUtils {
    public static boolean isEmpty(String string){
        return string == null || string.isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
