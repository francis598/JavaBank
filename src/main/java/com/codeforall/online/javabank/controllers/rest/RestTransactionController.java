package com.codeforall.online.javabank.controllers.rest;

import com.codeforall.online.javabank.command.TransactionDto;
import com.codeforall.online.javabank.command.TransferDto;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.services.AccountService;
import com.codeforall.online.javabank.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * A REST API Transaction Controller responsible for handling account operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestTransactionController {

    private AccountService accountService;
    private CustomerService customerService;

    /**
     * Handles HTTP PUT requests to transfer funds from one account to another.
     * @param cid The ID of the customer initiating the transfer.
     * @param transferDto The {@link TransferDto} containing the source account ID, destination account ID, and the transfer amount.
     * @param bindingResult Contains validation errors if any occurred during the validation of {@code transferDto}.
     * @return A {@link ResponseEntity} indicating the result of the transfer operation:
     *         - HTTP 200 OK if the transfer is successful.
     *         - HTTP 400 Bad Request if there are validation errors or the transfer is invalid
     *              or if the specified customer doesn't own the source account.
     *         - HTTP 404 Not Found if any account, customer, or recipient is not found.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/transfer")
    public ResponseEntity<TransferDto> transfer(@PathVariable Integer cid, @Valid @RequestBody TransferDto transferDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Account srcAccount = accountService.get(transferDto.getSrcId());

            if(srcAccount.getCustomers().stream().noneMatch(c -> c.getId() == cid)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            accountService.transfer(transferDto.getSrcId(), transferDto.getDstId(), Double.parseDouble(transferDto.getAmount()), cid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException | CustomerNotFoundException | RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles HTTP PUT requests to deposit funds into a specified account.
     * @param cid The ID of the customer making the deposit.
     * @param transactionDto The {@link TransactionDto} containing the account ID and the deposit amount.
     * @param bindingResult Contains validation errors if any occurred during the validation of {@code transactionDto}.
     * @return A {@link ResponseEntity} indicating the result of the deposit operation:
     *         - HTTP 200 OK if the deposit is successful.
     *         - HTTP 400 Bad Request if there are validation errors or the deposit is invalid.
     *         - HTTP 404 Not Found if the account or customer is not found.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/deposit")
    public ResponseEntity<TransactionDto> deposit(@PathVariable Integer cid, @Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Customer customer = customerService.get(cid);
            Account account = accountService.get(transactionDto.getAccountId());

            if(account.getCustomers().stream().noneMatch(c -> c.equals(customer))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            accountService.deposit(transactionDto.getAccountId(), Double.parseDouble(transactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException | CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles HTTP PUT requests to withdraw funds from a specified account.
     * @param cid The ID of the customer making the withdrawal.
     * @param transactionDto The {@link TransactionDto} containing the account ID and the withdrawal amount.
     * @param bindingResult Contains validation errors if any occurred during the validation of {@code transactionDto}.
     * @return A {@link ResponseEntity} indicating the result of the withdrawal operation:
     *         - HTTP 200 OK if the withdrawal is successful.
     *         - HTTP 400 Bad Request if there are validation errors or the withdrawal is invalid.
     *         - HTTP 404 Not Found if the account or customer is not found.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{cid}/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@PathVariable Integer cid, @Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Customer customer = customerService.get(cid);
            Account account = accountService.get(transactionDto.getAccountId());

            if(account.getCustomers().stream().noneMatch(c -> c.equals(customer))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            accountService.withdraw(transactionDto.getAccountId(), Double.parseDouble(transactionDto.getAmount()));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AccountNotFoundException | CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
     * Set the customer service
     * @param customerService to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
