import I18n from "i18n-js";

I18n.translations.en = {
  header: {
    title: "Student Mobility",
    logout: "Logout"
  },
  login: {
    login: "Login",
    info: "To access the Student Mobility educational catalog you first need to login.",
    process: "When you hit the login button below then you will redirected to eduID where you can login or create a new eduID account",
    logoutTitle: "You have been logged out",
    logoutStatus: "To finalise the logout process you must now close this browser.",
  },
  notFound: {
    title: "Whoops...",
    title2: "Something went wrong (404)."
  },
  home: {
    welcome: "Welcome {{name}}",
    info: "Personal info",
    courses: "Courses"
  },
  footer: {
    privacy: "Privacy policy",
    terms: "Terms of Use",
    help: "Help",
    poweredBy: "Powered by"
  },
  modal: {
    cancel: "Cancel",
    confirm: "Confirm"
  },
};

I18n.ts = (key, model) => I18n.t(key, model);
