package com.upwork.magnus.api.core;

import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public interface BaseService {
    boolean validate();
    void process();
    Response response();
}
