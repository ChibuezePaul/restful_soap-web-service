package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;

public interface FIMockService {
	ExecuteServiceResponse createSuccessfulResponse ( String request);
	String createFailedResponse (String requestId);
}
