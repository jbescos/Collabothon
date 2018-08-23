package com.commerzsystems.collbthn.ctrl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.commerzsystems.collbthn.service.FileService;

@RestController
@RequestMapping("/files")
public class FileResource {

	private final Logger logger = LoggerFactory.getLogger(FileResource.class);
	private final FileService fileservice;
	
	@Autowired
	public FileResource(FileService fileservice) {
		this.fileservice = fileservice;
	}
	@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException {
    	fileservice.uploadFile(file, path);
        return "Uploaded "+file.getOriginalFilename();
    }
	
}
