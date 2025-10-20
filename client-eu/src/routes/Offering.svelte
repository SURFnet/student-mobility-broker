<script>
    import I18n from "i18n-js";
    import enroll from "../icons/icons-studmob/official-building-3.svg?raw";
    import myAcademicID from "../icons/logo_myAcademicID.svg?raw";
    import balancer from "../icons/balancer.svg?raw";
    import DOMPurify from "isomorphic-dompurify";
    import Cookies from "js-cookie";
    import highFive from "../icons/icons-studmob/undraw_High_five.svg?raw";
    import moody from "../icons/icons-studmob/undraw_feeling_blue_4b7q.svg?raw";
    import accessDenied from "../icons/undraw_access_denied_422.svg?raw";
    import lightBulb from "../icons/icons-studmob/Lightbulb.svg?raw";
    import questions from "../icons/icons-studmob/undraw_faq_rjoy.svg?raw";
    import {offering} from "../stores/offering";
    import {config} from "../stores/config";
    import {playground} from "../stores/playground";
    import {LottiePlayer} from '@lottiefiles/svelte-lottie-player';
    import scooter from "../lotties/scooter.json";
    import student from "../lotties/student.json";
    import errorAnimation from "../lotties/error.json";
    import {authentication, startRegistration} from "../api";
    import {onMount} from "svelte";
    import {getParameterByName} from "../utils/queryParameters";
    import Button from "../components/Button.svelte";
    import {navigate} from "svelte-routing";
    import Explanations from "../components/Explanations.svelte";
    import Course from "../components/Course.svelte";
    import Loading from "../components/Loading.svelte";
    import Modal from "../components/Modal.svelte";
    import {isEmpty} from "../utils/forms";
    import Poll from "../components/Poll.svelte";
    import {getValue} from "../utils/multiLanguageAttributes";

    const timeoutStep = 1500;
    const STEPS = {
        approve: "approve",
        enroll: "enroll",
        finished: "finished"
    }

    export let offeringError = null;

    let title = I18n.t("offering.approve");
    let subTitle = I18n.t("offering.enrolled");
    let activity = null;
    let step = STEPS.approve;
    let result = null;
    let finished = false;
    let start = false;
    let error;
    let landing;
    let loaded = false;
    let balancing = false;
    let showModal = false;
    let name;
    let showPoll = false;
    let offeringType = null;

    onMount(() => {
        offeringType = $offering.offering.offeringType
        step = getParameterByName("step");
        error = getParameterByName("error");
        if (error || offeringError) {
            document.title = I18n.t("pages.error");
        } else {
            document.title = I18n.t("pages.approve");
        }
        if (error && I18n.translations[I18n.locale].error[error]) {
            error = I18n.translations[I18n.locale].error[error];
        } else {
            error = I18n.t("error.unknown");
        }
        landing = getParameterByName("landing");
        loaded = true;
        if (error || landing || offeringError) {
            title = I18n.t("offering.landing");
            subTitle = I18n.t("offering.landing");
        }
        const urlSearchParams = new URLSearchParams(window.location.search);
        const reload = urlSearchParams.has("reload");
        if (step === STEPS.enroll && !reload) {
            document.title = I18n.t("pages.registration", {abbreviation: $offering.guestInstitution.abbreviation});
            name = getParameterByName("name");
            title = subTitle = I18n.t("offering.wait", {name});
            changeActivity(1);
            setTimeout(() => start = true, 75);
            const correlationID = getParameterByName("correlationID");
            $offering.correlationID = correlationID;
            const body = $playground.active ?
                {
                    code: $playground.code,
                    redirect: $playground.redirect,
                    message: $playground.message,
                    reference: 123456
                } : {correlationID}

            if ($playground.active) {
                playground.reset();
            }

            startRegistration(body)
                .then(res => {
                    res.code = parseInt(res.code, 10);
                    invariantState(res, finished);
                })
                .catch(e => {
                    invariantState({code: 500, message: e.message}, finished);
                });
        }
    });

    const translationPresent = code => {
        const errors = I18n.translations[I18n.locale].error
        return !isEmpty(errors[code])
    }

    const changeLanguage = lang => () => {
        const urlSearchParams = new URLSearchParams(window.location.search);
        urlSearchParams.set("lang", lang);
        urlSearchParams.set("reload", "false");
        Cookies.set("lang", lang, {expires: 365, secure: true, sameSite: "Lax"});
        window.location.search = urlSearchParams.toString();
    };

    const invariantState = (aResult, isFinished) => {
        result = aResult || result;
        if (result && isFinished) {
            step = STEPS.finished;
            if (result.redirect) {
                document.title = I18n.t("pages.almostThere");
                title = subTitle = I18n.t("offering.almost");
            } else if (result.code === 200) {
                document.title = I18n.t("pages.registrationSent");
                title = subTitle = I18n.t("offering.done");
                setTimeout(() => showPoll = true, 500);
            } else {
                document.title = I18n.t("pages.error");
                title = subTitle = I18n.t("offering.error");
            }
        }
    }

    const changeActivity = count => {
        activity = I18n.t(`offering.progress.${count}`, {
            abbreviation:
                count < 3 ? $offering.homeInstitution.abbreviation : $offering.guestInstitution.abbreviation
        });
        if (count < 5) {
            setTimeout(() => changeActivity(++count), timeoutStep);
        } else {
            finished = true;
            invariantState(result, true);
        }
    }

    const startAuthentication = () => {
        authentication(
            $offering.enrollmentRequest.personURI,
            $offering.enrollmentRequest.personAuth,
            $offering.enrollmentRequest.associationsURI,
            $offering.enrollmentRequest.homeInstitution,
            $offering.enrollmentRequest.scope,
            $offering.authenticationActionUrl
        );
    }

    const gotoPlay = () => {
        if ($config.allowPlayground) {
            navigate("/intake");
        }
    }

    const genericErrorMessage = code => {
        const supportLink = ($offering.guestInstitution && $offering.guestInstitution.supportLink) ?
            $offering.guestInstitution.supportLink : I18n.t("error.supportLink");
        return I18n.t(`error.${code === 422 ? "noRetry" : "generic"}`, {
            supportDisplay: supportLink.replaceAll("mailto:", ""),
            supportLink: supportLink
        })
    }

    const registrationSuccessful = (currentResult, isFinished) => currentResult && currentResult.code === 200 && !result.redirect && isFinished;
    const pendingApproval = currentStep => currentStep === STEPS.approve;

