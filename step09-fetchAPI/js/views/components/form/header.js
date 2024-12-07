import { div } from "/js/views/components/commons/div.js";

function formHeader() {
  const profileHeader = div(["profile-header"]);
  const noAvatar = div(["no-avatar"]);

  profileHeader.appendChild(noAvatar);

  return profileHeader;
}

export { formHeader };
