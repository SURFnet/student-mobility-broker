import {getParameterByName, replaceQueryParameter} from "../../utils/queryParameters";


test("getParameterByName", () => {
  const val = getParameterByName("param", "?param=value");
  expect(val).toBe("value");
});

test("replaceQueryParameter", () => {
  const val = replaceQueryParameter("param","new", "?param=value");
  expect(val).toBe("?param=new");
});