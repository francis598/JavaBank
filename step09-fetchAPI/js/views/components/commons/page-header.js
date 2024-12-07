import { element } from "/js/views/components/commons/element.js";
import { div } from "/js/views/components/commons/div.js";
import { button } from "/js/views/components/commons/button.js";

function pageHeader(titleText, buttonText, event) {
  const header = div(["header"]);

  const h1 = element("h1", [], titleText);
  header.appendChild(h1);

  const btn = button(buttonText, event, "CTA");
  header.appendChild(btn);

  return header;
}

export { pageHeader };
