package com.codeforall.online.javabank.exceptions;

import com.codeforall.online.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that the recipient was not found
 */
public class RecipientNotFoundException extends JavaBankException {

        /**
         * @see JavaBankException#JavaBankException(String)
         */
        public RecipientNotFoundException() {
            super(ErrorMessage.RECIPIENT_NOT_FOUND);
        }
}
