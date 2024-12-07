import { cardHeader } from "/js/views/components/customer-card/customer-card-header.js";
import { cardDetailsContainer as cardDetails } from "/js/views/components/customer-card/customer-card-details.js";
import { div } from "/js/views/components/commons/div.js";

function cardContent(
  customerName,
  customerDetails,
  customerAccountsGeneralDetails,
  events
) {
  const content = div(["content"]);
  const header = cardHeader(customerName, events);
  const cardDetailsContainer = cardDetails(
    customerDetails,
    customerAccountsGeneralDetails
  );

  content.appendChild(header);
  content.appendChild(cardDetailsContainer);

  return content;
}

export { cardContent };
