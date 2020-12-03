<script>
  import {offering} from "../stores/offering";
  import {playground} from "../stores/playground";
  import pressPlay from "../icons/icons-studmob/undraw_press_play_bx2d.svg";
  import {navigate} from "svelte-routing";
  import Select from 'svelte-select';
  import {onMount} from "svelte";
  import data from "../data/offering.json";

  const responses = [
    {value: 200, label: "200 - All is good"},
    {value: 500, label: "500 - Not so good"}
  ];

  let redirect;
  let message;
  let response = responses[0];

  onMount(() => {
    $offering = data;
    playground.reset();
  });

  const start = () => {
    let code = response.value;
    playground.start(code, code === 200 ? redirect : null, code === 500 ? message : null);
    navigate(`/?step=enroll&name=${encodeURIComponent("John Doe")}&correlationID=1`);
  }

  const handleSelect = val => response = val.detail;

</script>

<style lang="scss">

  .container {
    max-width: 720px;
    margin: 40px auto;
    width: 100%;
    display: flex;
    flex-direction: column;

    @media (max-width: 780px) {
      padding: 0 20px;
    }


    h1 {
      margin-bottom: 40px;
    }

    h4 {
      font-weight: bold;
      font-size: 18px;
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
    }

    .institution {
      display: flex;
      align-items: center;
      align-content: center;

      img {
        width: 110px;
        margin-left: auto;
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
  }


</style>
<div class="container">
    <h1>Mock an enrollment response</h1>
    <div class="institution">
        <div>
            <h4>Home institution</h4>
            <span>{$offering.homeInstitution.name}</span>
        </div>
        <img src={$offering.homeInstitution.logoURI} alt=""/>
    </div>
    <div class="institution">
        <div>
            <h4>Guest institution</h4>
            <span>{$offering.guestInstitution.name}</span>
        </div>
        <img src={$offering.guestInstitution.logoURI} alt=""/>
    </div>
    <p>Response code</p>
    <span class="info">It's either good or bad</span>
    <Select items={responses} isSearchable={false} showIndicator={true} isClearable={false}
            selectedValue={response} on:select={handleSelect}/>

    <p>Redirect</p>
    <span class="info">Optional and only for a 200 response</span>
    <input bind:value={redirect} disabled={response.value === 500} on:keyup={e=>e.key==="Enter" && start()}/>

    <p>Message</p>
    <span class="info">For a 500 response</span>
    <input bind:value={message} disabled={response.value !== 500} on:keyup={e=>e.key==="Enter" && start()}/>
    <div class="play" on:click={start}>
        {@html pressPlay}
    </div>
</div>

