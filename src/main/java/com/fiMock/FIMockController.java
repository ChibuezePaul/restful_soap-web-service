package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
public class FIMockController {
	
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	private FIMockService fiMockService;
	
	@Autowired
	public FIMockController ( FIMockService fiMockService ) {this.fiMockService = fiMockService;}
	
	public FIMockController () {}
	
	@ResponseBody
	@PostMapping ( value = "/fi", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE )
	public ExecuteServiceResponse mockSuccessfulFIRequest ( @RequestBody String request ) {
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			String reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
			response = fiMockService.executeServiceResponse ( reqId, request );
			return response;
		}
		catch ( Exception e ) {
			logger.info ( "Level 1 Error Occurred, Request is empty : {}, Error : {}", request.isEmpty () , e );
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping ( value = "/fi/error", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE )
	public String mockFailedFIRequest( @RequestBody String request ){
		String reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
		return fiMockService.createFailedResponse ( reqId );
	}
	
	@GetMapping
	public String index () {
		return "/fiXML/index";
	}
}