<script>
    import I18n from "i18n-js";
    import calendar from "../icons/icons-studmob/calendar-1.svg?raw";
    import launches from "../icons/icons-studmob/startup-launch.svg?raw";
    import pin from "../icons/icons-studmob/pin.svg?raw";
    import ects from "../icons/icons-studmob/school-book-trophy.svg?raw";
    import lang from "../icons/icons-studmob/messages-bubble-square-text.svg?raw";
    import {offering} from "../stores/offering";
    import {getValue} from "../utils/multiLanguageAttributes";
    import {onMount} from "svelte";

    export let className = "desktop";
    const formatOptions = {weekday: "long", year: "numeric", month: "long", day: "numeric"};

    let offeringType = null;

    onMount(() => {
        offeringType = $offering.offering.offeringType
    })
</script>

<style lang="scss">

    @media (max-width: 780px) {
        .course.desktop {
            display: none;
        }
    }

    .course {

        min-width: 40%;
        max-width: 40%;
        background-color: white;
        border-radius: 10px;
        padding: 20px;
        border: 0.93px solid #CBD0D5;

        @media (max-width: 780px) {
            min-width: 100%;
            max-width: 100%;
        }

        .course-header {
                      display: flex;
            align-items: center;
            .dates {
                display: flex;
                flex-direction: column;
                .start-date {
                    color: #008741;
                    font-size: 22px;
                    font-weight: 600;
                }
                .end-date {
                    color: #353535;
                    font-size: 14px;
                }
            }
        }

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
                    max-height: 100px;
                    max-width: 100px;
                }

            }

            td.icon {
                padding: 10px 0 0 0;

                :global(svg) {
                    width: 28px;
                    height: auto;
                }
            }

            td.value {
                width: 90%;
                padding-left: 30px;
            }

            @media (max-width: 780px) {
                td.value {
                    padding-left: 10px;
                }
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

</style>
{#if $offering.offering}
    <div class={`course ${className}`}>
        <div class="course-header">
            <div class="dates">
                {#if $offering.offering.startDate}
                    <span class="start-date">{$offering.offering.startDate}</span>
                {/if}
                {#if $offering.offering.endDate}
                    <span class="end-date">{I18n.t("offering.endDate", {date: $offering.offering.endDate})}</span>
                {/if}
            </div>
            {#if $offering.guestInstitution.logoURI}
                <img class="logo" src={$offering.guestInstitution.logoURI} alt=""/>
            {/if}
        </div>
        <table>
            <tbody>
            <tr>
                {#if $offering.offering[offeringType] && $offering.offering[offeringType].name}
                    <th class="name">{getValue($offering.offering[offeringType].name)}</th>
                {/if}

            </tr>
            </tbody>
        </table>
        <table class="values">
            <tbody>
            {#if $offering.offering[offeringType] && $offering.offering[offeringType].studyLoad}
                <tr>
                    <td class="icon">{@html ects}</td>
                    <td class="value">{I18n.t("offering.studyLoad", {
                        value: $offering.offering[offeringType].studyLoad.value,
                        studyLoadUnit: $offering.offering[offeringType].studyLoad.studyLoadUnit.toUpperCase()
                    })}</td>
                </tr>
            {/if}
            {#if $offering.guestInstitution.name}
                <tr>
                    <td class="icon">{@html pin}</td>
                    <td class="value">{$offering.guestInstitution.name}</td>
                </tr>
            {/if}
            {#if $offering.offering.teachingLanguage}
                <tr>
                    <td class="icon">{@html lang}</td>
                    <td class="value">{I18n.translations[I18n.locale].offering.lang[$offering.offering.teachingLanguage] ?
                        I18n.t(`offering.lang.${$offering.offering.teachingLanguage}`) :
                        $offering.offering.teachingLanguage}</td>
                </tr>
            {/if}
            </tbody>
        </table>
        <table>
            <tbody>
            <tr>
                <th class="name">{I18n.t("offering.dateTime")}</th>
            </tr>
            </tbody>
        </table>
        <table class="values">
            <tbody>
            {#if $offering.offering.academicSession && $offering.offering.academicSession.name}
                <tr>
                    <td class="icon">{@html calendar}</td>
                    <td class="value"><strong>{getValue($offering.offering.academicSession.name)}</strong></td>
                </tr>
            {/if}
            {#if $offering.offering.academicSession && $offering.offering.academicSession.startDate}
                <tr>
                    <td class="icon">{@html launches}</td>
                    <td class="value">{new Date($offering.offering.academicSession.startDate)
                        .toLocaleString(I18n.locale === "nl" ? "nl-NL" : "en-GB", formatOptions) }</td>
                </tr>
            {/if}
            </tbody>
        </table>
    </div>
{/if}
