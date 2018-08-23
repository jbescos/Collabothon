package com.commerzsystems.collbthn.ctrl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
	
	@Autowired
	public CustomerResource(CustomerService cs) {
		this.cs = cs;
	}

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public ResponseEntity<Map<Integer, Customer>> list() throws IOException {
		return ResponseEntity.ok(cs.getCustomers());
	}
	
	@RequestMapping(path = "/invoices", method = RequestMethod.GET)
	public ResponseEntity<List<Invoice>> list(@RequestParam(value="invoices", required=true) int id) throws IOException {
		return ResponseEntity.ok(cs.getCustomers().get(id).getInvoices());
	}
	
}
