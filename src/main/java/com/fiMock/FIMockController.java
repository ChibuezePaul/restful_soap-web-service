package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FIMockController {
	
	private FIMockService fiMockService;
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	
	@Autowired
	public FIMockController ( FIMockService fiMockService ) {this.fiMockService = fiMockService;}
	
	public FIMockController(){}
	
	@PostMapping(value = "/fi",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ExecuteServiceResponse getRequest(@RequestBody String request){
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			String reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>" , "</ServiceRequestId>");
			response = fiMockService.executeServiceResponse ( reqId, request );
			return response;
		}catch ( Exception e ){
			logger.info ( "Level1 Error Occurred : {}",e.getMessage () );
			e.printStackTrace ();
		}
		return response;
	}
	
	@GetMapping
	public String index(){
		return "THIS IS A FINACLE MOCK SERVER...URI - http://{hostname}:8888/fi";
	}
}
