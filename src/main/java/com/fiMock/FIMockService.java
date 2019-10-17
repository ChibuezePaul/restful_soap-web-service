package com.fiMock;

import com.fiMock.fiXMLResponse.ExecuteServiceResponse;

public interface FIMockService {
	ExecuteServiceResponse executeServiceResponse ( String serviceRequestId, String request );
}
