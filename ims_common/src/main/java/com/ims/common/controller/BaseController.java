package com.ims.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BaseController {

    public HttpServletRequest request;
    public HttpServletResponse response;
    protected String companyId="1";
    protected String companyName="IMS";
    protected String userId;

    @ModelAttribute
    public void serResAndReq(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        this.request = request;
        this.response = response;

        if (request.getHeader("userId")!=null){
            this.userId = request.getHeader("userId");
        }
        if (request.getHeader("companyId")!=null){
            this.companyId = request.getHeader("companyId");
        }
        if (request.getHeader("companyName")!=null){
            this.companyName = request.getHeader("companyName");
        }
    }
}
