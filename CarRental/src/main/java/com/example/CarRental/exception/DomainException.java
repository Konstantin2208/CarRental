package com.example.CarRental.exception;

public class DomainException extends RuntimeException{
    public DomainException(String massage){
        super(massage);
    }
}