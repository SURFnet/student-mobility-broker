<script>
    import {offering} from "../stores/offering";
    import Select from 'svelte-select';
    import {postResultsPlayground} from "../api";
    import Button from "../components/Button.svelte";
    import Loading from "../components/Loading.svelte";

    export let institutions = [];

    const responses = [
        {value: 200, label: "ECTS 6 - Top notch"},
        {value: 500, label: "ECTS 1 - Damn"}
    ];
    let response = responses[0];

    let finished = false;
    let loading = false;

    const postResults = () => {
        loading = true;
        const results = {
            result: {
                "state": "completed",
                "score": response.label
            }
        };
        postResultsPlayground($offering.correlationID, results)
            .then(() => {
                finished = true;
                loading = false;
                setTimeout(() => finished = false, 7500 * 2);
            });
    }

    const handleSelectResult = val => response = val.detail;

</script>

<style lang="scss">
    .page {
        display: flex;
        flex-direction: column;
    }

    h4 {
        margin-top: 25px;
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

    span.info {
        font-size: 14px;
        margin-bottom: 5px;
    }

    .institution {
        display: flex;

        .institution-detail {
            flex-grow: 2;
        }

        img {
            width: 110px;
            margin: auto 0 0 25px;
        }

    }

    div.actions {
        display: flex;
        margin-top: 35px;

        :global(.button) {
            max-width: 200px;
        }

        :global(.button:first-child) {
            margin-left: auto;
        }

        :global(.button:last-child) {
            margin-left: 20px;
        }
    }

    :global(.selectContainer.disabled) {
        color: black !important;
    }


</style>
<div class="page">
    {#if loading}
        <Loading/>
    {:else}
        <h4>Mock an enrollment result</h4>
        {#if !$offering.correlationID || $offering.correlationID === "1"}
            <p class="note">In order to mimic the POST results back (v1) to the intake-ontvanger-generiek you need to
                first
                login and successfully enroll.</p>
        {:else}
            {#if finished}
                <p>Check your mailbox for a association email.</p>
            {/if}
            <div class="institution">
                <div class="institution-detail">
                    <p>Home institution</p>
                    <span class="info">Institution where the user has registered</span>
                    <Select items={institutions}
                            isSearchable={false}
                            showIndicator={true}
                            isClearable={false}
                            selectedValue={$offering.homeInstitution}
                            isDisabled={true}/>
                </div>
                <img src={$offering.homeInstitution.logoURI} alt=""/>
            </div>
            <div class="institution">
                <div class="institution-detail">
                    <p>Guest institution</p>
                    <span class="info">Institution where the user has enrolled for a  course</span>
                    <Select items={institutions}
                            isSearchable={false}
                            showIndicator={true}
                            isClearable={false}
                            selectedValue={$offering.guestInstitution}
                            isDisabled={true}/>
                </div>
                <img src={$offering.guestInstitution.logoURI} alt=""/>
            </div>
            <p>Results</p>
            <span class="info">It's either very good or very bad</span>
            <Select items={responses}
                    isSearchable={false}
                    showIndicator={true}
                    isClearable={false}
                    selectedValue={response}
                    on:select={handleSelectResult}/>
            <p>Correlation ID used to mimic the POST results back to intake-ontvanger-generiek and subsequently the
                home
                institution:</p>
            <span>{$offering.correlationID}</span>
            <div class="actions">
                <Button onClick={postResults}
                        active={true}
                        disabled={loading}
                        label="Post results"/>
            </div>
        {/if}
    {/if}
</div>
