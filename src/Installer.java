import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    private static final String ZipName = "Files.zip";
    
    Main() {
        InputStream is = getClass().getResourceAsStream(ZipName);
        ZipInputStream zis = new ZipInputStream(is, Charset.forName("CP866"));
        ZipEntry entry;

        try {
            while ((entry = zis.getNextEntry()) != null) {
                FileOutputStream stream = new FileOutputStream(System.getProperty("user.dir") + "/" + entry.getName());
                for (int i = zis.read(); i != -1; i = zis.read()) stream.write(i);
                stream.flush();
                zis.closeEntry();
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { new Main(); }
}
