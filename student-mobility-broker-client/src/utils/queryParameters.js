const queryParameter = {

  //shameless refactor of https://gist.githubusercontent.com/pduey/2764606/raw/e8b9d6099f1e4161f7dd9f81d71c2c7a1fecbd5b/querystring.js

  searchToHash: windowLocationSearch => {
    const h = {};
    if (windowLocationSearch === undefined || windowLocationSearch.length < 1) {
      return h;
    }
    const q = windowLocationSearch.slice(1).split("&");
    q.forEach(part => {
      const keyVal = part.split("=");
      // replace '+' (alt space) char explicitly since decode does not
      const hkey = decodeURIComponent(keyVal[0]).replace(/\+/g, " ");
      const hval = decodeURIComponent(keyVal[1]).replace(/\+/g, " ");
      if (h[hkey] === undefined) {
        h[hkey] = [];
      }
      h[hkey].push(hval);
    });
    return h;
  },

  hashToSearch: newSearchHash => {
    let search = "?";
    Object.keys(newSearchHash).forEach(key => {
      for (let i = 0; i < newSearchHash[key].length; i++) {
        search += search === "?" ? "" : "&";
        search += encodeURIComponent(key) + "=" + encodeURIComponent(newSearchHash[key][i]);
      }
    });
    return search;
  }
};

export function replaceQueryParameter(name, value, windowLocationSearch = window.location.search) {
  const newSearchHash = queryParameter.searchToHash(windowLocationSearch);
  delete newSearchHash[name];
  newSearchHash[decodeURIComponent(name)] = [decodeURIComponent(value)];
  return queryParameter.hashToSearch(newSearchHash);
}

export function getParameterByName(name, windowLocationSearch = window.location.search) {
  // eslint-disable-next-line
  const replacedName = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  const regex = new RegExp("[\\?&]" + replacedName + "=([^&#]*)")
  const results = regex.exec(windowLocationSearch);
  return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, " "));
}