package com.example.program.util.exception;

public class SimpleDesktopException extends RuntimeException{
    public SimpleDesktopException(String msg, Class clazz){
        super(msg);
        System.out.println("Error occurred in " + clazz.getName());
    }

}
