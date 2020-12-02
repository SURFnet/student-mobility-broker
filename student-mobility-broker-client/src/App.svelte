<script>
  import {navigate, Route, Router} from "svelte-routing";
  import {onMount} from "svelte";
  import Cookies from "js-cookie";
  import NotFound from "./routes/NotFound.svelte";
  import Offering from "./routes/Offering.svelte";
  import PlayGround from "./routes/PlayGround.svelte";
  import {broker, features, selectedOffering} from "./api";
  import {offering} from "./stores/offering";
  import {config} from "./stores/config";
  import I18n from "i18n-js";
  import {getParameterByName} from "./utils/queryParameters";
  import data from "./data/offering.json";

  export let url = "";
  let loaded = false;

  if (typeof window !== "undefined") {
    const urlSearchParams = new URLSearchParams(window.location.search);
    if (urlSearchParams.has("lang")) {
      I18n.locale = urlSearchParams.get("lang");
    } else if (Cookies.get("lang")) {
      I18n.locale = Cookies.get("lang");
    } else {
      I18n.locale = navigator.language.toLowerCase().substring(0, 2);
    }
  } else {
    I18n.locale = "en";
  }
  if (["nl", "en"].indexOf(I18n.locale) < 0) {
    I18n.locale = "en";
  }


  onMount(() => {
    features().then(json => {
      $config = json;
      const step = getParameterByName("step");
      const playGround = window.location.pathname.indexOf("play") > -1;
      if (json.local && !step && !playGround && json.allowPlayground) {
        //Mock the call from catalog to broker to ensure there is a selected offering
        broker("utrecht.nl", "eindhoven.nl", "1");
      } else if (!playGround) {
        selectedOffering().then(json => {
          $offering = json;
          loaded = true;
        });
      } else if (playGround && json.allowPlayground) {
        $offering = data;
        loaded = true;
      } else {
        loaded = true;
      }
    });
  });

</script>

<style global lang="scss">
  @import "stylesheets/main";

  .broker {
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;
  }

  .loader:empty,
  .loader:empty:after {
    border-radius: 50%;
    width: 10em;
    height: 10em;
  }

  .loader:empty {
    margin: 60px auto;
    font-size: 10px;
    position: relative;
    text-indent: -9999em;
    border: 1.1em solid white;
    border-top-color: var(--color-primary-blue);
    border-bottom-color: var(--color-primary-blue);
    transform: translateZ(0);
    animation: load8 1.5s infinite linear;
  }

  @-webkit-keyframes load8 {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

  @keyframes load8 {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>
{#if loaded}
    <div class="broker">
        <Router url="{url}">
            <Route path="/" component={Offering}/>
            {#if $config.allowPlayground}
                <Route path="/play" component={PlayGround}/>
            {/if}
            <Route component={NotFound}/>
        </Router>
    </div>
{:else}
    <div class="loader"></div>
{/if}
