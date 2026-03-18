package com.example.teamStack.exceptions;

public class DuplicateEmailFound extends RuntimeException{
   public DuplicateEmailFound(){}
    public DuplicateEmailFound(String msg){
        super(msg);
    }
}
