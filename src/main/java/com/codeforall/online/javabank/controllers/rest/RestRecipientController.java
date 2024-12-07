package com.codeforall.online.javabank.controllers.rest;

import com.codeforall.online.javabank.converters.RecipientDtoToRecipient;
import com.codeforall.online.javabank.converters.RecipientToRecipientDto;
import com.codeforall.online.javabank.command.RecipientDto;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.services.CustomerService;
import com.codeforall.online.javabank.services.RecipientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


/**
 * A REST API Recipient Controller responsible for rendering {@link Recipient} related views
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestRecipientController {

    private CustomerService customerService;
    private RecipientService recipientService;
    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtoToRecipient;

    /**
     * Handles HTTP GET requests to retrieve a list of recipients associated with a specific customer.
     * @param cid the ID of the customer whose recipients are to be retrieved
     * @return a {@link ResponseEntity} containing a list of {@link RecipientDto} objects if the customer exists,
     *         or a 404 (Not Found) status if the customer with the specified ID is not found
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{cid}/recipient", "/{cid}/recipient/"})
    public ResponseEntity<List<RecipientDto>> listRecipients(@PathVariable Integer cid) {

        try {

            return new ResponseEntity<>(recipientToRecipientDto.convert(recipientService.getCustomerRecipients(cid)), HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP GET requests to retrieve a specific recipient associated with a given customer.
     * @param cid the ID of the customer to whom the recipient should be associated
     * @param rid the ID of the recipient to be retrieved
     * @return a {@link ResponseEntity} containing the {@link RecipientDto} object if the recipient exists and is associated
     *         with the specified customer, or a 404 (Not Found) status if the recipient does not exist, or if the customer does not exist,
     *         or a 400 (Bad Request) with the recipient is not associated with the specified customer.
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{cid}/recipient/{rid}","/{cid}/recipient/{rid}/"})
    public ResponseEntity<RecipientDto> showRecipient(@PathVariable Integer cid, @PathVariable Integer rid) {

        try {
            Recipient recipient = recipientService.getRecipient(rid);

            Customer customer = customerService.get(cid);

            if (recipient.getCustomer() != customer) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(recipientToRecipientDto.convert(recipient), HttpStatus.OK);

        } catch (CustomerNotFoundException | RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP POST requests to create a new recipient associated with a specified customer.
     * @param cid the ID of the customer to which the recipient should be associated
     * @param recipientDto the {@link RecipientDto} object containing the details of the recipient to be created
     * @param bindingResult holds validation results for the {@code recipientDto}
     * @param uriComponentsBuilder used to construct the URI of the newly created recipient
     * @return a {@link ResponseEntity} with a {@code Location} header pointing to the URI of the newly created recipient
     *         and a 201 (Created) status if the recipient is successfully created, or a 400 (Bad Request) status if the request
     *         is invalid, or a 404 (Not Found) status if the specified customer or recipient does not exist or the account number
     *         belongs to the customer itself
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient", "/{cid}/recipient/"})
    public ResponseEntity<?> addRecipient(@PathVariable Integer cid, @Valid @RequestBody RecipientDto recipientDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || recipientDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Recipient recipient = customerService.addRecipient(cid, recipientDtoToRecipient.convert(recipientDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + cid + "/recipient/" + recipient.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (AccountNotFoundException | CustomerNotFoundException | RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP PUT requests to update an existing recipient associated with a specified customer.
     * @param cid the ID of the customer to which the recipient is associated
     * @param rid the ID of the recipient to be updated
     * @param recipientDto the {@link RecipientDto} object containing the updated details of the recipient
     * @param bindingResult holds validation results for the {@code recipientDto}
     * @return a {@link ResponseEntity} with a 200 (OK) status if the recipient is successfully updated,
     *         or a 400 (Bad Request) status if the request is invalid (e.g., validation errors or mismatched IDs),
     *         or a 404 (Not Found) status if the specified customer or account does not exist
     */
    @RequestMapping(method = RequestMethod.PUT, path = {"/{cid}/recipient/{rid}", "/{cid}/recipient/{rid}/"})
    public ResponseEntity<RecipientDto> editRecipient(@PathVariable Integer cid, @PathVariable Integer rid, @Valid @RequestBody RecipientDto recipientDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            if (recipientDto.getId() != null && recipientDto.getId() != rid) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            recipientDto.setId(rid);

            Recipient savedRecipient = recipientDtoToRecipient.convert(recipientDto);
            customerService.addRecipient(cid, savedRecipient);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException | AccountNotFoundException | RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP DELETE requests to remove a specific recipient associated with a given customer.
     * @param cid the ID of the customer associated with the recipient to be deleted
     * @param id the ID of the recipient to be deleted
     * @return a {@link ResponseEntity} with a 204 (No Content) status if the recipient is successfully deleted,
     *         or a 404 (Not Found) status if the recipient or customer does not exist
     */
    @RequestMapping(method = RequestMethod.DELETE, path = {"/{cid}/recipient/{id}", "/{cid}/recipient/{id}/"})
    public ResponseEntity<?> deleteRecipient(@PathVariable Integer cid, @PathVariable Integer id) {

        try {

            customerService.removeRecipient(cid, id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CustomerNotFoundException | RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Set the customer service
     * @param customerService to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Set the recipient service
     * @param recipientService to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    /**
     * Set the recipient to recipient dto converter
     * @param recipientToRecipientDto to set
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    /**
     * Set the recipientDto to recipient converter
     * @param recipientDtoToRecipient to set
     */
    @Autowired
    public void setRecipientDtoToRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtoToRecipient = recipientDtoToRecipient;
    }
}
