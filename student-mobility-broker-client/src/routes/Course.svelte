<script>
    import {user, config, flash} from "../stores/user";
    import I18n from "i18n-js";
    import {register, courseByIdentifier, institutionBySchacHome, institutionSchacHomes, me} from "../api";
    import {navigate} from "svelte-routing";
    import logoEduid from "../img/logo_eduID.svg";
    import Button from "../components/Button.svelte";
    import {onMount} from "svelte";
    import Modal from "../components/Modal.svelte";
    import Spinner from "../components/Spinner.svelte";
    import Flash from "../components/Flash.svelte";

    const offering = "http://offering";
    const person = "http://person";
    const scope = "profile";
    const returnTo = "http://returnTo";
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
    <div class="course">
        <form action="http://localhost:8092/enrollment" method="post">
            <label for="offering">Offering</label>
            <input value={offering} name="offering" id="offering"/>

            <label for="person">Person</label>
            <input value={person} name="person" id="person"/>

            <label for="scope">Scope</label>
            <input value={scope} name="scope" id="scope"/>

            <label for="returnTo">ReturnTO</label>
            <input value={returnTo} name="returnTo" id="returnTo"/>

            <button type="submit"><span>{@html logoEduid}Submit</span></button>
        </form>
    </div>
