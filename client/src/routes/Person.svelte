<script>
    import {offering} from "../stores/offering";
    import {me} from "../api";
    import {onMount} from "svelte";
    import Loading from "../components/Loading.svelte";
    import {prettyPrintJson} from "pretty-print-json";
    import "../stylesheets/pretty-print-json.min.css";
    import DOMPurify from "dompurify";

    let loading = true;
    let personHtml = {};

    onMount(() => {
        if ($offering.correlationID && $offering.correlationID !== "1") {
            me($offering.correlationID).then(res => {
                personHtml = prettyPrintJson.toHtml(res);
                loading = false;
            })
        } else {
            loading = false;

        }
    });

</script>

<style lang="scss">
    .page {
        display: flex;
        flex-direction: column;
    }

    h4 {
        margin: 25px 0 10px 0;
        font-size: 22px;
    }

    p {
        margin-top: 25px;
        margin-bottom: 2px;
        font-weight: 600;

        &.note {
            font-weight: normal;
        }
    }


</style>
<div class="page">
    <h4>Person information</h4>
    {#if loading}
        <Loading/>
    {:else if !$offering.correlationID || $offering.correlationID === "1"}
        <p class="note">In order to fetch the person data from the home institution you need to first
            login and successfully enroll.</p>
    {:else }

        <pre class=json-container>{@html DOMPurify.sanitize(personHtml)}</pre>
    {/if}

</div>
