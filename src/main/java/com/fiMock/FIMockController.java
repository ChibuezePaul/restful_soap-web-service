package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
public class FIMockController {
	
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	private FIMockService fiMockService;
	
	@Autowired
	public FIMockController ( FIMockService fiMockService ) {this.fiMockService = fiMockService;}
	
	public FIMockController () {}
	
	@PostMapping ( value = "/fi", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE )
	public ExecuteServiceResponse getRequest ( @RequestBody String request, String reqId ) {
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
			response = fiMockService.executeServiceResponse ( reqId, request );
			return response;
		}
		catch ( Exception e ) {
			logger.info ( "Level1 Error Occurred, Request is empty : {}, Error : {}", request.isEmpty () , e.getMessage () );
			e.printStackTrace ();
		}
		return response;
	}
	
	@GetMapping
	public String index () {
		return "THIS IS A FINACLE MOCK SERVER...URI - http://{hostname}:8888/fi";
	}
}