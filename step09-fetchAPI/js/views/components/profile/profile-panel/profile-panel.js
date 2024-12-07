import { div } from "/js/views/components/commons/div.js";
import { goBackButton } from "/js/views/components/commons/go-back-button.js";
import { routes } from "/js/views/routes.js";
import { profileHeader } from "/js/views/components/profile/profile-panel/profile-header.js";
import { profileDetailsContainer } from "/js/views/components/profile/profile-panel/profile-details-container.js";

function profilePanel(customer, accountDetails) {
  const profileContainer = document.getElementById("profile-container");
  const profilePanel = populateProfilePanel(customer, accountDetails);
  profileContainer.appendChild(profilePanel);
}

function populateProfilePanel(customer, accountDetails) {
  const profilePanel = div(["profile-panel"]);
  const avatar = div(["avatar"]);

  profilePanel.appendChild(goBackButton(toHomePage));
  profilePanel.appendChild(avatar);
  profilePanel.appendChild(profileHeader(customer));
  profilePanel.appendChild(profileDetailsContainer(customer, accountDetails));

  return profilePanel;
}

function toHomePage() {
  window.location.href = routes.homePage.path;
}

export { profilePanel };
