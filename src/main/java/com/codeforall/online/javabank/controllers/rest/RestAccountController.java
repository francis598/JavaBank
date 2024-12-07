package com.codeforall.online.javabank.controllers.rest;

import com.codeforall.online.javabank.converters.AccountDtoToAccount;
import com.codeforall.online.javabank.converters.AccountToAccountDto;
import com.codeforall.online.javabank.command.AccountDto;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.services.AccountService;
import com.codeforall.online.javabank.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;

/**
 * A REST API Account Controller responsible for rendering {@link Account} related views
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestAccountController {

    private CustomerService customerService;
    private AccountService accountService;
    private AccountToAccountDto accountToAccountDto;
    private AccountDtoToAccount accountDtoToAccount;

    /**
     * Handles HTTP GET requests to retrieve a list of accounts for a specific customer.
     * @param cid the ID of the customer whose accounts are to be retrieved
     * @return a {@link ResponseEntity} containing a list of {@link AccountDto} objects
     *         and an HTTP status code. The status code is 200 (OK) if the customer is found,
     *         or 404 (Not Found) if the customer does not exist.
     */
    @RequestMapping(method = RequestMethod.GET, path ={ "/{cid}/account","/{cid}/account/"})
    public ResponseEntity<List<AccountDto>> listCustomerAccounts(@PathVariable Integer cid) {

        try {
            Customer customer = customerService.get(cid);

            List<AccountDto> accountDtos = accountToAccountDto.convert(customer.getAccounts()
                    .stream()
                    .sorted(Comparator.comparingInt(Account::getId)).toList());

            return new ResponseEntity<>(accountDtos, HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP GET requests to retrieve a specific account of a customer.
     * @param cid the ID of the customer
     * @param aid the ID of the account to retrieve
     * @return a {@link ResponseEntity} containing the {@link AccountDto} for the
     *         specified account and an HTTP status code. The status code is 200 (OK)
     *         if the account belongs to the customer, or 400 (Bad Request) if the account
     *         does not belong to the customer or 404 (Not Found) if the account does not exist.
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{cid}/account/{aid}", "/{cid}/account/{aid}/" })
    public ResponseEntity<AccountDto> showCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {
            Account account = accountService.get(aid);

            if(account.getCustomers().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Check if the account belongs to the described customer
            if (account.getCustomers().stream().noneMatch(c -> c.getId() == cid)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(accountToAccountDto.convert(account), HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP POST requests to create a new account for a specified customer.
     * @param cid the ID of the customer for whom the account is to be created
     * @param accountDto the {@link AccountDto} object containing account details. This should not include an ID
     *        as IDs are generated automatically during creation.
     * @param bindingResult holds the result of the validation and binding of the {@link AccountDto}. If there
     *        are validation errors, a 400 (Bad Request) status will be returned.
     * @param uriComponentsBuilder used to construct the URI of the newly created account for inclusion in the Location header.
     * @return a {@link ResponseEntity} containing headers and HTTP status code. Returns 201 (Created) with a
     *         Location header if the account is successfully created, or 400 (Bad Request) if there are validation
     *         errors or the request is invalid, or 404 (Not Found) if the specified customer does not exist.
     */
    @RequestMapping(method = RequestMethod.POST, path = { "/{cid}/account", "/{cid}/account/"})
    public ResponseEntity<?> addAccount(@PathVariable Integer cid, @Valid @RequestBody AccountDto accountDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || accountDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Account account = customerService.openAccount(cid, accountDtoToAccount.convert(accountDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + cid + "/account/" + account.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles HTTP DELETE requests to close an account for a specified customer.
     * @param cid the ID of the customer whose account is to be closed
     * @param aid the ID of the account to be closed
     * @return a {@link ResponseEntity} containing the HTTP status code. Returns 200 (OK) if the account is successfully
     *         closed, or 404 (Not Found) if either the customer or account does not exist, or 400 (Bad Request) if the
     *         account balance is different from 0.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = {"/{cid}/account/{aid}", "/{cid}/account/{aid}/"})
    public ResponseEntity<?> closeAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {

            customerService.closeAccount(cid, aid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException | AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
     * Set the account service
     * @param accountService to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Set the accountToAccountDto converter
     * @param accountToAccountDto to set
     */
    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }

    /**
     * Set the accountDtoToAccount converter
     * @param accountDtoToAccount to set
     */
    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }
}


