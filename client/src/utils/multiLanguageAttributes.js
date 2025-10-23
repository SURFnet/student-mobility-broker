import I18n from "i18n-js";
import {isEmpty} from "./forms.js";

export const getValue = multiLanguageAttribute => {
    const localeFormat = I18n.locale === "en" ? "en-gb" : "nl-nl";
    const entry = multiLanguageAttribute.find(attr => attr.language.toLowerCase() === localeFormat) || multiLanguageAttribute[0];
    return entry.value;
}

export const capitalize = val => {
    if (isEmpty(val)) {
        return val;
    }
    return val.substring(0, 1).toUpperCase() + val.substring(1);
}