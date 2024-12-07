package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.RecipientDto;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A converter class which converts {@link RecipientDto} objects into {@link Recipient} objects
 */
@Component
public class RecipientDtoToRecipient {

    private RecipientService recipientService;

    /**
     * Convert recipientDto into recipient
     * @param recipientDto to take info out of
     * @return the recipient
     * @throws RecipientNotFoundException if the recipient is not found
     */
    public Recipient convert(RecipientDto recipientDto) throws RecipientNotFoundException {

        Recipient recipient = (recipientDto.getId() != null ? recipientService.getRecipient(recipientDto.getId()) : new Recipient());

        recipient.setAccountNumber(recipientDto.getAccountNumber());
        recipient.setName(recipientDto.getName());
        recipient.setDescription(recipientDto.getDescription());

        return recipient;
    }

    /**
     * Set the recipient service
     * @param recipientService to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }
}
