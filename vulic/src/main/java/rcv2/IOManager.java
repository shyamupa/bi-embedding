package rcv2;//


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class IOManager {
    public IOManager() {
    }

    public static boolean isDirectoryExist(String dirPath) {
        File dir = new File(dirPath);
        return !dir.isDirectory()?false:dir.exists();
    }

    public static String[] listDirectory(String dirPath) {
        try {
            File e = new File(dirPath);
            String[] children = e.list();
            return children;
        } catch (Exception var3) {
            return null;
        }
    }

    public static boolean deleteDirectory(String dirPath) {
        File dir = new File(dirPath);
        if(dir.exists()) {
            File[] files = dir.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i].getAbsolutePath());
                } else {
                    files[i].delete();
                }
            }
        }

        return dir.delete();
    }

    public static BufferedReader openReader(String fname) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"));
            return reader;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean closeReader(BufferedReader reader) {
        try {
            reader.close();
            return true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static BufferedWriter openWriter(String fname) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), "UTF-8"));
            return writer;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean closeWriter(BufferedWriter writer) {
        try {
            writer.close();
            return true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static BufferedWriter openAppender(String fname) {
        try {
            BufferedWriter appender = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname, true), "UTF-8"));
            return appender;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean closeAppender(BufferedWriter appender) {
        try {
            appender.close();
            return true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean moveFile(String fileName, String directoryName) {
        File file = new File(fileName);
        File dir = new File(directoryName);
        File newFile = new File(dir, file.getName());
        if(isFileExist(newFile.getPath())) {
            deleteFile(newFile.getPath());
        }

        boolean success = file.renameTo(new File(dir, file.getName()));
        return success;
    }

    public static String readContent(String contentFileName) {
        BufferedReader reader = openReader(contentFileName);
        String content = "";

        try {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                content = content + line + " ";
            }

            content = content.trim();
            reader.close();
            return content;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> readLines(String fileName) {
        BufferedReader reader = openReader(fileName);
        ArrayList content = new ArrayList();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                content.add(line);
            }

            reader.close();
            return content;
        } catch (Exception var5) {
            var5.printStackTrace();
            System.out.println("Unable to read from file " + fileName);
            System.exit(1);
            return null;
        }
    }

    public static void writeLines(ArrayList<String> outputLines, String outputFile) {
        BufferedWriter writer = openWriter(outputFile);

        try {
            Iterator e = outputLines.iterator();

            while(e.hasNext()) {
                String line = (String)e.next();
                writer.write(line);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            System.out.println("Unable to write to file " + outputFile);
            System.exit(1);
        }

        closeWriter(writer);
    }

    public static void sleepingChild(int numSeconds) {
        try {
            Thread.sleep((long)(numSeconds * 1000));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return !file.isFile()?false:file.exists();
    }

    public static boolean deleteFile(String filePath) {
        boolean success = true;
        if(isFileExist(filePath)) {
            File file = new File(filePath);
            success = file.delete();
        }

        return success;
    }

    public static boolean createDirectory(String dirPath) {
        if(isDirectoryExist(dirPath)) {
            deleteDirectory(dirPath);
        }

        File dir = new File(dirPath);
        return dir.mkdir();
    }

    public static boolean createDirectoryNotDelete(String dirPath) {
        if(isDirectoryExist(dirPath)) {
            return true;
        } else {
            File dir = new File(dirPath);
            return dir.mkdir();
        }
    }

    public static String getFileExtension(String name) {
        int pos = name.lastIndexOf(46);
        return pos > 0 & pos < name.length() - 1?name.substring(pos + 1):"";
    }
}
