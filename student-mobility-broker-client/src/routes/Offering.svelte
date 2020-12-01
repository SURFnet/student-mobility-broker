<script>
  import I18n from "i18n-js";
  import check from "../icons/icons-studmob/Check-narrow.svg";
  import transfer from "../icons/icons-studmob/data-transfer-check.svg";
  import transferWhite from "../icons/icons-studmob/data-transfer-check-white.svg";
  import transferGrey from "../icons/icons-studmob/data-transfer-check-grey.svg";
  import enroll from "../icons/icons-studmob/official-building-3.svg";
  import enrollBlue from "../icons/icons-studmob/official-building-3-blue.svg";
  import enrollGrey from "../icons/icons-studmob/official-building-3-grey.svg";
  import enrollWhite from "../icons/icons-studmob/official-building-3-white.svg";
  import eduID from "../icons/logo_eduID.svg";
  import eduIDGrey from "../icons/logo_eduID_grey.svg";
  import relax from "../icons/icons-studmob/cocktail-glass.svg";
  import relaxGrey from "../icons/icons-studmob/cocktail-glass-grey.svg";
  import calendar from "../icons/icons-studmob/calendar-1.svg";
  import places from "../icons/icons-studmob/human-resources-offer-employee.svg";
  import launches from "../icons/icons-studmob/startup-launch.svg";
  import pin from "../icons/icons-studmob/pin.svg";
  import ects from "../icons/icons-studmob/school-book-trophy.svg";
  import lang from "../icons/icons-studmob/messages-bubble-square-text.svg";
  import {offering} from "../stores/offering";
  import {LottiePlayer} from '@lottiefiles/svelte-lottie-player';
  import scooter from "../lotties/lf20_Fyn2dD.json";
  import {authentication, startRegistration} from "../api";
  import {onMount} from "svelte";
  import {getParameterByName} from "../utils/queryParameters";
  import Button from "../components/Button.svelte";
  import hand from "../icons/icons-studmob/noun_Up hand drawn arrow_1563367.svg";

  let step = "approve";
  let showScooter = false;
  let finishedRegistration = false;
  let result;
  let hasErrors = false;

  const formatOptions = {weekday: "long", year: "numeric", month: "long", day: "numeric"};

  onMount(() => {
    step = getParameterByName("step");
    if (step === "enroll") {
      showScooter = true;
      startRegistration(getParameterByName("correlationID")).then(res => {
        result = res;
        showScooter = false;
        hasErrors = res.code !== 200;
        finishedRegistration = !hasErrors && !result.redirect;
      })
    }
  });

  const startAuthentication = () => {
    authentication($offering.enrollmentRequest.offeringURI,
      $offering.enrollmentRequest.personURI,
      $offering.enrollmentRequest.scope,
      $offering.authenticationActionUrl);
  }

  $: icons = [
    {
      name: I18n.t("offering.wizard.course"),
      icon: check,
      className: "done"
    },
    {
      name: I18n.t("offering.wizard.transfer"),
      icon: step === "approve" ? transfer : transferWhite,
      className: step === "approve" ? "current" : "done"
    },
    {
      name: I18n.t("offering.wizard.enroll"),
      icon: step === "approve" ? enroll : finishedRegistration ? enrollWhite : enrollBlue,
      className: step === "approve" ? "todo" : finishedRegistration ? "done" : "current",
    },
    {
      name: I18n.t("offering.wizard.relax"),
      icon: relax,
      className: finishedRegistration ? "current" : "todo"
    }
  ];

  const explanations = [
    {name: "eduID", icon: eduIDGrey},
    {name: "transfer", icon: transferGrey},
    {name: "enrollment", icon: enrollGrey},
    {name: "relax", icon: relaxGrey},
  ];

</script>

