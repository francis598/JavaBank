function element(tag, classNames = [], textContent = "", event = null) {
  const element = document.createElement(tag);

  classNames.forEach((className) => element.classList.add(className));

  if (textContent) {
    element.textContent = textContent;
  }

  if (event && typeof event === "function") {
    element.addEventListener("click", event);
  }

  return element;
}

export { element };
