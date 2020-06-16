//Internal API
import I18n from "i18n-js";
import {navigate} from "svelte-routing";

let csrfToken = null;

function validateResponse(res) {

  csrfToken = res.headers.get("x-csrf-token") || csrfToken;

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
  options.credentials = "same-origin";
  options.redirect = "manual";
  options.headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
    "Accept-Language": I18n.locale,
    "X-CSRF-TOKEN": csrfToken
  };
  return fetch(path, options).then(res => validateResponse(res));
}

function fetchJson(path, options = {}) {
  return validFetch(path, options);
}

function postPutJson(path, body, method) {
  return fetchJson(path, {method, body: JSON.stringify(body)});
}

export function logout() {
  return fetchJson("/intake/api/private/logout");
}

export function configuration() {
  return fetchJson("/intake/api/public/config");
}

export function me() {
  return fetchJson("/intake/api/private/me");
}

export function institutions() {
  return fetchJson("/intake/api/public/institutions");
}

export function institutionSchacHomes() {
  return fetchJson("/intake/api/public/institutions-schac-home");
}

export function institutionBySchacHome(schacHome) {
  return fetchJson(`/intake/api/public/institution?schac_home=${schacHome}`);
}

export function courseByIdentifier(identifier) {
  return fetchJson(`/intake/api/public/course?identifier=${identifier}`);
}

export function register(schacHomeInstitution, schacHomeGuestInstitution, courseIdentifier) {
  return postPutJson("/intake/api/private/register", {
    schacHomeInstitution,
    schacHomeGuestInstitution,
    courseIdentifier,
    preview: true}, "PUT");
}

