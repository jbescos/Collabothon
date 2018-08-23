package com.commerzsystems.collbthn.parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TextParser {

    private final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private final static AtomicInteger idCounter = new AtomicInteger(0);
    private static HashMap<String, Integer> idMap = new HashMap<>();

    public void parse(String input) {

        System.out.println(input);

        String[] lines = input.split("\r\n|\r|\n");

        processInfoAboutCustomer(lines);

        System.out.println();

    }

    //get the data about customer
    //create text file with invoiceNumber
    private void processInfoAboutCustomer(String[] input) {
        String name = input[3];

        //create folder for a customer
        int id = checkId(name);

        String address = input[4];
        String totalAmount = input[22];
        String invoice = input[14];

        int positionStart = invoice.indexOf("Nr.");
        int positionEnd = invoice.indexOf("Customer");
        String invoiceNumber = invoice.substring(positionStart + 4, positionEnd - 1);

        String cathegoryLine = input[16];
        int euroPosition = cathegoryLine.indexOf("€");
        String cathegoryLineUntilEuroSign = cathegoryLine.substring(0, euroPosition - 2);
        String cathegory = cathegoryLineUntilEuroSign.substring(2, cathegoryLineUntilEuroSign.lastIndexOf(" "));

        //insert the method here to DEFINE THE CATHEGORY HERE
        boolean mortgage = false;

        //if .txt wasn't created for that invoce then create it
        File theDir = new File(TEMP_DIR + id + "/" + invoiceNumber);
        if(!theDir.exists()) {
            createTXT(id, invoiceNumber, name, address, totalAmount, cathegory, mortgage);
        }


    }

    //assign a user a unique id and create folder for that customer
    private int checkId(String name) {
        if(idMap.containsKey(name)) {
            return idMap.get(name);
        } else {
            idMap.put(name, idCounter.incrementAndGet());
            createFolder(idMap.get(name));
            return idMap.get(name);
        }
    }

    //create a folder for a customer
    private void createFolder(int id) {
        try {
            Files.createDirectories(Paths.get(TEMP_DIR + "customers/" + id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create .txt for invoice payment
    private void createTXT(int id, String invoiceNumber, String name, String address, String totalAmount, String cathegory, boolean mortgage) {

        Path file = Paths.get(TEMP_DIR +"customers/" + id + "/" + invoiceNumber + ".txt");
        String[] infoToWrite = new String[6];
        infoToWrite[0] = String.valueOf(id);
        infoToWrite[1] = name;
        infoToWrite[2] = address;
        infoToWrite[3] = String.valueOf(invoiceNumber);
        infoToWrite[4] = cathegory;
        infoToWrite[5] = String.valueOf(totalAmount);

        try {
            Files.write(file, Arrays.asList(infoToWrite), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
