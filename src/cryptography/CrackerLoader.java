package cryptography;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class CrackerLoader extends ComponentLoader {

    public CrackerLoader(String algorithm, String nameOfClass) {
        super((algorithm + "_cracker"), nameOfClass);
    }

    public String executeDecryptMethod(String encryptedMessage, File publicKeyFile) {
        loadClazzFromJavaArchive();
        provideInstanceOfClass();
        provideComponentPort();
        try {
            Method method = port.getClass().getMethod("decrypt", String.class, File.class);
            String result = (String) method.invoke(port, encryptedMessage, publicKeyFile);
            return result;

        } catch (Exception e) {
            return null;
        }
    }

    public String executeDecryptMethod(String encryptedMessage) {
        loadClazzFromJavaArchive();
        provideInstanceOfClass();
        provideComponentPort();
        try {
            Method method = port.getClass().getMethod("decrypt", String.class);
            String result = (String) method.invoke(port, encryptedMessage);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static Callable<String> getCrackerCallable(String algorithm, String nameOfClass, String encryptedMessage) {
        return new Callable<String>() {
            public String call() throws Exception {
                return new CrackerLoader(algorithm, nameOfClass).executeDecryptMethod(encryptedMessage);
            }
        };
    }

    public static Callable<String> getCrackerCallable(String algorithm, String nameOfClass, String encryptedMessage, File publicKeyFile) {
        return new Callable<String>() {
            public String call() throws Exception {
                return new CrackerLoader(algorithm, nameOfClass).executeDecryptMethod(encryptedMessage, publicKeyFile);
            }
        };
    }
}
