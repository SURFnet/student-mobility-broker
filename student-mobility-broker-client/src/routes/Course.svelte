<script>
    import {user, config, flash} from "../stores/config";
    import I18n from "i18n-js";
    import {register, courseByIdentifier, institutionBySchacHome, institutionSchacHomes, me} from "../api";
    import {navigate} from "svelte-routing";
    import logoEduid from "../img/logo_eduID.svg";
    import Button from "../components/Button.svelte";
    import {onMount} from "svelte";
    import Modal from "../components/Modal.svelte";
    import Spinner from "../components/Spinner.svelte";
    import Flash from "../components/Flash.svelte";
    import { LottiePlayer } from '@lottiefiles/svelte-lottie-player';
    import scooter from "../lotties/lf20_Fyn2dD.json";
    import {offering} from "../stores/offering";
    const offeringURI = "http://offering";
    const personURI = "http://person";
    const scope = "profile";
    let loaded = false;
    let showModal = false;


    onMount(() => {
    });

    const startRegistration = () => {
    }

    const cancel = () => navigate("/courses");

</script>

<style>
    div.course form {
        display: flex;
        flex-direction: column;
        padding: 25px;
    }

    button {
        background-color: #4CAF50; /* Green */
        border: none;
        color: white;
        padding: 15px 32px;
        text-align: center;
        text-decoration: none;
        display: inline-block;
        font-size: 16px;
    }

</style>
<div>
    {JSON.stringify($offering.offering)}
</div>
<LottiePlayer
        src={scooter}
        autoplay="{true}"
        loop="{true}"
        controls="{false}"
        renderer="svg"
        background="transparent"
        height="{600}"
        width="{600}"
        controlsLayout={null}
/>
