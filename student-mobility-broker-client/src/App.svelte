<script>
  import {Route, Router} from "svelte-routing";
  import {onMount} from "svelte";
  import Cookies from "js-cookie";
  import NotFound from "./routes/NotFound.svelte";
  import Offering from "./routes/Offering.svelte";
  import {broker, features, selectedOffering} from "./api";
  import {offering} from "./stores/offering";
  import I18n from "i18n-js";
  import {getParameterByName} from "./utils/queryParameters";

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
      const step = getParameterByName("step");
      if (json.local && !step) {
        //Mock the call from catalog to broker to ensure there is a selected offering
        broker("utrecht.nl", "eindhoven.nl", "1");
      } else {
        selectedOffering().then(json => {
          $offering = json;
          loaded = true;
        });
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
    border-top-color: var(--color-primary-green);
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
            <Route component={NotFound}/>
        </Router>
    </div>
{:else}
    <div class="loader"></div>
{/if}
