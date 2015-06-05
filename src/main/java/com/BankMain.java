package main.java.com;

import java.util.ArrayList;
import java.util.Hashtable;


public class BankMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BankBusiness bb = new BankBusiness();
		//Tempory DB to save data
		Hashtable<DAOAccount, ArrayList<DAOTransaction>> ht = new Hashtable<DAOAccount, ArrayList<DAOTransaction>>();
		
		DAOAccount gzAccount = new DAOAccount();
		bb.openAccount(gzAccount, "001", "Gezhi Zhong", true, 100.0, true, 0.0, true, 0.0);
		bb.saveAccount(ht, gzAccount);
		DAOAccount hqAccount = new DAOAccount();
		bb.openAccount(hqAccount, "002", "Helen Qian", true, 0.0, true, 100.0, false, 0.0);
		bb.saveAccount(ht, hqAccount);
		
		//Type 1: checking, 2: savings, 3:Maxi-Savings
		bb.deposit(gzAccount,  ht, 1, 100);
		bb.deposit(gzAccount,  ht, 2, 100);
		bb.withdraw(gzAccount,  ht, 1, 50);
		bb.transfer(gzAccount, ht, 1, 2, 20);
		
		bb.getCustomerStatementByAccount(gzAccount, ht);
		
		bb.deposit(hqAccount,  ht, 1, 100);
		bb.deposit(hqAccount,  ht, 3, 100);
		bb.withdraw(hqAccount,  ht, 1, 50);
		bb.transfer(hqAccount, ht, 1, 2, 20);
		
		bb.getCustomerStatementByAccount(hqAccount, ht);
		
		bb.getCustomerInfo(ht);
		bb.getTotalInterestsPaidByBank(ht);

	}

}
