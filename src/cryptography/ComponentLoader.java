package cryptography;


import configuration.Configuration;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentLoader {

    protected Class clazz;
    protected Object instance;
    protected Object port;
    protected String nameOfJavaArchive;
    protected String pathToFile;
    protected String nameOfClass;

    public ComponentLoader(String nameOfJavaArchive, String nameOfClass) {
        this.nameOfJavaArchive = nameOfJavaArchive + ".jar";
        this.pathToFile = Configuration.instance.componentDirectory + Configuration.instance.fileSeparator + this.nameOfJavaArchive;
        this.nameOfClass = nameOfClass;
    }

    protected void loadClazzFromJavaArchive() {
        try {
            URL[] urls = {new File(pathToFile).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, ComponentLoader.class.getClassLoader());
            clazz = Class.forName(nameOfClass, true, urlClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void provideInstanceOfClass() {
        try {
            instance = clazz.getMethod("getInstance", new Class[0]).invoke(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void provideComponentPort() {
        try {
            port = clazz.getDeclaredField("port").get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
