import { div } from "/js/views/components/commons/div.js";
import { element } from "/js/views/components/commons/element.js";
import { button } from "/js/views/components/commons/button.js";

function accountHeader() {
  const header = div(["header"]);
  const h1 = element("h1", [], "Accounts list");
  header.appendChild(h1);

  const btnContainer = div(["btn-container"]);
  const newAccountBtn = button("New Account", null, "CTA");

  btnContainer.appendChild(newAccountBtn);
  header.appendChild(btnContainer);

  return header;
}

export { accountHeader };
