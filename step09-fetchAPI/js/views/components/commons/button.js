import { element } from "/js/views/components/commons/element.js";

function button(text, event, additionalClass = "") {
  return element(
    "button",
    ["btn", additionalClass].filter(Boolean),
    text,
    event
  );
}

export { button };
