import { div } from "/js/views/components/commons/div.js";
import { element } from "/js/views/components/commons/element.js";
import { formInput } from "/js/views/components/commons/form-input.js";
import { config } from "/js/views/components/profile/utils/profile-fields-config.js";
import { buttonContainer } from "/js/views/components/form/button-container.js";

function cardDetail() {
  const cardDetail = div(["card-detail"]);

  const formDetails = Object.keys(config);
  console.log(formDetails)
  console.log(formDetails);

  formDetails.forEach((label) => {
    const labelText = label === "id" ? "" : label;

    const elemLabel = element("label", [], labelText);
    elemLabel.setAttribute("for", config[label].id);
    cardDetail.appendChild(elemLabel);

    cardDetail.appendChild(formInput(config[label]));
  });

  return cardDetail;
}

function detailsContainer() {
  const form = element("form", ["details-container"]);
  const article = element("article");
  const h3 = element("h3", [], "Personal Details");
  const details = div(["details"]);
  const cardDetailComponent = cardDetail();

  details.appendChild(cardDetailComponent);
  article.appendChild(h3);
  article.appendChild(details);

  form.appendChild(article);

  const btnContainer = buttonContainer(form);
  form.appendChild(btnContainer);

  return form;
}

export { detailsContainer };
