package LoggerService;

import java.util.Arrays;

public class Logger {

   public static void info(String className,String methodName, String info){
      System.out.println("-- INFO :: \""+ info +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
   }

    public static void warn(String className,String methodName,String warn){
        System.out.println("-- WARN :: \""+ warn +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
    }

    public static void severe(String className, String methodName, String errorMessage,Throwable e){
       if(e != null){
           System.out.println("-- Expection :: \""+ errorMessage +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"" + " -- StackTrace :: " + e);
       }
       else {
           System.out.println("-- Expection :: \""+ errorMessage +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
       }
    }

}
