import { div } from "/js/views/components/commons/div.js";
import { detailsContainer } from "/js/views/components/form/details-container.js";
import { formHeader } from "/js/views/components/form/header.js";

function formPage() {
  const profilePanel = div(["profile-panel"]);
  const header = formHeader();
  const form = detailsContainer();

  profilePanel.appendChild(header);
  profilePanel.appendChild(form);

  return profilePanel;
}

export { formPage };
