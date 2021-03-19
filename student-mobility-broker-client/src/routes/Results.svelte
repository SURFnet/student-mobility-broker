<script>
  import {offering} from "../stores/offering";
  import pressPlay from "../icons/icons-studmob/undraw_press_play_bx2d.svg";
  import Select from 'svelte-select';
  import {mimicResultsFromSIS} from "../api";

  export let institutions = [];

  const responses = [
    {value: 200, label: "ECTS 6 - Top notch"},
    {value: 500, label: "ECTS 1 - Damn"}
  ];

  let finished = false;
  let response = responses[0];

  const results = () => {
    mimicResultsFromSIS($offering.correlationID, response.label)
      .then(() => {
        finished = true;
        $offering.correlationID = null;
        setTimeout(() => finished = false, 7500);
      });
  }

  const handleSelect = val => response = val.detail;

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
    font-weight: bold;
    margin-top: 25px;
    margin-bottom: 2px;
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

  .play {
    display: flex;

    :global(svg) {
      max-width: 150px;
      max-height: 150px;
      margin: 25px 0 50px auto;
      cursor: pointer;

    }
  }

  :global(.selectContainer.disabled) {
    color: black !important;
  }


</style>
<div class="page">
    <h4>Mock an enrollment result</h4>
    {#if finished}
        <p>Check your mailbox for a result email.</p>
    {:else}
        {#if !$offering.correlationID || $offering.correlationID === "1"}
            <p>In order to mimic the POST results back to the intake-ontvanger-generiek you need to first login and
                successfully enroll.</p>
        {:else}

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
            <Select items={responses} isSearchable={false} showIndicator={true} isClearable={false}
                    selectedValue={response} on:select={handleSelect}/>

            <p>Correlation ID used to mimic the POST results back to intake-ontvanger-generiek and subsequently the home
                institution</p>
            <span>{$offering.correlationID}</span>

            <div class="play" on:click={results}>
                {@html pressPlay}
            </div>
        {/if}
    {/if}
</div>
