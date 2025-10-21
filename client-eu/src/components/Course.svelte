<script>
    import I18n from "i18n-js";
    import euroteq from "../icons/eu-logos/logo_alli_euroteq.png";
    import ewuu from "../icons/eu-logos/logo_alli_ewuu.png";
    import lde from "../icons/eu-logos/logo_alli_lde.png";
    import {offering} from "../stores/offering";
    import {onMount} from "svelte";
    import {capitalize, getValue} from "../utils/multiLanguageAttributes.js";
    import {formatDate} from "../utils/date.js";

    export let className = "desktop";

    let offeringType = null;

    const alliances = {
        euroteq: euroteq,
        ewuu: ewuu,
        lde: lde,
    }

    onMount(() => {
        offeringType = $offering.offering.offeringType;
    })
</script>

<style lang="scss">

    @media (max-width: 780px) {
        .course.desktop {
            display: none;
        }
    }

    .course {
        min-width: 320px;
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
            padding-bottom: 20px;
            margin-bottom: 20px;
            border-bottom: 1px solid #EEEFF1;

            .dates {
                display: flex;
                flex-direction: column;

                .start-date {
                    color: #008741;
                    font-size: 22px;
                    font-weight: 500;
                }

                .end-date {
                    color: #353535;
                    font-size: 14px;
                }
            }

            img {
                margin-left: auto;
                width: auto;
                height: 48px;
                border-radius: 4px;
                border: 0.48px solid #CED1D7
            }
        }

        table {
            width: 100%;
            border-collapse: collapse;

            td {
                &.attribute {
                    padding: 4px 0;
                    width: 35%;
                }

                &.value {
                    width: 65%;
                    font-weight: 500;

                    img {
                        width: 150px;
                        height: auto;
                    }
                }

            }

            @media (max-width: 780px) {
                td.value {
                    padding-left: 10px;
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
                    <span class="start-date">{formatDate($offering.offering.startDate)}</span>
                {/if}
                {#if $offering.offering.endDate}
                    <span class="end-date">{I18n.t("offering.endDate", {date: formatDate($offering.offering.endDate)})}</span>
                {/if}
            </div>
            {#if $offering.guestInstitution.logoURI}
                <img class="logo" src={$offering.guestInstitution.logoURI} alt=""/>
            {/if}
        </div>
        <table class="values">
            <tbody>
            {#if $offering.offering.addresses && $offering.offering.addresses[0]?.city}
                <tr>
                    <td class="attribute">{I18n.t("course.location")}</td>
                    <td class="value">{$offering.offering.addresses[0]?.city}</td>
                </tr>
            {/if}
            {#if $offering.offering.teachingLanguage}
                <tr>
                    <td class="attribute">{I18n.t("course.language")}</td>
                    <td class="value">{I18n.translations[I18n.locale].offering.lang[$offering.offering.teachingLanguage] ?
                        I18n.t(`offering.lang.${$offering.offering.teachingLanguage}`) :
                        $offering.offering.teachingLanguage}</td>
                </tr>
            {/if}
            {#if $offering.offering[offeringType] && $offering.offering[offeringType].level}
                <tr>
                    <td class="attribute">{I18n.t("course.level")}</td>
                    <td class="value">{capitalize($offering.offering[offeringType].level)}</td>
                </tr>
            {/if}
            {#if $offering.offering[offeringType] && $offering.offering[offeringType].studyLoad}
                <tr>
                    <td class="attribute">{I18n.t("course.points")}</td>
                    <td class="value">{I18n.t("offering.studyLoad", {
                        value: $offering.offering[offeringType].studyLoad.value,
                        studyLoadUnit: $offering.offering[offeringType].studyLoad.studyLoadUnit.toUpperCase()
                    })}</td>
                </tr>
            {/if}
            {#if $offering.offering.academicSession && $offering.offering.academicSession?.name?.length > 0}
                <tr>
                    <td class="attribute">{I18n.t("course.period")}</td>
                    <td class="value">{getValue($offering.offering.academicSession.name)}</td>
                </tr>
            {/if}
            {#if $offering.enrollmentRequest.alliance}
                <tr>
                    <td class="attribute">{I18n.t("course.alliance")}</td>
                    <td class="value">{$offering.enrollmentRequest.alliance}</td>
                </tr>
            {/if}
            {#if $offering.enrollmentRequest.alliance && alliances[$offering.enrollmentRequest.alliance.toLowerCase()]}
                <tr>
                    <td class="attribute"></td>
                    <td class="value">
                        <img alt="Alliance" src={alliances[$offering.enrollmentRequest.alliance.toLowerCase()]}/>
                    </td>
                </tr>
            {/if}
            </tbody>
        </table>
    </div>
{/if}
