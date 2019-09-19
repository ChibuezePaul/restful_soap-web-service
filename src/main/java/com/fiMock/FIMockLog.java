package com.fiMock;

import java.sql.Timestamp;
import java.util.Date;

public class FIMockLog {
//	Timestamp id = new Timestamp(new Date ().getTime ());
	Date date;
	String request;
	String response;
	
	public FIMockLog ( Date date, String request, String response ) {
		this.date = date;
		this.request = request;
		this.response = response;
	}
	
	@Override
	public String toString () {
		return "FIMockLog{" +
			  "date=" + date +
			  ", request='" + request + '\'' +
			  ", response='" + response + '\'' +
			  '}';
	}
}
