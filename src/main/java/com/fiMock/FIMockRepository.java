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
			logger.info ( "MessageDateTime Error {}",e );
		}
	}
	
	public ExecuteServiceResponse executeFIScript ( String reqUUID, String channelId, String signId, boolean isLien,
		  boolean isChannel ) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( reqUUID, "executeFinacleScript", channelId ) );
			
			ExecuteFinacleScriptCustomData customData = new ExecuteFinacleScriptCustomData ();
			customData.setSuccessOrFailure ( "SUCCESS" );
			customData.setAcctName ( "");
			customData.setAcctBal ( "1000");
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
			logger.info ( "Level 3 Error Occured : {}",e );
		}
		return response;
	}
	
	public ExecuteServiceResponse updateCustomerInfo (String reqUUID, String custId, String servReqId, boolean isCorpCustomer ) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( reqUUID, servReqId, "CRM" ) );
			
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
				corpCustomerResponse.setupdateCorpCustomer_CustomData ( new updateCorpCustomer_CustomData () );
				
				body.setupdateCorpCustomerResponse ( corpCustomerResponse );
			}
			else if ( "RetCustMod".equals ( servReqId ) ){
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
			else {
				CustomerVerifyRs customerVerifyRs = new CustomerVerifyRs ();
				customerVerifyRs.setcifid ( custId );
				customerVerifyRs.setDesc ( "Customer Verified" );
				customerVerifyRs.setStatus ( "Success" );
				
				verifyCustomerDetailsResponse verifyCustomerDetailsResponse = new verifyCustomerDetailsResponse ();
				verifyCustomerDetailsResponse.setCustomerVerifyRs ( customerVerifyRs );
				verifyCustomerDetailsResponse.setverifyCustomerDetails_CustomData ( new verifyCustomerDetails_CustomData () );
				
				body.setverifyCustomerDetailsResponse ( verifyCustomerDetailsResponse );
			}
			
			FIXML fixml = new FIXML ();
			fixml.setBody ( body );
			fixml.setHeader ( header );
			
			ExecuteServiceReturn serviceReturn = new ExecuteServiceReturn ();
			serviceReturn.setFIXML ( fixml );
			
			response.setExecuteServiceReturn ( serviceReturn );
			return response;
		}catch ( Exception e ){
			logger.info ( "Level 3 Error Occured : {}",e );
		}
		return response;
	}
	
	public ExecuteServiceResponse addMandate (String reqUUID, String acctId, String acctCode, String bankCode, String sigPowerNum) {
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( reqUUID, "SignatureAdd", "COR" ) );
			
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
			logger.info ( "Level 3 Error Occured : {}",e );
		}
		return response;
	}
	
	private ResponseHeader createResponseHeader(String reqUUID, String servReqId, String channelId){
		
		RequestMessageKey messageKey = new RequestMessageKey ();
		messageKey.setRequestUUID ( reqUUID );
		messageKey.setServiceRequestId ( servReqId );
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
		
		return  responseHeader;
	}
}
