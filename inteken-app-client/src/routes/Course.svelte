<script>
    import {user, flash} from "../stores/user";
    import I18n from "i18n-js";
    import {register, courseByIdentifier, institutionBySchacHome, institutionSchacHomes} from "../api";
    import {navigate} from "svelte-routing";
    import chevron_left from "../icons/chevron-left.svg";
    import Button from "../components/Button.svelte";
    import {onMount} from "svelte";
    import Modal from "../components/Modal.svelte";
    import Spinner from "../components/Spinner.svelte";
    import Flash from "../components/Flash.svelte";

    let course = {};
    let institution = {};
    let loaded = false;
    let showModal = false;
    let registerUrl = "";
    let schacHomeInstitution = "nope";
    let schacHomeInstitutions = [];
    let linkedSchacHomeInstitutions = [];

    onMount(() => {
        const urlSearchParams = new URLSearchParams(window.location.search);
        const identifier = urlSearchParams.get("identifier");
        const schacHome = urlSearchParams.get("schacHome");
        Promise.all([courseByIdentifier(identifier), institutionBySchacHome(schacHome), institutionSchacHomes()])
                .then(res => {
                    course = res[0];
                    institution = res[1];
                    loaded = true;

                    linkedSchacHomeInstitutions = $user.eduperson_scoped_affiliation.map(s => s.substring(s.indexOf("@") + 1));
                    schacHomeInstitutions = linkedSchacHomeInstitutions.filter(sh => res[2].includes(sh));
                    schacHomeInstitution = schacHomeInstitutions[0];
                });
    });

    const registerCourse = showConfirmation => () => {
        if (showConfirmation) {
            showModal = true;
        } else {
            register(schacHomeInstitution, institution.schacHome, course.identifier).then(json => {
                registerUrl = json.url;
                flash.setValue(I18n.t("course.flash.registered", {name: course.name, institution: institution.name}));
                showModal = false;
            });
        }
    }

    const cancel = () => navigate("/courses");

</script>

<style>
    .course {
        width: 100%;
        display: flex;
        height: 100%;
    }

    @media (max-width: 820px) {
        .left {
            display: none;
        }

        .inner {
            border-left: none;
        }
    }

    .left {
        background-color: #f3f6f8;
        width: 270px;
        height: 100%;
        border-bottom-left-radius: 8px;
    }

    .inner {
        width: 100%;
        margin: 20px 0 190px 0;
        padding: 15px 15px 0 40px;
        border-left: 2px solid var(--color-primary-grey);
        display: flex;
        flex-direction: column;
        background-color: white;
    }

    .header {
        display: flex;
        align-items: center;
        align-content: center;
        color: var(--color-primary-green);
    }

    .header a {
        margin-top: 8px;
    }

    h2 {
        margin-left: 25px;
    }

    p.info {
        margin: 12px 0 32px 0;
    }

    table {
        width: 100%;
    }

    td {
        border-bottom: 1px solid var(--color-primary-grey);
    }

    td.attr {
        width: 50%;
        padding: 20px;
    }

    td.value {
        width: 50%;
        font-weight: bold;
        padding-left: 20px;
    }

    div.value-container {
        display: flex;
        flex-direction: column;
    }

    div.value-container span {
        padding: 5px 0;
    }

    .options {
        margin-top: 60px;
    }

    p.confirmation {
        margin-bottom: 12px;
    }

    :global(.options a:not(:first-child)) {
        margin-left: 25px;
    }

    div.registration {
        margin: 40px 0 15px 0;
        display: flex;
        flex-direction: column;
    }

    div.registration input {
        margin-top: 15px;
        border-radius: 8px;
        border: solid 1px #676767;
        padding: 14px;
        font-size: 16px;
        width: 100%;
    }

    div.no-schac-home {
        margin: 40px 0 15px 0;
        display: flex;
        flex-direction: column;
        color: var(--color-primary-red)
    }

    div.no-schac-home ul {
        margin: 20px 0 20px 40px;
        list-style: circle;
    }

    div.home-institutions {
        display: flex;
        flex-direction: column;
    }

    div.home-institutions label {
        display: flex;
        font-weight: bold;
        align-content: center;
        align-items: center;
        margin: 10px 0;
    }

    div.home-institutions input {
        margin: 0 15px;
    }


</style>
{#if loaded}
    <div class="course">
        <div class="left"></div>
        <div class="inner">
            <div class="header">
                <a href="/back" on:click|preventDefault|stopPropagation={cancel}>
                    {@html chevron_left}
                </a>
                <h2>{I18n.t("course.title")}</h2>
            </div>
            <p class="info">{I18n.t("course.info", {name: course.name})}</p>
            <table cellspacing="0">
                <thead></thead>
                <tbody>
                <tr class="name">
                    <td class="attr">{I18n.t("course.name")}</td>
                    <td class="value">
                        <div class="value-container">
                            <span>{`${course.name}`}</span>
                        </div>
                    </td>
                </tr>
                <tr class="name">
                    <td class="attr">{I18n.t("course.description")}</td>
                    <td class="value">
                        <div class="value-container">
                            <span>{`${course.description}`}</span>
                        </div>
                    </td>
                </tr>
                <tr class="name">
                    <td class="attr">{I18n.t("course.scopes")}</td>
                    <td class="value">
                        <div class="value-container">
                            {#each course.requiredScopes as scope}
                                <span>{`${scope}`}</span>
                            {/each}
                        </div>
                    </td>
                </tr>
                <tr class="name">
                    <td class="attr">{I18n.t("course.institution")}</td>
                    <td class="value">
                        <div class="value-container">
                            <span>{institution.name}</span>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>

            {#if registerUrl}
                <div class="registration">
                    <label>{I18n.t("course.registrationUrl")} </label>
                    <input disabled value={registerUrl}>
                </div>
            {/if}
            {#if !schacHomeInstitution}
                <div class="no-schac-home">
                    <p>{I18n.t("course.noPreConfiguredSchachHomeOrganization" )} </p>
                    <ul>
                        {#each linkedSchacHomeInstitutions as schacHome}
                            <li>{schacHome}</li>
                        {/each}
                    </ul>
                    <p>{I18n.t("course.noPreConfiguredSchachHomeOrganizationWarning")}</p>

                </div>
            {/if}
            <div class="options">
                <Button className="cancel" label={I18n.t("course.cancel")} onClick={cancel}/>

                <Button label={I18n.t("course.register")} disabled={!schacHomeInstitution}
                        onClick={registerCourse(true)}/>
            </div>
        </div>

    </div>
{:else}
    <Spinner/>
{/if}

{#if showModal}
    <Modal submit={registerCourse(false)}
           cancel={() => showModal = false}
           title={I18n.t("course.register")}
                   question={I18n.t("course.registerConfirmation", {name: course.name, institution: institution.name})}>
        {#if schacHomeInstitutions.length > 1}
            <p class="confirmation">{I18n.t("course.chooseSchacHome")}</p>
            <p class="confirmation">{I18n.t("course.chooseSchacHomeInfo")}</p>
            <div class="home-institutions">
                {#each schacHomeInstitutions as schacHome}
                    <label>
                        <input type=radio bind:group={schacHomeInstitution} value={schacHome}>
                        {schacHome}
                    </label>
                {/each}
            </div>
        {/if}
    </Modal>

{/if}
