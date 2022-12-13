import DOMPurify from "dompurify";

test("sanitize", () => {
  const sanitized = decodeURIComponent(DOMPurify.sanitize("<form action='https://evil.com'><input type='text'></form>")) ;
  expect(sanitized).toBe("<form action=\"https://evil.com\"><input type=\"text\"></form>");
});
