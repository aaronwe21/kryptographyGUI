package commands;


import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentLoader {

    private Class clazz;
    private Object instance;
    private Object port;
    private String nameOfJavaArchive;
    private String pathToFile;
    private String nameOfClass = "CaesarCipher";

    public ComponentLoader(String algorithm){
        this.nameOfJavaArchive = algorithm+".jar";
        this.pathToFile = Configuration.instance.componentDirectory+Configuration.instance.fileSeparator+nameOfJavaArchive;
    }

    private void loadClazzFromJavaArchive() {
        try {
            URL[] urls = {new File(pathToFile).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls,ComponentLoader.class.getClassLoader());
            clazz = Class.forName(nameOfClass,true,urlClassLoader);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void provideInstanceOfClass() {
        try {
            instance = clazz.getMethod("getInstance",new Class[0]).invoke(null,new Object[0]);
        } catch (Exception e) {
        System.out.println(e.getMessage());
        }
    }

    private void provideComponentPort() {
        try {
            port = clazz.getDeclaredField("port").get(instance);
            }
        catch (Exception e) {
            e.printStackTrace();
        }
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
