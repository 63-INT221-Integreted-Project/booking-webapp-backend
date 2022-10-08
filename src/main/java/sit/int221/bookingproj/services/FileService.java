package sit.int221.bookingproj.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;

@Service
public class FileService {
    private Path foundFile;
    public String uploadFile(String fileName, MultipartFile multipartFile) throws IOException {
        String fileCode = RandomStringUtils.randomAlphabetic(8);
        Path uploadDirectory = Paths.get("files");
        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadDirectory.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex){
            throw new IOException("Error saving uploading file " + fileName, ex);
        }
        return fileCode;
    }

    public Resource getFileAsResource(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("files");
        Files.list(uploadDirectory).forEach(file -> {
            if(file.getFileName().toString().startsWith(fileCode)){
                foundFile = file;
                return;
            }
        });
        if(foundFile != null){
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}
