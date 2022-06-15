<script>
    import I18n from "i18n-js";
    import calendar from "../icons/icons-studmob/calendar-1.svg";
    import launches from "../icons/icons-studmob/startup-launch.svg";
    import pin from "../icons/icons-studmob/pin.svg";
    import ects from "../icons/icons-studmob/school-book-trophy.svg";
    import lang from "../icons/icons-studmob/messages-bubble-square-text.svg";
    import {offering} from "../stores/offering";
    import {getValue} from "../utils/multiLanguageAttributes";

    export let className = "desktop";
    const formatOptions = {weekday: "long", year: "numeric", month: "long", day: "numeric"};


</script>

<style lang="scss">

    @media (max-width: 780px) {
        .course.desktop {
            display: none;
        }
    }

    .course {

        min-width: 50%;
        max-width: 50%;

        @media (max-width: 780px) {
            min-width: 100%;
            max-width: 100%;
        }

        padding: 25px;
        border: 2px solid var(--color-primary-grey);

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
        <table>
            <tr>
                {#if $offering.offering.course && $offering.offering.course.name}
                    <th class="name">{getValue($offering.offering.course.name)}</th>
                {/if}
                {#if $offering.guestInstitution.logoURI}
                    <th class="logo"><img src={$offering.guestInstitution.logoURI} alt=""/></th>
                {/if}
            </tr>
        </table>
        <table class="values">
            {#if $offering.offering.course && $offering.offering.course.studyLoad}
                <tr>
                    <td class="icon">{@html ects}</td>
                    <td class="value">{I18n.t("offering.studyLoad", {
                        value: $offering.offering.course.studyLoad.value,
                        studyLoadUnit: $offering.offering.course.studyLoad.studyLoadUnit.toUpperCase()
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
        </table>
        <table>
            <tr>
                <th class="name">{I18n.t("offering.dateTime")}</th>
            </tr>
        </table>
        <table class="values">
            {#if $offering.offering.academicSession && $offering.offering.academicSession.name}
                <tr>
                    <td class="icon">{@html calendar}</td>
                    <td class="value"><strong>{getValue($offering.offering.academicSession.name)}</strong></td>
                </tr>
            {/if}
            {#if $offering.offering.academicSession && $offering.offering.academicSession.startDate}
                <tr>
                    <td class="icon">{@html launches}</td>
                    <td class="value">{new Date($offering.offering.academicSession.startDate).toLocaleString("default", formatOptions) }</td>
                </tr>
            {/if}
        </table>
    </div>
{/if}
