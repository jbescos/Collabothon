package com.commerzsystems.collbthn.customer;

public class Invoice {

    public Invoice(String cathegory, int invoiceNumber, String amount) {
        this.cathegory = cathegory;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
    }

    public Invoice(int invoiceNumber, String amount) {
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Invoice) {
            Invoice invoice = (Invoice) obj;
            return this.getInvoiceNumber() == invoice.getInvoiceNumber();
        }
        return false;
    }

    private String cathegory;

    private int invoiceNumber;

    private String amount;

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void getMoneyInteger() {

    }

}
