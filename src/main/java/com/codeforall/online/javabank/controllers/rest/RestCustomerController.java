package com.codeforall.online.javabank.controllers.rest;

import com.codeforall.online.javabank.command.CustomerDto;
import com.codeforall.online.javabank.converters.CustomerDtoToCustomer;
import com.codeforall.online.javabank.converters.CustomerToCustomerDto;
import com.codeforall.online.javabank.command.BaseCustomerDto;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.services.CustomerService;
import com.codeforall.online.javabank.exceptions.AssociationExistsException;
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
 * A REST API Customer Controller responsible for rendering {@link Customer} related views
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestCustomerController {

    private CustomerService customerService;
    private CustomerToCustomerDto customerToCustomerDto;
    private CustomerDtoToCustomer customerDtoToCustomer;

    /**
     * Handles HTTP GET requests to retrieve a list of all customers.
     *
     * @return A {@link ResponseEntity} containing a list of {@link CustomerDto} objects and an HTTP status code:
     * - HTTP 200 OK if the customer list is successfully retrieved and converted.
     * - HTTP 404 Not Found if there is an issue with retrieving customer data.
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/", ""})
    public ResponseEntity<List<CustomerDto>> listCustomers() {

        try {
            return new ResponseEntity<>(customerToCustomerDto.convert(customerService.list()), HttpStatus.OK);

        } catch (CustomerNotFoundException | AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP GET requests to retrieve a specific customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return A {@link ResponseEntity} containing the {@link CustomerDto} representing the customer
     * and an HTTP status code:
     * - HTTP 200 OK if the customer is successfully retrieved and converted.
     * - HTTP 404 Not Found if the customer with the specified ID does not exist.
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{id}", "/{id}/"})
    public ResponseEntity<CustomerDto> showCustomer(@PathVariable Integer id) {
        try {
            Customer customer = customerService.get(id);

            return new ResponseEntity<>(customerToCustomerDto.convert(customer), HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP POST requests to create a new customer.
     * @param customerDto The {@link CustomerDto} containing the details of the customer to be created.
     * @param bindingResult The result of validating the {@link CustomerDto} object.
     * @param uriComponentsBuilder A builder used to create the URI of the newly created resource.
     * @return A {@link ResponseEntity} containing HTTP headers with the location of the newly created resource and an HTTP status code:
     *         - HTTP 201 Created if the customer is successfully created and the location is set in the response headers.
     *         - HTTP 400 Bad Request if the input data is invalid or the DTO contains an ID, because the customer already exists.
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/", ""})
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDto customerDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || customerDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Customer customer = customerDtoToCustomer.convert(customerDto);


            Customer savedCustomer = customerService.add(customer, customer.getAddress());

            // get help from the framework building the path for the newly created resource
            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + savedCustomer.getId()).build();

            // set headers with the created path
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles HTTP PUT requests to update an existing customer.
     * @param customerDto The {@link CustomerDto} containing the updated customer details.
     * @param bindingResult The result of validating the {@link CustomerDto} object.
     * @param id The ID of the customer to be updated, provided as a path variable.
     * @return A {@link ResponseEntity} with an HTTP status code:
     *         - HTTP 200 OK if the customer is successfully updated.
     *         - HTTP 400 Bad Request if there are validation errors or if the ID in the provided DTO does not match the path variable ID.
     *         - HTTP 404 Not Found if the customer with the specified ID does not exist.
     */
    @RequestMapping(method = RequestMethod.PUT, path = {"/{id}", "/{id}/"})
    public ResponseEntity<BaseCustomerDto> editCustomer(@Valid @RequestBody CustomerDto customerDto, BindingResult bindingResult, @PathVariable Integer id) {
        
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        if (customerDto.getId() != null && !customerDto.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            customerDto.setId(id);
            
            Customer customer = customerDtoToCustomer.convert(customerDto);
            
            customerService.add(customer, customer.getAddress());
            
            return new ResponseEntity<>(HttpStatus.OK);
            
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP DELETE requests to delete a customer by their ID.
     * @param id The ID of the customer to be deleted, provided as a path variable.
     * @return A {@link ResponseEntity} with an HTTP status code:
     *         - HTTP 204 No Content if the customer is successfully deleted.
     *         - HTTP 400 Bad Request if there are associations that prevent the deletion.
     *         - HTTP 404 Not Found if the customer with the specified ID does not exist.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = {"/{id}", "/{id}/"})
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {

        try {
            customerService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (AssociationExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (CustomerNotFoundException e) {
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
     * Set the customerToCustomerDto converter
     * @param customerToCustomerDto to set
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Set the CustomerDtoToCustomer converter
     * @param customerDtoToCustomer to set
     */
    @Autowired
    public void setCustomerDtoToCustomer(CustomerDtoToCustomer customerDtoToCustomer) {
        this.customerDtoToCustomer = customerDtoToCustomer;
    }
}
