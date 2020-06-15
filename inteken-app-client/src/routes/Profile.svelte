<script>
    import {user} from "../stores/user";
    import I18n from "i18n-js";

</script>

<style>
    .profile {
        width: 100%;
        height: 100%;
    }

    .inner {
        height: 100%;
        margin: 0 auto;
        padding: 15px 30px 15px 0;
        display: flex;
        flex-direction: column;
    }

    h2 {
        margin: 20px 0 10px 0;
        color: var(--color-primary-green);
    }

    p.info {
        font-weight: 300;
        margin-bottom: 26px;
    }

    p.info2 {
        font-size: 22px;
        margin-bottom: 24px;
        font-family: Proxima Nova, sans-serif;
    }

    p {
        font-size: 18px;
        line-height: 1.33;
        letter-spacing: normal;
    }

    table {
        width: 100%;
    }
    td {
        border-bottom: 1px solid var(--color-primary-grey);
    }
    td.attr {
        width: 30%;
        padding: 20px;
    }

    td.value {
        width: 70%;
        font-weight: bold;
        padding-left: 20px;
    }

    td.value span {
        display: block;
        margin: 8px 0;
    }


</style>


<div class="profile">
    <div class="inner">
        <h2>{I18n.t("profile.title")}</h2>
        <p class="info">{I18n.t("profile.info")}</p>
        <p class="info2">{I18n.t("profile.profile")}</p>
        <table cellspacing="0">
            <thead></thead>
            <tbody>
            <tr>
                <td class="attr">{I18n.t("profile.name")}</td>
                <td class="value">{$user.name}</td>
            </tr>
            <tr>
                <td class="attr">{I18n.t("profile.email")}</td>
                <td class="value">{$user.email}</td>
            </tr>
            <tr>
                <td class="attr">{I18n.t("profile.eduperson_scoped_affiliation")}</td>
                <td class="value">
                    {#each $user.eduperson_scoped_affiliation as affiliation}
                        <span>{`${affiliation}`}</span>
                    {/each}
                </td>
            </tr>
            <tr>
                <td class="attr">{I18n.t("profile.eduid")}</td>
                <td class="value">{$user.eduid}</td>
            </tr>
            </tbody>
        </table>
    </div>

</div>