</script>

<style lang="scss">
    .eu-header {
        width: 100%;
        display: flex;
        background-color: #004C97;
        padding: 12px 0;

        .eu-header-inner {
            width: 1067px;
            color: white;
            margin: 0 auto;
            display: flex;
            align-items: center;

            .left {
                font-size: 26px;
                font-weight: 600;
                margin: 0;
            }

            a.right {
                margin-left: auto;
                color: white;
                text-decoration: none;
            }

        }
    }

    .container {
        max-width: 1067px;
        margin: 0 auto 40px auto;
        width: 100%;
        display: flex;
        flex-direction: column;

        .mobile {
            display: none;
        }

        .offering {
            position: relative;
        }

        h2.desktop, h2.mobile {
            font-weight: bold;
            font-size: 24px;
        }

        .lottie-error {
            margin: auto;
            width: 70%;
        }

        @media (max-width: 780px) {
            :global(div.lottie-player) {
                width: 100%;
                margin: auto;
                height: auto;
            }
            div.lottie-container {
                display: flex;
                width: 100%;
            }
            .lottie-error {
                width: 95%;
            }
            :global(div.lottie-container svg) {
                width: 320px;
                height: auto;
            }
        }

        @media (max-width: 780px) {
            padding: 0 20px;
            .desktop {
                display: none;
            }
            .mobile {
                display: block;
            }
        }

        span.balancer {
            position: absolute;
            top: 0;
            right: 50%;
            cursor: pointer;

            :global(svg) {
                width: auto;
                height: 50px;
            }
        }

        img.euroteq {
            width: 100px;
            position: absolute;
            right: 0;
        }

        span.myAcademicID {
            width: 140px;
        }

        p.error-msg {
            font-weight: 600;
        }

        :global(span.balancing svg #rotate-container-left ) {
            transform-origin: 38% 66%;
            animation: goup 3s ease infinite;
        }

        :global(span.balancing svg #rotate-container-right ) {
            transform-origin: 46% 34%;
            animation: goup 3s ease infinite;
        }

        @keyframes goup {
            0% {
                transform: rotate(0deg);
            }
            50% {
                transform: rotate(30deg);
            }
            100% {
                transform: rotate(00deg);
            }
        }


    }

    h2 {
        margin: 20px 0 20px 0;

        &.mobile, &.desktop {
            margin: 0;
        }
    }

    @media (max-width: 780px) {
        h3 {
            font-size: 32px;
            line-height: 34px;
            font-weight: 600;
        }
    }


    div.header {
        display: flex;
        margin-top: 40px;
        flex-direction: column;
        @media (max-width: 780px) {
            display: block;
        }

        h2.offering-title {
            font-weight: 600;
            font-size: 32px
        }

        p.offering-title {
            font-weight: 400;
            font-size: 18px
        }

        :global(.button) {
            margin-left: auto;
            fill: white;
            margin-top: 30px;
            height: 40px;
            font-size: small;
        }

        :global(svg) {
            width: auto;
            height: 30px;
        }
    }

    div.language-switcher {
        margin: 15px 0 0 auto;
        display: none;

        ul {
            list-style: none;

            li {
                display: inline-block;
                padding: 0 10px;
            }

            li:last-child {
                border-left: 1px solid black;
            }

            li.non_active a {
                color: #0077c8;
                font-weight: normal;
            }

            li.active a {
                font-weight: 600;
                color: black;
                cursor: default;
            }
        }
    }


    div.landing, div.error {
        display: flex;
        flex-direction: column;

        p {
            margin-bottom: 15px;
        }
    }

    .course-container {
        display: flex;
        background-color: #EEEFF1;
        border-radius: 10px;
        padding: 30px;
        gap: 25px;

        @media (max-width: 780px) {
            flex-direction: column;
            &:not(.with-course) {
                padding: 0 30px 30px 30px;
            }

        }
    }

    .details {
        display: flex;
        @media (max-width: 780px) {
            flex-direction: column;
        }

        .status {
            padding: 0 25px;
            display: flex;
            flex-direction: column;
            min-width: 60%;
            max-width: 60%;

            @media (max-width: 780px) {
                padding: 15px 0 0 0;
                min-width: 100%;
                max-width: 100%;
            }


            span {
                margin-bottom: 10px;
            }

            .result {
                display: flex;
                flex-direction: column;
                position: relative;
                margin-top: 20px;
                h3 {
                    margin-bottom: 20px;
                }

                span.progress {
                    padding: 1em 2em;
                    text-align: center;
                    margin-top: 40px;
                    color: white;
                    background: linear-gradient(to left, var(--color-primary-grey) 50%, var(--color-primary-blue) 50%) right;
                    background-size: 200%;
                    transition: 6.0s linear;

                    &.start {
                        background-position: left;
                    }
                }

                span.activity {
                    margin-top: 10px;
                    font-size: 15px;
                    font-style: italic;
                    word-break: break-word;

                    @media (max-width: 780px) {
                        max-width: 100%;
                    }
                }

                div.hero {
                    margin-bottom: 20px;

                    @media (max-width: 780px) {
                        display: flex;
                        width: 100%;
                    }

                    :global(svg) {
                        width: 275px;
                        height: auto;

                        @media (max-width: 780px) {
                            width: 70%;
                            margin: auto;
                        }
                    }

                }

                div.final-action {
                    display: flex;

                    &.error-result {
                        flex-direction: column;
                    }

                    :global(svg) {
                        margin: 0 12px auto 0;
                        width: 66px;
                        height: auto;
                    }

                    span.final-action-msg {
                        margin: auto 0;
                        font-size: 16px;
                        line-height: 22px;
                    }

                    span.error-message {
                        line-height: 22px;
                    }
                }

                div.redirect {
                    display: flex;
                    flex-direction: column;

                    p {
                        font-weight: 600;
                    }

                    span {
                        margin-bottom: 25px;
                    }
                }
            }

            .no-results {
                margin-top: 15px;
                display: flex;
                flex-direction: column;

                span.last {
                    display: inline-block;
                    margin: 8px 0 25px 0;
                }
            }

        }
    }

    ul.personals, ul.attributes {
        list-style-type: initial;
        margin: 10px 0 20px 20px;
    }

