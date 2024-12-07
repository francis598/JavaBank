const API_BASE_URL = "http://localhost:9001/javabank/api/customer";

async function getCustomerList() {
  const response = await fetch(`${API_BASE_URL}`);

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.json();
}

async function getCustomer(id) {
  const response = await fetch(`${API_BASE_URL}/${id}`);

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.json();
}

async function deleteCustomer(id) {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response;
}

async function addOrEditCustomer(customer) {
  if (customer?.customerId) {
    return await editCustomer(customer);
  }

  return await addCustomer(customer);
}

async function editCustomer(customer) {
  const {
    customerId,
    firstName,
    lastName,
    email,
    phone,
    street,
    city,
    zipCode,
  } = customer;

  const response = await fetch(`${API_BASE_URL}/${customerId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      street: street,
      city: city,
      zipCode: zipCode,
    }),
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response;
}

async function addCustomer(customer) {
  const { firstName, lastName, email, phone, street, city, zipCode } = customer;

  const response = await fetch(`${API_BASE_URL}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      street: street,
      city: city,
      zipCode: zipCode,
    }),
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response;
}

async function getCustomerAccounts(id) {
  const response = await fetch(`${API_BASE_URL}/${id}/account`);

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage);
  }

  return response.json();
}

async function getCustomerAccountsGeneralDetails(id) {
  const customerAccounts = await getCustomerAccounts(id);

  return {
    numOfAccounts: getNumOfAccounts(customerAccounts),
    totalBalance: getTotalBalance(customerAccounts),
  };
}

function getNumOfAccounts(customerAccounts) {
  return customerAccounts ? customerAccounts.length : 0;
}

function getTotalBalance(customerAccounts) {
  return customerAccounts
    ? customerAccounts.reduce((sum, account) => sum + account.balance, 0)
    : 0;
}

export {
  getCustomerList,
  getCustomer,
  deleteCustomer,
  addOrEditCustomer,
  getCustomerAccounts,
  getCustomerAccountsGeneralDetails,
};
