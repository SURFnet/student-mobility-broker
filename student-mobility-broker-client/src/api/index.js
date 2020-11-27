//Internal API
import I18n from "i18n-js";

function validateResponse(res) {

  if (!res.ok) {
    if (res.type === "opaqueredirect") {
      return res;
    }
    throw res;
  }

  return res.json();
}

function validFetch(path, options) {
  options = options || {};
  options.credentials = "include";
  options.redirect = "manual";
  options.headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
    "Accept-Language": I18n.locale,
  };
  return fetch(path, options).then(res => validateResponse(res));
}

function fetchJson(path, options = {}) {
  return validFetch(path, options);
}

function postPutJson(path, body, method) {
  return fetchJson(path, {method, body: JSON.stringify(body)});
}

// API
export function features() {
  return fetchJson("/api/features");
}

export function selectedOffering() {
  return fetchJson("/api/offering");
}

const formPost = (fields, path) => {
  const form = document.createElement("form");
  form.method = "POST";
  form.action = path;
  Object.entries(fields).forEach(field => {
    const hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.name = field[0];
    hiddenField.value = field[1];
    form.appendChild(hiddenField);
  });
  document.body.appendChild(form);
  form.submit();
}

export function register(offeringURI, personURI, scope, path) {
  formPost({ offeringURI, personURI, scope}, path)
}

// This is normally called by the Catalog, but for testing purposes we call it
export function broker(homeInstitutionSchacHome, guestInstitutionSchacHome, offeringID) {
  formPost({ homeInstitutionSchacHome, guestInstitutionSchacHome, offeringID}, "http://localhost:8091/api/broker")
}

