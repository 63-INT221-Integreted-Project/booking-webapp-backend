package sit.int221.bookingproj.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.bookingproj.entities.File;
import sit.int221.bookingproj.repositories.FileRepository;
import sit.int221.bookingproj.services.FileService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class FileController {
    @Autowired
    public FileService fileService;

    @Autowired
    public FileRepository fileRepository;

    @PostMapping("/api/uploadFile")
    public File uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String fileCode = fileService.uploadFile(fileName, multipartFile);
        File file = new File();
        file.setFileName(fileName);
        file.setFileSize(Long.toString(size));
        file.setFilePath("/downloadFile/" + fileCode);
        return fileRepository.saveAndFlush(file);
    }

    @GetMapping("/api/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode){
        Resource resource = null;
        try{
            resource = fileService.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if(resource == null){
            return new ResponseEntity<>("File not found" , HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

}
