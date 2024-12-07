import { div } from "/js/views/components/commons/div.js";
import { cardContent } from "/js/views/components/customer-card/customer-card-content.js";

function card(
  customerName,
  customerDetails,
  customerAccountsGeneralDetails,
  events
) {
  const card = div(["card"]);
  const avatar = div(["avatar"]);
  const content = cardContent(
    customerName,
    customerDetails,
    customerAccountsGeneralDetails,
    events
  );

  card.appendChild(avatar);
  card.appendChild(content);

  return card;
}

export { card };
