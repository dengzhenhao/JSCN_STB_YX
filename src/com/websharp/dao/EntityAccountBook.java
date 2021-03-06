package com.websharp.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_ACCOUNT_BOOK.
 */
public class EntityAccountBook {

    public String acctBalanceId;
    public String balanceTypeId;
    public String balanceTypeName;
    public String balance;
    public String effDate;
    public String expDate;
    public String accountInfo_Id;

    public EntityAccountBook() {
    }

    public EntityAccountBook(String acctBalanceId, String balanceTypeId, String balanceTypeName, String balance, String effDate, String expDate, String accountInfo_Id) {
        this.acctBalanceId = acctBalanceId;
        this.balanceTypeId = balanceTypeId;
        this.balanceTypeName = balanceTypeName;
        this.balance = balance;
        this.effDate = effDate;
        this.expDate = expDate;
        this.accountInfo_Id = accountInfo_Id;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getBalanceTypeId() {
        return balanceTypeId;
    }

    public void setBalanceTypeId(String balanceTypeId) {
        this.balanceTypeId = balanceTypeId;
    }

    public String getBalanceTypeName() {
        return balanceTypeName;
    }

    public void setBalanceTypeName(String balanceTypeName) {
        this.balanceTypeName = balanceTypeName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getAccountInfo_Id() {
        return accountInfo_Id;
    }

    public void setAccountInfo_Id(String accountInfo_Id) {
        this.accountInfo_Id = accountInfo_Id;
    }

}
