package cryptography;

import java.io.File;
import java.lang.reflect.Method;

public class AlgorithmLoader extends ComponentLoader {

    public AlgorithmLoader(String algorithm, String nameOfClass){
        super(algorithm, nameOfClass);
    }

    public String executeEncryptMethod(String plainMessage, File keyFile){
        loadClazzFromJavaArchive();
        provideInstanceOfClass();
        provideComponentPort();
        try {
            Method method = port.getClass().getMethod("encrypt", String.class, File.class);
            String result = (String)method.invoke(port, plainMessage, keyFile);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String executeDecryptMethod(String encryptedMessage, File keyFile){
        loadClazzFromJavaArchive();
        provideInstanceOfClass();
        provideComponentPort();
        try {
            Method method = port.getClass().getMethod("decrypt", String.class, File.class);
            String result = (String)method.invoke(port, encryptedMessage, keyFile);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
