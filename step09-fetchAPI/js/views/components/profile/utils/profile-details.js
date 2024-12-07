import { config } from "/js/views/components/profile/utils/profile-fields-config.js";

const customerDetails = Object.keys(config);

function getFormattedDetails(details, customer) {
  return details.map((label) => {
    const fieldName = config[label].name;

    return {
      label,
      value: customer[fieldName],
    };
  });
}

function getPersonalDetails(customer) {
  // get only phone and email
  const personalFields = customerDetails.slice(3, 5);

  return getFormattedDetails(personalFields, customer);
}

function getPersonalDetailsWithAddress(customer) {
  const personalFields = getPersonalDetails(customer);

  // get street, city and zipCode
  const addressFields = customerDetails.slice(5);

  const addressDetails = getFormattedDetails(addressFields, customer);

  return personalFields.concat(addressDetails);
}

function getAccountsGeneralDetails(accountDetails) {
  return [
    {
      label: "Number of accounts",
      value: `${accountDetails.numOfAccounts} active accounts`,
    },
    {
      label: "Accounts total",
      value: `${accountDetails.totalBalance}€`,
    },
  ];
}

export {
  getPersonalDetails,
  getPersonalDetailsWithAddress,
  getAccountsGeneralDetails,
};
