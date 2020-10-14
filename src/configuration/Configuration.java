package configuration;

import gui.GUI;

public enum Configuration {
    instance;

    // common
    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");

    // database
    public final String dataDirectory = userDirectory + fileSeparator + "hsqldb" + fileSeparator;
    public final String databaseFile = dataDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    public GUI gui;

    // component
    public String componentDirectory = userDirectory + fileSeparator + "component";
    public final int secondsToCrack = 30;

    // log
    public String logDirectory = userDirectory + fileSeparator + "log";

    // key
    public String keyDirectory = userDirectory + fileSeparator + "key";

    // debug-mode
    private boolean debugModeActive = false;

    public void changeDebugMode(){
        debugModeActive = !debugModeActive;
    }

    public boolean getDebugModeActive(){
        return debugModeActive;
    }


}