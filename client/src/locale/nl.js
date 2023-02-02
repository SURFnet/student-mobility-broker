import I18n from "i18n-js";

I18n.translations.nl = {
    pages: {
        approve: "Toestemming geven voor inschrijven",
        registration: "Inschrijven bij {{abbreviation}}",
        almostThere: "Inschrijving in afwachting",
        registrationSent: "Inschrijving verstuurd",
        error: "Fout bij inschrijven",
        notFound: "Niet gevonden"
    },
    tabs: {
        intake: "Intake",
        results: "Results",
        resultsV4: "Results (old)",
        person: "Person"
    },
    offering: {
        title: "Register for a {{type}} at {{abbreviation}}",
        types: {
            course: "course",
            program: "program",
            component: "component"
        },
        wizard: {
            course: "Maak een keuze",
            transfer: "Bevestig overdracht",
            enroll: "Aanmelden",
            relax: "Relax"
        },
        approve: "Overdracht van persoonlijke gegevens goedkeuren",
        enrolled: "Je meldt je aan voor",
        wait: "Even geduld. We melden je aan {{name}}",
        enrolling: "Enrolling', rolling', rolling... ",
        progress: {
            "1": "Opvragen persoonlijke gegevens bij {{abbreviation}}",
            "2": "Verwerken persoonlijke gegevens opgehaald bij {{abbreviation}}",
            "3": "Verwerken aanmelding bij {{abbreviation}}",
            "4": "Afronden aanmelding bij {{abbreviation}}",
            "5": "Afronden aanmelding bij {{abbreviation}}",
        },
        done: "Klaar voor nu. Er wordt contact met je opgenomen",
        landing: "Student Mobility Broker",
        error: "Er is iets fout gegaan...",
        errorTitle: "Details van {{abbreviation}}",
        noResultErrorMessage: "Er is helaas niet meer informatie.",
        notFoundResultErrorMessage: "Jouw persoonlijke gegevens konden niet worden opgehaald bij {{institution}}",
        conflictResultErrorMessage: "Je sessie is verloren gegaan. Probeer het nogmaals.",
        almost: "Bijna klaar, nog één ding!",
        questions: "Er zijn extra vragen",
        questionsDetail: "Om je inschrijving definitief te maken, moet je nog wat extra vragen beantwoorden",
        questionsWhere: "Deze vragen moet je beantwoorden in het LMS van {{abbreviation}}",
        goToLMS: "Vragen beantwoorden",
        next: "Wat volgt nu?",
        receiveMail: "Je ontvangt bericht van {{abbreviation}} over de volgende stappen.",
        homeInstitution: "Jouw thuisinstelling",
        personal: "<b>{{guest}}</b> moet alle nodige informatie aanvragen voor je inschrijving bij <b>{{home}}</b>.",
        personalBullet1: "Beide instellingen zijn verwerkingsverantwoordelijke van deze verwerking van persoonsgegevens (lees de <a href='{{privacyEndpoint}}' target='_blank'>volledige privacy statement</a>).",
        personalBullet2sub1: "Begrijp precies welke ",
        personalBullet2sub2: "persoonlijke gegevens",
        personalBullet2sub3: " vereist zijn.",
        permission: "<b>{{guest}}</b> heeft je toestemming nodig om door te gaan",
        subPersonal: "{{abbreviation}} vraagt alle nodige informatie voor je inschrijving bij je thuisinstelling.",
        subPersonalGrant: "Hiervoor moet je  {{abbreviation}} toestemming geven.",
        approveButton: "Goedkeuren met eduID",
        studyLoad: "{{value}} {{studyLoadUnit}}",
        lang: {
            "nl-NL": "Dutch",
            "nld": "Dutch",
            "eng": "English",
            "en-EN": "English",
            "en-GB": "English",
            "en-gb": "English"
        },
        dateTime: "Datum & tijd",
        places: "{{nbr}} plaatsen",
        backToCatalog: "terug naar eduXchange"
    },
    landing: {
        info: "Je bent beland op de homepage van Student Mobility, waar je normaal gesproken pas terechtkomt na het selecteren van een vak of minor in de onderwijscatalogus.",
        subInfo: "We willen je het gevoel geven dat je welkom bent, maar helaas is er niets meer te laten zien zonder een cursus of minorkeuze. ",
        surfLink: "Voor meer informatie, ga naar de <a href=\"https://eduxchange.nl/\" target=\"_blank\">eduXchange website</a>."
    },
    error: {
        info: "Er is een onverwachte fout opgetreden.",
        subInfo: " De informatie die we op dit moment met je kunnen delen is:",
        surfLink: "Ga naar <a href=\"https://eduxchange.nl/contact\">eduXchange</a> voor hulp.",
        expired: "De aanmelding is verlopen",
        offering: "De informatie kon niet opgehaald worden bij {{name}}",
        400: "Backend error",//Invalig guest or home schachome
        404: "Je gegevens zijn niet gevonden worden bij {{name}}",//404 person endpoint
        409: "De wachtrij sessie validatie is mislukt",//queue-it validation error
        412: "Je sessie is verloren gegaan. Probeer het opnieuw",//invalid enrollmentRequest
        417: "Je sessie is verloren gegaan. Probeer het opnieuw",//tokenrequest failed
        419: "Backend error",//eduID not present in the ARP
        reference: "Je referentienummer is {{reference}}.",
        unknown: "",
        supportLink: "https://eduxchange.nl/contact",
        generic: "Probeer het nogmaals. Als het probleem zich voor blijft doen, " +
            " neem contact op met de student administratie van <a href=\"{{supportLink}}\" target=\"_blank\">{{supportLink}}</a>"
    },
    explanation: {
        title: "Hoe werkt aanmelden?",
        eduID: {
            title: "Je meldt je aan met je eduID (<a class=\"link\" href=\"https://eduid.nl\" target=\"_blank\">read why</a>)",
            subTitle: "Maak je geen zorgen als je nog geen eduID hebt, het aanmaken ervan kost minder dan 30 seconden.",
        },
        transfer: {
            title: "Overdracht goedkeuren",
            subTitle: "{{abbreviation}} neemt contact op met je huidige instelling en verwerkt je inschrijving."
        },
        enrollment: {
            title: "Inschrijving afronden",
            subTitle: "Het kan zijn dat {{abbreviation}} aanvullende vragen heeft na je aanmelding."
        },
        relax: {
            title: "Leun achterover en relax",
            subTitle: "Alle details liggen nu bij {{abbreviation}}. Zij nemen het vanaf daar over en informeren je via e-mail."
        }
    },
    modal: {
        title: "Vereiste persoonlijke gegevens",
        ok: "Sluiten",
        info: "De volgende gegevens worden gevraagd om jouw inschrijving te voltooien:",
        attributes: {
            names: "Voor en achternaam",
            title: "Titel",
            dateOfBirth: "Geboortedatum",
            placeOfBirth: "Geboorteplaats",
            countryOfBirth: "Land van geboorte",
            nationality: "Nationaliteit",
            sex: "Geslacht",
            proof: "Bewijs van studentschap",
            email: "E-mailadres thuisinstelling",
            emailPersonal: "Privé e-mailadres",
            phone: "Telefoonnummers",
            address: "Adres",
            ice: "ICE details",
            identifier: "Unieke identificatie"

        }
    }
};
