import {getParameterByName, replaceQueryParameter} from "../../utils/queryParameters";

test("getParameterByName", () => {
  const val = getParameterByName("param", "?param=value");
  expect(val).toBe("value");
});

test("getParameterByNameSanitized", () => {
  const val = getParameterByName("error", "?error=Institution+eindhoven.nl+unknown+%3CMETA%20HTTP-EQUIV=%22refresh%22%20CONTENT=%220;url=https://google.nl%22%3E");
  expect(val).toBe("Institution eindhoven.nl unknown ");
});

test("getParameterByNameNull", () => {
  const val = getParameterByName("error", "?");
  expect(val).toBe("");
});

test("replaceQueryParameter", () => {
  const val = replaceQueryParameter("param","new", "?param=value");
  expect(val).toBe("param=new");
});
