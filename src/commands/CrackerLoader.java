package commands;

import java.io.File;
import java.lang.reflect.Method;

public class CrackerLoader extends ComponentLoader{

    public CrackerLoader(String cracker, String nameOfClass){
        super(cracker, nameOfClass);
    }

    public String executeDecryptMethod(String encryptedMessage){
        loadClazzFromJavaArchive();
        provideInstanceOfClass();
        provideComponentPort();
        try {
            Method method = port.getClass().getMethod("decrypt", String.class);
            String result = (String)method.invoke(port, encryptedMessage);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
