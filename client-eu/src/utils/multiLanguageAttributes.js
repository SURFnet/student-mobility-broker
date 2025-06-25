import I18n from "i18n-js";

export const getValue = multiLanguageAttribute => {
    const localeFormat = I18n.locale === "en" ? "en-gb" : "nl-nl";
    const entry = multiLanguageAttribute.find(attr => attr.language.toLowerCase() === localeFormat) || multiLanguageAttribute[0];
    return entry.value;
}