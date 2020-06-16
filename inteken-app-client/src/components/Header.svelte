<script>

    import I18n from "i18n-js";
    import logo from "../img/student-clipart.png";
    import {navigate} from "svelte-routing";
    import {onMount} from "svelte";
    import {logout} from "../api";
    import {user, config} from "../stores/user";
    import Button from "./Button.svelte";

    onMount(() => {
        const urlSearchParams = new URLSearchParams(window.location.search);
        if (urlSearchParams.has("login")) {
            me().then(json => {
                for (var key in json) {
                    if (json.hasOwnProperty(key)) {
                        $user[key] = json[key];
                    }
                }
                $user.guest = false;
            });
        }
    });

    const logoutUser = () => {
        logout().then(() => {
            $user = {guest: true};
        });
    }

    const loginUser = () => {
        const urlSearchParams = new URLSearchParams(window.location.search);
        urlSearchParams.set("login", "true");
        const location = `${window.location.pathname}?${urlSearchParams.toString()}`;
        window.location.href = $config.server_login + `?location=${encodeURIComponent(location)}`;
    }

</script>

<style>

    .header {
        width: 100%;
        max-width: var(--width-app);
        margin: 0 auto;
        display: flex;
        background-color: var(--color-primary-grey);
        align-items: center;
        align-content: center;
        position: relative;
        flex-direction: row;
        color: #94d6ff;
        border-left: 28px solid var(--color-primary-grey);

    }

    .logo {
        padding: 10px 0;
        max-width: 340px;
    }

    h1 {
        color: var(--color-primary-green);
        margin-left: 25px;
    }

    @media (max-width: 800px) {
        h1 {
            display: none;
        }
    }

    div.logout, div.login {
        margin: 0 25px 0 auto;
    }

</style>
<div class="header">

    <div class="logo">
        <a href="/"><img src={logo} alt=""></a>
    </div>
    <h1>{I18n.t("header.title")}</h1>
    {#if !$user.guest}
        <div class="logout">
            <Button href="/logout" label={I18n.t("header.logout")} onClick={logoutUser} className="cancel small"/>
        </div>
    {:else}
        <div class="login">
            <Button href="/login" label={I18n.t("header.login")} onClick={loginUser} className="cancel small"/>
        </div>
    {/if}
</div>
