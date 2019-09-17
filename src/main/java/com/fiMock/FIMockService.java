package com.fiMock;

import com.fiMock.fiXMLResponse.*;

public interface FIMockService {
	ExecuteServiceResponse executeServiceResponse(String serviceRequestId, String request);
}
