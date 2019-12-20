package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FIMockServiceImpl implements FIMockService {
	
	private static final String UPDATE_CORP_CUSTOMER = "updateCorpCustomer";
	private final FIMockRepository fiMockRepo;
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	
	@Autowired
	public FIMockServiceImpl ( FIMockRepository fiMockRepo ) {this.fiMockRepo = fiMockRepo;}
	
	@Override
	public ExecuteServiceResponse createSuccessfulResponse (@NotNull String request) {
		String serviceRequestId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		String requestParam1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
		String requestParam2 = StringUtils.substringBetween ( request, "<ChannelId>" , "</ChannelId>");
		String requestParam3 = StringUtils.substringBetween ( request, "<signId>" , "</signId>");
		String requestParam4 = StringUtils.substringBetween ( request, "<requestId>", "</requestId>" );
		String requestParam5 = StringUtils.substringBetween ( request, "<accountID>" , "</accountID>");
		List<String> requestParametersList = Arrays.asList (requestParam1,requestParam2,requestParam3,requestParam4,requestParam5);
		try{
			switch ( serviceRequestId ){
				case "executeFinacleScript" :
					response = fiMockRepo.executeFIScriptResponse ( requestParametersList );
					break;
					
				case "RetCustMod" :
				case UPDATE_CORP_CUSTOMER:
				case "verifyCustomerDetails" :
					if ( "RetCustMod".equals ( serviceRequestId ) )
						requestParam2 = StringUtils.substringBetween ( request, "<CustId>" , "</CustId>");
					else
						requestParam2 = ( UPDATE_CORP_CUSTOMER.equals ( serviceRequestId ) ) ? StringUtils.substringBetween ( request, "<corp_key>","</corp_key>")
						  : StringUtils.substringBetween ( request, "<cifId>","</cifId>" );
					requestParametersList = Arrays.asList (requestParam1,requestParam2,serviceRequestId);
					response = fiMockRepo.updateCustomerInfoResponse ( requestParametersList );
					break;
					
				case "SignatureAdd":
					requestParam2 = StringUtils.substringBetween ( request, "<AcctId>" , "</AcctId>");
					requestParam3 = StringUtils.substringBetween ( request, "<AcctCode>" , "</AcctCode>");
					requestParam4 = StringUtils.substringBetween ( request, "<BankCode>" , "</BankCode>");
					requestParam5 = StringUtils.substringBetween ( request, "<SigPowerNum>" , "</SigPowerNum>");
					requestParametersList = Arrays.asList (requestParam1, requestParam2, requestParam3, requestParam4, requestParam5);
					response = fiMockRepo.addMandateResponse ( requestParametersList );
					break;
					
				default:
					response = fiMockRepo.executeFIScriptResponse ( requestParametersList );
					break;
			}
		}catch ( Exception e ){
			logger.info ( "Level2 Error Occurred, RequestId is empty : {}, {} {}", serviceRequestId.isEmpty (), e.getMessage (), e.getStackTrace() );
		}
		logger.info ( "FIMOCK LOG : {}", new FIMockLog ( new Date (), request, filterResponseForLog ( response.toString () ) ) );
		return response;
	}
	
	private String filterResponseForLog ( String response ){
		
		StringBuilder filteredResponse = new StringBuilder ();
		final String NULL_EXECUTE_FIN_SCRIPT = "<executeFinacleScriptResponse>null</executeFinacleScriptResponse>";
		final String NULL_UPDATE_CORP_CUSTOMER = "<updateCorpCustomerResponse>null</updateCorpCustomerResponse>";
		final String NULL_RET_CUST_MOD = "<RetCustModResponse>null</RetCustModResponse>";
		final String NULL_SIGNATURE_ADD = "<SignatureAddResponse>null</SignatureAddResponse>";
		final String NULL_VERIFY_CUSTOMER_DETAILS = "<verifyCustomerDetailsResponse>null</verifyCustomerDetailsResponse>";
		final String placeHolder = "%s|%s";
		
		if(response!=null) {
			if ( ! StringUtils.substringBetween ( response, "<executeFinacleScriptResponse>", "</executeFinacleScriptResponse>" ).equals ( "null" ) )
				filteredResponse.append ( response
					  .replaceAll ( String.format ( placeHolder, NULL_RET_CUST_MOD, NULL_SIGNATURE_ADD ), "" )
					  .replaceAll ( String.format ( placeHolder, NULL_UPDATE_CORP_CUSTOMER, NULL_VERIFY_CUSTOMER_DETAILS ), "" ) );
			
			if ( ! StringUtils.substringBetween ( response, "<RetCustModResponse>", "</RetCustModResponse>" ).equals ( "null" ) )
				filteredResponse.append ( response
					  .replaceAll ( String.format ( placeHolder, NULL_EXECUTE_FIN_SCRIPT, NULL_SIGNATURE_ADD ), "" )
					  .replaceAll ( String.format ( placeHolder, NULL_UPDATE_CORP_CUSTOMER, NULL_VERIFY_CUSTOMER_DETAILS ), "" ) );
			
			if ( ! StringUtils.substringBetween ( response, "<updateCorpCustomerResponse>", "</updateCorpCustomerResponse>" ).equals ( "null" ) )
				filteredResponse.append ( response
					  .replaceAll ( String.format ( placeHolder, NULL_EXECUTE_FIN_SCRIPT, NULL_SIGNATURE_ADD ), "" )
					  .replaceAll ( String.format ( placeHolder, NULL_RET_CUST_MOD, NULL_VERIFY_CUSTOMER_DETAILS ), "" ) );
			
			if ( ! StringUtils.substringBetween ( response, "<verifyCustomerDetailsResponse>", "</verifyCustomerDetailsResponse>" ).equals ( "null" ) )
				filteredResponse.append ( response
					  .replaceAll ( String.format ( placeHolder, NULL_EXECUTE_FIN_SCRIPT, NULL_RET_CUST_MOD ), "" )
					  .replaceAll ( String.format ( placeHolder, NULL_UPDATE_CORP_CUSTOMER, NULL_SIGNATURE_ADD ), "" ) );
			
			if ( ! StringUtils.substringBetween ( response, "<SignatureAddResponse>", "</SignatureAddResponse>" ).equals ( "null" ) )
				filteredResponse.append ( response
					  .replaceAll ( String.format ( placeHolder, NULL_EXECUTE_FIN_SCRIPT, NULL_RET_CUST_MOD ), "" )
					  .replaceAll ( String.format ( placeHolder, NULL_UPDATE_CORP_CUSTOMER, NULL_VERIFY_CUSTOMER_DETAILS ), "" ) );
			
			return filteredResponse.toString ();
		}
		return filteredResponse.toString ();
	}
	
	public String createFailedResponse (String requestID){
		final String CUSTOM_FAILED_RESPONSE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
			  "                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
			  "    <soapenv:Header/>\n" +
			  "    <soapenv:Body>\n" +
			  "        <p594:createSuccessfulResponse xmlns:p594=\"http://webservice.fiusb.ci.infosys.com\">\n" +
			  "            <executeServiceReturn><FIXML xsi:schemaLocation=&quot;http://www.finacle.com/fixml " +
			  "executeFinacleScript.xsd&quot;\n" +
			  "                xmlns=&quot;http://www.finacle.com/fixml&quot; xmlns:xsi=&quot;http://www" +
			  ".w3.org/2001/XMLSchema-instance&quot;>\n" +
			  "                <Header>\n" +
			  "                    <ResponseHeader>\n" +
			  "                        <RequestMessageKey>\n" +
			  "                            <RequestUUID>Req_1561473245562</RequestUUID>\n" +
			  "                            <ServiceRequestId>"+requestID+"</ServiceRequestId>\n" +
			  "                            <ServiceRequestVersion>10.2</ServiceRequestVersion>\n" +
			  "                            <ChannelId>COR</ChannelId>\n" +
			  "                        </RequestMessageKey>\n" +
			  "                        <ResponseMessageInfo>\n" +
			  "                            <BankId>01</BankId>\n" +
			  "                            <TimeZone></TimeZone>\n" +
			  "                            <MessageDateTime>2019-06-25T14:33:47.301</MessageDateTime>\n" +
			  "                        </ResponseMessageInfo>\n" +
			  "                        <UBUSTransaction>\n" +
			  "                            <Id>null</Id>\n" +
			  "                            <Status>FAILED</Status>\n" +
			  "                        </UBUSTransaction>\n" +
			  "                        <HostTransaction>\n" +
			  "                            <Id>0000</Id>\n" +
			  "                            <Status>FAILURE</Status>\n" +
			  "                        </HostTransaction>\n" +
			  "                        <HostParentTransaction>\n" +
			  "                            <Id>null</Id>\n" +
			  "                            <Status>null</Status>\n" +
			  "                        </HostParentTransaction>\n" +
			  "                        <CustomInfo/>\n" +
			  "                    </ResponseHeader>\n" +
			  "                </Header>\n" +
			  "                <Body>\n" +
			  "                    <Error>\n" +
			  "                        <FIBusinessException>\n" +
			  "                            <ErrorDetail>\n" +
			  "                                <ErrorCode>SYS</ErrorCode>\n" +
			  "                                <ErrorDesc>Runtime error has occured</ErrorDesc>\n" +
			  "                                <ErrorSource></ErrorSource>\n" +
			  "                                <ErrorType>BE</ErrorType>\n" +
			  "                            </ErrorDetail>\n" +
			  "                        </FIBusinessException>\n" +
			  "                    </Error>\n" +
			  "                </Body>\n" +
			  "            </FIXML>\n" +
			  "        </executeServiceReturn>\n" +
			  "    </p594:createSuccessfulResponse>\n" +
			  "</soapenv:Body></soapenv:Envelope>";
		final String PRODUCT_FAILED_RESPONSE = "<soapenv:Envelope xmlns:soapenv=\"\"http://schemas.xmlsoap" +
			  ".org/soap/envelope/\"\" xmlns:soapenc=\"\"http://schemas.xmlsoap.org/soap/encoding/\"\" " +
			  "xmlns:xsd=\"\"http://www.w3.org/2001/XMLSchema\"\" xmlns:xsi=\"\"http://www" +
			  ".w3.org/2001/XMLSchema-instance\"\"><soapenv:Header/><soapenv:Body><p594:createSuccessfulResponse " +
			  "xmlns:p594=\"\"http://webservice.fiusb.ci.infosys.com\"\"><executeServiceReturn><FIXML " +
			  "xsi:schemaLocation=&quot;http://www.finacle.com/fixml RetCustMod.xsd&quot; xmlns=&quot;http://www.finacle" +
			  ".com/fixml&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;>\n" +
			  "    <Header>\n" +
			  "        <ResponseHeader>\n" +
			  "            <RequestMessageKey>\n" +
			  "                <RequestUUID>Req_1571058701975</RequestUUID>\n" +
			  "                <ServiceRequestId>"+requestID+"</ServiceRequestId>\n" +
			  "                <ServiceRequestVersion>10.2</ServiceRequestVersion>\n" +
			  "                <ChannelId>CRM</ChannelId>\n" +
			  "            </RequestMessageKey>\n" +
			  "            <ResponseMessageInfo>\n" +
			  "                <BankId>01</BankId>\n" +
			  "                <TimeZone></TimeZone>\n" +
			  "                <MessageDateTime>2019-10-14T13:11:47.164</MessageDateTime>\n" +
			  "            </ResponseMessageInfo>\n" +
			  "            <UBUSTransaction>\n" +
			  "                <Id>null</Id>\n" +
			  "                <Status>FAILED</Status>\n" +
			  "            </UBUSTransaction>\n" +
			  "            <HostTransaction>\n" +
			  "                <Id>0000</Id>\n" +
			  "                <Status>FAILURE</Status>\n" +
			  "            </HostTransaction>\n" +
			  "            <HostParentTransaction>\n" +
			  "                <Id>null</Id>\n" +
			  "                <Status>null</Status>\n" +
			  "            </HostParentTransaction>\n" +
			  "            <CustomInfo/>\n" +
			  "        </ResponseHeader>\n" +
			  "    </Header>\n" +
			  "    <Body>\n" +
			  "        <Error>\n" +
			  "            <FISystemException>\n" +
			  "                <ErrorDetail><ErrorCode>9999</ErrorCode><ErrorDesc>Finacle System Error Occoured!!! Please " +
			  "contact System Administrator.</ErrorDesc><ErrorType>SE</ErrorType></ErrorDetail>\n" +
			  "            </FISystemException>\n" +
			  "        </Error></Body>\n" +
			  "</FIXML>\n" +
			  "</executeServiceReturn></p594:createSuccessfulResponse></soapenv:Body></soapenv:Envelope>";
		
		if( requestID.equals ( "executeFinacleScript" ))
			return CUSTOM_FAILED_RESPONSE;
		return PRODUCT_FAILED_RESPONSE;
	}
}