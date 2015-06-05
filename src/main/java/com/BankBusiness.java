package main.java.com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


public class BankBusiness {
	/************************* VO ****************************/
	// Open an account
	public void openAccount(DAOAccount daoAccount, String accountID,
			String name, boolean isChecking, double balOfChecking,
			boolean isSavings, double balOfSavings, boolean isMaxiSavings,
			double balOfMaxi) {

		daoAccount.setAccountID(accountID);
		daoAccount.setName(name);
		daoAccount.setOpenCheckingAccount(isChecking);
		daoAccount.setBalanceOfCheckingAccount(balOfChecking);
		daoAccount.setOpenSavingsAccount(isSavings);
		daoAccount.setBalanceOfSavingsAccount(balOfSavings);
		daoAccount.setOpenMaxiSavingsAccount(isMaxiSavings);
		daoAccount.setBalanceOfMaxiSavingsAccount(balOfMaxi);
		daoAccount.setInterestOfCheckingAccount(0.0);
		daoAccount.setLastCalculateChecking(getCurrentTime());
		daoAccount.setInterestOfSavingsAccount(0.0);
		daoAccount.setLastCalculateSavings(getCurrentTime());
		daoAccount.setInterestOfMaxiSavingsAccount(0.0);
		daoAccount.setLastCalculateMaxiSavings(getCurrentTime());
	}

	// Withdraw
	public synchronized void withdraw(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht, int type,
			double cash) {

		double balance = getValidAccountBalance(daoAccount, ht, type);
		if (balance != -1) {
			balance = payBalance(balance, cash);
			if (balance != -1) {
				setValidAccountBalance(daoAccount, type, balance);
				saveTransaction(daoAccount, ht, getCurrentTime(), " Withdraw ",
						"$" + cash + " " + getAccountName(type) + " Total: $"
								+ balance);
			} else {
				System.out.println("ERROR>>Your saving isn't enough.");
			}
		} else {
			System.out
					.println("ERROR>>the account type which you want to withdraw doesn't open.");
		}

	}

	// Deposit
	public synchronized void deposit(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht, int type,
			double cash) {

		double balance = getValidAccountBalance(daoAccount, ht, type);

		if (balance != -1) {
			setValidAccountBalance(daoAccount, type, addBalance(balance, cash));
			saveTransaction(daoAccount, ht, getCurrentTime(), " Deposit ", "$"
					+ cash + " " + getAccountName(type) + " Total: $" + balance);
		} else {
			System.out
					.println("ERROR>>the account type which you want to deposit doesn't open.");
		}

	}

	// Get customer statement by account
	public void getCustomerStatementByAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		System.out
				.println("------------------------------STATEMENT------------------------------");
		System.out.println("Account ID: " + daoAccount.getAccountID());
		System.out.println("Name: " + daoAccount.getName());
		if (daoAccount.isOpenCheckingAccount()) {
			System.out.println("Balance of Checking account: "
					+ getBalanceOfCheckingAccount(daoAccount, ht));
		}
		if (daoAccount.isOpenSavingsAccount()) {
			System.out.println("Balance of Savings account: "
					+ getBalanceOfSavingsAccount(daoAccount, ht));
		}
		if (daoAccount.isOpenMaxiSavingsAccount()) {
			System.out.println("Balance of Maxi-Savings account: "
					+ getBalanceOfMaxiSavingsAccount(daoAccount, ht));
		}

