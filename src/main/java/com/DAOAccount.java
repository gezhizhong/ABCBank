package main.java.com;

import java.util.Date;

public class DAOAccount {
	
	private String accountID;
	private String name;
	private boolean isOpenCheckingAccount;
	private double balanceOfCheckingAccount;
	private Date lastCalculateChecking;
	private double interestOfCheckingAccount;
	private boolean isOpenSavingsAccount;
	private double balanceOfSavingsAccount;
	private Date lastCalculateSavings;
	private double interestOfSavingsAccount;
	private boolean isOpenMaxiSavingsAccount;
	private double balanceOfMaxiSavingsAccount;
	private Date lastCalculateMaxiSavings;
	private double interestOfMaxiSavingsAccount;

	protected String getAccountID() {
		return accountID;
	}

	protected void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected boolean isOpenCheckingAccount() {
		return isOpenCheckingAccount;
	}

	protected void setOpenCheckingAccount(boolean isOpenCheckingAccount) {
		this.isOpenCheckingAccount = isOpenCheckingAccount;
	}

	protected double getBalanceOfCheckingAccount() {
		return balanceOfCheckingAccount + interestOfCheckingAccount;
	}

	protected void setBalanceOfCheckingAccount(double balanceOfCheckingAccount) {
		this.balanceOfCheckingAccount = balanceOfCheckingAccount;
	}

	protected Date getLastCalculateChecking() {
		return lastCalculateChecking;
	}

	protected void setLastCalculateChecking(Date lastCalculateChecking) {
		this.lastCalculateChecking = lastCalculateChecking;
	}

	protected double getInterestOfCheckingAccount() {
		return interestOfCheckingAccount;
	}

	protected void setInterestOfCheckingAccount(double interestOfCheckingAccount) {
		this.interestOfCheckingAccount = this.interestOfCheckingAccount
				+ interestOfCheckingAccount;
	}

	protected boolean isOpenSavingsAccount() {
		return isOpenSavingsAccount;
	}

	protected void setOpenSavingsAccount(boolean isOpenSavingsAccount) {
		this.isOpenSavingsAccount = isOpenSavingsAccount;
	}

	protected double getBalanceOfSavingsAccount() {
		return balanceOfSavingsAccount + interestOfSavingsAccount;
	}

	protected void setBalanceOfSavingsAccount(double balanceOfSavingsAccount) {
		this.balanceOfSavingsAccount = balanceOfSavingsAccount;
	}

	protected Date getLastCalculateSavings() {
		return lastCalculateSavings;
	}

	protected void setLastCalculateSavings(Date lastCalculateSavings) {
		this.lastCalculateSavings = lastCalculateSavings;
	}

	protected double getInterestOfSavingsAccount() {
		return interestOfSavingsAccount;
	}

	protected void setInterestOfSavingsAccount(double interestOfSavingsAccount) {
		this.interestOfSavingsAccount = this.interestOfSavingsAccount
				+ interestOfSavingsAccount;
	}

	protected boolean isOpenMaxiSavingsAccount() {
		return isOpenMaxiSavingsAccount;
	}

	protected void setOpenMaxiSavingsAccount(boolean isOpenMaxiSavingsAccount) {
		this.isOpenMaxiSavingsAccount = isOpenMaxiSavingsAccount;
	}

	protected double getBalanceOfMaxiSavingsAccount() {
		return balanceOfMaxiSavingsAccount + interestOfMaxiSavingsAccount;
	}

	protected void setBalanceOfMaxiSavingsAccount(
			double balanceOfMaxiSavingsAccount) {
		this.balanceOfMaxiSavingsAccount = balanceOfMaxiSavingsAccount;
	}

	protected Date getLastCalculateMaxiSavings() {
		return lastCalculateMaxiSavings;
	}

	protected void setLastCalculateMaxiSavings(Date lastCalculateMaxiSavings) {
		this.lastCalculateMaxiSavings = lastCalculateMaxiSavings;
	}

	protected double getInterestOfMaxiSavingsAccount() {
		return interestOfMaxiSavingsAccount;
	}

	protected void setInterestOfMaxiSavingsAccount(
			double interestOfMaxiSavingsAccount) {
		this.interestOfMaxiSavingsAccount = this.interestOfMaxiSavingsAccount
				+ interestOfMaxiSavingsAccount;
	}
}
