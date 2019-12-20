package com.fiMock;

import com.fiMock.fiXMLResponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class FIMockRepository {
	
	private static final String VALUE = "value";
	private static final String LEVEL_3_ERROR_OCCURRED = "Level 3 Error Occured : {} {}";
	private static final String SUCCESS_OR_FAILURE = "SUCCESS";
	private static final Logger logger = LoggerFactory.getLogger ( FIMockRepository.class );
	private static String messageDateTime;
	@Value ( "${spring.datasource.url}" )
	private String dbUrl;
	@Value ( "${spring.datasource.username}" )
	private String dbUsername;
	@Value ( "${spring.datasource.password}" )
	private String dbPassword;
	static {
		try {
			messageDateTime = String.valueOf( DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar ()).normalize());
		} catch (DatatypeConfigurationException e) {
			logger.info ( "MessageDateTime Error {}",e );
		}
	}
	
	public ExecuteServiceResponse executeFIScriptResponse ( List<String> requestParametersList ) {
		
		String requestUUID = requestParametersList.get (0);
		String channelId = requestParametersList.get (1);
		String signId = requestParametersList.get (2);
		String requestId = requestParametersList.get (3);
		String acctNumber = requestParametersList.get (4);
		
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( requestUUID, "executeFinacleScript", channelId ) );
			
			ExecuteFinacleScriptCustomData scriptCustomData = new ExecuteFinacleScriptCustomData ();
			scriptCustomData.setSuccessOrFailure ( SUCCESS_OR_FAILURE );
			
			if ( signId != null ) scriptCustomData.setSignId ( signId );
			
			if ( "addLienCustom.scr".equals ( requestId ) ) scriptCustomData.setLienB2KId ( "01183256054" );
			
			if ( "customHol.scr".equals ( requestId ) ) {
				scriptCustomData.setPrevHol ( "YYYYNNNNYYNNNNNYYNNNNNYYNNNNNY" );
				scriptCustomData.setMainHol ( "YYNNNN" );
			}
			
			if( "customAcctInfoUpdate.scr".equals ( requestId ) ){
				String query = String.format ( "select a.branch_sol, a.account_currency, b.value, a.account_category, a.account_scheme, " +
					  "a.account_secondary_category, a.account_officer_code, a.account_officer_desc, a.broker_code" +
					  " from account a, bio b where exists (select a.id where (b.id = a.account_name_id or b.id = a.account_status_id)" +
					  " and account_number = '%s')", acctNumber );
				generateAcctInfo ( scriptCustomData, query );
			}
			
			ExecuteFinacleScriptResponse scriptResponse = new ExecuteFinacleScriptResponse ();
			scriptResponse.setExecuteFinacleScriptCustomData ( scriptCustomData );
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
			logger.info ( LEVEL_3_ERROR_OCCURRED,e.getMessage (),e.getStackTrace() );
		}
		return response;
	}
	
	public ExecuteServiceResponse updateCustomerInfoResponse (List<String> requestParametersList) {
		
		String requestUUID = requestParametersList.get (0);
		String customerId = requestParametersList.get (1);
		String serviceRequestId = requestParametersList.get (2);
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( requestUUID, serviceRequestId, "CRM" ) );
			
			Body body = new Body ();
			
			if("updateCorpCustomer".equals ( serviceRequestId )){
				CustomerModOutputStruct customerModOutputStruct = new CustomerModOutputStruct ();
				customerModOutputStruct.setcifid ( customerId );
				customerModOutputStruct.setDesc ( String.format ( "Corporate Customer successfully updated with CIFID %s", customerId ) );
				customerModOutputStruct.setEntity ( "Corporate Customer" );
				customerModOutputStruct.setService ( "CIFCorpCustomerUpdate" );
				customerModOutputStruct.setStatus ( SUCCESS_OR_FAILURE );
				
				updateCorpCustomerResponse corpCustomerResponse = new updateCorpCustomerResponse ();
				corpCustomerResponse.setCustomerModOutputStruct ( customerModOutputStruct );
				corpCustomerResponse.setupdateCorpCustomer_CustomData ( new updateCorpCustomer_CustomData () );
				
				body.setupdateCorpCustomerResponse ( corpCustomerResponse );
			}
			else if ( "RetCustMod".equals ( serviceRequestId ) ){
				RetCustModRs retCustModRs = new RetCustModRs ();
				retCustModRs.setCustId ( customerId );
				retCustModRs.setDesc ( String.format ( "Retail Customer successfully updated with CIFID %s", customerId ) );
				retCustModRs.setEntity ( "Retail Customer" );
				retCustModRs.setService ( "CIFRetailCustomerUpdate" );
				retCustModRs.setStatus ( SUCCESS_OR_FAILURE );
				
				RetCustModResponse custModResponse = new RetCustModResponse ();
				custModResponse.setRetCustModCustomData ( "" );
				custModResponse.setRetCustModRs ( retCustModRs );
				
				body.setRetCustModResponse ( custModResponse );
			}
			else {
				CustomerVerifyRs customerVerifyRs = new CustomerVerifyRs ();
				customerVerifyRs.setcifid ( customerId );
				customerVerifyRs.setDesc ( "Customer Verified" );
				customerVerifyRs.setStatus ( SUCCESS_OR_FAILURE );
				
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
			logger.info ( LEVEL_3_ERROR_OCCURRED,e.getMessage (),e.getStackTrace() );
		}
		return response;
	}
	
	public ExecuteServiceResponse addMandateResponse (List<String> requestParametersList) {
		
		String requestUUID = requestParametersList.get (0);
		String acctId = requestParametersList.get (1);
		String acctCode = requestParametersList.get (2);
		String bankCode = requestParametersList.get (3);
		String sigPowerNum = requestParametersList.get (4);
		
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		
		try {
			Header header = new Header ();
			header.setResponseHeader ( createResponseHeader ( requestUUID, "SignatureAdd", "COR" ) );
			
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
			logger.info ( LEVEL_3_ERROR_OCCURRED,e.getMessage (),e.getStackTrace() );
		}
		return response;
	}
	
	private ResponseHeader createResponseHeader(String requestUUID, String serviceRequestId, String channelId){
		
		RequestMessageKey messageKey = new RequestMessageKey ();
		messageKey.setRequestUUID ( requestUUID );
		messageKey.setServiceRequestId ( serviceRequestId );
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
		hostTransaction.setStatus ( SUCCESS_OR_FAILURE );
		
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
	
	private void generateAcctInfo ( ExecuteFinacleScriptCustomData customData, String query ) {
		try ( final Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword); final PreparedStatement preparedStatement = connection.prepareCall(query); final ResultSet resultSet = preparedStatement.executeQuery() ) {
			logger.info("Database Connected Successfully");
			String queryValue;
			resultSet.next ();
			queryValue = resultSet.getString ( "branch_sol" ) != null ? resultSet.getString ( "branch_sol" ) : "001";
			customData.setSolId ( queryValue );
			queryValue = resultSet.getString ( "account_currency" ) != null ? resultSet.getString ( "account_currency" ) : "NGN";
			customData.setCrncyCode ( queryValue );
			customData.setFreezeCode ( "null" );
			customData.setAcctBal ( "1000000");
			queryValue = resultSet.getString ( "account_category" ) != null ? resultSet.getString ( "account_category" ) : "CAA";
			customData.setSchmType ( queryValue );
			queryValue = resultSet.getString ( "account_scheme" ) != null ? resultSet.getString ( "account_scheme" ) : "CA202";
			customData.setSchmCode ( queryValue );
			queryValue = resultSet.getString ( "account_secondary_category" ) != null ? resultSet.getString ( "account_secondary_category" ) : "PREMIUM CURRENT";
			customData.setSchmCodeDesc ( queryValue );
			queryValue = resultSet.getString ( "account_officer_code" ) != null ? resultSet.getString ( "account_officer_code" ) : "001DE";
			customData.setAcctOfficerCode ( queryValue );
			queryValue = resultSet.getString ( "broker_code" ) != null ? resultSet.getString ( "broker_code" ) : "D1782";
			customData.setAcctBrokerCode ( queryValue );
			customData.setBrokerCodeDesc ( "ENEJE JANE ONYINYE" );
			//The column VALUE holds the customer name from the first row returned
			queryValue = resultSet.getString ( VALUE ) != null ? resultSet.getString ( VALUE ) : "OKOROAFOR VINCENT ONYEKWERE";
			customData.setAcctName ( queryValue );
			customData.setAcctSMSStatus ( "N" );
			customData.setAcctEmailStatus( "Y" );
			resultSet.next ();
			//The column VALUE holds the customer account status from the second row returned
			queryValue = resultSet.getString ( VALUE ) != null ? resultSet.getString ( VALUE ) : "A";
			customData.setAcctStatus ( queryValue );
			logger.info("Record Size {}",resultSet.getFetchSize ());
		} catch (Exception e) {
			logger.error ("Database Error Occured : {} {}", e.getMessage (),e.getStackTrace ());
			customData.setSolId ( "001" );
			customData.setCrncyCode ( "NGN" );
			customData.setFreezeCode ( "null" );
			customData.setAcctBal ( "1000000");
			customData.setSchmType ( "CAA" );
			customData.setSchmCode ( "CA202" );
			customData.setSchmCodeDesc ( "PREMIUM CURRENT" );
			customData.setAcctOfficerCode ( "001DE" );
			customData.setAcctBrokerCode ( "D1782" );
			customData.setBrokerCodeDesc ( "ENEJE JANE ONYINYE" );
			//The column VALUE holds the customer name from the first row returned
			customData.setAcctName ( "OKOROAFOR VINCENT ONYEKWERE" );
			customData.setAcctSMSStatus ( "N" );
			customData.setAcctEmailStatus( "Y" );
			//The column VALUE holds the customer account status from the second row returned
			customData.setAcctStatus ( "A" );
		}
	}
}
