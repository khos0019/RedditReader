package logic;


/**
 *
 * @author Shariar (Shawn) Emami
 */
public abstract class LogicFactory<T> {
        
    private static String PACKAGE = "logic";
    private static String SUFFIX = "Logic";
    
    private LogicFactory(){
        
    }
  
    public static <T> T getFor(String entityName){
        StringBuilder sb = new StringBuilder();
        
        sb.append(Character.toUpperCase(entityName.charAt(0)));
        
        if (entityName.length() > 1) {
            sb.append(entityName.substring(1, entityName.length()).toLowerCase());
        }
        
        String entityClass = sb.toString();
        
        String logicClass = entityClass + SUFFIX;
        
    }
    
   
    public static <T> T getFor(Class<T> type){
        try{
           return type.newInstance();
           
        } catch(Exception e){
            e.printStackTrace();
        }
       return null;
    }
    
        
 /*   public T getFor(String entityName) {

        String[] parts = entityName.split("_");
        String className = "";
        for (String part : parts) {
            className = className + part.substring(0, 1).toUpperCase() +
                    part.substring(1).toLowerCase();
        }
        System.out.println("Class name: " + className);

        try {
            Class<?> genericClass = Class.forName(PACKAGE + "." + className + SUFFIX);
            return getFor((Class<T>) genericClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
 */ 

    //this is just a place holder to keep the code working.
/*    public static AccountLogic getFor(String account) {
        return new AccountLogic();
    }
*/  
}
