import { config } from "/js/views/components/profile/utils/profile-fields-config.js";

function populateForm(customer) {
  const someCustomer = {
    customerId: customer["id"],
    ...customer,
  };

  Object.values(config).forEach(({ id }) => {
    const input = document.querySelector(`#${id}`);
    if (input) {
      input.setAttribute("value", someCustomer[id] || "");
    }
  });
}

export { populateForm };
