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
package com.blackducksoftware.integration.eclipseplugin.internal.exception;

public class ComponentLookupNotFoundException extends Exception {
    public ComponentLookupNotFoundException() {
        super();
    }

    public ComponentLookupNotFoundException(String message) {
        super(message);
    }

    public ComponentLookupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentLookupNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ComponentLookupNotFoundException(Throwable cause) {
        super(cause);
    }
}
