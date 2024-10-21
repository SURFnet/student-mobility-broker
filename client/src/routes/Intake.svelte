<script>
    import {offering} from "../stores/offering";
    import {playground} from "../stores/playground";
    import pressPlay from "../icons/icons-studmob/undraw_press_play_bx2d.svg";
    import Select from 'svelte-select';
    import {broker} from "../api";
    import {config} from "../stores/config";

    export let institutions = [];

  const responses = [
      {value: 200, label: "200 - All is good"},
      {value: 400, label: "400 - Backend error"},
      {value: 404, label: "404 - Person endpoint not found"},
      {value: 409, label: "409 - Queue-session validation failed"},
      {value: 412, label: "412 - Invalid enrollmentRequest"},
      {value: 417, label: "417 - Token request failed"},
      {value: 419, label: "419 - eduID not present in the ARP"},
      {value: 422, label: "422 - Administrative error (already enrolled)"},
      {value: 500, label: "500 - Not so good"}
  ];

  let redirect;
  let message;
  let response = responses[0];
  const start = () => {
    let code = response.value;
    playground.start(code, code === 200 ? redirect : null, code === 500 ? message : null);
    broker($offering.homeInstitution.schacHome,
           $offering.guestInstitution.schacHome,
           $config.offeringId,
           "course",
           $config.startBrokerEndpoint + "?play=true");
  }

  const handleSelect = val => response = val.detail;

  const handleSelectGuestInstitution = val => {
    $offering.guestInstitution = val.detail;
  }

  const handleSelectHomeInstitution = val => {
    $offering.homeInstitution = val.detail;
  }

  const offeringIdChanged = e => {
    $config.offeringId = e.target.value;
  }

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

  input {
    border: 1px solid #d8dbdf;
    border-radius: 3px;
    height: 42px;
    padding: 0 16px;
    font-size: 16px;
    line-height: 22px;

      &:disabled {
          cursor: not-allowed;
      }
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


</style>
<div class="page">
    <h4>Mock an enrollment response</h4>
    <div class="institution">
        <div class="institution-detail">
            <p>Home institution</p>
            <span class="info">Institution where the user has registered</span>
            <Select items={institutions}
                    isSearchable={false}
                    showIndicator={true}
                    isClearable={false}
                    selectedValue={$offering.homeInstitution}
                    on:select={handleSelectHomeInstitution}/>

            <!--                <span>{$offering.homeInstitution.name}</span>-->
        </div>
        {#if $offering.guestInstitution.logoURI}
            <img src={$offering.homeInstitution.logoURI} alt=""/>
        {/if}
    </div>
    <div class="institution">
        <div class="institution-detail">
            <p>Guest institution</p>
            <span class="info">Institution where the user want to enrol for a  course</span>
            <Select items={institutions}
                    isSearchable={false}
                    showIndicator={true}
                    isClearable={false}
                    selectedValue={$offering.guestInstitution}
                    on:select={handleSelectGuestInstitution}/>


            <!--                <span>{$offering.guestInstitution.name}</span>-->
        </div>
        {#if $offering.guestInstitution.logoURI}
            <img src={$offering.guestInstitution.logoURI} alt=""/>
        {/if}
    </div>
    <p>Offering ID</p>
    <span class="info">The unique identifier of the cours / offering</span>
    <input bind:value={$config.offeringId} on:change={offeringIdChanged}/>

    <p>Response code</p>
    <span class="info">Choose the response code from the server</span>
    <Select items={responses} isSearchable={false} showIndicator={true} isClearable={false}
            selectedValue={response} on:select={handleSelect}/>

    <p>Redirect</p>
    <span class="info">Optional and only for a 200 response</span>
    <input bind:value={redirect} disabled={response.value !== 200} on:keyup={e=>e.key==="Enter" && start()}/>

    <p>Message</p>
    <span class="info">For non 200 responses (e.g. 4xx or 5xx)</span>
    <input bind:value={message} disabled={response.value === 200} on:keyup={e=>e.key==="Enter" && start()}/>
    <div class="play" on:click={start}>
        {@html pressPlay}
    </div>
</div>
