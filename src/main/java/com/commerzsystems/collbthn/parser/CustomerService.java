package com.commerzsystems.collbthn.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerzsystems.collbthn.customer.Customer;
import com.commerzsystems.collbthn.customer.Invoice;
import com.commerzsystems.collbthn.service.ICathegorizer;
import com.commerzsystems.collbthn.service.MockCathegorizer;

@Service
public class CustomerService {

    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<Integer, Customer> idMap = new ConcurrentHashMap<>();
    private final ICathegorizer cathegorizer;
    
    @Autowired
    public CustomerService(MockCathegorizer cathegorizer) {
    	this.cathegorizer = cathegorizer;
    }

	public Customer parse(String input) throws Exception {
        String[] lines = input.split("\r\n|\r|\n");
		return processInfoAboutCustomer(lines);
    }

	private Customer processInfoAboutCustomer(String[] input) throws Exception {

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
		String totalAmount = "0.00";
		for (String string : input) {
			if (string.contains("€")) {
				totalAmount = string.substring(0, input[22].length() - 3);
			}
		}

        int totalAmountInt = currencyToBigDecimalFormat(totalAmount);

        String invoice = input[14];
        int positionStart = invoice.indexOf("Nr.");
        int positionEnd = invoice.indexOf("Customer");
        String invoiceNumber = invoice.substring(positionStart + 4, positionEnd - 1);

		Invoice newInvoice = new Invoice(Integer.valueOf(invoiceNumber), totalAmountInt);

        String cathegoryLine = input[16];
        int euroPosition = cathegoryLine.indexOf("€");
        String cathegoryLineUntilEuroSign = cathegoryLine.substring(0, euroPosition - 2);
        String cathegory = cathegoryLineUntilEuroSign.substring(2, cathegoryLineUntilEuroSign.lastIndexOf(" "));
        /////////////////////////////////////////////////////////////////
		cathegory = cathegorizer.categorize(cathegory);
        //insert the method here to DEFINE THE CATHEGORY HERE
        boolean mortgage = cathegory.equals("mortgage");

        if (!newCustomer.getInvoices().contains(newInvoice)){
			newInvoice.setMortage(mortgage);
            newCustomer.addInvoice(newInvoice);
			newCustomer.proceedAmount(newInvoice);
        }

		informBank(newCustomer);

		return newCustomer;

    }

	private void informBank(Customer newCustomer) {
		if ((callTresholdForCustomer(newCustomer.getId()) - newCustomer.getTotalAmount()) <= 0) {
			newCustomer.setUpfrontFeeExceeded(true);
		}
		newCustomer.setUpfrontFeeExceeded(false);

	}

	private int callTresholdForCustomer(int id) {
		if (id == 0 && id == 1) {
			return 2000;
		}
		if (id == 2) {
			return 100000;
		}
		return 0;

	}

	public Customer getCustomer(String name) {
		return idMap.get(name);
	}
	
	public Map<Integer, Customer> getCustomers(){
		return idMap;
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
