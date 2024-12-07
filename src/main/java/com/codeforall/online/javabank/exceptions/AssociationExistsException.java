package com.codeforall.online.javabank.exceptions;

import com.codeforall.online.javabank.errors.ErrorMessage;

public class AssociationExistsException extends JavaBankException{

    public AssociationExistsException() {
        super(ErrorMessage.ASSOCIATION_EXISTS);
    }
}
