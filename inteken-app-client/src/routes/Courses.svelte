<script>
    import {user} from "../stores/user";
    import {onMount} from "svelte";
    import I18n from "i18n-js";
    import {navigate} from "svelte-routing";
    import Button from "../components/Button.svelte";

    let courses = [];

    onMount(() => {

    });


</script>

<style>
    .courses {
        display: flex;
        justify-content: center;
        flex-direction: column;
        background-color: white;
        height: auto;
        min-height: 500px;
        padding: 25px;
        align-items: center;
        align-content: center;
    }


    h1 {
        margin-bottom: 35px;
        text-align: center;
    }

    h2 {
        margin-bottom: 60px;
        color: var(--color-primary-green);
        text-align: center;
    }


</style>


<div class="courses">
    {JSON.stringify(courses, null, 4)}

</div>
