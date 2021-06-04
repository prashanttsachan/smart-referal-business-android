package com.example.ylifebsb;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class transaction_Data implements Serializable {

    String type, status, remarks, member, amount, balance, createdAt, updatedAt, id;

    @Override
    public String toString() {
      return   "transaction_Data {" +
                "type='" + type +'\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", member='" + member + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}