/*
 * Copyright (C) 2016 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.eclipseplugin.internal;

import com.blackducksoftware.integration.hub.rest.RestConnection;

public class AuthorizationResponse {

    private RestConnection connection;

    private String responseMessage;

    public AuthorizationResponse(RestConnection connection, String responseMessage) {
        this.connection = connection;
        this.responseMessage = responseMessage;
    }

    public AuthorizationResponse(String responseMessage) {
        this(null, responseMessage);
    }

    public RestConnection getConnection() {
        return connection;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

}
