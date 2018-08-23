package com.commerzsystems.collbthn.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.commerzsystems.collbthn.customer.Customer;
import com.commerzsystems.collbthn.parser.CustomerService;
import com.commerzsystems.collbthn.utils.ZipUtils;

@RestController
@RequestMapping("/files")
public class FileResource {

	private final Logger logger = LoggerFactory.getLogger(FileResource.class);

	private final CustomerService cs;
	
	@Autowired
	public FileResource(CustomerService cs) {
		this.cs = cs;
	}

	@PostMapping("/upload")
	public Customer handleFileUpload(@RequestParam("file") MultipartFile multiPartFile,
			@RequestParam("path") String path) throws Exception {

		List<File> fileList = new ArrayList<File>();
		File file = null;

		// need an array of files
		if (multiPartFile.getOriginalFilename().contains(".zip")) {
			ZipUtils.unzipToFileArray(multiPartFile.getInputStream(), fileList);
		} else {
			file = convert(multiPartFile);
			fileList.add(file);
		}

		Customer customer = null;
		for (File fileElem : fileList) {
			PDDocument document = PDDocument.load(fileElem);
			PDFTextStripper ts = new PDFTextStripper();
			String str = ts.getText(document);
			customer = cs.parse(str);
			document.close();
		}

		return customer;
    }

	private File convert(MultipartFile file) throws IOException {
		File convFile = null;
		FileOutputStream fos = null;
		try {
			convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();
			fos = new FileOutputStream(convFile);
		} catch (FileNotFoundException e) {
			logger.error("Error", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	@RequestMapping("/test")
	String home() {
		return "Hello World!";
	}
	

	@PostMapping("/bankview")
	public Customer getCustomer(@RequestParam("name") String name) {
		return cs.getCustomer(name);
	}

}
