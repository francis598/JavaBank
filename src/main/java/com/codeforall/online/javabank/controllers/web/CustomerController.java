package com.codeforall.online.javabank.controllers.web;

import com.codeforall.online.javabank.command.*;
import com.codeforall.online.javabank.converters.*;
import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.AssociationExistsException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Controller responsible for rendering {@link Customer} related views
 */
@Controller
@SessionAttributes("customerId")
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;
    private CustomerToListCustomerDto customerToListCustomerDto;
    private CustomerDtoToCustomer customerDtoToCustomer;
    private CustomerToCustomerDto customerToCustomerDto;
    private CustomerToProfileCustomerDto customerToProfileCustomerDto;
    private RecipientToRecipientDto recipientToRecipientDto;
    private AccountToAccountDto accountToAccountDto;
    private AddressDtoToAddress addressDtoToAddress;

    /**
     * Initialises the value to be stored in the session for current customer
     * @return 0 to symbolize that no customer has being selected yet
     */
    @ModelAttribute("customerId")
    public int getCurrentCustomerId() {
        return 0;
    }

    /**
     * Render a view with a list of customers
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {

        try {
            model.addAttribute("customers", customerToListCustomerDto.convert(customerService.list()));

        } catch (CustomerNotFoundException | AccountNotFoundException e) {
            // do nothing, just show the customers that exist
        }

        return "index";
    }

    /**
     * Add a customer
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new CustomerDto());

        return "add-update";
    }

    /**
     * Edit a customer
     * @param id of the customer to edit
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/edit")
    public String editCustomer(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

        try {
            model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(id)));

            return "add-update";

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "No customer to edit");

            return "redirect:/customer/list";
        }
    }

    /**
     * Render a view with customer details
     * @param id    the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String showCustomer(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

        try {
            Customer customer = customerService.get(id);
            double balance = customerService.getBalance(id);

            customer.setTotalBalance(balance);

            ProfileCustomerDto customerDto = customerToProfileCustomerDto.convert(customer);
            List<RecipientDto> recipients = recipientToRecipientDto.convert(customer.getRecipients().stream().toList());
            List<Account> accounts = new ArrayList<>(customer.getAccounts());
            accounts.sort(Comparator.comparingInt(Account::getId));

            List<AccountDto> accountDtos =  accountToAccountDto.convert(accounts);

            model.addAttribute("customer", customerDto);
            model.addAttribute("accounts", accountDtos);
            model.addAttribute("recipients", recipients);

            // add DTOs for the modals
            model.addAttribute("account", new AccountDto());
            model.addAttribute("recipient", new RecipientDto());
            model.addAttribute("transaction", new TransactionDto());
            model.addAttribute("transfer", new TransferDto());

            model.addAttribute("customerId", id);

            return "profile";

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure", "Customer Not Found");

            return "redirect:/customer/list";
        }
    }

    /**
     * Save the customer form submission and renders a view with the customer details
     * @param customerDto the customer form object
     * @param bindingResult the result of validating the {@link CustomerDto} object.
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/", ""})
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerDto customerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            return "add-update";
        }

        try {

            Customer customer = customerDtoToCustomer.convert(customerDto);

            Customer savedCustomer = customerService.add(customer, addressDtoToAddress.convert(customerDto.getAddressDto()));

            redirectAttributes.addFlashAttribute("lastAction", "Added " + savedCustomer.getFirstName() + " " + savedCustomer.getLastName() + " successfully!");

            return "redirect:/customer/list";

        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("failure","Customer doesn't exists");

            return "redirect:/customer/list";
        }
    }

    /**
     * Delete a customer
     * @param id of the customer to delete
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            Customer customer = customerService.get(id);

            customerService.delete(id);

            redirectAttributes.addFlashAttribute("lastAction", "Deleted " + customer.getFirstName() +
                    " " + customer.getLastName());

            return "redirect:/customer/list";

        } catch (AssociationExistsException ex) {
            redirectAttributes.addFlashAttribute("failure","Customer contains accounts.");

            return "redirect:/customer/list";

        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("failure","Customer doesn't exist");

            return "redirect:/customer/list";
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
     * Set the customerToListCustomerDto converter
     * @param customerToListCustomerDto to set
     */
    @Autowired
    public void setCustomerToListCustomerDto(CustomerToListCustomerDto customerToListCustomerDto) {
        this.customerToListCustomerDto = customerToListCustomerDto;
    }

    /**
     * Set the CustomerDtoToCustomer converter
     * @param customerDtoToCustomer to set
     */
    @Autowired
    public void setCustomerDtoToCustomer(CustomerDtoToCustomer customerDtoToCustomer) {
        this.customerDtoToCustomer = customerDtoToCustomer;
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
     * Set the customerToProfileCustomerDto converter
     * @param customerToProfileCustomerDto to set
     */
    @Autowired
    public void setCustomerToProfileCustomerDto(CustomerToProfileCustomerDto customerToProfileCustomerDto) {
        this.customerToProfileCustomerDto = customerToProfileCustomerDto;
    }

    /**
     * Set the recipientToRecipientDto converter
     * @param recipientToRecipientDto to set
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
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
     * Set the addressDtoToAddress converter
     * @param addressDtoToAddress to set
     */
    @Autowired
    public void setAddressDtoToAddress(AddressDtoToAddress addressDtoToAddress) {
        this.addressDtoToAddress = addressDtoToAddress;
    }
}
