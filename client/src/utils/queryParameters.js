import DOMPurify from "isomorphic-dompurify";

export function replaceQueryParameter(name, value, windowLocationSearch = window.location.search) {
    const urlSearchParams = new URLSearchParams(windowLocationSearch);
    urlSearchParams.set(name, value);
    return urlSearchParams.toString();
}

export function getParameterByName(name, windowLocationSearch = window.location.search) {
    const urlSearchParams = new URLSearchParams(windowLocationSearch);
    return DOMPurify.sanitize(urlSearchParams.get(name));
}
