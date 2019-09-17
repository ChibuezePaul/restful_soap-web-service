package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FIMockController {
	
	@Autowired
	FIMockService fiMockService;
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	
	@PostMapping(value = "/fi",consumes = "text/xml",produces = "text/xml")
	public ExecuteServiceResponse getRequest(@RequestBody String request){
		try {
			String reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>" , "</ServiceRequestId>");
			return fiMockService.executeServiceResponse ( reqId, request );
		}catch ( Exception e ){
			logger.info ( "Level1 Error Occurred : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return new ExecuteServiceResponse ();
	}
}
