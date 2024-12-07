import { button } from "/js/views/components/commons/button.js";

function goBackButton(event) {
  const goBackButton = button("Go back", event, "go-back");
  goBackButton.innerHTML = '<img src="/assets/arrow-left.svg"/>Go back';

  return goBackButton;
}

export { goBackButton };
