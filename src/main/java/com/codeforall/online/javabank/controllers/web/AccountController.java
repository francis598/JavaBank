package com.codeforall.online.javabank.controllers.web;

import com.codeforall.online.javabank.command.AccountDto;
import com.codeforall.online.javabank.command.TransactionDto;
import com.codeforall.online.javabank.command.TransferDto;
import com.codeforall.online.javabank.converters.AccountDtoToAccount;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.model.account.AccountType;
import com.codeforall.online.javabank.services.AccountService;
import com.codeforall.online.javabank.services.CustomerService;
import com.codeforall.online.javabank.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for rendering {@link Account} related views
 */
@Controller
@SessionAttributes("customerId")
@RequestMapping("/account")
public class AccountController {

    private CustomerService customerService;
    private AccountService accountService;
    private TransactionService transactionService;
    private AccountDtoToAccount accountDtoToAccount;


    /**
     * Add an account to a specific customer
     * @param cid the customer's id
     * @param accountDto the account data transfer object
     * @param bindingResult for error registration capabilities, allowing for a Validator to be applied
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the customer view
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/add"})
    public String addAccount(@PathVariable Integer cid, @Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure","Account creation failed. Balance must be at least 100");
            return redirectToCustomerPage(cid);
        }

        try {
            Account account = accountDtoToAccount.convert(accountDto);
            customerService.openAccount(cid, account);

            redirectAttributes.addFlashAttribute("lastAction", "Created " + account.getAccountType() + " account.");

            return redirectToCustomerPage(cid);

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Customer does not exist");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Deposit money into a specific account of a specific customer
     * @param cid the customer's id
     * @param transactionDto the transaction data transfer object
     * @param bindingResult for error registration capabilities, allowing for a Validator to be applied
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the customer view
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/deposit"})
    public String deposit(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Deposit failed. Please use valid information.");

            return redirectToCustomerPage(cid);
        }

        try {
            accountService.deposit(transactionDto.getAccountId(), Double.parseDouble(transactionDto.getAmount()));

            redirectAttributes.addFlashAttribute("lastAction", "Deposited " + transactionDto.getAmount() + "€ into account #" + transactionDto.getAccountId());

            return redirectToCustomerPage(cid);

        } catch (AccountNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "This account does not exist");

            return redirectToCustomerPage(cid);

        } catch (TransactionInvalidException e) {
            redirectAttributes.addFlashAttribute("failure", "You can't deposit 0.00€");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Withdraw money from a specific account of a specific customer
     * @param cid the customer's id
     * @param transactionDto the transaction data transfer object
     * @param bindingResult for error registration capabilities, allowing for a Validator to be applied
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the customer view
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/withdraw"})
    public String withdraw(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Withdraw failed. Please use valid information.");
            return redirectToCustomerPage(cid);
        }

        try {
            accountService.withdraw(transactionDto.getAccountId(), Double.parseDouble(transactionDto.getAmount()));

            redirectAttributes.addFlashAttribute("lastAction", "Withdrew " + transactionDto.getAmount() + "€ from account #" + transactionDto.getAccountId());

            return redirectToCustomerPage(cid);

        } catch (TransactionInvalidException ex) {
            handleInvalidOperationsOnWithdraw(transactionDto, redirectAttributes);

            return redirectToCustomerPage(cid);

        } catch (AccountNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "This account does not exist");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Transfer money to an account
     * @param cid the sender customer's id
     * @param transferDto the transfer data transfer object
     * @param bindingResult for error registration capabilities, allowing for a Validator to be applied
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the sender customer's view
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/transfer"})
    public String transferToAccount(@PathVariable Integer cid, @Valid @ModelAttribute("transfer") TransferDto transferDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Transfer failed. Please use valid information.");

            return redirectToCustomerPage(cid);
        }

        try {
            accountService.transfer(transferDto.getSrcId(), transferDto.getDstId(), Double.parseDouble(transferDto.getAmount()), cid);

            redirectAttributes.addFlashAttribute("lastAction", "Account #" + transferDto.getSrcId() + " transferred " + transferDto.getAmount() + "€ to account #" + transferDto.getDstId());

            return redirectToCustomerPage(cid);

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform transaction: value above the allowed amount");

            return redirectToCustomerPage(cid);

        } catch (AccountNotFoundException ex) {
            redirectAttributes.addFlashAttribute("failure", "Account not found. Source account must be yours or Destination Account might not exist");

            return redirectToCustomerPage(cid);

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Customer not found");

            return redirectToCustomerPage(cid);

        } catch (RecipientNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Recipient not found");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Close an account
     * @param cid the customer's id
     * @param aid the id of the account to be closed
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the customer's page
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/close/{aid}")
    public String closeAccount(@PathVariable Integer cid, @PathVariable Integer aid, RedirectAttributes redirectAttributes) {

        try {
            customerService.closeAccount(cid, aid);
            redirectAttributes.addFlashAttribute("lastAction", "Closed account " + aid);

            return redirectToCustomerPage(cid);

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform closing operation. Account # " + aid + " still has funds");

            return redirectToCustomerPage(cid);

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Customer not found");

            return redirectToCustomerPage(cid);

        } catch (AccountNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Account not found");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Render a view with a list of transactions connected to an account
     * @param id the account id
     * @param model the model object
     * @param redirectAttributes to select attributes for a redirect scenario
     * @return the account statement view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{id}"})
    public String listTransactions(@PathVariable Integer id, @ModelAttribute("customerId") int cid, Model model, RedirectAttributes redirectAttributes) {

        try {
            model.addAttribute("transactions", transactionService.getAccountStatement(id));

            return "account-statement";

        } catch (AccountNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Account not found");

            return redirectToCustomerPage(cid);
        }
    }

    /**
     * Redirect to a specific customer page
     * @param cid the customer's id
     * @return a string with the redirected url
     */
    private String redirectToCustomerPage(Integer cid) {
        return "redirect:/customer/" + cid;
    }

    /**
     * Handle invalid operations on withdraw
     * @param transactionDto to inform the account id
     * @param redirectAttributes to create redirect attribute messages
     */
    private void handleInvalidOperationsOnWithdraw(TransactionDto transactionDto, RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.get(transactionDto.getAccountId());
            boolean isSavings = account.getAccountType().equals(AccountType.SAVINGS);

            String noWithdrawOnSavingsMessage = "You cannot withdraw from a savings account";
            String overBalanceMessage = "Withdraw failed. " + transactionDto.getAmount()
                    + "€ is over the current balance for account #" + transactionDto.getAccountId();

            String flashMessage = isSavings ? noWithdrawOnSavingsMessage : overBalanceMessage;

            redirectAttributes.addFlashAttribute("failure", flashMessage);

        } catch (AccountNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "This account does not exist");
        }
    }

    /**
     * Set the customer service
     * @param customerService the customer service to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Set the account service
     * @param accountService the account service to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Set the transaction service
     * @param transactionService the transaction service to set
     */
    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Set the converter of AccountDtoToAccount
     * @param accountDtoToAccount to set
     */
    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }
}
