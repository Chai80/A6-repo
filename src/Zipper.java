import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Zipper {
    private Boolean initialized = false;

    private String filename;
    private String password;
    private String contents;

    public Zipper(String command, String contents) {
        String[] tokens = command.split(",");

        if (tokens.length != 3) {
            Logger.println("Expected 2 parameters. Received: " + (tokens.length - 1));
        } else {
            this.filename = tokens[1];
            this.password = tokens[2];
            this.contents = contents;

            initialized = true;
        }
    }

    public void create() {
        final String FOLDER = "output";

        if (!initialized) {
            return;
        }

        File directory = new File(FOLDER);

        if (!directory.exists()) {
            boolean success = directory.mkdir();

            if (!success) {
                System.err.println("Output folder could not be created.");

                return;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(FOLDER + "/" + filename + ".zip");

            ZipOutputStream zos = new ZipOutputStream(fos, password.toCharArray());

            ZipParameters parameters = new ZipParameters();

            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

            parameters.setFileNameInZip(filename + ".txt");

            zos.putNextEntry(parameters);

            zos.write(contents.getBytes(StandardCharsets.UTF_8));

            zos.closeEntry();

            zos.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
