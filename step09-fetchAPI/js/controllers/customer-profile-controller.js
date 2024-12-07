import {
  getCustomer,
  getCustomerAccountsGeneralDetails,
  getCustomerAccounts,
} from "/js/services/customer-service.js";
import { profilePanel } from "/js/views/components/profile/profile-panel/profile-panel.js";
import { accountsPanel } from "/js/views/components/profile/accounts-panel/accounts-panel.js";

window.addEventListener("DOMContentLoaded", async function () {
  createProfilePage();
});

async function createProfilePage() {
  let customer;
  let customerId = getCustomerIdFromURL();

  // Check if a query with customerId was added to the URL, if not, send an error message
  if (customerId == null) {
    console.error("Customer ID not specified in the URL.");
    alert("Customer ID not specified in the URL.");

    return;
  }

  customerId = parseInt(customerId);

  try {
    customer = await getCustomer(customerId);
  } catch (error) {
    console.error(`Customer with ID ${customerId} not found.`);
    alert(`Customer with ID ${customerId} not found.`);
  }

  const customerAccountsGeneralDetails =
    await getCustomerAccountsGeneralDetails(customerId);

  profilePanel(customer, customerAccountsGeneralDetails);

  const customerAccounts = await getCustomerAccounts(customerId);

  const formattedAccounts = customerAccounts.map((account) => {
    return {
      id: account.id,
      accountType: account.accountType,
      balance: account.balance,
    };
  });

  accountsPanel(customerAccountsGeneralDetails, formattedAccounts);
}

function getCustomerIdFromURL() {
  const params = new URLSearchParams(window.location.search);

  return params.get("id");
}
