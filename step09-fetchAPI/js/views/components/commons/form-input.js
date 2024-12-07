import { element } from "/js/views/components/commons/element.js";

function formInput(config) {
  const { id, name, type, title, required, minLength } = config;

  const input = element("input");

  input.id = id;
  input.name = name;
  input.type = type;
  input.title = title;
  input.required = required;
  input.minLength = minLength;

  return input;
}

export { formInput };
