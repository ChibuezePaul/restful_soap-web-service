package com.fiMock;

import com.fiMock.fiXMLResponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;

@Component
public class FIMockRepository {
	
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	private String messageDateTime;
	
	{
		try {
			messageDateTime = String.valueOf( DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar ()).normalize());
		} catch (DatatypeConfigurationException e) {
			e.getMessage();
		}
	}
	
	public ExecuteServiceResponse executeFIScript ( String reqUUID, String channelId, String signId, boolean isLien,
		  boolean isChannel ) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			RequestMessageKey messageKey = new RequestMessageKey ();
			messageKey.setRequestUUID ( reqUUID );
			messageKey.setServiceRequestId ( "executeFinacleScript" );
			messageKey.setServiceRequestVersion ( "10.2" );
			messageKey.setChannelId ( channelId );
			
			ResponseMessageInfo messageInfo = new ResponseMessageInfo ();
			messageInfo.setBankId ( "01" );
			messageInfo.setMessageDateTime ( messageDateTime );
			messageInfo.setTimeZone ( "" );
			
			UBUSTransaction ubusTransaction = new UBUSTransaction ();
			ubusTransaction.setId ( "" );
			ubusTransaction.setStatus ( "" );
			
			HostTransaction hostTransaction = new HostTransaction ();
			hostTransaction.setId ( hostTransaction.getId () );
			hostTransaction.setStatus ( "SUCCESS" );
			
			HostParentTransaction parentTransaction = new HostParentTransaction ();
			parentTransaction.setId ( "" );
			parentTransaction.setStatus ( "" );
			
			ResponseHeader responseHeader = new ResponseHeader ();
			responseHeader.setRequestMessageKey ( messageKey );
			responseHeader.setResponseMessageInfo ( messageInfo );
			responseHeader.setUBUSTransaction ( ubusTransaction );
			responseHeader.setHostTransaction ( hostTransaction );
			responseHeader.setHostParentTransaction ( parentTransaction );
			responseHeader.setCustomInfo ( "" );
			
			Header header = new Header ();
			header.setResponseHeader ( responseHeader );
			
			ExecuteFinacleScriptCustomData customData = new ExecuteFinacleScriptCustomData ();
			customData.setSuccessOrFailure ( "SUCCESS" );
			if ( signId != null ) customData.setSignId ( signId );
			if ( isLien ) customData.setLienB2KId ( "01183256054" );
			if ( isChannel ) {
				customData.setPrevHol ( "YYYYNNNNYYNNNNNYYNNNNNYYNNNNNY" );
				customData.setMainHol ( "YYNNNN" );
			}
			
			ExecuteFinacleScriptResponse scriptResponse = new ExecuteFinacleScriptResponse ();
			scriptResponse.setExecuteFinacleScriptCustomData ( customData );
			scriptResponse.setExecuteFinacleScriptOutputVO ( "" );
			
			Body body = new Body ();
			body.setExecuteFinacleScriptResponse ( scriptResponse );
			
			FIXML fixml = new FIXML ();
			fixml.setBody ( body );
			fixml.setHeader ( header );
			
			ExecuteServiceReturn serviceReturn = new ExecuteServiceReturn ();
			serviceReturn.setFIXML ( fixml );
			response.setExecuteServiceReturn ( serviceReturn );
			
		}catch ( Exception e ){
			logger.info ( "Level3 Error Occured : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return response;
	}
	
	public ExecuteServiceResponse updateCustomerInfo (String reqUUID, String custId, boolean isCorpCustomer ) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			RequestMessageKey messageKey = new RequestMessageKey ();
			messageKey.setRequestUUID ( reqUUID );
			messageKey.setServiceRequestId ( "RetCustMod" );
			messageKey.setServiceRequestVersion ( "10.2" );
			messageKey.setChannelId ( "CRM" );
			
			ResponseMessageInfo messageInfo = new ResponseMessageInfo ();
			messageInfo.setBankId ( "01" );
			messageInfo.setMessageDateTime ( messageDateTime );
			messageInfo.setTimeZone ( "" );
			
			UBUSTransaction ubusTransaction = new UBUSTransaction ();
			ubusTransaction.setId ( "" );
			ubusTransaction.setStatus ( "" );
			
			HostTransaction hostTransaction = new HostTransaction ();
			hostTransaction.setId ( "" );
			hostTransaction.setStatus ( "SUCCESS" );
			
			HostParentTransaction parentTransaction = new HostParentTransaction ();
			parentTransaction.setId ( "" );
			parentTransaction.setStatus ( "" );
			
			ResponseHeader responseHeader = new ResponseHeader ();
			responseHeader.setRequestMessageKey ( messageKey );
			responseHeader.setResponseMessageInfo ( messageInfo );
			responseHeader.setUBUSTransaction ( ubusTransaction );
			responseHeader.setHostTransaction ( hostTransaction );
			responseHeader.setHostParentTransaction ( parentTransaction );
			responseHeader.setCustomInfo ( "" );
			
			Header header = new Header ();
			header.setResponseHeader ( responseHeader );
			
			Body body = new Body ();
			
			if(isCorpCustomer){
				
				CustomerModOutputStruct customerModOutputStruct = new CustomerModOutputStruct ();
				customerModOutputStruct.setcifid ( custId );
				customerModOutputStruct.setDesc ( String.format ( "Corporate Customer successfully updated with CIFID %s", custId ) );
				customerModOutputStruct.setEntity ( "Corporate Customer" );
				customerModOutputStruct.setService ( "CIFCorpCustomerUpdate" );
				customerModOutputStruct.setStatus ( "SUCCESS" );
				
				updateCorpCustomerResponse corpCustomerResponse = new updateCorpCustomerResponse ();
				corpCustomerResponse.setCustomerModOutputStruct ( customerModOutputStruct );
				
				body.setupdateCorpCustomerResponse ( corpCustomerResponse );
			}
			else {
				RetCustModRs retCustModRs = new RetCustModRs ();
				retCustModRs.setCustId ( custId );
				retCustModRs.setDesc ( String.format ( "Retail Customer successfully updated with CIFID %s", custId ) );
				retCustModRs.setEntity ( "Retail Customer" );
				retCustModRs.setService ( "CIFRetailCustomerUpdate" );
				retCustModRs.setStatus ( "SUCCESS" );
				
				RetCustModResponse custModResponse = new RetCustModResponse ();
				custModResponse.setRetCustModCustomData ( "" );
				custModResponse.setRetCustModRs ( retCustModRs );
				
				body.setRetCustModResponse ( custModResponse );
			}
			
			FIXML fixml = new FIXML ();
			fixml.setBody ( body );
			fixml.setHeader ( header );
			
			ExecuteServiceReturn serviceReturn = new ExecuteServiceReturn ();
			serviceReturn.setFIXML ( fixml );
			
			response.setExecuteServiceReturn ( serviceReturn );
			return response;
		}catch ( Exception e ){
			logger.info ( "Level3 Error Occured : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return response;
	}
	
	public ExecuteServiceResponse addMandate (String reqUUID, String acctId, String acctCode, String bankCode, String sigPowerNum) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			RequestMessageKey messageKey = new RequestMessageKey ();
			messageKey.setRequestUUID ( reqUUID );
			messageKey.setServiceRequestId ( "executeFinacleScript" );
			messageKey.setServiceRequestVersion ( "10.2" );
			messageKey.setChannelId ( "COR" );
			
			ResponseMessageInfo messageInfo = new ResponseMessageInfo ();
			messageInfo.setBankId ( "01" );
			messageInfo.setMessageDateTime ( messageDateTime );
			messageInfo.setTimeZone ( "" );
			
			UBUSTransaction ubusTransaction = new UBUSTransaction ();
			ubusTransaction.setId ( "" );
			ubusTransaction.setStatus ( "" );
			
			HostTransaction hostTransaction = new HostTransaction ();
			hostTransaction.setId ( "" );
			hostTransaction.setStatus ( "SUCCESS" );
			
			HostParentTransaction parentTransaction = new HostParentTransaction ();
			parentTransaction.setId ( "" );
			parentTransaction.setStatus ( "" );
			
			ResponseHeader responseHeader = new ResponseHeader ();
			responseHeader.setRequestMessageKey ( messageKey );
			responseHeader.setResponseMessageInfo ( messageInfo );
			responseHeader.setUBUSTransaction ( ubusTransaction );
			responseHeader.setHostTransaction ( hostTransaction );
			responseHeader.setHostParentTransaction ( parentTransaction );
			responseHeader.setCustomInfo ( "" );
			
			Header header = new Header ();
			header.setResponseHeader ( responseHeader );
			
			SignatureAddRs signatureAddRs = new SignatureAddRs ();
			signatureAddRs.setAcctId ( acctId );
			signatureAddRs.setAcctCode ( acctCode );
			signatureAddRs.setCustId ( "" );
			signatureAddRs.setEmployeeIdent ( "" );
			signatureAddRs.setBankCode ( bankCode );
			signatureAddRs.setSigPowerNum ( sigPowerNum );
			signatureAddRs.setSigAddStatusCode ( "true" );
			
			SignatureAddResponse signatureAddResponse = new SignatureAddResponse ();
			signatureAddResponse.setSignatureAddCustomData ( "" );
			signatureAddResponse.setSignatureAddRs ( signatureAddRs );
			Body body = new Body ();
			body.setSignatureAddResponse ( signatureAddResponse );
			
			FIXML fixml = new FIXML ();
			fixml.setBody ( body );
			fixml.setHeader ( header );
			
			ExecuteServiceReturn serviceReturn = new ExecuteServiceReturn ();
			serviceReturn.setFIXML ( fixml );
			
			response.setExecuteServiceReturn ( serviceReturn );
			return response;
		}catch ( Exception e ){
			logger.info ( "Level3 Error Occured : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return response;
	}
}
