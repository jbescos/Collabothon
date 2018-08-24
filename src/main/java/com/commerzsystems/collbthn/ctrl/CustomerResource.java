package com.commerzsystems.collbthn.ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerzsystems.collbthn.customer.Customer;
import com.commerzsystems.collbthn.customer.Invoice;
import com.commerzsystems.collbthn.parser.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerResource {
	
	private final CustomerService cs;
	private final static Logger log = LoggerFactory.getLogger(CustomerResource.class);
	
	@Autowired
	public CustomerResource(CustomerService cs) {
		this.cs = cs;
	}

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public ResponseEntity<Map<Integer, Customer>> list(@RequestParam(value="customer", required=false) String customer) throws IOException {
		if(customer == null) {
			return ResponseEntity.ok(cs.getCustomers());
		}else {
			Map<Integer, Customer> customers = new HashMap<>();
			log.info("Loading for {}", customer);
			for(Entry<Integer, Customer> entry : cs.getCustomers().entrySet()) {
				log.info("Check name {}", entry.getValue().getName());
				if(customer.equalsIgnoreCase(entry.getValue().getName())) {
					customers.put(entry.getKey(), new Customer("Coba", ""));
					customers.put(-1, new Customer("ING", ""));
					customers.put(-2, new Customer("mBank", ""));
				}
			}
			return ResponseEntity.ok(customers);
		}
	}
	
	@RequestMapping(path = "/invoices", method = RequestMethod.GET)
	public ResponseEntity<List<Invoice>> invoices(@RequestParam(value="id", required=true) int id) throws IOException {
		return ResponseEntity.ok(cs.getCustomers().get(id).getInvoices());
	}
	
}
