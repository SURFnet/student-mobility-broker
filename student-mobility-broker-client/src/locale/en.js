import I18n from "i18n-js";

I18n.translations.en = {
  offering: {
    title: "Enroll for an elective course at {{abbreviation}}",
    wizard: {
      course: "Select course",
      transfer: "Approve transfer",
      enroll: "Enroll",
      relax: "Relax"
    },
    approve: "Approve transfer of personal information",
    wait: "Please wait while we enroll you {{name}}",
    enrolling: "Enrolling', rolling', rolling... ",
    progress: {
      "1": "Requesting your personal information at {{abbreviation}}",
      "2": "Processing personal information",
      "3": "Processing enrollment at {{abbreviation}}",
      "4": "Finishing your enrollment at {{abbreviation}}",
    },
    done: "Done for now, we'll be in touch",
    error: "Something went wrong...",
    errorTitle: "Provided details from {{abbreviation}}",
    almost: "Almost there, one more thing!",
    questions: "There are some extra questions",
    questionsDetail: "To finalise your enrollment for this specific course, some extra questions need to be answered",
    questionsWhere: "You will have to answer these questions within the LMS of {{abbreviation}}",
    goToLMS: "Answer the questions",
    next: "What will happen next?",
    receiveMail: "You will receive mail from {{abbreviation}} what the next steps will be.",
    homeInstitution: "Your home institution",
    personal : "Your personal information",
    subPersonal: "{{abbreviation}} will request all necessary information for your enrollment at your home institution.",
    subPersonalGrant: "For this you need to grant {{abbreviation}} permission to do so",
    approveButton: "Approve with eduID",
    ects: "{{ects}} ECTS",
    lang: {
      "nl-NL" : "Dutch",
      "en-EN" : "English",
    },
    dateTime: "Date & Time",
    places: "{{nbr}} places"
  },
  explanation: {
    title: "How does enrolling work?",
    eduID: {
      title: "You will enroll with your eduID (<a class=\"link\" href=\"https://eduid.nl\" target=\"_blank\">read why</a>)",
      subTitle: "Don't worry if you don't have an eduID yet, we'll help you get one in less than 30 seconds.",
    },
    transfer: {
      title: "Approve transfer",
      subTitle: "{{abbreviation}} will contact your current institution and will process your enrollment."
    },
    enrollment: {
      title: "Finalize enrollment",
      subTitle: "{{abbreviation}} might have some additional questions when you register for an elective."
    },
    relax: {
      title: "Sit back and relax",
      subTitle: "All details are now at {{abbreviation}}. They will take it from here and inform you via email."
    }
  }
};
