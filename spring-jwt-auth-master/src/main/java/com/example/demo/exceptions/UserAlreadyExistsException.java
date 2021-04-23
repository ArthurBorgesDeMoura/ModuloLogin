package com.example.demo.exceptions;

public class UserAlreadyExistsException extends Exception {
  public String message;
  public String usuario;

  public UserAlreadyExistsException(String usuario){
    this.usuario = usuario;
    this.message = "Este usuário já esta em uso";
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return message + " " + usuario;
  }

}
