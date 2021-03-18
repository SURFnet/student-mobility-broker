<script>
  import I18n from "i18n-js";
  import check from "../icons/icons-studmob/Check-narrow.svg";
  import transfer from "../icons/icons-studmob/data-transfer-check.svg";
  import transferWhite from "../icons/icons-studmob/data-transfer-check-white.svg";
  import enroll from "../icons/icons-studmob/official-building-3.svg";
  import enrollBlue from "../icons/icons-studmob/official-building-3-blue.svg";
  import enrollWhite from "../icons/icons-studmob/official-building-3-white.svg";
  import eduID from "../icons/logo_eduID.svg";
  import balancer from "../icons/balancer.svg";

  import relax from "../icons/icons-studmob/cocktail-glass.svg";
  import highFive from "../icons/icons-studmob/undraw_High_five.svg";
  import moody from "../icons/icons-studmob/undraw_feeling_blue_4b7q.svg";
  import lightBulb from "../icons/icons-studmob/Lightbulb.svg";
  import questions from "../icons/icons-studmob/undraw_faq_rjoy.svg";
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

  const timeoutStep = 1500;
  const STEPS = {
    approve: "approve",
    enroll: "enroll",
    finished: "finished"
  }

  let title = I18n.t("offering.approve");
  let activity = null;
  let step = STEPS.approve;
  let result = null;
  let finished = false;
  let start = false;
  let error;
  let landing;
  let loaded = false;
  let balancing = false;

  onMount(() => {
    step = getParameterByName("step");
    error = getParameterByName("error");
    landing = getParameterByName("landing");
    loaded = true;
    if (error || landing) {
      title = I18n.t("offering.landing");
    }
    if (step === STEPS.enroll) {
      const name = getParameterByName("name");
      title = I18n.t("offering.wait", {name});
      changeActivity(1);
      setTimeout(() => start = true, 75);
      const correlationID = getParameterByName("correlationID");
      const body = $playground.active ?
        {
          code: $playground.code,
          redirect: $playground.redirect,
          message: $playground.message
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

  const invariantState = (aResult, isFinished) => {
    result = aResult || result;
    if (result && isFinished) {
      step = STEPS.finished;
      if (result.redirect) {
        title = I18n.t("offering.almost");
      } else if (result.code === 200) {
        title = I18n.t("offering.done");
      } else {
        title = I18n.t("offering.error");
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
      $offering.enrollmentRequest.resultsURI,
      $offering.enrollmentRequest.scope,
      $offering.authenticationActionUrl
    );
  }

  const gotoPlay = () => {
    if ($config.allowPlayground) {
      navigate("/play");
    }
  }

  const registrationSuccessful = (currentResult, isFinished) => currentResult && currentResult.code === 200 && !result.redirect && isFinished;
  const pendingApproval = currentStep => currentStep === STEPS.approve;

  $: icons = [
    {
      name: I18n.t("offering.wizard.course"),
      icon: check,
      className: "done",
      action: () => balancing = !balancing
    },
    {
      name: I18n.t("offering.wizard.transfer"),
      icon: pendingApproval(step) ? transfer : transferWhite,
      className: pendingApproval(step) ? "current" : "done"
    },
    {
      name: I18n.t("offering.wizard.enroll"),
      icon: pendingApproval(step) ? enroll : registrationSuccessful(result, finished) ? enrollWhite : enrollBlue,
      className: pendingApproval(step) ? "todo" : registrationSuccessful(result, finished) ? "done" : "current",
    },
    {
      name: I18n.t("offering.wizard.relax"),
      icon: relax,
      className: registrationSuccessful(result, finished) ? "current" : "todo"
    }
  ];

  $: statuses = [
    "transparent",
    "done",
    pendingApproval(step) ? "todo" : "done",
    registrationSuccessful(result, finished) ? "done" : "todo"
  ];

</script>

<style lang="scss">

  .container {
    max-width: 720px;
    margin: 0 auto 40px auto;
    width: 100%;
    display: flex;
    flex-direction: column;

    @media (max-width: 780px) {
      padding: 0 20px;
    }
  }

  h2 {
    margin: 30px 0;
  }

  div.icons {
    display: flex;
    justify-content: space-between;
    position: absolute;
    top: -39px;
    width: 100%;

    div.icon-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      z-index: 2;
      background-color: transparent;

      p {
        font-weight: bold;
        margin: 10px 0 4px 0;
      }

      &.done, &.current {
        p {
          color: var(--color-primary-blue);
        }
      }
    }

    div.icon {
      border-radius: 50%;
      width: 90px;
      height: 90px;
      display: flex;
      z-index: 2;
      position: relative;

      span {
        margin: auto;

        :global(svg) {
          width: 45px;
          height: auto;
        }
      }

      &.done {
        background-color: var(--color-primary-blue);
      }

      &.current {
        background-color: white;
        border: 3px solid var(--color-primary-blue);
        color: var(--color-primary-blue);
        fill: var(--color-primary-blue);
      }

      &.todo {
        background-color: var(--color-secondary-blue);
      }

    }

  }

  div.header {
    display: flex;

    span.balancer {
      margin-left: auto;
      margin-bottom: auto;
      margin-top: 10px;
      cursor: pointer;
    }

    :global(svg) {
      width: 128px;
      height: auto;
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


  div.landing, div.error {
    display: flex;
    flex-direction: column;

    p {
      margin-bottom: 25px;
    }
  }

  div.lines {
    display: flex;
    height: 12px;
    width: 100%;
    padding: 0 10px;
    position: relative;
    background-color: transparent;
    margin: 70px 0 95px 0;

    div.line {
      height: 12px;
      width: calc(100% / 3 - 2px);

      &.transparent {
        width: 6px;
      }

      &.todo {
        background-color: var(--color-secondary-blue);
      }

      &.done {
        background-color: var(--color-primary-blue);
      }
    }
  }

  .details {
    display: flex;

    .status {
      margin-left: auto;
      display: flex;
      flex-direction: column;
      min-width: 40%;
      max-width: 40%;

      span {
        margin-bottom: 10px;

        &.label {
          font-weight: bold;
        }

        &.last {
          margin-bottom: 40px;
        }

        &.personal {
          font-style: italic;
        }
      }

      .result {
        display: flex;
        flex-direction: column;

        h3 {
          margin-bottom: 20px;
        }

        @media (max-width: 780px) {
          :global(div.lottie-player) {
            width: 175px;
            height: auto;
          }

        }

        span.progress {
          padding: 1em 2em;
          text-align: center;
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
          max-width: 240px;
          word-break: break-word;
          text-align: center;
        }

        div.hero {
          margin-bottom: 20px;

          :global(svg) {
            width: 275px;
            height: auto;

            @media (max-width: 780px) {
              width: 175px;
            }
          }

        }

        div.final-action {
          display: flex;

          &.error-result {
            flex-direction: column;
          }

          :global(svg) {
            margin-right: 12px;
            width: 66px;
            height: auto;
          }

          span.error-message {
            line-height: 22px;
          }
        }

        div.redirect {
          display: flex;
          flex-direction: column;

          p {
            font-weight: bold;
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
      }

    }
  }

</style>
{#if loaded }
    <div class="container">
        <div class="offering">
            {#if !landing && !error }
                <div class="header">
                    <h2>{I18n.t("offering.title", {abbreviation: $offering.guestInstitution.abbreviation})}</h2>
                    {#if $config.allowPlayground}
                        <span id="balancer" class="balancer" class:balancing={balancing}
                              on:click={gotoPlay}>{@html balancer}</span>
                    {/if}
                </div>
                <div class="lines">
                    {#each statuses as status}
                        <div class={`line ${status}`}></div>
                    {/each}
                    <div class="icons">
                        {#each icons as {name, icon, className, action}}
                            <div class={`icon-container ${className}`} on:click={() => action & action()}>
                                <div class={`icon ${className}`}>
                                    <span>{@html icon}</span>
                                </div>
                                <p>{name}</p>
                            </div>
                        {/each}
                    </div>
                </div>
            {/if}
            <h2>{title}</h2>
            <div class="details">
                {#if landing}
                    <div class="landing">
                        <p>{I18n.t("landing.info")}</p>
                        <p>
                            <span>{I18n.t("landing.subInfo")}</span>
                            <span>{@html I18n.t("landing.surfLink")}</span>
                        </p>
                        <LottiePlayer
                                src={student}
                                autoplay="{true}"
                                loop="{true}"
                                speed={0.5}
                                controls="{false}"
                                renderer="svg"
                                background="transparent"
                                height="100%"
                                width="100%"
                                controlsLayout={null}
                        />
                    </div>
                {:else if error}
                    <div class="error">
                        <p>{I18n.t("error.info")}</p>
                        <p>
                            <span>{@html I18n.t("error.subInfo", {msg: error})}</span>
                            <span>{@html I18n.t("error.surfLink")}</span>
                        </p>
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
                {:else}
                    <Course/>
                    <div class="status">
                        {#if step === STEPS.enroll}
                            <div class="result">
                                <LottiePlayer
                                        src={scooter}
                                        autoplay="{true}"
                                        loop="{true}"
                                        controls="{false}"
                                        renderer="svg"
                                        background="transparent"
                                        height="{250}"
                                        width="{250}"
                                        controlsLayout={null}
                                />
                                <span class:start class="progress">{I18n.t("offering.enrolling")}</span>
                                {#if activity}
                                    <span class="activity">{activity}</span>
                                {/if}
                            </div>
                        {:else if pendingApproval(step)}
                            <div class="no-results">
                                <span class="label">{I18n.t("offering.homeInstitution")}</span>
                                <span class="value last">{$offering.homeInstitution.name}</span>

                                <span class="label">{I18n.t("offering.personal")}</span>
                                <span class="value personal">{I18n.t("offering.subPersonal", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                                <span class="value personal last">{I18n.t("offering.subPersonalGrant", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                                <Button href="/authentication" label={I18n.t("offering.approveButton")} icon={eduID}
                                        onClick={startAuthentication}/>
                            </div>
                        {:else if result && result.code !== 200}
                            <div class="result">
                                <div class="hero">
                                    {@html moody}
                                </div>
                                <h3>{I18n.t("offering.errorTitle", {abbreviation: $offering.guestInstitution.abbreviation})}</h3>
                                <div class="final-action error-result">
                                    {#if result.message}
                                        <span class="error-message">{@html result.message}</span>
                                        <span class="error-message">{@html I18n.t("offering.resultErrorMessage")}</span>
                                    {:else}
                                        <span class="error-message">{@html I18n.t("offering.noResultErrorMessage")}</span>
                                    {/if}
                                </div>
                            </div>
                        {:else if result && result.redirect}
                            <div class="result">
                                <div class="hero">
                                    {@html questions}
                                </div>
                                <h3>{I18n.t("offering.almost")}</h3>
                                <div class="redirect">
                                    <p>{I18n.t("offering.questions")}</p>
                                    <span>{I18n.t("offering.questionsDetail")}</span>
                                    <span>{I18n.t("offering.questionsWhere", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                                    <Button onClick={() => window.location.href = result.redirect}
                                            label={I18n.t("offering.goToLMS")}/>
                                </div>
                            </div>
                        {:else if registrationSuccessful(result, finished)}
                            <div class="result">
                                <div class="hero">
                                    {@html highFive}
                                </div>
                                <h3>{I18n.t("offering.next")}</h3>
                                <div class="final-action">
                                    {@html lightBulb}
                                    <span>{I18n.t("offering.receiveMail", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                                </div>
                            </div>
                        {/if}
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
