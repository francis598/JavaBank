import {
  getCustomerList,
  deleteCustomer,
  getCustomerAccountsGeneralDetails,
} from "/js/services/customer-service.js";
import { card } from "/js/views/components/customer-card/customer-card.js";
import { pageHeader } from "/js/views/components/commons/page-header.js";
import { div } from "/js/views/components/commons/div.js";
import { routes } from "/js/views/routes.js";

window.addEventListener("DOMContentLoaded", async function () {
  createHomePage();
});

async function createHomePage() {
  populateHomePage(await getCustomerList());
}

async function populateHomePage(customers) {
  const container = document.getElementById("container");

  renderHomePageHeader(container);

  const cardContainer = renderHomePageCardContainer(container);

  renderCustomerCards(customers, cardContainer);
}

function renderHomePageHeader(container) {
  const header = pageHeader("Customer List", "Add new", addNewEvent);

  container.appendChild(header);
}

function renderHomePageCardContainer(container) {
  const cardContainer = div(["card-container"]);

  container.appendChild(cardContainer);

  return cardContainer;
}

async function renderCustomerCards(customers, cardContainer) {
  for (const customer of customers) {
    const customerCard = await createCustomerCard(customer);

    cardContainer.appendChild(customerCard);
  }
}

async function createCustomerCard(customer) {
  const { id, firstName, lastName, ...customerDetails } = customer;


  const customerName = `${firstName} ${lastName}`;

  const customerAccountsGeneralDetails =
    await getCustomerAccountsGeneralDetails(id);

  const events = createEvents(id);

  return card(
    customerName,
    customerDetails,
    customerAccountsGeneralDetails,
    events
  );
}

function createEvents(id) {
  return {
    editCustomer() {
      editEvent(id);
    },
    deleteCustomer() {
      deleteEvent(id);
    },
    viewCustomerAccounts() {
      viewAccountsEvent(id);
    },
  };
}

async function addNewEvent() {
  window.location.href = routes.addUpdateForm.path;
}

async function editEvent(id) {
  window.location.href = `${routes.addUpdateForm.path}?id=${id}`;
}

async function deleteEvent(id) {
  await deleteCustomer(id);

  window.location.href = window.location.href;
}

function viewAccountsEvent(id) {
  window.location.href = `${routes.profile.path}?id=${id}`;
}
