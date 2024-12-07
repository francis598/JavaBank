import { formPage } from "/js/views/components/form/form.js";
import { getCustomer } from "/js/services/customer-service.js";
import { populateForm } from "/js/views/components/form/populate-form.js";

window.addEventListener("DOMContentLoaded", async function () {
  const url = new URL(window.location.href);

  initializeFormPage();

  if (hasIdParameter(url)) {
    const customer = await getCustomer(url.searchParams.get("id"));
    populateForm(customer);
  }
});

function initializeFormPage() {
  const editContainer = document.querySelector(".edit-container");

  editContainer.appendChild(formPage());
}

function hasIdParameter(url) {
  return url.searchParams.has("id");
}