<style lang="scss">

  @mixin line {
    height: 12px;
    width: 100%;
    top: 38px;
    position: absolute;
    content: '';
    @media (max-width: 640px) {
      display: none;
    }
  }

  @mixin line-before {
    @include line;
    right: 84px;
  }

  @mixin line-after {
    @include line;
    right: -86px;
  }

  @mixin line-current-before {
    @include line;
    top: 35px;
    right: 84px;
  }

  @mixin line-current-after {
    @include line;
    top: 35px;
    right: -86px;
    border-left: 1px solid var(--color-primary-blue)
  }

  .container {
    max-width: 720px;
    margin: 0 auto 40px auto;
    width: 100%;
    display: flex;
    flex-direction: column;

    @media (max-width: 720px) {
      padding: 0 20px;
    }
  }

  .explanation-container {
    background-color: var(--color-grey-background);
    width: 100%;

    .explanations {
      max-width: 480px;
      margin: auto;
      display: flex;
      flex-direction: column;
      position: relative;
      flex-direction: column;
      @media (max-width: 720px) {
        padding: 0 20px;
      }

      span.hand {
        position: absolute;
        right: -60px;
        top: -40px;
      }

      h2 {
        text-align: center;
        color: var(--color-tertiary-grey);
      }

      p {
        color: var(--color-tertiary-grey);

        &.title {
          font-weight: bold;
          margin-bottom: 3px;
        }

        :global(a.link) {
          color: var(--color-tertiary-grey);
          text-decoration: underline;
        }
      }

      .explanations-left {
        border-left: 12px solid var(--color-secondary-grey);
        margin-bottom: 60px;
      }

      .explanation {
        position: relative;
        display: flex;
        background-color: white;
        padding: 10px 10px 10px 66px;
        border-radius: 4px;
        margin-bottom: 25px;

        &:last-child {
          margin-bottom: 0;
        }

        .explanation-icon {
          border-radius: 50%;
          width: 90px;
          height: 90px;
          display: flex;
          position: absolute;
          top: 0;
          left: -51px;
          background-color: var(--color-secondary-grey);

          span {
            margin: auto;
            :global(svg) {
              width: 45px;
            }
          }
          span.eduID {
            :global(svg) {
              width: 60px;
            }
          }
        }
      }

    }

  }

  h2 {
    margin: 40px 0;
  }

  div.icons {
    display: flex;
    justify-content: space-between;

    div.icon-container {
      display: flex;
      flex-direction: column;
      align-items: center;

      p {
        font-weight: bold;
        margin-top: 10px;
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
        }
      }

      &.done {
        background-color: var(--color-primary-blue);
      }

      &.current {
        border: 3px solid var(--color-primary-blue);
        color: var(--color-primary-blue);
        fill: var(--color-primary-blue);
      }

      &.todo {
        background-color: var(--color-secondary-blue);
      }

      &.done:before {
        @include line-before;
        background-color: var(--color-primary-blue);
      }

      &.current:before {
        @include line-current-before;
        background-color: var(--color-primary-blue);
      }

      &.todo:before {
        @include line-before;
        background-color: var(--color-secondary-blue);
      }

      &.done:after {
        @include line-after;
        background-color: var(--color-primary-blue);
      }

      &.current:after {
        @include line-current-after;
        background-color: var(--color-secondary-blue);
      }

      &.todo:after {
        @include line-after;
        background-color: var(--color-secondary-blue);
      }

      &.first:before, &.last:after {
        display: none;
      }

    }

  }

  .details {
    display: flex;

    .course {
      width: 60%;
      padding: 25px;
      border: 2px solid var(--color-primary-grey);
      margin-right: 15px;

      table {
        width: 100%;
        border-collapse: collapse;

        th.name {
          text-align: left;
          width: 70%;
        }

        th.logo {
          width: 82px;

          img {
            max-height: 56px;
            max-width: 56px;
          }

        }

        td.icon {
          padding: 10px 0 0px 0;

          :global(svg) {
            width: 28px;
          }
        }

        td.value {
          width: 90%;
          padding-left: 30px;
        }

        &.values {
          margin: 10px 0 20px 0;

          tr {
            border-top: 1px solid var(--color-primary-grey);

            &:last-child {
              border-bottom: 1px solid var(--color-primary-grey);
            }
          }
        }
      }
    }

    .status {
      width: 40%;
      margin: 15px 0 0 15px;
      display: flex;
      flex-direction: column;

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
    }
  }

