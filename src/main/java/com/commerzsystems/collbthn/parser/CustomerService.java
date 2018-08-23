package com.commerzsystems.collbthn.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.commerzsystems.collbthn.customer.Customer;
import com.commerzsystems.collbthn.customer.Invoice;

@Service
public class CustomerService {

    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<Integer, Customer> idMap = new ConcurrentHashMap<>();

    public void parse(String input) throws Exception {
        String[] lines = input.split("\r\n|\r|\n");
        processInfoAboutCustomer(lines);
    }

    private void processInfoAboutCustomer(String[] input) throws Exception {

        String name = input[3];
        String address = input[4];

        int userIdToProcess = 0;
        Customer newCustomer = null;

        if(!idMap.containsValue(new Customer(name, address))) {
            userIdToProcess = idCounter.incrementAndGet();
            newCustomer = new Customer(userIdToProcess, name, address);
            idMap.put(userIdToProcess, newCustomer);
        } else {
            userIdToProcess = getKeyByValue(idMap, new Customer(name, address));
            newCustomer = idMap.get(userIdToProcess);
        }

        /////////////////////////////////////////////////////////////////


        //invoice info
        String totalAmount = input[22].substring(0, input[22].length() -3);

        int totalAmountInt = currencyToBigDecimalFormat(totalAmount);



        String invoice = input[14];
        int positionStart = invoice.indexOf("Nr.");
        int positionEnd = invoice.indexOf("Customer");
        String invoiceNumber = invoice.substring(positionStart + 4, positionEnd - 1);

        Invoice newInvoice = new Invoice(Integer.valueOf(invoiceNumber), totalAmount);

        String cathegoryLine = input[16];
        int euroPosition = cathegoryLine.indexOf("€");
        String cathegoryLineUntilEuroSign = cathegoryLine.substring(0, euroPosition - 2);
        String cathegory = cathegoryLineUntilEuroSign.substring(2, cathegoryLineUntilEuroSign.lastIndexOf(" "));
        /////////////////////////////////////////////////////////////////

        //insert the method here to DEFINE THE CATHEGORY HERE
        boolean mortgage = false;

        if (!newCustomer.getInvoices().contains(newInvoice)){
            newCustomer.addInvoice(newInvoice);
            newCustomer.proceedAmount(totalAmountInt);
        }

    }

    public static <T, E> int getKeyByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        for (Object id : keys) {
            return Integer.parseInt(id.toString());
        }
        return 0;
    }


    public static int currencyToBigDecimalFormat(String currency) throws Exception {

        if(!doesMatch(currency,"^[+-]?[0-9]{1,3}(?:[0-9]*(?:[.,][0-9]{0,2})?|(?:,[0-9]{3})*(?:\\.[0-9]{0,2})?|(?:\\.[0-9]{3})*(?:,[0-9]{0,2})?)$"))
            throw new Exception("Currency in wrong format " + currency);

        // Replace all dots with commas
        currency = currency.replaceAll("\\.", ",");

        // If fractions exist, the separator must be a .
        if(currency.length()>=3) {
            char[] chars = currency.toCharArray();
            if(chars[chars.length-2] == ',') {
                chars[chars.length-2] = '.';
            } else if(chars[chars.length-3] == ',') {
                chars[chars.length-3] = '.';
            }
            currency = new String(chars);
        }

        // Remove all commas
        currency = currency.replaceAll(",", "");
        currency = currency.substring(0, currency.indexOf("."));
        return Integer.parseInt(currency);
    }

    public static boolean doesMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }



}
