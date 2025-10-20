import I18n from "i18n-js";

I18n.translations.en = {
    pages: {
        approve: "Approve registration",
        registration: "Registering at {{abbreviation}}",
        almostThere: "Registration pending",
        registrationSent: "Registration sent",
        error: "Error in registration",
        notFound: "Not found"
    },
    tabs: {
        intake: "Intake",
        results: "Results",
        resultsV4: "Results (old)",
        person: "Person"
    },
    offering: {
        title: "Register at {{abbreviation}} for {{type}}",
        types: {
            course: "course",
            program: "program",
            component: "component",
            undefined: "course or program"
        },
        wizard: {
            course: "Select course",
            transfer: "Approve transfer",
            enroll: "Register",
            relax: "Relax"
        },
        endDate: "Ends {{date}}",
        approve: "Approve the transfer of your personal information",
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
        error: "Unable to register",
        errorTitle: "Provided details from {{abbreviation}}",
        noResultErrorMessage: "No details were disclosed.",
        almost: "Almost there, one more thing!",
        questions: "There are some extra questions",
        questionsDetail: "To finalise your enrolment for this specific course, some extra questions need to be answered",
        questionsWhere: "You will have to answer these questions within the LMS of {{abbreviation}}",
        goToLMS: "Answer the questions",
        next: "What will happen next?",
        receiveMail: "You have been successfully registered. You will receive mail from {{abbreviation}} what the next steps will be.",
        homeInstitution: "Your home institution",
        personal: "To proceed with this enrollment, <b>{{guest}}</b> needs to contact <b>{{home}}</b> to request your information.",
        personalBullet1sub1: "Learn which ",
        personalBullet1sub2: "personal data",
        personalBullet1sub3: " is requested.",
        personalBullet2: "Both institutions are controllers in this processing of personal data",
        personalBullet3: "Read the <a href='{{privacyEndpoint}}' target='_blank'>full privacy statement</a>.",
        permission: "<b>{{guest}}</b> needs your permission to start this transfer.",
        proceed: "Proceed with MyAcademicID",
        subPersonal: "{{abbreviation}} will request all necessary information for your enrolment at your home institution.",
        subPersonalGrant: "For this you need to grant {{abbreviation}} permission to do so.",
        approveButton: "Approve & continue",
        studyLoad: "{{value}} {{studyLoadUnit}}",
        lang: {
            "nl-NL": "Dutch",
            "nld": "Dutch",
            "eng": "English",
            "en-EN": "English",
            "en-GB": "English",
            "en-gb": "English"
        },
        dateTime: "Date & Time",
        places: "{{nbr}} places",
        backToCatalog: "Return",
        registration: "Registration",
        backToEduXchange: "← back to eduXchange"
    },
    landing: {
        info: "You have landed on the Student Mobility homepage, where you normally only would land after selecting a course in the education catalogue.",
        subInfo: "We want to make you feel welcome, but unfortunately there is nothing more to show without a course selection. ",
        surfLink: "For more information check out the <a href=\"https://eduxchange.nl/\" target=\"_blank\">eduXchange website</a>."
    },
    error: {
        info: "An unexpected error has occurred.",
        subInfo: " The information we currently can share with you is:",
        surfLink: "See <a href=\"https://eduxchange.nl/contact\">eduXchange</a> for help.",
        expired: "The enrollment has expired",
        offering: "The course information could not be retrieved from {{name}}",
        400: "Backend error.",//Invalid guest or home schachome
        404: "Your personal information could not be found from {{homeInstitution}}.",//404 person endpoint
        407: "The queue-session validation failed",//queue-it validation error
        408: "Institution {{guestInstitution}} took too long to respond. Please check if you received a confirmation of your registration via email within 10 minutes. If not, please try registering again later. If the problem persists, please check the information on your university's minor website on what to do.",
        409: "Your session was lost. Please try again.",//conflict
        412: "Your session was lost. Please try again.",//invalid enrollmentRequest
        417: "Your session was lost (Token error). Please try again",//tokenrequest failed
        419: "Backend error",//eduID not present in the ARP
        422: "There is an administrative reason why you can not be enrolled.",
        reference: "Your reference number is {{reference}}.",
        unknown: "",
        supportLink: "https://eduxchange.nl/all/help",
        generic: "Please try again later. If the problem persists, " +
            " <a href=\"{{supportLink}}\" target=\"_blank\">contact the student administration</a> for more information.",
        noRetry: "You can <a href=\"{{supportLink}}\" target=\"_blank\">contact the student administration</a> for more information."
    },
    explanation: {
        title: "How does enrolling work?",
        myAcademicID: {
            title: "You will enrol with MyAcademicID (<a class=\"link\" href=\"https://myacademic-id.eu/\" target=\"_blank\">read why</a>)",
            subTitle: "Log in with your own institution account.",
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
    },
    course: {
        location: "Location",
        language: "Language",
        points: "Points",
        period: "Period",
        level: "Level",
        alliance: "Alliance"
    },
    poll: {
        registerQuestion: "How easy or hard was it for you to register?",
        scores: {
            veryEasy: "Very easy",
            easy: "Easy",
            neutral: "Neutral",
            hard: "Hard",
            veryHard: "Very hard"
        },
        why: "Please explain",
        join: "Contact us about your experience?.",
        submit: "Submit & done",
        submitAppointment: "Submit & Contact us",
        thanksFeedback: "Thank you for your feedback",
        missingOut: "Still want to <a href='{{href}}' target='_blank'>contact us?</a>."
    }
};

export default I18n.translations.en;