</style>
<div class="container">
    <div class="offering">
        <h2>{I18n.t("offering.title", {abbreviation: $offering.homeInstitution.abbreviation})}</h2>
        <div class="icons">
            {#each icons as {name, icon, className}, i}
                <div class={`icon-container ${className}`}>
                    <div class={`icon ${className} ${i === (icons.length - 1) ? "last" : ""} ${i === 0 ? "first" : ""}`}>
                        <span>{@html icon}</span>
                    </div>
                    <p>{name}</p>
                </div>
            {/each}
        </div>
        <h2>{I18n.t("offering.approve")}</h2>
        <div class="details">
            <div class="course">
                <table>
                    <tr>
                        <th class="name">{$offering.offering.name}</th>
                        <th class="logo"><img src={$offering.guestInstitution.logoURI} alt=""/></th>
                    </tr>
                </table>
                <table class="values">
                    <tr>
                        <td class="icon">{@html ects}</td>
                        <td class="value">{I18n.t("offering.ects", {ects: $offering.offering.resultValueType})}</td>
                    </tr>
                    <tr>
                        <td class="icon">{@html pin}</td>
                        <td class="value">{$offering.guestInstitution.name}</td>
                    </tr>
                    <tr>
                        <td class="icon">{@html lang}</td>
                        <td class="value">{I18n.t(`offering.lang.${$offering.offering.mainLanguage}`)}</td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <th class="name">{I18n.t("offering.dateTime")}</th>
                    </tr>
                </table>
                <table class="values">
                    <tr>
                        <td class="icon">{@html calendar}</td>
                        <td class="value"><strong>{$offering.offering.academicSession.name}</strong></td>
                    </tr>
                    <tr>
                        <td class="icon">{@html launches}</td>
                        <td class="value">{new Date($offering.offering.academicSession.startDate).toLocaleString("default", formatOptions) }</td>
                    </tr>
                    <tr>
                        <td class="icon">{@html places}</td>
                        <td class="value">{I18n.t("offering.places", {nbr: $offering.offering.courseOffering.maxNumberStudents})}</td>
                    </tr>
                </table>
            </div>
            <div class="status">
                {#if step === "approve"}
                    <span class="label">{I18n.t("offering.homeInstitution")}</span>
                    <span class="value last">{$offering.homeInstitution.name}</span>

                    <span class="label">{I18n.t("offering.personal")}</span>
                    <span class="value personal">{I18n.t("offering.subPersonal", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                    <span class="value personal last">{I18n.t("offering.subPersonalGrant", {abbreviation: $offering.guestInstitution.abbreviation})}</span>
                    <Button href="/authentication" label={I18n.t("offering.approveButton")} icon={eduID}
                            onClick={startAuthentication}/>
                {:else if showScooter}
                    <LottiePlayer
                            src={scooter}
                            autoplay="{true}"
                            loop="{true}"
                            controls="{false}"
                            renderer="svg"
                            background="transparent"
                            height="{600}"
                            width="{600}"
                            controlsLayout={null}
                    />
                {:else if hasErrors}
                    <span>ERRORS</span>
                {:else if result && result.redirect}
                    <span>REDIRECT</span>
                {:else if finishedRegistration}
                    <span>SUCCESS</span>
                {/if}
            </div>
        </div>
    </div>
</div>
<div class="explanation-container">
    <div class="explanations">
        <h2>{I18n.t("explanation.title")}</h2>
        <span class="hand">{@html hand}</span>
        <div class="explanations-left">
            {#each explanations as {name, icon}}
                <div class="explanation">
                    <div class="explanation-icon">
                        <span class={name}>{@html icon}</span>
                    </div>
                    <div class="text">
                        <p class="title">{@html I18n.t(`explanation.${name}.title`)}</p>
                        <p>{@html I18n.t(`explanation.${name}.subTitle`, {abbreviation: $offering.guestInstitution.abbreviation})}</p>
                    </div>
                </div>
            {/each}
        </div>
    </div>
</div>
