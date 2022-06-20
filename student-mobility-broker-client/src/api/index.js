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
    "Accept-Language": navigator.language //If there ever is a possibility to switch languageI18n.locale,
  };
  return fetch(path, options).then(res => validateResponse(res));
}

function fetchJson(path, options = {}) {
  return validFetch(path, options);
}

// API
export function features() {
  return fetchJson("/api/features");
}

export function selectedOffering() {
  return fetchJson("/api/offering");
}

export function startRegistration(body) {
  return fetchJson("/api/start", {method: "POST", body: JSON.stringify(body)});
}

export function postResultsPlayground(correlationID, result) {
  const body = {...result, correlationID, v4: true};
  return fetchJson("/api/results", {method: "POST", body: JSON.stringify(body)});
}

export function newAssociationPlayground(correlationID, result) {
  const body = {...result, correlationID};
  return fetchJson("/api/results", {method: "POST", body: JSON.stringify(body)});
}

export function updateAssociationPlayground(correlationID, result, associationId) {
  const body = {...result, correlationID, associationId};
  return fetchJson("/api/results", {method: "POST", body: JSON.stringify(body)});
}

export function me(correlationID) {
  return fetchJson("/api/me", {method: "POST", body: JSON.stringify({correlationID})});
}

export function serviceRegistry() {
  return fetchJson("/api/service-registry");
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

export function authentication(personURI, personAuth, associationsURI, homeInstitution, scope, path) {
  formPost({ personURI, personAuth, associationsURI, homeInstitution, scope}, path)
}

// This is normally called by the Catalog, but for testing purposes we call it
export function broker(homeInstitutionSchacHome, guestInstitutionSchacHome, offeringID, type, startBrokerEndpoint) {
  formPost({ homeInstitutionSchacHome, guestInstitutionSchacHome, offeringID, type}, startBrokerEndpoint)
}

