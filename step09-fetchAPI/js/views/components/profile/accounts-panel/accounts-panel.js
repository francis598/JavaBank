import { div } from "/js/views/components/commons/div.js";
import { accountHeader } from "/js/views/components/profile/accounts-panel/accounts-header.js";
import {
  accountTable,
  populateAccountTable,
} from "/js/views/components/profile/accounts-panel/accounts-table.js";

function accountsPanel(customerAccountDetails, accounts) {
  const accountContainer = createAccountContainer();

  document.getElementById("profile-container").appendChild(accountContainer);

  const totalBalance = customerAccountDetails.totalBalance;
  populateAccountTable(accountContainer, accounts, totalBalance);
}

function createAccountContainer() {
  const accountContainer = div(["account-container"]);

  const header = accountHeader();
  accountContainer.appendChild(header);

  const table = accountTable();
  accountContainer.appendChild(table);

  return accountContainer;
}

export { accountsPanel };
