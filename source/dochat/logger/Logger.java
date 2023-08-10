package dochat.logger;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Logger {

   public static void info(String className,String methodName, String info, Throwable e){
       if(e != null && info != null){
           System.out.println("-- INFO :: \""+ info +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"" + " --- Stack Trace : "+ e);
       }
       else if(e == null){
           System.out.println("-- INFO :: \""+ info +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
       }
   }

    public static void warn(String className,String methodName,String warn){
        System.out.println("-- WARN :: \""+ warn +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
    }

    public static void severe(String className, String methodName, String errorMessage,Throwable e){
        String formattedDate = getCurrentDate();
       if(errorMessage != null && e != null){
           System.out.println(formattedDate + "-- Expection :: \""+ errorMessage +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"" + " -- StackTrace :: " + Arrays.toString(e.getStackTrace()));
       }
       else if(e == null){
           System.out.println(formattedDate + "-- Expection :: \""+ errorMessage +"\" From Class -- \"" + className + "\" :: Method -- \""  + methodName+"\"");
       }
    }

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        return sdf.format(date);
    }

}
