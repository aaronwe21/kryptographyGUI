package persistence;

import configuration.Configuration;

import java.io.*;

public class Log {
    private File logfile;

    public Log(LogOperationType type, String algorithm){
        long unixSeconds = System.currentTimeMillis() / 1000L;
        String filename = type.toString() + "_"+ algorithm +"_"+unixSeconds+".txt";
        String filepath = Configuration.instance.logDirectory+Configuration.instance.fileSeparator+filename;

        File logDirectory = new File(Configuration.instance.logDirectory);
        if(!logDirectory.isDirectory()){
            logDirectory.mkdir();
        }
        this.logfile = new File(filepath);

        try{
            logfile.createNewFile();
            System.out.println("Logfile with filename: "+filename+" created.");
        }
        catch (IOException e){
            System.out.println("Logfile could not be created!");
            System.out.println(e);
        }
    }

    public void addLineToLog(String logMessage){
        try {
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logfile, true));
            logWriter.write(logMessage);
            logWriter.newLine();
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNewestLogText(){
        File newestLogFile = getNewestLogFile();

        if (newestLogFile != null){
            String logText = "";
            try {
                BufferedReader logReader = new BufferedReader(new FileReader(newestLogFile));
                String line;
                while ((line = logReader.readLine()) != null){
                    logText+=line;
                    logText+= "\n";
                }
                logReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return logText;
        }

        return "--No log file found--";
    }

    private static File getNewestLogFile() {
        File logDirectory = new File(Configuration.instance.logDirectory);
        File newestFile = null;
        if (logDirectory.isDirectory()){
            File[] logFiles = logDirectory.listFiles();

            if (logFiles.length > 0){
                Long currentHighestUnixSeconds = 0L;
                for (int i =0; i< logFiles.length; i++){
                    String[] splittedFilename = logFiles[i].getName().split("_");
                    Long unixSeconds = Long.parseLong(splittedFilename[splittedFilename.length -1].replace(".txt", ""));
                    if (unixSeconds>currentHighestUnixSeconds){
                        newestFile = logFiles[i];
                    }
                }
            }
        }
        else{
            logDirectory.mkdir();
        }
        return newestFile;
    }
}
