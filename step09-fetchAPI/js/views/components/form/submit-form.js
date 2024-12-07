import { addOrEditCustomer } from "/js/services/customer-service.js";
import { routes } from "/js/views/routes.js";

// Function to validate form inputs
const validateInputs = (inputs) => {
  let isValid = true;

  inputs.forEach((input) => {
    if (!input.checkValidity()) {
      input.reportValidity();
      isValid = false;
    } else {
      input.setCustomValidity("");
    }
  });

  return isValid;
};

async function submitForm() {
  const inputs = document.querySelectorAll("input");

  if (!validateInputs(inputs)) {
    return;
  }

  const customer = [...inputs].reduce((acc, input) => {
    const key = input.id;
    const value = input.value;

    acc[key] = value;
    return acc;
  }, {});

  try {
    await addOrEditCustomer(customer);
  } catch (error) {
    console.log(customer);
    console.error("An error occurred:", error.message);
    return;
  }

  window.location.href = routes.homePage.path;
}

export { submitForm };
