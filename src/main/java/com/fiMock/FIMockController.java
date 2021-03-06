package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

@Controller
public class FIMockController {
	
	private final Logger logger = LoggerFactory.getLogger ( this.getClass () );
	private FIMockService fiMockService;
	
	@Autowired
	public FIMockController ( FIMockService fiMockService ) {this.fiMockService = fiMockService;}
	
	public FIMockController () {}
	
	@ResponseBody
	@PostMapping ( value = "/fi", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE )
	public ExecuteServiceResponse mockSuccessfulFIRequest ( @NotNull @RequestBody String request ) {
		ExecuteServiceResponse response = new ExecuteServiceResponse ();
		try {
			String reqId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
			response = fiMockService.createSuccessfulResponse ( request );
			return response;
		}
		catch ( Exception e ) {
			logger.info ( "Level 1 Error Occurred, Request is empty : {}, Error : {}", request.isEmpty () , e );
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping ( value = "/fi/error", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_XML_VALUE )
	public String mockFailedFIRequest( @NotNull @RequestBody String request ){
		String requestId = StringUtils.substringBetween ( request, "<ServiceRequestId>", "</ServiceRequestId>" );
		return fiMockService.createFailedResponse ( requestId );
	}
	
	@GetMapping
	public String index () {
		return "/fiXML/index";
	}
}