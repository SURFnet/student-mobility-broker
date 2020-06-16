import I18n from "i18n-js";

I18n.translations.en = {
  header: {
    title: "Student Mobility",
    logout: "Logout",
    login: "Login",
  },
  login: {
    login: "Login",
    info: "To access the Student Mobility educational catalog you first need to login.",
    process: "When you hit the login button below then you will redirected to eduID where you can login or create a new eduID account",
    logoutTitle: "You have been logged out",
    logoutStatus: "To finalise the logout process you must now close this browser.",
  },
  home: {
    welcome: "Welcome {{name}}",
    profile: "Personal info",
    courses: "Courses"
  },
  profile: {
    title: "Personal information",
    info: "Basic information like your name and email address.",
    email: "Email address",
    schacHomeOrganization: "Institution ID",
    name: "Name",
    profile: "Profile",
    eduperson_scoped_affiliation: "Affiliations",
    eduid: "eduID"
  },
  courses: {
    title: "Courses",
    info: "All courses. go to the details of a course to register",
    institution: "Institution",
    link: "{{name}} (<span style='font-style: italic;font-weight: normal'>{{institution}}</span>)"
  },
  course: {
    title: "Details",
    info: "Details for course {{name}}",
    name: "Name",
    description: "Description",
    institution: "Institution",
    register: "Register",
    cancel: "Cancel",
    scopes: "Required scopes",
    registrationUrl: "Registration URL",
    registrationUrlParams: "Registration URL query parameters",
    login: "When you hit the register button below then you will redirected to eduID where you can login or create a new eduID account",
    registerConfirmation: "Are you sure you want to start the registration for course {{name}} at {{institution}}?",
    noPreConfiguredSchachHomeOrganization: "Your eduID account is linked to home institution(s):",
    noPreConfiguredSchachHomeOrganizationWarning: "However none of these institutions are configured in the the Student Mobility app",
    chooseSchacHome: "You have multiple home institutions linked to your eduID account.",
    chooseSchacHomeInfo: "Please choose one of your home institutions to be used in the registration process.",
    flash: {
      registered: "Successfully started the registration for course {{name}} at {{institution}}"
    }

  },
  notFound: {
    title: "Whoops...",
    title2: "Something went wrong (404)."
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
