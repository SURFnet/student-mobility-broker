<script>
    import I18n from "i18n-js";
    import Button from "./Button.svelte";
    import lightBulb from "../icons/light.svg?raw";
    import close from "../icons/close.svg?raw";
    import {postPollResponse} from "../api/index.js";
    import PollButton from "./PollButton.svelte";
    import {config} from "../stores/config";

    export let visible;
    export let name;
    export let crossInstitutionRequest = false;

    let poll;
    let motivation;
    let done = false;
    let hidden = false;
    let makeAppointment = false;

    const doSendPoll = (showAppointment) => {
        makeAppointment = showAppointment;
        postPollResponse({poll, name, motivation});
        if (showAppointment) {
            setTimeout(() => window.open($config.pollSurvey, "_blank").focus(), 650);
        }
        done = true;
    };

    const init = el => {
        el.focus();
    }
</script>

<style lang="scss">
    /* Center the container */
    .poll-container {
        display: flex;
        flex-direction: column;
        background-color: #fdfae6;
        padding: 25px;
        width: 640px;
        position: absolute;
        top: 85px;
        right: -100vw;
        transition: right 400ms linear;
        box-shadow: 0 4px 4px 0 #00000040;

        &.hidden {
            display: none;
        }

        &.visible {
            right: -100%;
            z-index: 999;
        }

        @media (max-width: 1520px) {
            &.visible {
                right: 0;
            }
        }

        @media (max-width: 870px) {
            &.visible {
                right: 140px;
            }
        }
        @media (max-width: 780px) {
            top: -360px;
            width: 100vw;

            &.visible {
                right: -20px;
            }
        }

        .poll-header {
            display: flex;
            width: 100%;
            align-items: center;
            margin-bottom: 15px;

            &.done {
                margin-bottom: 0;
            }

            :global(svg:first-child) {
                margin-right: 12px;
            }

            span.close {
                margin-left: auto;
                cursor: pointer;
            }
        }

        p.missing-out {
            margin-top: 20px;
        }

        .poll-options {
            display: flex;
            width: 100%;
            gap: 15px;

            @media (max-width: 780px) {
                flex-direction: column;
            }

        }


        @keyframes slide-in {
            from {
                top: 0;
                left: -100;
            }
            to {
                top: 0;
                left: 100px;
            }
        }

        div.motivation {
            width: 100%;
            display: flex;
            flex-direction: column;
            margin-top: 25px;

            p.top {
                font-weight: 600;
                margin-bottom: 10px;
            }

            textarea {
                height: 140px;
                padding: 8px;
                border-radius: 6px;
                font-size: 16px;
                font-family: "Source Sans Pro", sans-serif;
                border: 1px solid var(--color-tertiary-grey);
            }

            p.voucher {
                margin: 20px 0;
                font-size: 18px;
            }
        }

        div.actions {
            display: flex;
            gap: 15px;

            &.reverse {
                flex-direction: row-reverse;
            }
        }
    }
</style>

<div class="poll-container" class:visible class:hidden>

    <div class="poll-header" class:done>
        {@html lightBulb}
        <h3>{I18n.t(`poll.${done ? "thanksFeedback" : "registerQuestion"}`)}</h3>
        <span class="close" on:click={() => hidden = true}>
            {@html close}
        </span>
    </div>
    {#if done && !makeAppointment && crossInstitutionRequest}
        <p class="missing-out"> {@html I18n.t("poll.missingOut", {href: $config.pollSurvey})}</p>
    {/if}
    {#if !done}
        <div class="poll-options">
            {#each Object.keys(I18n.translations[I18n.locale].poll.scores) as score}
                <PollButton label={I18n.translations[I18n.locale].poll.scores[score]}
                            active={poll === score}
                            onClick={() => poll = score}/>
            {/each}
        </div>
        {#if poll}
            <div class="motivation">
                <p class="top">{I18n.t("poll.why")}</p>
                <textarea bind:value={motivation} use:init></textarea>
                <p class="voucher">{@html crossInstitutionRequest ? I18n.t("poll.join") : ""}</p>
            </div>
        {/if}
        {#if poll}
            <div class="actions" class:reverse={!crossInstitutionRequest}>
                <Button onClick={() => doSendPoll()}
                        cancel={true}
                        label={I18n.t("poll.submit")}/>
                {#if crossInstitutionRequest}
                    <Button
                            onClick={() => doSendPoll(true)}
                            label={I18n.t("poll.submitAppointment")}/>
                {/if}
            </div>
        {/if}
    {/if}
</div>
