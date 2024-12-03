<script>
  import I18n from "i18n-js";
  import transferGrey from "../icons/icons-studmob/data-transfer-check-grey.svg?raw";
  import enrollGrey from "../icons/icons-studmob/official-building-3-grey.svg?raw";
  import eduIDGrey from "../icons/logo_eduID_grey.svg?raw";
  import relaxGrey from "../icons/icons-studmob/cocktail-glass-grey.svg?raw";
  import {offering} from "../stores/offering";
  import hand from "../icons/icons-studmob/noun_Up hand drawn arrow_1563367.svg?raw";
  import DOMPurify from "dompurify";

  const explanations = [
    {name: "eduID", icon: eduIDGrey},
    {name: "transfer", icon: transferGrey},
    {name: "enrollment", icon: enrollGrey},
    {name: "relax", icon: relaxGrey},
  ];

</script>

<style lang="scss">

  .explanation-container {
    background-color: var(--color-grey-background);
    width: 100%;

    h2 {
      margin: 40px 0;
    }

    .explanations {
      max-width: 480px;
      margin: auto;
      display: flex;
      flex-direction: column;
      position: relative;

      @media (max-width: 780px) {
        margin-left: 50px;
        span.hand {
          display: none;
        }
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
          font-weight: 600;
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

        @media (max-width: 780px) {
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

            :global(svg) {
              width: 45px;
              height: auto;
            }
          }

          span.eduID {
            :global(svg) {
              width: 60px;
              height: auto;
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
                        <p>{@html DOMPurify.sanitize(I18n.t(`explanation.${name}.subTitle`, {abbreviation: $offering.guestInstitution.abbreviation}))}</p>
                    </div>
                </div>
            {/each}
        </div>
    </div>
</div>
