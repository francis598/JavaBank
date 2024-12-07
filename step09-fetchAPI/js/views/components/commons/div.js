import { element } from "/js/views/components/commons/element.js";

function div(classNames = []) {
  const div = element("div", classNames);

  return div;
}

export { div };
