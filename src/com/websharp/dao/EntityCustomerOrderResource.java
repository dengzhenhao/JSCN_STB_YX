package com.websharp.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_CUSTOMER_ORDER_RESOURCE.
 */
public class EntityCustomerOrderResource {

    public String PROD_ORDER_ID;
    public String RES_CODE;
    public String RES_NAME;
    public String RES_EQU_NO;
    public String VALID_DATE;
    public String RES_USE_MODE;
    public String STATE;

    public EntityCustomerOrderResource() {
    }

    public EntityCustomerOrderResource(String PROD_ORDER_ID, String RES_CODE, String RES_NAME, String RES_EQU_NO, String VALID_DATE, String RES_USE_MODE, String STATE) {
        this.PROD_ORDER_ID = PROD_ORDER_ID;
        this.RES_CODE = RES_CODE;
        this.RES_NAME = RES_NAME;
        this.RES_EQU_NO = RES_EQU_NO;
        this.VALID_DATE = VALID_DATE;
        this.RES_USE_MODE = RES_USE_MODE;
        this.STATE = STATE;
    }

    public String getPROD_ORDER_ID() {
        return PROD_ORDER_ID;
    }

    public void setPROD_ORDER_ID(String PROD_ORDER_ID) {
        this.PROD_ORDER_ID = PROD_ORDER_ID;
    }

    public String getRES_CODE() {
        return RES_CODE;
    }

    public void setRES_CODE(String RES_CODE) {
        this.RES_CODE = RES_CODE;
    }

    public String getRES_NAME() {
        return RES_NAME;
    }

    public void setRES_NAME(String RES_NAME) {
        this.RES_NAME = RES_NAME;
    }

    public String getRES_EQU_NO() {
        return RES_EQU_NO;
    }

    public void setRES_EQU_NO(String RES_EQU_NO) {
        this.RES_EQU_NO = RES_EQU_NO;
    }

    public String getVALID_DATE() {
        return VALID_DATE;
    }

    public void setVALID_DATE(String VALID_DATE) {
        this.VALID_DATE = VALID_DATE;
    }

    public String getRES_USE_MODE() {
        return RES_USE_MODE;
    }

    public void setRES_USE_MODE(String RES_USE_MODE) {
        this.RES_USE_MODE = RES_USE_MODE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

}