		ArrayList<DAOTransaction> list = ht.get(daoAccount);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				DAOTransaction daoTran = (DAOTransaction) list.get(i);
				System.out.println(daoTran.getTranDate() + ">> Opt:"
						+ daoTran.getAction() + ": " + daoTran.getContent());
			}
		} else {
			System.out.println("No transactions");
		}
		System.out
				.println("---------------------------------------------------------------------");
	}

	// Get customer account information by manager
	public void getCustomerInfo(
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		System.out
				.println("------------------------------Customers & Accounts------------------------------");

		Iterator it = ht.entrySet().iterator();
		int nbr = 0;

		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			DAOAccount daoAccount = (DAOAccount) e.getKey();

			nbr++;
			String checking = daoAccount.isOpenCheckingAccount() ? "Checking Account|"
					: "";
			String savings = daoAccount.isOpenSavingsAccount() ? "Savings Account|"
					: "";
			String maxiSavings = daoAccount.isOpenMaxiSavingsAccount() ? "Maxi-Savings Account"
					: "";

			System.out.println("Account ID: " + daoAccount.getAccountID()
					+ " Name: " + daoAccount.getName() + " Accounts: "
					+ checking + savings + maxiSavings);
		}

		System.out
				.println("--------------------------------------------------------------------------------");
		System.out.println("Total customer number: " + nbr);
		System.out
				.println("--------------------------------------------------------------------------------");
	}

	// Get customer account information by manager
	public void getTotalInterestsPaidByBank(
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		System.out
				.println("------------------------------Interest paid------------------------------");

		Iterator it = ht.entrySet().iterator();
		double sumInterest = 0;

		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			DAOAccount daoAccount = (DAOAccount) e.getKey();

			double checkingInterest = daoAccount.isOpenCheckingAccount() ? getInterestOfCheckingAccount(
					daoAccount, ht) : 0;
			double savingsInterest = daoAccount.isOpenSavingsAccount() ? getInterestOfSavingsAccount(
					daoAccount, ht) : 0;
			double maxiSavingsInterest = daoAccount.isOpenMaxiSavingsAccount() ? getInterestOfMaxiSavingsAccount(
					daoAccount, ht) : 0;
			sumInterest = checkingInterest + savingsInterest
					+ maxiSavingsInterest;

			System.out.println("Account ID: " + daoAccount.getAccountID()
					+ " Name: " + daoAccount.getName() + " Interest: "
					+ sumInterest);
		}

		System.out
				.println("-------------------------------------------------------------------------");
		System.out.println("Total interest balance: " + sumInterest);
		System.out
				.println("-------------------------------------------------------------------------");
	}

	// Transfer
	public synchronized void transfer(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht, int fromType,
			int toType, double cash) {

		double fromBalance = getValidAccountBalance(daoAccount, ht, fromType);
		double toBalance = getValidAccountBalance(daoAccount, ht, toType);

		if (fromBalance != -1 && toBalance != -1) {
			fromBalance = payBalance(fromBalance, cash);
			if (fromBalance != -1) {
				setValidAccountBalance(daoAccount, fromType, fromBalance);
				setValidAccountBalance(daoAccount, toType,
						addBalance(toBalance, cash));
				saveTransaction(daoAccount, ht, getCurrentTime(), " Transfer ",
						"$" + cash + " From:" + getAccountName(fromType)
								+ " To:" + getAccountName(toType));
			} else {
				System.out
						.println("ERROR>>You have not enough money to transfer.");
			}
		} else {
			System.out
					.println("ERROR>>One of account type which you want to transfer doesn't open.");
		}

	}

	/************************* BUSINESS ****************************/
	// Save new account to Hashtable
	public Hashtable<DAOAccount, ArrayList<DAOTransaction>> saveAccount(
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht,
			DAOAccount daoAccount) {

		ht.put(daoAccount, new ArrayList<DAOTransaction>());
		return ht;
	}

	// Get valid account balance
	protected double getValidAccountBalance(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht, int type) {

		if (type == 1 && daoAccount.isOpenCheckingAccount()) {
			return getBalanceOfCheckingAccount(daoAccount, ht);
		} else if (type == 2 && daoAccount.isOpenSavingsAccount()) {
			return getBalanceOfSavingsAccount(daoAccount, ht);
		} else if (type == 3 && daoAccount.isOpenMaxiSavingsAccount()) {
			return getBalanceOfMaxiSavingsAccount(daoAccount, ht);
		} else {
			return -1;
		}
	}

	// Set valid account balance
	protected void setValidAccountBalance(DAOAccount daoAccount, int type,
			double balance) {

		if (type == 1 && daoAccount.isOpenCheckingAccount()) {
			daoAccount.setBalanceOfCheckingAccount(balance);
		} else if (type == 2 && daoAccount.isOpenSavingsAccount()) {
			daoAccount.setBalanceOfSavingsAccount(balance);
		} else {
			daoAccount.setBalanceOfMaxiSavingsAccount(balance);
		}
	}

	// Save transaction
	protected void saveTransaction(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht,
			Date recordDate, String action, String content) {

		ArrayList<DAOTransaction> list = ht.get(daoAccount);
		DAOTransaction daoTran = new DAOTransaction();
		daoTran.setTranDate(recordDate);
		daoTran.setAction(action);
		daoTran.setContent(content);
		list.add(daoTran);
	}

	// Get balance of checking account
	protected double getBalanceOfCheckingAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		double interest = getInterestOfCheckingAccount(daoAccount, ht);
		return daoAccount.getBalanceOfCheckingAccount() + interest;
	}

	// Get balance of savings account
	protected double getBalanceOfSavingsAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		double interest = getInterestOfSavingsAccount(daoAccount, ht);
		return daoAccount.getBalanceOfSavingsAccount() + interest;
	}

	// Get balance of Maxi-Savings account
	protected double getBalanceOfMaxiSavingsAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		double interest = getInterestOfMaxiSavingsAccount(daoAccount, ht);
		return daoAccount.getBalanceOfMaxiSavingsAccount() + interest;
	}

	// Get Interest of checking account
	protected double getInterestOfCheckingAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		double interest = calculateCheckingInterest(
				daoAccount.getBalanceOfCheckingAccount(),
				calculateDiffDays(daoAccount.getLastCalculateChecking(),
						getCurrentTime()));
		daoAccount.setInterestOfCheckingAccount(interest);

		return interest;
	}

	// Get Interest of savings account
	protected double getInterestOfSavingsAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		double interest = calculateSavingsInterest(
				daoAccount.getBalanceOfSavingsAccount(),
				calculateDiffDays(daoAccount.getLastCalculateSavings(),
						getCurrentTime()));
		daoAccount.setInterestOfSavingsAccount(interest);

		return interest;
	}

	// Get Interest of Maxi-Savings account
	protected double getInterestOfMaxiSavingsAccount(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht) {

		boolean withdraw = checkActionInDays(daoAccount, ht, "Withdraw", 10);
		double interest = calculateMaxiSavingsInterest(
				daoAccount.getBalanceOfMaxiSavingsAccount(),
				calculateDiffDays(daoAccount.getLastCalculateChecking(),
						getCurrentTime()), false, withdraw);
		daoAccount.setInterestOfMaxiSavingsAccount(interest);

		return interest;
	}

	// Check whether do some actions in regular days
	protected boolean checkActionInDays(DAOAccount daoAccount,
			Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht, String action,
			int days) {

		ArrayList<DAOTransaction> list = ht.get(daoAccount);
		for (int i = 0; i < list.size(); i++) {
			DAOTransaction daoTran = (DAOTransaction) list.get(i);

			if (calculateDiffDays(daoTran.getTranDate(), getCurrentTime()) <= days) {
				if (daoTran.getAction().trim().equals(action)) {
					return true;
				}
			}

		}

		return false;
	}

	// Calculate checking account interest
	protected synchronized double calculateCheckingInterest(double balance,
			int day) {

		int daysOfYear = getDaysOfYear();

		return formatNumber(balance * day * (0.1 / (100 * daysOfYear)));
	}

	// Calculate savings account interest
	protected synchronized double calculateSavingsInterest(double balance,
			int day) {

		int daysOfYear = getDaysOfYear();

		if (balance > 1000) {
			return formatNumber(1000 * day * (0.1 / (100 * daysOfYear))
					+ (balance - 1000) * day * (0.2 / (100 * daysOfYear)));
		} else {
			return formatNumber(balance * day * (0.1 / (100 * daysOfYear)));
		}
	}

	// Calculate Maxi-Savings account interest
	protected synchronized double calculateMaxiSavingsInterest(double balance,
			int day, boolean newRate, boolean withDraw) {

		int daysOfYear = getDaysOfYear();
		if (newRate) {
			double rate = 5;
			if (withDraw) {
				rate = 0.1;
			}
			return formatNumber(balance * day * (rate / (100 * daysOfYear)));
		} else {
			if (balance > 2000) {
				return formatNumber(1000 * day * (2 / (100 * daysOfYear))
						+ 1000 * day * (5 / (100 * daysOfYear))
						+ +(balance - 2000) * day * (10 / (100 * daysOfYear)));
			} else if (balance > 1000) {
				return formatNumber(1000 * day * (2 / (100 * daysOfYear))
						+ (balance - 1000) * day * (5 / (100 * daysOfYear)));
			} else {
				return formatNumber(balance * day * (2 / (100 * daysOfYear)));
			}
		}

	}

	// Pay balance
	protected synchronized double payBalance(double balance, double cash) {

		if (balance - cash >= 0) {
			balance = balance - cash;
			return balance;
		} else {
			return -1;
		}
	}

	// Add balance
	protected synchronized double addBalance(double balance, double cash) {

		balance = balance + cash;
		return balance;
	}

	/************************* Util ****************************/
	// Calculate different days between two date
	protected int calculateDiffDays(Date smallDate, Date bigDate) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date recordDate = sdf.parse(sdf.format(smallDate));
			Date currentDate = sdf.parse(sdf.format(bigDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(recordDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(currentDate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));

		} catch (ParseException e) {
			System.out.println("ERROR>>Date parse failed.");
			return 0;
		}

	}

	// Get current time
	protected Date getCurrentTime() {

		return new java.util.Date();
	}

	// Get days of this year
	protected int getDaysOfYear() {

		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	// Format money to decimal
	protected double formatNumber(double number) {

		double epsilon = 0.004f;
		if (Math.abs(Math.round(number) - number) < epsilon) {
			return Double.parseDouble(String.format("%10.0f", number));
		} else {
			return Double.parseDouble(String.format("%10.2f", number));
		}
	}

	// Get account name
	public String getAccountName(int type) {

		if (type == 1) {
			return "Checking Account";
		} else if (type == 2) {
			return "Savings Account";
		} else {
			return "Maxi-Savings Account";
		}
	}
}
