package com.fiMock;

import com.fiMock.fiXMLResponse.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class FIMockServiceImpl implements FIMockService {
	
	@Autowired
	private FIMockRepository fiMockRepo;
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	private String text1;
	private String text2;
	private String text3;
	private String text4;
	private String text5;
	private boolean check1;
	private boolean check2;
	private boolean check3;
	
	
	@Override
	public ExecuteServiceResponse executeServiceResponse (String serviceRequestId, String request) {
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try{
			switch ( serviceRequestId ){
				case "executeFinacleScript" :
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<ChannelId>" , "</ChannelId>");
					text3 = StringUtils.substringBetween ( request, "<signId>" , "</signId>");
					check1 = request.contains ( "<lienType>");
					check2 = StringUtils.substringBetween ( request, "<requestId>" , "</requestId>").contains ( "customHol.scr" );
					check3 = StringUtils.substringBetween ( request, "<requestId>" , "</requestId>").contains ( "customHol.scr" );
					response = fiMockRepo.executeFIScript ( text1, text2, text3, check1, check2, check3 );
					break;
				case "RetCustMod" :
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<CustId>" , "</CustId>");
					response = fiMockRepo.updateCustomerInfo ( text1, text2 );
					break;
				case "SignatureAdd":
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<AcctId>" , "</AcctId>");
					text3 = StringUtils.substringBetween ( request, "<AcctCode>" , "</AcctCode>");
					text4 = StringUtils.substringBetween ( request, "<BankCode>" , "</BankCode>");
					text5 = StringUtils.substringBetween ( request, "<SigPowerNum>" , "</SigPowerNum>");
					response = fiMockRepo.addMandate ( text1, text2, text3, text4, text5 );
					break;
			}
		}catch ( Exception e ){
			logger.info ( "Level2 Error Occurred : {}",e.getMessage () );
			e.printStackTrace ();
		}
		FIMockLog fiMockLog = new FIMockLog ( new Date (), request, filterResponseForLog ( response.toString () ) );
		logger.info ( "FIMOCK LOG : {}", fiMockLog );
		return response;
	}
	
	private String filterResponseForLog ( String response ){
		StringBuilder newResponse = new StringBuilder (  );
		
		if(!StringUtils
			  .substringBetween ( response, "<executeFinacleScriptResponse>" , "</executeFinacleScriptResponse>")
			  .equalsIgnoreCase ("null" ) )
				newResponse
				  .append ( response.replaceAll ( "<RetCustModResponse>null</RetCustModResponse>|<SignatureAddResponse>null</SignatureAddResponse>","" ) );
		
		if(!StringUtils
			  .substringBetween ( response, "<RetCustModResponse>" , "</RetCustModResponse>")
			  .equalsIgnoreCase ("null" ) )
				newResponse
				  .append ( response.replaceAll ( "<executeFinacleScriptResponse>null</executeFinacleScriptResponse>|<SignatureAddResponse>null</SignatureAddResponse>","" ) );
		
		if( !StringUtils
			  .substringBetween ( response, "<SignatureAddResponse>", "</SignatureAddResponse>" )
			  .equalsIgnoreCase ("null" ) )
				newResponse
				  .append ( response.replaceAll ( "<executeFinacleScriptResponse>null</executeFinacleScriptResponse>|<RetCustModResponse>null</RetCustModResponse>","" ) );
		
		return newResponse.toString ();
	}
}