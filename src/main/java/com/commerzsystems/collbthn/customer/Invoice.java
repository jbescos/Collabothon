package com.commerzsystems.collbthn.customer;

public class Invoice {

	private boolean mortage;

	private int invoiceNumber;

	private String amount;

	public Invoice(boolean mortage, int invoiceNumber, String amount) {
		this.setMortage(mortage);
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

	public boolean isMortage() {
		return mortage;
	}

	public void setMortage(boolean mortage) {
		this.mortage = mortage;
	}

}
