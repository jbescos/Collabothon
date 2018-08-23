package com.commerzsystems.collbthn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileService {

	private final Logger logger = LoggerFactory.getLogger(FileService.class);
	private final int FILEBUFFERSIZE = 102400000; // 100 MB
	
	public void uploadFile(MultipartFile file, String rootLocation) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file " + filename);
        }
        logger.debug("Uploading file {}", filename);
        byte[] bytes = new byte[FILEBUFFERSIZE];
    	int bytesRead;
        File newFile = Paths.get(rootLocation, filename).toFile();
        InputStream input = file.getInputStream();
        try(OutputStream output = new FileOutputStream(newFile)){
        	while ((bytesRead = input.read(bytes)) != -1) {
        		output.write(bytes, 0, bytesRead);
        		output.flush();
        		logger.debug("Writting {} bytes", bytesRead);
        	}
        }
        logger.debug("Uploaded file in {}", newFile.getAbsolutePath());
    }
	
}
