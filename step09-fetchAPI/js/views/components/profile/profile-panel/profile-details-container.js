import { div } from "/js/views/components/commons/div.js";
import { detailSection } from "/js/views/components/customer-card/customer-card-details.js";
import {
  getPersonalDetailsWithAddress,
  getAccountsGeneralDetails,
} from "/js/views/components/profile/utils/profile-details.js";

function profileDetailsContainer(customer, accountDetails) {
  const detailsContainer = div(["details-container"]);

  detailsContainer.appendChild(
    detailSection("Personal details", getPersonalDetailsWithAddress(customer))
  );

  detailsContainer.appendChild(
    detailSection("Account info", getAccountsGeneralDetails(accountDetails))
  );

  return detailsContainer;
}

export { profileDetailsContainer };
