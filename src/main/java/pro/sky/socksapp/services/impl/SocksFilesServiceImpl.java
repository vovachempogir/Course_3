package pro.sky.socksapp.services.impl;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socksapp.services.SocksFilesService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SocksFilesServiceImpl implements SocksFilesService {
    @Value("${path.to.sock.file}")
    private String sockFilePath;

    @Value("${name.of.sock.file}")
    private String sockFileName;

    @Override
    public boolean saveToFile(String json) {
        try {
            cleanDataFile();
            Files.writeString(Path.of(sockFilePath, sockFileName), json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readFromFile() {
        try {
            return Files.readString(Path.of(sockFilePath, sockFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getDataFile() {
        return new File(sockFilePath + "/" + sockFileName);
    }

    @Override
    public boolean cleanDataFile() {
        try {
            Path path = Path.of(sockFilePath, sockFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean uploadDataFile(MultipartFile file) {
        cleanDataFile();
        File dataFile = getDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
