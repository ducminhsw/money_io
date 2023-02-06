package com.example.moneyio.item.home_item;

import java.util.HashMap;
import java.util.Map;

public class ExpenseItem {
    private int ExpenseId;
    private int ExpenseImg;
    private String ExpenseName;
    private String ExpenseDate;
    private int ExpenseMoney;
    private int ExpenseType;

    public ExpenseItem() {
    }

    public ExpenseItem(int expenseId, int expenseImg, String expenseName, String expenseDate, int expenseMoney, int expenseType) {
        ExpenseId = expenseId;
        ExpenseImg = expenseImg;
        ExpenseName = expenseName;
        ExpenseDate = expenseDate;
        ExpenseMoney = expenseMoney;
        ExpenseType = expenseType;
    }

    public int getExpenseId() {
        return ExpenseId;
    }

    public void setExpenseId(int expenseId) {
        ExpenseId = expenseId;
    }

    public int getExpenseImg() {
        return ExpenseImg;
    }

    public void setExpenseImg(int expenseImg) {
        ExpenseImg = expenseImg;
    }

    public String getExpenseName() {
        return ExpenseName;
    }

    public void setExpenseName(String expenseName) {
        ExpenseName = expenseName;
    }

    public String getExpenseDate() {
        return ExpenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        ExpenseDate = expenseDate;
    }

    public int getExpenseMoney() {
        return ExpenseMoney;
    }

    public void setExpenseMoney(int expenseMoney) {
        ExpenseMoney = expenseMoney;
    }

    public int getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(int expenseType) {
        ExpenseType = expenseType;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("expenseDate", ExpenseDate);
        result.put("expenseId", ExpenseId);
        result.put("expenseImg", ExpenseImg);
        result.put("expenseMoney", ExpenseMoney);
        result.put("expenseName", ExpenseName);
        result.put("expenseType", ExpenseType);

        return result;
    }
}