</style>
{#if loaded }
    <div class="eu-header">
        <div class="eu-header-inner">
            <h2 class="left">
                {I18n.t("offering.registration")}
            </h2>
            <a class="right" href="{$config.catalogUrl}">{I18n.t("offering.backToEduXchange")}</a>
        </div>
    </div>
    <div class="container">
        <div class="offering">
            <!--            <div class="header">-->
            <!--                <div class="language-switcher">-->
            <!--                    <ul>-->
            <!--                        <li class="{I18n.locale === 'en' ? 'active' : 'non_active'}">-->
            <!--                            <a href="/en" on:click|preventDefault|stopPropagation={changeLanguage("en")}>EN</a>-->
            <!--                        </li>-->
            <!--                        <li class="{I18n.locale === 'nl' ? 'active' : 'non_active'}">-->
            <!--                            <a href="/nl" on:click|preventDefault|stopPropagation={changeLanguage("nl")}>NL</a>-->
            <!--                        </li>-->
            <!--                    </ul>-->
            <!--                </div>-->
            <!--            </div>-->
            {#if !landing && !error && !offeringError}
                {#if $config.allowPlayground}
                        <span id="balancer" class="balancer" class:balancing={balancing}
                              on:click={gotoPlay}>{@html balancer}</span>
                {/if}
                <div class="header">
                    <p class="offering-title">{I18n.t("offering.title", {
                        type: I18n.t(`offering.types.${$offering.offering.offeringType}`),
                        abbreviation: $offering.guestInstitution.abbreviation
                    })}
                    </p>
                    {#if $offering.offering[offeringType] && $offering.offering[offeringType].name}
                        <h2 class="offering-title">{getValue($offering.offering[offeringType].name)}</h2>
                    {/if}

                </div>
            {/if}
            <div class="details">
                {#if landing}
                    <div class="landing">
                        <p>{I18n.t("landing.info")}</p>
                        <p>
                            <span>{I18n.t("landing.subInfo")}</span>
                            <span>{@html I18n.t("landing.surfLink")}</span>
                        </p>
                        <div class="lottie-container">
                            <LottiePlayer
                                    src={student}
                                    autoplay="{true}"
                                    loop="{true}"
                                    speed={0.5}
                                    controls="{false}"
                                    renderer="svg"
                                    background="transparent"
                                    height="320px"
                                    width="auto"
                                    controlsLayout={null}
                            />
                        </div>
                    </div>
                {:else if error || offeringError}
                    <div class="error">
                        <p>
                            <span>{I18n.t("error.info")}</span>
                            <span>{I18n.t("error.subInfo")}</span>
                        </p>
                        <div>
                            <p class="error-msg">{DOMPurify.sanitize(error || offeringError)}</p>
                        </div>
                        <p>
                            <span>{@html genericErrorMessage(error || offeringError)}</span>
                        </p>
                        <div class="lottie-container lottie-error">
                            <LottiePlayer
                                    src={errorAnimation}
                                    autoplay="{true}"
                                    loop="{true}"
                                    speed={0.8}
                                    controls="{false}"
                                    renderer="svg"
                                    background="transparent"
                                    height="100%"
                                    width="100%"
                                    controlsLayout={null}
                            />
                        </div>
                    </div>
                {:else}
                    <div class={`course-container ${pendingApproval(step) ? "with-course" : ""}`}>
                        <Course className={step === STEPS.approve ? "mobile" : "desktop"}/>
                        <div class="status">
                            <h2 class="desktop">{title}</h2>
                            {#if !pendingApproval(step)}
                                <h2 class="mobile">{subTitle}</h2>
                            {/if}
                            {#if step === STEPS.enroll}
                                <div class="result">
                                    <div class="lottie-container">
                                        <LottiePlayer
                                                src={scooter}
                                                autoplay="{true}"
                                                loop="{true}"
                                                controls="{false}"
                                                renderer="svg"
                                                background="transparent"
                                                height="320"
                                                width="320"
                                                controlsLayout={null}
                                        />
                                    </div>
                                    <span class:start class="progress">{I18n.t("offering.enrolling")}</span>
                                    {#if activity}
                                        <span class="activity">{activity}</span>
                                    {/if}
                                </div>
                            {:else if pendingApproval(step)}
                                <h2 class="mobile">{title}</h2>
                                <div class="no-results">
                                <span>{@html I18n.t("offering.personal", {
                                    guest: $offering.guestInstitution.abbreviation,
                                    home: $offering.homeInstitution.name
                                })}</span>
                                    <ul class="personals">
                                        <li>
                                            {@html I18n.t("offering.personalBullet1sub1")}
                                            <a href="/"
                                               on:click|preventDefault|stopPropagation={() => showModal=true}>{I18n.t("offering.personalBullet1sub2")}</a>
                                            {@html I18n.t("offering.personalBullet1sub3")}
                                        </li>
                                        <li>
                                            {I18n.t("offering.personalBullet2")}
                                        </li>
                                        <li>
                                            {@html DOMPurify.sanitize(I18n.t("offering.personalBullet3", {privacyEndpoint: $offering.guestInstitution.privacyEndpoint}),
                                                {ADD_ATTR: ["target"]})}
                                        </li>
                                    </ul>
                                    <p>{I18n.t("offering.proceed")}</p>
                                    <span class="last">{@html DOMPurify.sanitize(I18n.t("offering.permission", {guest: $offering.guestInstitution.abbreviation}))}</span>
                                    <Button href="/authentication"
                                            class="myacademicid"
                                            label={I18n.t("offering.approveButton")}
                                            icon={myAcademicID}
                                            onClick={startAuthentication}/>
                                </div>
                            {:else if result && result.code >= 400}
                                <div class="result">
                                    <div class="hero">
                                        {#if result.code === 422}
                                            {@html accessDenied}
                                        {:else}
                                            {@html moody}
                                        {/if}
                                    </div>
                                    <div class="final-action error-result">
                                        {#if result.message}
                                            <span class="error-message">{DOMPurify.sanitize(result.message)}</span>
                                        {:else if translationPresent(result.code)}
                                        <span class="error-message">{@html DOMPurify.sanitize(I18n.t(`error.${result.code.toString()}`,
                                            {
                                                guestInstitution: $offering.guestInstitution.name,
                                                homeInstitution: $offering.homeInstitution.name
                                            }))}</span>
                                        {:else}
                                            <span class="error-message">{@html I18n.t("offering.noResultErrorMessage")}</span>
                                        {/if}
                                        <span class="error-message">{@html genericErrorMessage(result.code)}</span>
                                        {#if result.reference}
                                            <span class="error-message">{@html I18n.t("error.reference", {reference: result.reference})}</span>
                                        {/if}
                                    </div>
                                </div>
                            {:else if result && result.redirect}
                                <div class="result">
                                    <div class="hero">
                                        {@html questions}
                                    </div>
                                    <div class="redirect">
                                        <p>{I18n.t("offering.questions")}</p>
                                        <span>{I18n.t("offering.questionsDetail")}</span>
                                        <span>{I18n.t("offering.questionsWhere", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                                        <Button onClick={() => window.open(result.redirect, "_blank")}
                                                label={I18n.t("offering.goToLMS")}/>
                                    </div>
                                </div>
                            {:else if registrationSuccessful(result, finished)}
                                <div class="result">
                                    <div class="hero">
                                        {@html highFive}
                                    </div>
                                    <Poll name={name}
                                          crossInstitutionRequest={result.crossInstitutionRequest}
                                          visible={showPoll}
                                    />
                                    <div class="final-action">
                                        {@html lightBulb}
                                        {#if result.message}
                                            <span class="final-action-msg">{@html DOMPurify.sanitize(result.message)}</span>
                                        {:else}
                                            <span class="final-action-msg">{DOMPurify.sanitize(I18n.t("offering.receiveMail", {abbreviation: $offering.guestInstitution.abbreviation}))}</span>
                                        {/if}
                                    </div>
                                </div>
                            {/if}
                        </div>
                    </div>
                {/if}
            </div>
        </div>
    </div>
{:else}
    <Loading/>
{/if}
{#if pendingApproval(step) || landing || error}
    <Explanations/>
{/if}
{#if showModal}
    <Modal cancel={() => showModal=false}
           title={I18n.t("modal.title")}>
        <div>
            <p>{I18n.t("modal.info")}</p>
            <ul class="attributes">
                {#each Object.keys(I18n.translations[I18n.locale].modal.attributes) as key}
                    <li>{I18n.translations[I18n.locale].modal.attributes[key]}</li>
                {/each}
            </ul>
        </div>
    </Modal>
{/if}
