package com.commerzsystems.collbthn.customer;

public class Invoice {

	private boolean mortage;

	private int invoiceNumber;

	private int amount;
	
	private String cathegory;

	public Invoice(boolean mortage, int invoiceNumber, int amount) {
		this.setMortage(mortage);
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
    }

	public Invoice(int invoiceNumber, int amount, String cathegory) {
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.cathegory = cathegory;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Invoice) {
            Invoice invoice = (Invoice) obj;
            return this.getInvoiceNumber() == invoice.getInvoiceNumber();
        }
        return false;
    }

	@Override
	public String toString() {
		return "Invoice [mortage=" + mortage + ", invoiceNumber=" + invoiceNumber + ", amount=" + amount
				+ ", cathegory=" + cathegory + "]";
	}

	public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

	public int getAmount() {
        return amount;
    }

	public void setAmount(int amount) {
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

	public String getCathegory() {
		return cathegory;
	}

	public void setCathegory(String cathegory) {
		this.cathegory = cathegory;
	}

}
