package com.commerzsystems.collbthn.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.commerzsystems.collbthn.parser.TextParser;

@RestController
@RequestMapping("/files")
public class FileResource {

	private final Logger logger = LoggerFactory.getLogger(FileResource.class);

	private TextParser textParser = new TextParser();

	@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile multiPartFile, @RequestParam("path") String path) throws IOException {

		File file = convert(multiPartFile);

		PDDocument document = PDDocument.load(file);

		System.out.println("PDF loaded");

		//Adding a blank page to the document
		document.addPage(new PDPage());

		PDFTextStripper ts = new PDFTextStripper();

		String str = ts.getText(document);
		//Closing the document

        textParser.parse(str);

		document.close();

		return null;
    }

	private File convert(MultipartFile file) throws IOException {
		File convFile = null;
		FileOutputStream fos = null;
		try {
			convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();
			fos = new FileOutputStream(convFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
	public String getCustomer(@RequestParam("name") String name) {
		textParser.getCustomerEntry(name);

		return name;
	}

}
