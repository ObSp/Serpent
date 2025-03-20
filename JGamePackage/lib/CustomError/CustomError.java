package JGamePackage.lib.CustomError;

public class CustomError {
    private String message;
    private int errorType;
    private String ansiColor;
    private String[] jumpOverElementsContaining;

    public static final int ERROR = 0;
    public static final int WARNING = 1;

    /** Constructs a CustomError based on the given parameters.
     * Note that any occurences of the regex "%s" will be replaced with the corresponding 
     * insertions when this.Throw(String[]) is called.
     * 
     * @param errorMessage
     * @param errorType
     * @param stackTraceElementBlacklist
     */
    public CustomError(String errorMessage, int errorType, String stackTraceElementBlacklist) {
        this(errorMessage, errorType, new String[] {stackTraceElementBlacklist});
    }

    /** Constructs a CustomError based on the given parameters.
     * Note that any occurences of the regex "%s" will be replaced with the corresponding 
     * insertions when this.Throw(String[]) is called.
     * 
     * @param errorMessage
     * @param errorType
     * @param stackTraceElementBlacklist
     */
    public CustomError(String errorMessage, int errorType, String[] stackTraceElementBlacklist) {
        this.message = errorMessage;
        this.errorType = errorType;
        this.jumpOverElementsContaining = stackTraceElementBlacklist;

        if (this.errorType == CustomError.ERROR) {
            ansiColor = "\u001B[31m";
        } else if (this.errorType == CustomError.WARNING) {
            ansiColor = "\033[38;2;255;180;0m";
        }
    }

    public void Throw(String[] insertions) {
        for (String s : insertions) {
            message = message.replaceFirst("%s", s);
        }

        System.out.println((char) 27 +"[4m"+ ansiColor+message+"\033[0m");
        System.out.print(ansiColor);
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            boolean cont = false;
            for (String s : jumpOverElementsContaining) {
                if (stackTrace[i].toString().contains(s)) {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;

            System.out.println("    at "+stackTrace[i]);
        }
        System.out.print("\033[0m");
        if (errorType == ERROR) {
            System.exit(0);
        }
    }

    public void Throw(){
        this.Throw(new String[0]);
    }

    public void Throw(String s) {
        this.Throw(new String[] {s});
    }
}
