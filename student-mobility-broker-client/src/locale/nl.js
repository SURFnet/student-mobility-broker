import I18n from "i18n-js";

I18n.translations.nl = {
  tabs: {
    intake: "Intake",
    results: "Results",
  },
  offering: {
    title: "Register for a course at {{abbreviation}}",
    wizard: {
      course: "Select course",
      transfer: "Approve transfer",
      enroll: "Register",
      relax: "Relax"
    },
    approve: "Approve transfer of personal information",
    enrolled: "You are enrolling for",
    wait: "Please wait while we enrol you {{name}}",
    enrolling: "Enrolling', rolling', rolling... ",
    progress: {
      "1": "Requesting your personal information at {{abbreviation}}",
      "2": "Processing personal information retrieved from {{abbreviation}}",
      "3": "Processing enrolment at {{abbreviation}}",
      "4": "Finishing your enrolment at {{abbreviation}}",
      "5": "Finishing your enrolment at {{abbreviation}}",
    },
    done: "Done for now, we'll be in touch",
    landing: "Student Mobility Broker",
    error: "Something went wrong...",
    errorTitle: "Provided details from {{abbreviation}}",
    noResultErrorMessage: "No details were disclosed. Please contact <a href=\"mailto:student.mobiliteit@surf.nl\">student.mobiliteit@surf.nl</a>",
    notFoundResultErrorMessage: "Your personal information could not be retrieved from {{institution}}",
    conflictResultErrorMessage: "Your session was lost. Please try again.",
    resultErrorMessage: "Please contact <a href=\"mailto:student.mobiliteit@surf.nl\">student.mobiliteit@surf.nl</a> for help.",
    almost: "Almost there, one more thing!",
    questions: "There are some extra questions",
    questionsDetail: "To finalise your enrolment for this specific course, some extra questions need to be answered",
    questionsWhere: "You will have to answer these questions within the LMS of {{abbreviation}}",
    goToLMS: "Answer the questions",
    next: "What will happen next?",
    receiveMail: "You will receive mail from {{abbreviation}} what the next steps will be.",
    homeInstitution: "Your home institution",
    personal : "<b>{{guest}}</b> needs to request all necessary information for your enrollment at <b>{{home}}</b>.",
    personalBullet1: "Both institutions are controllers of this processing of personal data (read the <a href='{{privacyEndpoint}}' target='_blank'>full privacy statement</a>).",
    personalBullet2sub1: "Learn exactly what ",
    personalBullet2sub2: "personal information",
    personalBullet2sub3: " is requested.",
    permission: "<b>{{guest}}</b> needs your permission to continue",
    subPersonal: "{{abbreviation}} will request all necessary information for your enrolment at your home institution.",
    subPersonalGrant: "For this you need to grant {{abbreviation}} permission to do so.",
    approveButton: "Approve with eduID",
    ects: "{{ects}} ECTS",
    lang: {
      "nl-NL" : "Dutch",
      "en-EN" : "English",
      "en-GB" : "English",
      "en-gb" : "English"
    },
    dateTime: "Date & Time",
    places: "{{nbr}} places",
    backToCatalog: "Back to the education catalogue"
  },
  landing: {
    info: "You have landed on the Student Mobility homepage, where you normally only would land after selecting a course in the education catalogue.",
    subInfo: "We want to make you feel welcome, but unfortunately there is nothing more to show without a course selection. ",
    surfLink: "For more information check out the <a href=\"https://www.surf.nl/en/eduid\" target=\"_blank\">SURF website</a>."
  },
  error: {
    info: "This is embarrassing. An error has occurred.",
    subInfo: "The only information we currently can share with you is: <span style='font-style: italic'>{{msg}}</span>",
    surfLink: "Please contact the <a href=\"mailto:student.mobiliteit@surf.nl\">SURF helpdesk</a> for further support.",
    expired: "The enrollment has been expired",
    offering: "The course information could not be retrieved from {{name}}"
  },
  explanation: {
    title: "How does enrolling work?",
    eduID: {
      title: "You will enrol with your eduID (<a class=\"link\" href=\"https://eduid.nl\" target=\"_blank\">read why</a>)",
      subTitle: "Don't worry if you don't have an eduID yet, we'll help you get one in less than 30 seconds.",
    },
    transfer: {
      title: "Approve transfer",
      subTitle: "{{abbreviation}} will contact your current institution and will process your enrolment."
    },
    enrollment: {
      title: "Finalize enrolment",
      subTitle: "{{abbreviation}} might have some additional questions when you register for an elective."
    },
    relax: {
      title: "Sit back and relax",
      subTitle: "All details are now at {{abbreviation}}. They will take it from here and inform you via email."
    }
  },
  modal: {
    title: "Requested personal information",
    ok: "Close",
    info: "The following information will be requested to complete your enrollment:",
    attributes: {
      names: "First and lastname",
      title: "Title",
      dateOfBirth: "Date of birth",
      placeOfBirth: "Place of birth",
      countryOfBirth: "Country of birth",
      nationality: "Nationality",
      sex: "Sex",
      proof: "Proof of student",
      email: "Email address home institution",
      emailPersonal: "Personal email address",
      phone: "Phonenumbers",
      address: "Address",
      ice: "ICE details",
      identifier: "Unique identifier"

    }
  }
};
