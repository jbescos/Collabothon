package com.commerzsystems.collbthn.customer;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    public Customer(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    private int totalAmount = 0;

    public void proceedAmount(int lastInvoice) {
        totalAmount += lastInvoice;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Customer) {
            Customer customer = (Customer) obj;
            return this.getName().equals(customer.getName()) && this.getAddress().equals(customer.getAddress());
        }
        return false;
    }

    private List<Invoice> invoices = new ArrayList<>();

    private int id;

    private String name;

    private String address;

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
