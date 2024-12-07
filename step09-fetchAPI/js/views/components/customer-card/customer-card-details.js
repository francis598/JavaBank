import { div } from "/js/views/components/commons/div.js";
import { element } from "/js/views/components/commons/element.js";
import {
  getPersonalDetails,
  getAccountsGeneralDetails,
} from "/js/views/components/profile/utils/profile-details.js";

function detailSection(titleText, details) {
  const article = element("article", []);
  const h3 = element("h3", [], titleText);
  article.appendChild(h3);

  const detailsDiv = div(["details"]);

  details.forEach((detail) => detailsDiv.appendChild(createCardDetail(detail)));
  article.appendChild(detailsDiv);

  return article;
}

function createCardDetail(detail) {
  const cardDetail = div(["card-detail"]);

  const h5 = element("h5", [], detail.label);
  const h4 = element("h4", [], detail.value);

  cardDetail.appendChild(h5);
  cardDetail.appendChild(h4);

  return cardDetail;
}

function cardDetailsContainer(customerDetails, customerAccountsGeneralDetails) {
  const cardDetailsContainer = div(["card-details-container"]);

  const personalDetails = getPersonalDetails(customerDetails);
  const accountDetails = getAccountsGeneralDetails(
    customerAccountsGeneralDetails
  );

  const details = personalDetails.concat(accountDetails);

  details.forEach((detail) => {
    const cardDetail = createCardDetail(detail);
    cardDetailsContainer.appendChild(cardDetail);
  });

  return cardDetailsContainer;
}

export { cardDetailsContainer, detailSection };
