<script>
  import I18n from "i18n-js";
  import calendar from "../icons/icons-studmob/calendar-1.svg";
  import places from "../icons/icons-studmob/human-resources-offer-employee.svg";
  import launches from "../icons/icons-studmob/startup-launch.svg";
  import pin from "../icons/icons-studmob/pin.svg";
  import ects from "../icons/icons-studmob/school-book-trophy.svg";
  import lang from "../icons/icons-studmob/messages-bubble-square-text.svg";
  import {offering} from "../stores/offering";

  const formatOptions = {weekday: "long", year: "numeric", month: "long", day: "numeric"};

</script>

<style lang="scss">


  .course {
    min-width: 50%;
    max-width: 50%;
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
          padding-left: 0;
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
{/if}