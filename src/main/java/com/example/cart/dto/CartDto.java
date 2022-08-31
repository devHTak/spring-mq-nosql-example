package com.example.cart.dto;

public class CartDto {
    private int count;

    private String memberId;

    private String itemId;

    public CartDto() {}

    public CartDto(int count, String memberId, String itemId) {
        this.count = count;
        this.memberId = memberId;
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
