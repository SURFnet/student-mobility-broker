import I18n from "i18n-js";

export const formatDate = val => {
    const date = new Date(val);
    const localeFormat = I18n.locale === "en" ? "en-gb" : "nl-nl";
    const formatter = new Intl.DateTimeFormat(localeFormat, {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
    return formatter.format(date);
}
