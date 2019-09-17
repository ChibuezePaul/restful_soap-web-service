package com.fiMock;

import com.fiMock.fiXMLResponse.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private String text6;
	private boolean check1;
	private boolean check2;
	private boolean check3;
	
	
	@Override
	public ExecuteServiceResponse executeServiceResponse (String serviceRequestId, String request) {
		try{
			switch ( serviceRequestId ){
				case "executeFinacleScript" :
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<ChannelId>" , "</ChannelId>");
					text3 = StringUtils.substringBetween ( request, "<signId>" , "</signId>");
					text4 = StringUtils.substringBetween ( request, "<SuccessOrFailure>" , "</SuccessOrFailure>");
					check1 = request.contains ( "<lienType>");
					check2 = StringUtils.substringBetween ( request, "<requestId>" , "</requestId>").contains ( "customHol.scr" );
					check3 = StringUtils.substringBetween ( request, "<requestId>" , "</requestId>").contains ( "customHol.scr" );
					return fiMockRepo.executeFIScript ( text1, text2, text3, check1, check2, check3, text4 );
					
				case "RetCustMod" :
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<CustId>" , "</CustId>");
					text3 = "";
					text4 = "";
					text5 = "";
					text6 = "";
					return fiMockRepo.updateCustomerInfo ( text1, text2, text3, text4, text5, text6 );
					
				case "SignatureAdd":
					text1 = StringUtils.substringBetween ( request, "<RequestUUID>" , "</RequestUUID>");
					text2 = StringUtils.substringBetween ( request, "<AcctId>" , "</AcctId>");
					text3 = StringUtils.substringBetween ( request, "<AcctCode>" , "</AcctCode>");
					text4 = StringUtils.substringBetween ( request, "<BankCode>" , "</BankCode>");
					text5 = StringUtils.substringBetween ( request, "<SigPowerNum>" , "</SigPowerNum>");
					text6 = "";
					return fiMockRepo.addMandate ( text1, text2, text3, text4, text5, text6 );
					
				default:
					return new ExecuteServiceResponse ();
			}
		}catch ( Exception e ){
			logger.info ( "Level2 Error Occurred : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return null;
	}
}
