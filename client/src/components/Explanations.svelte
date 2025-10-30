<script>
    import I18n from "i18n-js";
    import transferGrey from "../icons/data-transfer-square-horizontal.svg?raw";
    import enrollGrey from "../icons/icons-studmob/official-building-3-grey.svg?raw";
    import proceedGrey from "../icons/access.svg?raw";
    import hand from "../icons/icons-studmob/noun_Up hand drawn arrow_1563367.svg?raw";
    import DOMPurify from "isomorphic-dompurify";
    import {config,} from "../stores/config";
    import {offering} from "../stores/offering";

    const isEUInstance = $config.brokerInstance === "euroteq";
    const explanations = [
        {
            name: isEUInstance ? "myAcademicID" : "eduID",
            icon: proceedGrey
        },
        {name: "transfer", icon: transferGrey},
        {name: "enrollment", icon: enrollGrey}
    ];

</script>

<style lang="scss">

    .explanation-container {
        width: 100%;

        h2 {
            margin: 40px 0;
        }

        .explanations {
            max-width: 913px;
            margin: auto;
            display: flex;
            flex-direction: column;
            position: relative;

            @media (max-width: 1000px) {
                margin-left: 60px;
                span.hand {
                    display: none;
                }
            }

            span.hand {
                position: absolute;
                left: 340px;
                top: -40px;
            }

            h2 {
                margin-left: -40px;
                color: var(--color-tertiary-grey);
            }

            p {
                color: var(--color-tertiary-grey);

                &.title {
                    font-weight: 500;
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
                margin-bottom: 30px;

                @media (max-width: 1000px) {
                    max-width: 420px;
                }

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
                        display: flex;

                        :global(svg) {
                            width: 45px;
                            height: auto;
                            margin: auto;
                            fill: #807c7c;
                        }
                    }
                }
            }

        }

    }
</style>

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
                        <p>{@html DOMPurify.sanitize(I18n.t(`explanation.${name}.subTitle`,
                            {abbreviation: $offering.guestInstitution.abbreviation}))}</p>
                    </div>
                </div>
            {/each}
        </div>
    </div>
</div>
