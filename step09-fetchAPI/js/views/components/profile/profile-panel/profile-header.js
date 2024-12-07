import { div } from "/js/views/components/commons/div.js";
import { element } from "/js/views/components/commons/element.js";

function profileHeader(customer) {
  const profileHeader = div(["profile-header"]);
  const content = div(["content"]);

  const nameElement = element(
    "h2",
    [],
    `${customer.firstName} ${customer.lastName}`
  );

  content.appendChild(nameElement);
  profileHeader.appendChild(content);

  return profileHeader;
}

export { profileHeader };
