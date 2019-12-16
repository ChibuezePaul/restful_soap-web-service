package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;

public interface FIMockService {
	ExecuteServiceResponse createSuccessfulResponse ( String serviceRequestId, String request );
	String createFailedResponse (String reqID);
}
