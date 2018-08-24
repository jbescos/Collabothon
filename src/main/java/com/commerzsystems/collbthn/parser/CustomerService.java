package com.commerzsystems.collbthn.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.commerzsystems.collbthn.customer.Customer;
import com.commerzsystems.collbthn.customer.Invoice;
import com.commerzsystems.collbthn.service.ICathegorizer;
import com.commerzsystems.collbthn.service.MockCathegorizer;

@Service
public class CustomerService {

	private final static Logger log = LoggerFactory.getLogger(CustomerService.class);
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
            log.info("New customer {}", newCustomer);
        } else {
            userIdToProcess = getKeyByValue(idMap, new Customer(name, address));
            newCustomer = idMap.get(userIdToProcess);
            log.info("Existing customer {}", newCustomer);
        }
		if (idMap != null) {
			log.info("Map size {}", idMap.size());
			Set<Integer> keys = idMap.keySet();
			for (Integer i : keys) {
				log.info("Map names {}", idMap.get(i).getName());
			}

		}

        /////////////////////////////////////////////////////////////////


        //invoice info
		String totalAmount = "0.00";
		String invoiceNumber = "";
		String cathegory = "";
		boolean description = false;
		for (String string : input) {
			if (string.contains("€")) {
				totalAmount = string.substring(0, string.length() - 3);
			}
			if(string.contains("Nr.")){
				int positionStart = string.indexOf("Nr.");
		        int positionEnd = string.indexOf("Customer");
		        invoiceNumber  = string.substring(positionStart + 4, positionEnd - 1);
			}
			if (description) {
				String cathegoryLine = string;
				int euroPosition = cathegoryLine.indexOf("€");
				String cathegoryLineUntilEuroSign = cathegoryLine.substring(0, euroPosition - 2);
				cathegory = cathegoryLineUntilEuroSign.substring(2, cathegoryLineUntilEuroSign.lastIndexOf(" "));
				description = false;
			}
			if (string.contains("Pos")) {
				description = true;
			}
		}

        int totalAmountInt = currencyToBigDecimalFormat(totalAmount);

        Invoice newInvoice = new Invoice(Integer.valueOf(invoiceNumber), totalAmountInt, cathegory);
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
		newCustomer.setUpfrontFee(callTresholdForCustomerFromCoBa(newCustomer.getName(), newCustomer.getId()));
		
		if ((newCustomer.getUpfrontFee() - newCustomer.getTotalAmount()) <= 0) {
			newCustomer.setUpfrontFeeExceeded(true);
		} else {
			newCustomer.setUpfrontFeeExceeded(false);
		}

	}

	private int callTresholdForCustomer(int id) {

		if (id == 1) {
			return 20000;
		}
		if (id == 2) {
			return 100000;
		}
		if (id == 3) {
			return 2000;
		}
		if (id == 4) {
			return 30000;
		}
		if (id == 5) {
			return 12345;
		}
		return 0;

	}

	private int callTresholdForCustomerFromCoBa(String name, int id) {

		String accountId = name.replace(" ", "");

		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(500).setReadTimeout(500).build();
		final String uri = "https://api-sandbox.commerzbank.com/accounts-api/v1-s/accounts/" + accountId + "/balances";

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("keyId", "61ed9633-ae6a-4ba6-96fd-94f5398e9844");
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			if (result.getStatusCode().equals(HttpStatus.OK)) {
				String body = result.getBody();
				String amount = body.substring(body.lastIndexOf("amount"));
				int beginIndex = amount.indexOf(":");
				int endIndex = amount.indexOf(".");
				String balance = amount.substring(beginIndex + 1, endIndex);
				return Integer.parseInt(balance);
			}
		} catch (Exception e) {
			log.error(e.toString());
			return callTresholdForCustomer(id);
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

		// Replace all dots with commas
		if (currency.contains(".")) {
			currency = currency.replaceAll("\\.", "");
		}

		// Remove all commas
		if (currency.contains(",")) {
			currency = currency.substring(0, currency.indexOf(","));
		}
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
