<script>
    import I18n from "i18n-js";
    import Button from "./Button.svelte";
    import lightBulb from "../icons/light.svg?raw";
    import close from "../icons/close.svg?raw";
    import {postPollResponse} from "../api/index.js";
    import PollButton from "./PollButton.svelte";

    export let visible;
    export let name;
    export let cancel;
    export let finish;

    let poll;
    let motivation;
    let showDetails;
    let done = false;

    const doSendPoll = () => {
        postPollResponse({poll, name, motivation});
        done = true;
        setTimeout(() => finish(), 3750);
    };

</script>

<style lang="scss">
    /* Center the container */
    .poll-container {
        display: flex;
        flex-direction: column;
        background-color: #fdfae6;
        padding: 25px;

        position: absolute;
        top: 0;
        right: -75vw;
        transition: right 1s linear;
        opacity: 0;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

        &.visible {
            right: 0; /* Move to the center of the screen */
            opacity: 1;
        }

        .poll-header {
            display: flex;
            width: 100%;
            align-items: center;
            margin-bottom: 15px;

            :global(svg:first-child) {
                margin-right: 20px;
            }

            span.close {
                margin-left: auto;
                cursor: pointer;
            }
        }

        .poll-options {
            display: flex;
            width: 100%;
            gap: 20px;
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
    }
</style>

<div class="poll-container" class:visible={visible}>

    <div class="poll-header">
        {@html lightBulb}
        <h3>{I18n.t("poll.registerQuestion")}</h3>
        <span class="close" on:click={() => cancel()}>
            {@html close}
        </span>
    </div>

    <div class="poll-options">
        {#each Object.keys(I18n.translations[I18n.locale].poll.scores) as score}
            <PollButton label={I18n.translations[I18n.locale].poll.scores[score]}
                        active={poll === score}
                        onClick={() => poll = score}/>
        {/each}
    </div>

    <div class="actions">
        <Button className="cancel"
                onClick={cancel}
                label={I18n.t("poll.submit")}/>
        <Button cancel={true}
                onClick={cancel}
                label={I18n.t("poll.submitAppointment")}/>
    </div>
</div>
