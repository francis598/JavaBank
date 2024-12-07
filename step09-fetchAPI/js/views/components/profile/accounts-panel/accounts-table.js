import { element } from "/js/views/components/commons/element.js";
import { div } from "/js/views/components/commons/div.js";

function accountTable() {
  const table = element("table", ["table"]);

  const thead = createAccountTableHeader();
  table.appendChild(thead);

  const tbody = element("tbody", []);
  table.appendChild(tbody);

  const tfoot = createAccountTableFooter();
  table.appendChild(tfoot);

  return table;
}

function createAccountTableHeader() {
  const thead = element("thead", []);

  const trHead = element("tr", []);

  ["ID", "Balance", "Type"].forEach((columnTitle) => {
    const th = element("th", [], columnTitle);
    trHead.appendChild(th);
  });

  thead.appendChild(trHead);

  return thead;
}

function createAccountTableFooter() {
  const tfoot = element("tfoot", []);
  const trFoot = element("tr", []);

  const totalTd = element("td", [], "Total");
  trFoot.appendChild(totalTd);

  const totalBalanceTd = element("td", []);
  totalBalanceTd.setAttribute("id", "total-balance");
  trFoot.appendChild(totalBalanceTd);

  // leave a blank space below Type
  trFoot.appendChild(element("td", []));
  tfoot.appendChild(trFoot);

  return tfoot;
}

function populateAccountTable(accountContainer, accounts, totalBalance) {
  const tbody = accountContainer.querySelector("tbody");

  accounts.forEach((account) => {
    const tr = createAccountRow(account);
    tbody.appendChild(tr);
  });

  accountContainer.querySelector(
    "#total-balance"
  ).textContent = `${totalBalance}€`;
}

function createAccountRow(account) {
  const tr = element("tr", []);

  const idTd = element("td", [], account.id);
  tr.appendChild(idTd);

  const balanceTd = element("td", [], `${account.balance}€`);
  tr.appendChild(balanceTd);

  const typeTd = element("td", []);
  const tagDiv = div(["tag", account.accountType.toLowerCase()]);
  const typeSpan = element("span", [], account.accountType);

  tagDiv.appendChild(typeSpan);
  typeTd.appendChild(tagDiv);
  tr.appendChild(typeTd);

  return tr;
}

export { accountTable, populateAccountTable };
