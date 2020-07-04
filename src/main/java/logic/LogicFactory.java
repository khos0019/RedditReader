package logic;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public abstract class LogicFactory<T> {

    private static String PACKAGE = "logic";
    private static String SUFFIX = "Logic";

    //empty constructor
    private LogicFactory() {

    }

    /**
     * Allows you to get the logics needed to create and add entities.
     *
     * @param entityName specific entity name
     * @return the next function with class name
     */
    public static <T> T getFor(String entityName) {

        try {
            Class<?> genericClass = Class.forName(PACKAGE + "." + entityName + SUFFIX);
            return getFor((Class<T>) genericClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a new instance of the declared constructor.
     *
     * @param <T>
     * @param type
     * @return a new instance or null
     */
    public static <T> T getFor(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
