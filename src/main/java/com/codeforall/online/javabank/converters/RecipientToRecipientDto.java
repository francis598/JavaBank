package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.RecipientDto;
import com.codeforall.online.javabank.model.Recipient;

import org.springframework.stereotype.Component;

/**
 * A concrete converter class that transforms an {@link Recipient} entity into an {@link RecipientDto}.
 */
@Component
public class RecipientToRecipientDto extends AbstractConverter<Recipient, RecipientDto> {

    /**
     * Convert a recipient into a recipientDto
     * @param recipient to take the info out of
     * @return the recipientDto
     */
    @Override
    public RecipientDto convert(Recipient recipient) {

        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setId(recipient.getId());
        recipientDto.setAccountNumber(recipient.getAccountNumber());
        recipientDto.setName(recipient.getName());
        recipientDto.setDescription(recipient.getDescription());

        return recipientDto;
    }
}
