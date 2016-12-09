import java.lang.reflect.Method;
//*** GENERIC JAVA VERSION ***//

/**
 * utils.L is a class that helps you log your application better.
 * By using static method: "log" the displayed message will be in the format:
 * [Thread] : [APPLICATION_NAME] (@line) CLASS_NAME : METHOD_NAME(PARAMETERS)
 * utils.L also provide a clean way to use a toast, just pass the context
 * <br><br>
 * You can turn the logs off by calling the static method 'setActive(false)'
 * If 'active' is false - no logs will appear
 * Default value is true
 * */
public final class L {
    private static final String APPLICATION_TAG = "MyApplication"; // Change this in accordance to your app's name
    private static boolean active = true;

    /** You are not to create any instances from utils.L class*/
    private L () {
    }

    /**
     * Prints to log with no message
     * */
    public static void log() {
        print(null);
    }

    /**
     * Prints to log the message given
     * @param msg The message to be printed to the log
     * */
    public static void log(String msg) {
        print(msg);
    }

    /**
     * Checks if active is true or false.<br>
     * If true - logs will appear
     * if false - logs will NOT appear
     * */
    public static boolean isActive() {
        return active;
    }

    /**
     * Sets the active value to true/false
     * @param active true - logs will appear
     *               false - logs will not appear<br>
     *
     * */
    public static void setActive(boolean active) {
        L.active = active;
    }


    private static void print(String str) {
        int depth = 3; // 4 = Takes the method which called this class's log function, change this if you know what you're doing
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StackTraceElement current = ste[depth];

        int lineNumber = current.getLineNumber();
        String className = keepEndToFirstDot(current.getClassName());
        String methodName = current.getMethodName();

        Class<?> callingClass;
        String parameters = "";
        try {
            callingClass = Class.forName(current.getClassName());

            Method[] methods = callingClass.getMethods();
            Method calledMethod = null;
            for (Method i : methods) {
                if (i.getName().equals(methodName)){
                    calledMethod = i;
                    break;
                }
            }
            if (calledMethod != null) {
                Class<?> [] parameterTypes = calledMethod.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters = parameters + parameterTypes[i].getSimpleName();
                    if (i == parameterTypes.length-1){
                        parameters = parameters + "";
                    } else {
                        parameters = parameters + ", ";
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (isActive()) { // Will only print if active is set to true
            String message = "[Thread: " + Thread.currentThread().getName() + "]: [" + APPLICATION_TAG +
                    "] (@" + lineNumber + ") " + className + ": " + methodName + "(" + parameters + ")";
            if (str != null) {
                System.out.println(message + ": " + str);
            } else {
                System.out.println(message);
            }
        }
    }

    private static String keepEndToFirstDot(String str) {
        for (int i = str.length()-1; i >= 0; i--) {
            if (str.charAt(i) == '.') {
                str = str.substring(i+1);
                break;
            }
        }
        return str;
    }
}