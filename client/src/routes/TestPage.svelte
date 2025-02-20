<script>
    import I18n from "i18n-js";
    import DOMPurify from "isomorphic-dompurify";
    import highFive from "../icons/icons-studmob/undraw_High_five.svg?raw";
    import lightBulb from "../icons/icons-studmob/Lightbulb.svg?raw";
    import Button from "../components/Button.svelte";
    import {config} from "../stores/config";
    import Poll from "../components/Poll.svelte";

    let result = {message: "Ok"};
    let start = false;

</script>

<style lang="scss">

    .container {

        .result {
            display: flex;
            flex-direction: column;
            position: relative;
        }

        max-width: 820px;
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
            :global(div.lottie-container div) {
                width: 100%;
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
    }

    @media (max-width: 780px) {
        h3 {
            font-size: 32px;
            line-height: 34px;
            font-weight: 600;
        }
    }

    div.icons {
        display: flex;
        justify-content: space-between;
        position: absolute;
        top: -39px;
        left: 1px;
        width: 100%;

        @media (max-width: 780px) {
            top: -30px;
        }

        div.icon-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            z-index: 2;
            background-color: transparent;

            @media (max-width: 780px) {
                max-width: 80px;
                text-align: center;
            }

            p {
                font-weight: 600;
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

            @media (max-width: 780px) {
                width: 75px;
                height: 75px;
            }

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
        @media (max-width: 780px) {
            display: block;
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

    div.landing, div.error {
        display: flex;
        flex-direction: column;

        p {
            margin-bottom: 15px;
        }
    }


    .details {
        display: flex;
        @media (max-width: 780px) {
            flex-direction: column;
        }

        .status {
            margin-left: auto;
            display: flex;
            flex-direction: column;
            min-width: 40%;
            max-width: 40%;

            @media (max-width: 780px) {
                margin-left: 0;
                min-width: 100%;
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

    }


    ul.personals, ul.attributes {
        list-style-type: initial;
        margin: 10px 0 20px 20px;
    }

</style>
<div class="container">
    <div class="offering">
        <div class="header">
            <h2>H2</h2>
            <span>start: {start.toString()}</span>
            <Button label="Start" onClick={() => start = !start}/>
        </div>
        <div class="details">
            <a href="{$config.pollSurvey}">{$config.pollSurvey}</a>
            <div class="result">
                <div class="hero">
                    {@html highFive}
                </div>
                <Poll visible={start}
                      cancel={() => start = !start}
                      name={"test"}/>
                <div class="final-action">
                    {@html lightBulb}
                    {#if result.message}
                        <span class="final-action-msg">{@html DOMPurify.sanitize(result.message)}</span>
                    {:else}
                        <span class="final-action-msg">{DOMPurify.sanitize(I18n.t("offering.receiveMail", {abbreviation: "test"}))}</span>
                    {/if}
                </div>
            </div>
        </div>
    </div>
</div>
