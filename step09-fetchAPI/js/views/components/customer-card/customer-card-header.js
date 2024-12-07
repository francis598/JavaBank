import { div } from "/js/views/components/commons/div.js";
import { element } from "/js/views/components/commons/element.js";
import { button } from "/js/views/components/commons/button.js";

function cardHeader(customerName, events) {
  const cardHeader = div(["card-header"]);

  const title = element("h2", ["title"], customerName);

  const cardButtons = buttonContainer(events);

  cardHeader.appendChild(title);
  cardHeader.appendChild(cardButtons);

  return cardHeader;
}

function buttonContainer(events) {
  const btnContainer = div(["btn-container"]);

  const { editCustomer, deleteCustomer, viewCustomerAccounts } = events;

  const viewAccountsButton = button("View Account", viewCustomerAccounts);
  const editButton = button("Edit", editCustomer);
  const deleteButton = button("Delete", deleteCustomer);

  btnContainer.appendChild(viewAccountsButton);
  btnContainer.appendChild(editButton);
  btnContainer.appendChild(deleteButton);

  return btnContainer;
}

export { cardHeader };
