<script>
    import {user, config, redirectPath} from "../stores/user";
    import {onMount} from "svelte";
    import I18n from "i18n-js";
    import {navigate} from "svelte-routing";
    import Button from "../components/Button.svelte";

    let isLogoutRedirect = false;

    onMount(() => {
        const urlSearchParams = new URLSearchParams(window.location.search);
        isLogoutRedirect = urlSearchParams.get("logout");
    });

    const login = () => {
        window.location.href = $config.server_login + "?location=" + $redirectPath;
    }

</script>

<style>
    .login {
        display: flex;
        flex-direction: column;
        background-color: white;
        height: auto;
        min-height: 500px;
        align-items: center;
        align-content: center;
    }

    div.inner {
        margin: 25px auto;
    }

    h3 {
        color: var(--color-primary-green);
        margin-bottom: 20px;
    }

    p {
        color: var(--color-primary-black);
        font-size: 18px;
        margin: 25px 0;
    }

    div.options {
        margin-top: 75px;
    }

</style>


<div class="login">
    <div class="inner">
        {#if isLogoutRedirect}
            <h3>{I18n.t("login.logoutTitle")}</h3>
            <p>{I18n.t("login.logoutStatus")}</p>
        {:else}
            <p>{I18n.t("login.info")}</p>
            <p>{I18n.t("login.process")}</p>
        {/if}
        <div class="options">
        <Button href="/login" label={I18n.t("login.login")} onClick={login}/>
        </div>
    </div>
</div>
