package main.java.com;

import java.util.Date;

public class DAOTransaction {
	
	private Date tranDate;
	private String action;
	private String content;

	protected Date getTranDate() {
		return tranDate;
	}

	protected void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}
	
	protected String getAction() {
		return action;
	}

	protected void setAction(String action) {
		this.action = action;
	}

	protected String getContent() {
		return content;
	}

	protected void setContent(String content) {
		this.content = content;
	}

}
